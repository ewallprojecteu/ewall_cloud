package eu.ewall.platform.notificationmanager.dao;

import eu.ewall.platform.notificationmanager.NotificationContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class CaregiverNotificationDBcontent {
	
	private String _id;
	private String caregiver;
	private String primaryUser;
	private Date dateTime;
	private String content;
	private String type;
	private String title;
	private String priority;
	private String source;
	
	
	public String get_Id() {
		return _id;
	}
	
	public String getCaregiver() {
		return caregiver;
	}
	
	public String getPrimaryUser() {
		return primaryUser;
	}
	
	public Date getDateTime() {
		return dateTime;
	}
	
	public String getContent() {
		return content;
	}
	
	public String getType() {
		return type;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getPriority() {
		return priority;
	}
	
	public String getSource() {
		return source;
	}
	
	public void set_Id(String id) {
		this._id = id;
	}
	
	public void setCaregiver(String cg) {
		this.caregiver = cg;
	}
	
	public void setPrimaryUser(String uname) {
		this.primaryUser = uname;
	}
	
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	
	public void setContent(String cont) {
		this.content = cont;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setPriority(String prior) {
		this.priority = prior;
	}
	
	public void setSource(String source) {
		this.source = source;
	}

}
