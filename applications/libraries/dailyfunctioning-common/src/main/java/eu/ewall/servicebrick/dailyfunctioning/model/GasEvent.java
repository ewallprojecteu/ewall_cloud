package eu.ewall.servicebrick.dailyfunctioning.model;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.springframework.data.annotation.PersistenceConstructor;

import eu.ewall.servicebrick.common.model.SensorEvent;

public abstract class GasEvent extends SensorEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private double gasValue;
	
	public GasEvent() {
	}
	
	public GasEvent(DateTime timestamp, double gasValue) {
		this(null, timestamp, gasValue);
	}
	
	public GasEvent(String username, DateTime timestamp, double gasValue) {
		this(username, timestamp, gasValue, null);
	}

	@PersistenceConstructor
	public GasEvent(String username, DateTime timestamp, double gasValue, String location) {
		this.username = username;
		this.timestamp = timestamp;
		this.gasValue = gasValue;
		this.setLocation(location);
	}

	public double getGasValue() {
		return gasValue;
	}

	public void setGasValue(double gasValue) {
		this.gasValue = gasValue;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();	
		sb.append("timestamp: "  + timestamp.toDate() + ", username: " + username + ", " + this.getClass().getCanonicalName() + ":"  + gasValue + ", location: " + location);
		return sb.toString();
	}
}
