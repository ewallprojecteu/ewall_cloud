package eu.ewall.platform.idss.response.ewall.weather;

import java.util.List;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import eu.ewall.platform.idss.service.model.type.UnitSystem;
import eu.ewall.platform.idss.utils.json.DateTimeFromEpochTimeDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherServiceBrickResponse {
	
	@JsonProperty("id")
	private Integer id;
	
	@JsonProperty("dt")
	@JsonDeserialize(using=DateTimeFromEpochTimeDeserializer.class)
	private DateTime retrievalTime;

	@JsonProperty("name")
	private String locationDescriptor;
	
	@JsonProperty("coord")
	private WeatherCoordinates weatherCoordinates;
	
	@JsonProperty("sys")
	private WeatherSystem weatherSystem;
	
	@JsonProperty("main")
	private WeatherMain weatherMain;
	
	@JsonProperty("clouds")
	private WeatherClouds weatherClouds;
	
	@JsonProperty("weather")
	private List<WeatherCondition> weatherConditions;
	
	@JsonProperty("rain")
	private WeatherRain weatherRain;
	
	@JsonProperty("snow")
	private WeatherSnow weatherSnow;
	
	private UnitSystem unitsOfMeasure;
	
	// ----- GETTERS -----
	
	public Integer getId() {
		return id;
	}
	
	public DateTime getRetrievalTime() {
		return retrievalTime;
	}
	
	public String getLocationDescriptor() {
		return locationDescriptor;
	}
	
	public WeatherCoordinates getWeatherCoordinates() {
		return weatherCoordinates;
	}
	
	public WeatherSystem getWeatherSystem() {
		return weatherSystem;
	}
	
	public WeatherMain getWeatherMain() {
		return weatherMain;
	}
	
	public WeatherClouds getWeatherClouds() {
		return weatherClouds;
	}
	
	public List<WeatherCondition> getWeatherConditions() {
		return weatherConditions;
	}
	
	public WeatherRain getWeatherRain() {
		return weatherRain;
	}
	
	public WeatherSnow getWeatherSnow() {
		return weatherSnow;
	}
	
	public UnitSystem getUnitsOfMeasure() {
		return unitsOfMeasure;
	}
	
	// ----- SETTERS -----
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setRetrievalTime(DateTime retrievalTime) {
		this.retrievalTime = retrievalTime;
	}
	
	public void setLocationDescriptor(String locationDescriptor) {
		this.locationDescriptor = locationDescriptor;
	}
	
	public void setWeatherCoordinates(WeatherCoordinates weatherCoordinates) {
		this.weatherCoordinates = weatherCoordinates;
	}
	
	public void setWeatherSystem(WeatherSystem weatherSystem) {
		this.weatherSystem = weatherSystem;
	}
	
	public void setWeatherMain(WeatherMain weatherMain) {
		this.weatherMain = weatherMain;
	}
	
	public void setWeatherClouds(WeatherClouds weatherClouds) {
		this.weatherClouds = weatherClouds;
	}
	
	public void setWeatherConditions(List<WeatherCondition> weatherConditions) {
		this.weatherConditions = weatherConditions;
	}
	
	public void setWeatherRain(WeatherRain weatherRain) {
		this.weatherRain = weatherRain;
	}
	
	public void setWeatherSnow(WeatherSnow weatherSnow) {
		this.weatherSnow = weatherSnow;
	}
	
	public void setUnitsOfMeasure(UnitSystem unitsOfMeasure) {
		this.unitsOfMeasure = unitsOfMeasure;
	}
	
	// ----- toString -----
	
	@Override
	public String toString() {
		String result = "";
		result += "["+retrievalTime.toString()+"] "+weatherMain.getTemperature()+" degrees "+getTemperatureUnit()+".";
		return result;
	}
	
	private String getTemperatureUnit() {
		if(unitsOfMeasure == null) {
			return "?";
		} else if(unitsOfMeasure == UnitSystem.STANDARD) {
			return "Kelvin";
		} else if(unitsOfMeasure == UnitSystem.METRIC) {
			return "Celcius";
		} else if(unitsOfMeasure == UnitSystem.IMPERIAL) {
			return "Fahrenheit";
		} else {
			return "?";
		}
	}
	
}
