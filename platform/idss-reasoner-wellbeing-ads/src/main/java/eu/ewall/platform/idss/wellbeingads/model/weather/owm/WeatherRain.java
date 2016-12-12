package eu.ewall.platform.idss.wellbeingads.model.weather.owm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class WeatherRain.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class WeatherRain  implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3813141809262220035L;
	
	/** The Precipitation volume for last 3 hours */
	@JsonProperty("3h")
	float three_h = 0.0f;

	/**
	 * Gets the three_h.
	 *
	 * @return the three_h
	 */
	public float getThree_h() {
		return three_h;
	}

	/**
	 * Sets the three_h.
	 *
	 * @param three_h the new three_h
	 */
	public void setThree_h(float three_h) {
		this.three_h = three_h;
	} 
	
}
