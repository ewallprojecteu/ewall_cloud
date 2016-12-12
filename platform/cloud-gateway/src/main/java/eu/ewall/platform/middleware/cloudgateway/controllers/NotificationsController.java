package eu.ewall.platform.middleware.cloudgateway.controllers;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.ewall.platform.commons.datamodel.message.Notification;
import eu.ewall.platform.middleware.cloudgateway.services.NotificationsServiceImpl;

// TODO: Auto-generated Javadoc
/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/


@RestController
@RequestMapping("/sensingenvironments/{sensing_environment_id}")
public class NotificationsController {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(NotificationsController.class);


	/** The notification service. */
	@Autowired
	NotificationsServiceImpl notificationService;
	
	/**
	 * Process notification.
	 *
	 * @param sensing_environment_id the sensing_environment_id
	 * @param notification the notification
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/notifications")
	public ResponseEntity<String> processNotification(
			@PathVariable String sensing_environment_id,
			@RequestBody Notification notification) {

		LOG.debug("processNotification()");

		try {
			if (notification != null) {
				notificationService.postNotification(notification, UUID.fromString(sensing_environment_id));
			
			} else
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<String>(HttpStatus.CREATED);

	}
	
	
	
	/**
	 * Process caregiver notification.
	 *
	 * @param sensing_environment_id the sensing_environment_id
	 * @param notification the notification
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/caregiverNotifications")
	public ResponseEntity<String> processCaregiverNotification(
			@PathVariable String sensing_environment_id,
			@RequestBody Notification notification) {

		LOG.debug("processCaregiverNotification()");

		try {
			if (notification != null) {
				notificationService.postCaregiverNotification(notification, UUID.fromString(sensing_environment_id));
			
			} else
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<String>(HttpStatus.CREATED);

	}
	
	
}
