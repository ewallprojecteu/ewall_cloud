package eu.ewall.platform.middleware.cloudgateway.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.ewall.platform.commons.datamodel.device.Device;
import eu.ewall.platform.commons.datamodel.device.DeviceType;
import eu.ewall.platform.middleware.cloudgateway.services.DevicesServiceImpl;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * 
 * @author emirmos
 *
 */
@RestController
@RequestMapping("/sensingenvironments/{sensing_environment_id}/devices")
public class DevicesController {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(DevicesController.class);

	/** The env. */
	@Autowired
	private Environment env;

	/** The devices service. */
	@Autowired
	private DevicesServiceImpl devicesService;

	/**
	 * Gets the devices.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param type
	 *            the type
	 * @param room_name
	 *            the room_name
	 * @param isActuator
	 *            the is actuator
	 * @param isSensor
	 *            the is sensor
	 * @return the devices
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Device>> getDevices(
			@PathVariable String sensing_environment_id,
			@RequestParam(value = "type", required = false) DeviceType type,
			@RequestParam(value = "room", required = false) String room_name,
			@RequestParam(value = "actuator", required = false) Boolean isActuator,
			@RequestParam(value = "sensor", required = false) Boolean isSensor) {

		LOG.debug("getting list of all devices in sensing environment "
				+ sensing_environment_id);

		try {
			List<Device> devices = devicesService.getDevices(
					sensing_environment_id, type, room_name, isActuator,
					isSensor);
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

	/**
	 * Adds the new device.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param device
	 *            the device
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<String> addNewDevice(
			@PathVariable String sensing_environment_id,
			@RequestBody Device device) {

		LOG.debug("Adding new device");

		try {
			if (device != null) {
				// if the sensingEnvironment (with this uuid or with same name
				// within sensing environment) already exist,
				// report conflict (409)
				if (devicesService.addDevice(device) == false) {
					return new ResponseEntity<String>(HttpStatus.CONFLICT);
				}
			} else
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<String>(HttpStatus.CREATED);

	}

	/**
	 * Adds the default devices set.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/defaultSet")
	public ResponseEntity<String> addDefaultDevicesSet(
			@PathVariable String sensing_environment_id) {

		LOG.debug("Adding default device set");

		try {
			if (sensing_environment_id != null
					&& !sensing_environment_id.isEmpty()) {
				addDefaultDevicesSetForSensEnvId(sensing_environment_id);
			} else {
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<String>(HttpStatus.CREATED);

	}

	/**
	 * Adds the default devices set for sens env id.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @throws Exception
	 *             the exception
	 */
	private void addDefaultDevicesSetForSensEnvId(String sensing_environment_id)
			throws Exception {
		String[] devicesNames = env.getProperty("devices.names").split(",");

		if (devicesNames != null) {
			for (String deviceNameProp : devicesNames) {
				String deviceName = deviceNameProp.replace('?', '#');
				Device device = new Device(deviceName);
				device.setSensingEnvironmentUUID(sensing_environment_id);

				boolean isWearable = Boolean.parseBoolean(env.getProperty(
						deviceNameProp + ".isWearable").trim());
				boolean isActuator = Boolean.parseBoolean(env.getProperty(
						deviceNameProp + ".isActuator").trim());
				boolean isSensor = Boolean.parseBoolean(env.getProperty(
						deviceNameProp + ".isSensor").trim());

				DeviceType type = DeviceType.valueOf(env
						.getProperty(deviceNameProp + ".type").trim()
						.toUpperCase());

				String indoorPlaceName = env.getProperty(
						deviceNameProp + ".indoorPlaceName").trim();
				Float serialNumber = Float.valueOf(env.getProperty(
						deviceNameProp + ".serialNumber").trim());

				device.setWearable(isWearable);
				device.setActuator(isActuator);
				device.setSensor(isSensor);
				device.setType(type);
				device.setIndoorPlaceName(indoorPlaceName);
				device.setSerialNumber(serialNumber);

				devicesService.addDevice(device);
			}
		}
	}

	/**
	 * Update device.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param device_id
	 *            the device_id
	 * @param device
	 *            the device
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/{device_id}")
	public ResponseEntity<String> updateDevice(
			@PathVariable String sensing_environment_id,
			@PathVariable String device_id, @RequestBody Device device) {
		LOG.info("updateDevice: updating device " + device_id);

		if (device == null) {
			LOG.warn("null device.");
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);

		}

		if (!device_id.equals(device.getUuid().toString())) {
			LOG.warn("Path device id does not match device id from content. Setting device id to path device id paramater: "
					+ device_id);
			device.setObjectId(device_id);
		}

		try {
			if (devicesService.updateDevice(device) == false)
				return new ResponseEntity<String>(HttpStatus.CONFLICT);
			else
				return new ResponseEntity<String>(HttpStatus.OK);

		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * Delete device.
	 *
	 * @param device_id
	 *            the device_id
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{device_id}")
	public ResponseEntity<String> deleteDevice(@PathVariable String device_id) {

		LOG.debug("deleting device with id :" + device_id);

		try {
			if (devicesService.deleteDevice(device_id))

				return new ResponseEntity<String>(HttpStatus.OK);
			else {
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Delete devices for sensing environment.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteDevicesForSensingEnvironment(
			@PathVariable String sensing_environment_id) {

		LOG.debug("deleting devices with sensing environment id :"
				+ sensing_environment_id);

		try {
			if (devicesService
					.deleteDevicesForSensingEnvironment(sensing_environment_id)) {

				return new ResponseEntity<String>(HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Gets the device info.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param device_id
	 *            the device_id
	 * @return the device info
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{device_id}")
	public ResponseEntity<Device> getDevice(
			@PathVariable String sensing_environment_id,
			@PathVariable String device_id) {

		LOG.debug("getDeviceInfo ");

		try {
			Device device = devicesService.getDevice(device_id);
			if (device != null) {
				return new ResponseEntity<Device>(device, HttpStatus.OK);
			} else {
				return new ResponseEntity<Device>(HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<Device>(HttpStatus.BAD_REQUEST);
		}

	}

}
