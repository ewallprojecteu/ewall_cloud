package eu.ewall.platform.reasoner.activitycoach.service.messages

/**
 * Message representation class for 'greeting'.
 * 
 * @author Harm op den Akker (RRD)
 */
class GreetingRepresentation extends BaseRepresentation {

	private boolean withFirstName = false
	private String dayPart = null;
	
	/**
	 * Sets whether this greeting addresses the user by first name.
	 * 
	 * @param withFirstName true if this greeting addresses the user by first name, false otherwise.
	 * @return this {@link GreetingRepresentation} object.
	 */
	def withFirstName(boolean withFirstName) {
		this.withFirstName = withFirstName
		this
	}
	/**
	 * Sets the {@link DayPart} for which this {@link GreetingRepresentation} is appropriate as 
	 * a {@link String} value.
	 * @param dayPart the {@link DayPart} for which this {@link GreetingRepresentation} is appropriate as 
	 * a {@link String} value.
	 * @return this {@link GreetingRepresentation} object.
	 */
	def dayPart(String dayPart) {
		this.dayPart = dayPart
		this
	}
	
	public boolean getWithFirstName() {
		return withFirstName;
	}
	
	public String getDayPart() {
		return dayPart;
	}
	
}
