/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.cloudgateway.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * The Class SensingDataNotificationDAO.
 * 
 * @author emirmos
 */
@Component
public class SensingDataNotificationDAO {

	/** The template. */
	@Qualifier("ewall")
	private RestOperations ewallClient;

	// TODO should be notification and subscription objects
	/**
	 * Send notification.
	 *
	 * @param username
	 *            the username
	 * @param source
	 *            the source
	 */
	@Async
	public void sendNotification(String username, String source) {

		String BASE_URL = "http://localhost:8080/idss-fall-detection-reasoner/fallDetected";

		String targetUrl = UriComponentsBuilder.fromUriString(BASE_URL)
				.queryParam("user", username).queryParam("source", source)
				.build().toString();

		ewallClient.postForLocation(targetUrl, null);

	}

}
