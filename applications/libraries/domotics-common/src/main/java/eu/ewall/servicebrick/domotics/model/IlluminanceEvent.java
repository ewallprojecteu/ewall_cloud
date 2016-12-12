package eu.ewall.servicebrick.domotics.model;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.ewall.servicebrick.common.model.SensorEvent;

/**
 * 
 */
@Document(collection="illuminanceEvents")
public class IlluminanceEvent extends SensorEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private double illuminance;
	private DateTime from;
	private DateTime to;
	
	public IlluminanceEvent() {
		// TODO Auto-generated constructor stub
	}

	public IlluminanceEvent(DateTime timestamp, double illuminance) {
		this(null, timestamp, illuminance);
	}
	
	public IlluminanceEvent(String username, DateTime timestamp, double illuminance) {
		this(null, timestamp, illuminance, null);
	}
	
	public IlluminanceEvent(String username, DateTime from, DateTime to, double illuminance) {
		this(null, from, to, illuminance, null);
	}

	public IlluminanceEvent(String username, DateTime timestamp, double illuminance, String location) {
		this.username = username;
		this.timestamp = timestamp;
		this.illuminance = illuminance;
		this.setLocation(location);
	}

	public IlluminanceEvent(String username, DateTime from, DateTime to, double illuminance, String location) {
		this.username = username;
		this.from = from;
		this.to = to;
		this.illuminance = illuminance;
		this.setLocation(location);
	}
	
	public double getIlluminance() {
		return illuminance;
	}

	public void setIlluminance(double illuminance) {
		this.illuminance = illuminance;
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
		sb.append("timestamp: "  + timestamp.toDate() + ", username: " + username + ", illuminance: "  + illuminance + ", location: " + location);
		return sb.toString();
	}
}
