/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.idss.wellbeingads.model.weather;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import eu.ewall.platform.idss.wellbeingads.model.weather.owm.Weather;
import eu.ewall.platform.idss.wellbeingads.model.weather.owm.WeatherClouds;
import eu.ewall.platform.idss.wellbeingads.model.weather.owm.WeatherCoordinates;
import eu.ewall.platform.idss.wellbeingads.model.weather.owm.WeatherMain;
import eu.ewall.platform.idss.wellbeingads.model.weather.owm.WeatherRain;
import eu.ewall.platform.idss.wellbeingads.model.weather.owm.WeatherSnow;
import eu.ewall.platform.idss.wellbeingads.model.weather.owm.WeatherSys;
import eu.ewall.platform.idss.wellbeingads.model.weather.owm.WeatherWind;

/**
 * The Class WeatherCurrentResponse contains parameters of API respond for
 * current and historical weather, see:
 * http://openweathermap.org/weather-data#current.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class WeatherCurrentResponse implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The City identification. */
	long id;

	/** The Data receiving time. */
	long dt;

	/** The City name. */
	String name;

	/** The cod. */
	int cod;

	/** The coordinates (lattitude and longitude). */
	WeatherCoordinates coord;

	/** The sys. */
	WeatherSys sys;

	/** The main. */
	WeatherMain main;

	/** The wind. */
	WeatherWind wind;

	/** The clouds. */
	WeatherClouds clouds;

	/** The weather. */
	List<Weather> weather;

	/** The rain. */
	WeatherRain rain;

	/** The snow. */
	WeatherSnow snow;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

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
	 * @param dt
	 *            the new dt
	 */
	public void setDt(long dt) {
		this.dt = dt;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the coord.
	 *
	 * @return the coord
	 */
	public WeatherCoordinates getCoord() {
		return coord;
	}

	/**
	 * Sets the coord.
	 *
	 * @param coord
	 *            the new coord
	 */
	public void setCoord(WeatherCoordinates coord) {
		this.coord = coord;
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
	 * @param sys
	 *            the new sys
	 */
	public void setSys(WeatherSys sys) {
		this.sys = sys;
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
	 * @param main
	 *            the new main
	 */
	public void setMain(WeatherMain main) {
		this.main = main;
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
	 * @param wind
	 *            the new wind
	 */
	public void setWind(WeatherWind wind) {
		this.wind = wind;
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
	 * @param clouds
	 *            the new clouds
	 */
	public void setClouds(WeatherClouds clouds) {
		this.clouds = clouds;
	}

	/**
	 * Gets the rain.
	 *
	 * @return the rain
	 */
	public WeatherRain getRain() {
		return rain;
	}

	/**
	 * Sets the rain.
	 *
	 * @param rain
	 *            the new rain
	 */
	public void setRain(WeatherRain rain) {
		this.rain = rain;
	}

	/**
	 * Gets the snow.
	 *
	 * @return the snow
	 */
	public WeatherSnow getSnow() {
		return snow;
	}

	/**
	 * Sets the snow.
	 *
	 * @param snow
	 *            the new snow
	 */
	public void setSnow(WeatherSnow snow) {
		this.snow = snow;
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
	 * @param weather
	 *            the new weather
	 */
	public void setWeather(List<Weather> weather) {
		this.weather = weather;
	}

	/**
	 * Gets the cod.
	 *
	 * @return the cod
	 */
	public int getCod() {
		return cod;
	}

	/**
	 * Sets the cod.
	 *
	 * @param cod
	 *            the new cod
	 */
	public void setCod(int cod) {
		this.cod = cod;
	}
}
