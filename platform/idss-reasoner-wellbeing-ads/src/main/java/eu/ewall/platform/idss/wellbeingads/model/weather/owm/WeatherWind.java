package eu.ewall.platform.idss.wellbeingads.model.weather.owm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class WeatherWind.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherWind  implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2524181433021361292L;
	
	/** The Wind speed. */
	float speed;
	
	/** The Wind direction-> degrees (meteorological) */
	float deg;
	
	/** The Wind gust-> meter/sec */
	float gust;
	
	/**
	 * Gets the speed.
	 *
	 * @return the speed
	 */
	public float getSpeed() {
		return speed;
	}
	
	/**
	 * Sets the speed.
	 *
	 * @param speed the new speed
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	/**
	 * Gets the deg.
	 *
	 * @return the deg
	 */
	public float getDeg() {
		return deg;
	}
	
	/**
	 * Sets the deg.
	 *
	 * @param deg the new deg
	 */
	public void setDeg(float deg) {
		this.deg = deg;
	}
	
	/**
	 * Gets the gust.
	 *
	 * @return the gust
	 */
	public float getGust() {
		return gust;
	}
	
	/**
	 * Sets the gust.
	 *
	 * @param gust the new gust
	 */
	public void setGust(float gust) {
		this.gust = gust;
	}
	
}
