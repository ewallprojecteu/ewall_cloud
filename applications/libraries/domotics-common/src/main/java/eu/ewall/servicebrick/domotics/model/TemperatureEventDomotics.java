package eu.ewall.servicebrick.domotics.model;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.ewall.servicebrick.common.model.SensorEvent;

/**
 * 
 */
@Document(collection="temperatureEventsDomotics")
public class TemperatureEventDomotics extends SensorEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private double temperature;
	private DateTime from;
	private DateTime to;
	private String aggregation;
	
	
	public TemperatureEventDomotics() {
		// TODO Auto-generated constructor stub
	}
	
	
	@PersistenceConstructor
	

	public TemperatureEventDomotics(String username, DateTime from, DateTime to, double temperature) {
		this(null, from, to, temperature, null, null);
	}
	
	public TemperatureEventDomotics(DateTime from, DateTime to, String location) {
		
		this.from = from;
		this.to = to;
		this.setLocation(location);
	}
	

	public TemperatureEventDomotics(String username, DateTime from, DateTime to, double temperature, String location,String aggregation) {
		this.username = username;
		this.from = from;
		this.to = to;
		this.temperature = temperature;
		this.setLocation(location);
		this.setAggregation(aggregation);
	}
	
	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
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

	public void addTemperature(double temperature) {
		this.temperature += temperature;
		this.temperature/=2;
	}
	
	/*@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();	
		sb.append("username: " + username + ", temperature: "  + temperature + ", location: " + location);
		return sb.toString();
	}*/
		
}
