/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.measure;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The Class BedSensorMeasurement.
 *
 * @author eandgrg, emirmos
 */
public class MattressPressureSensing extends IndoorMeasurement {

	/** The pressure. */
	private boolean pressure;
	
	/** The ima value. */
	private double imaValue;
	
	
	/**
	 * The Constructor.
	 */
	public MattressPressureSensing() {
		this.constantQuantityMeasureType = ConstantQuantityMeasureType.MATTRESS_PRESSURE_SENSING;
	}


	/**
	 * Instantiates a new bed sensor measurement.
	 *
	 * @param pressure            the pressure
	 * @param imaValue the ima value
	 * @param timestamp            the timestamp
	 */
	public MattressPressureSensing(boolean pressure, double imaValue, long timestamp) {
		super(null,  ConstantQuantityMeasureType.MATTRESS_PRESSURE_SENSING, timestamp);
		this.pressure = pressure;
		this.imaValue = imaValue;
	}


	/**
	 * Checks if is pressure.
	 *
	 * @return the pressure
	 */
	public boolean isPressure() {
		return pressure;
	}


	/**
	 * Sets the pressure.
	 *
	 * @param pressure the pressure to set
	 */
	public void setPressure(boolean pressure) {
		this.pressure = pressure;
	}


	/**
	 * Gets the ima value.
	 *
	 * @return the imaValue
	 */
	public double getImaValue() {
		return imaValue;
	}


	/**
	 * Sets the ima value.
	 *
	 * @param imaValue the imaValue to set
	 */
	public void setImaValue(double imaValue) {
		this.imaValue = imaValue;
	}

	
	/* (non-Javadoc)
	 * @see eu.ewall.platform.commons.datamodel.measure.IndoorMeasurement#getMeasuredValue()
	 */
	@JsonIgnore
	public String getMeasuredValue() {
		return measuredValue;
	}

}

