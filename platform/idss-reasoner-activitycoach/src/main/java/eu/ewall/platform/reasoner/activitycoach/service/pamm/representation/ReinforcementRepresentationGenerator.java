package eu.ewall.platform.reasoner.activitycoach.service.pamm.representation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;

import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.message.PhysicalActivityMotivationalMessage;
import eu.ewall.platform.idss.service.model.common.message.content.ReinforcementContent;
import eu.ewall.platform.idss.service.model.common.message.representation.MessageRepresentation;
import eu.ewall.platform.idss.service.model.type.PrimaryIntention;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.reasoner.activitycoach.service.VCDataCollection;
import eu.ewall.platform.reasoner.activitycoach.service.exception.MessageNotFoundException;
import eu.ewall.platform.reasoner.activitycoach.service.messages.MessageRepresentationSet;
import eu.ewall.platform.reasoner.activitycoach.service.messages.MessageStyle;
import eu.ewall.platform.reasoner.activitycoach.service.messages.ReinforcementRepresentation;

public class ReinforcementRepresentationGenerator {
	
	private MessageRepresentation messageRepresentation;
	private ReinforcementContent reinforcementContent;
	private MessageRepresentationSet reinforcementsDatabase;
	
	private Logger logger;
	private String userLogString;
	
	protected ReinforcementRepresentationGenerator(IDSSUserProfile idssUserProfile, VCDataCollection vcDataCollection, PhysicalActivityMotivationalMessage motivationalMessage) {
		this.messageRepresentation = motivationalMessage.getRepresentation();
		this.reinforcementContent = motivationalMessage.getContent().getReinforcementContent();
		this.reinforcementsDatabase = vcDataCollection.getMessageDatabase().getReinforcementRepresentations();
		
		logger = AppComponents.getLogger(MessageRepresentationGenerator.LOGTAG);
		userLogString = "["+idssUserProfile.getUsername()+"] ";
	}
	
	protected boolean generateReinforcement() throws MessageNotFoundException  {
		@SuppressWarnings("unchecked")
		List<ReinforcementRepresentation> remainingRepresentations = (List<ReinforcementRepresentation>) reinforcementsDatabase.getRepresentations();
		logger.info(userLogString+"Found "+remainingRepresentations.size()+" possible reinforcement representations, filtering now.");
		
		if(remainingRepresentations.size() == 0) {
			throw new MessageNotFoundException("No representations available of type: reinforcement.");
		}
		// Filter on 'reinforcementIntention'
		PrimaryIntention reinforcementIntention = reinforcementContent.getReinforcementIntention();
				
		List<ReinforcementRepresentation> candidates = new ArrayList<ReinforcementRepresentation>();
		for(ReinforcementRepresentation representation : remainingRepresentations) {
			String candidateReinforcementIntention = representation.getReinforcementIntention();
			String messageReinforcementIntention = reinforcementIntention.toString();
			if(candidateReinforcementIntention.toLowerCase().equals(messageReinforcementIntention.toLowerCase())) {
				candidates.add(representation);
			}
		}
		remainingRepresentations.clear();
		remainingRepresentations.addAll(candidates);
		candidates.clear();		
		logger.info(userLogString+"Filtered reinforcementIntention ("+remainingRepresentations.size()+" representations remaining).");
		
		// Filter on 'closeToGoal'
		if(!(reinforcementIntention.equals(PrimaryIntention.NEUTRAL))) {
			boolean closeToGoal = reinforcementContent.getCloseToGoal();
			for(ReinforcementRepresentation representation : remainingRepresentations) {
				if(representation.getCloseToGoal() == closeToGoal) {
					candidates.add(representation);
				}
			}
			remainingRepresentations.clear();
			remainingRepresentations.addAll(candidates);
			candidates.clear();		
			logger.info(userLogString+"Filtered closeToGoal ("+remainingRepresentations.size()+" representations remaining).");
		}
		
		selectFromCandidates(remainingRepresentations);
		
		return true;
	}
	
	private void selectFromCandidates(List<ReinforcementRepresentation> candidates) {
		Random random = new Random();
		
		int index = random.nextInt(candidates.size());
		ReinforcementRepresentation selectedReinforcementRepresentation = candidates.get(index);
		
		// Make a selection of the message variations - TODO: style variation selection / history based random
		List<MessageStyle> messages = selectedReinforcementRepresentation.getMessages();
		index = random.nextInt(messages.size());
		messageRepresentation.setReinforcementText(messages.get(index).getMessage());
	}
}