package eu.ewall.servicebrick.common.model;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;

/**
 * 
 */
public abstract class SensorEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// The id is using for saving the mongo _id only and not exposed externally at the moment
	@Id
	private ObjectId id;

	public static final String UNDEFINED = "undefined";

	protected String username;
	protected DateTime timestamp;
	protected String location = UNDEFINED;
	
	
	
	public SensorEvent() {
		// TODO Auto-generated constructor stub
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


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		if(location == null || location.isEmpty()) {
			location = UNDEFINED;
		}
		this.location = location;
	}
	


}
