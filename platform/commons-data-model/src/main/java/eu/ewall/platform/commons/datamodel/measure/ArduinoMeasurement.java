/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.measure;

/**
 * The Class ArduinoMeasurement.
 *
 * @author eandgrg, emirmos
 */
public class ArduinoMeasurement extends Measurement {

	/** The temperature. */
	protected float temperature;

	/** The humidity. */
	protected float humidity;

	/** The illuminance. */
	protected float illuminance;

	/** The movement. */
	protected boolean movement;

	/** The lpg gass. */
	protected boolean lpgGass;

	/** The ng gass. */
	protected boolean ngGass;

	/** The co gass. */
	protected boolean coGass;

	/**
	 * The Constructor.
	 */
	public ArduinoMeasurement() {

	}


	/**
	 * Instantiates a new arduino measurement without description.
	 *
	 * @param timestamp
	 *            the timestamp
	 * @param temperature
	 *            the temperature
	 * @param humidity
	 *            the humidity
	 * @param illuminance
	 *            the illuminance
	 * @param movement
	 *            the movement
	 * @param lpgGass
	 *            the lpg gass
	 * @param ngGass
	 *            the ng gass
	 * @param coGass
	 *            the co gass
	 */
	public ArduinoMeasurement(long timestamp, float temperature,
			float humidity, float illuminance, boolean movement,
			boolean lpgGass, boolean ngGass, boolean coGass) {
		this.timestamp = timestamp;
		this.temperature = temperature;
		this.humidity = humidity;
		this.illuminance = illuminance;
		this.movement = movement;
		this.lpgGass = lpgGass;
		this.ngGass = ngGass;
		this.coGass = coGass;
	}

	/**
	 * Gets the temperature.
	 *
	 * @return the temperature
	 */
	public float getTemperature() {
		return temperature;
	}

	/**
	 * Sets the temperature.
	 *
	 * @param temperature
	 *            the temperature to set
	 */
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	/**
	 * Gets the humidity.
	 *
	 * @return the humidity
	 */
	public float getHumidity() {
		return humidity;
	}

	/**
	 * Sets the humidity.
	 *
	 * @param humidity
	 *            the humidity to set
	 */
	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}

	/**
	 * Gets the illuminance.
	 *
	 * @return the illuminance
	 */
	public float getIlluminance() {
		return illuminance;
	}

	/**
	 * Sets the illuminance.
	 *
	 * @param illuminance
	 *            the illuminance to set
	 */
	public void setIlluminance(float illuminance) {
		this.illuminance = illuminance;
	}

	/**
	 * Checks if is movement.
	 *
	 * @return the movement
	 */
	public boolean isMovement() {
		return movement;
	}

	/**
	 * Sets the movement.
	 *
	 * @param movement
	 *            the movement to set
	 */
	public void setMovement(boolean movement) {
		this.movement = movement;
	}

	/**
	 * Checks if is lpg gass.
	 *
	 * @return the lpgGass
	 */
	public boolean isLpgGass() {
		return lpgGass;
	}

	/**
	 * Sets the lpg gass.
	 *
	 * @param lpgGass
	 *            the lpgGass to set
	 */
	public void setLpgGass(boolean lpgGass) {
		this.lpgGass = lpgGass;
	}

	/**
	 * Checks if is ng gass.
	 *
	 * @return the ngGass
	 */
	public boolean isNgGass() {
		return ngGass;
	}

	/**
	 * Sets the ng gass.
	 *
	 * @param ngGass
	 *            the ngGass to set
	 */
	public void setNgGass(boolean ngGass) {
		this.ngGass = ngGass;
	}

	/**
	 * Checks if is co gass.
	 *
	 * @return the coGass
	 */
	public boolean isCoGass() {
		return coGass;
	}

	/**
	 * Sets the co gass.
	 *
	 * @param coGass
	 *            the coGass to set
	 */
	public void setCoGass(boolean coGass) {
		this.coGass = coGass;
	}

}
