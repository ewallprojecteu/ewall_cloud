/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.measure;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * @author emirmos
 */
public class CarbonMonoxideMeasurement extends IndoorMeasurement {

	/**
	 * The Constructor.
	 */
	public CarbonMonoxideMeasurement() {
		this.constantQuantityMeasureType = ConstantQuantityMeasureType.CARBON_MONOXIDE_MEASUREMENT;
	}

	
		
	/**
	 * Instantiates a new carbon monoxide measurement.
	 *
	 * @param measuredValue the measured value
	 * @param timestamp the timestamp
	 * @param indoorPlaceName the indoor place name
	 */
	public CarbonMonoxideMeasurement(int measuredValue,
			long timestamp, String indoorPlaceName) {
		super(String.valueOf(measuredValue), ConstantQuantityMeasureType.CARBON_MONOXIDE_MEASUREMENT, timestamp, indoorPlaceName);
	}


	/**
	 * Instantiates a new carbon monoxide measurement.
	 *
	 * @param indoorEnvironmentalMeasurement the indoor environmental measurement
	 */
	public CarbonMonoxideMeasurement(IndoorMeasurement indoorEnvironmentalMeasurement) {
			this.measuredValue = indoorEnvironmentalMeasurement.getMeasuredValue();
			this.timestamp = indoorEnvironmentalMeasurement.getTimestamp();
			this.indoorPlaceName = indoorEnvironmentalMeasurement.getIndoorPlaceName();
			//TODO check that ConstantQuantityMeasureType is CARBON_MONOXIDE_MEASUREMENT or null
			this.constantQuantityMeasureType = ConstantQuantityMeasureType.CARBON_MONOXIDE_MEASUREMENT;
	}

	/**
	 * Instantiates a new carbon monoxide (CO) measurement.
	 *
	 * @param measuredValue
	 *            the measured value
	 * @param timestamp
	 *            the timestamp
	 */
	public CarbonMonoxideMeasurement(int measuredValue, long timestamp) {
		super(String.valueOf(measuredValue), ConstantQuantityMeasureType.CARBON_MONOXIDE_MEASUREMENT, timestamp);
	}
	
	

	
	/**
	 * Gets an integer value between 0 to 10 indicating the level of gas. The
	 * levels 1­10 are approximately logarithmic in terms of gas concentration.
	 * Level 0 indicates a value below the lower measurable concentration for
	 * every sensor.
	 *
	 * @return the level of gas in integer value between 0 to 10, -1 if error
	 */
	@JsonIgnore
	public int getLevel() {
		try {
			return Integer.valueOf(measuredValue);
		} catch (NumberFormatException e) {
			return -1;
		}

	}


}
