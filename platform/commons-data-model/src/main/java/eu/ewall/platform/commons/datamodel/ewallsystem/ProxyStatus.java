/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.ewallsystem;

/**
 * The Enum ProxyStatus.
 *
 * @author emirmos
 */
public enum ProxyStatus {

	/** The not initialized. */
	NOT_INITIALIZED,
	/** The offline. */
	OFFLINE,
	/** The online. */
	ONLINE,
	/** The busy. */
	BUSY,
	/** The failure. */
	FAILURE;

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
	 * @return the proxy status
	 */
	public static ProxyStatus value(String v) {
		return valueOf(v);
	}
}
