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

import eu.ewall.platform.commons.datamodel.sensing.AppliancePowerSensing;
import eu.ewall.platform.commons.datamodel.sensing.ApplianceType;
import eu.ewall.platform.profilingserver.services.AppliancePowerSensingServiceImpl;


/**
 * The Class SpeakerSensingController.
 * 
 * @author emirmos
 */
@RestController
@RequestMapping(value = "/users/{username}/appliances")
public class AppliancePowerSensingController {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(AppliancePowerSensingController.class);

	/** The appliance power sensing service. */
	@Autowired
	private AppliancePowerSensingServiceImpl appliancePowerSensingService;

	/**
	 * Instantiates a new appliance power sensing controller.
	 */
	public AppliancePowerSensingController() {
	}

	/**
	 * Gets the appliance power sensing between timestamps.
	 *
	 * @param username            the username
	 * @param from            the from
	 * @param to            the to
	 * @param applianceName            the appliance name
	 * @param applianceType the appliance type
	 * @param roomName            the room name
	 * @return the appliance power sensing between timestamps
	 */
	@RequestMapping(value = "/status", method = RequestMethod.GET)
	public ResponseEntity<List<AppliancePowerSensing>> getAppliancePowerSensingBetweenTimestamps(
			@PathVariable String username,
			@RequestParam("from") long from,
			@RequestParam("to") long to,
			@RequestParam(value = "name", required = false) String applianceName,
			@RequestParam(value = "type", required = false) ApplianceType applianceType,
			@RequestParam(value = "room", required = false) String roomName) {

		LOG.debug("getAppliancePowerSensingBetweenTimestamps: " + from
				+ " and " + to);

		List<AppliancePowerSensing> appliancePowerSensingList;

		try {
			appliancePowerSensingList = appliancePowerSensingService
					.getAppliancePowerSensingBetweenTimestamps(username, from,
							to, applianceName, applianceType, roomName);

			if (appliancePowerSensingList != null) {
				return new ResponseEntity<List<AppliancePowerSensing>>(
						appliancePowerSensingList, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<AppliancePowerSensing>>(
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<List<AppliancePowerSensing>>(
					HttpStatus.BAD_REQUEST);
		}

	}

}
