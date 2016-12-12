package eu.ewall.platform.reasoner.activitycoach.service.messages

/**
 * Message representation class for 'reinforcement'.
 * 
 * @author Dennis Hofs (RRD)
 * @author Harm op den Akker (RRD)
 */
class ReinforcementRepresentation extends BaseRepresentation {
	
	private String reinforcementIntention = null;
	private boolean closeToGoal;
	//private boolean funny;
	
	/**
	 * Sets the specific intention of this reinforcement as either {@link PrimaryIntention.NEUTRAL}, 
	 * {@link PrimaryIntention.ENCOURAGE}, or {@link PrimaryIntention.DISCOURAGE}.
	 *
	 * @param reinforcementIntention the specific intention for this reinforcement
	 * @return this {@link ReinforcementRepresentation} object.
	 */
	def reinforcementIntention(String reinforcementIntention) {
		this.reinforcementIntention = reinforcementIntention
		this
	}
	
	/**
	 * Defines whether or not this reinforcement message mentions that the user is currently
	 * close to achieving his/her goal.
	 * @param closeToGoal true if the message specifies closeness to the goal, false otherwise.
	 * @return this {@link ReinforcementRepresentation} object.
	 */
	def closeToGoal(boolean closeToGoal) {
		this.closeToGoal = closeToGoal
		this
	}
	
	public String getReinforcementIntention() {
		return reinforcementIntention;
	}
	
	public boolean getCloseToGoal() {
		return closeToGoal;
	}
	
	/**
	 * Defines whether or not this reinforcement message is (supposed to be) funny.
	 * @param funny true if this reinforcement message is supposed to be funny, false otherwise.
	 * @return this {@link ReinforcementRepresentation} object.
	 */
	//def funny(boolean funny) {
	//	this.funny = funny
	//	this
	//}
	
}
