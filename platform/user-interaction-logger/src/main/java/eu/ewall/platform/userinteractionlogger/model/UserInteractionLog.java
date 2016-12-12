package eu.ewall.platform.userinteractionlogger.model;

import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.utils.json.DateTimeFromIsoDateTimeDeserializer;
import eu.ewall.platform.idss.utils.json.IsoDateTimeSerializer;

public class UserInteractionLog extends AbstractDatabaseObject {
	@DatabaseField(value=DatabaseType.STRING)
	private String userid;
	
	@DatabaseField(value=DatabaseType.STRING)
	private String applicationName;
	
	@DatabaseField(value=DatabaseType.STRING)
	private String buttonId;
	
	@DatabaseField(value=DatabaseType.TEXT)
	private String comment;
	
	@DatabaseField(value=DatabaseType.LONG, index=true)
	private long utcTime;
	
	@DatabaseField(value=DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime tzTime;
	
	@DatabaseField(value=DatabaseType.STRING, index=true)
	private String dateString;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getButtonId() {
		return buttonId;
	}

	public void setButtonId(String buttonId) {
		this.buttonId = buttonId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public long getUtcTime() {
		return utcTime;
	}

	public void setUtcTime(long utcTime) {
		this.utcTime = utcTime;
	}

	public DateTime getTzTime() {
		return tzTime;
	}

	public void setTzTime(DateTime tzTime) {
		this.tzTime = tzTime;
	}
	
	public String getDateString() {
		return dateString;
	}
	
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
}
