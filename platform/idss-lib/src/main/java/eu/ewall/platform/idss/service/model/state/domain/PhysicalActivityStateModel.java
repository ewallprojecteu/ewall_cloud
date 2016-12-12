package eu.ewall.platform.idss.service.model.state.domain;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.service.model.state.StateModel;
import eu.ewall.platform.idss.service.model.type.StageOfChange;

/**
 * This model defines the state of a user with respect to the health domain
 * of physical activity. When you set an attribute, you should also set the
 * source reliability and update time. This can be done at once using {@link
 * #setAttribute(String, Object, Double, DateTime) setAttribute()} from {@link
 * StateModel StateModel}.
 * 
 * @author Dennis Hofs (RRD)
 * @author Harm op den Akker (RRD)
 */
public class PhysicalActivityStateModel extends StateModel {
	
	// TODO: Set these values based on thesis Miriam C.
	public static final Double SELF_EFFICACY_LOW_THRESHOLD = 0.2;
	public static final Double SELF_EFFICACY_HIGH_THRESHOLD = 0.8;
	
	private static final int DECAY_TIME_STAGE_OF_CHANGE = 604800000; // One week
	private static final int DECAY_TIME_SELF_EFFICACY = 604800000; // One week
	
	@DatabaseField(value=DatabaseType.OBJECT)
	private ActivityState currentActivityState = null;
	
	@DatabaseField(value=DatabaseType.OBJECT)
	private ActivityState goalActivityState = null;
	
	@DatabaseField(value= DatabaseType.FLOAT)
	private Double selfEfficacy = null;

	@DatabaseField(value=DatabaseType.STRING)
	private StageOfChange stageOfChange = null;

	/**
	 * Returns the current activity state (expressed in possibly various
	 * units dependent on the application) of the user.
	 * 
	 * @return the current activity state of the user.
	 */
	public ActivityState getCurrentActivityState() {
		return currentActivityState;
	}

	/**
	 * Sets the current activity state (expressed in possibly various
	 * units dependent on the application) of the user.
	 * 
	 * @param currentActivityState the current {@link ActivityState} of the user.
	 */
	public void setCurrentActivityState(ActivityState currentActivityState) {
		this.currentActivityState = currentActivityState;
	}

	/**
	 * Returns the target {@link ActivityState}, e.g. the activity goals
	 * expressed in possibly various units dependent on the application.
	 * 
	 * @return the target {@link ActivityState} for the user.
	 */
	public ActivityState getGoalActivityState() {
		return goalActivityState;
	}

	/**
	 * Sets the target {@link ActivityState}, e.g. the activity goals
	 * expressed in possibly various units dependent on the application.
	 * 
	 * @param goalActivityState the target {@link ActivityState} for the user.
	 */
	public void setGoalActivityState(ActivityState goalActivityState) {
		this.goalActivityState = goalActivityState;
	}

	/**
	 * Returns the self-efficacy (0..1).
	 * 
	 * @return the self-efficacy
	 */
	public Double getSelfEfficacy() {
		return selfEfficacy;
	}

	/**
	 * Sets the self-efficacy (0..1).
	 * 
	 * @param selfEfficacy the self-efficacy
	 */
	public void setSelfEfficacy(Double selfEfficacy) {
		this.selfEfficacy = selfEfficacy;
	}

	/**
	 * Returns the stage of change.
	 * 
	 * @return the stage of change
	 */
	public StageOfChange getStageOfChange() {
		return stageOfChange;
	}

	/**
	 * Sets the stage of change.
	 * 
	 * @param stageOfChange the stage of change
	 */
	public void setStageOfChange(StageOfChange stageOfChange) {
		this.stageOfChange = stageOfChange;
	}
	
	// ----------
	
	/**
	 * Returns the current activity level of the user in steps, or {@code null} if no
	 * current {@link ActivityState} is known.
	 * @return the current activity level of the user in steps.
	 */
	@JsonIgnore
	public Integer getCurrentActivitySteps() {
		if(currentActivityState != null)
			return currentActivityState.getSteps();
		else return null;
	}
	
	/**
	 * Returns the current activity level of the user in burned calories, or {@code null} if no
	 * current {@link ActivityState} is known.
	 * @return the current activity level of the user in steps.
	 */
	@JsonIgnore
	public Integer getCurrentActivityBurnedCalories() {
		if(currentActivityState != null)
			return currentActivityState.getBurnedCalories();
		else return null;
	}
	
	/**
	 * Returns the current activity level in kilometers (truncated), or {@code null} if no
	 * current {@link ActivityState} is known.
	 * @return the current activity level in kilometers (truncated).
	 */
	@JsonIgnore
	public Double getCurrentActivityKilometers() {
		if(currentActivityState != null) {
			Float currentActivityKilometers = currentActivityState.getKilometers();
			Double truncatedKilometers = new BigDecimal(currentActivityKilometers).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
			return truncatedKilometers;
		} else return null;
	}
	
	/**
	 * Returns the current goal value of the user in steps.
	 * @return the current goal value of the user in steps.
	 */
	@JsonIgnore
	public Integer getCurrentGoalSteps() {
		if(goalActivityState != null)
			return goalActivityState.getSteps();
		else return null;
	}
	
	/**
	 * Returns the current goal value of the user in burned calories.
	 * @return the current goal value of the user in burned calories.
	 */
	@JsonIgnore
	public Integer getCurrentGoalBurnedCalories() {
		if(goalActivityState != null)
			return goalActivityState.getBurnedCalories();
		else return null;
	}
	
	/**
	 * Returns the current goal value of the user in kilometers traveled (truncated).
	 * @return the current goal value of the user in kilometers traveled (truncated).
	 */
	public Double getCurrentGoalKilometers() {
		if(goalActivityState != null) {
			Float currentGoalKilometers = goalActivityState.getKilometers();
			Double truncatedKilometers = new BigDecimal(currentGoalKilometers).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
			return truncatedKilometers;
		} else return null;
	}
	
	/**
	 * Returns how many steps the user still has to take in order to reach his goal, or how
	 * many steps the user has surpassed his goal, or {@code null} if no current activity or
	 * activity goal state is known.
	 * @return the distance in steps between current activity and goal activity.
	 */
	@JsonIgnore
	public Integer getStepsFromGoal() {
		if(currentActivityState != null && goalActivityState != null) {
			return Math.abs(goalActivityState.getSteps() - currentActivityState.getSteps());
		} else {
			return null;
		}
	}
	
	/**
	 * Returns how many kilometers the user still has to walk in order to reach his goal, or how
	 * many kilometers the user has surpassed his goal, or {@code null} if no current activity or
	 * activity goal state is known.
	 * @return the distance in kilometers between current activity and goal activity.
	 */
	@JsonIgnore
	public Double getKilometersFromGoal() {
		if(currentActivityState != null && goalActivityState != null) {
			float kilometersFromGoal = Math.abs(goalActivityState.getKilometers() - currentActivityState.getKilometers());
			Double truncatedKilometers = new BigDecimal(kilometersFromGoal).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
			return truncatedKilometers;
		} else {
			return null;
		}
	}
	
	/**
	 * Returns how many calories the user still has to burn in order to reach his goal, or how
	 * many burned calories the user has surpassed his goal, or {@code null} if no current activity or
	 * activity goal state is known.
	 * @return the number of burned calories between current activity and goal activity.
	 */
	public Integer getBurnedCaloriesFromGoal() {
		if(currentActivityState != null && goalActivityState != null) {
			return Math.abs(goalActivityState.getBurnedCalories() - currentActivityState.getBurnedCalories());
		} else {
			return null;
		}
	}
	
	/**
	 * Returns the percentage of the current activity step goal the user has achieved.
	 * @return the percentage of the current activity step goal the user has achieved.
	 */
	@JsonIgnore
	public Integer getGoalPercentageSteps() {
		if(currentActivityState != null && goalActivityState != null) {
			int currentSteps =  currentActivityState.getSteps();
			int currentStepsGoal = goalActivityState.getSteps();
			
			int goalPercentage = (int)Math.round((currentSteps / currentStepsGoal) * 100.0);
			return goalPercentage;			
		} else {
			return null;
		}
	}
	
	@Override
	public int getReliabilityDecayTime(String attribute) {
		if(attribute.equals("stageOfChange")) {
			return DECAY_TIME_STAGE_OF_CHANGE;
		} else if(attribute.equals("selfEfficacy")) {
			return DECAY_TIME_SELF_EFFICACY;
		} else {
			return DEFAULT_RELIABILITY_DECAY_TIME;
		}
	}
		
}
