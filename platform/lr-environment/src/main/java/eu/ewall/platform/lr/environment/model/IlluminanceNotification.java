package eu.ewall.platform.lr.environment.model;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.utils.json.SqlDateDeserializer;
import eu.ewall.platform.idss.utils.json.SqlDateSerializer;

public class IlluminanceNotification extends AbstractDatabaseObject {
	
	@DatabaseField(value=DatabaseType.STRING)
	private String username;
	
	@DatabaseField(value=DatabaseType.DATE)
	@JsonSerialize(using=SqlDateSerializer.class)
	@JsonDeserialize(using=SqlDateDeserializer.class)
	private LocalDate sendDate1;
	
	@DatabaseField(value=DatabaseType.DATE)
	@JsonSerialize(using=SqlDateSerializer.class)
	@JsonDeserialize(using=SqlDateDeserializer.class)
	private LocalDate sendDate2;
	
	@DatabaseField(value=DatabaseType.STRING)
	private String notificationMessage;
	
	@DatabaseField(value=DatabaseType.STRING)
	private String notificationType;
	
	@DatabaseField(value=DatabaseType.INT)
	private int timesSent;
	
	public IlluminanceNotification() {
		timesSent = 0;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public LocalDate getSendDate1() {
		return sendDate1;
	}
	public void setSendDate1(LocalDate sendDate1) {
		this.sendDate1 = sendDate1;
	}
	public String getNotificationMessage() {
		return notificationMessage;
	}
	public void setNotificationMessage(String notificationMessage) {
		this.notificationMessage = notificationMessage;
	}
	public String getNotificationType() {
		return notificationType;
	}
	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}
	public int getTimesSent() {
		return timesSent;
	}
	public void setTimesSent(int timesSent) {
		this.timesSent = timesSent;
	}

	public LocalDate getSendDate2() {
		return sendDate2;
	}

	public void setSendDate2(LocalDate sendDate2) {
		this.sendDate2 = sendDate2;
	}
	
	
}
