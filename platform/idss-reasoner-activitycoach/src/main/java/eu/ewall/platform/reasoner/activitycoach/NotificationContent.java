package eu.ewall.platform.reasoner.activitycoach;

import java.util.ArrayList;
import java.util.List;

public class NotificationContent {
	private String type;
	private String subtype;
	private String title;
	private String stepId = null;
	private List<NotificationContentStatement> statements =
			new ArrayList<NotificationContentStatement>();
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

	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

	public List<NotificationContentStatement> getStatements() {
		return statements;
	}

	public void setStatements(List<NotificationContentStatement> statements) {
		this.statements = statements;
	}
	
	public void addStatement(String type, String text) {
		this.statements.add(new NotificationContentStatement(type, text));
	}
	
	public void addStatement(NotificationContentStatement statement) {
		this.statements.add(statement);
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
}
