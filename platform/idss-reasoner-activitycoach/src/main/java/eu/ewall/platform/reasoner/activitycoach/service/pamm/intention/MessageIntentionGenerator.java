package eu.ewall.platform.reasoner.activitycoach.service.pamm.intention;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joda.time.DateTime;
import org.slf4j.Logger;

import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.message.PhysicalActivityMotivationalMessage;
import eu.ewall.platform.idss.service.model.common.message.intention.MessageIntention;
import eu.ewall.platform.idss.service.model.common.message.timing.MessageTiming;
import eu.ewall.platform.idss.service.model.state.domain.ActivityState;
import eu.ewall.platform.idss.service.model.type.GoalIntention;
import eu.ewall.platform.idss.service.model.type.PrimaryIntention;
import eu.ewall.platform.idss.service.model.type.SecondaryIntention;
import eu.ewall.platform.idss.service.model.type.StageOfChange;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.idss.utils.datetime.VirtualClock;
import eu.ewall.platform.reasoner.activitycoach.service.VCConfiguration;
import eu.ewall.platform.reasoner.activitycoach.service.VCDataCollection;

/**
 * The message intention generator can add a {@link MessageIntention
 * MessageIntention} component to a {@link PhysicalActivityMotivationalMessage}.
 * 
 * @author Dennis Hofs (RRD)
 * @author Harm op den Akker (RRD)
 */
public class MessageIntentionGenerator {
	private static final String LOGTAG = "MessageIntentionGenerator";
	private IDSSUserProfile idssUserProfile;
	private VCDataCollection vcDataCollection;
	private VCConfiguration config;
	private Logger logger;
	private String userLogString = null;
	
	private static double PROBABILITY_GREETING = 0.5;
	
	/**
	 * Constructs a new message intention generator.
	 * 
	 * @param user the user
	 * @param state the current state
	 */
	public MessageIntentionGenerator(IDSSUserProfile idssUserProfile, VCDataCollection vcDataCollection, VCConfiguration config) {
		this.idssUserProfile = idssUserProfile;
		this.vcDataCollection = vcDataCollection;
		this.config = config;
		logger = AppComponents.getLogger(MessageIntentionGenerator.LOGTAG);
		userLogString = "["+idssUserProfile.getUsername()+"] ";
	}
	
	/**
	 * Adds a message intention to the specified message. It determines the
	 * primary intention from the physical activity state model attributes "goalActivityState",
	 * "currentActivityState" and "goalIntention". If there is no current activity state
	 * or activity goal state known, the message sets the "SYNCHRONIZE" primary intention with
	 * not secondary intentions, and return {@code true}.
	 * 
	 * @param message the message
	 * @return true if the message intention was generated, false otherwise (never happens).
	 */
	public boolean generateMessageIntention(PhysicalActivityMotivationalMessage message) {
		MessageTiming timing = message.getTiming();
		
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		MessageIntention intention = new MessageIntention();
		
		// -----
		// ----- Set Primary Intention
		// -----
		ActivityState currentActivityState = null;
		ActivityState goalActivityState = null;
		
		currentActivityState = vcDataCollection.getCurrentActivityState();
		if(currentActivityState == null) {
			logger.warn(userLogString+"No physical activity data available, primary intention set to WARNING.");
			intention.setPrimaryIntention(PrimaryIntention.WARNING);
		}
		
		// Continue getting goal information is primary intention is not yet set.
		if(intention.getPrimaryIntention() == null) {
			goalActivityState = vcDataCollection.getGoalActivityState();
			if(goalActivityState == null) {
				logger.warn(userLogString+"No physical activity goal data available, primary intention set to WARNING.");
				intention.setPrimaryIntention(PrimaryIntention.WARNING);
			}
		}
		
		// Continue calculating activity and goal steps if primary intention is not yet set.
		Integer activity = null;
		Integer goal = null;
		if(intention.getPrimaryIntention() == null) {
			activity = currentActivityState.getSteps();
			goal = goalActivityState.getSteps();
			
			if (activity == null || goal == null) {
				logger.warn(userLogString+"No activity ("+activity+") or goal ("+goal+") data known, primary intention set to WARNING.");
				intention.setPrimaryIntention(PrimaryIntention.WARNING);
			} else if(activity == 0) {
				logger.warn(userLogString+"No activity recorded yet today ("+activity+"/"+goal+"), primary intention set to WARNING.");
				intention.setPrimaryIntention(PrimaryIntention.WARNING);
			}
		}
		
		// Continue defining primary intention (encourage, neutral or discourage) if not yet set.
		if(intention.getPrimaryIntention() == null) {
			// TODO: Set these values based on the "wake up" and "to bed" times from sleep-monitor
			String userDayStartTime = config.getDefaultDayStartTime();
			String userDayEndTime = config.getDefaultDayEndTime();
			
			Integer expectedActivity = vcDataCollection.getCurrentExpectedSteps(now,userDayStartTime,userDayEndTime);
			
			// Get goal intention
			
			GoalIntention goalIntention = vcDataCollection.getGoalIntention();
							
			if (goalIntention == null) {
				goalIntention = GoalIntention.INCREASE;
				logger.info(userLogString+"Goal intention set to '"+goalIntention+"' (default).");
			} else {
				logger.info(userLogString+"Goal intention set to: '"+goalIntention+"' (from user model).");
			}
			
			// Define primary intention
			
			int allowedActivityDeviation = (int) (expectedActivity * config.getAllowedGoalDeviation());
			
			if (activity < (expectedActivity - allowedActivityDeviation)) {
				intention.setPrimaryIntention(PrimaryIntention.ENCOURAGE);
				
				logger.info(userLogString+"Activity ("+activity+") below expected ("+expectedActivity+"±"+allowedActivityDeviation+"), primary intention set to '"+intention.getPrimaryIntention()+"'.");
			
				
			} else if(activity > (expectedActivity + allowedActivityDeviation)) {
						
				if(goalIntention == GoalIntention.BALANCE) {
					intention.setPrimaryIntention(PrimaryIntention.DISCOURAGE);
				} else {
					intention.setPrimaryIntention(PrimaryIntention.NEUTRAL);
				}
				
				logger.info(userLogString+"Activity ("+activity+") above expected ("+expectedActivity+"±"+allowedActivityDeviation+"), primary intention set to '"+intention.getPrimaryIntention()+"'.");	
	
			} else {
				intention.setPrimaryIntention(PrimaryIntention.NEUTRAL);
				logger.info(userLogString+"Activity ("+activity+") within range of ("+expectedActivity+"±"+allowedActivityDeviation+"), primary intention set to '"+intention.getPrimaryIntention()+"'.");
			}
		}
		
		// -----
		// ----- Set Secondary Intentions
		// -----
		
		List<SecondaryIntention> secondaryIntentions = new ArrayList<SecondaryIntention>();
		
		// In case of "user initiated timing", the secondary intention(s) requested should be honored
		if (!timing.isSystemInitiated()) {
			
			if(timing.isRequireGreeting()) {
				secondaryIntentions.add(SecondaryIntention.GREETING);
				logger.info(userLogString+"Secondary intention 'greeting' enabled (requested by user).");
			}
			
			if (timing.isRequireFeedback()) {
				secondaryIntentions.add(SecondaryIntention.FEEDBACK);
				logger.info(userLogString+"Secondary intention 'feedback' enabled (requested by user).");
			}
			
			if (timing.isRequireArgument()) {
				secondaryIntentions.add(SecondaryIntention.ARGUMENT);
				logger.info(userLogString+"Secondary intention 'argument' enabled (requested by user).");
			}
			
			if (timing.isRequireSuggestion()) {
				secondaryIntentions.add(SecondaryIntention.SUGGESTION);
				logger.info(userLogString+"Secondary intention 'suggestion' enabled (requested by user).");
			}
			
			if(timing.isRequireReinforcement()) {
				secondaryIntentions.add(SecondaryIntention.REINFORCEMENT);
				logger.info(userLogString+"Secondary intention 'reinforcement' enabled (requested by user).");
			}
			
			if(timing.isRequireSynchronizeSensor()) {
				secondaryIntentions.add(SecondaryIntention.SYNCHRONIZE_SENSOR);
				logger.info(userLogString+"Secondary intention 'synchronizeSensor' enabled (requested by user).");
			}
			
			// If no secondary intentions are requested, enable FEEDBACK to avoid empty response
			if(secondaryIntentions.isEmpty()) {
				secondaryIntentions.add(SecondaryIntention.FEEDBACK);
				logger.info(userLogString+"Secondary intention 'feedback' enabled (to avoid empty secondary intentions).");
			}
		
		// In case of "system initiated timing", we can choose the most appropriate secondary intention(s)
		} else {
			Random random = new Random();			
			
			// Decide on "greeting" secondary intention.
			// TODO: Add smarter, more context aware decision on adding a greeting.
			if(random.nextDouble() <= PROBABILITY_GREETING) {
				secondaryIntentions.add(SecondaryIntention.GREETING);
			}
			
			if(intention.getPrimaryIntention().equals(PrimaryIntention.WARNING)) {
				if(activity == null) {
					secondaryIntentions.add(SecondaryIntention.SYNCHRONIZE_SENSOR);
				} else if(activity == 0) {
					secondaryIntentions.add(SecondaryIntention.SYNCHRONIZE_SENSOR);
				}
			
			// For encouraging, discouraging, and neutral primary intentions:
			} else {
			
				// Decide on "feedback" secondary intention.
				// TODO: Add smarter decision rule on selecting feedback intention (for now: always).
				secondaryIntentions.add(SecondaryIntention.FEEDBACK);
				
				// Decide on "reinforcement" secondary intention.
				// Only makes sense after receiving also feedback
				// Influenced by the user's self efficacy towards physical activity.
				if(secondaryIntentions.contains(SecondaryIntention.FEEDBACK)) {
					
					double probabilityReinforcement;
					Double physicalActivitySelfEfficacy = vcDataCollection.getPhysicalActivitySelfEfficacy();
					
					if(physicalActivitySelfEfficacy != null) {
						probabilityReinforcement = 1.0d - physicalActivitySelfEfficacy;
						logger.info(userLogString+" Probability of reinforcement set to "+probabilityReinforcement+" (self efficacy: "+physicalActivitySelfEfficacy+").");
						 
					} else {
						probabilityReinforcement = 0.5;
						logger.info(userLogString+" Probability of reinforcement set to "+probabilityReinforcement+" (self efficacy: unknown).");
					}
					
					if(random.nextDouble() <= probabilityReinforcement) {
						secondaryIntentions.add(SecondaryIntention.REINFORCEMENT);
					}
				}
				
				// Decide on argument selection, based on user's stage of change in
				// physical activity
				double probabilityArgument = 0.5;
				
				StageOfChange physicalActivityStageOfChange = vcDataCollection.getPhysicalActivityStageOfChange();
				if(physicalActivityStageOfChange != null) {
					if(physicalActivityStageOfChange == StageOfChange.PRECONTEMPLATION) {
						// In the pre-contemplation phase, arguments probability is highest.
						probabilityArgument = 0.8;
					} else if(physicalActivityStageOfChange == StageOfChange.CONTEMPLATION) {
						// In the contemplation phase, send arguments fairly often.
						probabilityArgument = 0.7;
					} else if(physicalActivityStageOfChange == StageOfChange.PREPARATION) {
						// In the preparation phase, send arguments similar as per default (50/50).
						probabilityArgument = 0.5;
					} else if(physicalActivityStageOfChange == StageOfChange.ACTION) {
						// In the action phase, arguments should be very rare.
						probabilityArgument = 0.1;
					} else if(physicalActivityStageOfChange == StageOfChange.MAINTENANCE) {
						// In the maintenance phase, arguments for physical activity are not needed.
						probabilityArgument = 0.0;
					}
				}
				
				if(random.nextDouble() < probabilityArgument) {
					secondaryIntentions.add(SecondaryIntention.ARGUMENT);
				}
			
				// TODO: Suggestion-Lifestyle
				
				// TODO: Suggestion-Activity
			
			} // End encouraging, discouraging, and neutral block
			
		} // End system-initiated timing block
		intention.setSecondaryIntentions(secondaryIntentions);
		message.setIntention(intention);
		return true;
	}
}
