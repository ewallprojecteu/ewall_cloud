package eu.ewall.platform.idss.service.model.common.message.content;

import com.fasterxml.jackson.annotation.JsonIgnore;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;

/**
 * 
 * @author Harm op den Akker (RRD) 
 * @author Dennis Hofs (RRD)
 */
public class MessageContent extends AbstractDatabaseObject {
	@JsonIgnore
	private String id;
	
	@DatabaseField(value=DatabaseType.OBJECT)
	private GreetingContent greetingContent;
	
	@DatabaseField(value=DatabaseType.OBJECT)
	private FeedbackContent feedbackContent;
	
	@DatabaseField(value=DatabaseType.OBJECT)
	private ArgumentContent argumentContent;
	
	@DatabaseField(value=DatabaseType.OBJECT)
	private ReinforcementContent reinforcementContent;
	
	// ---------- GETTERS ---------- //
	
	@Override
	public String getId() {
		return id;
	}
	
	/**
	 * Returns the {@link GreetingContent} object associated with this {@link MessageContent}.
	 * @return the {@link GreetingContent} object associated with this {@link MessageContent}.
	 */
	public GreetingContent getGreetingContent() {
		return greetingContent;
	}
	
	/**
	 * Returns the {@link FeedbackContent} object associated with this {@link MessageContent}.
	 * @return the {@link FeedbackContent} object associated with this {@link MessageContent}.
	 */
	public FeedbackContent getFeedbackContent() {
		return feedbackContent;
	}
	
	/**
	 * Returns the {@link ArgumentContent} object associated with this {@link MessageContent}.
	 * @return the {@link ArgumentContent} object associated with this {@link MessageContent}.
	 */
	public ArgumentContent getArgumentContent() {
		return argumentContent;
	}
	
	/**
	 * Returns the {@link ReinforcementContent} object associated with this {@link MessageContent}.
	 * @return the {@link ReinforcementContent} object associated with this {@link MessageContent}.
	 */
	public ReinforcementContent getReinforcementContent() {
		return reinforcementContent;
	}
	
	// ---------- SETTERS ---------- //
	
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Sets the {@link GreetingContent} object associated with this {@link MessageContent}.
	 * @param greetingContent the {@link GreetingContent} object associated with this {@link MessageContent}.
	 */
	public void setGreetingContent(GreetingContent greetingContent) {
		this.greetingContent = greetingContent;
	}
	
	/**
	 * Sets the {@link FeedbackContent} object associated with this {@link MessageContent}.
	 * @param feedbackContent the {@link FeedbackContent} object associated with this {@link MessageContent}.
	 */
	public void setFeedbackContent(FeedbackContent feedbackContent) {
		this.feedbackContent = feedbackContent;
	}
	
	/**
	 * Sets the {@link ArgumentContent} object associated with this {@link MessageContent}.
	 * @param argumentContent the {@link ArgumentContent} object associated with this {@link MessageContent}.
	 */
	public void setArgumentContent(ArgumentContent argumentContent) {
		this.argumentContent = argumentContent;
	}
	
	/**
	 * Sets the {@link ReinforcementContent} object associated with this {@link MessageContent}.
	 * @param reinforcementContent the {@link ReinforcementContent} object associated with this {@link MessageContent}.
	 */
	public void setReinforcementContent(ReinforcementContent reinforcementContent) {
		this.reinforcementContent = reinforcementContent;
	}
	
}
