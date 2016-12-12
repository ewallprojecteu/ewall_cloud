package eu.ewall.platform.lr.environment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.InternalServerErrorException;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.idss.CommonsUserReader;
import eu.ewall.platform.idss.service.RemoteMethodException;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.lr.environment.model.IlluminanceHistory;
import eu.ewall.platform.lr.environment.service.LRInputProvider;

public class LRSpringInputProvider implements LRInputProvider{
	
	private RestOperations ewallClient;
	private String environmentServiceBrickURL;
	private String profilingServerURL;
	
	public LRSpringInputProvider(RestOperations ewallClient,
			String environmentServiceBrickURL, String profilingServerURL) {
		this.ewallClient = ewallClient;
		this.environmentServiceBrickURL = environmentServiceBrickURL;
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
	public IlluminanceHistory getIlluminanceData(
			IDSSUserProfile user, 
			DateTime from, 
			DateTime to,
			String location)
			throws IOException, RemoteMethodException, Exception {
		String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
		String url = environmentServiceBrickURL + "/v1/{userid}/illuminance" +
				"?from={from}&to={to}";
		IlluminanceHistory response;
		
		if(location.equals("") || location == null) {
			response = ewallClient.getForObject(url,
					IlluminanceHistory.class, user.getUsername(),
					from.toString(dateFormat), to.toString(dateFormat));
		} else {
			url = environmentServiceBrickURL + "/v1/{userid}/illuminance" +
					"?location={location}&from={from}&to={to}";
			
			response = ewallClient.getForObject(url,
					IlluminanceHistory.class, user.getUsername(),
					location ,from.toString(dateFormat), to.toString(dateFormat));
			
		}
		
		return response;
	}
		

}
