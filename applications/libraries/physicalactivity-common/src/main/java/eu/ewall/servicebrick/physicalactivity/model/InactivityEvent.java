package eu.ewall.servicebrick.physicalactivity.model;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Contains an inactivity event, i.e. a change in the inactivity status detected at a certain timestamp.
 */
@Document(collection="inactivityEvents")
public class InactivityEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// The id is using for saving the mongo _id only and not exposed externally at the moment
	@Id
	private ObjectId id;
	
	private String username;
	private DateTime timestamp;
	private boolean inactive;
	
	public InactivityEvent(DateTime timestamp, boolean inactive) {
		this(null, timestamp, inactive);
	}
	
	@PersistenceConstructor
	public InactivityEvent(String username, DateTime timestamp, boolean inactive) {
		this.username = username;
		this.timestamp = timestamp;
		this.inactive = inactive;
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

	public boolean isInactive() {
		return inactive;
	}

	public void setInactive(boolean inactive) {
		this.inactive = inactive;
	}
	
}
