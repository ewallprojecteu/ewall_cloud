package eu.ewall.platform.idss.automaticgoalsetting.service;

import java.io.IOException;
import java.util.List;

import org.joda.time.LocalDate;

import eu.ewall.platform.idss.service.RemoteMethodException;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.ActivityMeasure;
import eu.ewall.platform.idss.service.model.common.AverageActivity;
import eu.ewall.platform.idss.service.model.common.LRSleepWeekPattern;
import eu.ewall.platform.idss.service.model.common.SleepGoalMeasure;

/**
 * Interface for the pull input provider for the {@link
 * IDSSAutomaticGoalSettingService IDSSAutomaticGoalSettingService}. The
 * service uses this provider to pull input from other components in the
 * system. We call them remote components, although they may run in the same
 * process. An implementation must be set before starting the service.
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
	 * Returns the week day that defines the start of the week for the
	 * specified user.
	 * 
	 * @param user the username
	 * @return the week day (1 is Monday, 7 is Sunday)
	 * @throws IOException if an error occurs while communicating with the
	 * remote component
	 * @throws RemoteMethodException if the remote component returned an error
	 * @throws Exception if any other error occurs
	 */
	int getWeekStart(String user)
			throws IOException, RemoteMethodException, Exception;
	
	/**
	 * Returns the date until which activity data has been processed to
	 * generate a week pattern. This date is inclusive. If no activity data
	 * has been processed, this method returns null.
	 * 
	 * @param user the user name
	 * @return the date until which activity data has been processed or null
	 * @throws IOException if an error occurs while communicating with the
	 * remote component
	 * @throws RemoteMethodException if the remote component returned an error
	 * @throws Exception if any other error occurs
	 */
	LocalDate getLastActivityWeekPatternUpdate(String user)
			throws IOException, RemoteMethodException, Exception;

	/**
	 * Returns the week pattern of physical activity for the specified user as
	 * known after the specified date. This means that no activity data after
	 * that date has been processed to calculate the week pattern. If the date
	 * is set to null, it returns the most recent week pattern. It returns at
	 * most one instance for each week day. Days with no activity history are
	 * not included. You may call {@link
	 * #getLastActivityWeekPatternUpdate(String)
	 * getLastActivityWeekPatternUpdate()} to find out how much activity data
	 * has been processed so far.
	 * 
	 * @param user the username
	 * @param measure the activity measure
	 * @param date the date as described above
	 * @return the week pattern of physical activity
	 * @throws IOException if an error occurs while communicating with the
	 * remote component
	 * @throws RemoteMethodException if the remote component returned an error
	 * @throws Exception if any other error occurs
	 */
	List<AverageActivity> getActivityWeekPattern(String user,
			ActivityMeasure measure, LocalDate date) throws IOException,
			RemoteMethodException, Exception;
	
	/**
	 * Returns the ultimate activity goal for the specified user and activity
	 * measure.
	 * 
	 * @param user the username
	 * @param measure the activity measure
	 * @return the ultimate activity goal
	 * @throws IOException if an error occurs while communicating with the
	 * remote component
	 * @throws RemoteMethodException if the remote component returned an error
	 * @throws Exception if any other error occurs
	 */
	int getUltimateActivityGoal(String user, ActivityMeasure measure)
			throws IOException, RemoteMethodException, Exception;

	/**
	 * Returns the date following the night until which sleep data has been
	 * processed to generate a week pattern. If no sleep data has been
	 * processed, this method returns null.
	 * 
	 * @param user the user name
	 * @return the date until which sleep data has been processed or null
	 * @throws IOException if an error occurs while communicating with the
	 * remote component
	 * @throws RemoteMethodException if the remote component returned an error
	 * @throws Exception if any other error occurs
	 */
	public LocalDate getLastSleepWeekPatternUpdate(String user)
			throws IOException, RemoteMethodException, Exception;

	/**
	 * Returns the sleep week pattern for the specified user as known after the
	 * night that precedes the specified date. This means that no sleep data
	 * after that night has been processed to calculate the week pattern. If
	 * the date is set to null, it returns the most recent week pattern. You
	 * may call {@link #getLastSleepWeekPatternUpdate(String)
	 * getLastSleepWeekPatternUpdate()} to find out how much sleep data has
	 * been processed so far.
	 * 
	 * @param user the user name
	 * @param date the date as described above
	 * @return the sleep week pattern
	 * @throws IOException if an error occurs while communicating with the
	 * remote component
	 * @throws RemoteMethodException if the remote component returned an error
	 * @throws Exception if any other error occurs
	 */
	LRSleepWeekPattern getSleepWeekPattern(String user, LocalDate date)
			throws IOException, RemoteMethodException, Exception;
	
	/**
	 * Returns the ultimate sleep goal for the specified user and sleep goal
	 * measure.
	 * 
	 * @param user the username
	 * @param measure the sleep goal measure
	 * @return the ultimate sleep goal
	 * @throws IOException if an error occurs while communicating with the
	 * remote component
	 * @throws RemoteMethodException if the remote component returned an error
	 * @throws Exception if any other error occurs
	 */
	int getUltimateSleepGoal(String user, SleepGoalMeasure measure)
			throws IOException, RemoteMethodException, Exception;
}
