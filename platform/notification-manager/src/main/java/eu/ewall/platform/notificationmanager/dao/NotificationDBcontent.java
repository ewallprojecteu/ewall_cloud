package eu.ewall.platform.notificationmanager.dao;

import eu.ewall.platform.commons.datamodel.message.NotificationContentMsg;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class NotificationDBcontent extends NotificationContentMsg {
	
	private String primaryUser;
	
	private String _id;
	
	private String response;
	
	private Date dateTime;
	
	private String source;
	
	public String getPrimaryUser() {
		return primaryUser;
	}
	
	public String get_Id() {
		return _id;
	}
	
	public String getResponse() {
		return response;
	}
	
	public String getSource() {
		return source;
	}
	
	public Date getDateTime() {
		return dateTime;
	}
	
	public void setPrimaryUser(String uname) {
		this.primaryUser = uname;
	}
	
	public void set_Id(String id) {
		this._id = id;
	}
	
	public void setResponse(String response) {
		this.response = response;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

}
