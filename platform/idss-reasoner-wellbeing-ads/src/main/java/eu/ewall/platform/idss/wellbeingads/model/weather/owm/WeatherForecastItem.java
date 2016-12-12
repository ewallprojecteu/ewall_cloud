package eu.ewall.platform.idss.wellbeingads.model.weather.owm;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class WeatherForecastItem.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherForecastItem  implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8014652332533216522L;
	
	/** The dt. */
	long dt;
	
	/** The main. */
	WeatherMain main;
	
	/** The weather. */
	List<Weather> weather;
	
	/** The clouds. */
	WeatherClouds clouds;
	
	/** The wind. */
	WeatherWind wind;
	
	/** The sys. */
	WeatherSys sys;
	
	/** The dt_txt. */
	String dt_txt;
	
	/**
	 * Gets the dt.
	 *
	 * @return the dt
	 */
	public long getDt() {
		return dt;
	}
	
	/**
	 * Sets the dt.
	 *
	 * @param dt the new dt
	 */
	public void setDt(long dt) {
		this.dt = dt;
	}
	
	/**
	 * Gets the main.
	 *
	 * @return the main
	 */
	public WeatherMain getMain() {
		return main;
	}
	
	/**
	 * Sets the main.
	 *
	 * @param main the new main
	 */
	public void setMain(WeatherMain main) {
		this.main = main;
	}
	
	/**
	 * Gets the weather.
	 *
	 * @return the weather
	 */
	public List<Weather> getWeather() {
		return weather;
	}
	
	/**
	 * Sets the weather.
	 *
	 * @param weather the new weather
	 */
	public void setWeather(List<Weather> weather) {
		this.weather = weather;
	}
	
	/**
	 * Gets the clouds.
	 *
	 * @return the clouds
	 */
	public WeatherClouds getClouds() {
		return clouds;
	}
	
	/**
	 * Sets the clouds.
	 *
	 * @param clouds the new clouds
	 */
	public void setClouds(WeatherClouds clouds) {
		this.clouds = clouds;
	}
	
	/**
	 * Gets the wind.
	 *
	 * @return the wind
	 */
	public WeatherWind getWind() {
		return wind;
	}
	
	/**
	 * Sets the wind.
	 *
	 * @param wind the new wind
	 */
	public void setWind(WeatherWind wind) {
		this.wind = wind;
	}
	
	/**
	 * Gets the sys.
	 *
	 * @return the sys
	 */
	public WeatherSys getSys() {
		return sys;
	}
	
	/**
	 * Sets the sys.
	 *
	 * @param sys the new sys
	 */
	public void setSys(WeatherSys sys) {
		this.sys = sys;
	}
	
	/**
	 * Gets the dt_txt.
	 *
	 * @return the dt_txt
	 */
	public String getDt_txt() {
		return dt_txt;
	}
	
	/**
	 * Sets the dt_txt.
	 *
	 * @param dt_txt the new dt_txt
	 */
	public void setDt_txt(String dt_txt) {
		this.dt_txt = dt_txt;
	}
	
}
