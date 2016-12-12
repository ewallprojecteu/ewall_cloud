package eu.ewall.platform.commons.datamodel.calendar;

/**
 * The Class SocialEvent.
 */
public class SocialEvent extends Event {

	/** The event name. */
	public String eventName;

	/** The location. */
	public EventLocation location;

	/** The wallet. */
	public Boolean wallet;

	/** The keys. */
	public Boolean keys;

	/** The other belongings. */
	public String otherBelongings;

	/** The transportation. */
	public String transportation;

	/**
	 * Gets the event name.
	 *
	 * @return the event name
	 */
	public String getEventName() {
		return eventName;
	}

	/**
	 * Sets the event name.
	 *
	 * @param eventName
	 *            the new event name
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
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

	/**
	 * Gets the wallet.
	 *
	 * @return the wallet
	 */
	public Boolean getWallet() {
		return wallet;
	}

	/**
	 * Sets the wallet.
	 *
	 * @param wallet
	 *            the new wallet
	 */
	public void setWallet(Boolean wallet) {
		this.wallet = wallet;
	}

	/**
	 * Gets the keys.
	 *
	 * @return the keys
	 */
	public Boolean getKeys() {
		return keys;
	}

	/**
	 * Sets the keys.
	 *
	 * @param keys
	 *            the new keys
	 */
	public void setKeys(Boolean keys) {
		this.keys = keys;
	}

	/**
	 * Gets the other belongings.
	 *
	 * @return the other belongings
	 */
	public String getOtherBelongings() {
		return otherBelongings;
	}

	/**
	 * Sets the other belongings.
	 *
	 * @param otherBelongings
	 *            the new other belongings
	 */
	public void setOtherBelongings(String otherBelongings) {
		this.otherBelongings = otherBelongings;
	}

	/**
	 * Gets the transportation.
	 *
	 * @return the transportation
	 */
	public String getTransportation() {
		return transportation;
	}

	/**
	 * Sets the transportation.
	 *
	 * @param transportation
	 *            the new transportation
	 */
	public void setTransportation(String transportation) {
		this.transportation = transportation;
	}

}
