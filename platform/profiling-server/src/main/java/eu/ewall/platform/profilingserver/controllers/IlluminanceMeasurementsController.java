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

import eu.ewall.platform.commons.datamodel.measure.IlluminanceMeasurement;
import eu.ewall.platform.profilingserver.services.IlluminanceMeasurementServiceImpl;

/**
 * The Class IlluminanceMeasurementsController.
 * 
 * @author EMIRMOS
 * 
 */
@RestController
@RequestMapping(value = "/users/{username}/illuminance")
public class IlluminanceMeasurementsController {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(IlluminanceMeasurementsController.class);

	/** The illuminance measurement service. */
	@Autowired
	private IlluminanceMeasurementServiceImpl illuminanceMeasurementService;

	/**
	 * Instantiates a new measurements controller.
	 */
	public IlluminanceMeasurementsController() {
	}

	/**
	 * Gets the illuminance measurements between timestamps.
	 *
	 * @param username the username
	 * @param from the from
	 * @param to the to
	 * @param room_name the room_name
	 * @return the illuminance measurements between timestamps
	 */
	@RequestMapping(value = "/timestamp", method = RequestMethod.GET)
	public ResponseEntity<List<IlluminanceMeasurement>> getIlluminanceMeasurementsBetweenTimestamps(
			@PathVariable String username, @RequestParam("from") long from,
			@RequestParam("to") long to, @RequestParam(value = "room", required = false) String room_name) {

		LOG.debug("getIlluminanceMeasurementsBetweenTimestamps: " + from
				+ " and " + to);

		List<IlluminanceMeasurement> illuminanceMeasurementsList;

		try {
			illuminanceMeasurementsList = illuminanceMeasurementService
					.getIlluminanceMeasurementsBetweenTimestamps(username,
							from, to, room_name);

			if (illuminanceMeasurementsList != null) {
				return new ResponseEntity<List<IlluminanceMeasurement>>(
						illuminanceMeasurementsList, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<IlluminanceMeasurement>>(
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<List<IlluminanceMeasurement>>(
					HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * Gets the all illuminance measurements for user.
	 *
	 * @param username the username
	 * @param timestamp the timestamp
	 * @return the all illuminance measurements for user
	 */
/*	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<List<IlluminanceMeasurement>> getAllIlluminanceMeasurementsForUser(
			@PathVariable String username) {

		LOG.info("Getting all IlluminanceMeasurements");

		List<IlluminanceMeasurement> illuminanceMeasurementsList;

		try {
			illuminanceMeasurementsList = illuminanceMeasurementService
					.getAllIlluminanceMeasurementsForUser(username);

			if (illuminanceMeasurementsList != null) {
				return new ResponseEntity<List<IlluminanceMeasurement>>(
						illuminanceMeasurementsList, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<IlluminanceMeasurement>>(
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<List<IlluminanceMeasurement>>(
					HttpStatus.BAD_REQUEST);
		}
	}
*/
	/**
	 * Gets the illuminance measurements by timestamp.
	 *
	 * @param username the username
	 * @param timestamp the timestamp
	 * @return the illuminance measurements by timestamp
	 */
	@RequestMapping(value = "timestamp/{timestamp}", method = RequestMethod.GET)
	public ResponseEntity<IlluminanceMeasurement> getIlluminanceMeasurementsByTimestamp(
			@PathVariable String username, @PathVariable long timestamp) {

		LOG.debug("getIlluminanceMeasurementsByTimestamp: " + timestamp);

		IlluminanceMeasurement illuminanceMeasurement;

		try {
			illuminanceMeasurement = illuminanceMeasurementService
					.getIlluminanceMeasurementsByTimestamp(username, timestamp);

			if (illuminanceMeasurement != null) {
				return new ResponseEntity<IlluminanceMeasurement>(
						illuminanceMeasurement, HttpStatus.OK);
			} else {
				return new ResponseEntity<IlluminanceMeasurement>(
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<IlluminanceMeasurement>(
					HttpStatus.BAD_REQUEST);
		}

	}

}
