/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.fusioner.twitter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import eu.ewall.fusioner.twitter.dao.mongo.TwitterCredentialsMongo;
import eu.ewall.fusioner.twitter.dao.mongo.factory.MongoDBFactory;
import eu.ewall.fusioner.twitter.model.TwitterCredentials;

/**
 * The Class TwitterCredentialsServiceImpl.
 */
@Service("twitterCredentialsServiceImpl")
public class TwitterCredentialsServiceImpl implements DisposableBean {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(TwitterCredentialsServiceImpl.class);

	/** The exercise videos suggestions mongo. */
	private TwitterCredentialsMongo twitterCredentialsMongo;

	/**
	 * Instantiates a new twitter credentials service impl.
	 */
	public TwitterCredentialsServiceImpl() {
		this.twitterCredentialsMongo = new TwitterCredentialsMongo();
	}

	/**
	 * Gets the e wall twitter credentials for username.
	 *
	 * @param username the username
	 * @return the e wall twitter credentials for username
	 */
	public TwitterCredentials getEWallTwitterCredentialsForUsername(
			String username) {
		LOG.debug("getEWallTwitterCredentialsForUsername");
		return twitterCredentialsMongo
				.getEWallTwitterCredentialsForUsername(username);
	}

	/**
	 * Adds the or modify e wall twitter credentials.
	 *
	 * @param twitterCredentials the twitter credentials
	 */
	public void addOrModifyEWallTwitterCredentials(
			TwitterCredentials twitterCredentials) {
		LOG.debug("addOrModifyEWallTwitterCredentials");
		if (doTwitterCredentialsExistForUsername(twitterCredentials.getUsername())) {
			twitterCredentialsMongo.modifyEWallTwitterCredentials(twitterCredentials);
		} else {
			twitterCredentialsMongo.addEWallTwitterCredentials(twitterCredentials);
		}
	}

	/**
	 * Gets the twitter credentials mongo.
	 *
	 * @return the twitter credentials mongo
	 */
	public TwitterCredentialsMongo getTwitterCredentialsMongo() {
		return twitterCredentialsMongo;
	}

	/**
	 * Sets the twitter credentials mongo.
	 *
	 * @param twitterCredentialsMongo the new twitter credentials mongo
	 */
	public void setTwitterCredentialsMongo(
			TwitterCredentialsMongo twitterCredentialsMongo) {
		this.twitterCredentialsMongo = twitterCredentialsMongo;
	}

	/**
	 * Do twitter credentials exist for username.
	 *
	 * @param username the username
	 * @return true, if successful
	 */
	public boolean doTwitterCredentialsExistForUsername(String username) {
		return (getEWallTwitterCredentialsForUsername(username) != null);
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() throws Exception {
		MongoDBFactory.close();
	}
}
