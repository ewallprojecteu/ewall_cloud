/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.cloudgateway.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The Class SensorsController.
 *
 * @author emirmos
 */
// @RestController
@RequestMapping("/sensingenvironments/{sensing_environment_id}/devices/{device_id}/sensors/")
public class SensorsController {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(SensorsController.class);

	/**
	 * Gets the sensors.
	 *
	 * @param device_id
	 *            the device_id
	 * @return the sensors
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/")
	public ResponseEntity<String> getSensors(@PathVariable String device_id) {

		LOG.debug("getSensors");

		return new ResponseEntity<String>(HttpStatus.METHOD_NOT_ALLOWED);

	}

	/**
	 * Adds the new sensor.
	 *
	 * @param sensor_id
	 *            the sensor_id
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/{sensor_id}")
	public ResponseEntity<String> addNewSensor(@PathVariable String sensor_id) {

		LOG.debug("addNewSensor " + sensor_id);

		return new ResponseEntity<String>(HttpStatus.METHOD_NOT_ALLOWED);

	}

	/**
	 * Update sensor.
	 *
	 * @param sensor_id
	 *            the sensor_id
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/{sensor_id}")
	public ResponseEntity<String> updateSensor(@PathVariable String sensor_id) {

		LOG.debug("updateSensor " + sensor_id);

		return new ResponseEntity<String>(HttpStatus.METHOD_NOT_ALLOWED);

	}

	/**
	 * Delete sensor.
	 *
	 * @param sensor_id
	 *            the sensor_id
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{sensor_id}")
	public ResponseEntity<String> deleteSensor(@PathVariable String sensor_id) {

		LOG.debug("deleteSensor " + sensor_id);

		return new ResponseEntity<String>(HttpStatus.METHOD_NOT_ALLOWED);

	}

	/**
	 * Gets the sensor info.
	 *
	 * @return the sensor info
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{sensor_id}")
	public ResponseEntity<String> getSensorInfo() {

		LOG.debug("getSensorInfo ");

		return new ResponseEntity<String>(HttpStatus.METHOD_NOT_ALLOWED);

	}

}
