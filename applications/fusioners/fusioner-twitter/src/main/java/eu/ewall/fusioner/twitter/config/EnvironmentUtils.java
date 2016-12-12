/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.fusioner.twitter.config;

import org.springframework.core.env.Environment;

/**
 * The Class EnvironmentUtils.
 * 
 */
public class EnvironmentUtils {

	/** The env. */
	private static Environment env;

	/**
	 * Sets the environment.
	 *
	 * @param env the new environment
	 */
	public static void setEnvironment(Environment env) {
		EnvironmentUtils.env = env;
	}
	
	/**
	 * Gets the property.
	 *
	 * @param propertyName the property name
	 * @return the property
	 */
	public static String getProperty(String propertyName) {
		return env.getProperty(propertyName);
	}

	/**
	 * Gets the flag only listen for auth user statuses.
	 *
	 * @return the flag only listen for auth user statuses
	 */
	public static boolean getFlagOnlyListenForAuthUserStatuses() {
		String flagStr = env.getProperty("status.fromAuthUserOnly")
				.toLowerCase();
		return flagStr.equals("true") ? true : false;
	}
}
