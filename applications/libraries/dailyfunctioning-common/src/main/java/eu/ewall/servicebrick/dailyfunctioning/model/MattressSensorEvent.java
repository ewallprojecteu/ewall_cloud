package eu.ewall.servicebrick.dailyfunctioning.model;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.ewall.servicebrick.common.model.SensorEvent;

/**
 * 
 */
@Document(collection="mattressSensorEvents")
public class MattressSensorEvent extends SensorEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private double imaValue;
	private boolean pressure;
	
	public MattressSensorEvent(DateTime timestamp, boolean pressure, double imaValue) {
		this(null,timestamp, pressure, imaValue);
	}
	
	public MattressSensorEvent(String username, DateTime timestamp, boolean pressure, double imaValue) {
		this(username, timestamp, pressure, imaValue, null);
	}

	@PersistenceConstructor
	public MattressSensorEvent(String username, DateTime timestamp, boolean pressure, double imaValue, String location) {
		this.username = username;
		this.timestamp = timestamp;
		this.pressure = pressure;
		this.imaValue = imaValue;
		this.location = location;
	}

	public double getImaValue() {
		return imaValue;
	}

	public void setImaValue(double imaValue) {
		this.imaValue = imaValue;
	}

	public boolean getPressure() {
		return pressure;
	}

	public void setPressure(boolean pressure) {
		this.pressure = pressure;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();	
		sb.append("timestamp: "  + timestamp.toDate() + ", username: " + username + ", pressure: "  + pressure + ", imaValue: "  + imaValue + ", location: " + location);
		return sb.toString();
	}
	
}
