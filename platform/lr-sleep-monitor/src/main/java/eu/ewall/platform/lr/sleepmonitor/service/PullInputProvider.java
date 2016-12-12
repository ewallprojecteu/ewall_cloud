package eu.ewall.platform.lr.sleepmonitor.service;

import java.io.IOException;
import java.util.List;

import org.joda.time.LocalDate;

import eu.ewall.platform.idss.service.RemoteMethodException;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;

/**
 * Interface for the pull input provider for the {@link LRSleepMonitorService
 * LRSleepMonitorService}. The service uses this provider to pull input from
 * other components in the system. We call them remote components, although
 * they may run in the same process. An implementation must be set before
 * starting the service.
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
	 * Returns the sleep data for the night preceding the specified date. This
	 * method may returns null.
	 * 
	 * @param user the user name
	 * @param date the date
	 * @return the sleep data or null
	 * @throws IOException if an error occurs while communicating with the
	 * remote component
	 * @throws RemoteMethodException if the remote component returned an error
	 * @throws Exception if any other error occurs
	 */
	NightSleepData getNightSleepData(IDSSUserProfile user, LocalDate date)
			throws IOException, RemoteMethodException, Exception;
}
