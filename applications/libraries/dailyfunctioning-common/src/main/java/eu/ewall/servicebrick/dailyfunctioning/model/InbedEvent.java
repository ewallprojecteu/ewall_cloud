package eu.ewall.servicebrick.dailyfunctioning.model;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Contains an inactivity event, i.e. a change in the inactivity status detected at a certain timestamp.
 */
@Document(collection="inbedEvents")
public class InbedEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// The id is using for saving the mongo _id only and not exposed externally at the moment
	@Id
	private ObjectId id;
	
	private String username;
	private DateTime timestamp;
	private boolean inbed;
	
	public InbedEvent(DateTime timestamp, boolean inbed) {
		this(null, timestamp, inbed);
	}
	
	@PersistenceConstructor
	public InbedEvent(String username, DateTime timestamp, boolean inbed) {
		this.username = username;
		this.timestamp = timestamp;
		this.inbed = inbed;
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

	public boolean isInbed() {
		return inbed;
	}

	public void setInbed(boolean inbed) {
		this.inbed = inbed;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();	
		sb.append("timestamp: "  + timestamp.toDate() + ", username: " + username + ", inbed: "  + inbed);
		return sb.toString();
	}
	
}
