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
public class IlluminanceMeasurement extends IndoorMeasurement {


	/**
	 * The Constructor.
	 */
	public IlluminanceMeasurement() {
		this.constantQuantityMeasureType = ConstantQuantityMeasureType.ILLUMINANCE_MEASURE;
	}

	
	
	
	/**
	 * Instantiates a new illuminance measurement.
	 *
	 * @param measuredValue the measured value
	 * @param timestamp the timestamp
	 * @param indoorPlaceName the indoor place name
	 */
	public IlluminanceMeasurement(Double measuredValue,	long timestamp, String indoorPlaceName) {
		super(String.valueOf(measuredValue), ConstantQuantityMeasureType.ILLUMINANCE_MEASURE, timestamp, indoorPlaceName);
	}



	/**
	 * Instantiates a new illuminance measurement.
	 *
	 * @param indoorEnvironmentalMeasurement the indoor environmental measurement
	 */
	public IlluminanceMeasurement(IndoorMeasurement indoorEnvironmentalMeasurement) {

			this.measuredValue = indoorEnvironmentalMeasurement
					.getMeasuredValue();
			this.timestamp = indoorEnvironmentalMeasurement.getTimestamp();
			this.indoorPlaceName = indoorEnvironmentalMeasurement.getIndoorPlaceName();
			//TODO check that ConstantQuantityMeasureType is ILLUMINANCE_MEASURE or null
			this.constantQuantityMeasureType = ConstantQuantityMeasureType.ILLUMINANCE_MEASURE;
	}

	/**
	 * Instantiates a new illuminance measurement.
	 *
	 * @param measuredValue
	 *            the measured value
	 * @param timestamp
	 *            the timestamp
	 */
	public IlluminanceMeasurement(double measuredValue, long timestamp) {
		super(String.valueOf(measuredValue), ConstantQuantityMeasureType.ILLUMINANCE_MEASURE, timestamp);
	}

	
	
	/**
	 * Gets the measured value.
	 *
	 * @return the measured value
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
