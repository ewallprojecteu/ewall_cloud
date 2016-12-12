/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

package eu.ewall.platform.commons.datamodel.sensing;

/**
 * The Class EnvironmentalSensing.
 *
 * @author EMIRMOS
 */
public class EnvironmentalSensing extends Sensing {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The movement. */
	private Boolean movement;
	
	/** The illuminance. */
	private Double illuminance;
	
	/** The temperature. */
	private Double temperature;
	
	/** The humidity. */
	private Double humidity;
	
	/** The natural gas. */
	private Integer naturalGas;
	
	/** The carbon monoxide. */
	private Integer carbonMonoxide;
	
	/** The liquefied petroleum gas. */
	private Integer liquefiedPetroleumGas;
	
	/** The door open. */
	private Boolean doorOpen;
	
	/** The indoor place name. */
	protected String indoorPlaceName;

	/**
	 * Gets the movement.
	 *
	 * @return the movement
	 */
	public Boolean getMovement() {
		return movement;
	}

	/**
	 * Sets the movement.
	 *
	 * @param movement the new movement
	 */
	public void setMovement(Boolean movement) {
		this.movement = movement;
	}

	/**
	 * Gets the illuminance.
	 *
	 * @return the illuminance
	 */
	public Double getIlluminance() {
		return illuminance;
	}

	/**
	 * Sets the illuminance.
	 *
	 * @param illuminance the new illuminance
	 */
	public void setIlluminance(Double illuminance) {
		this.illuminance = illuminance;
	}

	/**
	 * Gets the temperature.
	 *
	 * @return the temperature
	 */
	public Double getTemperature() {
		return temperature;
	}

	/**
	 * Sets the temperature.
	 *
	 * @param temperature the new temperature
	 */
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	/**
	 * Gets the humidity.
	 *
	 * @return the humidity
	 */
	public Double getHumidity() {
		return humidity;
	}

	/**
	 * Sets the humidity.
	 *
	 * @param humidity the new humidity
	 */
	public void setHumidity(Double humidity) {
		this.humidity = humidity;
	}

	/**
	 * Gets the natural gas.
	 *
	 * @return the natural gas
	 */
	public Integer getNaturalGas() {
		return naturalGas;
	}

	/**
	 * Sets the natural gas.
	 *
	 * @param naturalGas the new natural gas
	 */
	public void setNaturalGas(Integer naturalGas) {
		this.naturalGas = naturalGas;
	}

	/**
	 * Gets the carbon monoxide.
	 *
	 * @return the carbon monoxide
	 */
	public Integer getCarbonMonoxide() {
		return carbonMonoxide;
	}

	/**
	 * Sets the carbon monoxide.
	 *
	 * @param carbonMonoxide the new carbon monoxide
	 */
	public void setCarbonMonoxide(Integer carbonMonoxide) {
		this.carbonMonoxide = carbonMonoxide;
	}

	/**
	 * Gets the liquefied petroleum gas.
	 *
	 * @return the liquefied petroleum gas
	 */
	public Integer getLiquefiedPetroleumGas() {
		return liquefiedPetroleumGas;
	}

	/**
	 * Sets the liquefied petroleum gas.
	 *
	 * @param liquefiedPetroleumGas the new liquefied petroleum gas
	 */
	public void setLiquefiedPetroleumGas(Integer liquefiedPetroleumGas) {
		this.liquefiedPetroleumGas = liquefiedPetroleumGas;
	}

	/**
	 * Gets the door open.
	 *
	 * @return the door open
	 */
	public Boolean getDoorOpen() {
		return doorOpen;
	}

	/**
	 * Sets the door open.
	 *
	 * @param doorOpen the new door open
	 */
	public void setDoorOpen(Boolean doorOpen) {
		this.doorOpen = doorOpen;
	}

	/**
	 * Gets the indoor place name.
	 *
	 * @return the indoor place name
	 */
	public String getIndoorPlaceName() {
		return indoorPlaceName;
	}

	/**
	 * Sets the indoor place name.
	 *
	 * @param indoorPlaceName the new indoor place name
	 */
	public void setIndoorPlaceName(String indoorPlaceName) {
		this.indoorPlaceName = indoorPlaceName;
	}

}
