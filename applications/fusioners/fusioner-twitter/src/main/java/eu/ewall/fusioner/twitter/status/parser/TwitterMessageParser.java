/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.fusioner.twitter.status.parser;

import twitter4j.Status;
import twitter4j.User;
import eu.ewall.fusioner.twitter.model.TweetCreator;
import eu.ewall.fusioner.twitter.model.TweetData;

/**
 * The Class TwitterMessageParser.
 * 
 */
public class TwitterMessageParser {

	/**
	 * Parses the twitter message.
	 *
	 * @param tweet
	 *            the tweet
	 * @return the tweet data
	 */
	public static TweetData parseTwitterMessage(Status tweet) {
		TweetData tweetData = new TweetData();
		TweetCreator tweetCreator = new TweetCreator();
		User user = tweet.getUser();

		/* General tweet info */
		tweetData.setCreatedAt(tweet.getCreatedAt());
		tweetData.setLang(tweet.getLang());
		tweetData.setRetweeted(tweet.isRetweeted());
		tweetData.setText(tweet.getText());
		tweetData.setTweetCreator(tweetCreator);

		/* Tweet creator info */
		tweetCreator.setDefaultProfileImage(user.isDefaultProfileImage());
		tweetCreator.setFollowRequestSent(user.isFollowRequestSent());
		tweetCreator.setGeoEnabled(user.isGeoEnabled());
		tweetCreator.setId(user.getId());
		tweetCreator.setLang(user.getLang());
		tweetCreator.setProfileImageUrl(user.getProfileImageURL());
		tweetCreator.setTimeZone(user.getTimeZone());
		tweetCreator.setUserName(user.getName());
		tweetCreator.setUserScreenName(user.getScreenName());

		return tweetData;
	}
}
