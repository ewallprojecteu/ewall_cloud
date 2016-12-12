/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.ewallsystem;

/**
 * The Enum LongPollMessageType.
 *
 * @author emirmos
 */
public enum LongPollMessageType {

	/** No new requests for remote proxy available on cloud gateway. */
	NO_NEW_REQUESTS,
	
	/** New request available: get remote proxy configuration. */
	GET_CONFIG,
	
	/** New request available: set remote proxy configuration. */
	SET_CONFIG,
	
	/** The update actuator control. */
	UPDATE_ACTUATOR_COMMAND,
	
	/** Empty remote proxy long polling request to cloud gateway. */
	REQUEST_WITHOUT_DATA,
	
	/** Remote proxy long polling request to cloud gateway with response data. */
	REQUEST_WITH_DATA,
	
	/** Remote proxy failed to execute requested action. */
	REQUEST_WITH_ERROR;

	/**
	 * Value.
	 *
	 * @return the string
	 */
	public String value() {
		return name();
	}

	/**
	 * Value.
	 *
	 * @param v
	 *            the v
	 * @return the long poll message type
	 */
	public static LongPollMessageType value(String v) {
		return valueOf(v);
	}
}
