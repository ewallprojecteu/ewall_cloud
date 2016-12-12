package eu.ewall.servicebrick.common.model;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="fitBitActivityUpdates")
public class FitBitActivityUpdate {
	
	@Id
	private ObjectId id;

	private String username;
	private DateTime lastRetrieveTime;
	private DateTime lastTrackerSyncTime;
	private int lastSteps;
	private int lastCalories;
	private double lastDistance;


	public FitBitActivityUpdate(String username, DateTime lastTrackerSyncTime) {
		this.username=username;
		this.lastTrackerSyncTime=lastTrackerSyncTime;
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public DateTime getLastRetrieveTime() {
		return lastRetrieveTime;
	}

	public void setLastRetrieveTime(DateTime lastRetrieveTime) {
		this.lastRetrieveTime = lastRetrieveTime;
	}

	
	public DateTime getLastTrackerSyncTime() {
		return lastTrackerSyncTime;
	}

	public void setLastTrackerSyncTime(DateTime lastTrackerSyncTime) {
		this.lastTrackerSyncTime = lastTrackerSyncTime;
	}
	
	public int getLastSteps() {
		return lastSteps;
	}

	public void setLastSteps(int lastSteps) {
		this.lastSteps = lastSteps;
	}
	
	public int getLastCalories() {
		return lastCalories;
	}
	
	public void setLastCalories(int lastCalories) {
		this.lastCalories = lastCalories;
	}
	
	public void setLastDistance(double lastDistance) {
		this.lastDistance = lastDistance;
	}
	
	
	public double getLastDistance() {
		return lastDistance;
	}

}
