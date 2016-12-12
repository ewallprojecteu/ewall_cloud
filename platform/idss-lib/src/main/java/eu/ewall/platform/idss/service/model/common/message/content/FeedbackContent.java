package eu.ewall.platform.idss.service.model.common.message.content;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.service.model.type.ActivityUnit;

/**
 * A {@link FeedbackContent} object models the {@link MessageContent} part related
 * to the provision of feedback to the user.
 * 
 * @author Harm op den Akker (RRD)
 */
public class FeedbackContent extends AbstractDatabaseObject {
	
	@DatabaseField(value= DatabaseType.INT)
	private boolean hasGoalSetting = false;
	
	@DatabaseField(value= DatabaseType.INT)
	private boolean hasIdentification = false;
	
	@DatabaseField(value= DatabaseType.INT)
	private boolean isUnderGoal = false;
	
	@DatabaseField(value= DatabaseType.INT)
	private boolean isOverGoal = false;
	
	@DatabaseField(value=DatabaseType.STRING)
	private ActivityUnit activityUnit;
	
	// ---------- CONSTRUCTORS ---------- //
	
	/**
	 * Creates an instance of a {@link FeedbackContent} object with the given parameters. Keep in mind that
	 * only one of {@link FeedbackContent#getIsUnderGoal()} or {@link FeedbackContent#getIsOverGoal()} can be set to
	 * {@code true}. If both {@code isUnderGoal} and {@code isOverGoal} are passed as {@code true}, the {@code isOverGoal} will be 
	 * set as last, and will set {@code isUnderGoal} to {@code false}.
	 * @param hasGoalSetting whether or not this {@link FeedbackContent} should include goal-setting.
	 * @param hasIdentification whether or not this {@link FeedbackContent} should include identification.
	 * @param isUnderGoal whether or not the {@link FeedbackContent} should indicate that the user
	 * has not yet reached his/her goal. 
	 * @param isOverGoal whether or not the {@link FeedbackContent} should indicate that the user
	 * has already reached his/her goal.
	 */
	public FeedbackContent(boolean hasGoalSetting, boolean hasIdentification, boolean isUnderGoal, boolean isOverGoal, ActivityUnit activityUnit) {
		this.hasGoalSetting = hasGoalSetting;
		this.hasIdentification = hasIdentification;
		setIsUnderGoal(isUnderGoal);
		setIsOverGoal(isOverGoal);
		this.activityUnit = activityUnit;
	}
	
	/**
	 * Creates an instance of a {@link FeedbackContent} object with all of its parameters set 
	 * to {@code false}.
	 */
	public FeedbackContent() { } 
	
	// ---------- GETTERS ---------- //
	
	/**
	 * Returns whether or not this {@link FeedbackContent} should include goal-setting.
	 * @return whether or not this {@link FeedbackContent} should include goal-setting.
	 */
	public boolean getHasGoalSetting() {
		return hasGoalSetting;
	}
	
	/**
	 * Returns whether or not this {@link FeedbackContent} should include identification.
	 * @return whether or not this {@link FeedbackContent} should include identification.
	 */
	public boolean getHasIdentification() {
		return hasIdentification;
	}
	
	/**
	 * Returns whether or not the {@link FeedbackContent} should indicate that the user
	 * has not yet reached his/her goal. If this method returns {@code true}, the method 
	 * {@link FeedbackContent#getIsOverGoal()} should return false, and vice-versa.
	 * @return whether or not the {@link FeedbackContent} should indicate that the user
	 * has not yet reached his/her goal.
	 */
	public boolean getIsUnderGoal() {
		return isUnderGoal;
	}
	
	/**
	 * Returns whether or not the {@link FeedbackContent} should indicate that the user
	 * has already reached his/her goal. If this method returns {@code true}, the method 
	 * {@link FeedbackContent#getIsUnderGoal()} should return false, and vice-versa.
	 * @return whether or not the {@link FeedbackContent} should indicate that the user
	 * has already reached his/her goal.
	 */
	public boolean getIsOverGoal() {
		return isOverGoal;
	}
	
	/**
	 * Returns the activity unit as either {@link ActivityUnit#STEPS}, {@link ActivityUnit#CALORIES},
	 * or {@link ActivityUnit#DISTANCE} in which the feedback should be presented.
	 * @return the activity unit for this {@link FeedbackContent}.
	 */
	public ActivityUnit getActivityUnit() {
		return activityUnit;
	}
	
	// ---------- SETTERS ---------- //
	
	/**
	 * Indicates whether this {@link FeedbackContent} should include the use of goal-setting.
	 * @param hasGoalSetting whether or not goal-setting should be used.
	 */
	public void setHasGoalSetting(boolean hasGoalSetting) {
		this.hasGoalSetting = hasGoalSetting;
	}
	
	/**
	 * Indicates whether this {@link FeedbackContent} should make use of identification (i.e.
	 * calling the user by name.
	 * @param hasIdentification whether or not identification should be used.
	 */
	public void setHasIdentification(boolean hasIdentification) {
		this.hasIdentification = hasIdentification;
	}
	
	/**
	 * Sets whether or not the user has not yet reached his goal. If true, the value of
	 * {@link FeedbackContent#getIsOverGoal()} is set to false.
	 * @param isUnderGoal whether or not the user has not yet reached his goal.
	 */
	public void setIsUnderGoal(boolean isUnderGoal) {
		this.isUnderGoal = isUnderGoal;
		if(isUnderGoal) {
			this.isOverGoal = false;
		}
	}
	
	/**
	 * Sets whether or not the user has already passed his goal. If true, the value of
	 * {@link FeedbackContent#getIsUnderGoal()} is set to false.
	 * @param isOverGoal whether or not the user has already passed his goal.
	 */
	public void setIsOverGoal(boolean isOverGoal) {
		this.isOverGoal = isOverGoal;
		if(isOverGoal) {
			this.isUnderGoal = false;
		}
	}
	
	/**
	 * Sets the activity unit as either {@link ActivityUnit#STEPS}, {@link ActivityUnit#CALORIES},
	 * or {@link ActivityUnit#DISTANCE} in which the feedback should be presented.
	 * @param activityUnit the activity unit for this {@link FeedbackContent}.
	 */
	public void setActivityUnit(ActivityUnit activityUnit) {
		this.activityUnit = activityUnit;
	}
}
