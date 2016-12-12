package eu.ewall.platform.lr.sleepmonitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.profile.UserRole;
import eu.ewall.platform.idss.CommonsUserReader;
import eu.ewall.platform.idss.service.RemoteMethodException;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.LRSleepDayParameter;
import eu.ewall.platform.lr.sleepmonitor.service.NightSleepData;
import eu.ewall.platform.lr.sleepmonitor.service.PullInputProvider;
import eu.ewall.platform.lr.sleepmonitor.service.SleepParameter;

/**
 * Implementation of {@link PullInputProvider PullInputProvider} for eWall.
 * 
 * @author Dennis Hofs (RRD)
 */
public class LRSpringPullInputProvider implements PullInputProvider {
	private RestOperations ewallClient;
	private String idssSleepReasonerURL;
	private String profilingServerURL;
	
	/**
	 * Constructs a new instance.
	 * 
	 * @param ewallClient the eWall client
	 * @param dailyFunctioningServiceBrickURL base URL to
	 * service-brick-dailyfunctioning, without trailing slash
	 * @param profilingServerURL base URL to profiling-server, without trailing
	 * slash
	 */
	public LRSpringPullInputProvider(RestOperations ewallClient,
			String idssSleepReasonerURL,
			String profilingServerURL) {
		this.ewallClient = ewallClient;
		this.idssSleepReasonerURL = idssSleepReasonerURL;
		this.profilingServerURL = profilingServerURL;
	}

	@Override
	public List<IDSSUserProfile> getUsers() throws IOException, RemoteMethodException,
			Exception {
		String url = profilingServerURL + "/users/";
		List<?> objList = ewallClient.getForObject(url, List.class);
		List<IDSSUserProfile> idssUserProfiles = new ArrayList<IDSSUserProfile>();
		ObjectMapper mapper = new ObjectMapper();
		for (Object obj : objList) {
			User user = mapper.convertValue(obj, User.class);
			
			// Sleep Lifestyle Reasoner service only runs for primary users
			if(user.getUserRole().toString().equals(UserRole.PRIMARY_USER.toString())) {
				idssUserProfiles.add(CommonsUserReader.readUser(user));
			}
			
		}
		return idssUserProfiles;
	}

	@Override
	public NightSleepData getNightSleepData(IDSSUserProfile user,
			LocalDate date) throws IOException, RemoteMethodException,
			Exception {
		String url = idssSleepReasonerURL +
				"/sleepdata?username={username}&from={from}&to={to}";
		String timeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
		DateTime startTime = date.toDateTimeAtStartOfDay(user.getTimeZone());
		DateTime endTime = date.plusDays(1).toDateTimeAtStartOfDay(
				user.getTimeZone());
		List<?> response = ewallClient.getForObject(url, List.class,
				user.getUsername(), startTime.toString(timeFormat),
				endTime.toString(timeFormat));
		ObjectMapper mapper = new ObjectMapper();
		List<SleepDataServiceResponse> sleepDates = mapper.convertValue(
				response, new TypeReference<List<SleepDataServiceResponse>>() {});
		for (SleepDataServiceResponse sleepDate : sleepDates) {
			if (sleepDate.getTimestamp().isEqual(date))
				return serviceResponseToSleepData(user, sleepDate);
		}
		return null;
	}
	
	/**
	 * Converts the response from the IDSS sleep reasoner service to a {@link
	 * NightSleepData NightSleepData} object. It sets each known {@link
	 * SleepParameter SleepParameter} by copying the value from the service
	 * response to the sleep data object.
	 * 
	 * @param user the user profile
	 * @param response the service response
	 * @return the sleep data
	 */
	private NightSleepData serviceResponseToSleepData(IDSSUserProfile user,
			SleepDataServiceResponse response) {
		NightSleepData data = new NightSleepData();
		data.setUser(response.getUsername());
		data.setDate(response.getTimestamp());
		DateTime bedOffTime = new DateTime(response.getBedOffTime(),
				user.getTimeZone());
		String timeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
		data.putSleepParameter(LRSleepDayParameter.PARAM_BED_OFF_TIME,
				bedOffTime.toString(timeFormat));
		DateTime bedOnTime = new DateTime(response.getBedOnTime(),
				user.getTimeZone());
		data.putSleepParameter(LRSleepDayParameter.PARAM_BED_ON_TIME,
				bedOnTime.toString(timeFormat));
		data.putSleepParameter(LRSleepDayParameter.PARAM_FREQUENCY_WAKING_UP,
				Integer.toString(response.getFrequencyWakingUp()));
		data.putSleepParameter(LRSleepDayParameter.PARAM_SLEEP_EFFICIENCY,
				Integer.toString(response.getSleepEfficiency()));
		data.putSleepParameter(LRSleepDayParameter.PARAM_TOTAL_SLEEP_TIME,
				Integer.toString(response.getTotalSleepTime()));
		return data;
	}
}
