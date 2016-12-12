package eu.ewall.platform.reasoner.activitycoach.service.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
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

public class UseDomoticsApplicationDialogueGenerator extends DialogueGenerator {
	
	private static final String LOGTAG = "UseDomoticsApplicationDialogueGenerator";
	private Logger logger;
	private List<String> otherReminderDialogueTypes;
	
	public UseDomoticsApplicationDialogueGenerator(VCService vcService) {
		super(vcService);
		logger = AppComponents.getLogger(LOGTAG);
		otherReminderDialogueTypes = new ArrayList<String>();
		otherReminderDialogueTypes.add(DialogueConstants.DT_USE_ACTIVITY_APPLICATION);
		otherReminderDialogueTypes.add(DialogueConstants.DT_USE_CALENDAR_APPLICATION);
		otherReminderDialogueTypes.add(DialogueConstants.DT_USE_COGNITIVE_EXERCISE_APPLICATION);
		otherReminderDialogueTypes.add(DialogueConstants.DT_USE_SLEEP_APPLICATION);
		otherReminderDialogueTypes.add(DialogueConstants.DT_USE_HEALTH_APPLICATION);
		otherReminderDialogueTypes.add(DialogueConstants.DT_USE_VIDEO_EXERCISE_APPLICATION);
	}
	
	@Override
	public VCMessageAvailable generateDialogueAvailable(IDSSUserProfile idssUserProfile) {
		VCMessageFactory vcMessageFactory = new VCMessageFactory(getConfig());
		String username = idssUserProfile.getUsername();
		VCDataCollection vcDataCollection = getVCService().getDataCollections().get(username);
		
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		// Check if a dialogue is already in the queue
		VCMessageAvailableQueue queue = getVCService().getMessageAvailableQueues().get(username);
		if(queue.hasDialogueAvailable(getDialogueTypeId())) {
			if(queue.isExpiredDialogue(getDialogueTypeId(),now)) {
				queue.removeDialogueAvailable(getDialogueTypeId());
			} else {
				return vcMessageFactory.generateDialogueAvailable(idssUserProfile,getDialogueTypeId());
			}
		}
		
		// Check if another "application reminder is currently in the queue
		for(String otherReminderType : otherReminderDialogueTypes) {
			if(queue.hasDialogueAvailable(otherReminderType)) {
				if(queue.isExpiredDialogue(otherReminderType,now)) {
					queue.removeDialogueAvailable(otherReminderType);
				} else {
					return null;
				}
			}
		}
		
		// Check if a dialogue is currently ongoing
		DialogueManager dialogueManager = getVCService().getDialogueManager(idssUserProfile);
		if(dialogueManager.hasActiveDialogue()) {
			String activeDialogueTypeId = dialogueManager.getActiveDialogue().getDialogueTypeId();
			if(activeDialogueTypeId.equals(getDialogueTypeId())) {
				return null; // Dialogue is active, so should not be generated
			}
		}
				
		// Check if the reminder has been postponed.
		DateTime postponeTime = null;
		try {
			postponeTime = vcDataCollection.getPostponeDomoticsApplicationReminder();
			if(postponeTime != null) {
				if(now.isBefore(postponeTime)) {
					return null;
				}				
			}
		} catch (Exception e) {
			logger.error("["+username+"] Failed to read Domotics Application reminder postpone information from InteractionModel.",e);
			return null;
		}
		
		// Generate reminder based on the time of last interaction
		DateTime lastInteraction = null;
		try {
			lastInteraction = vcDataCollection.getLastInteractionDomoticsApplication();
		} catch (Exception e) {
			logger.error("["+username+"] Failed to read last interaction with Domotics Application information from InteractionModel.",e);
			return null;
		}
		
		int minMinutes = getConfig().getInteractionReminderMinimumAllowedMinutes();
		int maxMinutes = getConfig().getInteractionReminderMaximumAllowedMinutes();
		double coolDownFactor = getConfig().getInteractionReminderCoolDownFactor();
		if(coolDownFactor <= 0.0d) coolDownFactor = 1.0d;
		
		int minutesAgo = 0;
		
		if(lastInteraction != null) {
			minutesAgo = Minutes.minutesBetween(lastInteraction, now).getMinutes();
		} else {
			minutesAgo = maxMinutes;
		}
				
		if(minutesAgo <= minMinutes) {
			return null;
		} else if(minutesAgo >= maxMinutes) {
			return generateVCMessageAvailable(idssUserProfile,getDialogueTypeId());			
		} else {
			double probabilityOfReminder = (double)(((double)minutesAgo - (double)minMinutes) / (double)((double)maxMinutes - (double)minMinutes));
			probabilityOfReminder = probabilityOfReminder / (double)coolDownFactor;
			Random random = new Random();
			if(probabilityOfReminder > random.nextDouble()) {
				return generateVCMessageAvailable(idssUserProfile,getDialogueTypeId());
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
		return DialogueConstants.DT_USE_DOMOTICS_APPLICATION;
	}

}
