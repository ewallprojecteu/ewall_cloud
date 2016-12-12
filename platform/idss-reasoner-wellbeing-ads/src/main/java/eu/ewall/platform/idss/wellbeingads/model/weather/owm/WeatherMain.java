package eu.ewall.platform.idss.wellbeingads.model.weather.owm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class WeatherMain.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherMain  implements Serializable {
	
	private static final long serialVersionUID = -4361708124214479569L;

	/** The temp. */
	float temp;
	
	/** The humidity. */
	int humidity;
	
	/** The temp_min. */
	float temp_min;
	
	/** The temp_max. */
	float temp_max;
	
	/** The pressure. */
	int pressure;
	
	/** The sea_level. */
	int sea_level;
	
	/** The grnd_level. */
	int grnd_level;
	
	/**
	 * Gets the temp.
	 *
	 * @return the temp
	 */
	public float getTemp() {
		return temp;
	}
	
	/**
	 * Sets the temp.
	 *
	 * @param temp the new temp
	 */
	public void setTemp(float temp) {
		this.temp = temp;
	}
	
	/**
	 * Gets the humidity.
	 *
	 * @return the humidity
	 */
	public int getHumidity() {
		return humidity;
	}
	
	/**
	 * Sets the humidity.
	 *
	 * @param humidity the new humidity
	 */
	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}
	
	/**
	 * Gets the temp_min.
	 *
	 * @return the temp_min
	 */
	public float getTemp_min() {
		return temp_min;
	}
	
	/**
	 * Sets the temp_min.
	 *
	 * @param temp_min the new temp_min
	 */
	public void setTemp_min(float temp_min) {
		this.temp_min = temp_min;
	}
	
	/**
	 * Gets the temp_max.
	 *
	 * @return the temp_max
	 */
	public float getTemp_max() {
		return temp_max;
	}
	
	/**
	 * Sets the temp_max.
	 *
	 * @param temp_max the new temp_max
	 */
	public void setTemp_max(float temp_max) {
		this.temp_max = temp_max;
	}
	
	/**
	 * Gets the pressure.
	 *
	 * @return the pressure
	 */
	public int getPressure() {
		return pressure;
	}
	
	/**
	 * Sets the pressure.
	 *
	 * @param pressure the new pressure
	 */
	public void setPressure(int pressure) {
		this.pressure = pressure;
	}
	
	/**
	 * Gets the sea_level.
	 *
	 * @return the sea_level
	 */
	public int getSea_level() {
		return sea_level;
	}
	
	/**
	 * Sets the sea_level.
	 *
	 * @param sea_level the new sea_level
	 */
	public void setSea_level(int sea_level) {
		this.sea_level = sea_level;
	}
	
	/**
	 * Gets the grnd_level.
	 *
	 * @return the grnd_level
	 */
	public int getGrnd_level() {
		return grnd_level;
	}
	
	/**
	 * Sets the grnd_level.
	 *
	 * @param grnd_level the new grnd_level
	 */
	public void setGrnd_level(int grnd_level) {
		this.grnd_level = grnd_level;
	}
	
}
