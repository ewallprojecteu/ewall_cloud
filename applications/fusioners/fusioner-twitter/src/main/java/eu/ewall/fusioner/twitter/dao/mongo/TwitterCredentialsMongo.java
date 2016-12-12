/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.fusioner.twitter.dao.mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import eu.ewall.fusioner.twitter.dao.mongo.factory.MongoDBFactory;
import eu.ewall.fusioner.twitter.model.TwitterCredentials;

/**
 * The Class TwitterCredentialsMongo.
 */
public class TwitterCredentialsMongo {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(TwitterCredentialsMongo.class);

	/** The Constant TWITTER_CREDENTIALS_COLLECTION. */
	public static final String TWITTER_CREDENTIALS_COLLECTION = "twitterCredentialsCollection";

	/** The mongo ops. */
	private MongoOperations mongoOps;

	/**
	 * Instantiates a new twitter credentials mongo.
	 */
	public TwitterCredentialsMongo() {
		this.mongoOps = MongoDBFactory.getMongoOperations();
	}

	/**
	 * Gets the e wall twitter credentials for username.
	 *
	 * @param username the username
	 * @return the e wall twitter credentials for username
	 */
	public TwitterCredentials getEWallTwitterCredentialsForUsername(
			String username) {
		LOG.debug(String
				.format("Retrieveing Twitter credentials for username %s from mongodb.",
						username));
		Query query = new Query(Criteria.where("username").is(username));
		return this.mongoOps.findOne(query, TwitterCredentials.class,
				TWITTER_CREDENTIALS_COLLECTION);
	}

	/**
	 * Adds the e wall twitter credentials.
	 *
	 * @param twitterCredentials the twitter credentials
	 */
	public void addEWallTwitterCredentials(TwitterCredentials twitterCredentials) {
		LOG.info("Adding Twitter credentials for username "
				+ twitterCredentials.getUsername() + " to mongodb.");
		mongoOps.insert(twitterCredentials, TWITTER_CREDENTIALS_COLLECTION);
	}

	/**
	 * Modify e wall twitter credentials.
	 *
	 * @param twitterCredentials the twitter credentials
	 */
	public void modifyEWallTwitterCredentials(TwitterCredentials twitterCredentials) {
		LOG.info("Modifying Twitter credentials for username "
				+ twitterCredentials.getUsername() + " in mongodb.");
		Update update = new Update();
		update.set("accessToken", twitterCredentials.getAccessToken());
		update.set("accessTokenSecret",
				twitterCredentials.getAccessTokenSecret());

		Query query = new Query(Criteria.where("username").is(
				twitterCredentials.getUsername()));
		mongoOps.updateFirst(query, update, TwitterCredentials.class,
				TWITTER_CREDENTIALS_COLLECTION);
	}
}