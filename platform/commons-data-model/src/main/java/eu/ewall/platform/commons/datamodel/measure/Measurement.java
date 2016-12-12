/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.measure;


import com.fasterxml.jackson.annotation.JsonIgnore;


/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * @author eandgrg, emirmos
 */
public class Measurement {


	/** The constant quantity type. */
	protected ConstantQuantityMeasureType constantQuantityMeasureType;
	
	/** The timestamp. */
	protected long timestamp;
	
	/** The device id. */
	@JsonIgnore
	private String deviceId;


	/**
	 * The Constructor.
	 */
	public Measurement() {

	}
	
	
	/**
	 * Instantiates a new measurement.
	 *
	 * @param constantQuantityMeasureType the constant quantity measure type
	 * @param timestamp the timestamp
	 */
	public Measurement(ConstantQuantityMeasureType constantQuantityMeasureType,
			long timestamp) {
		super();
		this.constantQuantityMeasureType = constantQuantityMeasureType;
		this.timestamp = timestamp;
	}



	/**
	 * Gets the constant quantity measure type.
	 *
	 * @return the constantQuantityMeasureType
	 */
	public ConstantQuantityMeasureType getConstantQuantityMeasureType() {
		return constantQuantityMeasureType;
	}



	/**
	 * Sets the constant quantity measure type.
	 *
	 * @param constantQuantityMeasureType the constantQuantityMeasureType to set
	 */
	public void setConstantQuantityMeasureType(
			ConstantQuantityMeasureType constantQuantityMeasureType) {
		this.constantQuantityMeasureType = constantQuantityMeasureType;
	}



	/**
	 * Gets the timestamp.
	 *
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the timestamp.
	 *
	 * @param timestamp
	 *            the timestamp
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}


	/**
	 * Gets the device id.
	 *
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}


	/**
	 * Sets the device id.
	 *
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
}
