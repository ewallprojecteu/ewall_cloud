package eu.ewall.servicebrick.dailyfunctioning.model;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Contains an inactivity event, i.e. a change in the inactivity status detected at a certain timestamp.
 */
@Document(collection="locationEvents")
public class LocationEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String username;
	private DateTime timestamp;
	private String location;
	
	public LocationEvent(DateTime timestamp, String location) {
		this(null, timestamp, location);
	}
	
	@PersistenceConstructor
	public LocationEvent(String username, DateTime timestamp, String location) {
		this.username = username;
		this.timestamp = timestamp;
		this.location = location;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public DateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getLocation() {
		return location;
	}

	public void setlocation(String location) {
		this.location = location;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();	
		sb.append("timestamp: "  + timestamp.toDate() + ", username: " + username + ", location: "  + location + ", location: " + location);
		return sb.toString();
	}
	
}
