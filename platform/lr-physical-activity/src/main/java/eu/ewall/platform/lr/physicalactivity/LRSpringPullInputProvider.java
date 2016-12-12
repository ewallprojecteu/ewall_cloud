package eu.ewall.platform.lr.physicalactivity;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.profile.UserRole;
import eu.ewall.platform.idss.CommonsUserReader;
import eu.ewall.platform.idss.response.ewall.activity.ActivityServiceBrickResponse;
import eu.ewall.platform.idss.service.RemoteMethodException;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.lr.physicalactivity.service.LRPhysicalActivityService;
import eu.ewall.platform.lr.physicalactivity.service.PullInputProvider;

/**
 * Implementation of {@link PullInputProvider PullInputProvider} for eWall.
 * 
 * @author Dennis Hofs (RRD)
 */
public class LRSpringPullInputProvider implements PullInputProvider {
	private RestOperations ewallClient;
	private String activityServiceBrickURL;
	private String profilingServerURL;
	
	/**
	 * Constructs a new instance.
	 * 
	 * @param ewallClient the eWall client
	 * @param activityServiceBrickURL base URL to
	 * service-brick-physicalactivity, without trailing slash
	 * @param profilingServerURL base URL to profiling-server, without trailing
	 * slash
	 */
	public LRSpringPullInputProvider(RestOperations ewallClient,
			String activityServiceBrickURL, String profilingServerURL) {
		this.ewallClient = ewallClient;
		this.activityServiceBrickURL = activityServiceBrickURL;
		this.profilingServerURL = profilingServerURL;
	}

	@Override
	public List<IDSSUserProfile> getUsers() throws IOException, RemoteMethodException,
			Exception {
		String url = profilingServerURL + "/users/";
		List<?> objList = ewallClient.getForObject(url, List.class);
		List<IDSSUserProfile> users = new ArrayList<IDSSUserProfile>();
		ObjectMapper mapper = new ObjectMapper();
		for (Object obj : objList) {
			User user = mapper.convertValue(obj, User.class);
			if (user.getUserRole() == UserRole.PRIMARY_USER)
				users.add(CommonsUserReader.readUser(user));
		}
		return users;
	}

	@Override
	public int getStepCount(IDSSUserProfile user, LocalDate date)
			throws IOException, RemoteMethodException, Exception {
		DateTime start = date.toDateTimeAtStartOfDay(user.getTimeZone());
		DateTime end = date.plusDays(1).toDateTimeAtStartOfDay(
				user.getTimeZone());
		String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
		String url = activityServiceBrickURL + "/v1/{userid}/movement" +
				"?from={from}&to={to}&aggregation=1d";
		Logger logger = AppComponents.getLogger(
				LRPhysicalActivityService.LOGTAG);
		String encodedUrl = url.replaceAll("\\{userid\\}",
						URLEncoder.encode(user.getUsername(), "UTF-8"))
				.replaceAll("\\{from\\}",
						URLEncoder.encode(start.toString(dateFormat), "UTF-8"))
				.replaceAll("\\{to\\}",
						URLEncoder.encode(end.toString(dateFormat), "UTF-8"));
		logger.info("Call activity service brick: " + encodedUrl);
		ActivityServiceBrickResponse response = ewallClient.getForObject(url,
				ActivityServiceBrickResponse.class, user.getUsername(),
				start.toString(dateFormat), end.toString(dateFormat));
		return response.getTotalSteps();
	}

	@Override
	public int getCalories(IDSSUserProfile user, LocalDate date)
			throws IOException, RemoteMethodException, Exception {
		return 0;
	}

	@Override
	public int getDistance(IDSSUserProfile user, LocalDate date)
			throws IOException, RemoteMethodException, Exception {
		return 0;
	}
}
