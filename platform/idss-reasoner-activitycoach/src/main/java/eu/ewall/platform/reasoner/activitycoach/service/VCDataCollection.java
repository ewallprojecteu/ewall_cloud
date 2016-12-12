package eu.ewall.platform.reasoner.activitycoach.service;

import java.util.List;
import java.util.StringTokenizer;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.message.PhysicalActivityMotivationalMessage;
import eu.ewall.platform.idss.service.model.state.StateModelAccessPoint;
import eu.ewall.platform.idss.service.model.state.context.ContextModel;
import eu.ewall.platform.idss.service.model.state.domain.ActivityState;
import eu.ewall.platform.idss.service.model.state.domain.PhysicalActivityStateModel;
import eu.ewall.platform.idss.service.model.state.interaction.InteractionModel;
import eu.ewall.platform.idss.service.model.state.user.DailyRoutine;
import eu.ewall.platform.idss.service.model.state.user.UserModel;
import eu.ewall.platform.idss.service.model.state.user.WeeklyRoutine;
import eu.ewall.platform.idss.service.model.type.ActivityUnit;
import eu.ewall.platform.idss.service.model.type.GoalIntention;
import eu.ewall.platform.idss.service.model.type.StageOfChange;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.idss.utils.datetime.VirtualClock;
import eu.ewall.platform.idss.utils.i18n.I18nResourceFinder;
import eu.ewall.platform.reasoner.activitycoach.service.messages.MessageDatabase;
import eu.ewall.platform.reasoner.activitycoach.service.messages.MessageDatabaseReader;

/**
 * Repository of state models and other user related data that can be used
 * during the generation of messages or dialogues.
 * 
 * @author Dennis Hofs (Roessingh Research and Development)
 * @author Harm op den Akker (Roessingh Research and Development)
 */
public class VCDataCollection {
	private static final String LOGTAG = "VCDataCollection";
	
	private IDSSUserProfile idssUserProfile;
	private String username;
	private StateModelAccessPoint stateModelAccessPoint;
	private VCConfiguration config;
	private Database database;
	
	private Logger logger;
	
	private UserModel userModel;
	private ContextModel contextModel;
	private InteractionModel interactionModel;
	private PhysicalActivityStateModel physicalActivityStateModel;
	private MessageHistory messageHistory;
	private MessageDatabase messageDatabase;
	
	private DateTime messageHistoryRetrievedTime;
	
	private int ATTRIBUTE_UPDATE_TIME;
	
	// ---------- Constructors ---------- //
	
	/**
	 * Creates an instance of a {@link VCDataCollection} for the given username.
	 * @param idssUserProfile the {@link IDSSUserProfile) of the user for which this {@link VCDataCollection} holds data.
	 */
	public VCDataCollection(IDSSUserProfile idssUserProfile, StateModelAccessPoint stateModelAccessPoint, Database database, VCConfiguration config) {
		this.idssUserProfile = idssUserProfile;
		this.username = idssUserProfile.getUsername();
		this.stateModelAccessPoint = stateModelAccessPoint;
		this.config = config;
		this.database = database;
		
		this.ATTRIBUTE_UPDATE_TIME = config.getAttributeUpdateTime();
		
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		logger = AppComponents.getLogger(LOGTAG);
		
		try {
			this.userModel = stateModelAccessPoint.readUserModel(username);
		} catch(Exception e) {
			logger.error("["+username+"] Unable to initialize User Model through IDSS Core.");
		}
		
		try {
			this.contextModel = stateModelAccessPoint.readContextModel(username);
		} catch(Exception e) {
			logger.error("["+username+"] Unable to initialize Context Model through IDSS Core.");
		}
		
		try {
			this.physicalActivityStateModel = stateModelAccessPoint.readPhysicalActivityStateModel(username);
		} catch(Exception e) {
			logger.error("["+username+"] Unable to initialize Physical Activity State Model through IDSS Core.");
		}
		
		try {
			this.interactionModel = stateModelAccessPoint.readInteractionModel(username);
		} catch(Exception e) {
			logger.error("["+username+"] Unable to initialize Interaction Model through IDSS Core.");
		}
		
		try {
			this.messageHistory = MessageHistoryReader.readMessageHistory(database, idssUserProfile);
			this.messageHistoryRetrievedTime = now;
		} catch (Exception e) {
			logger.error("["+username+"] Unable to initialize Message History from database.");
		}
		
		MessageDatabaseReader reader = new MessageDatabaseReader();
		I18nResourceFinder finder = new I18nResourceFinder("messages");
		finder.setExtension("dsl");
		finder.setLoadClass(reader.getClass());
		finder.setUserLocales(idssUserProfile.getLocales());
		if (!finder.find()) {
			logger.error("["+username+"] Resource messages.dsl not found.");
		} else {
			try {
				this.messageDatabase = reader.read(finder.getUrl(), this);
			} catch (Exception e) {
				logger.error("["+username+"] Unable to initialize Message Database from message.dsl file.");
			}
		}
	}
	
	// ---------- Getters ---------- //
	
	/**
	 * Returns the {@link IDSSUserProfile} associated with this {@link VCDataCollection}.
	 * @return the {@link IDSSUserProfile} associated with this {@link VCDataCollection}.
	 */
	public IDSSUserProfile getIDSSUserProfile() {
		return idssUserProfile;
	}
	
	/**
	 * Returns the username associated with this {@link VCDataCollection}.
	 * @return the username associated with this {@link VCDataCollection}.
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Returns an instance of a {@link StateModelAccessPoint} associated with this {@link VCDataCollection}.
	 * @return an instance of a {@link StateModelAccessPoint} associated with this {@link VCDataCollection}.
	 */
	public StateModelAccessPoint getStateModelAccessPoint() {
		return stateModelAccessPoint;
	}
	
	/**
	 * Returns the user model.
	 * @return the user model
	 */
	public UserModel getUserModel() {
		return userModel;
	}
	
	/**
	 * Returns the context model.
	 * @return the context model
	 */
	public ContextModel getContextModel() {
		return contextModel;
	}
	
	/**
	 * Returns the interaction model.
	 * @return the interaction model
	 */
	public InteractionModel getInteractionModel() {
		return interactionModel;
	}
	
	/**
	 * Returns the health domain model for physical activity.
	 * @return the health domain model for physical activity
	 */
	public PhysicalActivityStateModel getPhysicalActivityStateModel() {
		return physicalActivityStateModel;
	}
	
	/**
	 * Returns the history of {@link PhysicalActivityMotivationalMessage}s for this user as a
	 * {@link MessageHistory} object. If the message history has not been updated for a configured
	 * amount of time ({@link VCConfiguration#getAttributeUpdateTime()}) the message history database
	 * will be re-read from the database.
	 * @return the history of {@link PhysicalActivityMotivationalMessage}s for this user.
	 */
	public MessageHistory getMessageHistory() {
		
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		if((messageHistoryRetrievedTime == null) || Minutes.minutesBetween(messageHistoryRetrievedTime, now).getMinutes() > config.getAttributeUpdateTime()) {
			try {
				this.messageHistory = MessageHistoryReader.readMessageHistory(database, idssUserProfile);
				this.messageHistoryRetrievedTime = now;
				logger.info("["+username+"] Refreshed Message History Database.");
			} catch (Exception e) {
				logger.error("["+username+"] Unable to initialize Message History from database.");
			}			
		}		
		return messageHistory;
	}
	
	/**
	 * Returns the message database.
	 * @return the message database
	 */
	public MessageDatabase getMessageDatabase() {
		MessageDatabaseReader reader = new MessageDatabaseReader();
		I18nResourceFinder finder = new I18nResourceFinder("messages");
		finder.setExtension("dsl");
		finder.setLoadClass(reader.getClass());
		finder.setUserLocales(idssUserProfile.getLocales());
		if (!finder.find()) {
			logger.error("["+username+"] Resource messages.dsl not found.");
		} else {
			try {
				this.messageDatabase = reader.read(finder.getUrl(), this);
			} catch (Exception e) {
				logger.error("["+username+"] Unable to initialize Message Database from message.dsl file.");
			}
		}
		return messageDatabase;
	}
	
	// ---------- Setters ---------- //

	/**
	 * Sets the {@link IDSSUserProfile} associated with this {@link VCDataCollection}.
	 * @param idssUserProfile the {@link IDSSUserProfile} associated with this {@link VCDataCollection}.
	 */
	public void setIDSSUserProfile(IDSSUserProfile idssUserProfile) {
		this.idssUserProfile = idssUserProfile;
	}
	
	/**
	 * Sets the username associated with this {@link VCDataCollection}.
	 * @param username the username associated with this {@link VCDataCollection}.
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Sets an instance of a {@link StateModelAccessPoint} associated with this {@link VCDataCollection}.
	 * @param stateModelAccessPoint an instance of a {@link StateModelAccessPoint} associated with this {@link VCDataCollection}.
	 */
	public void setStateModelAccessPoint(StateModelAccessPoint stateModelAccessPoint) {
		this.stateModelAccessPoint = stateModelAccessPoint;
	}
	
	/**
	 * Sets the user model.
	 * @param userModel the user model
	 */
	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}

	/**
	 * Sets the context model.
	 * @param contextModel the context model
	 */
	public void setContextModel(ContextModel contextModel) {
		this.contextModel = contextModel;
	}

	/**
	 * Sets the interaction model.
	 * @param interactionModel the interaction model
	 */
	public void setInteractionModel(InteractionModel interactionModel) {
		this.interactionModel = interactionModel;
	}

	/**
	 * Sets the health domain model for physical activity.
	 * @param physicalActivityStateModel the health domain model for physical activity
	 */
	public void setPhysicalActivityStateModel(PhysicalActivityStateModel physicalActivityStateModel) {
		this.physicalActivityStateModel = physicalActivityStateModel;
	}
	
	/**
	 * Sets the history of {@link PhysicalActivityMotivationalMessage}s for this user.
	 * @param messageHistory the history of {@link PhysicalActivityMotivationalMessage}s for this user.
	 */
	public void setMessageHistory(MessageHistory messageHistory) {
		this.messageHistory = messageHistory;
	}

	/**
	 * Sets the message database.
	 * @param messageDatabase the message database
	 */
	public void setMessageDatabase(MessageDatabase messageDatabase) {
		this.messageDatabase = messageDatabase;
	}
	
	// ---------------------------------------------- //
	// ---------- Data Retrieval Functions ---------- //
	// ---------------------------------------------- //
	
	public List<String> getUnlockedRewards() throws Exception {
		UserModel userModel = stateModelAccessPoint.readUserModel(username, "unlockedRewards");
		return userModel.getUnlockedRewards();
	}
	
	public DateTime getLastInteractionGoodMorning() throws Exception {
		InteractionModel interactionModel = stateModelAccessPoint.readInteractionModel(username);
		return interactionModel.getLastInteractionGoodMorning();
	}
	
	public Integer getWakeUpTimeToday() throws Exception {
		Integer wakeUpTimeToday = null;
		UserModel userModel = stateModelAccessPoint.readUserModel(username, "weeklyRoutine");
		WeeklyRoutine weeklyRoutine = userModel.getWeeklyRoutine();
		if(weeklyRoutine != null) {
			VirtualClock clock = VirtualClock.getInstance();
			DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
			DailyRoutine dailyRoutine = null;
			int currentDayOfWeek = now.getDayOfWeek();
			
			switch(currentDayOfWeek) {
				
				case DateTimeConstants.MONDAY:
					dailyRoutine = weeklyRoutine.getMondayRoutine();
					break;
				
				case DateTimeConstants.TUESDAY:
					dailyRoutine = weeklyRoutine.getTuesdayRoutine();
					break;
					
				case DateTimeConstants.WEDNESDAY:
					dailyRoutine = weeklyRoutine.getWednesdayRoutine();
					break;
				
				case DateTimeConstants.THURSDAY:
					dailyRoutine = weeklyRoutine.getThursdayRoutine();
					break;
					
				case DateTimeConstants.FRIDAY:
					dailyRoutine = weeklyRoutine.getFridayRoutine();
					break;
					
				case DateTimeConstants.SATURDAY:
					dailyRoutine = weeklyRoutine.getSaturdayRoutine();
					break;
					
				case DateTimeConstants.SUNDAY:
					dailyRoutine = weeklyRoutine.getSundayRoutine();
					break;
			}
			
			if(dailyRoutine != null) {
				wakeUpTimeToday = dailyRoutine.getWakeUpTime();
			}

		}
		
		if(wakeUpTimeToday == null) {
			String defaultWakeUpTime = config.getDefaultDayStartTime();
			StringTokenizer st = new StringTokenizer(defaultWakeUpTime,":");
			int hours = new Integer(st.nextToken()).intValue();
			int minutes = new Integer(st.nextToken()).intValue();
			wakeUpTimeToday = (hours * 60) + minutes;
		}
		
		return wakeUpTimeToday;
	}
	
	public boolean requireUpdateStageOfChange() throws Exception {
		PhysicalActivityStateModel physicalActivityStateModel = stateModelAccessPoint.readPhysicalActivityStateModel(username,"stageOfChange");
		long stageOfChangeDecayTime = (long)physicalActivityStateModel.getReliabilityDecayTime("stageOfChange");
		DateTime stageOfChangeUpdateTime = physicalActivityStateModel.getAttributeUpdated("stageOfChange");
		
		if(stageOfChangeUpdateTime == null) {
			// stageOfChange variable unknown, requires update
			return true;
		} else if((stageOfChangeUpdateTime.plus(stageOfChangeDecayTime)).isBefore(DateTime.now())) {
			// stageOfChange decayed, needs update
			return true;
		} else {
			// stageOfChange info is up-to-date, no update required
			return false;
		}
	}
	
	/**
	 * Returns the time until which a PA Stage of Change dialogue should be postponed. The 
	 * information will never be more than ATTRIBUTE_UPDATE_TIME minutes old.
	 * @return the time until which a PA Stage of Change dialogue should be postponed.
	 * @throws Exception if an updated value can not be retrieved through the IDSS Core.
	 */
	public DateTime getPostponePAStageOfChangeReminder() throws Exception {
		String attributeName = "postponePAStageOfChangeReminder";
		DateTime retrieved = interactionModel.getAttributeRetrieved(attributeName);
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		if((retrieved == null) || Minutes.minutesBetween(retrieved, now).getMinutes() > ATTRIBUTE_UPDATE_TIME) {
			this.interactionModel = stateModelAccessPoint.readInteractionModel(username, attributeName);
			this.interactionModel.setAttributeRetrieved(attributeName, now);
		}
		return interactionModel.getPostponePAStageOfChangeReminder();
	}
	
	/**
	 * Returns the time until which sleep application reminders should be postponed. The 
	 * information will never be more than ATTRIBUTE_UPDATE_TIME minutes old.
	 * @return the time until which sleep application reminders should be postponed.
	 * @throws Exception if an updated value can not be retrieved through the IDSS Core.
	 */
	public DateTime getPostponeSleepApplicationReminder() throws Exception {
		String attributeName = "postponeSleepApplicationReminder";
		DateTime retrieved = interactionModel.getAttributeRetrieved(attributeName);
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		if((retrieved == null) || Minutes.minutesBetween(retrieved, now).getMinutes() > ATTRIBUTE_UPDATE_TIME) {
			this.interactionModel = stateModelAccessPoint.readInteractionModel(username,attributeName);
			this.interactionModel.setAttributeRetrieved(attributeName, now);
		}
		return interactionModel.getPostponeSleepApplicationReminder();
	}
	
	/**
	 * Returns the time until which video exercise application reminders should be postponed. The 
	 * information will never be more than ATTRIBUTE_UPDATE_TIME minutes old.
	 * @return the time until which video exercise application reminders should be postponed.
	 * @throws Exception if an updated value can not be retrieved through the IDSS Core.
	 */
	public DateTime getPostponeVideoExerciseApplicationReminder() throws Exception {
		String attributeName = "postponeVideoExerciseApplicationReminder";
		DateTime retrieved = interactionModel.getAttributeRetrieved(attributeName);
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		if((retrieved == null) || Minutes.minutesBetween(retrieved, now).getMinutes() > ATTRIBUTE_UPDATE_TIME) {
			this.interactionModel = stateModelAccessPoint.readInteractionModel(username,attributeName);
			this.interactionModel.setAttributeRetrieved(attributeName, now);
		}
		return interactionModel.getPostponeVideoExerciseApplicationReminder();
	}
	
	/**
	 * TODO: Javadoc.
	 * @return
	 * @throws Exception
	 */
	public DateTime getPostponeCalendarApplicationReminder() throws Exception {
		String attributeName = "postponeCalendarApplicationReminder";
		DateTime retrieved = interactionModel.getAttributeRetrieved(attributeName);
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		if((retrieved == null) || Minutes.minutesBetween(retrieved, now).getMinutes() > ATTRIBUTE_UPDATE_TIME) {
			this.interactionModel = stateModelAccessPoint.readInteractionModel(username,attributeName);
			this.interactionModel.setAttributeRetrieved(attributeName, now);
		}
		return interactionModel.getPostponeCalendarApplicationReminder();
	}

	/**
	 * TODO: Javadoc.	
	 * @return
	 * @throws Exception
	 */
	public DateTime getPostponeCognitiveExerciseApplicationReminder() throws Exception {
		String attributeName = "postponeCognitiveExerciseApplicationReminder";
		DateTime retrieved = interactionModel.getAttributeRetrieved(attributeName);
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		if((retrieved == null) || Minutes.minutesBetween(retrieved, now).getMinutes() > ATTRIBUTE_UPDATE_TIME) {
			this.interactionModel = stateModelAccessPoint.readInteractionModel(username,attributeName);
			this.interactionModel.setAttributeRetrieved(attributeName, now);
		}
		return interactionModel.getPostponeCognitiveExerciseApplicationReminder();
	}
	
	/**
	 * TODO: Javadoc.
	 * @return
	 * @throws Exception
	 */
	public DateTime getPostponeDomoticsApplicationReminder() throws Exception {
		String attributeName = "postponeDomoticsApplicationReminder";
		DateTime retrieved = interactionModel.getAttributeRetrieved(attributeName);
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		if((retrieved == null) || Minutes.minutesBetween(retrieved, now).getMinutes() > ATTRIBUTE_UPDATE_TIME) {
			this.interactionModel = stateModelAccessPoint.readInteractionModel(username,attributeName);
			this.interactionModel.setAttributeRetrieved(attributeName, now);
		}
		return interactionModel.getPostponeDomoticsApplicationReminder();
	}
	
	/**
	 * TODO: Javadoc.
	 * @return
	 * @throws Exception
	 */
	public DateTime getPostponeActivityApplicationReminder() throws Exception {
		String attributeName = "postponeActivityApplicationReminder";
		DateTime retrieved = interactionModel.getAttributeRetrieved(attributeName);
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		if((retrieved == null) || Minutes.minutesBetween(retrieved, now).getMinutes() > ATTRIBUTE_UPDATE_TIME) {
			this.interactionModel = stateModelAccessPoint.readInteractionModel(username,attributeName);
			this.interactionModel.setAttributeRetrieved(attributeName, now);
		}
		return interactionModel.getPostponeActivityApplicationReminder();
	}
	
	/**
	 * TODO: Javadoc.
	 * @return
	 * @throws Exception
	 */
	public DateTime getPostponeHealthApplicationReminder() throws Exception {
		String attributeName = "postponeHealthApplicationReminder";
		DateTime retrieved = interactionModel.getAttributeRetrieved(attributeName);
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		if((retrieved == null) || Minutes.minutesBetween(retrieved, now).getMinutes() > ATTRIBUTE_UPDATE_TIME) {
			this.interactionModel = stateModelAccessPoint.readInteractionModel(username,attributeName);
			this.interactionModel.setAttributeRetrieved(attributeName, now);
		}
		return interactionModel.getPostponeHealthApplicationReminder();
	}	
	
	/**
	 * Returns the last time the user interacted with the sleep application. The value
	 * will never be more than ATTRIBUTE_UPDATE_TIME minutes old.
	 * @return the last time the user interacted with the sleep application.
	 * @throws Exception if an updated value can not be retrieved through the IDSS Core.
	 */
	public DateTime getLastInteractionSleepApplication() throws Exception {
		String attributeName = "lastInteractionSleepApplication";
		DateTime retrieved = interactionModel.getAttributeRetrieved(attributeName);
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		if((retrieved == null) || Minutes.minutesBetween(retrieved, now).getMinutes() > ATTRIBUTE_UPDATE_TIME) {
			this.interactionModel = stateModelAccessPoint.readInteractionModel(username, attributeName);
			this.interactionModel.setAttributeRetrieved(attributeName, now);
		}		
		return interactionModel.getLastInteractionSleepApplication();
	}
	
	/**
	 * Returns the last time the user interacted with the video exercise application. The value
	 * will never be more than ATTRIBUTE_UPDATE_TIME minutes old.
	 * @return the last time the user interacted with the video exercise application.
	 * @throws Exception if an updated value can not be retrieved through the IDSS Core.
	 */
	public DateTime getLastInteractionVideoExerciseApplication() throws Exception {
		String attributeName = "lastInteractionVideoExerciseApplication";
		DateTime retrieved = interactionModel.getAttributeRetrieved(attributeName);
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		if((retrieved == null) || Minutes.minutesBetween(retrieved, now).getMinutes() > ATTRIBUTE_UPDATE_TIME) {
			this.interactionModel = stateModelAccessPoint.readInteractionModel(username, attributeName);
			this.interactionModel.setAttributeRetrieved(attributeName, now);
		}		
		return interactionModel.getLastInteractionVideoExerciseApplication();
	}
	
	/**
	 * TODO: Javadoc.
	 * @return
	 * @throws Exception
	 */
	public DateTime getLastInteractionCalendarApplication() throws Exception {
		String attributeName = "lastInteractionCalendarApplication";
		DateTime retrieved = interactionModel.getAttributeRetrieved(attributeName);
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		if((retrieved == null) || Minutes.minutesBetween(retrieved, now).getMinutes() > ATTRIBUTE_UPDATE_TIME) {
			this.interactionModel = stateModelAccessPoint.readInteractionModel(username, attributeName);
			this.interactionModel.setAttributeRetrieved(attributeName, now);
		}		
		return interactionModel.getLastInteractionCalendarApplication();
	}
	
	/**
	 * TODO: Javadoc.
	 * @return
	 * @throws Exception
	 */
	public DateTime getLastInteractionCognitiveExerciseApplication() throws Exception {
		String attributeName = "lastInteractionCognitiveExerciseApplication";
		DateTime retrieved = interactionModel.getAttributeRetrieved(attributeName);
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		if((retrieved == null) || Minutes.minutesBetween(retrieved, now).getMinutes() > ATTRIBUTE_UPDATE_TIME) {
			this.interactionModel = stateModelAccessPoint.readInteractionModel(username, attributeName);
			this.interactionModel.setAttributeRetrieved(attributeName, now);
		}		
		return interactionModel.getLastInteractionCognitiveExerciseApplication();
	}
	
	/**
	 * TODO: Javadoc.
	 * @return
	 * @throws Exception
	 */
	public DateTime getLastInteractionDomoticsApplication() throws Exception {
		String attributeName = "lastInteractionDomoticsApplication";
		DateTime retrieved = interactionModel.getAttributeRetrieved(attributeName);
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		if((retrieved == null) || Minutes.minutesBetween(retrieved, now).getMinutes() > ATTRIBUTE_UPDATE_TIME) {
			this.interactionModel = stateModelAccessPoint.readInteractionModel(username, attributeName);
			this.interactionModel.setAttributeRetrieved(attributeName, now);
		}		
		return interactionModel.getLastInteractionDomoticsApplication();
	}
	
	/**
	 * TODO: Javadoc.
	 * @return
	 * @throws Exception
	 */
	public DateTime getLastInteractionMyActivityApplication() throws Exception {
		String attributeName = "lastInteractionMyActivityApplication";
		DateTime retrieved = interactionModel.getAttributeRetrieved(attributeName);
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		if((retrieved == null) || Minutes.minutesBetween(retrieved, now).getMinutes() > ATTRIBUTE_UPDATE_TIME) {
			this.interactionModel = stateModelAccessPoint.readInteractionModel(username, attributeName);
			this.interactionModel.setAttributeRetrieved(attributeName, now);
		}		
		return interactionModel.getLastInteractionMyActivityApplication();
	}
	
	/**
	 * TODO: Javadoc.
	 * @return
	 * @throws Exception
	 */
	public DateTime getLastInteractionMyHealthApplication() throws Exception {
		String attributeName = "lastInteractionMyHealthApplication";
		DateTime retrieved = interactionModel.getAttributeRetrieved(attributeName);
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		if((retrieved == null) || Minutes.minutesBetween(retrieved, now).getMinutes() > ATTRIBUTE_UPDATE_TIME) {
			this.interactionModel = stateModelAccessPoint.readInteractionModel(username, attributeName);
			this.interactionModel.setAttributeRetrieved(attributeName, now);
		}		
		return interactionModel.getLastInteractionMyHealthApplication();
	}
	
	/**
	 * Returns the total amount of eWALL Coins that the user has. The value will never be
	 * more than ATTRIBUTE_UPDATE_TIME minutes old.
	 * @return the total amount of eWALL Coins of the user of this {@link VCDataCollection}.
	 * @throws Exception if an updated value can not be retrieved through the IDSS Core.
	 */
	public Integer getTotalCoins() throws Exception {
		String attributeName = "coinTotal";
		DateTime retrieved = userModel.getAttributeRetrieved(attributeName);
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		if((retrieved == null) || Minutes.minutesBetween(retrieved, now).getMinutes() > ATTRIBUTE_UPDATE_TIME) {
			this.userModel = stateModelAccessPoint.readUserModel(username, attributeName);
			this.userModel.setAttributeRetrieved(attributeName, now);
		}				
		return userModel.getCoinTotal();
	}
	
	public String getFirstName() {
		String attributeName ="firstName";
		DateTime retrieved = userModel.getAttributeRetrieved(attributeName);
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		if((retrieved == null) || Minutes.minutesBetween(retrieved, now).getMinutes() > config.getAttributeUpdateTime()) {
			try {
				this.userModel = stateModelAccessPoint.readUserModel(username, attributeName);
				this.userModel.setAttributeRetrieved(attributeName, now);
			} catch (Exception e) {
				logger.error("["+username+"] Unable to update User Model (retrieving '"+attributeName+"').");
			}			
		}
		return userModel.getFirstName();
	}
	
	public ActivityUnit getPreferredActivityUnit() {
		String attributeName ="preferredActivityUnit";
		DateTime retrieved = userModel.getAttributeRetrieved(attributeName);
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		if((retrieved == null) || Minutes.minutesBetween(retrieved, now).getMinutes() > config.getAttributeUpdateTime()) {
			try {
				this.userModel = stateModelAccessPoint.readUserModel(username, attributeName);
				this.userModel.setAttributeRetrieved(attributeName, now);
			} catch (Exception e) {
				logger.error("["+username+"] Unable to update User Model (retrieving '"+attributeName+"').");
			}			
		}
		return userModel.getPreferredActivityUnit();
	}
	
	public ActivityState getCurrentActivityState() {
		String attributeName ="currentActivityState";
		DateTime retrieved = physicalActivityStateModel.getAttributeRetrieved(attributeName);
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		if((retrieved == null) || Minutes.minutesBetween(retrieved, now).getMinutes() > config.getAttributeUpdateTime()) {
			try {
				this.physicalActivityStateModel = stateModelAccessPoint.readPhysicalActivityStateModel(username, attributeName);
				this.physicalActivityStateModel.setAttributeRetrieved(attributeName, now);
			} catch (Exception e) {
				logger.error("["+username+"] Unable to update Physical Activity State Model (retrieving '"+attributeName+"').");
			}			
		}
		
		return this.physicalActivityStateModel.getCurrentActivityState();
	}
	
	public ActivityState getGoalActivityState() {
		String attributeName ="goalActivityState";
		DateTime retrieved = physicalActivityStateModel.getAttributeRetrieved(attributeName);
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		if((retrieved == null) || Minutes.minutesBetween(retrieved, now).getMinutes() > config.getAttributeUpdateTime()) {
			try {
				this.physicalActivityStateModel = stateModelAccessPoint.readPhysicalActivityStateModel(username, attributeName);
				this.physicalActivityStateModel.setAttributeRetrieved(attributeName, now);
				logger.info("["+username+"] Updated Physical Activity State Model (attribute '"+attributeName+"').");
			} catch (Exception e) {
				logger.error("["+username+"] Unable to update Physical Activity State Model (retrieving '"+attributeName+"').");
			}			
		}
		
		return this.physicalActivityStateModel.getGoalActivityState();
	}
	
	/**
	 * Returns the expected level of physical activity in steps given this user's physical activity goal
	 * as defined in {@link PhysicalActivityStateModel#getGoalActivityState()} and taking into account
	 * the given day start- end end-times as "HH:mm:ss" time formats. The expected level of physical activity
	 * at any given time is based on an even distribution of the total amount of physical activity in the
	 * goal, spread equally between the given start- and end times of the day (e.g. normally one could
	 * expected physical activity to take place mostly between "08:00:00" and "22:00:00", but this could
	 * be user specific.
	 * 
	 * @param now the current time as a {@link DateTime} object.
	 * @param dayStartTime the pre-defined start of the day as HH:mm:ss string dateformat.
	 * @param dayEndTime the pre-defined end of the day as HH:mm:ss string dateformat.
	 * @return the currently expected level of physical activity.
	 */
	public int getCurrentExpectedSteps(DateTime now, String dayStartTime, String dayEndTime) {
		int goalSteps = getGoalActivityState().getSteps();
		
		DateTimeFormatter timeParser = DateTimeFormat.forPattern("HH:mm:ss");
		LocalTime dayStart = timeParser.parseLocalTime(dayStartTime);
		LocalTime dayEnd = timeParser.parseLocalTime(dayEndTime);
		LocalTime localTime = now.toLocalTime();
		int dayDuration = dayEnd.getMillisOfDay() - dayStart.getMillisOfDay();
		int elapsed = localTime.getMillisOfDay() - dayStart.getMillisOfDay();
		
		if (elapsed < 0)
			return 0;
		else if (elapsed > dayDuration)
			return goalSteps;
		else {
			int expectedActivity = (int)((long)goalSteps * elapsed / dayDuration);
			return expectedActivity;
		}
	}
	
	public GoalIntention getGoalIntention() {
		String attributeName ="goalIntention";
		DateTime retrieved = userModel.getAttributeRetrieved(attributeName);
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		if((retrieved == null) || Minutes.minutesBetween(retrieved, now).getMinutes() > config.getAttributeUpdateTime()) {
			try {
				this.userModel = stateModelAccessPoint.readUserModel(username, attributeName);
				this.userModel.setAttributeRetrieved(attributeName, now);
			} catch (Exception e) {
				logger.error("["+username+"] Unable to update User Model (retrieving '"+attributeName+"').");
			}			
		}
		return userModel.getGoalIntention();
	}
	
	public Double getPhysicalActivitySelfEfficacy() {
		String attributeName ="selfEfficacy";
		DateTime retrieved = physicalActivityStateModel.getAttributeRetrieved(attributeName);
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		if((retrieved == null) || Minutes.minutesBetween(retrieved, now).getMinutes() > config.getAttributeUpdateTime()) {
			try {
				this.physicalActivityStateModel = stateModelAccessPoint.readPhysicalActivityStateModel(username, attributeName);
				this.physicalActivityStateModel.setAttributeRetrieved(attributeName, now);
				logger.info("["+username+"] Updated Physical Activity State Model (attribute '"+attributeName+"').");
			} catch (Exception e) {
				logger.error("["+username+"] Unable to update Physical Activity State Model (retrieving '"+attributeName+"').");
			}			
		}
		
		return this.physicalActivityStateModel.getSelfEfficacy();
	}
	
	public StageOfChange getPhysicalActivityStageOfChange() {
		String attributeName ="stageOfChange";
		DateTime retrieved = physicalActivityStateModel.getAttributeRetrieved(attributeName);
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		if((retrieved == null) || Minutes.minutesBetween(retrieved, now).getMinutes() > config.getAttributeUpdateTime()) {
			try {
				this.physicalActivityStateModel = stateModelAccessPoint.readPhysicalActivityStateModel(username, attributeName);
				this.physicalActivityStateModel.setAttributeRetrieved(attributeName, now);
				logger.info("["+username+"] Updated Physical Activity State Model (attribute '"+attributeName+"').");
			} catch (Exception e) {
				logger.error("["+username+"] Unable to update Physical Activity State Model (retrieving '"+attributeName+"').");
			}			
		}		
		return this.physicalActivityStateModel.getStageOfChange();
	}
	
	// ------------------------------------------- //
	// ---------- Data Update Functions ---------- //
	// ------------------------------------------- //
	
	public void setInteractionModelAttribute(String attributeName, String value, double reliability) throws Exception {
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		stateModelAccessPoint.setInteractionModelAttribute(username, attributeName, value, reliability, now);
		interactionModel.setAttribute(attributeName, value, reliability, now);
		interactionModel.setAttributeRetrieved(attributeName, now);
	}
	
	public void setPhysicalActivityStateModelAttribute(String attributeName, String value, double reliability) throws Exception {
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		stateModelAccessPoint.setPhysicalActivityStateModelAttribute(username, attributeName, value, reliability, now);
		physicalActivityStateModel.setAttribute(attributeName, value, reliability, now);
		physicalActivityStateModel.setAttributeRetrieved(attributeName, now);
	}
}
