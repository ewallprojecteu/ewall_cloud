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
import eu.ewall.platform.commons.datamodel.sensing.VisualSensing;
import eu.ewall.platform.profilingserver.services.VisualSensingServiceImpl;


/**
 * The Class VisualSensingController.
 */
@RestController
@RequestMapping(value = "/users/{username}/visual")
public class VisualSensingController {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(VisualSensingController.class);

	/** The visual sensing service. */
	@Autowired
	private VisualSensingServiceImpl visualSensingService;


	/**
	 * Instantiates a new visual sensing controller.
	 */
	public VisualSensingController() {
	}

	

	/**
	 * Gets the visual sensing between timestamps.
	 *
	 * @param username the username
	 * @param from the from
	 * @param to the to
	 * @return the visual sensing between timestamps
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<VisualSensing>> getVisualSensingBetweenTimestamps(
			@PathVariable String username, @RequestParam("from") long from,
			@RequestParam("to") long to) {

		LOG.debug("getVisualSensingBetweenTimestamps: " + from + " and "
				+ to);

		List<VisualSensing> visualSensingList;

		try {
			visualSensingList = visualSensingService
					.getVisualSensingBetweenTimestamps(username, from,
							to);

			if (visualSensingList != null) {
				return new ResponseEntity<List<VisualSensing>>(
						visualSensingList, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<VisualSensing>>(
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<List<VisualSensing>>(
					HttpStatus.BAD_REQUEST);
		}

	}
	

	
}
