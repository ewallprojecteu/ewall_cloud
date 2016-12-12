package eu.ewall.platform.idss.response.ewall.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherCoordinates {
	
	@JsonProperty("lat")
	private double latitude;
	
	@JsonProperty("lon")
	private double longitude;
	
	// ----- GETTERS -----
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	// ----- SETTERS -----
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
}
