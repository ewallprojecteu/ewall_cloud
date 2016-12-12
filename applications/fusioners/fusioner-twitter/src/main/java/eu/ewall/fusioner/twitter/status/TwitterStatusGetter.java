/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.fusioner.twitter.status;

import java.util.ArrayList;
import java.util.List;

import eu.ewall.fusioner.twitter.model.Tweet;
import eu.ewall.fusioner.twitter.status.parser.TweetParser;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;

/**
 * The Class TwitterStatusGetter.
 */
public class TwitterStatusGetter {

	/** The twitter rest. */
	private Twitter twitterRest;
	
	/** The max tweets count. */
	private int maxTweetsCount;
	
	/** The page count. */
	private int pageCount;

	/**
	 * Instantiates a new twitter status getter.
	 *
	 * @param twitterRest the twitter rest
	 * @param maxTweetsCount the max tweets count
	 * @param pageCount the page count
	 */
	public TwitterStatusGetter(Twitter twitterRest, int maxTweetsCount,
			int pageCount) {
		this.twitterRest = twitterRest;
		this.maxTweetsCount = maxTweetsCount;
		this.pageCount = pageCount;
	}

	/**
	 * Gets the tweets for username posted from to timestamps.
	 * 
	 * For better understanding algorithm check Twitter's documentation at links
	 * 'https://dev.twitter.com/rest/public/timelines' and
	 * 'https://dev.twitter.com/rest/reference/get/statuses/user_timeline'
	 *
	 * @param username the username
	 * @param from the from
	 * @param to the to
	 * @return the tweets for username posted from to timestamps
	 * @throws Exception the exception
	 */
	public List<Tweet> getTweetsForUsernameAndIdPostedFromToTimestamps(
			String username, long userId, long from, long to) throws Exception {
		List<Tweet> tweetsCollection = new ArrayList<Tweet>();

		int pageCounter = 1;
		long maxId;

		Paging paging = new Paging(pageCounter++, pageCount);
		List<Status> statuses = twitterRest.getUserTimeline(userId, paging);
		maxId = parseAndAddStatusesToTweetCollectionForTimestamp(statuses,
				tweetsCollection, from, to);

		/*
		 * Get tweets from next page if previous page was full and if sum of
		 * retrieved tweets is less or equals than max tweets that can be
		 * retrieved
		 */
		while (statuses.size() == pageCount
				&& (pageCounter * pageCount) <= maxTweetsCount) {
			paging = new Paging(pageCounter++, pageCount);
			paging.setMaxId(maxId - 1);
			statuses = twitterRest.getUserTimeline(userId, paging);
			maxId = parseAndAddStatusesToTweetCollectionForTimestamp(statuses,
					tweetsCollection, from, to);
		}

		addUsernameToTweetsInCollection(username, tweetsCollection);
		return tweetsCollection;
	}

	/**
	 * Parses the and add statuses to tweet collection for timestamp.
	 *
	 * @param statuses the statuses
	 * @param tweetsCollection the tweets collection
	 * @param from the from
	 * @param to the to
	 * @return the long
	 */
	private long parseAndAddStatusesToTweetCollectionForTimestamp(
			List<Status> statuses, List<Tweet> tweetsCollection, long from,
			long to) {
		long maxId = 0;

		for (Status status : statuses) {
			Tweet tweet = TweetParser.parseTwitterMessage(status);
			long tweetCreatedTime = tweet.getCreatedAt().getTime();
			if (tweetCreatedTime >= from && tweetCreatedTime <= to) {
				tweetsCollection.add(tweet);
			}
			maxId = status.getId();
		}

		return maxId;
	}

	/**
	 * Adds the username to tweets in collection.
	 *
	 * @param username the username
	 * @param tweetsCollection the tweets collection
	 */
	private void addUsernameToTweetsInCollection(String username,
			List<Tweet> tweetsCollection) {
		for (Tweet tweet : tweetsCollection) {
			tweet.setUsername(username);
		}
	}
}
