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

import eu.ewall.platform.commons.datamodel.sensing.SpeakerSensing;
import eu.ewall.platform.profilingserver.services.SpeakerSensingServiceImpl;

/**
 * The Class SpeakerSensingController.
 * 
 * @author emirmos
 */
@RestController
@RequestMapping(value = "/users/{username}/speaker")
public class SpeakerSensingController {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(SpeakerSensingController.class);

	/** The speaker sensing service. */
	@Autowired
	private SpeakerSensingServiceImpl speakerSensingService;


	/**
	 * Instantiates a new speaker sensing controller.
	 */
	public SpeakerSensingController() {
	}

	

	/**
	 * Gets the speaker sensing between timestamps.
	 *
	 * @param username the username
	 * @param from the from
	 * @param to the to
	 * @return the speaker sensing between timestamps
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<SpeakerSensing>> getSpeakerSensingBetweenTimestamps(
			@PathVariable String username, @RequestParam("from") long from,
			@RequestParam("to") long to) {

		LOG.debug("getSpeakerSensingBetweenTimestamps: " + from + " and "
				+ to);

		List<SpeakerSensing> speakerSensingList;

		try {
			speakerSensingList = speakerSensingService
					.getSpeakerSensingBetweenTimestamps(username, from,
							to);

			if (speakerSensingList != null) {
				return new ResponseEntity<List<SpeakerSensing>>(
						speakerSensingList, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<SpeakerSensing>>(
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<List<SpeakerSensing>>(
					HttpStatus.BAD_REQUEST);
		}

	}
	

	
}
