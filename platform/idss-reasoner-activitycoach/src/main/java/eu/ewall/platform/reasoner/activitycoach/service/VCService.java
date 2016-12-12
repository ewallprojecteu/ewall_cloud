package eu.ewall.platform.reasoner.activitycoach.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.springframework.web.client.RestOperations;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseConnection;
import eu.ewall.platform.idss.dao.DatabaseCriteria;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseFactory;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.idss.service.RemoteMethodException;
import eu.ewall.platform.idss.service.ScheduledService;
import eu.ewall.platform.idss.service.exception.DialogueNotFoundException;
import eu.ewall.platform.idss.service.exception.InvalidDialogueReplyException;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.dialogue.DialogueInstance;
import eu.ewall.platform.idss.service.model.common.dialogue.DialogueStep;
import eu.ewall.platform.idss.service.model.common.message.PhysicalActivityMotivationalMessage;
import eu.ewall.platform.idss.service.model.state.StateModelAccessPoint;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.idss.utils.datetime.VirtualClock;
import eu.ewall.platform.idss.utils.exception.FatalException;
import eu.ewall.platform.idss.utils.i18n.I18n;
import eu.ewall.platform.idss.utils.i18n.I18nLoader;
import eu.ewall.platform.reasoner.activitycoach.service.dialogue.DialogueAvailable;
import eu.ewall.platform.reasoner.activitycoach.service.dialogue.DialogueInstanceTable;
import eu.ewall.platform.reasoner.activitycoach.service.dialogue.DialogueManager;
import eu.ewall.platform.reasoner.activitycoach.service.dialogue.DialogueRepository;
import eu.ewall.platform.reasoner.activitycoach.service.exception.MessageNotFoundException;
import eu.ewall.platform.reasoner.activitycoach.service.generator.DialogueGenerator;
import eu.ewall.platform.reasoner.activitycoach.service.generator.GoodMorningDialogueGenerator;
import eu.ewall.platform.reasoner.activitycoach.service.generator.PAStageOfChangeDialogueGenerator;
import eu.ewall.platform.reasoner.activitycoach.service.generator.UseActivityApplicationDialogueGenerator;
import eu.ewall.platform.reasoner.activitycoach.service.generator.UseCalendarApplicationDialogueGenerator;
import eu.ewall.platform.reasoner.activitycoach.service.generator.UseCognitiveExerciseApplicationDialogueGenerator;
import eu.ewall.platform.reasoner.activitycoach.service.generator.UseDomoticsApplicationDialogueGenerator;
import eu.ewall.platform.reasoner.activitycoach.service.generator.UseHealthApplicationDialogueGenerator;
import eu.ewall.platform.reasoner.activitycoach.service.generator.UseSleepApplicationDialogueGenerator;
import eu.ewall.platform.reasoner.activitycoach.service.generator.UseVideoExerciseApplicationDialogueGenerator;
import eu.ewall.platform.reasoner.activitycoach.service.pamm.content.MessageContentGenerator;
import eu.ewall.platform.reasoner.activitycoach.service.pamm.intention.MessageIntentionGenerator;
import eu.ewall.platform.reasoner.activitycoach.service.pamm.representation.MessageRepresentationGenerator;
import eu.ewall.platform.reasoner.activitycoach.service.pamm.timing.MessageTimingGenerator;
import eu.ewall.platform.reasoner.activitycoach.service.protocol.VCMessage;
import eu.ewall.platform.reasoner.activitycoach.service.protocol.VCMessageAvailable;
import eu.ewall.platform.reasoner.activitycoach.service.protocol.VCMessageConstants;
import eu.ewall.platform.reasoner.activitycoach.service.protocol.VCMessageContent;
import eu.ewall.platform.reasoner.activitycoach.service.protocol.VCMessageError;
import eu.ewall.platform.reasoner.activitycoach.service.protocol.VCMessageFactory;
import eu.ewall.platform.reasoner.activitycoach.service.protocol.VCMessageSuccess;
import eu.ewall.platform.reasoner.activitycoach.service.protocol.VCResponse;
import eu.ewall.platform.reasoner.activitycoach.service.protocol.VCStatement;

/**
 * Scheduled service for the IDSS Virtual Coach service. After construction you must call {@link
 * #setPullInputProvider(PullInputProvider) setPullInputProvider()}, {@link
 * #setPushOutputPublisher(PushOutputPublisher) setPushOutputPublisher()} and
 * {@link #setStateModelAccessPoint(StateModelAccessPoint)
 * setStateModelAccessPoint()}.
 * 
 * <p>You must configure a {@link DatabaseFactory DatabaseFactory} in the
 * {@link AppComponents AppComponents}. You may also configure an {@link
 * ILoggerFactory ILoggerFactory}.</p>
 * 
 * <p>The service gets the time from the {@link VirtualClock VirtualClock} so
 * it supports quick simulations.</p>
 * 
 * @author Dennis Hofs (RRD)
 * @author Harm op den Akker (RRD)
 */
public class VCService extends ScheduledService {
	public static final String LOGTAG = "VirtualCoachService";
	
	private VCConfiguration config;
	private RestOperations ewallClient;
	
	private Map<String,IDSSUserProfile> idssUserProfiles;
	private Map<String,VCMessageAvailableQueue> messageAvailableQueues;
	private Map<String,DialogueManager> userDialogueManagers;
	private Map<Locale,DialogueRepository> dialogueRepositories;
	private Map<String,VCDataCollection> dataCollections;
	
	private final Object dbLock = new Object();
	private StateModelAccessPoint stateModelAccessPoint;
	private PullInputProvider pullInputProvider;
	private Logger logger;
	private List<DialogueGenerator> dialogueGenerators;
	
	
	// ---------------------------------- //
	// ---------- Constructors ---------- //
	// ---------------------------------- //
	
	/**
	 * Constructs a new instance.
	 * 
	 * @param dbName the database name
	 */
	public VCService(VCConfiguration config, RestOperations ewallClient) {
		this.config = config;
		this.ewallClient = ewallClient;
		messageAvailableQueues = new HashMap<String,VCMessageAvailableQueue>();
		dialogueRepositories = new HashMap<Locale,DialogueRepository>();
		userDialogueManagers = new HashMap<String,DialogueManager>();
		idssUserProfiles = new HashMap<String,IDSSUserProfile>();
		dataCollections = new HashMap<String,VCDataCollection>();
		logger = AppComponents.getLogger(LOGTAG);
		
		// Set dialogue generators
		// TODO: Keep in mind that the order of this list matters
		// Perhaps it should be randomized regularly, or the order should be set based on
		// the importance of the reminders.
		dialogueGenerators = new ArrayList<DialogueGenerator>();
		
		PAStageOfChangeDialogueGenerator paStageOfChangeGenerator = new PAStageOfChangeDialogueGenerator(this);
		dialogueGenerators.add(paStageOfChangeGenerator);		
		
		GoodMorningDialogueGenerator goodMorningDialogueGenerator = new GoodMorningDialogueGenerator(this);
		dialogueGenerators.add(goodMorningDialogueGenerator);
		
		UseSleepApplicationDialogueGenerator useSleepApplicationDialogueGenerator = new UseSleepApplicationDialogueGenerator(this);
		dialogueGenerators.add(useSleepApplicationDialogueGenerator);
		
		UseVideoExerciseApplicationDialogueGenerator useVideoExerciseApplicationDialogueGenerator = new UseVideoExerciseApplicationDialogueGenerator(this);
		dialogueGenerators.add(useVideoExerciseApplicationDialogueGenerator);
		
		UseCalendarApplicationDialogueGenerator useCalendarApplicationDialogueGenerator = new UseCalendarApplicationDialogueGenerator(this);
		dialogueGenerators.add(useCalendarApplicationDialogueGenerator);
		
		UseActivityApplicationDialogueGenerator useActivityApplicationDialogueGenerator = new UseActivityApplicationDialogueGenerator(this);
		dialogueGenerators.add(useActivityApplicationDialogueGenerator);
		
		UseHealthApplicationDialogueGenerator useHealthApplicationDialogueGenerator = new UseHealthApplicationDialogueGenerator(this);
		dialogueGenerators.add(useHealthApplicationDialogueGenerator);
		
		UseCognitiveExerciseApplicationDialogueGenerator useCognitiveExerciseApplicationDialogueGenerator = new UseCognitiveExerciseApplicationDialogueGenerator(this);
		dialogueGenerators.add(useCognitiveExerciseApplicationDialogueGenerator);
		
		UseDomoticsApplicationDialogueGenerator useDomoticsApplicationDialogueGenerator = new UseDomoticsApplicationDialogueGenerator(this);
		dialogueGenerators.add(useDomoticsApplicationDialogueGenerator);

	}
	
	// ----------------------------- //
	// ---------- Getters ---------- //
	// ----------------------------- //
	
	/**
	 * Returns the {@link StateModelAccessPoint} for this {@link VCService}.
	 * @return the {@link StateModelAccessPoint} for this {@link VCService}.
	 */
	public StateModelAccessPoint getStateModelAccessPoint() {
		return stateModelAccessPoint;
	}
	
	/**
	 * Returns the {@link PullInputProvider} for this {@link VCService}.
	 * @return the {@link PullInputProvider} for this {@link VCService}.
	 */
	public PullInputProvider getPullInputProvider() {
		return pullInputProvider;
	}
	
	/**
	 * Returns the mapping of usernames to {@link VCMessageAvailableQueue} objects.
	 * @return the mapping of usernames to {@link VCMessageAvailableQueue} objects.
	 */
	public Map<String,VCMessageAvailableQueue> getMessageAvailableQueues() {
		return messageAvailableQueues;
	}
	
	/**
	 * Returns the mapping of usernames to {@link VCDataCollection} objects.
	 * @return the mapping of usernames to {@link VCDataCollection} objects.
	 */
	public Map<String,VCDataCollection> getDataCollections() {
		return dataCollections;
	}
	
	/**
	 * Returns the {@link VCConfiguration} for this {@link VCService}.
	 * @return the {@link VCConfiguration} for this {@link VCService}.
	 */
	public VCConfiguration getConfiguration() {
		return config;
	}
	
	public RestOperations getEWallClient() {
		return ewallClient;
	}
	
	// ----------------------------- //
	// ---------- Setters ---------- //
	// ----------------------------- //
	
	/**
	 * Sets the state model access point. This must be called before starting
	 * the service.
	 * 
	 * @param stateModelAccessPoint the state model access point for this {@link VCService}.
	 */
	public void setStateModelAccessPoint(
			StateModelAccessPoint stateModelAccessPoint) {
		this.stateModelAccessPoint = stateModelAccessPoint;
	}
	
	/**
	 * Sets the pull input provider. This must be called before starting the
	 * service.
	 * 
	 * @param pullInputProvider the pull input provider
	 */
	public void setPullInputProvider(PullInputProvider pullInputProvider) {
		this.pullInputProvider = pullInputProvider;
	}
	
	/**
	 * Finds the message with the specified message ID and user name. If the
	 * message doesn't exist, this method throws an exception.
	 * 
	 * @param database the database
	 * @param user the user name
	 * @param msgid the message ID
	 * @return the message
	 * @throws MessageNotFoundException if the message doesn't exist
	 * @throws Exception if an error occurs while reading from the database
	 */
	private PhysicalActivityMotivationalMessage findMessage(Database database, String msgid,
			String user) throws MessageNotFoundException, Exception {
		DatabaseCriteria criteria = new DatabaseCriteria.And(
				new DatabaseCriteria.Equal("id", msgid),
				new DatabaseCriteria.Equal("user", user)
		);
		PhysicalActivityMotivationalMessage message = database.selectOne(
				new MotivationalMessageTable(), criteria, null);
		if (message == null) {
			throw new MessageNotFoundException("Message with ID \"" + msgid +
					"\" and user \"" + user + "\" not found");
		}
		return message;
	}

	@Override
	public void runTask() throws FatalException {
		long startMS = System.currentTimeMillis();
		logger.info("VCService executing scheduled task:");
		
		try {
			updateUserInfo();
			logger.info("  - Succesfully updated user profiles: "+idssUserProfiles.size()+" currently known user profiles.");
		} catch (IOException e) {
			logger.error("IOException while updating user profiles.",e);
		} catch (RemoteMethodException e) {
			logger.error("RemoteMethodException while updating user profiles.",e);
		} catch (Exception e) {
			logger.error("Exception while updating user profiles.",e);
		}
		
		logger.info("  - Shuffling dialogue generators.");
		Collections.shuffle(dialogueGenerators);
		
		long endMS = System.currentTimeMillis();
		logger.info("VCService finished scheduled task in "+(endMS-startMS)+"ms.");
				
	}
	
	// --------------------------------------------------------- //
	// ---------- External (REST end-point) Functions ---------- //
	// --------------------------------------------------------- //
	
	/**
	 * Check if any communication is available for the user identified by the given
	 * {@code username}.
	 * 
	 * @param username the username identifier for which user to check for communication.
	 */
	public List<VCMessage> poll(String username) {
		long startMS = System.currentTimeMillis();
		logger.info("[" + username + "] Poll: start");
		List<VCMessage> restResponseList = new ArrayList<VCMessage>();
		
		// Retrieve the IDSS User Profile associated with the given username.
		IDSSUserProfile idssUserProfile = idssUserProfiles.get(username);
		if(idssUserProfile == null) {
			logger.error("["+username+"] User Profile not found for given username.");
			restResponseList.add(new VCMessageError("User profile not found for username '"+username+"'."));
			return restResponseList;
		}
		
		// Check the last time the queue was updated
		logger.info("[" + username + "] Poll: check message available queue");
		VCMessageAvailableQueue queue = messageAvailableQueues.get(username);
		if(System.currentTimeMillis() - queue.getLocalLastUpdateTime() < config.getMaximumAllowedPollingFrequency()) {
			VCMessageFactory vcMessageFactory = new VCMessageFactory(config);
			if(queue.hasPhysicalActivityMotivationalMessage()) {
				restResponseList.add(vcMessageFactory.generatePAMMAvailable(idssUserProfile));
			}
			
			for(DialogueAvailable dialogueAvailable : queue.getAvailableDialogues()) {
				restResponseList.add(vcMessageFactory.generateDialogueAvailable(idssUserProfile,dialogueAvailable.getDialogueTypeId()));
			}
			long endMS = System.currentTimeMillis();
			logger.info("Poll executed (no refresh) for user '"+username+"' in "+(endMS-startMS)+"ms.");
			return restResponseList;			
		}
		
		logger.info("[" + username + "] Poll: system initiate PAMM");
		VCMessage pammAvailable = systemInitiatePAMM(idssUserProfile);
		if(pammAvailable != null) restResponseList.add(pammAvailable);
		
		// Check all dialogue generators
		logger.info("[" + username + "] Poll: generate dialogues");
		for(DialogueGenerator dialogueGenerator : dialogueGenerators) {
			VCMessageAvailable dialogueAvailable = dialogueGenerator.generateDialogueAvailable(idssUserProfile);
			if(dialogueAvailable != null) {
				restResponseList.add(dialogueAvailable);
			}
		}
		logger.info("[" + username + "] Poll: finish");
		
		long endMS = System.currentTimeMillis();
		queue.setLocalLastUpdateTime(endMS);
		logger.info("Poll executed for user '"+username+"' in "+(endMS-startMS)+"ms.");
		
		// Finally, either send the list of rest responses or null
		if(restResponseList.size() == 0) {
			return null;
		} else {
			return restResponseList;
		}
	}
	
	/**
	 * Generates a Physical Activity Motivational Message for the user identified
	 * by the given {@code username}. If there is no IDSS user known for this given
	 * username, the method will return {@code null}. The method will try to "finish"
	 * the {@link PhysicalActivityMotivationalMessage} that was previously generated from
	 * a call to the /poll end-point ({@link VCService#poll(String)}. If no
	 * message is available in the queue, it will instead generate completely new message
	 * on the fly where {@link PhysicalActivityMotivationalMessage#getTiming()} will be
	 * update to reflect that this message was generated as an error recovery attempt.
	 * 
	 * @param username the username of the {@link IDSSProfile} for which to generate the message.
	 * @return a {@link VCMessageContent} object containing the contents of the finalized
	 * {@link PhysicalActivityMotivationalMessage}, or {@code null} if the username is unknown in the system.
	 */
	public VCMessageContent generatePAMM(String username) {
		long startMS = System.currentTimeMillis();
		logger.info("[" + username + "] Generate PAMM: start");
		
		// Retrieve the IDSS User Profile associated with the given username.
		IDSSUserProfile idssUserProfile = idssUserProfiles.get(username);
		if(idssUserProfile == null) {
			logger.error("["+username+"] User Profile not found for given username.");
			return null;
		}
				
		// Initialization for database connection.
		Database database = null;
		DatabaseConnection databaseConnection = null;

		
		// Read all required Virtual Coach database elements.
		VCDataCollection vcDataCollection = dataCollections.get(username);
				
		PhysicalActivityMotivationalMessage message = null;
		
		// Retrieve the pamm from the user's queue
		VCMessageAvailableQueue queue = messageAvailableQueues.get(username);
		message = queue.getPhysicalActivityMotivationalMessage();
		
		// -- Apparently the User Interface element requested to "finish" a pamm that
		// -- was not generated in the first place (through the /poll procedure) or was 
		// -- already processed by this method in a previous call. This is an abnormal 
		// -- situation, which is caused by an error somewhere (either IDSS crashed and 
		// -- restarted), network communication failure, or something unknown. To make 
		// -- sure that the user doesn't notice this we pretend that there is nothing 
		// -- wrong and actually generate a whole new pamm (including timing). We achieve
		// -- this by creating a "userInitiated" message, that was not actually requested 
		// -- by the user but by the previously described error state.
		if(message == null) {
			logger.warn("["+username+"] Request to finish unknown message; force generation of new message timing.");
			message = new PhysicalActivityMotivationalMessage();
			message.setUser(idssUserProfile.getUsername());
			MessageTimingGenerator timingGen = new MessageTimingGenerator(idssUserProfile, vcDataCollection, config);
			
			timingGen.generateUserInitiated(message, false,
					false, false, false,
					false);
			
			message.getTiming().setSystemInitiated(true); // Pretend this was actually a system initiated message to automatically determine secondary intentions
			message.getTiming().setErrorRecovery(true); // Indicate that this message is not a truly 'user initiated' message.
						
			try {
				databaseConnection = openDatabaseConnection();
				database = getDatabase(databaseConnection);
				database.insert(MotivationalMessageTable.NAME, message);
				vcDataCollection.getMessageHistory().addMessage(message);
			} catch (DatabaseException e) {
				logger.error("["+username+"] Error storing pamm in database.",e);
				return null;
			} catch (IOException e) {
				logger.error("["+username+"] Error storing pamm in database.",e);
				return null;
			} finally {
				closeDatabaseConnection(databaseConnection);
			}
			
		} 
		
		// Proceed with generating the content of the pamm.
		
		// Generate up-to-date message intention
		logger.info("[" + username + "] Generate PAMM: generate intention");
		MessageIntentionGenerator intentionGenerator = new MessageIntentionGenerator(idssUserProfile, vcDataCollection, config);
		if (!intentionGenerator.generateMessageIntention(message))
			return null;
		
		// Generate up-to-date message content
		logger.info("[" + username + "] Generate PAMM: generate content");
		MessageContentGenerator contentGenerator = new MessageContentGenerator(idssUserProfile, vcDataCollection, config);
		if (!contentGenerator.generateMessageContent(message))
			return null;
		
		// Generate up-to-date message representation
		logger.info("[" + username + "] Generate PAMM: generate representation");
		MessageRepresentationGenerator representationGenerator = new MessageRepresentationGenerator(idssUserProfile, vcDataCollection);
		if (!representationGenerator.generateMessageRepresentation(message))
			return null;
		
		// Set the 'content generated time' info
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		message.getTiming().setContentGeneratedTime(now);
		
		logger.info("Generated motivational message for user \"{}\": {}",
				idssUserProfile.getUsername(), message.getRepresentation().toString());
		
		try {
			logger.info("[" + username + "] Generate PAMM: start update database");
			databaseConnection = openDatabaseConnection();
			database = getDatabase(databaseConnection);
			database.update(MotivationalMessageTable.NAME, message);
			logger.info("[" + username + "] Generate PAMM: end update database");
			vcDataCollection.getMessageHistory().removeMessageById(message.getId());
			vcDataCollection.getMessageHistory().addMessage(message);
		} catch (DatabaseException e) {
			logger.error("["+username+"] Error updating pamm in database.",e);
			return null;
		} catch (IOException e) {
			logger.error("["+username+"] Error updating pamm in database.",e);
			return null;
		} finally {
			closeDatabaseConnection(databaseConnection);
		}
		
		// Remove the message from the user's queue
		queue.removePhysicalActivityMotivationalMessage();
		VCMessageContent restResponse = new VCMessageContent();
		restResponse.setType(VCMessageConstants.MESSAGE_TYPE_CONTENT);
		restResponse.setSubType(VCMessageConstants.MESSAGE_SUBTYPE_PAMM);
		
		VCStatement statement = new VCStatement();
		statement.setType(VCMessageConstants.STATEMENT_TYPE_TEXT);
		statement.setValue(message.getRepresentation().getCompleteMessage());
		VCStatement[] statements = new VCStatement[1];
		statements[0] = statement;
		restResponse.setStatements(statements);
		
		I18n i18n = I18nLoader.getInstance().getI18n("messages_activity_coach",
				idssUserProfile.getLocales(), true, null); // TODO: Automatically set these 'honorifics' (now: true)
		VCResponse[] responses = null;
		
		VCResponse responseGoodbye = new VCResponse();
		responseGoodbye.setType(VCMessageConstants.RESPONSE_TYPE_BASIC);
		responseGoodbye.setLabel(i18n.get("goodbye"));
		responseGoodbye.setAction(VCMessageConstants.ACTION_FINISHED);
		responseGoodbye.setUrl(config.getServiceBaseUrl()+"/observedPAMM?username="+username+"&id="+message.getId());
		
		// Add action "open physical activity overview" only if the app is enabled
		if(idssUserProfile.getApplicationNames().contains(VCMessageConstants.APP_NAME_PHYSICAL_ACTIVITY_OVERVIEW)) {
			responses = new VCResponse[2];
			VCResponse responseOpenActivityOverview = new VCResponse();
			responseOpenActivityOverview.setType(VCMessageConstants.RESPONSE_TYPE_BASIC);
			responseOpenActivityOverview.setLabel(i18n.get("open_activity_book"));
			responseOpenActivityOverview.setAction(
					VCMessageConstants.ACTION_OPEN_APPLICATION+":"+VCMessageConstants.APP_NAME_PHYSICAL_ACTIVITY_OVERVIEW);
			responseOpenActivityOverview.setUrl(
					config.getServiceBaseUrl()+"/observedPAMM?username="+username+"&id="+message.getId());
			responses[0] = responseOpenActivityOverview;
			responses[1] = responseGoodbye;
		
		// Otherwise, only one response possible: "Goodbye."
		} else {
			responses = new VCResponse[1];
			responses[0] = responseGoodbye;
		}
		
		restResponse.setResponses(responses);
		logger.info("[" + username + "] Generate PAMM: finish");
		long endMS = System.currentTimeMillis();
		logger.info("Generate PAMM executed for user '"+username+"' in "+(endMS-startMS)+"ms.");
		return restResponse;
	}
	
	/**
	 * Indicate that the pamm with given {@code messageID} for the given {@code username} has been 
	 * observed by the user.
	 * @param username the username of the user who has observed the message.
	 * @param messageID the unique identifier of the message that should be set to observed.
	 * @return a {@link VCMessage} either of type {@link VCMessageSuccess} or {@link VCMessageError},
	 * depending on the success of the operation.
	 */
	public VCMessage observedPAMM(String username, String messageId) {
		
		// Retrieve the IDSS User Profile associated with the given username.
		IDSSUserProfile idssUserProfile = idssUserProfiles.get(username);
		if(idssUserProfile == null) {
			logger.error("["+username+"] User Profile not found for given username.");
			return new VCMessageError("Error retrieving profile information for user '"+username+"'.");
		}
		
		DatabaseConnection dbConn = null;
		
		try {
			dbConn = openDatabaseConnection();
			Database database = getDatabase(dbConn);
			PhysicalActivityMotivationalMessage message = findMessage(database,messageId,username);
			
			// Check if the message was already observed
			if(message.getTiming().getUserObservedTime() != null) {
				logger.error("["+username+"] Message with id '"+messageId+"' was already set to be observed.");
				return new VCMessageError("Message with id '"+messageId+"' was already set to be observed.");
			} else {			
				VirtualClock clock = VirtualClock.getInstance();
				DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
				message.getTiming().setUserObservedTime(now);

				logger.info("["+username+"] Set message with id '"+messageId+"' to be observed at '"+now.toString(VCMessageConstants.DATE_TIME_FORMAT));
				
				// Update the database
				database.update(MotivationalMessageTable.NAME, message);
				
				// Update the vcDataCollection
				VCDataCollection vcDataCollection = dataCollections.get(username);
				vcDataCollection.getMessageHistory().removeMessageById(messageId);
				vcDataCollection.getMessageHistory().addMessage(message);
				
				return new VCMessageSuccess("Message with id '"+messageId+"' for user '"+username+"' set to be observed at "+now.toString(VCMessageConstants.DATE_TIME_FORMAT)+".");
			}
		} catch (IOException e) {
			logger.error("["+username+"] IOException while updating pamm with id '"+messageId+"'.",e);
			return new VCMessageError("Error updating observed status for message with id '"+messageId+"' for user '"+username+"'.");
		} catch (DatabaseException e) {
			logger.error("["+username+"] DatabaseException while updating pamm with id '"+messageId+"'.",e);
			return new VCMessageError("Error updating observed status for message with id '"+messageId+"' for user '"+username+"'.");
		} catch (MessageNotFoundException e) {
			logger.error("["+username+"] MessageNotFoundException while updating pamm with id '"+messageId+"'.",e);
			return new VCMessageError("Error updating observed status for message with id '"+messageId+"' for user '"+username+"'.");
		} catch (Exception e) {
			logger.error("["+username+"] Exception while updating pamm with id '"+messageId+"'.",e);
			return new VCMessageError("Error updating observed status for message with id '"+messageId+"' for user '"+username+"'.");
		} finally {
			closeDatabaseConnection(dbConn);
		}		
		
	}
	
	/**
	 * Called by User Interface element to start a dialogue for the given {@code username}
	 * of the given {@code dialogueTypeId}.
	 * @param username the username for which to start the dialogue.
	 * @param dialogueTypeId the identifier of the type of dialogue to start.
	 * @return a {@code VCMessage} containing either the first step of the dialogue or an error message.
	 */
	public VCMessage startDialogue(String username, String dialogueTypeId) {
		
		// Retrieve the IDSS User Profile associated with the given username.
		IDSSUserProfile idssUserProfile = idssUserProfiles.get(username);
		if(idssUserProfile == null) {
			logger.error("["+username+"] User Profile not found for given username.");
			return new VCMessageError("Error retrieving profile information for user '"+username+"'.");
		}
		
		// Get the dialogue manager for this user
		DialogueManager dialogueManager = getDialogueManager(idssUserProfile);
		
		// Check if a dialogue is currently ongoing, and abort if this is the case
		if(dialogueManager.hasActiveDialogue()) {
			logger.warn("["+username+"] Aborting start of new dialogue. Requested to start dialogue of type '"+dialogueTypeId+"', but a dialogue is currently ongoing.");
			return new VCMessageError("Requested to start dialogue of type '"+dialogueTypeId+"' for user '"+username+"', but a dialogue is currently ongoing.");
		}
		
		DialogueStep dialogueStep = null;
		try {
			dialogueStep = dialogueManager.startDialogue(dialogueTypeId);
		} catch(DialogueNotFoundException e) {
			logger.error("["+username+"] Error starting new dialogue of type '"+dialogueTypeId+"' dialogue not found for user '"+username+"' and preferred locale '"+dialogueManager.getDialogueRepository().getLocale()+"'.");
			return new VCMessageError("Error starting new dialogue of type '"+dialogueTypeId+"' dialogue not found for user '"+username+"' and preferred locale '"+dialogueManager.getDialogueRepository().getLocale()+"'.");
		} catch (DatabaseException e) {
			logger.error("["+username+"] Error starting new dialogue of type '"+dialogueTypeId+"', error accessing database.",e); 
			return new VCMessageError("Error starting new dialogue of type '"+dialogueTypeId+"', error accessing database.");
		}
		
		// Ongoing dialogue should not be in the 'available' queue
		VCMessageAvailableQueue queue = messageAvailableQueues.get(username);
		if(queue.hasDialogueAvailable(dialogueTypeId)) {
			queue.removeDialogueAvailable(dialogueTypeId);
		}
		
		VCMessageFactory vcMessageFactory = new VCMessageFactory(config);
		return vcMessageFactory.generateVCMessageContent(dialogueStep, idssUserProfile);
	}
	
	/**
	 * Progresses the ongoing {@link DialogueInstance} for the given {@code username}, indicating
	 * that the user has selected the given {@code replyId}.
	 * @param username the username of the {@link IDSSProfile} for which to progress the current dialogue
	 * @param replyId the selected replyId from the user
	 * @return a {@link VCMessage} either of type {@link VCMessageError} in case of any error or 
	 * of type {@link VCMessageContent} containing the next step in the dialogue (or a first step in
	 * a newly initiated dialogue if applicable).
	 */
	public VCMessage progressDialogue(String username, String replyId) {
		
		// Retrieve the IDSS User Profile associated with the given username.
		IDSSUserProfile idssUserProfile = idssUserProfiles.get(username);
		if(idssUserProfile == null) {
			logger.error("["+username+"] User Profile not found for given username.");
			return new VCMessageError("Error retrieving profile information for user '"+username+"'.");
		}
		
		// Get the dialogue manager for this user
		DialogueManager userDialogueManager = getDialogueManager(idssUserProfile);
		
		// Check if a dialogue is currently ongoing, if not, abort operation
		if(!userDialogueManager.hasActiveDialogue()) {
			logger.warn("["+username+"] Aborting progress of dialogue, no dialogue is currently ongoing for user '"+username+"'.");
			return new VCMessageError("Aborting progress of dialogue, no dialogue is currently ongoing for user '"+username+"'.");
		}
		
		DialogueStep dialogueStep = null;
		try {
			dialogueStep = userDialogueManager.progressDialogue(replyId);
		} catch (DatabaseException e) {
			logger.error("["+username+"] Error accessing database while progressing dialogue.",e); 
			return new VCMessageError("Error accessing database while progressing dialogue.");
		} catch (DialogueNotFoundException e) {
			logger.error("["+username+"] "+e.getMessage(),e);
			return new VCMessageError(e.getMessage());
		} catch (InvalidDialogueReplyException e) {
			logger.error("["+username+"] "+e.getMessage(),e);
			return new VCMessageError(e.getMessage());
		}
		
		if(dialogueStep == null) {
			return new VCMessageSuccess("Reply to dialogue ('"+replyId+"') processed succesfully, dialogue is now finished.");
		} else {		
			VCMessageFactory vcMessageFactory = new VCMessageFactory(config);
			return vcMessageFactory.generateVCMessageContent(dialogueStep, idssUserProfile);
		}
	}
	
	// -------------------------------------------------------------------------- //
	// ---------- TEST Versions of External (REST end-point) Functions ---------- //
	// -------------------------------------------------------------------------- //
	
	public VCMessage resetUser(String username) {
		
		// Retrieve the IDSS User Profile associated with the given username.
		IDSSUserProfile idssUserProfile = idssUserProfiles.get(username);
		if(idssUserProfile == null) {
			logger.error("["+username+"] User Profile not found for given username.");
			return new VCMessageError("Error retrieving profile information for user '"+username+"'.");
		}
		
		VCMessageAvailableQueue queue = messageAvailableQueues.get(username);
		queue.removePhysicalActivityMotivationalMessage();
		
		DialogueManager dialogueManager = getDialogueManager(idssUserProfile);
		if(dialogueManager.hasActiveDialogue()) {
			dialogueManager.clearActiveDialogue();
			queue.removeAllDialogueAvailable();
		}
		
		return new VCMessageSuccess("Succesfully reset internal Virtual Coach state for user '"+username+"'.");
	}
	
	// ------------------------------------ //
	// ---------- Help Functions ---------- //
	// ------------------------------------ //
	
	/**
	 * Check if a system initiated pamm notification should be sent or not (return {@code null}).
	 * @param idssUserProfile
	 * @return
	 */
	private VCMessage systemInitiatePAMM(IDSSUserProfile idssUserProfile) {
		
		String username = idssUserProfile.getUsername();
		VCMessageFactory vcMessageFactory = new VCMessageFactory(config);
		Database database = null;
		DatabaseConnection databaseConnection = null;
		
		// Check if a message is already in the queue.		
		VCMessageAvailableQueue queue = messageAvailableQueues.get(username);
		if(queue.hasPhysicalActivityMotivationalMessage()) {
			logger.debug("["+username+"] PAMM found in queue for user, checking expiration time.");
			VirtualClock clock = VirtualClock.getInstance();
			DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
			if(queue.isExpiredPhysicalActivityMotivationalMessage(now)) {
				queue.removePhysicalActivityMotivationalMessage();
				logger.debug("["+username+"] PAMM in queue expired, continuing pamm generation.");
			} else {
				logger.debug("["+username+"] PAMM in queue active, skipping pamm generation.");
				return vcMessageFactory.generatePAMMAvailable(idssUserProfile);
			}
		}
		
		// No message in the queue, check if the time is opportune for a new message
		// Read all required Virtual Coach database elements.
		VCDataCollection vcDataCollection = dataCollections.get(username);
		
		MessageTimingGenerator timingGenerator = new MessageTimingGenerator(idssUserProfile,vcDataCollection,config);
		if (timingGenerator.checkSystemInitiated()) {
			// A Physical Activity Motivational Message (PAMM) should be sent.
			
			PhysicalActivityMotivationalMessage message = new PhysicalActivityMotivationalMessage();
			message.setUser(idssUserProfile.getUsername());
			timingGenerator.generateSystemInitiated(message); // Attach timing information to the message object.
			queue.setPhysicalActivityMotivationalMessage(message);
			
			try {
				databaseConnection = openDatabaseConnection();
				database = getDatabase(databaseConnection);
				database.insert(MotivationalMessageTable.NAME, message);
				vcDataCollection.getMessageHistory().addMessage(message);
			} catch (Exception e) {
				logger.error("Error saving physical activity motivational message to database for user '"+username+"'.",e);
				return new VCMessageError("Error saving physical activity motivational message to database for user '"+username+"': "+e.getMessage());
			} finally {
				closeDatabaseConnection(databaseConnection);
			}
			return vcMessageFactory.generatePAMMAvailable(idssUserProfile);				
		} else {
			return null;
		}		
	}
		
	/**
	 * Returns the database for this service. It will create or upgrade the
	 * database if needed.
	 * 
	 * @param dbConn the database connection
	 * @return the database
	 * @throws DatabaseException if a database error occurs
	 */
	public Database getDatabase(DatabaseConnection dbConn)
			throws DatabaseException {
		synchronized (dbLock) {
			List<DatabaseTableDef<?>> tableDefs =
					new ArrayList<DatabaseTableDef<?>>();
			tableDefs.add(new MotivationalMessageTable());
			tableDefs.add(new DialogueInstanceTable());
			tableDefs.add(new PerformanceMetricTable());
			return dbConn.initDatabase(config.getMongoDBName(), tableDefs, false);
		}
	}

	/**
	 * Updates the list of known {@link IDSSUserProfile}s for this {@link VCService}.
	 * @throws IOException in case of a network communication error.
	 * @throws RemoteMethodException in case the remote (profiling server) method call fails.
	 * @throws Exception in case of any network or communication error.
	 */
	private void updateUserInfo() throws IOException, RemoteMethodException, Exception {
		idssUserProfiles = new HashMap<String,IDSSUserProfile>();
		List<IDSSUserProfile> users = pullInputProvider.getUsers();
		
		// Remove any userCommunicationQueues for users that no longer exist
		Iterator<String> iterator = messageAvailableQueues.keySet().iterator();
		while(iterator.hasNext()) {
			String queueUsername = iterator.next();
			boolean removeQueue = true;
			for(IDSSUserProfile idssUserProfile : users) {
				if(queueUsername.equals(idssUserProfile.getUsername())) {
					removeQueue = false;
					break;
				}
			}
			if(removeQueue) {
				messageAvailableQueues.remove(queueUsername);
			}
		}
		
		// Remove any dataCollections for users that no longer exist
		iterator = dataCollections.keySet().iterator();
		while(iterator.hasNext()) {
			String dataCollectionUsername = iterator.next();
			boolean removeCollection = true;
			for(IDSSUserProfile idssUserProfile : users) {
				if(dataCollectionUsername.equals(idssUserProfile.getUsername())) {
					removeCollection = false;
					break;
				}
			}
			if(removeCollection) {
				dataCollections.remove(dataCollectionUsername);
			}
		}
		
		for(IDSSUserProfile idssUserProfile : users) {
			String username = idssUserProfile.getUsername();
			
			// Add IDSS profile to new list of idssUserProfiles for this service.
			idssUserProfiles.put(username,idssUserProfile);
			
			// Create any new userCommunicationQueues for users that don't have them yet
			if(!messageAvailableQueues.containsKey(username)) {
				VCMessageAvailableQueue queue = new VCMessageAvailableQueue(username);
				messageAvailableQueues.put(username, queue);
			}
			
			// Create any new dataCollection objects for users that don't have them yet
			if(!dataCollections.containsKey(username)) {
				Database database = null;
				DatabaseConnection databaseConnection = null;
				try {
					databaseConnection = openDatabaseConnection();
					database = getDatabase(databaseConnection);
					VCDataCollection vcDataCollection = new VCDataCollection(idssUserProfile,stateModelAccessPoint,database,config);
					dataCollections.put(username,vcDataCollection);
					logger.info("Retrieved VCDataCollection for user " + username);
				} catch (Exception e) {
					logger.error("["+username+"] Error opening database connection while trying to generate new VCDataCollection.",e);
				} finally {
					closeDatabaseConnection(databaseConnection);
				}
			}			
		}
	}
	
	/**
	 * Returns the {@link DialogueManager} associated with the given {@link IDSSUserProfile}.
	 * @param idssUserProfile the {@link IDSSUserProfile} for which to find the {@link DialogueManager}.
	 * @return the {@link DialogueManager} associated with the given {@link IDSSUserProfile}.
	 */
	public DialogueManager getDialogueManager(IDSSUserProfile idssUserProfile) {
		DialogueManager userDialogueManager = null;
		if(!userDialogueManagers.containsKey(idssUserProfile.getUsername())) {
	
			// Get the dialogue repository for the current locale of the user
			Locale preferredLocale = new Locale("en");
			List<Locale> userLocales = idssUserProfile.getLocales();
			if(userLocales != null) {
				preferredLocale = userLocales.get(0);
			}
			
			DialogueRepository dialogueRepository = null;
			if(!dialogueRepositories.containsKey(preferredLocale)) {
				dialogueRepository = new DialogueRepository(preferredLocale);
				dialogueRepositories.put(preferredLocale, dialogueRepository);
			} else {
				dialogueRepository = dialogueRepositories.get(preferredLocale);
			}
			
			userDialogueManager = new DialogueManager(idssUserProfile,dialogueRepository,this,dataCollections.get(idssUserProfile.getUsername()));
			userDialogueManagers.put(idssUserProfile.getUsername(), userDialogueManager);
		} else {
			userDialogueManager = userDialogueManagers.get(idssUserProfile.getUsername());
		}
		return userDialogueManager;
	}
}