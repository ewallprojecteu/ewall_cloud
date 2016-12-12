/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.cloudgateway.dao;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import eu.ewall.platform.commons.datamodel.message.Notification;

/**
 * The Class NotificationDAO.
 */
@Component
public class NotificationDAO {

	/** The log. */
	Logger log = LoggerFactory.getLogger(NotificationDAO.class);

	/** The notification manager url. */
	@Value("${notification-manager.url}")
	private String notificationManagerUrl;

	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;

	/**
	 * Post notification.
	 *
	 * @param notification
	 *            the notification
	 */
	public void postNotification(Notification notification) {

		try {
			String BASE_URL = notificationManagerUrl + "/Notif";

			Date date = new Date(notification.getCreationTime());
			DateFormat formatter = new SimpleDateFormat("dd-MM-yy");
			String dateFormatted = formatter.format(date);
			formatter = new SimpleDateFormat("HH:mm.mmm");
			String timeFormated = formatter.format(date);

			double priority;
			switch (notification.getPriority()) {
			case HIGH:
				priority = 1;
				break;
			case LOW:
				priority = 0.1;
				break;
			case NONE:
				priority = 0;
				break;
			default:
				priority = 0.5;
				break;
			}

			//TODO: this is temp fix, the notification manager API needs to be improved (content must be send via http message body, not as url parameter), then this will not be needed
			String decodedContent = URLDecoder.decode(
					notification.getContent(), "UTF8");
			decodedContent = URLDecoder.decode(decodedContent, "UTF8");

			String targetUrl = BASE_URL
					+ "?user={user}&date={date}&time={time}"
					+ "&type={type}&title={title}&content="
					+ "{content}&prior={prior}&source=local";

			ewallClient.postForObject(targetUrl, null, String.class,
					notification.getPrimaryUser(), dateFormatted, timeFormated,
					notification.getNotificationType(),
					notification.getTitle(), decodedContent, priority);

		} catch (RestClientException e) {
			log.error("RestClientException="+e.getMessage());
			// e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			log.error("UnsupportedEncodingException="+e.getMessage());
			//e.printStackTrace();
		}
	}

	/**
	 * Post caregiver notification.
	 *
	 * @param notification
	 *            the notification
	 */
	public void postCaregiverNotification(Notification notification) {

		try {
			String BASE_URL = notificationManagerUrl + "/"
					+ notification.getCaregiver() + "/notifications";

			Date date = new Date(notification.getCreationTime());
			DateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			String dateTime = formatter.format(date);

			double priority;
			switch (notification.getPriority()) {
			case HIGH:
				priority = 1;
				break;
			case LOW:
				priority = 0.1;
				break;
			case NONE:
				priority = 0;
				break;
			default:
				priority = 0.5;
				break;
			}

			String targetUrl = UriComponentsBuilder.fromUriString(BASE_URL)
					.queryParam("primaryUser", notification.getPrimaryUser())
					.queryParam("dateTime", dateTime)
					.queryParam("type", notification.getNotificationType())
					.queryParam("title", notification.getTitle())
					.queryParam("content", notification.getContent())
					.queryParam("prior", priority)
					.queryParam("source", "local").build().toString();

			log.info("postCaregiverNotification().targetUrl=" + targetUrl);

			ewallClient.postForLocation(targetUrl, null);
		} catch (RestClientException e) {
			log.error(e.getMessage());
			// e.printStackTrace();
		}

	}

}
