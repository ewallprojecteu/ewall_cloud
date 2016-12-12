package eu.ewall.platform.commons.datamodel.message;

import java.util.ArrayList;
import java.util.List;

public class NotificationContentMsg {
	
	private String type;
	private String subtype;
	private String title;
	private String motivational;
	private String suggestion;
	private List<NotificationStatements> statements =
			new ArrayList<NotificationStatements>();
	private List<NotificationContentFeedbackMsg> feedback =
			new ArrayList<NotificationContentFeedbackMsg>();
	
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

	public List<NotificationContentFeedbackMsg> getFeedback() {
		return feedback;
	}

	public void setFeedback(List<NotificationContentFeedbackMsg> feedback) {
		this.feedback = feedback;
	}
	
	public void addFeedback(NotificationContentFeedbackMsg feedback) {
		this.feedback.add(feedback);
	}
	
	public List<NotificationStatements> getStatements() {
		return statements;
	}
	
	public void setStatements(List<NotificationStatements> statements) {
		this.statements = statements;
	}
	
	public void addStatements(NotificationStatements statements) {
		this.statements.add(statements);
	}

}
