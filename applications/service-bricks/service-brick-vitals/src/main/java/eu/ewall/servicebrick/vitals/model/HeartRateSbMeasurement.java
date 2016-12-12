package eu.ewall.servicebrick.vitals.model;

import org.joda.time.DateTime;

import eu.ewall.servicebrick.common.model.SbMeasurement;

/**
 * A heart rate measurement in the output format used by service bricks.
 */
public class HeartRateSbMeasurement extends SbMeasurement {

	private int heartRate;
	private int heartRateVariability;
	
	public HeartRateSbMeasurement(DateTime timestamp, int heartRate, int heartRateVariability) {
		super(timestamp);
		this.heartRate = heartRate;
		this.heartRateVariability = heartRateVariability;
	}
	
	public int getHeartRate() {
		return heartRate;
	}
	
	public int getHeartRateVariability() {
		return heartRateVariability;
	}
	
}
