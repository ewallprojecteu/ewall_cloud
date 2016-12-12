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

public class GoodMorningDialogueGenerator extends DialogueGenerator {
	
	private static final String LOGTAG = "GoodMorningDialogueGenerator";
	private Logger logger;
	
	public GoodMorningDialogueGenerator(VCService vcService) {
		super(vcService);
		logger = AppComponents.getLogger(LOGTAG);
	}
	
	@Override
	public VCMessageAvailable generateDialogueAvailable(IDSSUserProfile idssUserProfile) {
		VCMessageFactory vcMessageFactory = new VCMessageFactory(getConfig());
		String username = idssUserProfile.getUsername();
		VCDataCollection vcDataCollection = getVCService().getDataCollections().get(username);
		
		// Check if a "DialogueConstants.DT_GOODMORNING" dialogue is already in the queue
		VCMessageAvailableQueue queue = getVCService().getMessageAvailableQueues().get(username);
		if(queue.hasDialogueAvailable(getDialogueTypeId())) {
			logger.info("["+username+"] Dialogue of type '"+getDialogueTypeId()+"' found in queue for user, checking expiration time.");
			VirtualClock clock = VirtualClock.getInstance();
			DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
			if(queue.isExpiredDialogue(getDialogueTypeId(),now)) {
				queue.removeDialogueAvailable(getDialogueTypeId());
				logger.info("["+username+"] Dialogue of type '"+getDialogueTypeId()+"' in queue expired, no more 'good morning dialogues' for today.");
				return null;
			} else {
				logger.info("["+username+"] Dialogue of type '"+getDialogueTypeId()+"' in queue active, skipping dialogue generation.");
				return vcMessageFactory.generateDialogueAvailable(idssUserProfile,getDialogueTypeId());
			}
		}
		
		// Check if a "DialogueConstants.DT_GOODMORNING" dialogue is currently ongoing
		DialogueManager dialogueManager = getVCService().getDialogueManager(idssUserProfile);
		if(dialogueManager.hasActiveDialogue()) {
			String activeDialogueTypeId = dialogueManager.getActiveDialogue().getDialogueTypeId();
			if(activeDialogueTypeId.equals(getDialogueTypeId())) {
				return null; // Good Morning dialogue is active, so should not be generated
			}
		}
		
		DateTime lastInteraction = null;
		try {
			lastInteraction = vcDataCollection.getLastInteractionGoodMorning();
		} catch (Exception e) {
			logger.error("["+username+"] failed to read last interaction information from InteractionModel.",e);
			return null;
		}
		
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		boolean goodMorningDialogueAllowed = false;
		if(lastInteraction == null) {
			goodMorningDialogueAllowed = true;
		} else if (now.isAfter(lastInteraction)) {
			if((now.getYear() != lastInteraction.getYear()) 
					|| (now.getMonthOfYear() != lastInteraction.getMonthOfYear())
					|| (now.getDayOfMonth() != lastInteraction.getDayOfMonth())) {
				goodMorningDialogueAllowed = true;
			}
		}
			
		if(goodMorningDialogueAllowed) {
			// Didn't have a "good morning" dialogue yet today.			
			try {
				Integer wakeUpTimeToday = vcDataCollection.getWakeUpTimeToday();
				int currentMinuteOfDay = now.getMinuteOfDay();
				
				if((currentMinuteOfDay > wakeUpTimeToday) && (currentMinuteOfDay < wakeUpTimeToday + 120)) {
					return generateVCMessageAvailable(idssUserProfile,getDialogueTypeId());
				} else {
					return null;
				}
				
			} catch (Exception e) {
				logger.error("["+username+"] Error retrieving daily routine through VCDataCollection.",e);
				return null;
			}			
		}		
		return null;
	}

	@Override
	public String getName() {
		return LOGTAG;
	}

	@Override
	protected String getDialogueTypeId() {
		return DialogueConstants.DT_GOOD_MORNING;
	}

}
