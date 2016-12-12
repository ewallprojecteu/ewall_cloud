package eu.ewall.platform.idss.response.ewall.weather;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import eu.ewall.platform.idss.utils.json.DateTimeFromEpochTimeDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherSystem {
	
	@JsonProperty("country")
	private String countryCode;
	
	@JsonProperty("sunrise")
	@JsonDeserialize(using=DateTimeFromEpochTimeDeserializer.class)
	private DateTime sunRise;
	
	@JsonProperty("sunset")
	@JsonDeserialize(using=DateTimeFromEpochTimeDeserializer.class)
	private DateTime sunSet;
	
	// ----- GETTERS -----
	
	public String getCountryCode() {
		return countryCode;
	}
	
	public DateTime getSunRise() {
		return sunRise;
	}
	
	public DateTime getSunSet() {
		return sunSet;
	}
	
	// ----- SETTERS -----
	
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public void setSunRise(DateTime sunRise) {
		this.sunRise = sunRise;
	}
	
	public void setSunSet(DateTime sunSet) {
		this.sunSet = sunSet;
	}
}
