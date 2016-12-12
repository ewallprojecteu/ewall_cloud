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
import eu.ewall.platform.commons.datamodel.measure.IlluminanceMeasurement;
import eu.ewall.platform.commons.datamodel.measure.MovementMeasurement;
import eu.ewall.platform.commons.datamodel.measure.TemperatureMeasurement;
import eu.ewall.platform.profilingserver.services.HumidityMeasurementServiceImpl;
import eu.ewall.platform.profilingserver.services.IlluminanceMeasurementServiceImpl;
import eu.ewall.platform.profilingserver.services.MovementMeasurementServiceImpl;
import eu.ewall.platform.profilingserver.services.TemperatureMeasurementServiceImpl;


/**
 * The Class EnvironmentSensingController.
 */
@RestController
@RequestMapping(value = "/users/{username}/environment")
public class EnvironmentSensingController {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(EnvironmentSensingController.class);

	/** The temperature measurement service. */
	@Autowired
	private TemperatureMeasurementServiceImpl temperatureMeasurementService;
	
	/** The humidity measurement service. */
	@Autowired
	private HumidityMeasurementServiceImpl humidityMeasurementService;
	
	/** The illuminance measurement service. */
	@Autowired
	private IlluminanceMeasurementServiceImpl illuminanceMeasurementService;
	
	/** The movement measurement service. */
	@Autowired
	private MovementMeasurementServiceImpl movementMeasurementService;

	/**
	 * Instantiates a new environment sensing controller.
	 */
	public EnvironmentSensingController() {
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
	@RequestMapping(value = "/temperature", method = RequestMethod.GET)
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
	 * Gets the humidity measurements between timestamps.
	 *
	 * @param username the username
	 * @param from the from
	 * @param to the to
	 * @param room_name the room_name
	 * @return the humidity measurements between timestamps
	 */
	@RequestMapping(value = "/humidity", method = RequestMethod.GET)
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
	 * Gets the illuminance measurements between timestamps.
	 *
	 * @param username the username
	 * @param from the from
	 * @param to the to
	 * @param room_name the room_name
	 * @return the illuminance measurements between timestamps
	 */
	@RequestMapping(value = "/illuminance", method = RequestMethod.GET)
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
	 * Gets the movement measurements between timestamps.
	 *
	 * @param username the username
	 * @param from the from
	 * @param to the to
	 * @param room_name the room_name
	 * @return the movement measurements between timestamps
	 */
	@RequestMapping(value = "/movement", method = RequestMethod.GET)
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

}
