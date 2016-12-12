/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.datamanager.dao.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import eu.ewall.platform.commons.datamodel.authentication.UserCredentialsEwall;
import eu.ewall.platform.middleware.datamanager.dao.config.dbtype.EWallDBType;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;

/**
 * The Class UserCredentialsDao.
 */
public class UserCredentialsDao {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(UserCredentialsDao.class);

	/** The Constant USERS_CREDENTIALS. */
	public static final String USERS_CREDENTIALS = "userCredentialsEwall";

	/** The mongo ops. */
	private MongoOperations mongoOps;

	/**
	 * Instantiates a new user credentials dao.
	 */
	public UserCredentialsDao() {
		mongoOps = MongoDBFactory
				.getMongoOperationsForDBType(EWallDBType.SYSTEM);
	}

	/**
	 * Gets the user credentials for username.
	 *
	 * @param username
	 *            the username
	 * @return the user credentials for username
	 */
	public UserCredentialsEwall getUserCredentialsEwallForUsername(
			String username) {

		Query query = new Query(Criteria.where("username").is(username));
		return this.mongoOps.findOne(query, UserCredentialsEwall.class,
				USERS_CREDENTIALS);
	}

	/**
	 * Adds the user credentials ewall.
	 *
	 * @param userCredentials
	 *            the user credentials
	 */
	public void addUserCredentialsEwall(UserCredentialsEwall userCredentials) {
		mongoOps.insert(userCredentials, USERS_CREDENTIALS);

		LOG.info("New userCredentials for username "
				+ userCredentials.getUsername() + " added to mongodb.");
	}

	/**
	 * Delete user credentials for username.
	 *
	 * @param username
	 *            the username
	 * @return true, if successful
	 */
	public boolean deleteUserCredentialsEwallForUsername(String username) {

		Query query = new Query(Criteria.where("username").is(username));
		this.mongoOps.remove(query, UserCredentialsEwall.class,
				USERS_CREDENTIALS);

		LOG.debug("UserCredentialsEwall  for username " + username
				+ "deleted from mongodb.");

		return true;

	}

	/**
	 * Modify user credentials ewall for username.
	 *
	 * @param username
	 *            the username
	 * @param newPasswordEncoded
	 *            the new password encoded
	 * @return true, if successful
	 */
	public boolean modifyUserCredentialsEwallForUsername(String username,
			String newPasswordEncoded) {

		Update update = new Update();
		// If a collection contains no documents, this inserts a
		// document
		update.set("hashedPassword", newPasswordEncoded);

		Query query = new Query(Criteria.where("username").is(username));
		this.mongoOps.updateMulti(query, update, UserCredentialsEwall.class,
				USERS_CREDENTIALS);
		return true;

	}
}
