package eu.ewall.platform.reasoner.activitycoach.service;

import eu.ewall.platform.idss.service.RemoteMethodException;

import eu.ewall.platform.idss.service.model.IDSSUserProfile;

import java.io.IOException;

import java.util.List;

/**
 * Interface for the pull input provider for the {@link VCService
 * ActivityCoachService}. The service uses this provider to pull input from
 * other components in the system. We call them remote components, although
 * they may run in the same process. An implementation must be set before
 * starting the service.
 * 
 * @author Dennis Hofs
 */
public interface PullInputProvider {

	/**
	 * Returns all users that the service should work on.
	 * 
	 * @return the users
	 * @throws IOException if an error occurs while communicating with the
	 * remote component
	 * @throws RemoteMethodException if the remote component returned an error
	 * @throws Exception if any other error occurs
	 */
	List<IDSSUserProfile> getUsers()
			throws IOException, RemoteMethodException, Exception;
}
