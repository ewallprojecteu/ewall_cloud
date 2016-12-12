/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.profilingserver.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.ewall.platform.commons.datamodel.measure.TemperatureMeasurement;
import eu.ewall.platform.profilingserver.services.TemperatureMeasurementServiceImpl;

/**
 * The Class TemperatureMeasurementsController.
 * 
 * @author EMIRMOS
 * 
 */
@RestController
@RequestMapping(value = "/users/{username}/temperature")
public class TemperatureMeasurementsController {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(TemperatureMeasurementsController.class);

	/** The temperature measurement service. */
	@Autowired
	private TemperatureMeasurementServiceImpl temperatureMeasurementService;

	/**
	 * Instantiates a new measurements controller.
	 */
	public TemperatureMeasurementsController() {
	}

	/**
	 * Gets the temperature measurements between timestamps.
	 *
	 * @param username the username
	 * @param from the from
	 * @param to the to
	 * @param room_name the room_name
	 * @return the temperature measurements between timestamps
	 */
	@RequestMapping(value = "/timestamp", method = RequestMethod.GET)
	public ResponseEntity<List<TemperatureMeasurement>> getTemperatureMeasurementsBetweenTimestamps(
			@PathVariable String username, @RequestParam("from") long from,
			@RequestParam("to") long to, @RequestParam(value = "room", required = false) String room_name) {

		LOG.debug("getTemperatureMeasurementsBetweenTimestamps: " + from
				+ " and " + to);

		List<TemperatureMeasurement> temperatureMeasurementsList;

		try {
			temperatureMeasurementsList = temperatureMeasurementService
					.getTemperatureMeasurementsBetweenTimestamps(username,
							from, to, room_name);

			if (temperatureMeasurementsList != null) {
				return new ResponseEntity<List<TemperatureMeasurement>>(
						temperatureMeasurementsList, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<TemperatureMeasurement>>(
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<List<TemperatureMeasurement>>(
					HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * Gets the all temperature measurements for user.
	 *
	 * @param username the username
	 * @param timestamp the timestamp
	 * @return the all temperature measurements for user
	 */
	/*	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<List<TemperatureMeasurement>> getAllTemperatureMeasurementsForUser(
			@PathVariable String username) {

		LOG.info("Getting all TemperatureMeasurements");

		List<TemperatureMeasurement> temperatureMeasurementsList;

		try {
			temperatureMeasurementsList = temperatureMeasurementService
					.getAllTemperatureMeasurementsForUser(username);

			if (temperatureMeasurementsList != null) {
				return new ResponseEntity<List<TemperatureMeasurement>>(
						temperatureMeasurementsList, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<TemperatureMeasurement>>(
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<List<TemperatureMeasurement>>(
					HttpStatus.BAD_REQUEST);
		}
	}
*/
	/**
	 * Gets the temperature measurements by timestamp.
	 *
	 * @param username the username
	 * @param timestamp the timestamp
	 * @return the temperature measurements by timestamp
	 */
	@RequestMapping(value = "timestamp/{timestamp}", method = RequestMethod.GET)
	public ResponseEntity<TemperatureMeasurement> getTemperatureMeasurementsByTimestamp(
			@PathVariable String username, @PathVariable long timestamp) {

		LOG.debug("getTemperatureMeasurementsByTimestamp: " + timestamp);

		TemperatureMeasurement temperatureMeasurement;

		try {
			temperatureMeasurement = temperatureMeasurementService
					.getTemperatureMeasurementsByTimestamp(username, timestamp);

			if (temperatureMeasurement != null) {
				return new ResponseEntity<TemperatureMeasurement>(
						temperatureMeasurement, HttpStatus.OK);
			} else {
				return new ResponseEntity<TemperatureMeasurement>(
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<TemperatureMeasurement>(
					HttpStatus.BAD_REQUEST);
		}

	}
}
