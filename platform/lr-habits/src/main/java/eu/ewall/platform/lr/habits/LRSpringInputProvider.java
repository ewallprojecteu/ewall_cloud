package eu.ewall.platform.lr.habits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.idss.CommonsUserReader;
import eu.ewall.platform.idss.service.RemoteMethodException;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.lr.habits.model.FunctioningActivityHistory;
import eu.ewall.platform.lr.habits.service.LRInputProvider;

public class LRSpringInputProvider implements LRInputProvider{
	
	private RestOperations ewallClient;
	private String habitsServiceBrickURL;
	private String profilingServerURL;
	
	public LRSpringInputProvider(RestOperations ewallClient,
			String habitsServiceBrickURL, String profilingServerURL) {
		this.ewallClient = ewallClient;
		this.habitsServiceBrickURL = habitsServiceBrickURL;
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
			users.add(CommonsUserReader.readUser(user));
		}
		return users;
	}
	
	@Override
	public FunctioningActivityHistory getFunctioningActivityData(
			String user, 
			DateTime from, 
			DateTime to)
			throws IOException, RemoteMethodException, Exception {
		String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
		String url = habitsServiceBrickURL + "/v1/{userid}/functioningactivity" +
				"?from={from}&to={to}";
		FunctioningActivityHistory response;
		
			response = ewallClient.getForObject(url,
					FunctioningActivityHistory.class, user,
					from.toString(dateFormat), to.toString(dateFormat));
		
		return response;
	}

}
