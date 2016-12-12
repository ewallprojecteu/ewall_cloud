package eu.ewall.servicebrick.domotics.model;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="sensorsUpdate")
public class DomoticsEvent {
	
	@Id
	private ObjectId id;
	
	private DateTime timestamp;
	private String username;
	private String location;
	private double humidity;
	private double temperature;
	private double illuminance;
//	private boolean mattress;
//	private boolean pirMovement;
//	private boolean door;
//	private double gasCO;
	
	public DomoticsEvent(){}

	public DateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getHumidity() {
		return humidity;
	}

	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public double getIlluminance() {
		return illuminance;
	}

	public void setIlluminance(double illuminance) {
		this.illuminance = illuminance;
	}

/*	public boolean isMattress() {
		return mattress;
	}

	public void setMattress(boolean mattress) {
		this.mattress = mattress;
	}

	public boolean isPirMovement() {
		return pirMovement;
	}

	public void setPirMovement(boolean pirMovement) {
		this.pirMovement = pirMovement;
	}

	public boolean isDoor() {
		return door;
	}

	public void setDoor(boolean door) {
		this.door = door;
	}

	public double getGasCO() {
		return gasCO;
	}

	public void setGasCO(double gasCO) {
		this.gasCO = gasCO;
	}*/
}
