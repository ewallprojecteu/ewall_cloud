package eu.ewall.platform.idss.service.model.common.dialogue;

import java.util.List;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;

/**
 * A {@link DialogueInstance} contains a number of these {@link DialogueStep}s. Every
 * {@link DialogueStep} models a "turn" in a dialogue from the Virtual Coach to the user.
 * The Virtual Coach holds dialogues with the user by presenting him/her with these
 * {@link DialogueStep}s. Each {@link DialogueStep} contains a list of possible 
 * {@link DialogueReply}s, that signify possible replies from the user back to
 * the Virtual Coach.
 * 
 * @author Harm op den Akker (Roessingh Research and Development)
 */
public class DialogueStep extends AbstractDatabaseObject {
	
	@DatabaseField(value=DatabaseType.STRING)
	private String stepId;
	
	@DatabaseField(value=DatabaseType.STRING)
	private String statement;
	
	@DatabaseField(value=DatabaseType.OBJECT_LIST)
	private List<DialogueReply> dialogueReplies;
	
	// ---------- Getters ---------- //
	
	/**
	 * Returns the step identifier, which should be unique within the {@link DialogueInstance}.
	 * @return the step identifier, which should be unique within the {@link DialogueInstance}.
	 */
	public String getStepId() {
		return stepId;
	}
	
	/**
	 * Returns the statement of the Virtual Coach to the user in this {@link DialogueStep}.
	 * @return the statement of the Virtual Coach to the user in this {@link DialogueStep}.
	 */
	public String getStatement() {
		return statement;
	}
	
	/**
	 * Returns a list of the possible {@link DialogueReply} options in this {@link DialogueStep}.
	 * @return a list of the possible {@link DialogueReply} options in this {@link DialogueStep}.
	 */
	public List<DialogueReply> getDialogueReplies() {
		return dialogueReplies;
	}
	
	// ---------- Setters ---------- //
	
	/**
	 * Sets the step identifier, which should be unique within the {@link DialogueInstance}.
	 * @param stepId the step identifier, which should be unique within the {@link DialogueInstance}.
	 */
	public void setStepId(String stepId) {
		this.stepId = stepId;
	}
	
	/**
	 * Sets the statement of the Virtual Coach to the user in this {@link DialogueStep}.
	 * @param statement the statement of the Virtual Coach to the user in this {@link DialogueStep}.
	 */
	public void setStatement(String statement) {
		this.statement = statement;
	}
	
	/**
	 * Sets a list of the possible {@link DialogueReply} options in this {@link DialogueStep}.
	 * @param dialogueReplies a list of the possible {@link DialogueReply} options in this {@link DialogueStep}.
	 */
	public void setDialogueReplies(List<DialogueReply> dialogueReplies) {
		this.dialogueReplies = dialogueReplies;
	}
	
	// ---------- Additional functions ---------- //
	
	/**
	 * Returns the {@link DialogueReply} identified by the given {@code replyId}, or {@code null} if
	 * a {@link DialogueReply} with the given identifier is not an option in this {@link DialogueStep}.
	 * @param replyId the unique identifier of the {@link DialogueReply} for which to search.
	 * @return a {@link DialogueReply} with given id, or {@code null} if it is not found.
	 */
	public DialogueReply getReplyById(String replyId) {
		for(DialogueReply dialogueReply : dialogueReplies) {
			if(dialogueReply.getReplyId().equals(replyId)) return dialogueReply;
		}
		return null;
	}
	
	public void removeDialogueReply(DialogueReply dialogueReply) {
		dialogueReplies.remove(dialogueReply);
	}
}