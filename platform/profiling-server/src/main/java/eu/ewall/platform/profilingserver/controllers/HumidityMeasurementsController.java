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

import eu.ewall.platform.commons.datamodel.measure.HumidityMeasurement;
import eu.ewall.platform.profilingserver.services.HumidityMeasurementServiceImpl;

/**
 * The Class HumidityMeasurementsController.
 * 
 * @author EMIRMOS
 * 
 */
@RestController
@RequestMapping(value = "/users/{username}/humidity")
public class HumidityMeasurementsController {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(HumidityMeasurementsController.class);

	/** The humidity measurement service. */
	@Autowired
	private HumidityMeasurementServiceImpl humidityMeasurementService;

	/**
	 * Instantiates a new measurements controller.
	 */
	public HumidityMeasurementsController() {
	}

	/**
	 * Gets the humidity measurements between timestamps.
	 *
	 * @param username the username
	 * @param from the from
	 * @param to the to
	 * @param room_name the room_name
	 * @return the humidity measurements between timestamps
	 */
	@RequestMapping(value = "/timestamp", method = RequestMethod.GET)
	public ResponseEntity<List<HumidityMeasurement>> getHumidityMeasurementsBetweenTimestamps(
			@PathVariable String username, @RequestParam("from") long from,
			@RequestParam("to") long to, @RequestParam(value = "room", required = false) String room_name) {

		LOG.debug("getHumidityMeasurementsBetweenTimestamps: " + from + " and "
				+ to);

		List<HumidityMeasurement> humidityMeasurementsList;

		try {
			humidityMeasurementsList = humidityMeasurementService
					.getHumidityMeasurementsBetweenTimestamps(username, from,
							to, room_name);

			if (humidityMeasurementsList != null) {
				return new ResponseEntity<List<HumidityMeasurement>>(
						humidityMeasurementsList, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<HumidityMeasurement>>(
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<List<HumidityMeasurement>>(
					HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * Gets the all humidity measurements for user.
	 *
	 * @param username the username
	 * @param timestamp the timestamp
	 * @return the all humidity measurements for user
	 */
	/*	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<List<HumidityMeasurement>> getAllHumidityMeasurementsForUser(
			@PathVariable String username) {

		LOG.info("Getting all HumidityMeasurements");

		List<HumidityMeasurement> humidityMeasurementsList;

		try {
			humidityMeasurementsList = humidityMeasurementService
					.getAllHumidityMeasurementsForUser(username);

			if (humidityMeasurementsList != null) {
				return new ResponseEntity<List<HumidityMeasurement>>(
						humidityMeasurementsList, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<HumidityMeasurement>>(
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<List<HumidityMeasurement>>(
					HttpStatus.BAD_REQUEST);
		}
	}
*/
	/**
	 * Gets the humidity measurements by timestamp.
	 *
	 * @param username the username
	 * @param timestamp the timestamp
	 * @return the humidity measurements by timestamp
	 */
	@RequestMapping(value = "timestamp/{timestamp}", method = RequestMethod.GET)
	public ResponseEntity<HumidityMeasurement> getHumidityMeasurementsByTimestamp(
			@PathVariable String username, @PathVariable long timestamp) {

		LOG.debug("getHumidityMeasurementsByTimestamp: " + timestamp);

		HumidityMeasurement humidityMeasurement;

		try {
			humidityMeasurement = humidityMeasurementService
					.getHumidityMeasurementsByTimestamp(username, timestamp);

			if (humidityMeasurement != null) {
				return new ResponseEntity<HumidityMeasurement>(
						humidityMeasurement, HttpStatus.OK);
			} else {
				return new ResponseEntity<HumidityMeasurement>(
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<HumidityMeasurement>(
					HttpStatus.BAD_REQUEST);
		}

	}

}
