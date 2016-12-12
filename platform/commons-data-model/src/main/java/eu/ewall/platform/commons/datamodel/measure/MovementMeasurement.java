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
public class MovementMeasurement extends IndoorMeasurement {


	/**
	 * The Constructor.
	 */
	public MovementMeasurement() {
		this.constantQuantityMeasureType = ConstantQuantityMeasureType.MOVEMENT_MEASURE;
	}

	

	/**
	 * Instantiates a new movement measurement.
	 *
	 * @param measuredValue the measured value
	 * @param timestamp the timestamp
	 * @param indoorPlaceName the indoor place name
	 */
	public MovementMeasurement(boolean measuredValue,
			long timestamp, String indoorPlaceName) {
		super(String.valueOf(measuredValue), ConstantQuantityMeasureType.MOVEMENT_MEASURE, timestamp, indoorPlaceName);
	}



	/**
	 * Instantiates a new movement measurement.
	 *
	 * @param measuredValue the measured value
	 * @param timestamp the timestamp
	 */
	public MovementMeasurement(boolean measuredValue,
			long timestamp) {
		super(String.valueOf(measuredValue), ConstantQuantityMeasureType.MOVEMENT_MEASURE, timestamp);
	}



	/**
	 * Instantiates a new movement measurement.
	 *
	 * @param indoorEnvironmentalMeasurement the indoor environmental measurement
	 */
	public MovementMeasurement(IndoorMeasurement indoorEnvironmentalMeasurement) {
				
			if (indoorEnvironmentalMeasurement
					.getMeasuredValue().equals("1"))
				this.measuredValue = String.valueOf(true);
			else if (indoorEnvironmentalMeasurement
					.getMeasuredValue().equals("0"))
				this.measuredValue = String.valueOf(false);
			else 
				//we use this to make sure that stored string value is "true" or "false"
				this.measuredValue = String.valueOf(Boolean.valueOf(indoorEnvironmentalMeasurement
					.getMeasuredValue()));
	
			this.timestamp = indoorEnvironmentalMeasurement.getTimestamp();
			this.indoorPlaceName = indoorEnvironmentalMeasurement.getIndoorPlaceName();
			this.constantQuantityMeasureType = ConstantQuantityMeasureType.MOVEMENT_MEASURE;
		

	}
	

	/**
	 * Gets the measured value.
	 *
	 * @return the measured value
	 */
	@JsonIgnore
	public boolean getMeasuredValueBoolean() {
		boolean measuredValueBoolean = Boolean.FALSE;
	
			try {
				if (measuredValue.equals("1"))
					measuredValueBoolean = true;
				else if (measuredValue.equals("0"))
					measuredValueBoolean = false;
				else 
					measuredValueBoolean = Boolean.valueOf(measuredValue);
			} catch (Exception e) {
				//TODO add logger.warn message
				//e.printStackTrace();
			}

		return measuredValueBoolean;
				

	}

	

}
