package eu.ewall.fusioner.fitbit.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.ewall.fusioner.fitbit.utils.http.HttpClient;
import eu.ewall.fusioner.fitbit.utils.http.HttpClientException;
import eu.ewall.fusioner.fitbit.utils.validation.MapReader;
import eu.ewall.platform.idss.utils.exception.ParseException;

public class FitbitApi {
	private static final String BASE_URL = "https://api.fitbit.com";
	
	private String clientId;
	private String consumerSecret;
	private String tokenType;
	private String accessToken;
	
	public FitbitApi(String clientId, String consumerSecret, String tokenType,
			String accessToken) {
		this.clientId = clientId;
		this.consumerSecret = consumerSecret;
		this.tokenType = tokenType;
		this.accessToken = accessToken;
	}
	
	public FitbitAccessToken getAccessToken(String authCode,
			String redirectUri) throws FitbitAuthCodeException,
			HttpClientException, IOException {
		HttpClient client = new HttpClient(BASE_URL + "/oauth2/token");
		String httpResult;
		try {
			client.setMethod("POST");
			String token = clientId + ":" + consumerSecret;
			String base64Token = new String(Base64.encodeBase64(token.getBytes()));
			client.addHeader("Authorization", "Basic " + base64Token)
				.writePostParam("code", authCode)
				.writePostParam("grant_type", "authorization_code")
				.writePostParam("client_id", clientId)
				.writePostParam("redirect_uri", redirectUri);
			httpResult = client.readString();
		} catch (HttpClientException ex) {
			if (isAuthCodeException(ex)) {
				throw new FitbitAuthCodeException("Invalid authorization code",
						ex);
			}
			throw ex;
		} finally {
			client.close();
		}
		ObjectMapper mapper = new ObjectMapper();
		FitbitAccessToken token = mapper.readValue(httpResult,
				FitbitAccessToken.class);
		this.tokenType = token.getToken_type();
		this.accessToken = token.getAccess_token();
		return token;
	}
	
	public FitbitAccessToken refreshAccessToken(String refreshToken)
			throws FitbitAccessTokenException, HttpClientException,
			IOException {
		HttpClient client = new HttpClient(BASE_URL + "/oauth2/token");
		String httpResult;
		try {
			client.setMethod("POST");
			String token = clientId + ":" + consumerSecret;
			String base64Token = new String(Base64.encodeBase64(token.getBytes()));
			client.addHeader("Authorization", "Basic " + base64Token)
				.writePostParam("grant_type", "refresh_token")
				.writePostParam("refresh_token", refreshToken);
			httpResult = client.readString();
		} catch (HttpClientException ex) {
			if (isAccessTokenException(ex)) {
				throw new FitbitAccessTokenException("Invalid refresh token",
						ex);
			}
			throw ex;
		} finally {
			client.close();
		}
		ObjectMapper mapper = new ObjectMapper();
		FitbitAccessToken token = mapper.readValue(httpResult,
				FitbitAccessToken.class);
		this.tokenType = token.getToken_type();
		this.accessToken = token.getAccess_token();
		return token;
	}
	
	public FitbitUserProfile getUserProfile()
			throws FitbitAccessTokenException, HttpClientException,
			ParseException, IOException {
		String json = query("/1/user/-/profile.json");
		ObjectMapper mapper = new ObjectMapper();
		Map<?,?> map;
		try {
			map = mapper.readValue(json, Map.class);
		} catch (Exception ex) {
			throw new ParseException("Can't parse JSON map: " +
					ex.getMessage(), ex);
		}
		MapReader reader = new MapReader(map);
		Object submap = reader.readObject("user");
		try {
			return mapper.convertValue(submap, FitbitUserProfile.class);
		} catch (Exception ex) {
			throw new ParseException("Can't parse user profile: " +
					ex.getMessage(), ex);
		}
	}
	
	public List<FitbitDevice> getDevices() throws FitbitAccessTokenException,
			HttpClientException, ParseException, IOException {
		String json = query("/1/user/-/devices.json");
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json,
					new TypeReference<List<FitbitDevice>>() {});
		} catch (Exception ex) {
			throw new ParseException("Can't parse devices: " + ex.getMessage(),
					ex);
		}
	}
	
	public FitbitActivitySummary getActivitySummary(LocalDate date)
			throws FitbitAccessTokenException, HttpClientException,
			ParseException, IOException {
		String path = String.format("/1/user/-/activities/date/%s.json",
				date.toString("yyyy-MM-dd"));
		String json = query(path);
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<?,?> map = mapper.readValue(json, Map.class);
			return mapper.convertValue(map.get("summary"),
					FitbitActivitySummary.class);
		} catch (Exception ex) {
			throw new ParseException("Can't parse activity summary: " +
					ex.getMessage(), ex);
		}
	}
	
	public List<FitbitSleepPeriod> getSleepSummary(LocalDate date)
			throws FitbitAccessTokenException, HttpClientException,
			ParseException, IOException {
		String path = String.format("/1/user/-/sleep/date/%s.json",
				date.toString("yyyy-MM-dd"));
		String json = query(path);
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<?,?> map = mapper.readValue(json, Map.class);
			return mapper.convertValue(map.get("sleep"),
					new TypeReference<List<FitbitSleepPeriod>>() {});
		} catch (Exception ex) {
			throw new ParseException("Can't parse sleep summary: " +
					ex.getMessage(), ex);
		}
	}
	
	/**
	 * Runs a request to get day data for the specified activities/...
	 * resource. Possible resources are specified in {@link
	 * FitbitSensor#ACTIVITY_DAY_RESOURCES FitbitSensor.ACTIVITY_DAY_RESOURCES}
	 * and {@link FitbitSensor#ACTIVITY_INTRADAY_RESOURCES
	 * FitbitSensor.ACTIVITY_INTRADAY_RESOURCES}. If the user's tracker does
	 * not support the specified resource, then this method returns null.
	 * 
	 * <p>Fitbit always returns data until the current time, even if the user
	 * did not sync the tracker with Fitbit yet. This means that any returned
	 * data may change later.</p>
	 * 
	 * @param resource the resource
	 * @param startDate the start date
	 * @param endData the end date (inclusive)
	 * @return the response or null
	 * @throws FitbitAccessTokenException if Fitbit returns an oauth error for
	 * the access token. This may happen because the access token needs to be
	 * refreshed.
	 * @throws HttpClientException if a response other than 200 OK was returned
	 * @throws ParseException if the response content could not be parsed
	 * @throws IOException if an error occurs while communicating with Fitbit
	 */
	public List<FitbitDaySample> getActivityDayData(String resource,
			LocalDate startDate, LocalDate endDate)
			throws FitbitAccessTokenException, HttpClientException,
			ParseException, IOException {
		String path = String.format("/1/user/-/activities/%s/date/%s/%s.json",
				resource, startDate.toString("yyyy-MM-dd"),
				endDate.toString("yyyy-MM-dd"));
		String json;
		try {
			json = query(path);
		} catch (HttpClientException ex) {
			if (isInvalidResourcePathException(ex))
				return null;
			else
				throw ex;
		}
		ObjectMapper mapper = new ObjectMapper();
		Map<?,?> map;
		try {
			map = mapper.readValue(json, Map.class);
		} catch (Exception ex) {
			throw new ParseException("Can't parse JSON map: " +
					ex.getMessage(), ex);
		}
		MapReader reader = new MapReader(map);
		Object list = reader.readObject("activities-" + resource);
		try {
			return mapper.convertValue(list,
					new TypeReference<List<FitbitDaySample>>() {});
		} catch (Exception ex) {
			throw new ParseException("Can't parse sample list: " +
					ex.getMessage(), ex);
		}
	}
	
	/**
	 * Runs a request to get intraday data per minute for the specified
	 * activities/... resource. Possible resources are specified in {@link
	 * FitbitSensor#ACTIVITY_INTRADAY_RESOURCES
	 * FitbitSensor.ACTIVITY_INTRADAY_RESOURCES}. If the user's tracker does
	 * not support the specified resource, then this method returns null.
	 * 
	 * <p>Fitbit always returns data until the current time, even if the user
	 * did not sync the tracker with Fitbit yet. This means that any returned
	 * data may change later.</p>
	 * 
	 * @param resource the resource
	 * @param date the date
	 * @return the response or null
	 * @throws FitbitAccessTokenException if Fitbit returns an oauth error for
	 * the access token. This may happen because the access token needs to be
	 * refreshed.
	 * @throws HttpClientException if a response other than 200 OK was returned
	 * @throws ParseException if the response content could not be parsed
	 * @throws IOException if an error occurs while communicating with Fitbit
	 */
	public FitbitIntradayResponse getActivityIntradayData(String resource,
			LocalDate date)
			throws FitbitAccessTokenException, HttpClientException,
			ParseException, IOException {
		String path = String.format(
				"/1/user/-/activities/%s/date/%s/1d/1min.json",
				resource, date.toString("yyyy-MM-dd"));
		String json;
		try {
			json = query(path);
		} catch (HttpClientException ex) {
			if (isInvalidResourcePathException(ex))
				return null;
			else
				throw ex;
		}
		ObjectMapper mapper = new ObjectMapper();
		Map<?,?> map;
		try {
			map = mapper.readValue(json, Map.class);
		} catch (Exception ex) {
			throw new ParseException("Can't parse JSON map: " +
					ex.getMessage(), ex);
		}
		MapReader reader = new MapReader(map);
		Object dayObj = reader.readObject("activities-" + resource);
		List<FitbitDaySample> daySamples;
		try {
			daySamples = mapper.convertValue(dayObj,
					new TypeReference<List<FitbitDaySample>>() {});
		} catch (Exception ex) {
			throw new ParseException("Can't parse day data: " +
					ex.getMessage(), ex);
		}
		Object intradayObj = reader.readObject("activities-" + resource +
				"-intraday");
		if (!(intradayObj instanceof Map))
			throw new ParseException("Intraday data is not a map");
		map = (Map<?,?>)intradayObj;
		reader = new MapReader(map);
		Object dataset = reader.readObject("dataset");
		List<FitbitIntradaySample> intradayData;
		try {
			intradayData = mapper.convertValue(dataset,
					new TypeReference<List<FitbitIntradaySample>>() {});
		} catch (Exception ex) {
			throw new ParseException("Can't parse intraday dataset: " +
					ex.getMessage(), ex);
		}
		if (daySamples.size() != 1) {
			throw new ParseException(String.format(
					"Expected day data for 1 day, found %d days",
					daySamples.size()));
		}
		FitbitIntradayResponse result = new FitbitIntradayResponse();
		result.setDayData(daySamples.get(0));
		result.setIntradayData(intradayData);
		return result;
	}
	
	/**
	 * Runs a request to get activity goals for today.
	 * 
	 * @return the activity goals
	 * @throws FitbitAccessTokenException if Fitbit returns an oauth error for
	 * the access token. This may happen because the access token needs to be
	 * refreshed.
	 * @throws HttpClientException if a response other than 200 OK was returned
	 * @throws ParseException if the response content could not be parsed
	 * @throws IOException if an error occurs while communicating with Fitbit
	 */
	public FitbitActivityGoals getActivityGoals()
			throws FitbitAccessTokenException, HttpClientException,
			ParseException, IOException {
		String path = "/1/user/-/activities/goals/daily.json";
		String json = query(path);
		ObjectMapper mapper = new ObjectMapper();
		Map<?,?> map;
		try {
			map = mapper.readValue(json, Map.class);
		} catch (Exception ex) {
			throw new ParseException("Can't parse JSON map: " +
					ex.getMessage(), ex);
		}
		MapReader reader = new MapReader(map);
		Object goals = reader.readObject("goals");
		try {
			return mapper.convertValue(goals, FitbitActivityGoals.class);
		} catch (Exception ex) {
			throw new ParseException("Can't parse activity goals: " +
					ex.getMessage(), ex);
		}
	}
	
	public String query(String path)
			throws FitbitAccessTokenException, HttpClientException,
			IOException {
		HttpClient client = new HttpClient(BASE_URL + path);
		try {
			client.addHeader("Authorization", tokenType + " " + accessToken);
			return client.readString();
		} catch (HttpClientException ex) {
			if (isAccessTokenException(ex)) {
				throw new FitbitAccessTokenException("Invalid access token",
						ex);
			} else {
				throw ex;
			}
		} finally {
			client.close();
		}
	}
	
	/**
	 * Tries to parse the error content of the specified exception and convert
	 * it to a {link FitbitErrorResponse FitbitErrorResponse}. If the error
	 * content can't be parsed, this method returns null.
	 * 
	 * @param ex the exception
	 * @return the Fitbit error response or null
	 */
	public static FitbitErrorResponse getFitbitErrorResponse(
			HttpClientException ex) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(ex.getErrorContent(),
					FitbitErrorResponse.class);
		} catch (Exception parseEx) {
			return null;
		}
	}
	
	/**
	 * Checks whether the specified exception contains an error about the
	 * Fitbit authentication code.
	 * 
	 * @param ex the exception
	 * @return true if the exception contains an error about the Fitbit
	 * authentication code, false otherwise
	 */
	public static boolean isAuthCodeException(HttpClientException ex) {
		if (ex.getStatusCode() != 400)
			return false;
		FitbitErrorResponse content = getFitbitErrorResponse(ex);
		if (content == null)
			return false;
		for (FitbitError error : content.getErrors()) {
			if (error.getErrorType().equals("oauth") &&
					error.getFieldName().equals("code")) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks whether the specified exception contains an error about
	 * insufficient scope.
	 * 
	 * @param ex the exception
	 * @return true if the exception contains an error about insufficient
	 * scope, false otherwise
	 */
	public static boolean isInsufficientScopeException(
			HttpClientException ex) {
		if (ex.getStatusCode() != 403)
			return false;
		FitbitErrorResponse content = getFitbitErrorResponse(ex);
		if (content == null)
			return false;
		for (FitbitError error : content.getErrors()) {
			if (error.getErrorType().equals("insufficient_scope"))
				return true;
		}
		return false;
	}
	
	private boolean isAccessTokenException(HttpClientException ex) {
		if (ex.getStatusCode() != 400 && ex.getStatusCode() != 401)
			return false;
		ObjectMapper mapper = new ObjectMapper();
		FitbitErrorResponse content;
		try {
			content = mapper.readValue(ex.getErrorContent(),
					FitbitErrorResponse.class);
		} catch (Exception parseEx) {
			return false;
		}
		for (FitbitError error : content.getErrors()) {
			if (error.getErrorType().equals("invalid_token") ||
					error.getErrorType().equals("invalid_grant") ||
					error.getErrorType().equals("expired_token")) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isInvalidResourcePathException(HttpClientException ex) {
		if (ex.getStatusCode() != 400)
			return false;
		ObjectMapper mapper = new ObjectMapper();
		FitbitErrorResponse content;
		try {
			content = mapper.readValue(ex.getErrorContent(),
					FitbitErrorResponse.class);
		} catch (Exception parseEx) {
			return false;
		}
		for (FitbitError error : content.getErrors()) {
			if (error.getErrorType().equals("validation") &&
					error.getFieldName().equals("resource path")) {
				return true;
			}
		}
		return false;
	}
}
