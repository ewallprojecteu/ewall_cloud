package eu.ewall.platform.idss.wellbeingads.model.weather.owm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherClouds implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4320649205461590158L;
	int all;

	public int getAll() {
		return all;
	}

	public void setAll(int all) {
		this.all = all;
	}
	
}
