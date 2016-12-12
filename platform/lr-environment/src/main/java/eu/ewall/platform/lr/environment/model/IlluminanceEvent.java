package eu.ewall.platform.lr.environment.model;

import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import eu.ewall.platform.idss.utils.json.DateTimeFromIsoDateTimeDeserializer;

public class IlluminanceEvent {
	private String username;
	
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime timestamp;
	
	private String location;
	private Double illuminance;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public DateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Double getIlluminance() {
		return illuminance;
	}
	public void setIlluminance(Double illuminance) {
		this.illuminance = illuminance;
	}
	
}
