package eu.ewall.servicebrick.physicalactivity.model;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.ewall.platform.commons.datamodel.activity.ActivityType;

/**
 * Contains a physical activity event, i.e. a change in the activity type detected at a certain timestamp.
 */
@Document(collection="physicalActivityEvents")
public class PhysicalActivityEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// The id is using for saving the mongo _id only and not exposed externally at the moment
	@Id
	private ObjectId id;
	
	private String username;
	private DateTime timestamp;
	private ActivityType activityType = ActivityType.UNKNOWN;
	
	public PhysicalActivityEvent(DateTime timestamp, ActivityType activityType) {
		this(null, timestamp, activityType);
	}
	
	@PersistenceConstructor
	public PhysicalActivityEvent(String username, DateTime timestamp, ActivityType activityType) {
		this.username = username;
		this.timestamp = timestamp;
		if(activityType==null){
			activityType = ActivityType.UNKNOWN;
		}
		this.activityType = activityType;
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

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}
	
}
