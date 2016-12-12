/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.profilingserver.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.mongodb.gridfs.GridFSDBFile;

import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.resource.Base64Image;
import eu.ewall.platform.middleware.datamanager.dao.profile.UserDao;
import eu.ewall.platform.middleware.datamanager.dao.profile.UserPhotoDao;
import eu.ewall.platform.profilingserver.authentication.UserAuthenticatorService;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;

/**
 * The Class UserServiceImpl.
 * 
 * @author eandgrg, emirmos
 */
@Service("userService")
public class UserServiceImpl implements DisposableBean {

	/** The log. */
	Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	/** The env. */
	@Autowired
	private Environment env;

	/** The user dao. */
	private UserDao userDao;

	/** The user photo dao. */
	private UserPhotoDao userPhotoDao;

	/**
	 * Instantiates a new user service impl.
	 */
	public UserServiceImpl() {
		userDao = new UserDao();
		userPhotoDao = new UserPhotoDao();
	}

	/** The user authenticator service. */
	@Autowired
	private UserAuthenticatorService userAuthenticatorService;

	/**
	 * Adds the e wall user.
	 *
	 * @param user
	 *            the user
	 * @return true, if successful
	 */
	public boolean addEWallUser(User user) {
		log.debug("addEWallUser from");
		if (userDao.addEWallUser(user)) {
			userAuthenticatorService
					.addNewUserCredentialsOrModifyPasswordForExistingUsername(
							user.getUsername(), user.getUsername());
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Gets the all e wall users.
	 *
	 * @return the all e wall users
	 */
	public List<User> getAllEWallUsers() {

		log.debug("getAllEWallUsers");
		return userDao.getAllEWallUsers();
	}

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
		log.debug("getAllEWallUsers with given name and surname");
		return userDao.getEWallUserByFirstAndLastname(firstName, lastName);
	}

	/**
	 * Gets the e wall user by username.
	 *
	 * @param username
	 *            the username
	 * @return the e wall user by username
	 */
	public User getEWallUserByUsername(String username) {
		return userDao.getEWallUserByUsername(username);
	}

	/**
	 * Gets the photo by username.
	 *
	 * @param username
	 *            the username
	 * @return the photo by username
	 */
	public GridFSDBFile getPhotoByUsername(String username) {
		return userPhotoDao.getPhotoByUsername(username);
	}

	/**
	 * Upload user photo.
	 *
	 * @param username
	 *            the username
	 * @param base64Image
	 *            the base64 image
	 * @return true, if successful
	 */
	public boolean uploadUserPhoto(String username, Base64Image base64Image) {
		return userPhotoDao.saveUserPhoto(username, base64Image);
	}

	/**
	 * Delete user photo.
	 *
	 * @param username
	 *            the username
	 * @return true, if successful
	 */
	public boolean deleteUserPhoto(String username) {
		return userPhotoDao.deleteUserPhoto(username);
	}

	/**
	 * Check if user is formal caregiver.
	 *
	 * @param username
	 *            the username
	 * @return true, if successful
	 */
	public boolean checkIfUserIsFormalCaregiver(String username) {
		return userDao.checkIfUserIsFormalCaregiver(username);

	}

	/**
	 * Gets the primary users associated with caregiver.
	 *
	 * @param username
	 *            the username
	 * @return the primary users associated with caregiver
	 */
	public List<User> getPrimaryUsersAssociatedWithCaregiver(String username) {
		return userDao.getPrimaryUsersAssociatedWithCaregiver(username);
	}

	/**
	 * Modify e wall user with username.
	 *
	 * @param username
	 *            the username
	 * @param user
	 *            the user
	 * @return true, if successful
	 */
	public boolean modifyEWallUserWithUsername(String username, User user) {
		return userDao.modifyEWallUserWithUsername(username, user);
	}

	/**
	 * Delete user with username.
	 *
	 * @param username
	 *            the username
	 * @return true, if successful
	 */
	public boolean deleteUserWithUsername(String username) {
		return userDao.deleteUserWithUsername(username);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() throws Exception {
		MongoDBFactory.close();
	}

	/**
	 * Inits the deafult data.
	 *
	 * @param user
	 *            the user
	 */
	public void initDeafultData(User user) {

		if (userDao.collectionExits()) {
			log.info("User collection exists. No init data added.");
		} else {
			log.info("User collection does not exist. Addding default admin user from config file");
			this.addEWallUser(user);
		}

	}

	/**
	 * Gets the user dao.
	 *
	 * @return the user dao
	 */
	public UserDao getUserDao() {
		return userDao;
	}

	/**
	 * Sets the user dao.
	 *
	 * @param userDao
	 *            the new user dao
	 */
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	/**
	 * Gets the user photo dao.
	 *
	 * @return the user photo dao
	 */
	public UserPhotoDao getUserPhotoDao() {
		return userPhotoDao;
	}

	/**
	 * Sets the user photo dao.
	 *
	 * @param userPhotoDao
	 *            the new user photo dao
	 */
	public void setUserPhotoDao(UserPhotoDao userPhotoDao) {
		this.userPhotoDao = userPhotoDao;
	}
}
