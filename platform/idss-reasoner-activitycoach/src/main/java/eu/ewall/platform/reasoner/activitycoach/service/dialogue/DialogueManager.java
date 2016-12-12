package eu.ewall.platform.reasoner.activitycoach.service.dialogue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.springframework.web.client.RestOperations;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseConnection;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.service.exception.DialogueNotFoundException;
import eu.ewall.platform.idss.service.exception.InvalidDialogueReplyException;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.dialogue.DialogueAction;
import eu.ewall.platform.idss.service.model.common.dialogue.DialogueBasicReply;
import eu.ewall.platform.idss.service.model.common.dialogue.DialogueCondition;
import eu.ewall.platform.idss.service.model.common.dialogue.DialogueConstants;
import eu.ewall.platform.idss.service.model.common.dialogue.DialogueHistoryEntry;
import eu.ewall.platform.idss.service.model.common.dialogue.DialogueInstance;
import eu.ewall.platform.idss.service.model.common.dialogue.DialogueReply;
import eu.ewall.platform.idss.service.model.common.dialogue.DialogueStep;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.idss.utils.datetime.VirtualClock;
import eu.ewall.platform.reasoner.activitycoach.service.VCDataCollection;
import eu.ewall.platform.reasoner.activitycoach.service.VCService;
import eu.ewall.platform.reasoner.activitycoach.service.dialogues.DialogueActionRepresentation;
import eu.ewall.platform.reasoner.activitycoach.service.dialogues.DialogueBasicReplyRepresentation;
import eu.ewall.platform.reasoner.activitycoach.service.dialogues.DialogueConditionRepresentation;
import eu.ewall.platform.reasoner.activitycoach.service.dialogues.DialogueReplyRepresentation;
import eu.ewall.platform.reasoner.activitycoach.service.dialogues.DialogueRepresentation;
import eu.ewall.platform.reasoner.activitycoach.service.dialogues.DialogueStepRepresentation;

/**
 * A {@link DialogueManager} keeps track of ongoing dialogues for a particular user and provides
 * methods for initiating and progressing (taking the next step in) dialogues.
 * 
 * @author Harm op den Akker, RRD
 * @author Dennis Hofs, RRD
 */
public class DialogueManager {
	
	private static final String LOGTAG = "DialogueManager";
	
	private IDSSUserProfile idssUserProfile;
	private String username;
	private DialogueRepository dialogueRepository;
	private VCService vcService;
	private VCDataCollection vcDataCollection;
	private DialogueInstance activeDialogue;
	private DialogueStepParser dialogueStepParser;
	private DialogueConditionParser dialogueConditionParser;
	private Random random;
	private Logger logger;
	
	/**
	 * Creates an instance of a {@link DialogueManager} for a given user.
	 * @param idssUserProfile the user associated with this {@link DialogueManager}.
	 * @param dialogueRepository the (localized) {@link DialogueRepository} associated with this {@link DialogueManager}.
	 * @param database a {@link Database} that provides access to stored {@link DialogueInstance}s.
	 */
	public DialogueManager(IDSSUserProfile idssUserProfile, DialogueRepository dialogueRepository, VCService vcService, VCDataCollection vcDataCollection) {
		this.idssUserProfile = idssUserProfile;
		this.username = idssUserProfile.getUsername();
		this.dialogueRepository = dialogueRepository;
		this.vcService = vcService;
		this.vcDataCollection = vcDataCollection;
		this.dialogueStepParser = new DialogueStepParser(vcService.getDataCollections().get(username));
		this.dialogueConditionParser = new DialogueConditionParser(vcService.getDataCollections().get(username));
		this.activeDialogue = null;
		this.random = new Random();
		logger = AppComponents.getLogger(LOGTAG);
	}
	
	/**
	 * Starts a dialogue of the given {@code dialogueTypeId} for the user and returns the first
	 * {@link DialogueStep}. The {@link DialogueManager} will read the dialogue representation for
	 * the given dialogueTypeId from its {@link DialogueRepository} and generate from this a 
	 * {@link DialogueInstance}. This dialogue instance will be set as activeDialogue for this user until
	 * the dialogue is finished through one or more calls to {@link DialogueManager#progressDialogue(String)}.
	 * 
	 * @param dialogueTypeId the identifier for the type of dialogue to start.
	 * @return the first step of the dialogue as a {@link DialogueStep}.
	 * @throws DialogueNotFoundException if a dialogue representation for the given {@code dialogueTypeId} could not be found.
	 * @throws DatabaseException if an error occurred while inserting the generated {@link DialogueInstance} into this
	 * {@link DialogueManager}'s {@link Database}.
	 */
	public DialogueStep startDialogue(String dialogueTypeId) throws DialogueNotFoundException, DatabaseException { 
		
		DialogueRepresentation dialogueRepresentation = dialogueRepository.getDialogue(dialogueTypeId);
		if (dialogueRepresentation == null)
			throw new DialogueNotFoundException("Dialogue representation with dialogueTypeId: '"+dialogueTypeId+"' not found for locale '"+dialogueRepository.getLocale()+"'.");
				
		DialogueInstance dialogueInstance = generateDialogueInstance(idssUserProfile, dialogueRepresentation);
		dialogueInstance.setCurrentStepId(dialogueInstance.getInitialStepId());
		
		Database database = null;
		DatabaseConnection databaseConnection = null;

		try {
			databaseConnection = vcService.openDatabaseConnection();
			database = vcService.getDatabase(databaseConnection);
			database.insert(DialogueInstanceTable.NAME, dialogueInstance);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			vcService.closeDatabaseConnection(databaseConnection);
		}
		
		this.activeDialogue = dialogueInstance;
		DialogueStep dialogueStep = dialogueInstance.getStepById(dialogueInstance.getCurrentStepId());
		dialogueStepParser.parse(dialogueStep);
		dialogueConditionParser.parse(dialogueStep);
		return dialogueStep;
	}
	
	public DialogueStep progressDialogue(String replyId) throws DatabaseException, DialogueNotFoundException, InvalidDialogueReplyException { 
		// Check if a dialogue is currently active
		if(activeDialogue == null) {
			throw new DialogueNotFoundException("Attempt to progress a dialogue, but no dialogue is currently active for user '"+idssUserProfile.getUsername()+"'.");
		}
		
		String currentStepId = activeDialogue.getCurrentStepId();
		DialogueStep currentStep = activeDialogue.getStepById(currentStepId);
		DialogueReply selectedReply = currentStep.getReplyById(replyId);
		
		if(selectedReply == null) {
			throw new InvalidDialogueReplyException("The given replyId ('"+replyId+"') is not valid for the current step of the active dialogue (dialogueType: '"+activeDialogue.getDialogueTypeId()+"', currentStepId: '"+currentStep.getStepId()+"').");
		}
		
		// Update the dialogue history
		VirtualClock clock = VirtualClock.getInstance();
		DateTime currentTime = clock.getTime().withZone(idssUserProfile.getTimeZone());
		DialogueHistoryEntry dialogueHistoryEntry = new DialogueHistoryEntry(currentTime,selectedReply);
		activeDialogue.addDialogueHistoryStep(dialogueHistoryEntry);
	
		DialogueStep nextStep = null;
		
		// Evaluate any possible actions associated with the reply that are not of type "startDialogue"
		List<DialogueAction> actions = selectedReply.getActions();
		DialogueAction startDialogueAction = null;
		if((actions != null) && (actions.size()>0)) {
			for(DialogueAction action : actions) {
				if(action.getActionType().equals(DialogueConstants.DA_START_DIALOGUE)) {
					startDialogueAction = action;
				} else {
					boolean result = executeAction(action);
					String nextStepIdString = null;
					if(result == true) {
						if(action.getNextStepIdSuccess() != null) {
							nextStepIdString = action.getNextStepIdSuccess();
						}
					} else {
						if(action.getNextStepIdFailure() != null) {
							nextStepIdString = action.getNextStepIdFailure();
						}
					}
					if(nextStepIdString != null) {
						nextStep = activeDialogue.getStepById(nextStepIdString);
					}
				}
			}
		}		
		
		if(nextStep == null) {
			if(selectedReply instanceof DialogueBasicReply) {
				DialogueBasicReply selectedBasicReply = (DialogueBasicReply)selectedReply;
				String nextStepId = selectedBasicReply.getNextStepId();
				
				if(nextStepId != null) {
					nextStep = activeDialogue.getStepById(nextStepId);
				}
			}
		}
		
		// If there is a next step in this dialogue, update dialogue and return the next step
		if(nextStep != null) {
			activeDialogue.setCurrentStepId(nextStep.getStepId());
			Database database = null;
			DatabaseConnection databaseConnection = null;

			try {
				databaseConnection = vcService.openDatabaseConnection();
				database = vcService.getDatabase(databaseConnection);
				database.update(DialogueInstanceTable.NAME, activeDialogue);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				vcService.closeDatabaseConnection(databaseConnection);
			}
			
			dialogueStepParser.parse(nextStep);
			dialogueConditionParser.parse(nextStep);
			return nextStep;
		
		// Otherwise, if there is a "startDialogue" action to be executed:
		// wrap up this dialogue and start the new one.
		} else if(startDialogueAction != null) {
			activeDialogue.setFinished(true);
			Database database = null;
			DatabaseConnection databaseConnection = null;
			try {
				databaseConnection = vcService.openDatabaseConnection();
				database = vcService.getDatabase(databaseConnection);
				database.update(DialogueInstanceTable.NAME, activeDialogue);
				logger.info("["+username+"] Dialogue of type '"+activeDialogue.getDialogueTypeId()+"' finished and stored.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				vcService.closeDatabaseConnection(databaseConnection);
			}			
			
			String dialogueTypeId = startDialogueAction.getParameters().get(0);
			return startDialogue(dialogueTypeId);
			
		// If all else fails, this dialogue is over and we return null.
		} else {
			activeDialogue.setFinished(true);
			Database database = null;
			DatabaseConnection databaseConnection = null;
			try {
				databaseConnection = vcService.openDatabaseConnection();
				database = vcService.getDatabase(databaseConnection);
				database.update(DialogueInstanceTable.NAME, activeDialogue);
				logger.info("["+username+"] Dialogue of type '"+activeDialogue.getDialogueTypeId()+"' finished and stored.");
				activeDialogue = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				vcService.closeDatabaseConnection(databaseConnection);
			}
			return null;
		}
		
	}
	
	private DialogueInstance generateDialogueInstance(IDSSUserProfile idssUserProfile, DialogueRepresentation dialogueRepresentation) {
		
		String dialogueTypeId = dialogueRepresentation.getDialogueTypeId();
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		logger.info("["+username+"] Generating dialogue instance of type '"+dialogueTypeId+"'.");
		DialogueInstance dialogueInstance = new DialogueInstance(dialogueTypeId, idssUserProfile.getUsername(), now);
		dialogueInstance.setInitialStepId(dialogueRepresentation.getInitialStepId());
		
		Map<String,DialogueStepRepresentation> stepRepresentations = dialogueRepresentation.getSteps();
		
		List<DialogueStep> dialogueSteps = new ArrayList<DialogueStep>();
		Iterator<String> iterator = stepRepresentations.keySet().iterator();
		while(iterator.hasNext()) {
			String stepId = iterator.next();
			DialogueStepRepresentation dialogueStepRepresentation = stepRepresentations.get(stepId);
			
			DialogueStep dialogueStep = new DialogueStep();
			dialogueStep.setStepId(stepId);
			dialogueStep.setStatement(selectStatement(dialogueStepRepresentation.getStatements()));
			logger.info("["+username+"]   * Adding dialogue step '"+dialogueStep.getStatement()+"' (stepId: '"+dialogueStep.getStepId()+"').");
			
			List<DialogueReply> dialogueReplies = new ArrayList<DialogueReply>();
						
			for(DialogueReplyRepresentation dialogueReplyRepresentation : dialogueStepRepresentation.getReplies()) {
				if(dialogueReplyRepresentation instanceof DialogueBasicReplyRepresentation) {
					DialogueBasicReplyRepresentation dialogueBasicReplyRepresentation = (DialogueBasicReplyRepresentation)dialogueReplyRepresentation;
					DialogueBasicReply dialogueBasicReply = new DialogueBasicReply();
					dialogueBasicReply.setReplyId(dialogueBasicReplyRepresentation.getReplyId());
					dialogueBasicReply.setNextStepId(dialogueBasicReplyRepresentation.getNextStepId());
					dialogueBasicReply.setStatement(selectStatement(dialogueBasicReplyRepresentation.getStatements()));
					logger.info("["+username+"]     - Adding basic reply option '"+dialogueBasicReply.getStatement()+"' (replyId: '"+dialogueBasicReply.getReplyId()+"').");
					
					// Extract dialogue actions
					List<DialogueActionRepresentation> actionRepresentations = dialogueBasicReplyRepresentation.getActions();
					List<DialogueAction> actions = new ArrayList<DialogueAction>();
					for(DialogueActionRepresentation actionRepresentation : actionRepresentations) {
						DialogueAction newAction = new DialogueAction();
						newAction.setActionType(actionRepresentation.getActionType());
						newAction.setParameters(actionRepresentation.getParameters());
						newAction.setNextStepIdSuccess(actionRepresentation.getNextStepIdSuccess());
						newAction.setNextStepIdFailure(actionRepresentation.getNextStepIdFailure());
						actions.add(newAction);
					}					
					dialogueBasicReply.setActions(actions);
					
					// Extract dialogue conditions
					List<DialogueConditionRepresentation> conditionRepresentations = dialogueBasicReplyRepresentation.getConditions();
					List<DialogueCondition> conditions = new ArrayList<DialogueCondition>();
					for(DialogueConditionRepresentation conditionRepresentation : conditionRepresentations) {
						DialogueCondition newCondition = new DialogueCondition();
						newCondition.setConditionType(conditionRepresentation.getConditionType());
						newCondition.setParameters(conditionRepresentation.getParameters());
						conditions.add(newCondition);
					}
					dialogueBasicReply.setConditions(conditions);
					
					dialogueReplies.add(dialogueBasicReply);
				}		
			}
			dialogueStep.setDialogueReplies(dialogueReplies);
			dialogueSteps.add(dialogueStep);
		}
		
		dialogueInstance.setDialogueSteps(dialogueSteps);
		
		return dialogueInstance;
	}
	
	/**
	 * Selects and returns a random {@link String} from the given {@link List} of {@link String}s.
	 * @param statements a {@link List} of {@link String}s.
	 * @return a randomly selected element from the given list.
	 */
	private String selectStatement(List<String> statements) {
		return statements.get(random.nextInt(statements.size()));
	}
	
	/**
	 * Returns the user associated with this {@link DialogueManager}.
	 * @return the user associated with this {@link DialogueManager}.
	 */
	public IDSSUserProfile getUser() {
		return idssUserProfile;
	}
	
	/**
	 * Returns the (localized) {@link DialogueRepository} associated with this {@link DialogueManager}.
	 * @return the (localized) {@link DialogueRepository} associated with this {@link DialogueManager}.
	 */
	public DialogueRepository getDialogueRepository() {
		return dialogueRepository;
	}
	
	/**
	 * Returns the current active {@link DialogueInstance} or {@code null} if no dialogue is currently active for the user.
	 * @return the current active {@link DialogueInstance} or {@code null} if no dialogue is currently active for the user.
	 */
	public DialogueInstance getActiveDialogue() {
		return activeDialogue;
	}
	
	public void clearActiveDialogue() {
		this.activeDialogue = null;
	}
	
	/**
	 * Returns {@code true} if a {@link DialogueInstance} is currently active for this user, {@code false} otherwise.
	 * @return {@code true} if a {@link DialogueInstance} is currently active for this user, {@code false} otherwise.
	 */
	public boolean hasActiveDialogue() {
		if(activeDialogue == null) return false;
		else return true;
	}
	
	// ------------------------------------------------------------------- //
	// -------------------- Dialogue Action Executors -------------------- //
	// ------------------------------------------------------------------- //
	
	private boolean executeAction(DialogueAction action) {
		logger.info("["+username+"] Executing action of type '"+action.getActionType()+"'.");
		for(String parameter : action.getParameters()) {
			logger.info("Parameter value: "+parameter);
		}
		
		String actionType = action.getActionType();
		List<String> parameters = action.getParameters();
		
		if(actionType.equals(DialogueConstants.DA_STAGE_OF_CHANGE_UPDATE)) {
			String stageOfChange = parameters.get(0);
			logger.info("["+username+"] Executing action '"+actionType+" ("+stageOfChange+").");
			return executeStageOfChangeUpdate(stageOfChange);
		}
		
		if(actionType.equals(DialogueConstants.DA_POSTPONE_REMINDER)) {
			String reminderName = parameters.get(0);
			int reminderDuration = 24;
			if(parameters.size() > 1) {
				String reminderDurationString = parameters.get(1);
				reminderDuration = new Integer(reminderDurationString).intValue();
			}
			logger.info("["+username+"] Executing action '"+actionType+" ("+reminderName+","+reminderDuration+").");
			return executePostponeReminder(reminderName,reminderDuration);
		}
		
		if(actionType.equals(DialogueConstants.DA_UNLOCK_REWARD)) {
			String rewardName = parameters.get(0);
			logger.info("["+username+"] Executing action '"+actionType+" ("+rewardName+").");
			return executeUnlockReward(parameters.get(0));
		}
		
		logger.info("["+username+"] Failed to execute action of type '"+actionType+"' (unsupported action).");
		return false;		
	}
	
	private boolean executeStageOfChangeUpdate(String stageOfChange) {
		try {
			vcDataCollection.setPhysicalActivityStateModelAttribute("stageOfChange", stageOfChange, 1.0d);
			logger.info("["+username+"] New stage of change value set: '"+stageOfChange+"'.");
			return true;
		} catch (Exception ex) {
			logger.error("["+username+"] Can't write stage of change to physical activity state model: " +
					ex.getMessage(), ex);
			return false;
		}
	}
	
	private boolean executePostponeReminder(String reminderName, int reminderDuration) {
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		DateTime postponeTime = now.plusHours(reminderDuration);
		
		String attributeName = null;
		
		if(reminderName.equals(DialogueConstants.DT_PHYSICAL_ACTIVITY_STAGE_OF_CHANGE)) {
			attributeName = "postponePAStageOfChangeReminder";
		} else if(reminderName.equals(DialogueConstants.DT_USE_CALENDAR_APPLICATION)) {
			attributeName = "postponeCalendarApplicationReminder";
		} else if(reminderName.equals(DialogueConstants.DT_USE_COGNITIVE_EXERCISE_APPLICATION)) {
			attributeName = "postponeCognitiveExerciseApplicationReminder";
		} else if(reminderName.equals(DialogueConstants.DT_USE_DOMOTICS_APPLICATION)) {
			attributeName = "postponeDomoticsApplicationReminder";
		} else if(reminderName.equals(DialogueConstants.DT_USE_ACTIVITY_APPLICATION)) {
			attributeName = "postponeActivityApplicationReminder";
		} else if(reminderName.equals(DialogueConstants.DT_USE_HEALTH_APPLICATION)) {
			attributeName = "postponeHealthApplicationReminder";
		} else if(reminderName.equals(DialogueConstants.DT_USE_SLEEP_APPLICATION)) {
			attributeName = "postponeSleepApplicationReminder";
		} else if(reminderName.equals(DialogueConstants.DT_USE_VIDEO_EXERCISE_APPLICATION)) {
			attributeName = "postponeVideoExerciseApplicationReminder";
		}
		
		if(attributeName != null) {			
			try {
				logger.info("["+username+"] Postponing future reminders ("+attributeName+") until after: "+postponeTime.toString());
				vcDataCollection.setInteractionModelAttribute(attributeName,postponeTime.toString(),1.0d);
				return true;
			} catch (Exception ex) {
				logger.error("["+username+"] Failed to update postpone time for reminder ("+attributeName+"): " +
						ex.getMessage(), ex);
				return false;
			}
		} else {
			return false;
		}
	}
	
	private boolean executeUnlockReward(String item) {
		String url = vcService.getConfiguration().getEWalletUrl() + "/buyItem?username="+username+"&item="+item; 
		
		try {
			RestOperations ewallClient = vcService.getEWallClient();
			Integer response = ewallClient.postForObject(url, null, Integer.class);
			
			logger.info("["+username+"] Attempt to buy item through eWALLET service returned response: '"+response.intValue()+"'.");
			
			if(response.intValue() == 1) {
				return true;
			} else {
				return false;
			}
		} catch(Exception e) {
			logger.error("["+username+"] Error attempting to buy reward '"+item+" through eWALLET service.");
			return false;
		}

	}
	
}
