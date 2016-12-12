package eu.ewall.servicebrick.vitals.model;

import org.joda.time.DateTime;

import eu.ewall.servicebrick.common.model.SbMeasurement;

/**
 * A blood pressure measurement in the output format used by service bricks.
 */
public class BloodPressureSbMeasurement extends SbMeasurement {

	private int systolicBloodPressure;
	private int diastolicBloodPressure;
	
	public BloodPressureSbMeasurement(DateTime timestamp, int systolicBloodPressure, int diastolicBloodPressure) {
		super(timestamp);
		this.systolicBloodPressure = systolicBloodPressure;
		this.diastolicBloodPressure = diastolicBloodPressure;
	}

	public int getSystolicBloodPressure() {
		return systolicBloodPressure;
	}

	public int getDiastolicBloodPressure() {
		return diastolicBloodPressure;
	}
	
}
