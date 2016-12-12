package eu.ewall.servicebrick.common.model;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="accelerometerUpdates")
public class AccelerometerUpdate {
	
	@Id
	private ObjectId id;

	private String username;
	private DateTime lastAccelerometerUpdate;
	private long lastStepsValue;
	private String lastActivityType;
	private boolean lastInactivityState;
	

	public AccelerometerUpdate(String username, DateTime lastAccelerometerUpdate) {
		this.username=username;
		this.lastAccelerometerUpdate=lastAccelerometerUpdate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public DateTime getLastAccelerometerUpdate() {
		return lastAccelerometerUpdate;
	}

	public void setLastAccelerometerUpdate(DateTime lastAccelerometerUpdate) {
		this.lastAccelerometerUpdate = lastAccelerometerUpdate;
	}

	public String getLastActivityType() {
		return lastActivityType;
	}

	public void setLastActivityType(String lastActivityType) {
		this.lastActivityType = lastActivityType;
	}

	public boolean getLastInactivityState() {
		return lastInactivityState;
	}

	public void setLastInactivityState(boolean lastInactivityState) {
		this.lastInactivityState = lastInactivityState;
	}

	public long getLastStepsValue() {
		return lastStepsValue;
	}

	public void setLastStepsValue(long lastStepsValue) {
		this.lastStepsValue = lastStepsValue;
	}

	
}
