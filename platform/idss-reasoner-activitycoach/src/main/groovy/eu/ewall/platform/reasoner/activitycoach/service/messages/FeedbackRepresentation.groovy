package eu.ewall.platform.reasoner.activitycoach.service.messages

/**
 * Message representation class for 'feedback'.
 * 
 * @author Dennis Hofs (RRD)
 * @author Harm op den Akker (RRD)
 */
class FeedbackRepresentation extends BaseRepresentation {

	private boolean withGoalSetting = false
	private boolean underGoal = false;
	private boolean overGoal = false;
	private String activityUnit = null;
	
	/**
	 * Sets whether this feedback message mentions the goal.
	 * 
	 * @param withGoalSetting true if this feedback message mentions the goal, false otherwise.
	 * @return this {@link FeedbackRepresentation} object.
	 */
	def withGoalSetting(boolean withGoalSetting) {
		this.withGoalSetting = withGoalSetting
		this
	}
	
	/**
	 * Sets whether this feedback message is only applicable when the user
	 * is currently under his activity goal.
	 * 
	 * @param underGoal true if the message mentions that the user is under his goal, false otherwise.
	 * @return this {@link FeedbackRepresentation} object.
	 */
	def underGoal(boolean underGoal) {
		this.underGoal = underGoal
		this
	}
	
	/**
	 * Sets whether this feedback message is only applicable when the user
	 * is currently over his activity goal.
	 *
	 * @param overGoal true if the message mentions that the user is over his goal, false otherwise.
	 * @return this {@link FeedbackRepresentation} object.
	 */
	def overGoal(boolean overGoal) {
		this.overGoal = overGoal
		this
	}
	
	/**
	 * Sets the {@link ActivityUnit} of this {@link FeedbackRepresentation} as a String value.
	 * @param activityUnit the {@link ActivityUnit} of this {@link FeedbackRepresentation} as a String value.
	 * @return this {@link FeedbackRepresentation} object.
	 */
	def activityUnit(String activityUnit) {
		this.activityUnit = activityUnit
		this
	}
	
	/**
	 * Returns whether this {@link FeedbackRepresentation} mentions the user's current goal.
	 * @return whether this {@link FeedbackRepresentation} mentions the user's current goal.
	 */
	public boolean getWithGoalSetting() {
		return withGoalSetting;
	}
	
	/**
	 * Returns whether this feedback message is only applicable when the user
	 * is currently under his activity goal.
	 * @return whether this feedback message is only applicable when the user
	 * is currently under his activity goal.
	 */
	public boolean getUnderGoal() {
		return underGoal;
	}
	
	/**
	 * Returns whether this feedback message is only applicable when the user
	 * is currently over his activity goal.
	 * @return whether this feedback message is only applicable when the user
	 * is currently over his activity goal.
	 */
	public boolean getOverGoal() {
		return overGoal;
	}
	
	/**
	 * Returns the {@link ActivityUnit} of this {@link FeedbackRepresentation} as a String value.
	 * @return the {@link ActivityUnit} of this {@link FeedbackRepresentation} as a String value.
	 */
	public String getActivityUnit() {
		return activityUnit;
	}
}
