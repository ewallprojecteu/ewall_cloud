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

import eu.ewall.platform.commons.datamodel.measure.CarbonMonoxideMeasurement;
import eu.ewall.platform.commons.datamodel.measure.LiquefiedPetroleumGasMeasurement;
import eu.ewall.platform.commons.datamodel.measure.NaturalGasMeasurement;
import eu.ewall.platform.profilingserver.services.GasesMeasurementServiceImpl;

/**
 * The Class HumidityMeasurementsController.
 * 
 * @author EMIRMOS
 * 
 */
@RestController
@RequestMapping(value = "/users/{username}/environment/gases")
public class GasesMeasurementsController {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(GasesMeasurementsController.class);

	/** The humidity measurement service. */
	@Autowired
	private GasesMeasurementServiceImpl gasesMeasurementService;

	/**
	 * Instantiates a new measurements controller.
	 */
	public GasesMeasurementsController() {
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
	@RequestMapping(value = "/co", method = RequestMethod.GET)
	public ResponseEntity<List<CarbonMonoxideMeasurement>> getCarbonMonoxideMeasurementsBetweenTimestamps(
			@PathVariable String username, @RequestParam("from") long from,
			@RequestParam("to") long to, @RequestParam(value = "room", required = false) String room_name) {

		LOG.debug("getCarbonMonoxideMeasurementsBetweenTimestamps: " + from + " and "
				+ to);

		List<CarbonMonoxideMeasurement> carbonMonoxideMeasurementList;

		try {
			carbonMonoxideMeasurementList = gasesMeasurementService
					.getCarbonMonoxideMeasurementBetweenTimestamps(username, from,
							to, room_name);

			if (carbonMonoxideMeasurementList != null) {
				return new ResponseEntity<List<CarbonMonoxideMeasurement>>(
						carbonMonoxideMeasurementList, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<CarbonMonoxideMeasurement>>(
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<List<CarbonMonoxideMeasurement>>(
					HttpStatus.BAD_REQUEST);
		}

	}
	
	/**
	 * Gets the LPG measurements between timestamps for room name.
	 *
	 * @param username the username
	 * @param from the from
	 * @param to the to
	 * @param room_name the room_name
	 * @return the LPG  measurements between timestamps
	 */
	@RequestMapping(value = "/lpg", method = RequestMethod.GET)
	public ResponseEntity<List<LiquefiedPetroleumGasMeasurement>> getLiquefiedPetroleumGasMeasurementBetweenTimestamps(
			@PathVariable String username, @RequestParam("from") long from,
			@RequestParam("to") long to, @RequestParam(value = "room", required = false) String room_name) {

		LOG.debug("getLiquefiedPetroleumGasMeasurementBetweenTimestamps: " + from + " and "
				+ to);

		List<LiquefiedPetroleumGasMeasurement> liquefiedPetroleumGasMeasurement;

		try {
			liquefiedPetroleumGasMeasurement = gasesMeasurementService
					.getLiquefiedPetroleumGasMeasurementBetweenTimestamps(username, from,
							to, room_name);

			if (liquefiedPetroleumGasMeasurement != null) {
				return new ResponseEntity<List<LiquefiedPetroleumGasMeasurement>>(
						liquefiedPetroleumGasMeasurement, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<LiquefiedPetroleumGasMeasurement>>(
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<List<LiquefiedPetroleumGasMeasurement>>(
					HttpStatus.BAD_REQUEST);
		}

	}

	
	/**
	 * Gets the natural gas measurements between timestamps for room name.
	 *
	 * @param username the username
	 * @param from the from
	 * @param to the to
	 * @param room_name the room_name
	 * @return the natural gas  measurements between timestamps
	 */
	@RequestMapping(value = "/ng", method = RequestMethod.GET)
	public ResponseEntity<List<NaturalGasMeasurement>> getNaturalGasMeasurementBetweenTimestamps(
			@PathVariable String username, @RequestParam("from") long from,
			@RequestParam("to") long to, @RequestParam(value = "room", required = false) String room_name) {

		LOG.debug("getNaturalGasMeasurementBetweenTimestamps: " + from + " and "
				+ to);

		List<NaturalGasMeasurement> naturalGasMeasurement;

		try {
			naturalGasMeasurement = gasesMeasurementService
					.getNaturalGasMeasurementBetweenTimestamps(username, from,
							to, room_name);

			if (naturalGasMeasurement != null) {
				return new ResponseEntity<List<NaturalGasMeasurement>>(
						naturalGasMeasurement, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<NaturalGasMeasurement>>(
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<List<NaturalGasMeasurement>>(
					HttpStatus.BAD_REQUEST);
		}

	}
	
}
