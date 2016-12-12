package eu.ewall.servicebrick.dailyfunctioning.model;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.ewall.servicebrick.common.model.SensorEvent;

/**
 * 
 */
@Document(collection="pirMovementEvents")
public class PirMovementEvent extends SensorEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean movementDetected;
	
	public PirMovementEvent(DateTime timestamp, boolean movementDetected) {
		this(null,timestamp, movementDetected);
	}
	
	public PirMovementEvent(String username, DateTime timestamp, boolean movementDetected) {
		this(username, timestamp, movementDetected, null);
	}

	@PersistenceConstructor
	public PirMovementEvent(String username, DateTime timestamp, boolean movementDetected, String location) {
		this.username = username;
		this.timestamp = timestamp;
		this.movementDetected = movementDetected;
		this.location = location;
	}

	public boolean getMovementDetected() {
		return movementDetected;
	}

	public void setMovementDetected(boolean movementDetected) {
		this.movementDetected = movementDetected;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();	
		sb.append("timestamp: "  + timestamp.toDate() + ", username: " + username + ", movementDetected: "  + movementDetected + ", location: " + location);
		return sb.toString();
	}
	
}
