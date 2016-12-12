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

import eu.ewall.platform.commons.datamodel.measure.DoorStatus;
import eu.ewall.platform.profilingserver.services.DoorStatusServiceImpl;

/**
 * The Class HumidityMeasurementsController.
 * 
 * @author EMIRMOS
 * 
 */
@RestController
@RequestMapping(value = "/users/{username}/environment/doors")
public class DoorStatusController {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(DoorStatusController.class);

	/** The humidity measurement service. */
	@Autowired
	private DoorStatusServiceImpl doorStatusService;

	/**
	 * Instantiates a new measurements controller.
	 */
	public DoorStatusController() {
	}

	
	/**
	 * Gets the carbon monoxide measurements between timestamps for room name.
	 *
	 * @param username the username
	 * @param from the from
	 * @param to the to
	 * @param room_name the room_name
	 * @return the carbon monoxide  measurements between timestamps
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<DoorStatus>> getDoorStatusBetweenTimestamps(
			@PathVariable String username, @RequestParam("from") long from,
			@RequestParam("to") long to, @RequestParam(value = "room", required = false) String room_name) {

		LOG.debug("getDoorStatusBetweenTimestamps: " + from + " and "
				+ to);

		List<DoorStatus> doorStatusList;

		try {
			doorStatusList = doorStatusService
					.getDoorStatusBetweenTimestamps(username, from,
							to, room_name);

			if (doorStatusList != null) {
				return new ResponseEntity<List<DoorStatus>>(
						doorStatusList, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<DoorStatus>>(
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<List<DoorStatus>>(
					HttpStatus.BAD_REQUEST);
		}

	}
	

	
}
