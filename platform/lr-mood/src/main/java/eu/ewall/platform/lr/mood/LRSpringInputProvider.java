package eu.ewall.platform.lr.mood;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.profile.UserRole;
import eu.ewall.platform.idss.CommonsUserReader;
import eu.ewall.platform.idss.service.RemoteMethodException;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.lr.mood.service.LRInputProvider;

public class LRSpringInputProvider implements LRInputProvider{
	
	private RestOperations ewallClient;
	private String activityServiceBrickURL;
	private String profilingServerURL;
	
	public LRSpringInputProvider(RestOperations ewallClient,
			String activityServiceBrickURL, String profilingServerURL) {
		this.ewallClient = ewallClient;
		this.activityServiceBrickURL = activityServiceBrickURL;
		this.profilingServerURL = profilingServerURL;
	}

	@Override
	public List<IDSSUserProfile> getUsers() throws IOException,
			RemoteMethodException, Exception {
		String url = profilingServerURL + "users/";
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
	public List<MoodDataItem> getMoodDataItem(IDSSUserProfile user, LocalDate date)
			throws IOException, RemoteMethodException, Exception {
		DateTime start = date.toDateTimeAtStartOfDay(user.getTimeZone());
		DateTime end = date.plusDays(1).toDateTimeAtStartOfDay(
				user.getTimeZone());
		String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
		String url = activityServiceBrickURL + "/v1/{userid}/mood" +
				"?from={from}&to={to}&aggregation=1d";
		MoodServiceBrickResponse response = ewallClient.getForObject(url,
				MoodServiceBrickResponse.class, user.getUsername(),
				start.toString(dateFormat), end.toString(dateFormat));
		return response.getMood();
	}

}
