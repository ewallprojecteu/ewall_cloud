package eu.ewall.platform.reasoner.activitycoach.service.pamm.representation;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;

import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.message.PhysicalActivityMotivationalMessage;
import eu.ewall.platform.idss.service.model.common.message.representation.MessageRepresentation;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.reasoner.activitycoach.service.VCDataCollection;
import eu.ewall.platform.reasoner.activitycoach.service.exception.MessageNotFoundException;
import eu.ewall.platform.reasoner.activitycoach.service.messages.MessageRepresentationSet;
import eu.ewall.platform.reasoner.activitycoach.service.messages.MessageStyle;
import eu.ewall.platform.reasoner.activitycoach.service.messages.SynchronizeSensorRepresentation;

public class SynchronizeSensorRepresentationGenerator {
	
	private MessageRepresentation messageRepresentation;
	private MessageRepresentationSet synchronizeSensorDatabase;
	
	private Logger logger = null;
	private String userLogString = null;
	
	protected SynchronizeSensorRepresentationGenerator(IDSSUserProfile idssUserProfile, VCDataCollection vcDataCollection, PhysicalActivityMotivationalMessage motivationalMessage) {
		this.messageRepresentation = motivationalMessage.getRepresentation();
		this.synchronizeSensorDatabase = vcDataCollection.getMessageDatabase().getSynchronizeSensorRepresentations();
		
		logger = AppComponents.getLogger(MessageRepresentationGenerator.LOGTAG);
		userLogString = "["+idssUserProfile.getUsername()+"] ";
	}
	
	protected boolean generateSynchronizeSensor() throws MessageNotFoundException {
		
		@SuppressWarnings("unchecked")
		List<SynchronizeSensorRepresentation> remainingRepresentations = (List<SynchronizeSensorRepresentation>) synchronizeSensorDatabase.getRepresentations();
		logger.info(userLogString+"Found "+remainingRepresentations.size()+" possible synchronizeSensor representations, filtering now.");
		
		if(remainingRepresentations.size() > 0) {
			selectFromCandidates(remainingRepresentations);
			return true;
		} else {
			return false;
		}
	}
	
	private void selectFromCandidates(List<SynchronizeSensorRepresentation> candidates) {
		Random random = new Random();
		
		int index = random.nextInt(candidates.size());
		SynchronizeSensorRepresentation selectedSynchronizeSensorRepresentation = candidates.get(index);
		
		// Make a selection of the message variations - TODO: style variation selection / history based random
		List<MessageStyle> messages = selectedSynchronizeSensorRepresentation.getMessages();
		index = random.nextInt(messages.size());
		messageRepresentation.setSynchronizeSensorText(messages.get(index).getMessage());
	}	
}