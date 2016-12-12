package eu.ewall.servicebrick.dailyfunctioning.model;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.ewall.servicebrick.common.model.SensorEvent;

/**
 * 
 */
@Document(collection="doorEvents")
public class DoorEvent extends SensorEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean doorStatus;
	
	public DoorEvent() {
		// TODO Auto-generated constructor stub
	}

	public DoorEvent(DateTime timestamp, boolean doorStatus) {
		this(null,timestamp, doorStatus);
	}
	
	public DoorEvent(String username, DateTime timestamp, boolean doorStatus) {
		this(username, timestamp, doorStatus, null);
	}

	public DoorEvent(String username, DateTime timestamp, boolean doorStatus, String location) {
		this.username = username;
		this.timestamp = timestamp;
		this.doorStatus = doorStatus;
		this.setLocation(location);
	}

	public boolean getDoorStatus() {
		return doorStatus;
	}

	public void setDoorStatus(boolean doorStatus) {
		this.doorStatus = doorStatus;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();	
		sb.append("timestamp: "  + timestamp.toDate() + ", username: " + username + ", doorStatus: "  + doorStatus + ", location: " + location);
		return sb.toString();
	}
	
}
