package eu.ewall.platform.lr.habits.model;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import eu.ewall.platform.idss.utils.json.DateTimeFromIsoDateTimeDeserializer;

public class FunctioningActivityEvent {
	@Id
	private ObjectId id;
	
	private String username;
	
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime timestamp;
	
	private String functioningActivity;
	
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
	
}
