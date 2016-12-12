package eu.ewall.servicebrick.domotics.model;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.ewall.servicebrick.common.model.SensorEvent;

/**
 * 
 */
@Document(collection="illuminanceEventsDomotics")
public class IlluminanceEventDomotics extends SensorEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	private double illuminance;
	private DateTime from;
	private DateTime to;
	private String aggregation;

	
	public IlluminanceEventDomotics() {
		// TODO Auto-generated constructor stub
	}

	
	
	
	public IlluminanceEventDomotics(String username, DateTime from, DateTime to, double illuminance) {
		this(null, from, to, illuminance, null, null);
	}
	
	@PersistenceConstructor
	

	public IlluminanceEventDomotics(DateTime from, DateTime to, String location) {
		
		this.from = from;
		this.to = to;
		this.setLocation(location);
	}

	public IlluminanceEventDomotics(String username, DateTime from, DateTime to, double illuminance, String location, String aggregation) {
		this.username = username;
		this.from = from;
		this.to = to;
		this.illuminance = illuminance;
		this.setLocation(location);
		this.aggregation = aggregation;
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
	
	
	
	public void setAggregation(String aggregation) {
		this.aggregation=aggregation;
	}
	
	public String getAggregation() {
		return aggregation;
	}
	
	public void addIlluminance(double illuminance) {
		this.illuminance += illuminance;
		this.illuminance/=2;
	}
	
 /*	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();	
		sb.append("username: " + username + ", illuminance: "  + illuminance + ", location: " + location);
		return sb.toString();
	}*/
}
