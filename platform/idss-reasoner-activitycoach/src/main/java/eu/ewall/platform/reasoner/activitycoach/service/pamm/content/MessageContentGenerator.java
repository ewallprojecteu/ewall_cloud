package eu.ewall.platform.reasoner.activitycoach.service.pamm.content;

import java.util.Random;

import org.joda.time.DateTime;
import org.slf4j.Logger;

import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.message.PhysicalActivityMotivationalMessage;
import eu.ewall.platform.idss.service.model.common.message.content.ArgumentContent;
import eu.ewall.platform.idss.service.model.common.message.content.FeedbackContent;
import eu.ewall.platform.idss.service.model.common.message.content.GreetingContent;
import eu.ewall.platform.idss.service.model.common.message.content.MessageContent;
import eu.ewall.platform.idss.service.model.common.message.content.ReinforcementContent;
import eu.ewall.platform.idss.service.model.common.message.content.SuggestionContent;
import eu.ewall.platform.idss.service.model.common.message.intention.MessageIntention;
import eu.ewall.platform.idss.service.model.common.message.timing.MessageTiming;
import eu.ewall.platform.idss.service.model.type.ActivityUnit;
import eu.ewall.platform.idss.service.model.type.PrimaryIntention;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.reasoner.activitycoach.service.MessageHistory;
import eu.ewall.platform.reasoner.activitycoach.service.VCConfiguration;
import eu.ewall.platform.reasoner.activitycoach.service.VCDataCollection;

/**
 * Used to generate the "Content" part of a physical activity motivational message. When calling
 * the function {@link MessageContentGenerator#generateMessageContent()} the function will attach
 * the message content to the given {@link PhysicalActivityMotivationalMessage}.
 * 
 * @author Harm op den Akker, RRD
 */
public class MessageContentGenerator {
	private static final String LOGTAG = "MessageContentGenerator";
	
	private VCDataCollection vcDataCollection;
	private IDSSUserProfile idssUserProfile;
	private VCConfiguration config;
	
	private Logger logger;
	private String userLogString;
	private Random random = new Random();
	
	private static final double PROBABILITY_USEFIRSTNAME = 0.5;
	private static final double PROBABILITY_ISTIMEBASED = 0.75;
	private static final double PROBABILITY_ACTIVITY_UNIT_STEPS = 0.75;	
	
	/**
	 * Creates an instance of a {@link MessageContentGenerator} for a specified
	 * {@link IDSSUserProfile} and the data collection represented by the
	 * {@link VCDataCollection}.
	 * @param idssUserProfile the user for which to initiate the {@link MessageContentGenerator}.
	 * @param acdc the activity coach data collection
	 */
	public MessageContentGenerator(IDSSUserProfile idssUserProfile, VCDataCollection vcDataCollection, VCConfiguration config) {
		this.vcDataCollection = vcDataCollection;
		this.idssUserProfile = idssUserProfile;
		this.config = config;
		logger = AppComponents.getLogger(MessageContentGenerator.LOGTAG);
		userLogString = "["+idssUserProfile.getUsername()+"] ";
	}
	
	/**
	 * Generate the {@link MessageContent} for the given {@link PhysicalActivityMotivationalMessage}.
	 * @param message the {@link PhysicalActivityMotivationalMessage} for which to generate the {@link MessageContent}.
	 * @return {@code true} if a {@link MessageContent} was generated successfully, {@code false} otherwise.
	 */
	public boolean generateMessageContent(PhysicalActivityMotivationalMessage message) {
		MessageIntention messageIntention = message.getIntention();

		MessageContent messageContent = new MessageContent();
		message.setContent(messageContent);
		
		boolean success = false;
		
		if(messageIntention.hasGreetingIntention()) {
			determineGreetingContent(message);
			success = true;
		}
		
		if(messageIntention.hasFeedbackIntention()) {
			determineFeedbackContent(message);
			success = true;
		}
		
		if(messageIntention.hasReinforcementIntention()) {
			determineReinforcementContent(message);
			success = true;
		}
		
		if(messageIntention.hasArgumentIntention()) {
			determineArgumentContent(message);
			success = true;
		}
		
		if(messageIntention.hasSuggestionIntention()) {
			determineSuggestionContent(message);
			success = true;
		}
		
		if(messageIntention.hasSynchronizeSensorIntention()) {
			determineSynchronizeSensorContent(message);
			success = true;
		}
		
		return success;
	}
	
	/**
	 * Attaches a {@link GreetingContent} to the given {@link PhysicalActivityMotivationalMessage}.
	 * @param message the {@link PhysicalActivityMotivationalMessage} to attach the content to.
	 */
	private void determineGreetingContent(PhysicalActivityMotivationalMessage message) {
		GreetingContent greetingContent = new GreetingContent();
		
		// Decide on tailoring to the time of day (day part)
		if(random.nextDouble() <= PROBABILITY_ISTIMEBASED) {
			greetingContent.setIsTimeBased(true);
		} else {
			greetingContent.setIsTimeBased(false);
		}
		
		// Decide on tailoring to use the first name
		boolean useFirstName = false;
		if(vcDataCollection.getFirstName() != null) {
			useFirstName = (random.nextDouble() <= PROBABILITY_USEFIRSTNAME);
		}
		if(useFirstName) {
			greetingContent.setUseFirstName(true);
		} else {
			greetingContent.setUseFirstName(false);
		}
		message.getContent().setGreetingContent(greetingContent);
	}
	
	/**
	 * Attaches a {@link FeedbackContent} to the given {@link PhysicalActivityMotivationalMessage}.
	 * @param message the {@link PhysicalActivityMotivationalMessage} to attach the content to.
	 */
	private void determineFeedbackContent(PhysicalActivityMotivationalMessage message) {
		FeedbackContent feedbackContent = new FeedbackContent();
		MessageHistory history = vcDataCollection.getMessageHistory();
		
		// ------ Decide whether or not to include goal setting in the feedback content
		// ------ Probability of including goal setting increases based on how long ago
		// ------ goal setting information was provided to the user. If more than 120
		// ------ minutes ago, goal setting will be provided with 100% probability.
		int elapsedTimeSinceGoalInfo = 1440; // If no message with goal setting today, this is set to 24 hours (1440 minutes) ago.
		double timeFactor = 0.0;
		PhysicalActivityMotivationalMessage lastMessageWithGoalSetting = history.lastObservedMessageWithGoalSetting();
		if(lastMessageWithGoalSetting == null) {
			feedbackContent.setHasGoalSetting(true);
			logger.info(userLogString+"Adding goal-setting to FeedbackContent (no message with goal setting information received so far today).");
		} else {		
			elapsedTimeSinceGoalInfo = history.minutesAgoObserved(lastMessageWithGoalSetting);
			if(elapsedTimeSinceGoalInfo > 120) {
				timeFactor = 1.0;
			} else {
				timeFactor = (double)elapsedTimeSinceGoalInfo / 120.0;
			}
			
			random = new Random();
			if(timeFactor > random.nextDouble()) {
				feedbackContent.setHasGoalSetting(true);
				logger.info(userLogString+"Adding goal-setting to FeedbackContent (with probability "+Math.round(timeFactor*100.0)+"%).");
			} else {
				feedbackContent.setHasGoalSetting(false);
				logger.info(userLogString+"Goal-setting excluded from FeedbackContent (with probability "+(100-Math.round(timeFactor*100.0))+"%).");
			}			
		}
		
		// ----- Set the 'isUnderGoal' or 'isOverGoal' parameter (or both false if user is in range of goal).
		// ----- Only in case goal-setting is requested in general.
		if(feedbackContent.getHasGoalSetting()) {
			int currentActivitySteps = vcDataCollection.getCurrentActivityState().getSteps();
			int activityGoalSteps = vcDataCollection.getGoalActivityState().getSteps();
			
			if (currentActivitySteps < (activityGoalSteps - (activityGoalSteps * config.getAllowedGoalDeviation()))) {
				feedbackContent.setIsUnderGoal(true);
				logger.info(userLogString+"FeedbackContent set to 'isUnderGoal' ("+currentActivitySteps+") below ("+activityGoalSteps+"-"+activityGoalSteps*config.getAllowedGoalDeviation()+").");
			} else if(currentActivitySteps > (activityGoalSteps + (activityGoalSteps * config.getAllowedGoalDeviation()))) {
				feedbackContent.setIsOverGoal(true);
				logger.info(userLogString+"FeedbackContent set to 'isOverGoal' ("+currentActivitySteps+") above ("+activityGoalSteps+"+"+activityGoalSteps*config.getAllowedGoalDeviation()+").");
			} else {
				feedbackContent.setIsUnderGoal(false);
				feedbackContent.setIsOverGoal(false);
				logger.info(userLogString+"FeedbackContent set to neither 'isOverGoal' nor 'isUnderGoal' ("+currentActivitySteps+") ~ ("+activityGoalSteps+"±"+activityGoalSteps*config.getAllowedGoalDeviation()+").");
			}
		}
		
		// ----- Determine the units of measure to use in the feedback (steps, kilometers, calories)
		// ----- If the user has indicated a preference, use that, otherwise decide here
		ActivityUnit selectedActivityUnit = null;
		if(vcDataCollection.getPreferredActivityUnit() == null) {
			if(random.nextDouble() <= PROBABILITY_ACTIVITY_UNIT_STEPS) {
				selectedActivityUnit = ActivityUnit.STEPS;
				logger.info(userLogString+"Selected activity unit: STEPS.");
			} else {
				selectedActivityUnit = ActivityUnit.DISTANCE;
				logger.info(userLogString+"Selected activity unit: DISTANCE");
				// TODO: When including burned calories, do this 50/50 switch between calories and distance.
				//if(random.nextDouble() <= 0.5) {
				//	selectedActivityUnit = ActivityUnit.CALORIES;
				//	logger.info(userLogString+"Selected activity unit: CALORIES.");
				//} else {
				//	selectedActivityUnit = ActivityUnit.DISTANCE;
				//	logger.info(userLogString+"Selected activity unit: DISTANCE.");
				//}
			}
		} else {
			// If the user has a preference for activity units, use that
			selectedActivityUnit = vcDataCollection.getPreferredActivityUnit();
		}		
		feedbackContent.setActivityUnit(selectedActivityUnit);		
		message.getContent().setFeedbackContent(feedbackContent);
	}
	
	/**
	 * Attaches a {@link ReinforcementContent} to the given {@link PhysicalActivityMotivationalMessage}.
	 * @param message the {@link PhysicalActivityMotivationalMessage} to attach the content to.
	 */
	private void determineReinforcementContent(PhysicalActivityMotivationalMessage message) {
		ReinforcementContent reinforcementContent = new ReinforcementContent();
		
		// ----- Set the 'reinforcement intention' parameter (based on MessageIntention).
		// ----- A "reinforcement intention" of neutral, means that the message's primary
		// ----- intention is 'neutral', i.e. that the user is in range of his goal, and is doing
		// ----- well. This means that a neutral reinforcementIntention would translate to
		// ----- e.g. "Well done!".
		reinforcementContent.setReinforcementIntention(message.getIntention().getPrimaryIntention());
		logger.info(userLogString+"Set 'reinforcementIntention': "+message.getIntention().getPrimaryIntention());
		
		// ----- Set the 'close to goal' parameter, in case of a discouraging or encouraging
		// ----- primary intention / reinforcement intention (these are equal). The question is
		// ----- how far from achieving the goal the user is. If 'closeToGoal' is set to true,
		// ----- the encouragement could be e.g. "You're almost there!" (encouraging+closeToGoal),
		// ----- or "Just relax a bit!" (discouraging+closeToGoal).
		if(!(reinforcementContent.getReinforcementIntention().equals(PrimaryIntention.NEUTRAL))) {
			MessageTiming timing = message.getTiming();
			DateTime now = timing.getUserNotifiedTime().withZone(idssUserProfile.getTimeZone()); //TODO: This should be userObservedTime!
			
			// TODO: Set these values based on the "wake up" and "to bed" times from sleep-monitor
			String userDayStartTime = config.getDefaultDayStartTime();
			String userDayEndTime = config.getDefaultDayEndTime();
			
			int currentSteps = vcDataCollection.getCurrentActivityState().getSteps();
			int expectedSteps = vcDataCollection.getCurrentExpectedSteps(now,userDayStartTime,userDayEndTime);
			int reasonableStepsDeviation = (int) (expectedSteps * config.getExtremeGoalDeviation());
			
			if(Math.abs(expectedSteps - currentSteps) <= reasonableStepsDeviation) {
				// Deviation from goal is still reasonable
				reinforcementContent.setCloseToGoal(true);
				logger.info(userLogString+"Set reinforcement 'closeToGoal': true.");
			} else {
				reinforcementContent.setCloseToGoal(false);
				logger.info(userLogString+"Set reinforcement 'closeToGoal': false.");
			}
		}
		
		message.getContent().setReinforcementContent(reinforcementContent);
	}
	
	/**
	 * Attaches a {@link ArgumentContent} to the given {@link PhysicalActivityMotivationalMessage}.
	 * @param message the {@link PhysicalActivityMotivationalMessage} to attach the content to.
	 */
	private void determineArgumentContent(PhysicalActivityMotivationalMessage message) {
		ArgumentContent argumentContent = new ArgumentContent();
		
		message.getContent().setArgumentContent(argumentContent);
	}
	
	/**
	 * Attaches a {@link SuggestionContent} to the given {@link PhysicalActivityMotivationalMessage}.
	 * @param message the {@link PhysicalActivityMotivationalMessage} to attach the content to.
	 */
	private void determineSuggestionContent(PhysicalActivityMotivationalMessage message) {
		
	}
	
	/**
	 * Attaches a {@link SynchronizeSensorContent} to the given {@link PhysicalActivityMotivationalMessage}.
	 * @param message the {@link PhysicalActivityMotivationalMessage} to attach the content to.
	 */
	private void determineSynchronizeSensorContent(PhysicalActivityMotivationalMessage message) {
		
	}
	
}
