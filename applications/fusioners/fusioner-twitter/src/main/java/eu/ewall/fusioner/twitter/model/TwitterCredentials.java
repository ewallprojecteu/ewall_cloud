/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.fusioner.twitter.model;

/**
 * The Class TwitterCredentials.
 */
public class TwitterCredentials {

	/** The username. */
	private String username;

	/** The access token. */
	private String accessToken;

	/** The access token secret. */
	private String accessTokenSecret;

	/**
	 * Instantiates a new twitter credentials.
	 */
	public TwitterCredentials() {
	}
	
	/**
	 * Instantiates a new twitter credentials.
	 *
	 * @param username the username
	 * @param accessToken the access token
	 * @param accessTokenSecret the access token secret
	 */
	public TwitterCredentials(String username, String accessToken,
			String accessTokenSecret) {
		this.username = username;
		this.accessToken = accessToken;
		this.accessTokenSecret = accessTokenSecret;
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
	 * Gets the access token.
	 *
	 * @return the access token
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * Sets the access token.
	 *
	 * @param accessToken
	 *            the new access token
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * Gets the access token secret.
	 *
	 * @return the access token secret
	 */
	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}

	/**
	 * Sets the access token secret.
	 *
	 * @param accessTokenSecret
	 *            the new access token secret
	 */
	public void setAccessTokenSecret(String accessTokenSecret) {
		this.accessTokenSecret = accessTokenSecret;
	}
}
