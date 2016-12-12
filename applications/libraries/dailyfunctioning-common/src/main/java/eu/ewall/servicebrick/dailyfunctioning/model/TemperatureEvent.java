package eu.ewall.servicebrick.dailyfunctioning.model;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.ewall.servicebrick.common.model.SensorEvent;

/**
 * 
 */
@Document(collection="temperatureEvents")
public class TemperatureEvent extends SensorEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private double temperature;
	private String measurementUnit;
	
	public TemperatureEvent(DateTime timestamp, double temperature) {
		this(null, timestamp, temperature);
	}
	
	@PersistenceConstructor
	public TemperatureEvent(String username, DateTime timestamp, double temperature) {
		this(username, timestamp, temperature, null);
	}

	public TemperatureEvent(String username, DateTime timestamp, double temperature, String location) {
		this.username = username;
		this.timestamp = timestamp;
		this.temperature = temperature;
		this.setLocation(location);
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public String getMeasurementUnit() {
		return measurementUnit;
	}

	public void setMeasurementUnit(String measurementUnit) {
		this.measurementUnit = measurementUnit;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();	
		sb.append("timestamp: "  + timestamp.toDate() + ", username: " + username + ", temperature: "  + temperature + ", location: " + location);
		return sb.toString();
	}
		
}
