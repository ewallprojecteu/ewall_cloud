package eu.ewall.platform.reasoner.activitycoach.service.pamm.representation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;

import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.message.PhysicalActivityMotivationalMessage;
import eu.ewall.platform.idss.service.model.common.message.content.MessageContent;
import eu.ewall.platform.idss.service.model.common.message.representation.MessageRepresentation;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.reasoner.activitycoach.service.VCDataCollection;
import eu.ewall.platform.reasoner.activitycoach.service.exception.MessageNotFoundException;
import eu.ewall.platform.reasoner.activitycoach.service.messages.FeedbackRepresentation;
import eu.ewall.platform.reasoner.activitycoach.service.messages.MessageRepresentationSet;
import eu.ewall.platform.reasoner.activitycoach.service.messages.MessageStyle;

public class FeedbackRepresentationGenerator {
	
	private MessageRepresentation messageRepresentation;
	private MessageContent messageContent;
	private MessageRepresentationSet feedbackDatabase;
	
	private Logger logger;
	private String userLogString = null;
	
	public FeedbackRepresentationGenerator(IDSSUserProfile idssUserProfile, VCDataCollection vcDataCollection, PhysicalActivityMotivationalMessage motivationalMessage) {	
		this.messageRepresentation = motivationalMessage.getRepresentation();
		this.messageContent = motivationalMessage.getContent();
		this.feedbackDatabase = vcDataCollection.getMessageDatabase().getFeedbackRepresentations();
		
		logger = AppComponents.getLogger(MessageRepresentationGenerator.LOGTAG);
		userLogString = "["+idssUserProfile.getUsername()+"] ";
	}
	
	protected boolean generateFeedback() throws MessageNotFoundException {
		Random random = new Random();
				
		@SuppressWarnings("unchecked")
		List<FeedbackRepresentation> remainingRepresentations = (List<FeedbackRepresentation>) feedbackDatabase.getRepresentations();
		
		logger.info(userLogString+"Found "+remainingRepresentations.size()+" possible feedback representations, filtering now.");
		
		// Start filtering all feedback representations based on the "content" decisions
		List<FeedbackRepresentation> candidates = new ArrayList<FeedbackRepresentation>();
		
		// Filter out based on the "goal-setting" decision
		for(FeedbackRepresentation representation : remainingRepresentations) {
			if(representation.getWithGoalSetting() == messageContent.getFeedbackContent().getHasGoalSetting()) {
				candidates.add(representation);
			}
		}
		remainingRepresentations.clear();
		remainingRepresentations.addAll(candidates);
		candidates.clear();
		
		logger.info(userLogString+"Filtered withGoalSetting ("+remainingRepresentations.size()+" representations remaining).");
		
		// Only perform further goal-setting related filtering if relevant
		if(messageContent.getFeedbackContent().getHasGoalSetting()) {
		
			// Filter out based on the "underGoal" decision
			for(FeedbackRepresentation representation : remainingRepresentations) {
				if(representation.getUnderGoal() == messageContent.getFeedbackContent().getIsUnderGoal()) {
					candidates.add(representation);
				}
			}
			remainingRepresentations.clear();
			remainingRepresentations.addAll(candidates);
			candidates.clear();			
			logger.info(userLogString+"Filtered underGoal ("+remainingRepresentations.size()+" representations remaining).");
			
			// Filter out based on the "overGoal" decision
			for(FeedbackRepresentation representation : remainingRepresentations) {
				if(representation.getOverGoal() == messageContent.getFeedbackContent().getIsOverGoal()) {
					candidates.add(representation);
				}
			}
			remainingRepresentations.clear();
			remainingRepresentations.addAll(candidates);
			candidates.clear();			
			logger.info(userLogString+"Filtered overGoal ("+remainingRepresentations.size()+" representations remaining).");
		}
		
		// Filter based on the activity unit used in the message
		for(FeedbackRepresentation representation : remainingRepresentations) {
			String representationActivityUnit = representation.getActivityUnit();
			if(messageContent.getFeedbackContent().getActivityUnit().toString().equalsIgnoreCase(representationActivityUnit)) {
				candidates.add(representation);
			}
		}
		remainingRepresentations.clear();
		remainingRepresentations.addAll(candidates);
		candidates.clear();			
		logger.info(userLogString+"Filtered activity unit ("+remainingRepresentations.size()+" representations remaining).");
		
		// If there are still candidates left, select a random one, otherwise return false.
		if(remainingRepresentations.size() > 0) {
			int index = random.nextInt(remainingRepresentations.size());
			FeedbackRepresentation selectedFeedbackRepresentation = remainingRepresentations.get(index);
			
			// Make a selection of the message variations - TODO: style variation selection / history based random
			List<MessageStyle> messages = selectedFeedbackRepresentation.getMessages();
			index = random.nextInt(messages.size());
			messageRepresentation.setFeedbackText(messages.get(index).getMessage());
			return true;
		} else {
			return false;
		}
	}	
}