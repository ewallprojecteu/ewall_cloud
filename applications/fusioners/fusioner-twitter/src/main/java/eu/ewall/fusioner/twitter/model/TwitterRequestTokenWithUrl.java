/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.fusioner.twitter.model;

/**
 * The Class TwitterRequestTokenWithUrl.
 */
public class TwitterRequestTokenWithUrl {

	/** The request token. */
	private String requestToken;
	
	/** The request token secret. */
	private String requestTokenSecret;
	
	/** The auth url. */
	private String authUrl;

	/**
	 * Gets the request token.
	 *
	 * @return the request token
	 */
	public String getRequestToken() {
		return requestToken;
	}

	/**
	 * Sets the request token.
	 *
	 * @param requestToken the new request token
	 */
	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}

	/**
	 * Gets the request token secret.
	 *
	 * @return the request token secret
	 */
	public String getRequestTokenSecret() {
		return requestTokenSecret;
	}

	/**
	 * Sets the request token secret.
	 *
	 * @param requestTokenSecret the new request token secret
	 */
	public void setRequestTokenSecret(String requestTokenSecret) {
		this.requestTokenSecret = requestTokenSecret;
	}

	/**
	 * Gets the auth url.
	 *
	 * @return the auth url
	 */
	public String getAuthUrl() {
		return authUrl;
	}

	/**
	 * Sets the auth url.
	 *
	 * @param authUrl the new auth url
	 */
	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}
}
