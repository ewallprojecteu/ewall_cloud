/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.cloudgateway.controllers;

import org.jasypt.util.text.BasicTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import eu.ewall.platform.commons.datamodel.device.Device;
import eu.ewall.platform.commons.datamodel.marshalling.json.DM2JsonObjectMapper;
import eu.ewall.platform.commons.datamodel.sensing.EnvironmentalSensing;
import eu.ewall.platform.middleware.cloudgateway.services.DevicesServiceImpl;
import eu.ewall.platform.middleware.cloudgateway.services.SimpleMeasurementsServiceImpl;


/**
 * The Class MeasurementsController.
 *
 * @author emirmos
 */
// TODO there will be resource containter uri to crud data, this is just for
// test
@RestController
@RequestMapping("/sensingenvironments/{sensing_environment_id}/devices/{device_id}/environmental")
public class SimpleMeasurementsController {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(SimpleMeasurementsController.class);

	/** The device handler. */
	@Autowired
	private DevicesServiceImpl devicesServiceImpl;
	
	@Autowired
	private SimpleMeasurementsServiceImpl measurementsService;

	@Value("${secret}")
	private String secret;
	

	/**
	 * Adds the new content.
	 *
	 * @param content
	 *            the content

	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param device_id
	 *            the device_id
	 * @param builder
	 *            the builder
	 * @param encrypted
	 *            the encrypted
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/add")
	public ResponseEntity<String> addNewContent(@RequestBody String content,
			@PathVariable String sensing_environment_id,
			@PathVariable String device_id, UriComponentsBuilder builder,
			@RequestParam(required = false) boolean encrypted) {

		LOG.debug("addNewContent() called with parameter sensing environmend id = " + sensing_environment_id 
				+ ", device id = " + device_id
				+ " and content size = " + content.length());

			
		try {
			if (encrypted) {
				BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
				
				if (secret != null)
					textEncryptor.setPassword(secret);

				content = textEncryptor.decrypt(content);
				LOG.debug("Content decrypted.");
			}

			
			EnvironmentalSensing environmentalSensing = (EnvironmentalSensing) DM2JsonObjectMapper
					.readValueAsString(content, EnvironmentalSensing.class);
			

			if (measurementsService.addMeasurement(sensing_environment_id, device_id, environmentalSensing)) {

				return new ResponseEntity<String>(HttpStatus.CREATED);

			} else {
				LOG.warn("measurementId not found");
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * Update content.
	 *
	 * @param content
	 *            the content
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param device_id
	 *            the device_id
	 * @param content_id
	 *            the content_id
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/{content_id}")
	public ResponseEntity<String> updateContent(@RequestBody String content,
			@PathVariable String sensing_environment_id,
			@PathVariable String device_id, @PathVariable String content_id) {

		LOG.debug("updateContent() called with parameter sensing environmend id = " + sensing_environment_id 
				+ ", device id = " + device_id
				+ " and content size = " + content.length());

		return new ResponseEntity<String>(HttpStatus.METHOD_NOT_ALLOWED);

	}

	/**
	 * Delete content.
	 *
	 * @param content_id
	 *            the content_id
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{content_id}")
	public ResponseEntity<String> deleteContent(@PathVariable String content_id) {

		LOG.debug("deleteContent: " + content_id);

		return new ResponseEntity<String>("Method is not implmented",
				HttpStatus.NOT_IMPLEMENTED);

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
			long lastTimestamp = measurementsService
					.getLastCollectionsTimestamp(sensing_environment_id, device.getIndoorPlaceName());

			return new ResponseEntity<Long>(lastTimestamp, HttpStatus.OK);

		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<Long>(HttpStatus.BAD_REQUEST);
		}
	}

}
