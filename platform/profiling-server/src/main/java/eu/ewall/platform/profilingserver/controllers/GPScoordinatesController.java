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

import eu.ewall.platform.commons.datamodel.location.GPScoordinates;
import eu.ewall.platform.profilingserver.services.GPScoordinatesServiceImpl;

/**
 * The Class GPScoordinatesController.
 *
 * @author eandgrg
 */

// GET for timestamp
// http://localhost:8080/profiling-server/users/testusername/gpscoordinates/timestamp/1234567

@RestController
@RequestMapping(value = "/users/{username}/gpscoordinates")
public class GPScoordinatesController {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(GPScoordinatesController.class);

	/** The gps coordinates service. */
	@Autowired
	private GPScoordinatesServiceImpl gpsCoordinatesService;

	/**
	 * Instantiates a new GPS coordinates controller.
	 */
	public GPScoordinatesController() {
	}

	/**
	 * Gets the GPS coordinates by timestamp.
	 *
	 * @param username the username
	 * @param timestamp            the timestamp
	 * @return the GPS coordinates by timestamp
	 */
	@RequestMapping(value = "/timestamp/{timestamp}", method = RequestMethod.GET)
	public ResponseEntity<GPScoordinates> getGPScoordinatesByTimestamp(
			@PathVariable String username,
			@RequestParam("timestamp") long timestamp) {

		LOG.debug("getGPScoordinatesByTimestamp: " + timestamp);

		GPScoordinates gpsCoordinates;

		try {
			gpsCoordinates = gpsCoordinatesService
					.getGPScoordinatesByTimestamp(username, timestamp);

			if (gpsCoordinates != null) {
				return new ResponseEntity<GPScoordinates>(gpsCoordinates,
						HttpStatus.OK);
			} else {
				return new ResponseEntity<GPScoordinates>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<GPScoordinates>(HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * Gets the GPS coordinates by name.
	 *
	 * @param username the username
	 * @param name            the name
	 * @return the GPS coordinates by name
	 */
	@RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
	public ResponseEntity<GPScoordinates> getGPScoordinatesByName(
			@PathVariable String username, @RequestParam("name") String name) {

		LOG.debug("getGPScoordinatesByName: " + name);

		GPScoordinates gpsCoordinates;

		try {
			gpsCoordinates = gpsCoordinatesService.getGPScoordinatesByName(
					username, name);

			if (gpsCoordinates != null) {
				return new ResponseEntity<GPScoordinates>(gpsCoordinates,
						HttpStatus.OK);
			} else {
				return new ResponseEntity<GPScoordinates>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<GPScoordinates>(HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * Gets the all gps coordinates for user.
	 *
	 * @param username the username
	 * @return the all gps coordinates for user
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<List<GPScoordinates>> getAllGPScoordinatesForUser(
			@PathVariable String username) {

		LOG.debug("Getting all GPScoordinates");

		List<GPScoordinates> gpsCoordinatesList;

		try {
			gpsCoordinatesList = gpsCoordinatesService
					.getAllGPScoordinates(username);

			if (gpsCoordinatesList != null) {
				return new ResponseEntity<List<GPScoordinates>>(
						gpsCoordinatesList, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<GPScoordinates>>(
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<List<GPScoordinates>>(
					HttpStatus.BAD_REQUEST);
		}
	}

}
