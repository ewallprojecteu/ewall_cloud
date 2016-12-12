package eu.ewall.platform.idss.wellbeingads.model.weather.owm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class WeatherSnow.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherSnow  implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3219583738709840330L;
	
	/** The Snow volume for last 3 hours . */
	@JsonProperty("3h")
	float three_h;

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
