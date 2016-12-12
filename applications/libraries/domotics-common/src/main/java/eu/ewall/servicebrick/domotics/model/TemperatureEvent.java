package eu.ewall.servicebrick.domotics.model;

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
	private DateTime from;
	private DateTime to;
	
	public TemperatureEvent() {
		// TODO Auto-generated constructor stub
	}
	
	public TemperatureEvent(DateTime timestamp, double temperature) {
		this(null, timestamp, temperature);
	}
	
	@PersistenceConstructor
	public TemperatureEvent(String username, DateTime timestamp, double temperature) {
		this(username, timestamp, temperature, null);
	}

	public TemperatureEvent(String username, DateTime from, DateTime to, double temperature) {
		this(null, from, to, temperature, null);
	}
	
	public TemperatureEvent(String username, DateTime timestamp, double temperature, String location) {
		this.username = username;
		this.timestamp = timestamp;
		this.temperature = temperature;
		this.setLocation(location);
	}

	public TemperatureEvent(String username, DateTime from, DateTime to, double temperature, String location) {
		this.username = username;
		this.from = from;
		this.to = to;
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
	
	public DateTime getFrom() {
		return from;
	}
	
	public void setFrom(DateTime from) {
		this.from=from;
	}
	
	public DateTime getTo() {
		return to;
	}
	
	public void setTo(DateTime to) {
		this.to=to;
	}
	

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();	
		sb.append("timestamp: "  + timestamp.toDate() + ", username: " + username + ", temperature: "  + temperature + ", location: " + location);
		return sb.toString();
	}
		
}
