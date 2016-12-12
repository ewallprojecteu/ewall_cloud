package eu.ewall.platform.idss.service.model.common.dialogue;

import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.utils.json.DateTimeFromIsoDateTimeDeserializer;
import eu.ewall.platform.idss.utils.json.IsoDateTimeSerializer;

/**
 * A {@link DialogueHistoryEntry} is an object used for tracking steps
 * in a {@link DialogueInstance} execution. It contains a timestamp and 
 * the {@link DialogueReply} that was selected by the user.
 * 
 * @author Harm op den Akker (Roessingh Research and Development)
 */
public class DialogueHistoryEntry extends AbstractDatabaseObject {
		
	@DatabaseField(value=DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime timeStamp;
	
	@DatabaseField(value=DatabaseType.OBJECT)
	private DialogueReply dialogueReply;
	
	// ---------- Constructors ----------- //
	
	/**
	 * The empty constructor for database reading.
	 */
	public DialogueHistoryEntry() { }
	
	/**
	 * Creates an instance of a {@link DialogueHistoryEntry} with given timestamp
	 * and {@link DialogueReply}
	 * @param timeStamp the time at which the dialogue step was taken as {@link DateTime} object.
	 * @param dialogueReply the {@link DialogueReply} selected by the user.
	 */
	public DialogueHistoryEntry(DateTime timeStamp, DialogueReply dialogueReply) {
		this.timeStamp = timeStamp;
		this.dialogueReply = dialogueReply;
	}
	
	// ---------- Getters ---------- //
	
	/**
	 * Returns the time at which the dialogue step was taken as {@link DateTime} object.
	 * @return the time at which the dialogue step was taken as {@link DateTime} object.
	 */
	public DateTime getTimeStamp() {
		return timeStamp;
	}
	
	/**
	 * Returns the {@link DialogueReply} selected by the user.
	 * @return the {@link DialogueReply} selected by the user.
	 */
	public DialogueReply getDialogueReply() {
		return dialogueReply;
	}
	
	// ---------- Setters ---------- //
	
	/**
	 * Sets the time at which the dialogue step was taken as {@link DateTime} object.
	 * @param timeStamp the time at which the dialogue step was taken as {@link DateTime} object.
	 */
	public void setTimeStamp(DateTime timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	/**
	 * Sets the {@link DialogueReply} selected by the user.
	 * @param dialogueReply the {@link DialogueReply} selected by the user.
	 */
	public void setDialogueReply(DialogueReply dialogueReply) {
		this.dialogueReply = dialogueReply;
	}
}
