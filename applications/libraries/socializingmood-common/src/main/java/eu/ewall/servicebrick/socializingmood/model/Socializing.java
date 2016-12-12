package eu.ewall.servicebrick.socializingmood.model;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * Contains the socializing data  on the given time interval.
 * 
 */
@Document(collection="socializingEvents")
public class Socializing implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// The id is used for saving the mongo _id only and not exposed externally at the moment
	@Id
	private ObjectId id;
	
	private String username;
	DateTime timestamp;
	private String socializingEvent;
	
	
		
	@PersistenceConstructor
	public Socializing(String username, DateTime timestamp, String socializingEvent ) {
		this.username=username;
		this.timestamp=timestamp;
		this.socializingEvent = socializingEvent;

	
	}
	

	// Don't include in output as the username is already stated in MovementHistory
	@JsonIgnore
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

		
	public String getSocializingEvent() {
		return socializingEvent;
	}

	public void setSocializingEvent(String socializingEvent) {
		this.socializingEvent = socializingEvent;
	}

	



	
}
