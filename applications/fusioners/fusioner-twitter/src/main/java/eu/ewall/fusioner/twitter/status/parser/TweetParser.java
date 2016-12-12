/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.fusioner.twitter.status.parser;

import twitter4j.Status;
import eu.ewall.fusioner.twitter.model.Tweet;

/**
 * The Class TweetParser.
 */
public class TweetParser {

	/**
	 * Parses the twitter message.
	 *
	 * @param status
	 *            the status
	 * @return the tweet
	 */
	public static Tweet parseTwitterMessage(Status status) {
		Tweet tweet = new Tweet();

		tweet.setText(status.getText());
		tweet.setCreatedAt(status.getCreatedAt());

		return tweet;
	}
}
