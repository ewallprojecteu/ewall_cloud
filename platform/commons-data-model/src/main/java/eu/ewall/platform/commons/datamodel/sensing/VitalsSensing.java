/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

package eu.ewall.platform.commons.datamodel.sensing;

/**
 * The Class HealthSensing.
 * 
 * @author EMIRMOS
 */
public class VitalsSensing extends Sensing {

	/** The hearth rate. */
	private int hearthRate;

	/** The hearth rate variability. */
	private int hearthRateVariability;

	/** The oxygen saturation. */
	private int oxygenSaturation;

	/** The systolic blood pressure. */
	private int systolicBloodPressure;

	/** The diastolic blood pressure. */
	private int diastolicBloodPressure;

	/**
	 * Gets the hearth rate.
	 *
	 * @return the hearthRate
	 */
	public int getHearthRate() {
		return hearthRate;
	}

	/**
	 * Sets the hearth rate.
	 *
	 * @param hearthRate
	 *            the hearthRate to set
	 */
	public void setHearthRate(int hearthRate) {
		this.hearthRate = hearthRate;
	}

	/**
	 * Gets the oxygen saturation.
	 *
	 * @return the oxygenSaturation
	 */
	public int getOxygenSaturation() {
		return oxygenSaturation;
	}

	/**
	 * Sets the oxygen saturation.
	 *
	 * @param oxygenSaturation
	 *            the oxygenSaturation to set
	 */
	public void setOxygenSaturation(int oxygenSaturation) {
		this.oxygenSaturation = oxygenSaturation;
	}

	/**
	 * Gets the hearth rate variability.
	 *
	 * @return the hearthRateVariability
	 */
	public int getHearthRateVariability() {
		return hearthRateVariability;
	}

	/**
	 * Sets the hearth rate variability.
	 *
	 * @param hearthRateVariability
	 *            the hearthRateVariability to set
	 */
	public void setHearthRateVariability(int hearthRateVariability) {
		this.hearthRateVariability = hearthRateVariability;
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
	 * @param systolicBloodPressure
	 *            the systolicBloodPressure to set
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
	 * @param diastolicBloodPressure
	 *            the diastolicBloodPressure to set
	 */
	public void setDiastolicBloodPressure(int diastolicBloodPressure) {
		this.diastolicBloodPressure = diastolicBloodPressure;
	}

}
