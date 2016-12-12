package eu.ewall.servicebrick.common.model;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import eu.ewall.platform.commons.datamodel.profile.EmotionalStateCategory;

@Document(collection="VisualSensingUpdates")
public class EmotionUpdate {
	
	@Id
	private ObjectId id;

	private String username;
	private DateTime lastMoodUpdate;
	private int track_id;
	private EmotionalStateCategory lastEmotion;
	private double lastEmotionConf;
	private double lastValence;
	private double lastArousal;
	

	public EmotionUpdate(String username, DateTime lastMoodUpdate) {
		this.username=username;
		this.lastMoodUpdate=lastMoodUpdate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public DateTime getLastMoodUpdate() {
		return lastMoodUpdate;
	}

	public void setLastMoodUpdate(DateTime lastMoodUpdate) {
		this.lastMoodUpdate = lastMoodUpdate;
	}

	public int getLastTrack_id() {
		return track_id;
	}
	
	public void setLastTrack_id(int track_id) {
		this.track_id = track_id;
	}
	
	public EmotionalStateCategory getLastEmotion() {
		return lastEmotion;
	}

	public void setLastEmotion(EmotionalStateCategory lastEmotion) {
		this.lastEmotion = lastEmotion;
	}
	
	public double getLastEmotionConf() {
		return lastEmotionConf;
	}
	
	public void setLastEmotionConf(double lastEmotionConf) {
		this.lastEmotionConf = lastEmotionConf;
	}
	

	public double getLastValence() {
		return lastValence;
	}

	public void setLastValence(double lastValence) {
		this.lastValence = lastValence;
	}

	public double getLastArousal() {
		return lastArousal;
	}

	public void setLastArousal(double lastArousal) {
		this.lastArousal = lastArousal;
	}
	
}
