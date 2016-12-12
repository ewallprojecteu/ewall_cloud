package eu.ewall.platform.idss.service.model.common;

/**
 * Response type for idss-automatic-goal-setting request /activitygoal. This
 * class wraps around the actual response, which may be null.
 * 
 * @author Dennis Hofs (RRD)
 */
public class ActivityGoalResponse extends ServiceResponse<ActivityGoal> {
	public ActivityGoalResponse() {
	}

	public ActivityGoalResponse(ActivityGoal value) {
		setValue(value);
	}
}
