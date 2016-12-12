/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.datamanager.dao.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.hateoas.Link;

import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.service.Application;
import eu.ewall.platform.middleware.datamanager.dao.config.dbtype.EWallDBType;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;
import eu.ewall.platform.middleware.datamanager.dao.profile.UserDao;

/**
 * The Class ApplicationDao.
 */
public class ApplicationDao {

	/** The log. */
	private static final Logger LOG = LoggerFactory
			.getLogger(ApplicationDao.class);

	/** The Constant APPLICATIONS_COLLECTION. */
	public static final String APPLICATIONS_COLLECTION = "application";

	/** The mongo ops. */
	private MongoOperations mongoOps;

	/**
	 * Instantiates a new application dao.
	 */
	public ApplicationDao() {
		mongoOps = MongoDBFactory
				.getMongoOperationsForDBType(EWallDBType.SYSTEM);
	}

	/**
	 * Gets the all e wall applications.
	 *
	 * @return the all e wall applications
	 */
	public List<Application> getAllEWallApplications() {
		LOG.debug("Retrieveing all applications from mongodb.");
		return this.mongoOps
				.findAll(Application.class, APPLICATIONS_COLLECTION);

	}

	/**
	 * Gets the e wall application by name.
	 *
	 * @param applicationName
	 *            the applicationname
	 * @return the e wall application by name
	 */
	public Application getEWallApplicationByName(String applicationName) {

		LOG.debug("Retrieveing application with application name "
				+ applicationName + " from mongodb.");

		Query query = new Query(Criteria.where("name").is(applicationName));
		return this.mongoOps.findOne(query, Application.class,
				APPLICATIONS_COLLECTION);
	}

	/**
	 * Adds the e wall application.
	 *
	 * @param application
	 *            the application
	 * @return true, if successful
	 */
	public boolean addEWallApplication(Application application) {

		// if application already exists report it cannot be added
		Query queryUsername = new Query(Criteria.where("name").is(
				application.getName()));
		if ((mongoOps.count(queryUsername, APPLICATIONS_COLLECTION) > 0)) {
			return false;

		}

		mongoOps.insert(application, APPLICATIONS_COLLECTION);

		LOG.debug("New application with application name "
				+ application.getName() + " added to mongodb.");

		return true;

	}

	/**
	 * Check if some user contained deleted application and if yes remove
	 * connection.
	 *
	 * @param applicationName
	 *            the application name
	 * @return true, if successful
	 */
	public boolean checkIfSomeUserContainedDeletedApplicationAndIfYesRemoveConnection(
			String applicationName) {
		boolean returnStatus = false;

		// get the list of all users
		UserDao userMongo = new UserDao();
		List<User> allUsers = userMongo.getAllEWallUsers();
		// go through all users
		for (User each : allUsers) {

			// if User contains the connection to the application delete the
			// connection
			if (each.getApplicationNamesList().contains(applicationName)) {

				// save change in User
				returnStatus = each
						.removeApplicationNameFromEncompassingApplicationNamesList(applicationName);
				this.mongoOps.save(each, UserDao.USERS_COLLECTION);

				LOG.debug("Deleting application name also from the User: "
						+ each.getUsername() + " from mongodb.");
			}
		}
		return returnStatus;

	}

	/**
	 * Delete application with name.
	 *
	 * @param applicationName
	 *            the application name
	 * @return true, if successful
	 */
	public boolean deleteApplicationWithName(String applicationName) {

		LOG.debug("Deleting application with application name "
				+ applicationName + " from mongodb.");

		Query query = new Query(Criteria.where("name").is(applicationName));
		this.mongoOps.remove(query, Application.class, APPLICATIONS_COLLECTION);

		checkIfSomeUserContainedDeletedApplicationAndIfYesRemoveConnection(applicationName);

		return true;

	}

	/**
	 * Modify e wall application.
	 *
	 * @param appName
	 *            the app name
	 * @param newApplication
	 *            the application
	 * @return the write result
	 */
	public boolean modifyEWallApplicationWithName(String appName,
			Application newApplication) {

		if (appName == null || newApplication == null)
			return false;

		Query query = new Query(Criteria.where("name").is(appName));
		Application appFromDB = mongoOps.findOne(query, Application.class,
				APPLICATIONS_COLLECTION);

		if (appFromDB == null) {
			addEWallApplication(newApplication);
			return true;
		}

		// update all except name (which is unique)
		// update type
		appFromDB.setType(newApplication.getType());
		List<Link> newLinks = newApplication.getLinks();
		appFromDB.removeLinks();
		appFromDB.add(newLinks);

		// update list of services (if defined)
		if (newApplication.getEncompassingServiceNames() != null
				&& !newApplication.getEncompassingServiceNames().isEmpty())
			appFromDB.setEncompassingServiceNames(newApplication
					.getEncompassingServiceNames());

		this.mongoOps.save(appFromDB, APPLICATIONS_COLLECTION);

		LOG.debug("Application with application name " + appName
				+ " modified in mongodb.");

		return true;

	}

	/**
	 * Collection exits.
	 *
	 * @return true, if successful
	 */
	public boolean collectionExits() {
		return mongoOps.collectionExists(APPLICATIONS_COLLECTION);
	}
}
