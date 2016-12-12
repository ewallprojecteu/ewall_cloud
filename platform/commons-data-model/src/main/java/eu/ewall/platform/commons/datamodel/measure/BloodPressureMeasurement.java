/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.measure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class BloodPressureMeasurement.
 */
@JsonIgnoreProperties({ "objectId", "uuid" })
public class BloodPressureMeasurement extends Measurement {

	/** The systolic blood pressure. */
	//The systolic blood pressure
	private int systolicBloodPressure;

	/** The diastolic blood pressure. */
	//The diastolic blood pressure
	private int diastolicBloodPressure;


	/**
	 * Instantiates a new blood pressure measurement.
	 */
	public BloodPressureMeasurement() {
		super();
	}

	/**
	 * Instantiates a new blood pressure measurement.
	 *
	 * @param systolicBloodPressure the systolic blood pressure
	 * @param diastolicBloodPressure the diastolic blood pressure
	 * @param timestamp the timestamp
	 */
	public BloodPressureMeasurement(int systolicBloodPressure,
			int diastolicBloodPressure, long timestamp) {
		super(ConstantQuantityMeasureType.BLOOD_PRESSURE, timestamp);
		this.systolicBloodPressure = systolicBloodPressure;
		this.diastolicBloodPressure = diastolicBloodPressure;

	}

	/**
	 * Gets the systolic blood pressure.
	 *
	 * @return the systolicBloodPressure
	 */
	public int getSystolicBloodPressure() {
		return systolicBloodPressure;
	}

	/**
	 * Sets the systolic blood pressure.
	 *
	 * @param systolicBloodPressure            the systolicBloodPressure to set
	 */
	public void setSystolicBloodPressure(int systolicBloodPressure) {
		this.systolicBloodPressure = systolicBloodPressure;
	}

	/**
	 * Gets the diastolic blood pressure.
	 *
	 * @return the diastolicBloodPressure
	 */
	public int getDiastolicBloodPressure() {
		return diastolicBloodPressure;
	}

	/**
	 * Sets the diastolic blood pressure.
	 *
	 * @param diastolicBloodPressure            the diastolicBloodPressure to set
	 */
	public void setDiastolicBloodPressure(int diastolicBloodPressure) {
		this.diastolicBloodPressure = diastolicBloodPressure;
	}

}
