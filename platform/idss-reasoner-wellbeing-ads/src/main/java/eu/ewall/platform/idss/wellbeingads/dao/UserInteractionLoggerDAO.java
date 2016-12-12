/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.idss.wellbeingads.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import eu.ewall.platform.idss.wellbeingads.model.lastinteraction.LastIntercationResponse;

/**
 * The Class UserInteractionLoggerDAO.
 */
@Component
public class UserInteractionLoggerDAO {

	/** The log. */
	Logger log = LoggerFactory.getLogger(UserInteractionLoggerDAO.class);

	/** The user interaction logger url. */
	@Value("${user-interaction-logger.url}")
	private String userInteractionLoggerUrl;

	/** The ewall client. */
	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;

	/**
	 * Post notification.
	 *
	 * @param username the username
	 * @return the last interaction my activity
	 */
	public LastIntercationResponse getLastInteractionMyActivity(String username) {
		String url = userInteractionLoggerUrl
				+ "/lastInteractionMyActivityApplication?username={username}";
		log.debug("Invoking endpoint: " + url);
		try {
			return ewallClient.getForObject(url, LastIntercationResponse.class,
					username);
		} catch (Exception e) {
			log.error("Error in getting last interaction date for activity app", e);
			return null;
		}

	}

	/**
	 * Gets the last interaction health.
	 *
	 * @param username the username
	 * @return the last interaction health
	 */
	public LastIntercationResponse getLastInteractionHealth(String username) {
		String url = userInteractionLoggerUrl
				+ "/lastInteractionMyHealthApplication?username={username}";
		log.debug("Invoking endpoint: " + url);
		try {
			return ewallClient.getForObject(url, LastIntercationResponse.class,
					username);
		} catch (Exception e) {
			log.error("Error in getting last interaction date for health app", e);
			return null;
		}

	}

	/**
	 * Gets the last interaction sleep.
	 *
	 * @param username the username
	 * @return the last interaction sleep
	 */
	public LastIntercationResponse getLastInteractionSleep(String username) {
		String url = userInteractionLoggerUrl
				+ "/lastInteractionSleepApplication?username={username}";
		log.debug("Invoking endpoint: " + url);
		try {
			return ewallClient.getForObject(url, LastIntercationResponse.class,
					username);
		} catch (Exception e) {
			log.error("Error in getting last interaction date for sleep app", e);
			return null;
		}

	}

	/**
	 * Gets the last interaction video exercise.
	 *
	 * @param username the username
	 * @return the last interaction video exercise
	 */
	public LastIntercationResponse getLastInteractionVideoExercise(
			String username) {
		String url = userInteractionLoggerUrl
				+ "/lastInteractionVideoExerciseApplication?username={username}";
		log.debug("Invoking endpoint: " + url);
		try {
			return ewallClient.getForObject(url, LastIntercationResponse.class,
					username);
		} catch (Exception e) {
			log.error("Error in getting last interaction date for video exercise", e);
			return null;
		}
	}

	/**
	 * Gets the last interaction cognitive exercise.
	 *
	 * @param username the username
	 * @return the last interaction cognitive exercise
	 */
	public LastIntercationResponse getLastInteractionCognitiveExercise(
			String username) {
		String url = userInteractionLoggerUrl
				+ "/lastInteractionCognitiveExerciseApplication?username={username}";
		log.debug("Invoking endpoint: " + url);
		try {
			return ewallClient.getForObject(url, LastIntercationResponse.class,
					username);
		} catch (Exception e) {
			log.error("Error in getting last interaction date for cognitive exercise", e);
			return null;
		}

	}
	
	
	/**
	 * Gets the last interaction daily life.
	 *
	 * @param username the username
	 * @return the last interaction daily life
	 */
	public LastIntercationResponse getLastInteractionDailyLife(
			String username) {
		String url = userInteractionLoggerUrl
				+ "/lastInteractionMyDailyLifeApplication?username={username}";
		log.debug("Invoking endpoint: " + url);
		try {
			return ewallClient.getForObject(url, LastIntercationResponse.class,
					username);
		} catch (Exception e) {
			log.error("Error in getting last interaction date for daily life app", e);
			return null;
		}

	}
	
	/**
	 * Gets the last interaction help.
	 *
	 * @param username the username
	 * @return the last interaction help
	 */
	public LastIntercationResponse getLastInteractionHelp(
			String username) {
		String url = userInteractionLoggerUrl
				+ "/lastInteractionHelpApplication?username={username}";
		log.debug("Invoking endpoint: " + url);
		try {
			return ewallClient.getForObject(url, LastIntercationResponse.class,
					username);
		} catch (Exception e) {
			log.error("Error in getting last interaction date for help app", e);
			return null;
		}

	}
}
