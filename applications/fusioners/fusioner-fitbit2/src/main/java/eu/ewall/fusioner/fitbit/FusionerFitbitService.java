package eu.ewall.fusioner.fitbit;

import java.security.SecureRandom;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.NotFoundException;

import org.slf4j.Logger;

import eu.ewall.fusioner.fitbit.api.FitbitAccessToken;
import eu.ewall.fusioner.fitbit.api.FitbitApi;
import eu.ewall.fusioner.fitbit.api.FitbitAuthCodeException;
import eu.ewall.fusioner.fitbit.exception.BadRequestException;
import eu.ewall.fusioner.fitbit.model.FitbitAuthRequest;
import eu.ewall.fusioner.fitbit.model.FitbitAuthRequestTable;
import eu.ewall.fusioner.fitbit.model.FitbitLink;
import eu.ewall.fusioner.fitbit.model.FitbitLinkTable;
import eu.ewall.fusioner.fitbit.utils.http.URLParameters;
import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseConnection;
import eu.ewall.platform.idss.dao.DatabaseCriteria;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.utils.AppComponents;

public class FusionerFitbitService {
	public static final String LOGTAG = "FusionerFitbit";
	
	public static final int MAX_AUTH_CALLBACK_DELAY = 3600000; // ms
	
	private String mongoDbName;
	
	private String fitbitClientId;
	private String fitbitSecret;
	private String fitbitCallbackUrl;
	
	public FusionerFitbitService(String mongoDbName, String fitbitClientId,
			String fitbitSecret, String fitbitCallbackUrl) {
		this.mongoDbName = mongoDbName;
		this.fitbitClientId = fitbitClientId;
		this.fitbitSecret = fitbitSecret;
		this.fitbitCallbackUrl = fitbitCallbackUrl;
	}
	
	public void close() {
	}
	
	public String authorize(String user, List<IDSSUserProfile> profiles)
			throws Exception {
		IDSSUserProfile profile = findUser(user, profiles);
		if (profile == null)
			throw new NotFoundException("User not found: " + user);
		String stateCode = generateHexKey(128);
		String fitbitBaseUrl = "https://www.fitbit.com/oauth2/authorize";
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("client_id", fitbitClientId);
		params.put("response_type", "code");
		params.put("scope", "activity profile settings sleep");
		params.put("redirect_uri", fitbitCallbackUrl);
		params.put("state", stateCode);
		String url = fitbitBaseUrl + "?" + URLParameters.getParameterString(
				params);
		long now = System.currentTimeMillis();
		FitbitAuthRequest request = new FitbitAuthRequest();
		request.setUser(user);
		request.setStateCode(stateCode);
		request.setRequestTime(now);
		DatabaseConnection dbConn = DatabaseLoader.connect();
		try {
			Database db = DatabaseLoader.initDatabase(dbConn, mongoDbName);
			db.insert(FitbitAuthRequestTable.NAME, request);
		} finally {
			dbConn.close();
		}
		return url;
	}

	/**
	 * Generates a hexadecimal string key of the specified number of bits.
	 *
	 * @param bits the number of bits (multiple of 8)
	 * @return the hexadecimal string key
	 */
	private String generateHexKey(int bits) {
		StringBuilder builder = new StringBuilder();
		byte[] bs = new byte[bits / 8];
		SecureRandom random = new SecureRandom();
		random.nextBytes(bs);
		for (byte b : bs) {
			builder.append(String.format("%02x", b & 0xff));
		}
		return builder.toString();
	}
	
	public String authorizeCallback(String state, String code, String error,
			String errorDescription) throws Exception {
		Logger logger = AppComponents.getLogger(FusionerFitbitService.LOGTAG);
		if (error != null && error.length() != 0) {
			String logMsg = "Fitbit authorization error: " + error;
			if (errorDescription != null && errorDescription.length() != 0)
				logMsg += ": " + errorDescription;
			logger.error(logMsg);
			return logMsg;
		}
		logger.info("Received Fitbit auth code");
		FitbitApi fitbitApi = new FitbitApi(fitbitClientId, fitbitSecret, null,
				null);
		FitbitAccessToken accessToken;
		try {
			accessToken = fitbitApi.getAccessToken(code, fitbitCallbackUrl);
		} catch (FitbitAuthCodeException ex) {
			throw new BadRequestException(ex.getMessage());
		}
		logger.info("Received Fitbit access token");
		DatabaseConnection dbConn = DatabaseLoader.connect();
		try {
			Database db = DatabaseLoader.initDatabase(dbConn, mongoDbName);
			long now = System.currentTimeMillis();
			long minReqTime = now - MAX_AUTH_CALLBACK_DELAY;
			DatabaseCriteria criteria = new DatabaseCriteria.And(
					new DatabaseCriteria.Equal("stateCode", state),
					new DatabaseCriteria.GreaterEqual("requestTime",
							minReqTime)
			);
			FitbitAuthRequest request = db.selectOne(
					new FitbitAuthRequestTable(), criteria, null);
			if (request == null) {
				String logMsg = "Request with state " + state +
						" not found or expired";
				logger.error(logMsg);
				
				return logMsg;
			}
			db.delete(FitbitAuthRequestTable.NAME, request);
			String user = request.getUser();
			criteria = new DatabaseCriteria.Equal("user", user);
			db.delete(FitbitLinkTable.NAME, criteria);
			FitbitLink link = new FitbitLink();
			link.setUser(user);
			link.setAccessToken(accessToken.getAccess_token());
			link.setExpires(now + accessToken.getExpires_in());
			link.setRefreshToken(accessToken.getRefresh_token());
			link.setTokenType(accessToken.getToken_type());
			link.setLinkTime(System.currentTimeMillis());
			db.insert(FitbitLinkTable.NAME, link);
			// TODO start synchronization
			String logMsg = "Authorized Fitbit access for user " + user;
			logger.info(logMsg);
			return logMsg;
		} finally {
			dbConn.close();
		}
	}
	
	public void runTask(List<IDSSUserProfile> users) throws Exception {
		DatabaseConnection dbConn = DatabaseLoader.connect();
		try {
			Database db = DatabaseLoader.initDatabase(dbConn, mongoDbName);
			FusionerFitbitSynchronizer sync = new FusionerFitbitSynchronizer(
					fitbitClientId, fitbitSecret);
			sync.runSync(db, users);
		} finally {
			dbConn.close();
		}
	}
	
	public static IDSSUserProfile findUser(String user,
			List<IDSSUserProfile> profiles) {
		for (IDSSUserProfile profile : profiles) {
			if (profile.getUsername().equals(user))
				return profile;
		}
		return null;
	}
}
