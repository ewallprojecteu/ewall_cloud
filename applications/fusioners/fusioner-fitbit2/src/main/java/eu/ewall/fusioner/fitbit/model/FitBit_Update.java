package eu.ewall.fusioner.fitbit.model;


import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;


public class FitBit_Update extends AbstractDatabaseObject {
	@DatabaseField(value=DatabaseType.STRING)
	private String user;
	private String lastRetrieveTime;
	private String lastTrackerSyncTime;
	


	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
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
