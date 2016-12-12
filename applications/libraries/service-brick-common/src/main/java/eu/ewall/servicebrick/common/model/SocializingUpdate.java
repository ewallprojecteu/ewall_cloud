package eu.ewall.servicebrick.common.model;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="socializingEvent")
public class SocializingUpdate {
	
	@Id
	private ObjectId id;
	
	private DateTime timestamp;
	private String username;
	private String socializingEvent;
	

	public SocializingUpdate(String username, DateTime timestamp, String socializingEvent) {
		this.username=username;
		this.timestamp=timestamp;
		this.socializingEvent=socializingEvent;
		
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


	public String getSocializingEvent() {
		return socializingEvent;
	}

	public void setSocializingEvent(String socializingEvent) {
		this.socializingEvent = socializingEvent;
	}

	
}
