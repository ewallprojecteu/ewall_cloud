package eu.ewall.platform.reasoner.sleep;

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
import eu.ewall.platform.reasoner.sleep.service.InputProvider;

public class SpringInputProvider implements InputProvider{
	private RestOperations ewallClient;
	private String activityServiceBrickURL;
	private String functioningServiceBrickURL;
	private String profilingServerURL;
	
	public SpringInputProvider(RestOperations ewallClient,
			String activityServiceBrickURL, String functioningServiceBrickURL,
			String profilingServerURL) {
		this.ewallClient = ewallClient;
		this.activityServiceBrickURL = activityServiceBrickURL;
		this.functioningServiceBrickURL = functioningServiceBrickURL;
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
	public InactivityDataResponse getInactivity(IDSSUserProfile user, LocalDate date)
			throws IOException, RemoteMethodException, Exception {
		DateTime start = date.toDateTimeAtStartOfDay(user.getTimeZone()).minusHours(3);
		DateTime end = date.toDateTimeAtStartOfDay(user.getTimeZone()).plusHours(10);
		
		String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
		
		
		String url = activityServiceBrickURL + "/v1/{userid}/inactivity" +
				"?from={from}&to={to}";
		
		InactivityDataResponse response = ewallClient.getForObject(url,
				InactivityDataResponse.class, user.getUsername(),
				start.toString(dateFormat), end.toString(dateFormat));
		return response;
	}
	
	@Override
	public InbedDataResponse getInbed(IDSSUserProfile user, LocalDate date)
			throws IOException, RemoteMethodException, Exception {
		
		DateTime start = date.toDateTimeAtStartOfDay(user.getTimeZone()).minusHours(3);
		DateTime end = date.toDateTimeAtStartOfDay(user.getTimeZone()).plusHours(10);
		
		String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
		
		String url = functioningServiceBrickURL + "/v1/{userid}/inbed" +
				"?from={from}&to={to}";
		
		InbedDataResponse response = ewallClient.getForObject(url,
				InbedDataResponse.class, user.getUsername(),
				start.toString(dateFormat), end.toString(dateFormat));
		return response;
	}
}
