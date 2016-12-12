package eu.ewall.servicebrick.dailyfunctioning.model;

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
	
	public IlluminanceEvent() {
		// TODO Auto-generated constructor stub
	}

	public IlluminanceEvent(DateTime timestamp, double illuminance) {
		this(null, timestamp, illuminance);
	}
	
	public IlluminanceEvent(String username, DateTime timestamp, double illuminance) {
		this(null, timestamp, illuminance, null);
	}

	public IlluminanceEvent(String username, DateTime timestamp, double illuminance, String location) {
		this.username = username;
		this.timestamp = timestamp;
		this.illuminance = illuminance;
		this.setLocation(location);
	}

	public double getIlluminance() {
		return illuminance;
	}

	public void setIlluminance(double illuminance) {
		this.illuminance = illuminance;
	}

	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();	
		sb.append("timestamp: "  + timestamp.toDate() + ", username: " + username + ", illuminance: "  + illuminance + ", location: " + location);
		return sb.toString();
	}
}
