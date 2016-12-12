package eu.ewall.platform.idss.service.model.common.dialogue;

import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;

/**
 * A {@link DialogueBasicReply} is a specific type of {@link DialogueReply} that represents
 * a simple "turn" in a conversational dialogue.
 * 
 * @author Harm op den Akker (Roessingh Research and Development)
 */
public class DialogueBasicReply extends DialogueReply {
	
	@DatabaseField(value=DatabaseType.STRING)
	private String statement;
	
	@DatabaseField(value=DatabaseType.STRING)
	private String nextStepId;
	
	// ---------- Getters ---------- //
	
	/**
	 * Returns the statement (utterance by the user) of this {@link DialogueBasicReply}.
	 * @return the statement (utterance by the user) of this {@link DialogueBasicReply}.
	 */
	public String getStatement() {
		return statement;
	}
	
	/**
	 * Returns the identifier of the {@link DialogueStep} that should be taken after
	 * this {@link DialogueReply} was selected, or {@code null} if this is an "ending" step
	 * of the dialogue.
	 * @return the identifier of the {@link DialogueStep}, or {@code null}.
	 */
	public String getNextStepId() {
		return nextStepId;
	}
	
	// ---------- Setters ---------- //
	
	/**
	 * Sets the statement (utterance by the user) of this {@link DialogueBasicReply}.
	 * @param statement the statement (utterance by the user) of this {@link DialogueBasicReply}.
	 */
	public void setStatement(String statement) {
		this.statement = statement;
	}
	
	/**
	 * Sets the identifier of the {@link DialogueStep} that should be taken after
	 * this {@link DialogueReply} was selected. If this {@link DialogueReply} is the selection
	 * of a "ending" step of the dialogue, this should be left to {@code null}.
	 * @param nextStepId the identifier of the {@link DialogueStep} that should be taken after
	 * this {@link DialogueReply} was selected.
	 */
	public void setNextStepId(String nextStepId) {
		this.nextStepId = nextStepId;
	}

}