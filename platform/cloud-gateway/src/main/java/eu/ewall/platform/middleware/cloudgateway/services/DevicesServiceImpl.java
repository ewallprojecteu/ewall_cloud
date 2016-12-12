/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.cloudgateway.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import eu.ewall.platform.commons.datamodel.device.Device;
import eu.ewall.platform.commons.datamodel.device.DeviceType;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;
import eu.ewall.platform.middleware.datamanager.dao.device.DeviceDao;

/**
 * The Class DevicesServiceImpl.
 *
 * @author emirmos
 */
@Service("devicesService")
public class DevicesServiceImpl implements DisposableBean {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(DevicesServiceImpl.class);

	/** The device dao. */
	private DeviceDao deviceDao;

	/**
	 * Instantiates a new devices service impl.
	 */
	public DevicesServiceImpl() {
		deviceDao = new DeviceDao();
	}

	/**
	 * Gets the device.
	 *
	 * @param uuid
	 *            the uuid
	 * @return the device
	 */
	public Device getDevice(String uuid) {

		return deviceDao.getDeviceByUUID(uuid);
	}

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
	public List<Device> getDevices(String sensing_environment_id,
			DeviceType type, String room_name, Boolean isActuator,
			Boolean isSensor) {
		return deviceDao.getDevicesByParameters(sensing_environment_id, type,
				room_name, isActuator, isSensor);
	}

	/**
	 * Adds the device.
	 *
	 * @param device
	 *            the device
	 * @return true, if successful
	 */
	public boolean addDevice(Device device) {
		if (device == null) {
			LOG.warn("addDevice(): device is null");
			return false;
		}
		if (deviceDao.getDeviceByUUID(device.getUuid().toString()) != null) {
			LOG.warn("addDevice(): device with id " + device.getUuid()
					+ " already exist. Not added.");
			return false;
		}
		if (existsDeviceWithSameNameWithinSensEnv(device)) {
			LOG.warn("addDevice(): device with name " + device.getName()
					+ " already exist for sensing environment with id "
					+ device.getSensingEnvironmentUUID() + ". Not added.");
			return false;
		}

		return deviceDao.addDevice(device);
	}

	/**
	 * Exists device with same name within sens env.
	 *
	 * @param device
	 *            the device
	 * @return true, if successful
	 */
	private boolean existsDeviceWithSameNameWithinSensEnv(Device device) {
		List<Device> devices = deviceDao
				.getDevicesBySensingEnvironmentUUID(device
						.getSensingEnvironmentUUID());
		for (Device d : devices) {
			/*
			 * If name is the same, but id is different to correctly handle
			 * adding and editing devices
			 */
			if (d.getName().equals(device.getName())
					&& !d.getUuid().toString()
							.equals(device.getUuid().toString())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Update device.
	 *
	 * @param device
	 *            the device
	 * @return true, if successful
	 */
	public boolean updateDevice(Device device) throws Exception {
		if (existsDeviceWithSameNameWithinSensEnv(device)) {
			LOG.warn("updateDevice(): device with name " + device.getName()
					+ " already exist for sensing environment with id "
					+ device.getSensingEnvironmentUUID() + ". Not modified.");
			return false;
		}

		if (!deviceDao.updateDevice(device)) {
			throw new Exception("Requested device not found.");
		} else {
			return true;
		}
	}

	/**
	 * Delete device.
	 *
	 * @param uuid
	 *            the uuid
	 * @return true, if successful
	 */
	public boolean deleteDevice(String uuid) {
		return deviceDao.deleteDevice(uuid);
	}

	/**
	 * Delete devices for sensing environment.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @return true, if successful
	 */
	public boolean deleteDevicesForSensingEnvironment(
			String sensing_environment_id) {
		return deviceDao
				.deleteDevicesForSensingEnvironment(sensing_environment_id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() throws Exception {
		MongoDBFactory.close();
	}

}
