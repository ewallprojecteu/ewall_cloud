package eu.ewall.platform.reasoner.activitycoach.service.generator;

import org.joda.time.DateTime;

import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.utils.datetime.VirtualClock;
import eu.ewall.platform.reasoner.activitycoach.service.VCConfiguration;
import eu.ewall.platform.reasoner.activitycoach.service.VCMessageAvailableQueue;
import eu.ewall.platform.reasoner.activitycoach.service.VCService;
import eu.ewall.platform.reasoner.activitycoach.service.dialogue.DialogueAvailable;
import eu.ewall.platform.reasoner.activitycoach.service.protocol.VCMessageAvailable;
import eu.ewall.platform.reasoner.activitycoach.service.protocol.VCMessageFactory;

public abstract class DialogueGenerator {
	
	private VCService vcService;
	
	public DialogueGenerator(VCService vcService) {
		this.vcService = vcService;
	}
		
	public abstract VCMessageAvailable generateDialogueAvailable(IDSSUserProfile idssUserProfile);
	
	public abstract String getName();
	
	protected abstract String getDialogueTypeId();
	
	protected VCService getVCService() {
		return vcService;
	}
	
	protected VCConfiguration getConfig() {
		return vcService.getConfiguration();
	}
	
	protected VCMessageAvailable generateVCMessageAvailable(IDSSUserProfile idssUserProfile, String dialogueTypeId) {
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		VCMessageFactory vcMessageFactory = new VCMessageFactory(getConfig());
		VCMessageAvailableQueue queue = vcService.getMessageAvailableQueues().get(idssUserProfile.getUsername());
		
		if(queue.hasDialogueAvailable(dialogueTypeId)) {
			queue.removeDialogueAvailable(dialogueTypeId);
		}
		
		DateTime expirationTime = now.plusMinutes(getConfig().getDialogueExpirationMinutes());
		DialogueAvailable dialogueAvailable = new DialogueAvailable(dialogueTypeId,expirationTime);
		queue.addDialogueAvailable(dialogueAvailable);
		
		
		return vcMessageFactory.generateDialogueAvailable(idssUserProfile,dialogueTypeId);
	}
	
}
