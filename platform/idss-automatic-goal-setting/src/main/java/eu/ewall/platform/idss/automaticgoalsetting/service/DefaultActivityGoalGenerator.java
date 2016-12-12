package eu.ewall.platform.idss.automaticgoalsetting.service;

import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.ActivityMeasure;

/**
 * This class can generate a default activity goal, which is assigned when no
 * activity data is available yet.
 * 
 * @author Dennis Hofs (RRD)
 */
public class DefaultActivityGoalGenerator {
	
	/**
	 * Returns the default goal for the specified activity measure and user.
	 * 
	 * @param measure the activity measure
	 * @param user the user profile
	 * @return the default goal
	 */
	public int getGoal(ActivityMeasure measure, IDSSUserProfile user) {
		switch (measure) {
		case STEPS:
			return getStepsGoal(user);
		case CALORIES:
			return getCaloriesGoal(user);
		case DISTANCE:
			return getDistanceGoal(user);
		default:
			throw new RuntimeException("Unknown ActivityMeasure: " + measure);
		}
	}
	
	/**
	 * Returns the default goal in steps.
	 * 
	 * @param user the user profile
	 * @return the default goal in steps
	 */
	private int getStepsGoal(IDSSUserProfile user) {
		return 10000;
	}
	
	/**
	 * Returns the default goal in calories.
	 * 
	 * @param user the user profile
	 * @return the default goal in calories
	 */
	private int getCaloriesGoal(IDSSUserProfile user) {
		return 2500;
	}

	/**
	 * Returns the default goal in metres.
	 * 
	 * @param user the user profile
	 * @return the default goal in metres
	 */
	private int getDistanceGoal(IDSSUserProfile user) {
		return 8000;
	}
}
