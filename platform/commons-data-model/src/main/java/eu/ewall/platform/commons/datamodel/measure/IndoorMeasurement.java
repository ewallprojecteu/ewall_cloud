/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.measure;


/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * Relates to indoor environmental data
 */
public class IndoorMeasurement extends Measurement {
	

	/** The measured value. */
	protected String measuredValue;
	
	/**
	 * The name of a location where measurement took place (i.e. bathroom, bedroom1, etc.)
	 */
	protected String indoorPlaceName;
	

	/**
	 * The Constructor.
	 */
	public IndoorMeasurement() {

	}
	
	/**
	 * Instantiates a new indoor measurement.
	 *
	 * @param indoorMeasurement the indoor measurement
	 */
	public IndoorMeasurement(IndoorMeasurement indoorMeasurement) {
		this.measuredValue = new String(indoorMeasurement.getMeasuredValue());
		this.constantQuantityMeasureType = indoorMeasurement.getConstantQuantityMeasureType();
		this.timestamp = indoorMeasurement.getTimestamp();
		this.indoorPlaceName = new String (indoorMeasurement.getIndoorPlaceName());
	}



	/**
	 * Instantiates a new indoor measurement.
	 *
	 * @param measuredValue the measured value
	 * @param constantQuantityMeasureType the constant quantity measure type
	 * @param timestamp the timestamp
	 * @param indoorPlaceName the indoor place name
	 */
	public IndoorMeasurement(String measuredValue,
			ConstantQuantityMeasureType constantQuantityMeasureType,
			long timestamp, String indoorPlaceName) {
		this.measuredValue = measuredValue;
		this.constantQuantityMeasureType = constantQuantityMeasureType;
		this.timestamp = timestamp;
		this.indoorPlaceName = indoorPlaceName;
	}
	
	
	/**
	 * Instantiates a new indoor measurement.
	 *
	 * @param measuredValue the measured value
	 * @param constantQuantityMeasureType the constant quantity measure type
	 * @param timestamp the timestamp
	 */
	public IndoorMeasurement(String measuredValue,
			ConstantQuantityMeasureType constantQuantityMeasureType,
			long timestamp) {
		this.measuredValue = measuredValue;
		this.constantQuantityMeasureType = constantQuantityMeasureType;
		this.timestamp = timestamp;
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
	 * Gets the measured value.
	 *
	 * @return the measuredValue
	 */
	public String getMeasuredValue() {
		return measuredValue;
	}



	/**
	 * Sets the measured value.
	 *
	 * @param measuredValue the measuredValue to set
	 */
	public void setMeasuredValue(String measuredValue) {
		this.measuredValue = measuredValue;
	}

}
