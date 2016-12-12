package eu.ewall.platform.idss.wellbeingads.model.weather.owm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class WeatherSys.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherSys  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1433279590929973988L;

	/** The System parameter. */
	String message;
	
	/** The country  code (GB, JP etc.)  */
	String country;
	
	/** The  Sunrise time . */
	long sunrise;
	
	/** The Sunset time. */
	long sunset;
	
	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
		this.message = message;
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
	 * Gets the sunrise.
	 *
	 * @return the sunrise
	 */
	public long getSunrise() {
		return sunrise;
	}
	
	/**
	 * Sets the sunrise.
	 *
	 * @param sunrise the new sunrise
	 */
	public void setSunrise(long sunrise) {
		this.sunrise = sunrise;
	}
	
	/**
	 * Gets the sunset.
	 *
	 * @return the sunset
	 */
	public long getSunset() {
		return sunset;
	}
	
	/**
	 * Sets the sunset.
	 *
	 * @param sunset the new sunset
	 */
	public void setSunset(long sunset) {
		this.sunset = sunset;
	}
	
}
