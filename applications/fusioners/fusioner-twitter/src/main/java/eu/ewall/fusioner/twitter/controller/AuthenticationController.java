/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.fusioner.twitter.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.ewall.fusioner.twitter.config.EnvironmentUtils;
import eu.ewall.fusioner.twitter.model.TwitterCredentials;
import eu.ewall.fusioner.twitter.model.TwitterRequestTokenWithUrl;
import eu.ewall.fusioner.twitter.model.TwitterRequestTokenWithVerifier;
import eu.ewall.fusioner.twitter.oauth.factory.TwitterAppFactory;
import eu.ewall.fusioner.twitter.service.TwitterCredentialsServiceImpl;
import twitter4j.Twitter;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * The Class AuthenticationController.
 * 
 */
@RestController
@RequestMapping(value = "/oauth")
public class AuthenticationController {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(AuthenticationController.class);

	/** The twitter credentials service. */
	@Autowired
	private TwitterCredentialsServiceImpl twitterCredentialsService;

	/**
	 * Gets the twitter request token with url.
	 *
	 * @return the twitter request token with url
	 */
	@RequestMapping(value = "/requestTokenWithUrl", method = RequestMethod.GET)
	public ResponseEntity<TwitterRequestTokenWithUrl> getTwitterRequestTokenWithUrl() {
		TwitterRequestTokenWithUrl twitterRequestTokenWithUrl = new TwitterRequestTokenWithUrl();
		Twitter twitter = TwitterAppFactory.getTwitterInstance();

		try {
			RequestToken requestToken = twitter
					.getOAuthRequestToken(EnvironmentUtils
							.getProperty("oauth.redirectUri"));

			twitterRequestTokenWithUrl.setAuthUrl(requestToken
					.getAuthorizationURL());
			twitterRequestTokenWithUrl.setRequestToken(requestToken.getToken());
			twitterRequestTokenWithUrl.setRequestTokenSecret(requestToken
					.getTokenSecret());

		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<TwitterRequestTokenWithUrl>(
					HttpStatus.BAD_REQUEST);
		}

		LOG.info("Retrieved TwitterRequestTokenUrl.");
		return new ResponseEntity<TwitterRequestTokenWithUrl>(
				twitterRequestTokenWithUrl, HttpStatus.OK);
	}

	/**
	 * Adds the or modify twitter credentials for username.
	 *
	 * @param username
	 *            the username
	 * @param twitterRequestTokenWithVerifier
	 *            the twitter request token with verifier
	 * @return the response entity
	 */
	@RequestMapping(value = "/{username}/credentials", method = RequestMethod.POST)
	public ResponseEntity<String> addOrModifyTwitterCredentialsForUsername(
			@PathVariable String username,
			@RequestBody TwitterRequestTokenWithVerifier twitterRequestTokenWithVerifier) {
		TwitterCredentials twitterCredentials;

		Twitter twitter = TwitterAppFactory.getTwitterInstance();
		RequestToken requestTokenObj = new RequestToken(
				twitterRequestTokenWithVerifier.getRequestToken(),
				twitterRequestTokenWithVerifier.getRequestTokenSecret());

		try {
			AccessToken accessTokenObj = twitter.getOAuthAccessToken(
					requestTokenObj,
					twitterRequestTokenWithVerifier.getVerifier());

			twitterCredentials = new TwitterCredentials();
			twitterCredentials.setUsername(username);
			twitterCredentials.setAccessToken(accessTokenObj.getToken());
			twitterCredentials.setAccessTokenSecret(accessTokenObj
					.getTokenSecret());

			twitterCredentialsService
					.addOrModifyEWallTwitterCredentials(twitterCredentials);

		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

		LOG.info(String.format("Posted Twitter credential for username %s.",
				username));
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	/**
	 * Do credentials exist.
	 *
	 * @param username the username
	 * @return the response entity
	 */
	@RequestMapping(value = "/{username}/credentials/exist", method = RequestMethod.GET)
	public ResponseEntity<Boolean> doCredentialsExist(@PathVariable String username) {
		boolean credentialsExist;

		try {
			credentialsExist = twitterCredentialsService.doTwitterCredentialsExistForUsername(username);

		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
		}

		LOG.info(String.format("Retrieved flag if Twitter credentials exist for username %s.", username));
		return new ResponseEntity<Boolean>(credentialsExist, HttpStatus.OK);
	}

	/**
	 * Are credentials verified.
	 *
	 * @param twitterCredentials
	 *            the twitter credentials
	 * @return the response entity
	 */
	@RequestMapping(value = "/verified", method = RequestMethod.POST)
	public ResponseEntity<Boolean> areCredentialsVerified(
			@RequestBody TwitterCredentials twitterCredentials) {
		boolean isVerified;

		Twitter twitter = TwitterAppFactory.getTwitterInstance();
		AccessToken accessTokenObj = new AccessToken(
				twitterCredentials.getAccessToken(),
				twitterCredentials.getAccessTokenSecret());
		twitter.setOAuthAccessToken(accessTokenObj);

		try {
			isVerified = twitter.verifyCredentials() != null;

		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
		}

		LOG.info("Retrieved flag if Twitter credentials are verified.");
		return new ResponseEntity<Boolean>(isVerified, HttpStatus.OK);
	}
}
