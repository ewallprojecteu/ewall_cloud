/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.device;


import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ewall.platform.commons.datamodel.core.ieeesumo.IEEEDevice;


/**
 * The Class EWallDevice.
 *
 * @author emirmos
 */
public class Device extends IEEEDevice {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(Device.class);
	
	/** The is wearable. */
	protected boolean isWearable;
	
	/** The is actuator. */
	protected boolean isActuator;
	
	/** The is sensor. */
	protected boolean isSensor;

	/** The type. */
	protected DeviceType type;

	/**  Name of the room where actuator is placed, if applicable*. */
	protected String indoorPlaceName;
	
	/** The sensing environment uuid. */
	protected String sensingEnvironmentUUID;
	
	/** The last data timestamp. */
	protected long lastDataTimestamp;
	
	
	/**
	 * Instantiates a new e wall device.
	 */
	public Device() {
		super();
	}


	/**
	 * Instantiates a new e wall device.
	 *
	 * @param name the name
	 */
	public Device(String name) {
		super(name);
	}
	
	/**
	 * Instantiates a new e wall device.
	 *
	 * @param name the name
	 * @param type the type
	 */
	public Device(String name, DeviceType type) {
		super(name);
		this.type = type;
	}

	/**
	 * Instantiates a new e wall device.
	 *
	 * @param name the name
	 * @param type the type
	 * @param indoorPlaceName the indoor place name
	 * @param isActuator the is actuator
	 * @param isSensor the is sensor
	 * @param sensingEnvironmentUUID the sensing environment uuid
	 */
	public Device(String name, DeviceType type, String indoorPlaceName, boolean isActuator,
			boolean isSensor, String sensingEnvironmentUUID) {
		this(name, type);
		this.isActuator = isActuator;
		this.isSensor = isSensor;
		this.indoorPlaceName = indoorPlaceName;
		this.sensingEnvironmentUUID = sensingEnvironmentUUID;
	}
	
	


	/**
	 * Checks if is wearable.
	 *
	 * @return true, if checks if is wearable
	 */
	public boolean isWearable() {
		return isWearable;
	}

	/**
	 * Sets the wearable.
	 *
	 * @param isWearable
	 *            the wearable
	 */
	public void setWearable(boolean isWearable) {
		this.isWearable = isWearable;
	}


	
	
	/**
	 * Checks if is actuator.
	 *
	 * @return the isActuator
	 */
	public boolean isActuator() {
		return isActuator;
	}


	/**
	 * Sets the actuator.
	 *
	 * @param isActuator the isActuator to set
	 */
	public void setActuator(boolean isActuator) {
		this.isActuator = isActuator;
	}


	/**
	 * Checks if is sensor.
	 *
	 * @return the isSensor
	 */
	public boolean isSensor() {
		return isSensor;
	}


	/**
	 * Sets the sensor.
	 *
	 * @param isSensor the isSensor to set
	 */
	public void setSensor(boolean isSensor) {
		this.isSensor = isSensor;
	}


	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public DeviceType getType() {
		return type;
	}


	/**
	 * Sets the type.
	 *
	 * @param type the type to set
	 */
	public void setType(DeviceType type) {
		this.type = type;
	}


	/**
	 * Gets the uuid.
	 *
	 * @return unique identifier
	 */
	public UUID getUuid() {
		UUID uuid = null;
		try {
			uuid = UUID.fromString(super.getObjectId());
		} catch (Exception e) {
			LOG.error("trying to convert string id to uuid. Error: "
					+ e.getMessage());
		}
		return uuid;
	}

	/**
	 * Sets the uuid.
	 *
	 * @param uuid
	 *            the new uuid
	 */
	public void setUuid(UUID uuid) {
		super.setObjectId(uuid.toString());
	}


	/**
	 * Gets the indoor place name.
	 *
	 * @return the indoorPlaceName
	 */
	public String getIndoorPlaceName() {
		return indoorPlaceName;
	}


	/**
	 * Sets the indoor place name.
	 *
	 * @param indoorPlaceName the indoorPlaceName to set
	 */
	public void setIndoorPlaceName(String indoorPlaceName) {
		this.indoorPlaceName = indoorPlaceName;
	}


	/**
	 * Gets the sensing environment uuid.
	 *
	 * @return the sensingEnvironmentUUID
	 */
	public String getSensingEnvironmentUUID() {
		return sensingEnvironmentUUID;
	}


	/**
	 * Sets the sensing environment uuid.
	 *
	 * @param sensingEnvironmentUUID the sensingEnvironmentUUID to set
	 */
	public void setSensingEnvironmentUUID(String sensingEnvironmentUUID) {
		this.sensingEnvironmentUUID = sensingEnvironmentUUID;
	}


	/**
	 * Gets the last data timestamp.
	 *
	 * @return the lastDataTimestamp
	 */
	public long getLastDataTimestamp() {
		return lastDataTimestamp;
	}


	/**
	 * Sets the last data timestamp.
	 *
	 * @param lastDataTimestamp the lastDataTimestamp to set
	 */
	public void setLastDataTimestamp(long lastDataTimestamp) {
		this.lastDataTimestamp = lastDataTimestamp;
	}
	
	

}