/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.datamanager.dao.device;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.WriteResult;

import eu.ewall.platform.commons.datamodel.device.Device;
import eu.ewall.platform.commons.datamodel.device.DeviceType;
import eu.ewall.platform.middleware.datamanager.dao.config.dbtype.EWallDBType;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;

/**
 * The Class DeviceDao.
 */
public class DeviceDao {

	/** The log. */
	private static final Logger LOG = LoggerFactory.getLogger(DeviceDao.class);

	/** The Constant DEVICES_COLLECTION. */
	public static final String DEVICES_COLLECTION = "device";

	/** The mongo ops. */
	private MongoOperations mongoOps;

	/**
	 * Instantiates a new region dao.
	 */
	public DeviceDao() {
		mongoOps = MongoDBFactory
				.getMongoOperationsForDBType(EWallDBType.SYSTEM);
	}

	/**
	 * Gets the all devices.
	 *
	 * @return the all devices
	 */
	public List<Device> getAllDevices() {
		LOG.debug("Retrieveing all devices from mongodb.");
		return this.mongoOps.findAll(Device.class, DEVICES_COLLECTION);

	}

	/**
	 * Gets the devices by sensing environment uuid.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @return the devices by sensing environment uuid
	 */
	public List<Device> getDevicesBySensingEnvironmentUUID(
			String sensing_environment_id) {

		LOG.debug("Retrieveing devices with sensing environment id "
				+ sensing_environment_id + " from mongodb.");
		Query query = new Query(Criteria.where("sensingEnvironmentUUID").is(
				sensing_environment_id));

		return this.mongoOps.find(query, Device.class, DEVICES_COLLECTION);

	}

	/**
	 * Gets the devices by parameters.
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
	 * @return the devices by parameters
	 */
	public List<Device> getDevicesByParameters(String sensing_environment_id,
			DeviceType type, String room_name, Boolean isActuator,
			Boolean isSensor) {

		LOG.debug("Retrieveing devices from mongodb.");
		Query query = new Query();

		if (sensing_environment_id != null)
			query.addCriteria(Criteria.where("sensingEnvironmentUUID").is(
					sensing_environment_id));

		if (type != null)
			query.addCriteria(Criteria.where("type").is(type));

		if (room_name != null)
			query.addCriteria(Criteria.where("indoorPlaceName").is(room_name));

		if (isActuator != null)
			query.addCriteria(Criteria.where("isActuator").is(
					isActuator.booleanValue()));

		if (isSensor != null)
			query.addCriteria(Criteria.where("isSensor").is(
					isSensor.booleanValue()));

		return this.mongoOps.find(query, Device.class, DEVICES_COLLECTION);

	}

	/**
	 * Adds the device.
	 *
	 * @param device
	 *            the device
	 * @return true, if successful
	 */
	public boolean addDevice(Device device) {

		mongoOps.insert(device, DEVICES_COLLECTION);

		LOG.debug("New device with name " + device.getName()
				+ " added to mongodb.");

		return true;

	}

	/**
	 * Gets the device by uuid.
	 *
	 * @param uuid
	 *            the uuid
	 * @return the device by uuid
	 */
	public Device getDeviceByUUID(String uuid) {

		LOG.debug("Retrieveing device with uuid " + uuid + " from mongodb.");
		Query query = new Query(Criteria.where("_id").is(uuid));

		return this.mongoOps.findOne(query, Device.class, DEVICES_COLLECTION);
	}

	/**
	 * Update device.
	 *
	 * @param device
	 *            the device
	 * @return true, if successful
	 */
	public boolean updateDevice(Device device) {
		Query query = new Query(Criteria.where("_id").is(
				device.getUuid().toString()));

		Device existingDevice = mongoOps.findOne(query, Device.class,
				DEVICES_COLLECTION);

		if (existingDevice == null) {
			LOG.info("Device with uuid " + device.getUuid()
					+ " not found in DB. Update terminated.");
			return false;
		}

		existingDevice.setActuator(device.isActuator());
		existingDevice.setSensor(device.isSensor());
		existingDevice.setIndoorPlaceName(device.getIndoorPlaceName());
		existingDevice.setLastDataTimestamp(device.getLastDataTimestamp());
		existingDevice.setManufacturer(device.getManufacturer());
		existingDevice.setName(device.getName());
		existingDevice.setSensingEnvironmentUUID(device
				.getSensingEnvironmentUUID());
		existingDevice.setSerialNumber(device.getSerialNumber());
		existingDevice.setType(device.getType());
		existingDevice.setWearable(device.isWearable());

		mongoOps.save(existingDevice, DEVICES_COLLECTION);

		LOG.debug("EWall Device with uuid " + existingDevice.getUuid()
				+ " updated");

		return true;

	}

	/**
	 * Delete device.
	 *
	 * @param uuid
	 *            the uuid
	 * @return true, if successful
	 */
	public boolean deleteDevice(String uuid) {
		LOG.debug("Deleting device with id  " + uuid + " from mongodb.");

		Query query = new Query(Criteria.where("_id").is(uuid));
		this.mongoOps.remove(query, Device.class, DEVICES_COLLECTION);

		return true;
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
		LOG.debug("Deleting devices with sensing environment id  "
				+ sensing_environment_id + " from mongodb.");

		Query query = new Query(Criteria.where("sensingEnvironmentUUID").is(
				sensing_environment_id));
		WriteResult writeResult = this.mongoOps.remove(query, Device.class,
				DEVICES_COLLECTION);

		LOG.debug("Deleted " + writeResult.getN()
				+ " devices with sensing environment id  "
				+ sensing_environment_id + " from mongodb.");
		return true;
	}
}
