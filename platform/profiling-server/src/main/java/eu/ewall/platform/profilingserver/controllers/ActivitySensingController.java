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
 * The Class ActivitySensingController.
 * 
 * @author emirmos
 */
@RestController
@RequestMapping(value = "/users/{username}/activity")
public class ActivitySensingController {

	
	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(ActivitySensingController.class);

	/** The accelerometer measurement service. */
	@Autowired
	private AccelerometerMeasurementServiceImpl accelerometerMeasurementService;


	/**
	 * Instantiates a new activity sensing controller.
	 */
	public ActivitySensingController() {
	}


	/**
	 * Gets the activity data between timestamps.
	 *
	 * @param username the username
	 * @param from the from
	 * @param to the to
	 * @return the activity data between timestamps
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<AccelerometerMeasurement>> getActivityDataBetweenTimestamps(
			@PathVariable String username, @RequestParam("from") long from,
			@RequestParam("to") long to) {

		LOG.debug("getActivityDataBetweenTimestamps: " + from
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

	

}
