package eu.ewall.servicebrick.weather.owm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class WeatherRain  implements Serializable {

	@JsonProperty("3h")
	float three_h = 0.0f;

	public float getThree_h() {
		return three_h;
	}

	public void setThree_h(float three_h) {
		this.three_h = three_h;
	} 
	
}
