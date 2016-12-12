/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.measure;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The Class DoorStatus.
 *
 * @author eandgrg, emirmos
 */
public class DoorStatus extends IndoorMeasurement {

	/**
	 * The Constructor.
	 */
	public DoorStatus() {
		this.constantQuantityMeasureType = ConstantQuantityMeasureType.DOOR_STATUS_OPEN;
	}

	

	/**
	 * Instantiates a new door status.
	 *
	 * @param measuredValue the measured value
	 * @param timestamp the timestamp
	 * @param indoorPlaceName the indoor place name
	 */
	public DoorStatus(boolean measuredValue,
			long timestamp, String indoorPlaceName) {
		super(String.valueOf(measuredValue), ConstantQuantityMeasureType.DOOR_STATUS_OPEN, timestamp, indoorPlaceName);
	}



	/**
	 * Instantiates a new door status.
	 *
	 * @param measuredValue the measured value
	 * @param timestamp the timestamp
	 */
	public DoorStatus(boolean measuredValue,
			long timestamp) {
		super(String.valueOf(measuredValue), ConstantQuantityMeasureType.DOOR_STATUS_OPEN, timestamp);
	}



	/**
	 * Instantiates a new door status.
	 *
	 * @param indoorEnvironmentalMeasurement the indoor environmental measurement
	 */
	public DoorStatus(IndoorMeasurement indoorEnvironmentalMeasurement) {

			//we use this to make sure that stored string value is "true" or "false"
			this.measuredValue = String.valueOf(Boolean.valueOf(indoorEnvironmentalMeasurement
				.getMeasuredValue()));
	
			this.timestamp = indoorEnvironmentalMeasurement.getTimestamp();
			this.indoorPlaceName = indoorEnvironmentalMeasurement.getIndoorPlaceName();
			this.constantQuantityMeasureType = ConstantQuantityMeasureType.DOOR_STATUS_OPEN;

	}
	

	/**
	 * Return true if doors are open. False if not.
	 *
	 * @return the measured value
	 */
	@JsonIgnore
	public boolean isDoorOpen() {
		return Boolean.valueOf(measuredValue);
	}

	

}
