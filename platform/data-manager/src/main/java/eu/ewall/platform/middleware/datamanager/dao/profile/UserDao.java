/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.datamanager.dao.profile;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.profile.UserRole;
import eu.ewall.platform.middleware.datamanager.dao.config.dbtype.EWallDBType;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;

/**
 * The Class UserDao.
 */
public class UserDao {

	/** The log. */
	private static final Logger LOG = LoggerFactory.getLogger(UserDao.class);

	/** The Constant USERS_COLLECTION. */
	public static final String USERS_COLLECTION = "user";

	/** The mongo ops. */
	private MongoOperations mongoOps;

	public UserDao() {
		mongoOps = MongoDBFactory
				.getMongoOperationsForDBType(EWallDBType.SYSTEM);
	}

	/**
	 * Gets the all e wall users.
	 *
	 * @return the all e wall users
	 */
	public List<User> getAllEWallUsers() {
		LOG.debug("Retrieveing all users from mongodb.");
		return this.mongoOps.findAll(User.class, USERS_COLLECTION);

	}

	/**
	 * Adds the e wall user only if his username and userID are unique.
	 *
	 * @param user
	 *            the user
	 * @return true, if successful
	 */
	public boolean addEWallUser(User user) {

		// if user with username already exists report it cannot be added
		Query queryUsername = new Query(Criteria.where("username").is(
				user.getUsername()));
		if ((mongoOps.count(queryUsername, USERS_COLLECTION) > 0)) {
			return false;
		} else {

			mongoOps.insert(user, USERS_COLLECTION);
			LOG.debug("New user with username " + user.getUsername()
					+ " added to mongodb.");

			return true;
		}

	}

	// /**
	// * Gets the e wall user by uuid.
	// *
	// * @param userUuid
	// * the user uuid
	// * @return the e wall user by uuid
	// */
	// public User getEWallUserByUUID(UUID userUuid) {
	// LOG.debug("Retrieving an existing person from database");
	// Query query = new Query(Criteria.where("username").is(userUuid));
	// return this.mongoOps.findOne(query, User.class, USERS_COLLECTION);
	// }

	/**
	 * Gets the e wall user by username.
	 *
	 * @param username
	 *            the username
	 * @return the e wall user by username
	 */
	public User getEWallUserByUsername(String username) {
		if (username.isEmpty() == true)
			return null;

		LOG.debug("Retrieveing user with username: " + username
				+ " from mongodb.");

		Query query = new Query(Criteria.where("username").is(username));
		return this.mongoOps.findOne(query, User.class, USERS_COLLECTION);
	}

	/**
	 * Gets the primary users associated with given caregiver.
	 *
	 * @param username
	 *            the username
	 * @return the primary users associated with caregiver
	 */
	public List<User> getPrimaryUsersAssociatedWithCaregiver(String username) {
		// check if username.isEmpty() == true is done in below method
		// (checkIfUserIsFormalCaregiver called from the UserControler before
		// this)
		LOG.debug("Retrieveing user profiles for caregiver user with username: "
				+ username + " from mongodb.");

		Query query = new Query(Criteria.where("caregiversUsernamesList")
				.exists(true));
		// get all users that have caregiver(s)
		List<User> userList = this.mongoOps.find(query, User.class,
				USERS_COLLECTION);

		List<User> primaryUsers = new ArrayList<User>();
		User tempUser;
		// go through all users that have caregivers
		for (int j = 0; j < userList.size(); j++) {
			// get current user
			tempUser = userList.get(j);
			for (int i = 0; i < tempUser.getCaregiversUsernamesList().size(); i++) {
				// go through all caregivers usernames entries in the list and
				// check if the current caregiver username is the same as the
				// user/caregiver that is given (connected with currently
				// observed user)
				if (tempUser.getCaregiversUsernamesList().get(i)
						.equals(username)) {
					// if currently observed user has a caregiver with given
					// username than store his UserProfile in a list (to be
					// returned)
					primaryUsers.add(tempUser);
				}
			}

		}
		return primaryUsers;
	}

	/**
	 * Check if user is formal caregiver.
	 *
	 * @param username
	 *            the username
	 * @return true, if successful
	 */
	public boolean checkIfUserIsFormalCaregiver(String username) {
		if (username.isEmpty() == true)
			return false;
		LOG.debug("Checking if user with username: " + username
				+ " is has a role of formal caregiver.");

		Query query = new Query(Criteria.where("username").is(username));
		User foundCaregiver = this.mongoOps.findOne(query, User.class,
				USERS_COLLECTION);

		if (foundCaregiver == null)
			return false;

		if (foundCaregiver.getUserRole().equals(UserRole.FORMAL_CAREGIVER)) {
			return true;
		} else
			return false;

	}

	// /**
	// * Gets the e wall user by user id.
	// *
	// * @param userId
	// * the user id
	// * @return the e wall user by user id
	// */
	// public User getEWallUserByUserId(Integer userId) {
	// if (userId == null)
	// return null;
	//
	// LOG.debug("Retrieveing user with userId " + userId + " from mongodb.");
	//
	// Query query = new Query(Criteria.where("userID").is(userId));
	// return this.mongoOps.findOne(query, User.class, USERS_COLLECTION);
	// }

	/**
	 * Gets the e wall user by first and lastname.
	 *
	 * @param firstName
	 *            the first name
	 * @param lastName
	 *            the last name
	 * @return the e wall user by first and lastname
	 */
	public List<User> getEWallUserByFirstAndLastname(String firstName,
			String lastName) {

		if (firstName == null || lastName == null)
			return null;

		LOG.debug("Retrieveing user with firstName " + firstName
				+ "and lastName " + lastName + " from mongodb.");

		Query query = new Query();

		query.addCriteria(Criteria
				.where("firstName")
				.exists(true)
				.andOperator(Criteria.where("firstName").is(firstName),
						Criteria.where("lastName").is(lastName)));
		return this.mongoOps.find(query, User.class, USERS_COLLECTION);

	}

	/**
	 * Delete user with username.
	 *
	 * @param username
	 *            the username
	 * @return true, if successful
	 */
	public boolean deleteUserWithUsername(String username) {
		if (username.isEmpty() == true)
			return false;

		LOG.debug("Deleting user with username " + username + " from mongodb.");

		Query query = new Query(Criteria.where("username").is(username));
		this.mongoOps.remove(query, User.class, USERS_COLLECTION);

		deleteUsernameFromAllCaregiversLists(username);
		return true;

	}

	/**
	 * Delete given username from all caregivers lists.
	 *
	 * @param username
	 *            the username
	 */
	public void deleteUsernameFromAllCaregiversLists(String username) {
		LOG.debug("Check if username: "
				+ username
				+ " was listed as a caregiver for any of the other users and if yes, delete it from their caregivers list.");

		Query query = new Query(Criteria.where("caregiversUsernamesList")
				.exists(true));
		// get all users that have caregiver(s)
		List<User> userList = this.mongoOps.find(query, User.class,
				USERS_COLLECTION);

		User tempUser;
		// go through all users that have caregivers
		for (int j = 0; j < userList.size(); j++) {
			// get current user
			tempUser = userList.get(j);
			for (int i = 0; i < tempUser.getCaregiversUsernamesList().size(); i++) {
				// go through all caregivers usernames entries in the list and
				// check if the current caregiver username is the same as the
				// user/caregiver that needs to be removed
				if (tempUser.getCaregiversUsernamesList().get(i)
						.equals(username)) {
					// if yes: remove caregiver username (connected with deleted
					// user/caregiver) for each user
					tempUser.getCaregiversUsernamesList().remove(i);

					// and save the changes for current user in mongodb
					mongoOps.save(tempUser, USERS_COLLECTION);
				}
			}

		}
	}

	/**
	 * Modify e wall user with username.
	 *
	 * @param username
	 *            the username
	 * @param userNew
	 *            the user new
	 * @return true, if successful
	 */
	public boolean modifyEWallUserWithUsername(String username, User userNew) {

		if (userNew == null || username == null)
			return false;

		Query query = new Query(Criteria.where("username").is(username));
		User userFromDB = mongoOps.findOne(query, User.class, USERS_COLLECTION);

		// if user with given userName does not exist add it
		if (userFromDB == null) {
			addEWallUser(userNew);
			return true;
		}

		// set top level element (firstname)
		userFromDB.setFirstName(userNew.getFirstName());
		// set top level element (lastname)
		userFromDB.setLastName(userNew.getLastName());
		// set top level element (role)
		userFromDB.setUserRole(userNew.getUserRole());

		// set top level element (list of app names)
		if (userNew.getApplicationNamesList() != null
				&& !userNew.getApplicationNamesList().isEmpty()) {
			userFromDB.setApplicationNamesList(userNew
					.getApplicationNamesList());
		}
		// set top level element (list of caregivers' usernames)
		if (userNew.getCaregiversUsernamesList() != null
				&& !userNew.getCaregiversUsernamesList().isEmpty()) {
			userFromDB.setCaregiversUsernamesList(userNew
					.getCaregiversUsernamesList());
		}

		// check if new UserProfile if defined
		if (userNew.getUserProfile() != null) {

			// if User does not have a UserProfile simply add a new one
			if (userFromDB.getUserProfile() == null) {
				userFromDB.setUserProfile(userNew.getUserProfile());

			} else
			// if however User has User profile then go deeper into the
			// SubProfiles and update only defined SubProfiles
			{
				// update ActvitiesSubProfile
				if (userNew.getUserProfile().getActivitiesSubProfile() != null)
					userFromDB.getUserProfile().setActivitiesSubProfile(
							userNew.getUserProfile().getActivitiesSubProfile());
				// update EmotionalStatesSubProfile
				if (userNew.getUserProfile().getEmotionalStatesSubProfile() != null)
					userFromDB.getUserProfile().setEmotionalStatesSubProfile(
							userNew.getUserProfile()
									.getEmotionalStatesSubProfile());
				// update HealthStatesSubProfile
				if (userNew.getUserProfile().getHealthSubProfile() != null)
					userFromDB.getUserProfile().setHealthSubProfile(
							userNew.getUserProfile().getHealthSubProfile());
				// update UserPreferencesStatesSubProfile
				if (userNew.getUserProfile().getUserPreferencesSubProfile() != null)
					userFromDB.getUserProfile().setUserPreferencesSubProfile(
							userNew.getUserProfile()
									.getUserPreferencesSubProfile());
				// update VCardSubProfile
				if (userNew.getUserProfile().getvCardSubProfile() != null)
					userFromDB.getUserProfile().setvCardSubProfile(
							userNew.getUserProfile().getvCardSubProfile());
				// update EWallSubProfile
				if (userNew.getUserProfile().geteWallSubProfile() != null)
					userFromDB.getUserProfile().seteWallSubProfile(
							userNew.getUserProfile().geteWallSubProfile());

			}
		}

		mongoOps.save(userFromDB, USERS_COLLECTION);

		return true;

	}

	/**
	 * Collection exits.
	 *
	 * @return true, if successful
	 */
	public boolean collectionExits() {
		return mongoOps.collectionExists(USERS_COLLECTION);
	}
}
