package eu.ewall.platform.idss.service.model.common.dialogue;

import java.util.List;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;

/**
 * A {@link DialogueReply} is an abstract option for a reply to a {@link DialogueStep} for the user.
 * Current implementation of the {@link DialogueReply} are:
 * <ul>
 *   <li>{@link DialogueBasicReply}</li>
 * </ul>
 * 
 * @author Harm op den Akker (Roessingh Research and Development)
 */
public abstract class DialogueReply extends AbstractDatabaseObject {
	
	@DatabaseField(value=DatabaseType.STRING)
	private String replyId;
	
	@DatabaseField(value=DatabaseType.OBJECT_LIST)
	private List<DialogueAction> actions;
	
	@DatabaseField(value=DatabaseType.OBJECT_LIST)
	private List<DialogueCondition> conditions;
	
	// ---------- Getters ---------- //
	
	/**
	 * Returns the unique reply identifier of this {@link DialogueReply}.
	 * @return the unique reply identifier of this {@link DialogueReply}.
	 */
	public String getReplyId() {
		return replyId;
	}
	
	/**
	 * Returns the list of {@link DialogueAction}s associated with this {@link DialogueReply}.
	 * @return the list of {@link DialogueAction}s associated with this {@link DialogueReply}.
	 */
	public List<DialogueAction> getActions() {
		return actions;
	}
	
	/**
	 * Returns the list of {@link DialogueCondition}s associated with this {@link DialogueReply}.
	 * @return the list of {@link DialogueCondition}s associated with this {@link DialogueReply}.
	 */
	public List<DialogueCondition> getConditions() {
		return conditions;
	}
	
	// ---------- Setters ---------- //
	
	/**
	 * Sets the unique reply identifier of this {@link DialogueReply}.
	 * @param replyId the unique reply identifier of this {@link DialogueReply}.
	 */
	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}
	
	/**
	 * Sets the list of {@link DialogueAction}s associated with this {@link DialogueReply}.
	 * @param actions the list of {@link DialogueAction}s associated with this {@link DialogueReply}.
	 */
	public void setActions(List<DialogueAction> actions) {
		this.actions = actions;
	}
	
	/**
	 * Sets the list of {@link DialogueCondition}s associated with this {@link DialogueReply}.
	 * @param actions the list of {@link DialogueCondition}s associated with this {@link DialogueReply}.
	 */
	public void setConditions(List<DialogueCondition> conditions) {
		this.conditions = conditions;
	}
	
}