package eu.ewall.platform.reasoner.activitycoach.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;

import eu.ewall.platform.idss.service.model.common.message.PhysicalActivityMotivationalMessage;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.reasoner.activitycoach.service.dialogue.DialogueAvailable;

public class VCMessageAvailableQueue {
	
	private static final String LOGTAG = "VCMessageAvailableQueue";
	private String username;
	private PhysicalActivityMotivationalMessage pamm;
	private List<DialogueAvailable> availableDialogues;
	private Logger logger;
	private long localLastUpdateTime;
	
	public VCMessageAvailableQueue(String username) {
		this.username = username;
		this.availableDialogues = new ArrayList<DialogueAvailable>();
		this.logger = AppComponents.getLogger(LOGTAG);
		this.localLastUpdateTime = 0l;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public long getLocalLastUpdateTime() {
		return localLastUpdateTime;
	}
	
	public void setLocalLastUpdateTime(long localLastUpdateTime) {
		this.localLastUpdateTime = localLastUpdateTime;
	}
	
	// ----------
	// ---------- Set / Get / Remove / Has for PAMM
	// ----------
	
	public void setPhysicalActivityMotivationalMessage(PhysicalActivityMotivationalMessage pamm) {
		this.pamm = pamm;
		logger.info("["+username+"] Added pamm to queue.");
	}
	
	public PhysicalActivityMotivationalMessage getPhysicalActivityMotivationalMessage() {
		return pamm;
	}
	
	public void removePhysicalActivityMotivationalMessage() {
		this.pamm = null;
		logger.info("["+username+"] Removed pamm from queue.");
	}
	
	public boolean hasPhysicalActivityMotivationalMessage() {
		if(pamm == null) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Checks whether the queued {@link PhysicalActivityMotivationalMessage} is expired given the current 
	 * date and time ({@code currentDateTime}). If the method is unable to determine expiration (e.g. when
	 * there is no pamm in the queue, or its timing information is missing), this method will return {@code true} and 
	 * the faulty message should be removed from the queue.
	 * 
	 * @param currentDateTime the current date and time to which to check expiration.
	 * @return {@code false} if the queued message is not expired, {@code true} if expired or unknown.
	 */
	public boolean isExpiredPhysicalActivityMotivationalMessage(DateTime currentDateTime) {
		if(pamm.getTiming() == null) {
			return true;
		} else {
			if(pamm.getTiming().getExpirationTime() == null) {
				return true;
			} else {
				DateTime expirationTime = pamm.getTiming().getExpirationTime();
				if(currentDateTime.isAfter(expirationTime)) {
					return true;
				} else {
					return false;
				}
			}
		}
	}
	
	// ----------
	// ---------- Set / Get / Remove / Has for Dialogues
	// ----------
	
	public List<DialogueAvailable> getAvailableDialogues() {
		return availableDialogues;
	}
	
	/**
	 * Check if a dialogue with given {@code dialogueTypeId} is already in this user's queue.
	 * @param dialogueTypeId the dialogue type id to look for.
	 * @return true if a {@link DialogueAvailable} with the given dialogue id is available
	 * in this queue, {@code false} otherwise.
	 */
	public boolean hasDialogueAvailable(String dialogueTypeId) {
		
		for(DialogueAvailable dialogueAvailable : availableDialogues) {
			if(dialogueAvailable.getDialogueTypeId().equals(dialogueTypeId)) return true;
		}
		
		return false;
	}
	
	public void addDialogueAvailable(DialogueAvailable dialogueAvailable) {
		availableDialogues.add(dialogueAvailable);
		logger.info("["+username+"] Added available dialogue of type '"+dialogueAvailable.getDialogueTypeId()+"'.");
	}
	
	/**
	 * Removes the {@link DialogueAvailable} with given {@code dialogueTypeId} from this 
	 * {@link VCMessageAvailableQueue}. This method returns {@code true} if the dialogue
	 * was successfully removed from the queue, or {@code false} if a {@link DialogueAvailable}
	 * with the given {@code dialogueTypeId} was not found.
	 * @param dialogueTypeId the dialogue type id of the {@link DialogueAvailable} to remove.
	 * @return true if the removal was successful, false otherwise
	 */
	public boolean removeDialogueAvailable(String dialogueTypeId) {
		for(DialogueAvailable dialogueAvailable : availableDialogues) {
			if(dialogueAvailable.getDialogueTypeId().equals(dialogueTypeId)) {
				availableDialogues.remove(dialogueAvailable);
				logger.info("["+username+"] Removed available dialogue of type '"+dialogueAvailable.getDialogueTypeId()+"'.");
				return true;
			}
		}
		return false;
	}
	
	public void removeAllDialogueAvailable() {
		availableDialogues = new ArrayList<DialogueAvailable>();
		logger.info("["+username+"] Removed all available dialogues.");
	}
	
	/**
	 * Checks the current {@link VCMessageAvailableQueue} for a {@link DialogueAvailable} with
	 * the given {@code dialogueTypeId} and checks its {@code expirationTime} versus the given
	 * {@code currentDateTime}. This function returns {@code true} if the {@link DialogueAvailable} 
	 * was found and is expired, or if the dialogue available was not found, or if it was found
	 * but its expiration time was missing.
	 * @param dialogueTypeId
	 * @param currentDateTime
	 * @return
	 */
	public boolean isExpiredDialogue(String dialogueTypeId, DateTime currentDateTime) {
		for(DialogueAvailable dialogueAvailable : availableDialogues) {
			if(dialogueAvailable.getDialogueTypeId().equals(dialogueTypeId)) {
				DateTime expirationTime = dialogueAvailable.getExpirationTime();
				if(expirationTime == null) {
					return true;
				} else {
					if(currentDateTime.isAfter(expirationTime)) {
						return true;
					} else {
						return false;
					}
				}
			}
		}
		// Dialogue does not exist, so consider expired.
		return true;
	}
}
