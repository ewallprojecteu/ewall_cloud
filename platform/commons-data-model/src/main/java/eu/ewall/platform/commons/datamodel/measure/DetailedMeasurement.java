/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.measure;

import eu.ewall.platform.commons.datamodel.location.Location;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * @author eandgrg, emirmos
 */
public class DetailedMeasurement extends Measurement {

	/**
	 * The degree of accuracy is half a unit each side of the unit of measure.
	 * When your instrument measures in "1"s then any value between 6½ and 7½ is
	 * measured as "7". e.g.: a fence is measured as 12,5 meters long, accurate
	 * to 0,1 of a meter. Accurate to 0,1 m means it could be up to 0,05 m
	 * either way: * Length = 12,5 ±0,05 m
	 */
	protected double accuracyDegree;

	/**
	 * The Absolute Error is the difference between the actual and measured
	 * value.
	 */
	protected double absoluteError;

	/** The constant quantity type. */
	protected ConstantQuantityMeasureType constantQuantityMeasureType;

	/** The max value. */
	protected double maxValue;

	/** The min value. */
	protected double minValue;

	/** The on location. */

	protected Location onLocation;
	
	/** The description	 */
	protected String description;
	

	/** The measured value. */
	protected String measuredValue;

	/**
	 * The Constructor.
	 */
	public DetailedMeasurement() {

	}

	/**
	 * The Constructor.
	 *
	 * @param measuredValue
	 *            the measured value
	 * @param minValue
	 *            the min value
	 * @param maxValue
	 *            the max value
	 * @param constantQuantityMeasureType
	 *            the constant quantity measure type
	 * @param accuracyDegree
	 *            the accuracyDegree
	 * @param absoluteError
	 *            the absolute error
	 * @param onLocation
	 *            the on location
	 * @param timestamp
	 *            the timestamp
	 * @param description
	 *            the description
	 */
	public DetailedMeasurement(String measuredValue, double minValue,
			double maxValue,
			ConstantQuantityMeasureType constantQuantityMeasureType,
			double accuracyDegree, double absoluteError, Location onLocation,
			long timestamp, String description) {

		this.accuracyDegree = accuracyDegree;
		this.measuredValue = measuredValue;
		this.maxValue = maxValue;
		this.minValue = minValue;
		this.onLocation = onLocation;
		this.timestamp = timestamp;
		this.description = description;
		this.absoluteError = absoluteError;
		this.constantQuantityMeasureType = constantQuantityMeasureType;
	}

	/**
	 * Gets the accuracy degree.
	 *
	 * @return the accuracyDegree
	 */
	public double getAccuracyDegree() {
		return accuracyDegree;
	}

	/**
	 * Sets the accuracy degree.
	 *
	 * @param accuracyDegree
	 *            the accuracyDegree to set
	 */
	public void setAccuracyDegree(double accuracyDegree) {
		this.accuracyDegree = accuracyDegree;
	}

	/**
	 * Gets the absolute error.
	 *
	 * @return the absoluteError
	 */
	public double getAbsoluteError() {
		return absoluteError;
	}

	/**
	 * Sets the absolute error.
	 *
	 * @param absoluteError
	 *            the absoluteError to set
	 */
	public void setAbsoluteError(double absoluteError) {
		this.absoluteError = absoluteError;
	}


	/**
	 * Gets the max value.
	 *
	 * @return the max value
	 */
	public double getMaxValue() {
		return maxValue;
	}

	/**
	 * Sets the max value.
	 *
	 * @param maxValue
	 *            the max value
	 */
	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}


	/**
	 * Gets the min value.
	 *
	 * @return the min value
	 */
	public double getMinValue() {
		return minValue;
	}

	/**
	 * Sets the min value.
	 *
	 * @param minValue
	 *            the min value
	 */
	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	/**
	 * Gets the on location.
	 *
	 * @return the on location
	 */
	public Location getOnLocation() {
		return onLocation;
	}

	/**
	 * Sets the on location.
	 *
	 * @param onLocation
	 *            the on location
	 */
	public void setOnLocation(Location onLocation) {
		this.onLocation = onLocation;
	}

	/**
	 * @return the measuredValue
	 */
	public String getMeasuredValue() {
		return measuredValue;
	}



	/**
	 * @param measuredValue the measuredValue to set
	 */
	public void setMeasuredValue(String measuredValue) {
		this.measuredValue = measuredValue;
	}

}
