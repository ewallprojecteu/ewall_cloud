/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.measure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import eu.ewall.platform.commons.datamodel.activity.ActivityType;


/**
 * The Class AccelerometerMeasurement.
 *
 * @author eandgrg, emirmos
 */
@JsonIgnoreProperties({ "objectId", "uuid" })
public class AccelerometerMeasurement extends Measurement {

	
	/** Integrated Mean Accelerations. */
	protected double imaValue;

	/** The Integrated Squared Accelerations value. */
	protected double isaValue;

	/** Number of steps. */
	protected long steps;

	/** The activity type. */
	protected ActivityType activityType = ActivityType.UNKNOWN;
	
	/** The fall detected. */
	protected boolean fallDetected;
	
	/**
	 * The Constructor.
	 */
	public AccelerometerMeasurement() {
		this.constantQuantityMeasureType = ConstantQuantityMeasureType.ACCELEROMETER_MEASURE;
	}

	
	/**
	 * The Constructor.
	 *
	 * @param imaValue            the ima value
	 * @param isaValue            the isa value
	 * @param steps            the steps
	 * @param activityType the activity type
	 * @param timestamp            the timestamp
	 */
	public AccelerometerMeasurement(double imaValue, double isaValue,
			long steps, ActivityType activityType,
			long timestamp) {
		this.imaValue = imaValue;
		this.isaValue = isaValue;
		this.steps = steps;
		this.activityType = activityType;
		this.timestamp = timestamp;
		this.constantQuantityMeasureType = ConstantQuantityMeasureType.ACCELEROMETER_MEASURE;
	}
	
	/**
	 * Instantiates a new accelerometer measurement.
	 *
	 * @param imaValue the ima value
	 * @param isaValue the isa value
	 * @param steps the steps
	 * @param activityType the activity type
	 * @param fallDetected the fall detected
	 * @param timestamp the timestamp
	 */
	public AccelerometerMeasurement(double imaValue, double isaValue,
			long steps, ActivityType activityType, boolean fallDetected,
			long timestamp) {
		this.imaValue = imaValue;
		this.isaValue = isaValue;
		this.steps = steps;
		this.activityType = activityType;
		this.fallDetected = fallDetected;
		this.timestamp = timestamp;
		this.constantQuantityMeasureType = ConstantQuantityMeasureType.ACCELEROMETER_MEASURE;
	}


	/**
	 * Gets the ima value.
	 *
	 * @return the imaValue
	 */
	public double getImaValue() {
		return imaValue;
	}

	/**
	 * Sets the ima value.
	 *
	 * @param imaValue
	 *            the imaValue to set
	 */
	public void setImaValue(double imaValue) {
		this.imaValue = imaValue;
	}

	/**
	 * Gets the isa value.
	 *
	 * @return the isaValue
	 */
	public double getIsaValue() {
		return isaValue;
	}

	/**
	 * Sets the isa value.
	 *
	 * @param isaValue
	 *            the isaValue to set
	 */
	public void setIsaValue(double isaValue) {
		this.isaValue = isaValue;
	}


	/**
	 * Gets the steps.
	 *
	 * @return the steps
	 */
	public long getSteps() {
		return steps;
	}

	/**
	 * Sets the steps.
	 *
	 * @param steps
	 *            the steps to set
	 */
	public void setSteps(long steps) {
		this.steps = steps;
	}


	/**
	 * Gets the activity type.
	 *
	 * @return the activityType
	 */
	public ActivityType getActivityType() {
		if(activityType == null){
			activityType = ActivityType.UNKNOWN;
		}
		return activityType;
	}


	/**
	 * Sets the activity type.
	 *
	 * @param activityType the activityType to set
	 */
	public void setActivityType(ActivityType activityType) {
		if(activityType == null){
			activityType = ActivityType.UNKNOWN;
		}
		this.activityType = activityType;
	}
	
	
	
	/**
	 * Checks if is fall detected.
	 *
	 * @return the fall
	 */
	public boolean isFallDetected() {
		return fallDetected;
	}


	/**
	 * Sets the fall detected.
	 *
	 * @param fallDetected the new fall detected
	 */
	public void setFallDetected(boolean fallDetected) {
		this.fallDetected = fallDetected;
	}

}
