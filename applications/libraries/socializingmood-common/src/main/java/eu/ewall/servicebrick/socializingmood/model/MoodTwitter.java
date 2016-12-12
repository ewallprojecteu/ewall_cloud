package eu.ewall.servicebrick.socializingmood.model;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Contains the mood data (valence and arousal) on the given time interval.
 * 
 */
@Document(collection="moodTwitter")
public class MoodTwitter implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// The id is used for saving the mongo _id only and not exposed externally at the moment
	@Id
	private ObjectId id;
	
	private String username;
	private DateTime timestamp;
	private String socializing;
	
	
		
	@PersistenceConstructor
	public MoodTwitter(String username, String socializing, DateTime timestamp) {
		this.username=username;
		this.socializing=socializing;
		this.timestamp=timestamp;
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


	public String getSocializing() {
		return socializing;
	}


	public void setSocializing(String socializing) {
		this.socializing = socializing;
	}
	
	public String toString() {
		return username+" "+timestamp+" "+socializing;
	}
}
