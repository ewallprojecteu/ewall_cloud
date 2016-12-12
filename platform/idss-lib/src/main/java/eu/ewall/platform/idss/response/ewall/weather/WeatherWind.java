package eu.ewall.platform.idss.response.ewall.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherWind {
	
	@JsonProperty("speed")
	private Double windSpeed;
	
	@JsonProperty("deg")
	private Double windDirection;
	
	@JsonProperty("gust")
	private Double windGust;
	
	// ----- GETTERS -----
	
	/**
	 * Returns the wind speed in meter/sec or miles/hour depending on the used metrics
	 * (see {@link WeatherServiceBrickResponse#getUnitsOfMeasure()}.
	 * @return the wind speed.
	 */
	public Double getWindSpeed() {
		return windSpeed;
	}
	
	/**
	 * Returns the wind direction in meteorological degrees.
	 * @return the wind direction in meteorological degrees.
	 */
	public Double getWindDirection() {
		return windDirection;
	}
	
	/**
	 * Returns the speed of wind gusts in meter/sec or miles/hour depending on the used metrics
	 * (see {@link WeatherServiceBrickResponse#getUnitsOfMeasure()}.
	 * @return the speed of wind gusts.
	 */
	public Double getWindGust() {
		return windGust;
	}
	
	// ----- SETTERS -----
	
	public void setWindSpeed(Double windSpeed) {
		this.windSpeed = windSpeed;
	}
	
	public void setWindDirection(Double windDirection) {
		this.windDirection = windDirection;
	}
	
	public void setWindGust(Double windGust) {
		this.windGust = windGust;
	}
}
