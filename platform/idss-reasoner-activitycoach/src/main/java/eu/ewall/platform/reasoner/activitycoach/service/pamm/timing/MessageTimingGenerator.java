package eu.ewall.platform.reasoner.activitycoach.service.pamm.timing;

import java.util.List;
import java.util.Random;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.message.PhysicalActivityMotivationalMessage;
import eu.ewall.platform.idss.service.model.common.message.timing.MessageTiming;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.idss.utils.datetime.VirtualClock;
import eu.ewall.platform.reasoner.activitycoach.service.VCDataCollection;
import eu.ewall.platform.reasoner.activitycoach.service.VCConfiguration;
import eu.ewall.platform.reasoner.activitycoach.service.MessageReader;

/**
 * The message timing generator can decide whether a system-initiated
 * {@link PhysicalActivityMotivationalMessage MotivationalMessage} should be generated in the
 * current state.
 * 
 * <p>When a decision is made to generate a system-initiated or user-initiated
 * message, this class can add the {@link MessageTiming MessageTiming}
 * component to the message.</p>
 * 
 * @author Dennis Hofs (RRD)
 * @author Harm op den Akker (RRD)
 */
public class MessageTimingGenerator {
	private static final String LOGTAG = "MessageTimingGenerator";
	private IDSSUserProfile idssUserProfile;
	private VCDataCollection vcDataCollection;
	private VCConfiguration config;
	private Logger logger;
	private String userLogString = null;
	
	/**
	 * Constructs a new message timing generator.
	 * 
	 * @param idssUserProfile the IDSS User Profile.
	 * @param state the current data collection
	 */
	public MessageTimingGenerator(IDSSUserProfile idssUserProfile, VCDataCollection vcDataCollection, VCConfiguration config) {
		this.idssUserProfile = idssUserProfile;
		this.vcDataCollection = vcDataCollection;
		this.config = config;
		ILoggerFactory logFactory = AppComponents.getInstance().getComponent(ILoggerFactory.class);
		logger = logFactory.getLogger(MessageTimingGenerator.LOGTAG);
		userLogString = "["+idssUserProfile.getUsername()+"] ";
	}
	
	/**
	 * A Smart(TM) algorithm for deciding whether or not a physical activity coaching
	 * message should be generated. Returns {@code true} if so, {@code false} otherwise.
	 * @return {@code true} if a message should be generated, {@code false} otherwise.
	 */
	private boolean getSmartTimingDecision() {
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		LocalTime localTime = now.toLocalTime();
		DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm:ss");
		
		// TODO: Set these values based on the "wake up" and "to bed" times from sleep-monitor
		String userDayStartTime = config.getDefaultDayStartTime();
		String userDayEndTime = config.getDefaultDayEndTime();
		
		LocalTime startTime = timeFormatter.parseLocalTime(userDayStartTime);
		LocalTime endTime = timeFormatter.parseLocalTime(userDayEndTime);
		
		// If the current time is outside the bounds of "day-time", don't generate messages.
		if (localTime.isBefore(startTime) || localTime.isAfter(endTime)) {
			logger.info(userLogString+"SmartTiming decided NOT to generate message (current time out of bounds ["+userDayStartTime+" - "+userDayEndTime+"]).");
			return false;
		}
		
		// TODO: Implement intelligent timing algorithm here.
		// Factors influencing chance of message generation:
		// * User location (@HOME = High, @OTHER = Low)
		// * Time since previous activity notification (increasing) (DONE)
		// * Distance from goal (larger distance = higher chance)
		// * Current weather (?)
				
		/*
		ContextModel contextModel = state.getContextModel();
		if(contextModel != null) {
			Location userLocation = contextModel.getLocation();
			if(userLocation != null) {
				LocationType userLocationType = userLocation.getType();
				if(userLocationType == LocationType.HOME) {
					logger.info("User is at home.");
				} else {
					logger.info("User is not at home.");
				}
			}	
		}
		*/
		
		// Calculate timeFactor (linear value between 0..1 for 0..120 minutes since last observed message).
		double timeFactor = 0.0;
		List<PhysicalActivityMotivationalMessage> observedHistoryToday = vcDataCollection.getMessageHistory().getObservedHistoryToday();
		if (observedHistoryToday.isEmpty()) {
			timeFactor = 1.0;
		} else {
			PhysicalActivityMotivationalMessage lastObservedMessage = observedHistoryToday.get(observedHistoryToday.size()-1);
			int elapsedTimeSinceObserved = vcDataCollection.getMessageHistory().minutesAgoObserved(lastObservedMessage);
			if(elapsedTimeSinceObserved > 120) {
				timeFactor = 1.0;
			} else {
				timeFactor = (double)elapsedTimeSinceObserved / 120.0;
			}
		}
		
		Random random = new Random();
		if(timeFactor > random.nextDouble()) {
			logger.info(userLogString+"SmartTiming decided to generate message (timeFactor: "+timeFactor+").");
			return true;			
		} else {
			logger.info(userLogString+"SmartTiming decided NOT to generate message (timeFactor: "+timeFactor+").");
			return false;
		}
	}
	
	/**
	 * Returns {@code true} if the last message generated needed was more than 
	 * {@link VCConfiguration.MESSAGE_INTERVAL_MINUTES} ago.
	 * @return {@code true} if a message should be generated, {@code false} otherwise.
	 */
	private boolean getFixedTimingDecision() {
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		List<PhysicalActivityMotivationalMessage> history = vcDataCollection.getMessageHistory().getHistory();
		if (history.isEmpty())
			return true;
		DateTime msgTime = MessageReader.getTime(history.get(history.size() - 1));
		if (msgTime == null)
			return true;
		long elapsed = now.getMillis() - msgTime.getMillis();
		int minutesSinceLastMessage = (int) (elapsed / 60000.0);
						
		if(minutesSinceLastMessage >= config.getFixedTimingPAMMIntervalMinutes()) {
			logger.info(userLogString+"FixedTiming decided to generate message (last message "+minutesSinceLastMessage+" minutes ago).");
			return true;
		} else {
			return false;
		}		
	}
	
	/**
	 * Decides whether a system-initiated message should be generated for the
	 * current user in the current state.
	 * 
	 * @return true if a message should be generated, false otherwise.
	 */
	public boolean checkSystemInitiated() {
		if(config.getSmartTimingEnabled()) {
			return getSmartTimingDecision();
		} else {
			return getFixedTimingDecision();
		}
	}
	
	/**
	 * Adds a system-initiated {@link MessageTiming MessageTiming} component to
	 * the specified message; setting the creation time to {@code now}, and the
	 * {@link MessageTiming#isSystemInitiated()} to {@code true}.
	 * 
	 * @param message the {@link PhysicalActivityMotivationalMessage} for which to generate the 
	 * {@link MessageTiming} object.
	 */
	public void generateSystemInitiated(PhysicalActivityMotivationalMessage message) {
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		MessageTiming timing = new MessageTiming();
		timing.setSystemInitiated(true);
		timing.setUserNotifiedTime(now);
		DateTime expirationTime = now.plusMinutes(config.getPAMMExpirationMinutes());
		timing.setExpirationTime(expirationTime);
		message.setTiming(timing);
	}
	
	/**
	 * Adds a user-initiated {@link MessageTiming MessageTiming} component to
	 * the specified message.
	 * 
	 * @param message the {@link PhysicalActivityMotivationalMessage} for which to generate the 
	 * {@link MessageTiming} object.
	 * @param requireFeedback {@code true} if the user wants a feedback intention from the message.
	 * @param requireArgument {@code true} if the user wants an argument intention from the message.
	 * @param requireSuggestion {@code true} if the user wants a suggestion intention from the message.
	 * @param requireReinforcement {@code true} if the user wants a reinforcement intention from the message.
	 */
	public void generateUserInitiated(PhysicalActivityMotivationalMessage message,
			boolean requireGreeting, boolean requireFeedback, boolean requireArgument,
			boolean requireSuggestion, boolean requireReinforcement) {
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		DateTime expirationTime = now.plusMinutes(config.getPAMMExpirationMinutes());
		
		MessageTiming timing = new MessageTiming();
		timing.setSystemInitiated(false);
		timing.setUserNotifiedTime(now);
		timing.setExpirationTime(expirationTime);
		timing.setRequireGreeting(requireGreeting);
		timing.setRequireFeedback(requireFeedback);
		timing.setRequireArgument(requireArgument);
		timing.setRequireSuggestion(requireSuggestion);
		timing.setRequireReinforcement(requireReinforcement);
		message.setTiming(timing);
	}
}