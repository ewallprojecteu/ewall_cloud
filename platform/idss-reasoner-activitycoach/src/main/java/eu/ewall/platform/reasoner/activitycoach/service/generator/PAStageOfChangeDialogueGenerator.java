package eu.ewall.platform.reasoner.activitycoach.service.generator;

import org.joda.time.DateTime;
import org.slf4j.Logger;

import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.dialogue.DialogueConstants;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.idss.utils.datetime.VirtualClock;
import eu.ewall.platform.reasoner.activitycoach.service.VCDataCollection;
import eu.ewall.platform.reasoner.activitycoach.service.VCMessageAvailableQueue;
import eu.ewall.platform.reasoner.activitycoach.service.VCService;
import eu.ewall.platform.reasoner.activitycoach.service.dialogue.DialogueManager;
import eu.ewall.platform.reasoner.activitycoach.service.protocol.VCMessageAvailable;
import eu.ewall.platform.reasoner.activitycoach.service.protocol.VCMessageFactory;

/**
 * A {@link PAStageOfChangeDialogueGenerator} is a {@link DialogueGenerator} that can be used to 
 * check if a dialogue of type {@link DialogueConstants.DT_PHYSICAL_ACTIVITY_STAGE_OF_CHANGE} should 
 * be offered to the user at this point in time.
 * 
 * @author Harm op den Akker (Roessingh Research and Development)
 */
public class PAStageOfChangeDialogueGenerator extends DialogueGenerator {
	
	private static final String LOGTAG = "PAStageOfChangeGenerator";
	private Logger logger;
	
	public PAStageOfChangeDialogueGenerator(VCService vcService) {
		super(vcService);
		logger = AppComponents.getLogger(LOGTAG);
	}
	
	public VCMessageAvailable generateDialogueAvailable(IDSSUserProfile idssUserProfile) {
		VCMessageFactory vcMessageFactory = new VCMessageFactory(getConfig());
		String username = idssUserProfile.getUsername();
		VCDataCollection vcDataCollection = getVCService().getDataCollections().get(username);
				
		
		// Check if a "DialogueConstants.DT_PHYSICALACTIVITYSTAGEOFCHANGE" dialogue is already in the queue
		VCMessageAvailableQueue queue = getVCService().getMessageAvailableQueues().get(username);
		if(queue.hasDialogueAvailable(getDialogueTypeId())) {
			logger.info("["+username+"] Dialogue of type '"+getDialogueTypeId()+"' found in queue for user, checking expiration time.");
			VirtualClock clock = VirtualClock.getInstance();
			DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
			if(queue.isExpiredDialogue(getDialogueTypeId(),now)) {
				queue.removeDialogueAvailable(getDialogueTypeId());
				logger.info("["+username+"] Dialogue of type '"+getDialogueTypeId()+"' in queue expired, continuing dialogue generation.");
			} else {
				logger.info("["+username+"] Dialogue of type '"+getDialogueTypeId()+"' in queue active, skipping dialogue generation.");
				return vcMessageFactory.generateDialogueAvailable(idssUserProfile,getDialogueTypeId());
			}
		}
		
		// Check if a "DialogueConstants.DT_PHYSICALACTIVITYSTAGEOFCHANGE" dialogue is currently ongoing
		DialogueManager dialogueManager = getVCService().getDialogueManager(idssUserProfile);
		if(dialogueManager.hasActiveDialogue()) {
			String activeDialogueTypeId = dialogueManager.getActiveDialogue().getDialogueTypeId();
			logger.info("Dialogue Manager for user '"+idssUserProfile.getUsername()+"' has active dialogue of type '"+activeDialogueTypeId+"'.");
			if(activeDialogueTypeId.equals(getDialogueTypeId())) {
				// Stage of Change dialogue is active, so should not be generated
				return null;
			}
		}
		
		// Continue checking if a new PA Stage of Change dialogue should be made available
			
		try {
			if(!vcDataCollection.requireUpdateStageOfChange()) return null;
		} catch (Exception e) {
			logger.error("["+username+"] Failed to read Stage of Change information from PhysicalActivityDomainModel.",e);
			return null;
		}
		
		// Check if the reminder has been postponed.
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());		
		
		try {
			DateTime postponeTime = vcDataCollection.getPostponePAStageOfChangeReminder();
			if(postponeTime != null) {
				logger.info("["+username+"] PA Stage of Change update reminder postponed until '"+postponeTime.toString()+"', skipping generation.");
				if(now.isBefore(postponeTime)) return null;
			}
		} catch (Exception e) {
			logger.error("["+username+"] Failed to read PA Stage of Change reminder postpone information from InteractionModel.",e);
			return null;
		}
			
		// Generate new PA Stage of Change dialogue available
		return generateVCMessageAvailable(idssUserProfile,getDialogueTypeId());
	}

	@Override
	public String getName() {
		return LOGTAG;
	}

	@Override
	protected String getDialogueTypeId() {
		return DialogueConstants.DT_PHYSICAL_ACTIVITY_STAGE_OF_CHANGE;
	}
	
}