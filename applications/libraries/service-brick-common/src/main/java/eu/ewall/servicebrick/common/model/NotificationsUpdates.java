package eu.ewall.servicebrick.common.model;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="notificationsUpdates")
public class NotificationsUpdates {
	
	@Id
	private ObjectId id;

	private String username;
	
	
	private DateTime lastUpdateTimestamp;
	
	public NotificationsUpdates() {
		// TODO Auto-generated constructor stub
	}
		
	public NotificationsUpdates(String username, DateTime lastGeneralUpdate) {
		this.username = username;
		this.lastUpdateTimestamp = lastGeneralUpdate;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}


	public DateTime getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}


	public void setLastUpdateTimestamp(DateTime lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}

	
}
