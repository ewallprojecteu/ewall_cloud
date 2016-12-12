package eu.ewall.platform.idss.response.ewall.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherClouds {
	
	@JsonProperty("all")
	private Double cloudiness;
	
	// ----- GETTERS: -----
	
	public Double getCloudiness() {
		return cloudiness;
	}
	
	// ----- SETTERS: -----
	
	public void setCloudiness(Double cloudiness) {
		this.cloudiness = cloudiness;
	}
}
