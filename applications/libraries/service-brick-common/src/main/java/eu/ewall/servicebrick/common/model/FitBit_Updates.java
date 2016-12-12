package eu.ewall.servicebrick.common.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="fitbit_updates")
public class FitBit_Updates {
	
	@Id
	private ObjectId id;

	private String username;
	private String lastRetrieveTime;
	private String lastTrackerSyncTime;



	public FitBit_Updates(String username, String lastRetrieveTime) {
		this.username=username;
		this.lastRetrieveTime=lastRetrieveTime;
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLastRetrieveTime() {
		return lastRetrieveTime;
	}

	public void setLastRetrieveTime(String lastRetrieveTime) {
		this.lastRetrieveTime = lastRetrieveTime;
	}

	
	public String getLastTrackerSyncTime() {
		return lastTrackerSyncTime;
	}

	public void setLastTrackerSyncTime(String lastTrackerSyncTime) {
		this.lastTrackerSyncTime = lastTrackerSyncTime;
	}
	
}
