package eu.ewall.servicebrick.weather.owm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherSnow  implements Serializable {

	@JsonProperty("3h")
	float three_h;

	public float getThree_h() {
		return three_h;
	}

	public void setThree_h(float three_h) {
		this.three_h = three_h;
	}
	
}
