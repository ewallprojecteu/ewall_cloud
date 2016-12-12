/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.cloudgateway.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.ewall.platform.commons.datamodel.device.Device;
import eu.ewall.platform.commons.datamodel.marshalling.json.DM2JsonObjectMapper;
import eu.ewall.platform.commons.datamodel.measure.MattressPressureSensing;
import eu.ewall.platform.middleware.cloudgateway.services.DevicesServiceImpl;
import eu.ewall.platform.middleware.cloudgateway.services.MattressPressureSensingServiceImpl;

/**
 * The Class AccelerometerMeasurementsController.
 *
 * @author emirmos
 */
@RestController
@RequestMapping("/sensingenvironments/{sensing_environment_id}/devices/{device_id}/furniture")
public class MattressPressureSensingController {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(MattressPressureSensingController.class);

	@Autowired
	private MattressPressureSensingServiceImpl mattressPressureSensingService;
	
	/** The device handler. */
	@Autowired
	private DevicesServiceImpl devicesServiceImpl;

	/**
	 * Adds the new content.
	 *
	 * @param content
	 *            the content
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param device_id
	 *            the device_id
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/add")
	public ResponseEntity<String> addNewContent(@RequestBody String content,
			@PathVariable String sensing_environment_id,
			@PathVariable String device_id) {

		LOG.debug("addNewContent() called with parameter sensing environmend id = " + sensing_environment_id 
				+ ", device id = " + device_id
				+ " and content size = " + content.length());

		try {

			MattressPressureSensing mattressPressureSensing = (MattressPressureSensing) DM2JsonObjectMapper
					.readValueAsString(content, MattressPressureSensing.class);

			// we will use shorter links for now
			// accMeasurement.add(new
			// Link(sensing_environment_id).withRel(SensingEnvironment.class.getSimpleName()));

			mattressPressureSensingService.addMattressPressureSensing(sensing_environment_id, mattressPressureSensing);

			return new ResponseEntity<String>(HttpStatus.CREATED);

		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

	}

	@RequestMapping(method = RequestMethod.GET, value = "/lastTimestamp")
	public ResponseEntity<Long> getLastCollectionsTimestamp(
			@PathVariable String sensing_environment_id,
			@PathVariable String device_id) {

		LOG.debug("getLastCollectionsTimestamp() called with parameter sensing environmend id = "
				+ sensing_environment_id + ", device id = " + device_id);

		Device device = devicesServiceImpl.getDevice(device_id);
		if (device == null) {
			LOG.warn("Device with id {} not found", device_id);
			return new ResponseEntity<Long>(HttpStatus.NOT_FOUND);
		}
		
		try {
			long lastTimestamp = mattressPressureSensingService
					.getLastCollectionsTimestamp(sensing_environment_id, device.getIndoorPlaceName());

			return new ResponseEntity<Long>(lastTimestamp, HttpStatus.OK);

		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<Long>(HttpStatus.BAD_REQUEST);
		}
	}
}
