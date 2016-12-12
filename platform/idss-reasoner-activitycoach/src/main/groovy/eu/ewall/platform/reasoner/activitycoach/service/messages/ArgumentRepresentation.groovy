package eu.ewall.platform.reasoner.activitycoach.service.messages

/**
 * Message content class for secondary intention 'argument'.
 * 
 * @author Dennis Hofs (RRD)
 * @author Harm op den Akker (RRD)
 */
class ArgumentRepresentation extends BaseRepresentation {
	
	private String goalIntention;
	
	/**
	 * Sets the {@link GoalIntention} of this {@link ArgumentRepresentation} as a String value.
	 * @param goalIntention the {@link GoalIntention} of this {@link ArgumentRepresentation} as a String value.
	 * @return this {@link ArgumentRepresentation} object.
	 */
	def goalIntention(String goalIntention) {
		this.goalIntention = goalIntention
		this
	}
	
	public String getGoalIntention() {
		return goalIntention;
	}
}
