/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.cloudgateway.controllers;

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

import eu.ewall.platform.commons.datamodel.marshalling.json.DM2JsonObjectMapper;
import eu.ewall.platform.commons.datamodel.sensing.VitalsSensing;
import eu.ewall.platform.middleware.cloudgateway.services.VitalsSensingServiceImpl;

/**
 * The Class VitalsSensingController.
 * 
 * @author emirmos
 */
@RestController
@RequestMapping("/sensingenvironments/{sensing_environment_id}/devices/{device_id}/vitals")
public class VitalsSensingController {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(VitalsSensingController.class);

	/** The vitlas sensing service. */
	@Autowired
	private VitalsSensingServiceImpl vitalsSensingService;

	/**
	 * Adds the new content.
	 *
	 * @param content
	 *            the content
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param device_id
	 *            the device_id
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/add")
	public ResponseEntity<String> addNewContent(@RequestBody String content,
			@PathVariable String sensing_environment_id,
			@PathVariable String device_id) {

		LOG.debug("addNewContent() called with parameter sensing environmend id = "
				+ sensing_environment_id
				+ ", device id = "
				+ device_id
				+ " and content size = " + content.length());

		try {

			VitalsSensing vitalsSensing = (VitalsSensing) DM2JsonObjectMapper
					.readValueAsString(content, VitalsSensing.class);

			vitalsSensingService.addHealthSensing(sensing_environment_id, device_id,
					vitalsSensing);

			return new ResponseEntity<String>(HttpStatus.CREATED);

		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Gets the last collections timestamp.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param device_id
	 *            the device_id
	 * @return the last collections timestamp
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/lastTimestamp")
	public ResponseEntity<Long> getLastCollectionsTimestamp(
			@PathVariable String sensing_environment_id,
			@PathVariable String device_id) {

		LOG.debug("getLastCollectionsTimestamp() called with parameter sensing environmend id = "
				+ sensing_environment_id + ", device id = " + device_id);

		try {
			long lastTimestamp = vitalsSensingService
					.getLastCollectionsTimestamp(sensing_environment_id, device_id);

			return new ResponseEntity<Long>(lastTimestamp, HttpStatus.OK);

		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<Long>(HttpStatus.BAD_REQUEST);
		}
	}
}
