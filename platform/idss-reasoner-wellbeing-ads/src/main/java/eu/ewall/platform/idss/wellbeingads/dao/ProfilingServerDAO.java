/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.idss.wellbeingads.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.sensing.VisualSensing;

/**
 * The Class ProfilingServerDAO.
 */
@Component
public class ProfilingServerDAO {

	/** The log. */
	Logger log = LoggerFactory.getLogger(ProfilingServerDAO.class);

	/** The Constant ONE_DAY. */
	private static final long ONE_DAY = 1000 * 60 * 60 * 24;

	/** The profiling server url. */
	@Value("${profiling-server.url}")
	private String profilingServerUrl;

	/** The ewall client. */
	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;

	/**
	 * Gets the user profile.
	 *
	 * @param username
	 *            the username
	 * @return the user profile
	 */
	public User getUserProfile(String username) {
		String url = profilingServerUrl + "/users/{username}";
		log.debug("Invoking endpoint: " + url);
		try {
			return ewallClient.getForObject(url, User.class, username);
		} catch (Exception e) {
			log.error("Error in getting user profile", e);
			return null;
		}

	}

	/**
	 * Gets the all users.
	 *
	 * @return the all users
	 */
	public List<User> getAllUsers() {
		String url = profilingServerUrl + "/users/?associatedWithSensEnvFilter=ALL_USERS";
		log.debug("Invoking endpoint: " + url);

		try {
			User[] users = ewallClient.getForObject(url, User[].class);
			return Arrays.asList(users);
		} catch (Exception e) {
			log.error("Error in getting users", e);
			return null;
		}

	}

	/**
	 * Gets the last visual sensing data.
	 *
	 * @param username
	 *            the username
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param maxDaysPerRequest
	 *            the max days per request
	 * @return the last visual sensing data
	 */
	// TODO add new endpoint to profiling server to get last visual data in
	// certain period only
	public VisualSensing getLastVisualSensingData(String username, Date from, Date to, int maxDaysPerRequest) {
		try {
			ArrayList<VisualSensing> visualSensingMeasurementsList = new ArrayList<VisualSensing>();
			Date newTo = new Date(to.getTime());
			while (true) {
				// If more then x days, we ask for x days
				if ((newTo.getTime() - from.getTime()) > (ONE_DAY * maxDaysPerRequest)) {
					newTo.setTime(from.getTime() + (ONE_DAY * maxDaysPerRequest));
				} else {
					// else we ask for the latest x-from days
					newTo.setTime(to.getTime());
				}

				String url = profilingServerUrl + "/users/" + username + "/visual?from=" + from.getTime() + "&to="
						+ newTo.getTime();
				log.debug("Invoking endpoint: " + url);
				VisualSensing[] visualSensingMeasurements = ewallClient.getForObject(url,
						new VisualSensing[0].getClass());
				visualSensingMeasurementsList.addAll(Arrays.asList(visualSensingMeasurements));
				if (newTo.compareTo(to) == 0) {
					break;
				}
				from.setTime(newTo.getTime());
				newTo.setTime(to.getTime());
			}

			log.debug("Ordering elements...");
			long now = System.currentTimeMillis();
			Collections.sort(visualSensingMeasurementsList, new Comparator<VisualSensing>() {
				public int compare(VisualSensing m1, VisualSensing m2) {
					return Long.compare(m1.getTimestamp(), m2.getTimestamp());
				};
			});
			log.debug("Ordering took " + (System.currentTimeMillis() - now) + " ms.");
			if (visualSensingMeasurementsList.size() == 0)
				return null;

			return visualSensingMeasurementsList.get(visualSensingMeasurementsList.size() - 1);
		} catch (Exception e) {
			log.error("Error in getting latest visual data", e);
			return null;
		}
	}
}
