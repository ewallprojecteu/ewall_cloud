package eu.ewall.servicebrick.socializingmood.model;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import eu.ewall.platform.commons.datamodel.activity.ActivityType;
import eu.ewall.platform.commons.datamodel.measure.ConstantQuantityMeasureType;

/**
 * Contains the mood data (valence and arousal) on the given time interval.
 * 
 */
@Document(collection="mood")
public class Mood implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// The id is used for saving the mongo _id only and not exposed externally at the moment
	@Id
	private ObjectId id;
	
	private String username;
	DateTime timestamp;
	private double valence;
	private double arousal;
	
	
		
	@PersistenceConstructor
	public Mood(String username, double valence, double arousal, DateTime timestamp) {
		this.username=username;
		this.valence=valence;
		this.arousal=arousal;
		this.timestamp=timestamp;
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

		
	public double getValence() {
		return valence;
	}

	public void setValence(double valence) {
		this.valence = valence;
	}

	
	public double getArousal() {
		return arousal;
	}

	public void setArousal(double arousal) {
		this.arousal = arousal;
	}
	



	
}
