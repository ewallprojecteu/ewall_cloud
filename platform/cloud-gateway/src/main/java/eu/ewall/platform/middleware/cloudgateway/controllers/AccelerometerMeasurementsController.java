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
import eu.ewall.platform.commons.datamodel.measure.AccelerometerMeasurement;
import eu.ewall.platform.middleware.cloudgateway.services.AccelerometerMeasurementServiceImpl;
import eu.ewall.platform.middleware.cloudgateway.services.SensingDataNotificationServiceImpl;

/**
 * The Class AccelerometerMeasurementsController.
 *
 * @author emirmos
 */
@RestController
@RequestMapping("/sensingenvironments/{sensing_environment_id}/devices/{device_id}/activity")
public class AccelerometerMeasurementsController {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(AccelerometerMeasurementsController.class);

	/** The device handler. */
	@Autowired
	private AccelerometerMeasurementServiceImpl accelerometerMeasurementService;
	
	@Autowired
	SensingDataNotificationServiceImpl sensingDataNotificationService;

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

		LOG.debug("addNewContent() called with parameter sensing environmend id = " + sensing_environment_id 
				+ ", device id = " + device_id
				+ " and content size = " + content.length());


		try {

			AccelerometerMeasurement accMeasurement = (AccelerometerMeasurement) DM2JsonObjectMapper
					.readValueAsString(content, AccelerometerMeasurement.class);

			accelerometerMeasurementService.addAccelerometerMeasurement(sensing_environment_id, accMeasurement);
			
			//we need to check if there is a subscribing entity for this type of data
			sensingDataNotificationService.processSensingData(sensing_environment_id, accMeasurement);

			return new ResponseEntity<String>(HttpStatus.CREATED);

		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * Update content.
	 *
	 * @param content
	 *            the content
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param device_id
	 *            the device_id
	 * @param content_id
	 *            the content_id
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/{content_id}")
	public ResponseEntity<String> updateContent(@RequestBody String content,
			@PathVariable String sensing_environment_id,
			@PathVariable String device_id, @PathVariable String content_id) {

		LOG.debug("updateContent() called with parameter sensing environmend id = " + sensing_environment_id 
				+ ", device id = " + device_id
				+ " and content size = " + content.length());

		return new ResponseEntity<String>(HttpStatus.METHOD_NOT_ALLOWED);
	}

	/**
	 * Delete content.
	 *
	 * @param content_id
	 *            the content_id
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{content_id}")
	public ResponseEntity<String> deleteContent(@PathVariable String content_id) {

		LOG.debug("deleteContent() called with parameter content_id = " + content_id);

		return new ResponseEntity<String>(HttpStatus.METHOD_NOT_ALLOWED);

	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/lastTimestamp")
	public ResponseEntity<Long> getLastCollectionsTimestamp(
			@PathVariable String sensing_environment_id,
			@PathVariable String device_id) {

		LOG.debug("getLastCollectionsTimestamp() called with parameter sensing environmend id = "
				+ sensing_environment_id + ", device id = " + device_id);

		try {
			long lastTimestamp = accelerometerMeasurementService
					.getLastCollectionsTimestamp(sensing_environment_id);

			return new ResponseEntity<Long>(lastTimestamp, HttpStatus.OK);

		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<Long>(HttpStatus.BAD_REQUEST);
		}
	}

}
