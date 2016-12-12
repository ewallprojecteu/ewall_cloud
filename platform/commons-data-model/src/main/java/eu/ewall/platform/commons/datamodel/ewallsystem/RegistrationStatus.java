/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.ewallsystem;

/**
 * The Enum RegistrationStatus.
 *
 * @author emirmos
 */
public enum RegistrationStatus {

	/** The not registered. */
	NOT_REGISTERED,
	/** The registered. */
	REGISTERED;

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
	 * @return the sensing environment status
	 */
	public static RegistrationStatus value(String v) {
		return valueOf(v);
	}
}
