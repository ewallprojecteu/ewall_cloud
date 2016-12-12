/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.profilingserver.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import eu.ewall.platform.commons.datamodel.authentication.UserCredentialsEwall;
import eu.ewall.platform.middleware.datamanager.dao.authentication.UserCredentialsDao;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;

/**
 * The Class UserAuthenticatorService.
 */
/**
 * @author eandgrg
 *
 */
@Service("userAuthenticatorService")
public class UserAuthenticatorService implements DisposableBean {

	/** The log. */
	Logger log = LoggerFactory.getLogger(UserAuthenticatorService.class);

	/** The password encoder. */
	@Autowired
	PasswordEncoder passwordEncoder;

	/** The user credentials dao. */
	private UserCredentialsDao userCredentialsDao;

	/**
	 * Instantiates a new user authenticator service.
	 */
	public UserAuthenticatorService() {
		userCredentialsDao = new UserCredentialsDao();
	}

	/**
	 * Adds the new user credentials or modify password for existing username.
	 *
	 * @param username
	 *            the username
	 * @param rawPassword
	 *            the raw password
	 */
	public void addNewUserCredentialsOrModifyPasswordForExistingUsername(
			String username, String rawPassword) {

		String hashedPass = passwordEncoder.encode(rawPassword);

		// if UserCredentials with given username exist then modify password,
		// else add new UserCredentials
		if (userCredentialsDao.getUserCredentialsEwallForUsername(username) != null) {
			log.info("Modifying credentials for username=" + username);
			userCredentialsDao.modifyUserCredentialsEwallForUsername(username,
					hashedPass);
		} else {
			log.info("Adding new credentials for username=" + username);
			UserCredentialsEwall userCredentials = new UserCredentialsEwall(
					username, hashedPass);
			userCredentialsDao.addUserCredentialsEwall(userCredentials);
		}

	}

	/**
	 * Do credentials exist for username.
	 *
	 * @param username
	 *            the username
	 * @return true, if credentials are successfully found (if they exist)
	 */
	public boolean doCredentialsExistForUsername(String username) {

		if (userCredentialsDao.getUserCredentialsEwallForUsername(username) != null) {
			log.info("User credentials exist for username=" + username);
			return true;
		} else {
			log.info("User credentials do not exist for username=" + username);
			return false;
		}

	}

	/**
	 * Delete user credentials for username.
	 *
	 * @param username
	 *            the username
	 * @return true, if successful
	 */
	public boolean deleteUserCredentialsForUsername(String username) {
		return userCredentialsDao
				.deleteUserCredentialsEwallForUsername(username);

	}

	/**
	 * Checks if is authenticated.
	 *
	 * @param username
	 *            the username
	 * @param providedRawPassword
	 *            the provided raw password
	 * @return true, if is authenticated
	 */
	public boolean isAuthenticated(String username, String providedRawPassword) {

		UserCredentialsEwall obtainedUserCredentialsEwall = userCredentialsDao
				.getUserCredentialsEwallForUsername(username);

		// credentials exist for the given username
		if (obtainedUserCredentialsEwall != null) {
			String encodedPasswordFromDB = obtainedUserCredentialsEwall
					.getHashedPassword();
			log.info("Checking if provided (" + providedRawPassword
					+ ") and stored password match for username=" + username);
			if (passwordEncoder.matches(providedRawPassword,
					encodedPasswordFromDB)) {
				return true;
			} else
				return false;

		} else {
			// credentials do not exist for the given username
			return false;
		}
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
}
