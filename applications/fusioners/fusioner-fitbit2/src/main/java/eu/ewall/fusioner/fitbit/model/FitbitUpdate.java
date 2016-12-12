package eu.ewall.fusioner.fitbit.model;

import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.utils.json.DateTimeFromIsoDateTimeDeserializer;
import eu.ewall.platform.idss.utils.json.IsoDateTimeSerializer;

public class FitbitUpdate extends AbstractDatabaseObject {
	@DatabaseField(value=DatabaseType.STRING)
	private String user;
	
	@DatabaseField(value=DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime lastRetrieveTime;
	
	@DatabaseField(value=DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime lastTrackerSyncTime;

	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
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
}
