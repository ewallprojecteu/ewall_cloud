/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.measure;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The Class HumidityMeasurement.
 *
 * @author eandgrg, emirmos
 */
public class HumidityMeasurement extends IndoorMeasurement {

	/**
	 * The Constructor.
	 */
	public HumidityMeasurement() {
		this.constantQuantityMeasureType = ConstantQuantityMeasureType.HUMIDITY_MEASURE;
	}

	
		
	/**
	 * Instantiates a new humidity measurement.
	 *
	 * @param measuredValue the measured value
	 * @param timestamp the timestamp
	 * @param indoorPlaceName the indoor place name
	 */
	public HumidityMeasurement(double measuredValue,
			long timestamp, String indoorPlaceName) {
		super(String.valueOf(measuredValue), ConstantQuantityMeasureType.HUMIDITY_MEASURE, timestamp, indoorPlaceName);
	}


	/**
	 * Instantiates a new humidity measurement.
	 *
	 * @param indoorEnvironmentalMeasurement the indoor environmental measurement
	 */
	public HumidityMeasurement(IndoorMeasurement indoorEnvironmentalMeasurement) {
			this.measuredValue = indoorEnvironmentalMeasurement.getMeasuredValue();
			this.timestamp = indoorEnvironmentalMeasurement.getTimestamp();
			this.indoorPlaceName = indoorEnvironmentalMeasurement.getIndoorPlaceName();
			//TODO check that ConstantQuantityMeasureType is HUMIDITY_MEASURE or null
			this.constantQuantityMeasureType = ConstantQuantityMeasureType.HUMIDITY_MEASURE;

	}

	/**
	 * Instantiates a new humidity measurement.
	 *
	 * @param measuredValue
	 *            the measured value
	 * @param timestamp
	 *            the timestamp
	 */
	public HumidityMeasurement(double measuredValue, long timestamp) {
		super(String.valueOf(measuredValue), ConstantQuantityMeasureType.HUMIDITY_MEASURE, timestamp);
	}
	
	

	
	/**
	 * Gets the measured value.
	 *
	 * @return the measured value in double format
	 */
	@JsonIgnore
	public double getMeasuredValueDouble() {
		double measuredValueDouble = Double.NaN;
		try {
				measuredValueDouble = Double.valueOf(measuredValue);
			} catch (NumberFormatException e) {
				//TODO add logger.warn message
				//e.printStackTrace();
			}
		
		return measuredValueDouble;
	}


}
