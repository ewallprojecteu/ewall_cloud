/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.idss.wellbeingads.model.lastinteraction;

import org.joda.time.DateTime;

/**
 * The Class LastIntercationResponse.
 */
public class LastIntercationResponse {
	/** The value. */
	private String value = null;

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets the date time value.
	 *
	 * @return the date time value
	 */
	public DateTime getDateTimeValue() {
		if (value != null)
			return DateTime.parse(value);
		
		return null;
	}
}
