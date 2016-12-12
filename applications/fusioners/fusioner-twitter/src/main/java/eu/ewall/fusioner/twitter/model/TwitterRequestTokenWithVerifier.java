/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.fusioner.twitter.model;

/**
 * The Class TwitterRequestTokenWithVerifier.
 */
public class TwitterRequestTokenWithVerifier {

	/** The request token. */
	private String requestToken;
	
	/** The request token secret. */
	private String requestTokenSecret;
	
	/** The verifier. */
	private String verifier;

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
	 * Gets the verifier.
	 *
	 * @return the verifier
	 */
	public String getVerifier() {
		return verifier;
	}

	/**
	 * Sets the verifier.
	 *
	 * @param verifier the new verifier
	 */
	public void setVerifier(String verifier) {
		this.verifier = verifier;
	}
}
