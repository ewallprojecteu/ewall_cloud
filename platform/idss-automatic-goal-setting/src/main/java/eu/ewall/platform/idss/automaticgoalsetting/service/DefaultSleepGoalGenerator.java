package eu.ewall.platform.idss.automaticgoalsetting.service;

import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.SleepGoalMeasure;

/**
 * This class can generate a default sleep goal, which is assigned when no
 * sleep data is available yet.
 * 
 * @author Dennis Hofs (RRD)
 */
public class DefaultSleepGoalGenerator {
	
	/**
	 * Returns the default goal for the specified sleep goal measure and user.
	 * 
	 * @param measure the sleep goal measure
	 * @param user the user profile
	 * @return the default goal
	 */
	public int getGoal(SleepGoalMeasure measure, IDSSUserProfile user) {
		switch (measure) {
		case MAXIMUM_MINUTES_SLEPT:
			return getMaximumMinutesSleptGoal(user);
		case MINIMUM_MINUTES_SLEPT:
			return getMinimumMinutesSleptGoal(user);
		case SLEEP_EFFICIENCY:
			return getSleepEfficiencyGoal(user);
		case GOING_TO_SLEEP_TIME:
			return getGoingToSleepTimeGoal(user);
		case WAKING_UP_TIME:
			return getWakingUpTimeGoal(user);
		default:
			throw new RuntimeException("Unknown SleepGoalMeasure: " + measure);
		}
	}
	
	/**
	 * Returns the default goal for maximum minutes slept.
	 * 
	 * @param user the user profile
	 * @return the default goal for maximum minutes slept
	 */
	private int getMaximumMinutesSleptGoal(IDSSUserProfile user) {
		return 720;
	}
	
	/**
	 * Returns the default goal for minimum minutes slept.
	 * 
	 * @param user the user profile
	 * @return the default goal for minimum minutes slept
	 */
	private int getMinimumMinutesSleptGoal(IDSSUserProfile user) {
		return 360;
	}

	/**
	 * Returns the default goal for sleep efficiency.
	 * 
	 * @param user the user profile
	 * @return the default goal for sleep effiency
	 */
	private int getSleepEfficiencyGoal(IDSSUserProfile user) {
		return 60;
	}

	/**
	 * Returns the default goal for going to sleep time.
	 * 
	 * @param user the user profile
	 * @return the default goal for going to sleep
	 */
	private int getGoingToSleepTimeGoal(IDSSUserProfile user) {
		return 1350;
	}
	
	/**
	 * Returns the default goal for waking up time.
	 * 
	 * @param user the user profile
	 * @return the default goal for waking up time
	 */
	private int getWakingUpTimeGoal(IDSSUserProfile user) {
		return 480;
	}
}
