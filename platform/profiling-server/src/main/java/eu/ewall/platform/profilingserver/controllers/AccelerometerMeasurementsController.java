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

import eu.ewall.platform.commons.datamodel.measure.AccelerometerMeasurement;
import eu.ewall.platform.profilingserver.services.AccelerometerMeasurementServiceImpl;

/**
 * The Class AccelerometerMeasurementsController.
 *
 * @author eandgrg
 */

// GET between timestamps
// http://localhost:8080/profiling-server/users/testusername/accelerometer/timestamp?from=1000&to=3000
// when deployed on cloud
// http://localhost:8080/platform-dev/profiling-server/users/testusername/accelerometer/timestamp?from=1000&to=3000

// GET all accelerometer data for user with some username
// http://localhost:8080/profiling-server/users/testusername/accelerometer/all

@RestController
@RequestMapping(value = "/users/{username}/accelerometer")
public class AccelerometerMeasurementsController {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(AccelerometerMeasurementsController.class);

	/** The accelerometer measurement service. */
	@Autowired
	private AccelerometerMeasurementServiceImpl accelerometerMeasurementService;

	/**
	 * Instantiates a new measurements controller.
	 */
	public AccelerometerMeasurementsController() {
	}

	/**
	 * Gets the accelerometer measurements between timestamps.
	 *
	 * @param username the username
	 * @param from            the from
	 * @param to            the to
	 * @return the accelerometer measurements between timestamps
	 */
	@RequestMapping(value = "/timestamp", method = RequestMethod.GET)
	public ResponseEntity<List<AccelerometerMeasurement>> getAccelerometerMeasurementsBetweenTimestamps(
			@PathVariable String username, @RequestParam("from") long from,
			@RequestParam("to") long to) {

		LOG.debug("getAccelerometerMeasurementsBetweenTimestamps: " + from
				+ " and " + to);

		List<AccelerometerMeasurement> accelerometerMeasurementsList;

		try {
			accelerometerMeasurementsList = accelerometerMeasurementService
					.getAccelerometerMeasurementsBetweenTimestamps(username,
							from, to);

			if (accelerometerMeasurementsList != null) {
				return new ResponseEntity<List<AccelerometerMeasurement>>(
						accelerometerMeasurementsList, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<AccelerometerMeasurement>>(
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<List<AccelerometerMeasurement>>(
					HttpStatus.BAD_REQUEST);
		}

	}

	// @RequestMapping(value = "/timestamp", method = RequestMethod.GET)
	// public ResponseEntity<String>
	// getAccelerometerMeasurementsBetweenTimestamps(
	// @RequestParam("from") long from, @RequestParam("to") long to) {
	//
	// LOG.info("getAccelerometerMeasurementsBetweenTimestamps: " + from
	// + " and " + to);
	//
	// List<AccelerometerMeasurement> accelerometerMeasurementsList;
	//
	// try {
	// accelerometerMeasurementsList = accelerometerMeasurementService
	// .getAccelerometerMeasurementsBetweenTimestamps(from, to);
	//
	// if (accelerometerMeasurementsList != null) {
	// return new ResponseEntity<String>(
	// DM2JsonObjectMapper
	// .writeValueAsString(accelerometerMeasurementsList),
	// HttpStatus.OK);
	// } else {
	// return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
	// }
	// } catch (Exception e) {
	// LOG.warn(e.getMessage());
	// return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	// }
	//
	// }

	/**
	 * Gets the all accelerometer measurements for user.
	 *
	 * @param username the username
	 * @param timestamp the timestamp
	 * @return the all accelerometer measurements for user
	 */
	/*@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<List<AccelerometerMeasurement>> getAllAccelerometerMeasurementsForUser(
			@PathVariable String username) {

		LOG.info("Getting all accelerometer measurements");

		List<AccelerometerMeasurement> accelerometerMeasurementsList;

		try {
			accelerometerMeasurementsList = accelerometerMeasurementService
					.getAllAccelerometerMeasurementsForUser(username);

			if (accelerometerMeasurementsList != null) {
				return new ResponseEntity<List<AccelerometerMeasurement>>(
						accelerometerMeasurementsList, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<AccelerometerMeasurement>>(
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<List<AccelerometerMeasurement>>(
					HttpStatus.BAD_REQUEST);
		}
	}
*/
	/**
	 * Gets the accelerometer measurements by timestamp.
	 *
	 * @param username the username
	 * @param timestamp            the timestamp
	 * @return the accelerometer measurements by timestamp
	 */
	@RequestMapping(value = "timestamp/{timestamp}", method = RequestMethod.GET)
	public ResponseEntity<AccelerometerMeasurement> getAccelerometerMeasurementsByTimestamp(
			@PathVariable String username, @PathVariable long timestamp) {

		LOG.debug("getAccelerometerMeasurementsByTimestamp: " + timestamp);

		AccelerometerMeasurement accelerometerMeasurement;

		try {
			accelerometerMeasurement = accelerometerMeasurementService
					.getAccelerometerMeasurementsByTimestamp(username,
							timestamp);

			if (accelerometerMeasurement != null) {
				return new ResponseEntity<AccelerometerMeasurement>(
						accelerometerMeasurement, HttpStatus.OK);
			} else {
				return new ResponseEntity<AccelerometerMeasurement>(
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<AccelerometerMeasurement>(
					HttpStatus.BAD_REQUEST);
		}

	}

}
