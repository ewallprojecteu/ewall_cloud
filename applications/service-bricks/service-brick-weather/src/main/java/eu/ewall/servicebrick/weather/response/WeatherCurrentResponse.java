package eu.ewall.servicebrick.weather.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import eu.ewall.servicebrick.weather.owm.Weather;
import eu.ewall.servicebrick.weather.owm.WeatherClouds;
import eu.ewall.servicebrick.weather.owm.WeatherCoordinates;
import eu.ewall.servicebrick.weather.owm.WeatherMain;
import eu.ewall.servicebrick.weather.owm.WeatherRain;
import eu.ewall.servicebrick.weather.owm.WeatherSnow;
import eu.ewall.servicebrick.weather.owm.WeatherSys;
import eu.ewall.servicebrick.weather.owm.WeatherWind;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class WeatherCurrentResponse implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Mapping form http://openweathermap.org/weather-data#current
	long id;
	long dt;
	String name;
	int cod;
	WeatherCoordinates coord;
	WeatherSys sys;
	WeatherMain main;
	WeatherWind wind;
	WeatherClouds clouds;
	List<Weather> weather;
	WeatherRain rain;
	WeatherSnow snow;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getDt() {
		return dt;
	}
	public void setDt(long dt) {
		this.dt = dt;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public WeatherCoordinates getCoord() {
		return coord;
	}
	public void setCoord(WeatherCoordinates coord) {
		this.coord = coord;
	}
	public WeatherSys getSys() {
		return sys;
	}
	public void setSys(WeatherSys sys) {
		this.sys = sys;
	}
	public WeatherMain getMain() {
		return main;
	}
	public void setMain(WeatherMain main) {
		this.main = main;
	}
	public WeatherWind getWind() {
		return wind;
	}
	public void setWind(WeatherWind wind) {
		this.wind = wind;
	}
	public WeatherClouds getClouds() {
		return clouds;
	}
	public void setClouds(WeatherClouds clouds) {
		this.clouds = clouds;
	}
	public WeatherRain getRain() {
		return rain;
	}
	public void setRain(WeatherRain rain) {
		this.rain = rain;
	}
	public WeatherSnow getSnow() {
		return snow;
	}
	public void setSnow(WeatherSnow snow) {
		this.snow = snow;
	}
	public List<Weather> getWeather() {
		return weather;
	}
	public void setWeather(List<Weather> weather) {
		this.weather = weather;
	}
	public int getCod() {
		return cod;
	}
	public void setCod(int cod) {
		this.cod = cod;
	}
}
