/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.authentication;

/**
 * The Class UserCredentials.
 */
public class UserCredentialsEwall {

	/** unique user identifier in the system. */
	private String username;

	/** unique user password in the system. */
	private String hashedPassword;

	/**
	 * Instantiates a new user credentials ewall.
	 *
	 * @param username the username
	 * @param hashedPassword the hashed password
	 */
	public UserCredentialsEwall(String username, String hashedPassword) {
		this.username = username;
		this.hashedPassword = hashedPassword;
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 *
	 * @param username
	 *            the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the hashed password.
	 *
	 * @return the hashed password
	 */
	public String getHashedPassword() {
		return hashedPassword;
	}

	/**
	 * Sets the hashed password.
	 *
	 * @param hashedPassword
	 *            the new hashed password
	 */
	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

}
