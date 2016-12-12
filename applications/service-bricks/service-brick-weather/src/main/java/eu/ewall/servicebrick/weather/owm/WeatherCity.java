package eu.ewall.servicebrick.weather.owm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherCity implements Serializable {

	
	long id;
	String name;
	String country;
	WeatherCoordinates coord;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public WeatherCoordinates getCoord() {
		return coord;
	}
	public void setCoord(WeatherCoordinates coord) {
		this.coord = coord;
	}
	
}
