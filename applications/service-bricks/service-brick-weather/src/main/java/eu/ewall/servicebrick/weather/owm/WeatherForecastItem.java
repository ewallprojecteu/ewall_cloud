package eu.ewall.servicebrick.weather.owm;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherForecastItem  implements Serializable {

	long dt;
	WeatherMain main;
	List<Weather> weather;
	WeatherClouds clouds;
	WeatherWind wind;
	WeatherSys sys;
	String dt_txt;
	
	public long getDt() {
		return dt;
	}
	public void setDt(long dt) {
		this.dt = dt;
	}
	public WeatherMain getMain() {
		return main;
	}
	public void setMain(WeatherMain main) {
		this.main = main;
	}
	public List<Weather> getWeather() {
		return weather;
	}
	public void setWeather(List<Weather> weather) {
		this.weather = weather;
	}
	public WeatherClouds getClouds() {
		return clouds;
	}
	public void setClouds(WeatherClouds clouds) {
		this.clouds = clouds;
	}
	public WeatherWind getWind() {
		return wind;
	}
	public void setWind(WeatherWind wind) {
		this.wind = wind;
	}
	public WeatherSys getSys() {
		return sys;
	}
	public void setSys(WeatherSys sys) {
		this.sys = sys;
	}
	public String getDt_txt() {
		return dt_txt;
	}
	public void setDt_txt(String dt_txt) {
		this.dt_txt = dt_txt;
	}
	
}
