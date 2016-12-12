package eu.ewall.platform.reasoner.activitycoach.service.dialogue;

import org.joda.time.DateTime;

public class DialogueAvailable {
	
	private String dialogueTypeId;
	private DateTime expirationTime;
	
	// ---------- Constructors ----------- //
	
	public DialogueAvailable(String dialogueTypeId) {
		this.dialogueTypeId = dialogueTypeId; 
	}
	
	public DialogueAvailable(String dialogueTypeId, DateTime expirationTime) {
		this.dialogueTypeId = dialogueTypeId;
		this.expirationTime = expirationTime;
	}
	
	// ---------- Getters ---------- //
	
	/**
	 * Returns the dialogue type id of this {@link DialogueAvailable}.
	 * @return the dialogue type id of this {@link DialogueAvailable}.
	 */
	public String getDialogueTypeId() {
		return dialogueTypeId;
	}
	
	/**
	 * Returns the expiration time of this {@link DialogueAvailable}.
	 * @return the expiration time of this {@link DialogueAvailable}.
	 */
	public DateTime getExpirationTime() {
		return expirationTime;
	}
	
	// ---------- Setters ---------- //
	
	/**
	 * Sets the dialogue type id of this {@link DialogueAvailable}.
	 * @param dialogueTypeId the dialogue type id of this {@link DialogueAvailable}.
	 */
	public void setDialogueTypeId(String dialogueTypeId) {
		this.dialogueTypeId = dialogueTypeId;
	}
	
	/**
	 * Sets the expiration time of this {@link DialogueAvailable}.
	 * @param expirationTime the expiration time of this {@link DialogueAvailable}.
	 */
	public void setExpirationTime(DateTime expirationTime) {
		this.expirationTime = expirationTime;
	}

}
