package eu.ewall.platform.reasoner.activitycoach.service.messages

/**
 * Message content class for secondary intention 'suggestion'.
 * 
 * @author Dennis Hofs (RRD)
 */
class SuggestionRepresentation extends BaseRepresentation {
	/**
	 * Defines whether this message is a lifestyle suggestion (true) or an
	 * activity suggestion (false). Default: false.
	 */
	boolean lifestyle = false

	/**
	 * Sets whether this message is a lifestyle suggestion (true) or an
	 * activity suggestion (false).
	 * 	
	 * @param lifestyle true if this message is a lifestyle suggestion, false
	 * if this message is an activity suggestion
	 * @return this suggestion content object
	 */
	def lifestyle(boolean lifestyle) {
		this.lifestyle = lifestyle
		this
	}
}
