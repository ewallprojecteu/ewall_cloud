package eu.ewall.platform.idss.wellbeingads.model.weather.owm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class WeatherCoordinates.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherCoordinates implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2108043751264530393L;

	/** The geo location, lattitude. */
	String lat;
	
	/** The geo location, longitude. */
	String lon;
	
	/**
	 * Gets the lat.
	 *
	 * @return the lat
	 */
	public String getLat() {
		return lat;
	}
	
	/**
	 * Sets the lat.
	 *
	 * @param lat the new lat
	 */
	public void setLat(String lat) {
		this.lat = lat;
	}
	
	/**
	 * Gets the lon.
	 *
	 * @return the lon
	 */
	public String getLon() {
		return lon;
	}
	
	/**
	 * Sets the lon.
	 *
	 * @param lon the new lon
	 */
	public void setLon(String lon) {
		this.lon = lon;
	}
	
}
