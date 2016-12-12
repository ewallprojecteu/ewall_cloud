package eu.ewall.servicebrick.domotics.model;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.ewall.servicebrick.common.model.SensorEvent;

/**
 * 
 */
@Document(collection="humidityEventsDomotics")
public class HumidityEventDomotics extends SensorEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private double humidity;
	private DateTime from;
	private DateTime to;
	private String aggregation;
	
	public HumidityEventDomotics() {
		// TODO Auto-generated constructor stub
	}
	
	public HumidityEventDomotics(DateTime timestamp, double humidity) {
		this(null, timestamp, humidity);
	}
	
	public HumidityEventDomotics(String username, DateTime timestamp, double humidity) {
		this(null, timestamp, humidity, null);
	}
	
	public HumidityEventDomotics(String username, DateTime from, DateTime to, double humidity) {
		this(null, from, to, humidity, null, null);
	}

	@PersistenceConstructor
	public HumidityEventDomotics(String username, DateTime timestamp, double humidity, String location) {
		this.username = username;
		this.timestamp = timestamp;
		this.humidity = humidity;
		this.setLocation(location);
	}
	
	
	public HumidityEventDomotics(DateTime from, DateTime to, String location) {
		this.from = from;
		this.to = to;
		this.setLocation(location);
	}
	
	public HumidityEventDomotics(String username, DateTime from, DateTime to, double humidity, String location, String aggregation) {
		this.username = username;
		this.from = from;
		this.to = to;
		this.humidity = humidity;
		this.setLocation(location);
		this.setAggregation(aggregation);
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

	public void setAggregation(String aggregation) {
		this.aggregation=aggregation;
	}
	
	public String getAggregation() {
		return aggregation;
	}
	
	public void addHumidity(double humidity) {
		this.humidity += humidity;
		this.humidity/=2;
	}
	
	/*@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();	
		sb.append("from: "  + from.toDate() + " , to: "  + to.toDate()+", username: " + username + ", humidity: "  + humidity + ", location: " + location);
		return sb.toString();
	}*/
}
