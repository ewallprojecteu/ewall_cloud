package eu.ewall.platform.commons.datamodel.calendar;

/**
 * The Class SelfcareEvent.
 */
public class SelfcareEvent extends Event {

	/** The type. */
	public SelfCareType type;

	/** The location. */
	public EventLocation location;

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public SelfCareType getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type
	 *            the new type
	 */
	public void setType(SelfCareType type) {
		this.type = type;
	}

	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public EventLocation getLocation() {
		return location;
	}

	/**
	 * Sets the location.
	 *
	 * @param location
	 *            the new location
	 */
	public void setLocation(EventLocation location) {
		this.location = location;
	}


}
