/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.profilingserver.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.ewall.platform.commons.datamodel.measure.BloodPressureMeasurement;
import eu.ewall.platform.commons.datamodel.measure.HeartRateMeasurement;
import eu.ewall.platform.commons.datamodel.measure.OxygenSaturationMeasurement;
import eu.ewall.platform.profilingserver.services.VitalsSensingServiceImpl;
import eu.ewall.platform.profilingserver.validation.ProfilingServerInputValidator;

/**
 * The Class VitalsSensingController.
 * 
 * @author emirmos
 */
@RestController
@RequestMapping(value = "/users/{username}/vitals")
public class VitalsSensingController {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(VitalsSensingController.class);

	/** The vitals sensing service. */
	@Autowired
	private VitalsSensingServiceImpl vitalsSensingService;

	/** The input validator. */
	private ProfilingServerInputValidator inputValidator;

	/**
	 * Instantiates a new health sensing controller.
	 */
	public VitalsSensingController() {
		this.inputValidator = new ProfilingServerInputValidator();
	}

	/**
	 * Gets the heart rate between timestamps.
	 *
	 * @param username
	 *            the username
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param latestEvents
	 *            the latest events
	 * @return the heart rate between timestamps
	 */
	@RequestMapping(value = "/heartrate", method = RequestMethod.GET)
	public ResponseEntity<List<HeartRateMeasurement>> getHeartRateBetweenTimestamps(
			@PathVariable String username,
			@RequestParam(value = "from", required = false) Long from,
			@RequestParam(value = "to", required = false) Long to,
			@RequestParam(value = "latestevents", required = false) Integer latestEvents) {

		LOG.debug("getHeartRateBetweenTimestamps: " + from + " and " + to
				+ ", latest = " + latestEvents);

		List<HeartRateMeasurement> heartRateList;

		inputValidator.validateLatestEvents(latestEvents);
		inputValidator.validateTimestamps(from, to);

		heartRateList = vitalsSensingService.getHeartRateBetweenTimestamps(
				username, from, to, latestEvents);

		if (heartRateList != null) {
			return new ResponseEntity<List<HeartRateMeasurement>>(
					heartRateList, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<HeartRateMeasurement>>(
					HttpStatus.NOT_FOUND);
		}

	}

	/**
	 * Gets the oxygen saturation between timestamps.
	 *
	 * @param username
	 *            the username
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param latestEvents
	 *            the latest events
	 * @return the oxygen saturation between timestamps
	 */
	@RequestMapping(value = "/oxygensaturation", method = RequestMethod.GET)
	public ResponseEntity<List<OxygenSaturationMeasurement>> getOxygenSaturationBetweenTimestamps(
			@PathVariable String username,
			@RequestParam(value = "from", required = false) Long from,
			@RequestParam(value = "to", required = false) Long to,
			@RequestParam(value = "latestevents", required = false) Integer latestEvents) {

		LOG.debug("getOxygenSaturationBetweenTimestamps: " + from + " and "
				+ to + ", latest = " + latestEvents);

		List<OxygenSaturationMeasurement> oxygenSaturationList;

		inputValidator.validateLatestEvents(latestEvents);
		inputValidator.validateTimestamps(from, to);

		oxygenSaturationList = vitalsSensingService
				.getOxygenSaturationBetweenTimestamps(username, from, to,
						latestEvents);

		if (oxygenSaturationList != null) {
			return new ResponseEntity<List<OxygenSaturationMeasurement>>(
					oxygenSaturationList, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<OxygenSaturationMeasurement>>(
					HttpStatus.NOT_FOUND);
		}

	}

	/**
	 * Gets the blood pressure between timestamps.
	 *
	 * @param username
	 *            the username
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param latestEvents
	 *            the latest events
	 * @return the blood pressure between timestamps
	 */
	@RequestMapping(value = "/bloodpressure", method = RequestMethod.GET)
	public ResponseEntity<List<BloodPressureMeasurement>> getBloodPressureBetweenTimestamps(
			@PathVariable String username,
			@RequestParam(value = "from", required = false) Long from,
			@RequestParam(value = "to", required = false) Long to,
			@RequestParam(value = "latestevents", required = false) Integer latestEvents) {

		LOG.debug("getBloodPressureBetweenTimestamps: " + from + " and " + to
				+ ", latest = " + latestEvents);

		List<BloodPressureMeasurement> bloodPRessureList;

		inputValidator.validateLatestEvents(latestEvents);
		inputValidator.validateTimestamps(from, to);

		bloodPRessureList = vitalsSensingService
				.getBloodPressureBetweenTimestamps(username, from, to,
						latestEvents);

		if (bloodPRessureList != null) {
			return new ResponseEntity<List<BloodPressureMeasurement>>(
					bloodPRessureList, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<BloodPressureMeasurement>>(
					HttpStatus.NOT_FOUND);
		}

	}

}
