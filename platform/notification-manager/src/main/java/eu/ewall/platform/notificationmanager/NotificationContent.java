

/****************************************************************
 * Copyright 2014 Ss Cyril and Methodius University in Skopje
***************************************************************/


package eu.ewall.platform.notificationmanager;

import java.util.ArrayList;
import java.util.List;

public class NotificationContent {
	private String type;
	private String subtype;
	private String title;
	private String motivational;
	private String suggestion;
	private List<Statements> statements =
			new ArrayList<Statements>();
	private List<NotificationContentFeedback> feedback =
			new ArrayList<NotificationContentFeedback>();
	
	public String getType() {
		return type;
	}
	
	public String getSubtype() {
		return subtype;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public void setSubtype(String stype) {
		this.subtype = stype;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMotivational() {
		return motivational;
	}

	public void setMotivational(String motivational) {
		this.motivational = motivational;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public List<NotificationContentFeedback> getFeedback() {
		return feedback;
	}

	public void setFeedback(List<NotificationContentFeedback> feedback) {
		this.feedback = feedback;
	}
	
	public void addFeedback(NotificationContentFeedback feedback) {
		this.feedback.add(feedback);
	}
	
	public List<Statements> getStatements() {
		return statements;
	}
	
	public void setStatements(List<Statements> statements) {
		this.statements = statements;
	}
	
	public void addStatements(Statements statements) {
		this.statements.add(statements);
	}
}
