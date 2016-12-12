package eu.ewall.servicebrick.socializingmood.model;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import eu.ewall.platform.commons.datamodel.profile.EmotionalStateCategory;

/**
 * Contains a Visual Sensing event
 */

@Document(collection="moodEvents")
public class VisualSensingEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// The id is using for saving the mongo _id only and not exposed externally at the moment
	@Id
	private ObjectId id;
	

	
	private String username;
	private DateTime timestamp;
	private int track_id;
	private Double emotionConf;
	private EmotionalStateCategory emotion = EmotionalStateCategory.NEUTRAL;
	

	
	public VisualSensingEvent(DateTime timestamp, int track_id, EmotionalStateCategory emotion, Double emotionConf) {
		this(null, timestamp, track_id, emotion, emotionConf);
	}
	
	
	@PersistenceConstructor
	public VisualSensingEvent(String username, DateTime timestamp, int track_id, EmotionalStateCategory emotion, Double emotionConf) {
		this.username = username;
		this.timestamp = timestamp;
		if(emotion==null){
			emotion = EmotionalStateCategory.NEUTRAL;
		}
		this.track_id=track_id;
		this.emotion = emotion;
		this.emotionConf = emotionConf;
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

	public int getTrack_id() {
		return track_id;
	}
	
	public void setTrack_id(int track_id) {
		this.track_id = track_id;
	}
	
	public  EmotionalStateCategory getEmotion() {
		return emotion;
	}

	public void setEmotion(EmotionalStateCategory emotion) {
		this.emotion = emotion;
	}
	
	
	public double getEmotionConf() {
		return  emotionConf;
	}

	public void setEmotionConf(double emotionConf) {
		this.emotionConf = emotionConf;
	}
	
		
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();	
		sb.append("timestamp: "  + timestamp.toDate() + ", track_id: " + track_id +", username: " + username + ", emotion: "  + emotion + ", emotionConf: "  + emotionConf);
		return sb.toString();
	}
	
}
