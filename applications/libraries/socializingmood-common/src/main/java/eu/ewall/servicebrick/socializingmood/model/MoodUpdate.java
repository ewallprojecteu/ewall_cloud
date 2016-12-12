package eu.ewall.servicebrick.socializingmood.model;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="mood")
public class MoodUpdate {
	
	@Id
	private ObjectId id;
	
	private DateTime timestamp;
	private String username;
	private double valence;
	private double arousal;
	

	public MoodUpdate(String username, DateTime timestamp) {
		this.username=username;
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



	public double getValence() {
		return valence;
	}

	public void setValence(double valence) {
		this.valence = valence;
	}

	public double getArousal() {
		return arousal;
	}

	public void setArousal(double vrousal) {
		this.arousal = vrousal;
	}
	
}
