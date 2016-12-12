package eu.ewall.platform.reasoner.activitycoach.service.pamm.representation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joda.time.DateTime;
import org.slf4j.Logger;

import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.message.PhysicalActivityMotivationalMessage;
import eu.ewall.platform.idss.service.model.common.message.content.GreetingContent;
import eu.ewall.platform.idss.service.model.common.message.representation.MessageRepresentation;
import eu.ewall.platform.idss.service.model.type.DayPart;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.idss.utils.datetime.DateTimeUtils;
import eu.ewall.platform.idss.utils.datetime.VirtualClock;
import eu.ewall.platform.reasoner.activitycoach.service.VCDataCollection;
import eu.ewall.platform.reasoner.activitycoach.service.exception.MessageNotFoundException;
import eu.ewall.platform.reasoner.activitycoach.service.messages.GreetingRepresentation;
import eu.ewall.platform.reasoner.activitycoach.service.messages.MessageRepresentationSet;
import eu.ewall.platform.reasoner.activitycoach.service.messages.MessageStyle;

public class GreetingRepresentationGenerator {
	
	private IDSSUserProfile idssUserProfile;
	
	private MessageRepresentation messageRepresentation;
	private GreetingContent greetingContent;
	private MessageRepresentationSet greetingsDatabase;
	
	private Logger logger = null;
	private String userLogString = null;
	
	
	protected GreetingRepresentationGenerator(IDSSUserProfile idssUserProfile, VCDataCollection vcDataCollection, PhysicalActivityMotivationalMessage motivationalMessage) {
		this.idssUserProfile = idssUserProfile;
		
		this.messageRepresentation = motivationalMessage.getRepresentation();
		this.greetingContent = motivationalMessage.getContent().getGreetingContent();
		this.greetingsDatabase = vcDataCollection.getMessageDatabase().getGreetingRepresentations();
		
		logger = AppComponents.getLogger(MessageRepresentationGenerator.LOGTAG);
		userLogString = "["+idssUserProfile.getUsername()+"] ";
	}
	
	protected boolean generateGreeting() throws MessageNotFoundException {
		boolean isTimeBased = greetingContent.getIsTimeBased();
		boolean useFirstName = greetingContent.getUseFirstName();
		
		@SuppressWarnings("unchecked")
		List<GreetingRepresentation> remainingRepresentations = (List<GreetingRepresentation>) greetingsDatabase.getRepresentations();
			
		logger.info(userLogString+"Found "+remainingRepresentations.size()+" possible greeting representations, filtering now.");
		
		if(remainingRepresentations.size() == 0) {
			throw new MessageNotFoundException("No representations available of type: greeting.");
		}
						
		List<GreetingRepresentation> candidates = new ArrayList<GreetingRepresentation>();
		
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		DayPart currentDayPart = DateTimeUtils.getDayPart(now);
		
		// Filter out based on the "useFirstName" decision
		for(GreetingRepresentation representation : remainingRepresentations) {
			if(representation.getWithFirstName() == useFirstName) {
				candidates.add(representation);
			}
		}			
		remainingRepresentations.clear();
		remainingRepresentations.addAll(candidates);
		candidates.clear();
		
		logger.info(userLogString+"Filtered useFirstName ("+remainingRepresentations.size()+" representations remaining).");
		
		// Filter out based on the "isTimeBased" decision
		for(GreetingRepresentation representation : remainingRepresentations) {
			if(!isTimeBased) {
				if(representation.getDayPart() == null) {
					candidates.add(representation);
				}
			} else {
				String dayPartString = representation.getDayPart();
				if(dayPartString != null) {
					if(currentDayPart.toString().equalsIgnoreCase(dayPartString)) {
						candidates.add(representation);
					}
				}
			}
		}
		
		logger.info(userLogString+"Filtered isTimeBased ("+candidates.size()+" representations remaining).");
		
		selectFromCandidates(candidates);
		return true;
	}
	
	private void selectFromCandidates(List<GreetingRepresentation> candidates) {
		Random random = new Random();
		
		int index = random.nextInt(candidates.size());
		GreetingRepresentation selectedGreetingRepresentation = candidates.get(index);
		
		// Make a selection of the message variations - TODO: style variation selection / history based random
		List<MessageStyle> messages = selectedGreetingRepresentation.getMessages();
		index = random.nextInt(messages.size());
		messageRepresentation.setGreetingText(messages.get(index).getMessage());
	}
}