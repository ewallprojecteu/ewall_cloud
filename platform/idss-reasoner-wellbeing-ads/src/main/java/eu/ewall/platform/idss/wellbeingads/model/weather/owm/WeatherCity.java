package eu.ewall.platform.idss.wellbeingads.model.weather.owm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class WeatherCity.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherCity implements Serializable {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4128800161339640939L;
	
	/** The City identification. */
	long id;
	
	/** The City name. */
	String name;
	
	/** The country. */
	String country;
	
	/** The coordinates */
	WeatherCoordinates coord;
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the country.
	 *
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	
	/**
	 * Sets the country.
	 *
	 * @param country the new country
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	
	/**
	 * Gets the coord.
	 *
	 * @return the coord
	 */
	public WeatherCoordinates getCoord() {
		return coord;
	}
	
	/**
	 * Sets the coord.
	 *
	 * @param coord the new coord
	 */
	public void setCoord(WeatherCoordinates coord) {
		this.coord = coord;
	}
	
}
