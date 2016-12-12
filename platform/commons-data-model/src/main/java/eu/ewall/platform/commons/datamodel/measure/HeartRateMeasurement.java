/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.measure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The Class HeartRateMeasurement.
 * 
 * @author emirmos
 */
@JsonIgnoreProperties({ "objectId", "uuid" })
public class HeartRateMeasurement extends Measurement  {

	/** The heart rate. */
	//The heart rate (integer number of heart beats per minute)
	private int heartRate;
	
	/** The heart rate variability. */
	//HRV: The heart rate variation (reporting SDNN, which is the standard deviation of normal S1S1 interval) in ms
	private int heartRateVariability;
	

	/**
	 * Instantiates a new heart rate measurement.
	 */
	public HeartRateMeasurement() {
		super();
	}


	/**
	 * Instantiates a new heart rate measurement.
	 *
	 * @param heartRate the heart rate
	 * @param heartRateVariability the heart rate variability
	 * @param timestamp the timestamp
	 */
	public HeartRateMeasurement(int heartRate, int heartRateVariability,
			long timestamp) {
		super(ConstantQuantityMeasureType.HEART_RATE, timestamp);
		this.heartRate = heartRate; 
		this.heartRateVariability = heartRateVariability;
	
	}
	
	
	/**
	 * Gets the heart rate.
	 *
	 * @return the heartRate
	 */
	public int getHeartRate() {
		return heartRate;
	}

	/**
	 * Gets the heart rate variability.
	 *
	 * @return the heartRateVariability
	 */
	public int getHeartRateVariability() {
		return heartRateVariability;
	}

	/**
	 * Sets the heart rate variability.
	 *
	 * @param heartRateVariability the heartRateVariability to set
	 */
	public void setHeartRateVariability(int heartRateVariability) {
		this.heartRateVariability = heartRateVariability;
	}

	/**
	 * Sets the heart rate.
	 *
	 * @param heartRate the heartRate to set
	 */
	public void setHeartRate(int heartRate) {
		this.heartRate = heartRate;
	}	
}
