package eu.ewall.platform.reasoner.activitycoach.service.pamm.representation;

import org.slf4j.Logger;

import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.message.PhysicalActivityMotivationalMessage;
import eu.ewall.platform.idss.service.model.common.message.intention.MessageIntention;
import eu.ewall.platform.idss.service.model.common.message.representation.MessageRepresentation;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.reasoner.activitycoach.service.VCDataCollection;
import eu.ewall.platform.reasoner.activitycoach.service.exception.MessageNotFoundException;

public class MessageRepresentationGenerator {
	protected static final String LOGTAG = "MessageRepresentationGenerator";
	
	private VCDataCollection vcDataCollection;
	private IDSSUserProfile idssUserProfile;
	private Logger logger;
		
	public MessageRepresentationGenerator(IDSSUserProfile idssUserProfile, VCDataCollection vcDataCollection) {
		this.idssUserProfile = idssUserProfile;
		this.vcDataCollection = vcDataCollection;
		logger = AppComponents.getLogger(MessageRepresentationGenerator.LOGTAG);
	}
	
	public boolean generateMessageRepresentation(PhysicalActivityMotivationalMessage motivationalMessage) {
		MessageRepresentation messageRepresentation = new MessageRepresentation();
		motivationalMessage.setRepresentation(messageRepresentation);
		MessageIntention messageIntention = motivationalMessage.getIntention();
		
		// Greetings
		if(messageIntention.hasGreetingIntention()) {
			GreetingRepresentationGenerator greetingGenerator = new GreetingRepresentationGenerator(idssUserProfile,vcDataCollection,motivationalMessage);
			try {
				greetingGenerator.generateGreeting();
			} catch(MessageNotFoundException mnfe) {
				logger.warn("Attempted to represent a greeting, but no messages were found.");
			}
		}		
		
		// Feedback
		if(messageIntention.hasFeedbackIntention()) {
			FeedbackRepresentationGenerator feedbackGenerator = new FeedbackRepresentationGenerator(idssUserProfile,vcDataCollection,motivationalMessage);
			try {
				feedbackGenerator.generateFeedback();
			} catch(MessageNotFoundException mnfe) {
				logger.warn("Attempted to represent feedback, but no messages were found.");
			}
		}
		
		// Reinforcement
		if(messageIntention.hasReinforcementIntention()) {
			ReinforcementRepresentationGenerator reinforcementGenerator = new ReinforcementRepresentationGenerator(idssUserProfile,vcDataCollection,motivationalMessage);
			try {
				reinforcementGenerator.generateReinforcement();
			} catch(MessageNotFoundException mnfe) {
				logger.warn("Attempted to represent reinforcement, but no messages were found.");
			}
		}
		
		if(messageIntention.hasSynchronizeSensorIntention()) {
			SynchronizeSensorRepresentationGenerator synchronizeSensorGenerator = new SynchronizeSensorRepresentationGenerator(idssUserProfile,vcDataCollection,motivationalMessage);
			try {
				synchronizeSensorGenerator.generateSynchronizeSensor();
			} catch(MessageNotFoundException mnfe) {
				logger.warn("Attempted to represent synchronizeSensor, but no messages were found.");
			}
		}
				
		return true;
	}
}
