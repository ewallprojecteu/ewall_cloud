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

import eu.ewall.platform.commons.datamodel.measure.MovementMeasurement;
import eu.ewall.platform.profilingserver.services.MovementMeasurementServiceImpl;

/**
 * The Class MovementMeasurementsController.
 * 
 * @author EMIRMOS
 * 
 */
@RestController
@RequestMapping(value = "/users/{username}/movement")
public class MovementMeasurementsController {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(MovementMeasurementsController.class);

	/** The movement measurement service. */
	@Autowired
	private MovementMeasurementServiceImpl movementMeasurementService;

	/**
	 * Instantiates a new measurements controller.
	 */
	public MovementMeasurementsController() {
	}

	/**
	 * Gets the movement measurements between timestamps.
	 *
	 * @param username the username
	 * @param from the from
	 * @param to the to
	 * @param room_name the room_name
	 * @return the movement measurements between timestamps
	 */
	@RequestMapping(value = "/timestamp", method = RequestMethod.GET)
	public ResponseEntity<List<MovementMeasurement>> getMovementMeasurementsBetweenTimestamps(
			@PathVariable String username, @RequestParam("from") long from,
			@RequestParam("to") long to, @RequestParam(value = "room", required = false) String room_name) {

		LOG.debug("getMovementMeasurementsBetweenTimestamps: " + from + " and "
				+ to);

		List<MovementMeasurement> movementMeasurementsList;

		try {
			movementMeasurementsList = movementMeasurementService
					.getMovementMeasurementsBetweenTimestamps(username, from,
							to, room_name);

			if (movementMeasurementsList != null) {
				return new ResponseEntity<List<MovementMeasurement>>(
						movementMeasurementsList, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<MovementMeasurement>>(
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<List<MovementMeasurement>>(
					HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * Gets the all movement measurements for user.
	 *
	 * @param username the username
	 * @param timestamp the timestamp
	 * @return the all movement measurements for user
	 */
	/*@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<List<MovementMeasurement>> getAllMovementMeasurementsForUser(
			@PathVariable String username) {

		LOG.info("Getting all MovementMeasurements");

		List<MovementMeasurement> movementMeasurementsList;

		try {
			movementMeasurementsList = movementMeasurementService
					.getAllMovementMeasurementsForUser(username);

			if (movementMeasurementsList != null) {
				return new ResponseEntity<List<MovementMeasurement>>(
						movementMeasurementsList, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<MovementMeasurement>>(
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<List<MovementMeasurement>>(
					HttpStatus.BAD_REQUEST);
		}
	}
*/
	/**
	 * Gets the movement measurements by timestamp.
	 *
	 * @param username the username
	 * @param timestamp the timestamp
	 * @return the movement measurements by timestamp
	 */
	@RequestMapping(value = "timestamp/{timestamp}", method = RequestMethod.GET)
	public ResponseEntity<MovementMeasurement> getMovementMeasurementsByTimestamp(
			@PathVariable String username, @PathVariable long timestamp) {

		LOG.debug("getMovementMeasurementsByTimestamp: " + timestamp);

		MovementMeasurement movementMeasurement;

		try {
			movementMeasurement = movementMeasurementService
					.getMovementMeasurementsByTimestamp(username, timestamp);

			if (movementMeasurement != null) {
				return new ResponseEntity<MovementMeasurement>(
						movementMeasurement, HttpStatus.OK);
			} else {
				return new ResponseEntity<MovementMeasurement>(
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<MovementMeasurement>(
					HttpStatus.BAD_REQUEST);
		}

	}

}
