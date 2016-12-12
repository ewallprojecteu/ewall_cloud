package eu.ewall.platform.idss.response.ewall.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherMain {
	
	@JsonProperty("temp")
	private Double temperature;
	
	@JsonProperty("humidity")
	private Double humidity;
	
	@JsonProperty("temp_min")
	private Double minimumTemperature;
	
	@JsonProperty("temp_max")
	private Double maximumTemperature;
	
	@JsonProperty("pressure")
	private Double atmosphericPressure;
	
	@JsonProperty("sea_level")
	private Double atmosphericPressureSeaLevel;
	
	@JsonProperty("grnd_level")
	private Double atmosphericPressureGroundLevel;
	
	// ----- GETTERS -----
	
	public Double getTemperature() {
		return temperature;
	}
	
	public Double getHumidity() {
		return humidity;
	}
	
	public Double getMinimumTemperature() {
		return minimumTemperature;
	}
	
	public Double getMaximumTemperature() {
		return maximumTemperature;
	}
	
	public Double getAtmosphericPressure() {
		return atmosphericPressure;
	}
	
	public Double getAtmosphericPressureSeaLevel() {
		return atmosphericPressureSeaLevel;
	}
	
	public Double getAtmosphericPressureGroundLevel() {
		return atmosphericPressureGroundLevel;
	}
	
	// ----- SETTERS -----
	
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	
	public void setHumidity(Double humidity) {
		this.humidity = humidity;
	}
	
	public void setMinimumTemperature(Double minimumTemperature) {
		this.minimumTemperature = minimumTemperature;
	}
	
	public void setMaximumTemperature(Double maximumTemperature) {
		this.maximumTemperature = maximumTemperature;
	}
	
	public void setAtmosphericPressure(Double atmosphericPressure) {
		this.atmosphericPressure = atmosphericPressure;
	}
	
	public void setAtmosphericPressureSeaLevel(Double atmosphericPressureSeaLevel) {
		this.atmosphericPressureSeaLevel = atmosphericPressureSeaLevel;
	}
	
	public void setAtmosphericPressureGroundLevel(Double atmosphericPressureGroundLevel) {
		this.atmosphericPressureGroundLevel = atmosphericPressureGroundLevel;
	}
}
