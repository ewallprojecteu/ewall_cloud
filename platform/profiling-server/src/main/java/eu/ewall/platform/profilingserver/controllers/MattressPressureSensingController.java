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

import eu.ewall.platform.commons.datamodel.measure.MattressPressureSensing;
import eu.ewall.platform.profilingserver.services.MattressPressureSensingServiceImpl;

/**
 * The Class MattressPressureSensingController.
 * 
 * @author EMIRMOS
 * 
 */
@RestController
@RequestMapping(value = "/users/{username}/environment/mattress_pressure")
public class MattressPressureSensingController {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(MattressPressureSensingController.class);

	/** The humidity measurement service. */
	@Autowired
	private MattressPressureSensingServiceImpl mattressPressureSensingService;

	/**
	 * Instantiates a new measurements controller.
	 */
	public MattressPressureSensingController() {
	}

	
	/**
	 * Gets the MattressPressureSensing between timestamps for room name.
	 *
	 * @param username the username
	 * @param from the from
	 * @param to the to
	 * @param room_name the room_name
	 * @return the MattressPressureSensing measurements between timestamps
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<MattressPressureSensing>> getMattressPressureSensingBetweenTimestamps(
			@PathVariable String username, @RequestParam("from") long from,
			@RequestParam("to") long to, @RequestParam(value = "room", required = false) String room_name) {

		LOG.debug("getMattressPressureSensingBetweenTimestamps: " + from + " and "
				+ to);

		List<MattressPressureSensing> mattressPressureSensingList;

		try {
			mattressPressureSensingList = mattressPressureSensingService
					.getMattressPressureSensingBetweenTimestamps(username, from,
							to, room_name);

			if (mattressPressureSensingList != null) {
				return new ResponseEntity<List<MattressPressureSensing>>(
						mattressPressureSensingList, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<MattressPressureSensing>>(
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<List<MattressPressureSensing>>(
					HttpStatus.BAD_REQUEST);
		}

	}
	

	
}
