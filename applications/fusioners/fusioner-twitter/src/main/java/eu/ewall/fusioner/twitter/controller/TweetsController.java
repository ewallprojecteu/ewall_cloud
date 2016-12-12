/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.fusioner.twitter.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterStream;
import twitter4j.UserStreamAdapter;
import twitter4j.UserStreamListener;
import twitter4j.auth.AccessToken;
import eu.ewall.fusioner.twitter.config.EnvironmentUtils;
import eu.ewall.fusioner.twitter.model.Tweet;
import eu.ewall.fusioner.twitter.model.TweetData;
import eu.ewall.fusioner.twitter.model.TwitterCredentials;
import eu.ewall.fusioner.twitter.oauth.factory.TwitterAppFactory;
import eu.ewall.fusioner.twitter.service.TwitterCredentialsServiceImpl;
import eu.ewall.fusioner.twitter.status.TwitterStatusGetter;
import eu.ewall.fusioner.twitter.status.parser.TwitterMessageParser;

/**
 * The Class TweetsController.
 */
@RestController
@RequestMapping(value = "/tweets")
public class TweetsController {

	/** The log. */
	private final Logger LOG = LoggerFactory.getLogger(TweetsController.class);

	/** The twitter credentials service. */
	@Autowired
	private TwitterCredentialsServiceImpl twitterCredentialsService;

	/**
	 * Gets the tweets for username and timestamp.
	 *
	 * @param username
	 *            the username
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @return the tweets for username and timestamp
	 */
	@RequestMapping(value = "/{username}/timestamp", method = RequestMethod.GET)
	public ResponseEntity<List<Tweet>> getTweetsForUsernameAndTimestamp(
			@PathVariable String username, @RequestParam("from") long from,
			@RequestParam("to") long to) {

		List<Tweet> tweetsCollection;

		Twitter twitterRest = TwitterAppFactory.getTwitterInstance();
		TwitterCredentials twitterCredentials = twitterCredentialsService
				.getEWallTwitterCredentialsForUsername(username);

		AccessToken accessTokenObj = new AccessToken(
				twitterCredentials.getAccessToken(),
				twitterCredentials.getAccessTokenSecret());
		twitterRest.setOAuthAccessToken(accessTokenObj);

		final int MAX_TWEETS_COUNT = 3200;
		final int PAGE_COUNT = 200;

		TwitterStatusGetter twitterStatusGetter = new TwitterStatusGetter(
				twitterRest, MAX_TWEETS_COUNT, PAGE_COUNT);

		try {
			long userId = twitterRest.getId();
			tweetsCollection = twitterStatusGetter
					.getTweetsForUsernameAndIdPostedFromToTimestamps(username,
							userId, from, to);

		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<List<Tweet>>(HttpStatus.BAD_REQUEST);
		}

		LOG.info(String.format("Getting statuses for username %s.", username));
		return new ResponseEntity<List<Tweet>>(tweetsCollection, HttpStatus.OK);
	}

	/**
	 * Post status for username.
	 *
	 * @param username
	 *            the username
	 * @param statusText
	 *            the status text
	 * @return the response entity
	 */
	@RequestMapping(value = "/{username}/update", method = RequestMethod.POST)
	public ResponseEntity<String> postStatusForUsername(
			@PathVariable String username, @RequestBody String statusText) {

		Twitter twitterRest = TwitterAppFactory.getTwitterInstance();
		TwitterCredentials twitterCredentials = twitterCredentialsService
				.getEWallTwitterCredentialsForUsername(username);

		AccessToken accessTokenObj = new AccessToken(
				twitterCredentials.getAccessToken(),
				twitterCredentials.getAccessTokenSecret());
		twitterRest.setOAuthAccessToken(accessTokenObj);

		try {
			twitterRest.updateStatus(statusText);

		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

		LOG.info(String.format("Posting status for username %s.", username));
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	/**
	 * Streaming statuses (messages coming from logged in Twitter user and user
	 * followers based on 'status.fromAuthUserOnly' flag in properties files).
	 *
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return the response entity
	 */
	@RequestMapping(value = "/{username}/stream", method = RequestMethod.GET)
	public ResponseEntity<String> streamStatuses(@PathVariable String username) {

		LOG.info("streaming statuses");

		final TwitterStream twitterStreaming = TwitterAppFactory
				.getTwitterStreamInstance();
		TwitterCredentials twitterCredentials = twitterCredentialsService
				.getEWallTwitterCredentialsForUsername(username);

		AccessToken accessTokenObj = new AccessToken(
				twitterCredentials.getAccessToken(),
				twitterCredentials.getAccessTokenSecret());
		twitterStreaming.setOAuthAccessToken(accessTokenObj);

		try {
			UserStreamListener userStreamListener = new UserStreamAdapter() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see twitter4j.StatusAdapter#onStatus(twitter4j.Status)
				 */
				@Override
				public void onStatus(Status status) {
					TweetData tweetData = TwitterMessageParser
							.parseTwitterMessage(status);
					try {
						logUserStatusBasedOnConfigFile(twitterStreaming,
								tweetData);
					} catch (Exception e) {
						LOG.warn(e.getMessage());
					}
				}

				private void logUserStatusBasedOnConfigFile(
						final TwitterStream twitterStreaming,
						TweetData tweetData) throws Exception {

					boolean onlyFromAuthUser = EnvironmentUtils
							.getFlagOnlyListenForAuthUserStatuses();

					/*
					 * Writes message to log based on flag set in properties
					 * file;
					 * 
					 * If set to write only messages from authenticated user, it
					 * checked if screen name of message creator and
					 * authenticated user are the same
					 */
					if (!onlyFromAuthUser
							|| (onlyFromAuthUser && tweetData.getTweetCreator()
									.getUserScreenName()
									.equals(twitterStreaming.getScreenName()))) {
						LOG.info(tweetData.toString());
					}
				}
			};
			twitterStreaming.addListener(userStreamListener);
			twitterStreaming.user();

		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

		LOG.info(String.format("Opened stream for username %s.", username));
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
