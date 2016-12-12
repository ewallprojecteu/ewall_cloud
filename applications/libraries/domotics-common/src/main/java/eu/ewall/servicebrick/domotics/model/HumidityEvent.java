package eu.ewall.servicebrick.domotics.model;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.ewall.servicebrick.common.model.SensorEvent;

/**
 * 
 */
@Document(collection="humidityEvents")
public class HumidityEvent extends SensorEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private double humidity;
	private DateTime from;
	private DateTime to;
	
	public HumidityEvent() {
		// TODO Auto-generated constructor stub
	}
	
	public HumidityEvent(DateTime timestamp, double humidity) {
		this(null, timestamp, humidity);
	}
	
	public HumidityEvent(String username, DateTime timestamp, double humidity) {
		this(null, timestamp, humidity, null);
	}
	
	public HumidityEvent(String username, DateTime from, DateTime to, double humidity) {
		this(null, from, to, humidity, null);
	}

	@PersistenceConstructor
	public HumidityEvent(String username, DateTime timestamp, double humidity, String location) {
		this.username = username;
		this.timestamp = timestamp;
		this.humidity = humidity;
		this.setLocation(location);
	}
	
	public HumidityEvent(String username, DateTime from, DateTime to, double humidity, String location) {
		this.username = username;
		this.from = from;
		this.to = to;
		this.humidity = humidity;
		this.setLocation(location);
	}

	public double getHumidity() {
		return humidity;
	}

	public void setHumidity(double humidity) {
		this.humidity = humidity;
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
		sb.append("timestamp: "  + timestamp.toDate() + ", username: " + username + ", humidity: "  + humidity + ", location: " + location);
		return sb.toString();
	}
}
