package eu.ewall.servicebrick.dailyfunctioning.model;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Contains a functioning activity event, i.e. a change in the activity type detected at a certain timestamp.
 */
@Document(collection="functioningActivityEvents")
public class FunctioningActivityEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// The id is using for saving the mongo _id only and not exposed externally at the moment
	@Id
	private ObjectId id;
	
	private String username;
	private DateTime timestamp;
	private String functioningActivity;
	
	public FunctioningActivityEvent(DateTime timestamp, String functioningActivity) {
		this(null, timestamp, functioningActivity);
	}
	
	@PersistenceConstructor
	public FunctioningActivityEvent(String username, DateTime timestamp, String functioningActivity) {
		this.username = username;
		this.timestamp = timestamp;
		this.functioningActivity = functioningActivity;
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

	public String getFunctioningActivity() {
		return functioningActivity;
	}

	public void setFunctioningActivity(String functioningActivity) {
		this.functioningActivity = functioningActivity;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();	
		sb.append("timestamp: "  + timestamp.toDate() + ", username: " + username + ", functioningActivity: "  + functioningActivity);
		return sb.toString();
	}
	
}
