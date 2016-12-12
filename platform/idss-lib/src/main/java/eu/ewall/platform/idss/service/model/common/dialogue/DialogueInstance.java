package eu.ewall.platform.idss.service.model.common.dialogue;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.utils.json.DateTimeFromIsoDateTimeDeserializer;
import eu.ewall.platform.idss.utils.json.IsoDateTimeSerializer;

/**
 * A {@link DialogueInstance} models a concrete instance of a dialogue between the
 * Virtual Coach and the user.
 * 
 * @author Harm op den Akker (Roessingh Research and Development)
 */
public class DialogueInstance extends AbstractDatabaseObject {
	
	@DatabaseField(value=DatabaseType.STRING, index=true)
	private String username;
	
	@DatabaseField(value=DatabaseType.STRING)
	private String dialogueTypeId;
	
	@DatabaseField(value=DatabaseType.STRING)
	private String initialStepId;
	
	@DatabaseField(value=DatabaseType.STRING)
	private String currentStepId;
	
	@DatabaseField(value=DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime startTime;
	
	@DatabaseField(value=DatabaseType.BYTE)
	private boolean finished = false;
	
	@DatabaseField(value=DatabaseType.OBJECT_LIST)
	private List<DialogueStep> dialogueSteps;
	
	@DatabaseField(value=DatabaseType.OBJECT_LIST)
	private List<DialogueHistoryEntry> dialogueHistory;
		
	// ---------- Constructors ---------- //
	
	/**
	 * Creates an empty instance of a {@link DialogueInstance}.
	 */
	public DialogueInstance() {
		this.dialogueSteps = new ArrayList<DialogueStep>();
		this.dialogueHistory = new ArrayList<DialogueHistoryEntry>();
	}
	
	/**
	 * Creates an instance of a {@link DialogueInstance} with a given {@code dialogueTypeId},
	 * for the given {@username}, and with a given {@code startTime}.
	 * @param dialogueTypeId the unique identifier of the textual representation of the {@link DialogueInstance}.
	 * @param username the username of the user with which this {@link DialogueInstance} is held.
	 * @param startTime the start time of the {@link DialogueInstance} as {@link DateTime} object.
	 */
	public DialogueInstance(String dialogueTypeId, String username, DateTime startTime) {
		this.dialogueTypeId = dialogueTypeId;
		this.username = username;
		this.startTime = startTime;
		this.dialogueSteps = new ArrayList<DialogueStep>();
		this.dialogueHistory = new ArrayList<DialogueHistoryEntry>();
	}

	// ---------- Getters ---------- //
	
	/**
	 * Returns the username of the user with which this {@link DialogueInstance} is held.
	 * @return the username of the user with which this {@link DialogueInstance} is held.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Returns the unique identifier of the representation of this {@link DialogueInstance}.
	 * @return the unique identifier of the representation of this {@link DialogueInstance}.
	 */
	public String getDialogueTypeId() {
		return dialogueTypeId;
	}
	
	/**
	 * Returns the stepId for the "opening" step for this {@link DialogueInstance}.
	 * @return the stepId for the "opening" step for this {@link DialogueInstance}.
	 */
	public String getInitialStepId() {
		return initialStepId;
	}
	
	/**
	 * Returns the stepId of the {@link DialogeStep} that is currently active in this {@link DialogueInstance}.
	 * @return the stepId of the {@link DialogeStep} that is currently active in this {@link DialogueInstance}.
	 */
	public String getCurrentStepId() {
		return currentStepId;
	}
	
	/**
	 * Returns the {@link DateTime} at which this {@link DialogueInstance} was started.
	 * @return the {@link DateTime} at which this {@link DialogueInstance} was started.
	 */
	public DateTime getStartTime() {
		return startTime;
	}
	
	/**
	 * Returns {@code true} if this {@link DialogueInstance} is finished, {@code false} otherwise.
	 * @return {@code true} if this {@link DialogueInstance} is finished, {@code false} otherwise.
	 */
	public boolean isFinished() {
		return finished;
	}
	
	/**
	 * Returns the list of available {@link DialogueStep}s in this {@link DialogueInstance}.
	 * @return the list of available {@link DialogueStep}s in this {@link DialogueInstance}.
	 */
	public List<DialogueStep> getDialogueSteps() {
		return dialogueSteps;
	}
	
	/**
	 * Returns the history of this {@link DialogueInstance} as a list of {@link DialogueHistoryEntry} items.
	 * @return the history of this {@link DialogueInstance} as a list of {@link DialogueHistoryEntry} items.
	 */
	public List<DialogueHistoryEntry> getDialogueHistory() {
		return dialogueHistory;
	}
	
	// ---------- Setters ---------- //
	
	/**
	 * Sets the username of the user with which this {@link DialogueInstance} is held.
	 * @param username the username of the user with which this {@link DialogueInstance} is held.
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Sets the unique identifier of the representation of this {@link DialogueInstance}.
	 * @param dialogueTypeId the unique identifier of the representation of this {@link DialogueInstance}.
	 */
	public void setDialogueTypeId(String dialogueTypeId) {
		this.dialogueTypeId = dialogueTypeId;
	}
	
	/**
	 * Sets the stepId for the "opening" step for this {@link DialogueInstance}.
	 * @param initialStepId the stepId for the "opening" step for this {@link DialogueInstance}.
	 */
	public void setInitialStepId(String initialStepId) {
		this.initialStepId = initialStepId;
	}
	
	/**
	 * Sets the stepId of the {@link DialogeStep} that is currently active in this {@link DialogueInstance}.
	 * @param currentStepId the stepId of the {@link DialogeStep} that is currently active in this {@link DialogueInstance}.
	 */
	public void setCurrentStepId(String currentStepId) {
		this.currentStepId = currentStepId;
	}
	
	/**
	 * Sets the {@link DateTime} at which this {@link DialogueInstance} was started.
	 * @param startTime the {@link DateTime} at which this {@link DialogueInstance} was started.
	 */
	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}
	
	/**
	 * Sets whether or not this {@link DialogueInstance} is finished. Set to {@code true} if this {@link DialogueInstance} 
	 * is finished, {@code false} otherwise.
	 * @param finished whether or not this {@link DialogueInstance} is finished.
	 */
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	
	/**
	 * Sets the list of available {@link DialogueStep}s in this {@link DialogueInstance}.
	 * @param dialogueSteps the list of available {@link DialogueStep}s in this {@link DialogueInstance}.
	 */
	public void setDialogueSteps(List<DialogueStep> dialogueSteps) {
		this.dialogueSteps = dialogueSteps;
	}
	
	/**
	 * Sets the history of this {@link DialogueInstance} as a list of {@link DialogueHistoryEntry} items.
	 * @param dialogueHistory the history of this {@link DialogueInstance} as a list of {@link DialogueHistoryEntry} items.
	 */
	public void setDialogueHistory(List<DialogueHistoryEntry> dialogueHistory) {
		this.dialogueHistory = dialogueHistory;
	}
	
	// ---------- Additional functions ---------- //
	
	/**
	 * Returns the {@link DialogueStep} identifier by the given {@code stepId}, or
	 * {@code null} if no {@link DialogueStep} with the given id was found.
	 * @param stepId the unique identifier of the {@link DialogueStep} to look for.
	 * @return the {@link DialogueStep}, or {@code null}.
	 */
	public DialogueStep getStepById(String stepId) {
		for(DialogueStep dialogueStep : dialogueSteps) {
			if(dialogueStep.getStepId().equals(stepId)) {
				return dialogueStep;
			}
		}
		return null;
	}
	
	/**
	 * Adds an {@link DialogueHistoryEntry} to the history for this {@link DialogueInstance}. 
	 * @param dialogueHistoryEntry an {@link DialogueHistoryEntry} to the history for this {@link DialogueInstance}. 
	 */
	public void addDialogueHistoryStep(DialogueHistoryEntry dialogueHistoryEntry) {
		dialogueHistory.add(dialogueHistoryEntry);
	}
}
