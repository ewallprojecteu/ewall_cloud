/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.cloudgateway.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import eu.ewall.platform.commons.datamodel.device.Device;
import eu.ewall.platform.commons.datamodel.device.DeviceType;
import eu.ewall.platform.commons.datamodel.ewallsystem.ActuatorCommand;
import eu.ewall.platform.commons.datamodel.ewallsystem.ActuatorCommandType;
import eu.ewall.platform.commons.datamodel.ewallsystem.SensingEnvironment;
import eu.ewall.platform.middleware.cloudgateway.services.DevicesServiceImpl;
import eu.ewall.platform.middleware.cloudgateway.services.RemoteProxyLongPollService;
import eu.ewall.platform.middleware.cloudgateway.services.SensingEnvironmentsServiceImpl;

/**
 * The Class ActuatorsController.
 *
 * @author emirmos
 */
@RestController
@RequestMapping("/users/{user_name}/actuators")
public class ActuatorsController {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(ActuatorsController.class);

	/** The long poll service. */
	@Autowired
	private RemoteProxyLongPollService longPollService;

	/** The sensing environments service. */
	@Autowired
	private SensingEnvironmentsServiceImpl sensingEnvironmentsService;

	/** The devices service. */
	@Autowired
	private DevicesServiceImpl devicesService;

	/**
	 * Update actuator command level.
	 *
	 * @param user_name
	 *            the user_name
	 * @param actuatorCommand
	 *            the actuator command
	 * @return the deferred result
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/sendCommand")
	public DeferredResult<ResponseEntity<String>> sendActuatorCommand(
			@PathVariable String user_name,
			@RequestBody ActuatorCommand actuatorCommand) {

		LOG.debug("sendActuatorCommand");

		DeferredResult<ResponseEntity<String>> result = null;

		try {
			String sensingEnvironmentId = getSensEnvForPrimaryUser(user_name);
			result = longPollService.setRPActuatorCommand(actuatorCommand,
					sensingEnvironmentId);

		} catch (Exception e) {
			LOG.warn(e.getMessage());
			DeferredResult<ResponseEntity<String>> def = new DeferredResult<ResponseEntity<String>>();
			def.setResult(new ResponseEntity<String>(
					HttpStatus.INTERNAL_SERVER_ERROR));
		}

		return result;
	}

	/**
	 * Send actuator command via request paramaters.
	 *
	 * @param user_name the user_name
	 * @param room_name the room_name
	 * @param actuator_name the actuator_name
	 * @param type the type
	 * @param value the value
	 * @return the deferred result
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/sendCommandParameters")
	public DeferredResult<ResponseEntity<String>> sendActuatorCommandViaRequestParamaters(
			@PathVariable String user_name,
			@RequestParam(value = "room", required = true) String room_name,
			@RequestParam(value = "name", required = true) String actuator_name,
			@RequestParam(value = "cmd_type", required = true) ActuatorCommandType type,
			@RequestParam(value = "cmd_value", required = true) int value) {

		LOG.debug("sendActuatorCommandViaRequestParamaters");

		DeferredResult<ResponseEntity<String>> result = null;

		ActuatorCommand actuatorCommand = new ActuatorCommand(room_name,
				actuator_name, value, type);

		try {
			String sensingEnvironmentId = getSensEnvForPrimaryUser(user_name);
			result = longPollService.setRPActuatorCommand(actuatorCommand,
					sensingEnvironmentId);

		} catch (Exception e) {
			LOG.warn(e.getMessage());
			DeferredResult<ResponseEntity<String>> def = new DeferredResult<ResponseEntity<String>>();
			def.setResult(new ResponseEntity<String>(
					HttpStatus.INTERNAL_SERVER_ERROR));
		}

		return result;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/lights/setColor")
	public DeferredResult<ResponseEntity<String>> setAllLightsRGB(
			@PathVariable String user_name,
			@RequestParam(value = "rgb", required = true) Integer rgb
) {

		LOG.debug("setAllLightsRGB");

		DeferredResult<ResponseEntity<String>> result = null;

		ActuatorCommand actuatorCommand = new ActuatorCommand("",
				"", rgb, ActuatorCommandType.SET_COLOR);

		try {
			String sensingEnvironmentId = getSensEnvForPrimaryUser(user_name);
			result = longPollService.setRPActuatorCommand(actuatorCommand,
					sensingEnvironmentId);

		} catch (Exception e) {
			LOG.warn(e.getMessage());
			DeferredResult<ResponseEntity<String>> def = new DeferredResult<ResponseEntity<String>>();
			def.setResult(new ResponseEntity<String>(
					HttpStatus.INTERNAL_SERVER_ERROR));
		}

		return result;
	}

	
	/**
	 * Send actuator command by id.
	 *
	 * @param user_name the user_name
	 * @param actuator_id the actuator_id
	 * @param type the type
	 * @param value the value
	 * @return the deferred result
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/{actuator_id}/sendCommand")
	public DeferredResult<ResponseEntity<String>> sendActuatorCommandById(
			@PathVariable String user_name,
			@PathVariable String actuator_id,
			@RequestParam(value = "cmd_type", required = true) ActuatorCommandType type,
			@RequestParam(value = "cmd_value", required = true) int value) {

		LOG.debug("sendActuatorCommandById");

		DeferredResult<ResponseEntity<String>> result = null;

		Device device = devicesService.getDevice(actuator_id);
		if (device == null) {
			LOG.warn("Actuator with id " + actuator_id + " not found");
			DeferredResult<ResponseEntity<String>> def = new DeferredResult<ResponseEntity<String>>();
			def.setResult(new ResponseEntity<String>(HttpStatus.NOT_FOUND));
			return def;
		}

		String actuator_name = device.getName();
		String room_name = device.getIndoorPlaceName();

		ActuatorCommand actuatorCommand = new ActuatorCommand(room_name,
				actuator_name, value, type);

		try {
			String sensingEnvironmentId = getSensEnvForPrimaryUser(user_name);
			result = longPollService.setRPActuatorCommand(actuatorCommand,
					sensingEnvironmentId);

		} catch (Exception e) {
			LOG.warn(e.getMessage());
			result = new DeferredResult<ResponseEntity<String>>();
			result.setResult(new ResponseEntity<String>(
					HttpStatus.INTERNAL_SERVER_ERROR));
		}

		return result;
	}

	/**
	 * Gets the sens env for primary user.
	 *
	 * @param user_name
	 *            the user_name
	 * @return the sens env for primary user
	 */
	private String getSensEnvForPrimaryUser(String user_name) {
		List<SensingEnvironment> sensEnvs = sensingEnvironmentsService
				.getSensingEnvironments();
		for (SensingEnvironment sensEnv : sensEnvs) {
			if (sensEnv.getPrimaryUser().equals(user_name)) {
				return sensEnv.getUuid().toString();
			}
		}
		return null;
	}
	

	/**
	 * Gets the actuators.
	 *
	 * @param user_name the user_name
	 * @param type the type
	 * @param room_name the room_name
	 * @return the actuators
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Device>> getActuators(
			@PathVariable String user_name,
			@RequestParam(value = "type", required = false) DeviceType type,
			@RequestParam(value = "room", required = false) String room_name
			) {

		LOG.debug("getting list of all actuator in users sensing environment "
				+ user_name);
		

		String sensing_environment_id = this.getSensEnvForPrimaryUser(user_name);
		if (sensing_environment_id == null) {
			LOG.warn("Sensing environment for user "+user_name+" not found.");
			return new ResponseEntity<List<Device>>(HttpStatus.BAD_REQUEST);
		}

		try {
			List<Device> devices = devicesService.getDevices(
					sensing_environment_id, type, room_name, true,
					null);
			if (devices != null) {
				return new ResponseEntity<List<Device>>(devices, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<Device>>(HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<List<Device>>(HttpStatus.BAD_REQUEST);
		}

	}
}
