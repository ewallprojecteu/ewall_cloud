package eu.ewall.servicebrick.weather.owm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherClouds implements Serializable {

	int all;

	public int getAll() {
		return all;
	}

	public void setAll(int all) {
		this.all = all;
	}
	
}
