package eu.ewall.platform.lr.physicalactivity.service;

import eu.ewall.platform.idss.service.RemoteMethodException;

import eu.ewall.platform.idss.service.model.IDSSUserProfile;

import java.io.IOException;

import java.util.List;

import org.joda.time.LocalDate;

/**
 * Interface for the pull input provider for the {@link
 * LRPhysicalActivityService LRPhysicalActivityService}. The service uses this
 * provider to pull input from other components in the system. We call them
 * remote components, although they may run in the same process. An
 * implementation must be set before starting the service.
 * 
 * @author Dennis Hofs (RRD)
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
	
	/**
	 * Returns the total step count for the specified date in the local time of
	 * the user.
	 * 
	 * @param user the user
	 * @param date the date
	 * @return the total step count
	 * @throws IOException if an error occurs while communicating with the
	 * remote component
	 * @throws RemoteMethodException if the remote component returned an error
	 * @throws Exception if any other error occurs
	 */
	int getStepCount(IDSSUserProfile user, LocalDate date)
			throws IOException, RemoteMethodException, Exception;
	
	/**
	 * Returns the total number of calories burnt for the specified date in the
	 * local time of the user.
	 * 
	 * @param user the user
	 * @param date the date
	 * @return the total number of calories
	 * @throws IOException if an error occurs while communicating with the
	 * remote component
	 * @throws RemoteMethodException if the remote component returned an error
	 * @throws Exception if any other error occurs
	 */
	int getCalories(IDSSUserProfile user, LocalDate date)
			throws IOException, RemoteMethodException, Exception;

	/**
	 * Returns the total distance in metres for the specified date in the local
	 * time of the user.
	 * 
	 * @param user the user
	 * @param date the date
	 * @return the total distance in metres
	 * @throws IOException if an error occurs while communicating with the
	 * remote component
	 * @throws RemoteMethodException if the remote component returned an error
	 * @throws Exception if any other error occurs
	 */
	int getDistance(IDSSUserProfile user, LocalDate date)
			throws IOException, RemoteMethodException, Exception;
}
