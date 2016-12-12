package eu.ewall.platform.reasoner.activitycoach;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.profile.UserRole;
import eu.ewall.platform.idss.CommonsUserReader;
import eu.ewall.platform.idss.service.RemoteMethodException;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.reasoner.activitycoach.service.PullInputProvider;

/**
 * eWALL specific implementation of a {@link PullInputProvider} used for
 * pulling data into the application from external sources.
 * 
 * @author Dennis Hofs (RRD)
 * @author Harm op den Akker (RRD)
 */
public class ActivityCoachSpringPullInputProvider implements PullInputProvider {
	private RestOperations ewallClient;
	private String profilingServerUrl;
	
	/**
	 * Creates an instance of an {@link ActivityCoachSpringPullInputProvider} with the given
	 * {@code ewallClient} object that provides secure access to {@link RestOperations} within the 
	 * eWALL project.
	 * 
	 * @param ewallClient access point for secure {@link RestOperations} within the eWALL project.
	 * @param profilingServerUrl the URL of the eWALL profiling server in the current running context.
	 */
	public ActivityCoachSpringPullInputProvider(RestOperations ewallClient,
			String profilingServerUrl) {
		this.ewallClient = ewallClient;
		this.profilingServerUrl = profilingServerUrl;
	}
	
	@Override
	public List<IDSSUserProfile> getUsers() throws IOException, RemoteMethodException, Exception {
		String url = profilingServerUrl + "/users/";
		List<?> objList = ewallClient.getForObject(url, List.class);
		List<IDSSUserProfile> idssUserProfiles = new ArrayList<IDSSUserProfile>();
		ObjectMapper mapper = new ObjectMapper();
		for (Object obj : objList) {
			User user = mapper.convertValue(obj, User.class);
			
			// ActivityCoach service only runs for primary users
			if(user.getUserRole().toString().equals(UserRole.PRIMARY_USER.toString())) {
				idssUserProfiles.add(CommonsUserReader.readUser(user));
			}
			
		}
		return idssUserProfiles;
	}
}