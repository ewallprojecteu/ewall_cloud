package eu.ewall.platform.idss.service.model.state.context;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.service.model.type.WeatherType;

/**
 * This class models a weather condition.
 * 
 * @author Dennis Hofs (RRD)
 */
public class Weather extends AbstractDatabaseObject {
	
	@DatabaseField(value= DatabaseType.STRING)
	private WeatherType type;
	
	@DatabaseField(value=DatabaseType.FLOAT)
	private double temperatureCelsius;

	/**
	 * Returns the weather type.
	 * 
	 * @return the weather type
	 */
	public WeatherType getType() {
		return type;
	}

	/**
	 * Sets the weather type.
	 * 
	 * @param type the weather type
	 */
	public void setType(WeatherType type) {
		this.type = type;
	}

	/**
	 * Returns the temperature in degrees Celsius.
	 * 
	 * @return the temperature in degrees Celsius
	 */
	public double getTemperatureCelsius() {
		return temperatureCelsius;
	}

	/**
	 * Sets the temperature in degrees Celsius.
	 * 
	 * @param temperatureCelsius the temperature in degrees Celsius
	 */
	public void setTemperatureCelsius(double temperatureCelsius) {
		this.temperatureCelsius = temperatureCelsius;
	}
}
