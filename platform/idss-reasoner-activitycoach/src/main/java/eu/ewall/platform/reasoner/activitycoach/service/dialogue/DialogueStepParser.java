package eu.ewall.platform.reasoner.activitycoach.service.dialogue;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;

import eu.ewall.platform.commons.datamodel.profile.RewardCosts;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.dialogue.DialogueAction;
import eu.ewall.platform.idss.service.model.common.dialogue.DialogueBasicReply;
import eu.ewall.platform.idss.service.model.common.dialogue.DialogueCondition;
import eu.ewall.platform.idss.service.model.common.dialogue.DialogueReply;
import eu.ewall.platform.idss.service.model.common.dialogue.DialogueStep;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.idss.utils.datetime.VirtualClock;
import eu.ewall.platform.idss.utils.i18n.I18n;
import eu.ewall.platform.idss.utils.i18n.I18nLoader;
import eu.ewall.platform.reasoner.activitycoach.service.VCDataCollection;

public class DialogueStepParser {
	
	private static final String LOGTAG = "DialogueStepParser";
	private VCDataCollection vcDataCollection;
	private String username;
	private Logger logger;
	
	public DialogueStepParser(VCDataCollection vcDataCollection) {
		this.vcDataCollection = vcDataCollection;
		username = vcDataCollection.getUsername();
		logger = AppComponents.getLogger(LOGTAG);
	}
	
	public DialogueStep parse(DialogueStep dialogueStep) {
		
		// Parse the statement
		String statement = dialogueStep.getStatement();
		statement = parseString(statement);
		dialogueStep.setStatement(statement);
			
		// Parse the reply options
		for(DialogueReply dialogueReply : dialogueStep.getDialogueReplies()) {
			if(dialogueReply instanceof DialogueBasicReply) {
				DialogueBasicReply basicReply = (DialogueBasicReply)dialogueReply;
				
				// Parse the reply statements
				String replyStatement = basicReply.getStatement();
				replyStatement = parseString(replyStatement);
				basicReply.setStatement(replyStatement);
			}
			
			// Parse the condition parameters
			for(DialogueCondition dialogueCondition : dialogueReply.getConditions()) {
				List<String> parsedParameters = new ArrayList<String>();
				for(String conditionParameter : dialogueCondition.getParameters()) {
					conditionParameter = parseString(conditionParameter);
					parsedParameters.add(conditionParameter);
				}
				dialogueCondition.setParameters(parsedParameters);
			}
			
			// Parse the action parameters
			for(DialogueAction dialogueAction : dialogueReply.getActions()) {
				List<String> parsedParameters = new ArrayList<String>();
				for(String actionParameter : dialogueAction.getParameters()) {
					actionParameter = parseString(actionParameter);
					parsedParameters.add(actionParameter);
				}
				dialogueAction.setParameters(parsedParameters);
			}
		}
		
		return dialogueStep;
	}
	
	private String parseString(String stringToParse) {
		// [firstName]
		if(stringToParse.contains("[firstName]")) {
			stringToParse = stringToParse.replaceAll("\\[firstName\\]", vcDataCollection.getUserModel().getFirstName());
		}
		
		// [daysAgoLastSleepInteraction]
		// [timeLastSleepInteraction]
		if(stringToParse.contains("[daysAgoLastSleepInteraction]") || stringToParse.contains("[timeLastSleepInteraction]")) {
			try {
				DateTime lastInteractionTime = vcDataCollection.getLastInteractionSleepApplication();
				String daysAgoLastInteraction = getDaysAgoString(vcDataCollection.getIDSSUserProfile(),lastInteractionTime);
				stringToParse = stringToParse.replaceAll("\\[daysAgoLastSleepInteraction\\]", daysAgoLastInteraction);
	
				DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
				String lastInteractionTimeString = fmt.print(lastInteractionTime);
				stringToParse = stringToParse.replaceAll("\\[timeLastSleepInteraction\\]", lastInteractionTimeString);			
			} catch(Exception e) {
				logger.error("["+username+"] Error parsing DialogueStep, unable to retrieve last interaction with Sleep application.",e);
			}
		}
		
		// [daysAgoLastVideoTrainerInteraction]
		// [timeLastVideoTrainerInteraction]
		if(stringToParse.contains("[daysAgoLastVideoTrainerInteraction]") || stringToParse.contains("[timeLastVideoTrainerInteraction]")) {
			try {
				DateTime lastInteractionTime = vcDataCollection.getLastInteractionVideoExerciseApplication();
				String daysAgoLastInteraction = getDaysAgoString(vcDataCollection.getIDSSUserProfile(),lastInteractionTime);
				stringToParse = stringToParse.replaceAll("\\[daysAgoLastVideoTrainerInteraction\\]", daysAgoLastInteraction);
	
				DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
				String lastInteractionTimeString = fmt.print(lastInteractionTime);
				stringToParse = stringToParse.replaceAll("\\[timeLastVideoTrainerInteraction\\]", lastInteractionTimeString);			
			} catch(Exception e) {
				logger.error("["+username+"] Error parsing DialogueStep, unable to retrieve last interaction with Video Trainer application.",e);
			}
		}
		
		// [daysAgoLastCalendarInteraction]
		// [timeLastCalendarInteraction]
		if(stringToParse.contains("[daysAgoLastCalendarInteraction]") || stringToParse.contains("[timeLastCalendarInteraction]")) {
			try {
				DateTime lastInteractionTime = vcDataCollection.getLastInteractionCalendarApplication();
				String daysAgoLastInteraction = getDaysAgoString(vcDataCollection.getIDSSUserProfile(),lastInteractionTime);
				stringToParse = stringToParse.replaceAll("\\[daysAgoLastCalendarInteraction\\]", daysAgoLastInteraction);
	
				DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
				String lastInteractionTimeString = fmt.print(lastInteractionTime);
				stringToParse = stringToParse.replaceAll("\\[timeLastCalendarInteraction\\]", lastInteractionTimeString);			
			} catch(Exception e) {
				logger.error("["+username+"] Error parsing DialogueStep, unable to retrieve last interaction with Calendar application.",e);
			}
		}
		
		// [daysAgoLastCognitiveTrainerInteraction]
		// [timeLastCognitiveTrainerInteraction]
		if(stringToParse.contains("[daysAgoLastCognitiveTrainerInteraction]") || stringToParse.contains("[timeLastCognitiveTrainerInteraction]")) {
			try {
				DateTime lastInteractionTime = vcDataCollection.getLastInteractionCognitiveExerciseApplication();
				String daysAgoLastInteraction = getDaysAgoString(vcDataCollection.getIDSSUserProfile(),lastInteractionTime);
				stringToParse = stringToParse.replaceAll("\\[daysAgoLastCognitiveTrainerInteraction\\]", daysAgoLastInteraction);
	
				DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
				String lastInteractionTimeString = fmt.print(lastInteractionTime);
				stringToParse = stringToParse.replaceAll("\\[timeLastCognitiveTrainerInteraction\\]", lastInteractionTimeString);			
			} catch(Exception e) {
				logger.error("["+username+"] Error parsing DialogueStep, unable to retrieve last interaction with Cognitive Exercise application.",e);
			}
		}		
		
		// [daysAgoLastDomoticsInteraction]
		// [timeLastDomoticsInteraction]
		if(stringToParse.contains("[daysAgoLastDomoticsInteraction]") || stringToParse.contains("[timeLastDomoticsInteraction]")) {
			try {
				DateTime lastInteractionTime = vcDataCollection.getLastInteractionDomoticsApplication();
				String daysAgoLastInteraction = getDaysAgoString(vcDataCollection.getIDSSUserProfile(),lastInteractionTime);
				stringToParse = stringToParse.replaceAll("\\[daysAgoLastDomoticsInteraction\\]", daysAgoLastInteraction);
	
				DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
				String lastInteractionTimeString = fmt.print(lastInteractionTime);
				stringToParse = stringToParse.replaceAll("\\[timeLastDomoticsInteraction\\]", lastInteractionTimeString);			
			} catch(Exception e) {
				logger.error("["+username+"] Error parsing DialogueStep, unable to retrieve last interaction with Domotics application.",e);
			}
		}	
		
		// [daysAgoLastActivityInteraction]
		// [timeLastActivityInteraction]
		if(stringToParse.contains("[daysAgoLastActivityInteraction]") || stringToParse.contains("[timeLastActivityInteraction]")) {
			try {
				DateTime lastInteractionTime = vcDataCollection.getLastInteractionMyActivityApplication();
				String daysAgoLastInteraction = getDaysAgoString(vcDataCollection.getIDSSUserProfile(),lastInteractionTime);
				stringToParse = stringToParse.replaceAll("\\[daysAgoLastActivityInteraction\\]", daysAgoLastInteraction);
	
				DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
				String lastInteractionTimeString = fmt.print(lastInteractionTime);
				stringToParse = stringToParse.replaceAll("\\[timeLastActivityInteraction\\]", lastInteractionTimeString);			
			} catch(Exception e) {
				logger.error("["+username+"] Error parsing DialogueStep, unable to retrieve last interaction with My Activity application.",e);
			}
		}
		
		// [daysAgoLastHealthInteraction]
		// [timeLastHealthInteraction]
		if(stringToParse.contains("[daysAgoLastHealthInteraction]") || stringToParse.contains("[timeLastHealthInteraction]")) {
			try {
				DateTime lastInteractionTime = vcDataCollection.getLastInteractionMyHealthApplication();
				String daysAgoLastInteraction = getDaysAgoString(vcDataCollection.getIDSSUserProfile(),lastInteractionTime);
				stringToParse = stringToParse.replaceAll("\\[daysAgoLastHealthInteraction\\]", daysAgoLastInteraction);
	
				DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
				String lastInteractionTimeString = fmt.print(lastInteractionTime);
				stringToParse = stringToParse.replaceAll("\\[timeLastHealthInteraction\\]", lastInteractionTimeString);			
			} catch(Exception e) {
				logger.error("["+username+"] Error parsing DialogueStep, unable to retrieve last interaction with My Health application.",e);
			}
		}		
		
		// [totalCoins]
		if(stringToParse.contains("[totalCoins]")) {
			try {
				Integer totalCoins = vcDataCollection.getTotalCoins();
				stringToParse = stringToParse.replaceAll("\\[totalCoins\\]",totalCoins.toString());
			} catch(Exception e) {
				logger.error("["+username+"] Error parsing DialogueStep, unable to retrieve total coins for user.");
			}
		}
		
		// [rewardCostsWallpaperOneSixties]
		if(stringToParse.contains("[rewardCostsWallpaperOneSixties]")) {
			stringToParse = stringToParse.replaceAll("\\[rewardCostsWallpaperOneSixties\\]",new Integer(RewardCosts.REWARD_COST_WALLPAPER_ONE_SIXTIES).toString());
		}
		
		// [rewardCostsWallpaperTwoStripes]
		if(stringToParse.contains("[rewardCostsWallpaperTwoStripes]")) {
			stringToParse = stringToParse.replaceAll("\\[rewardCostsWallpaperTwoStripes\\]",new Integer(RewardCosts.REWARD_COST_WALLPAPER_TWO_STRIPES).toString());
		}
		
		// [rewardCostsWallpaperThreeBlue]
		if(stringToParse.contains("[rewardCostsWallpaperThreeBlue]")) {
			stringToParse = stringToParse.replaceAll("\\[rewardCostsWallpaperThreeBlue\\]",new Integer(RewardCosts.REWARD_COST_WALLPAPER_THREE_BLUE).toString());
		}
		
		// [rewardCostsWallpaperFourYellow]
		if(stringToParse.contains("[rewardCostsWallpaperFourYellow]")) {
			stringToParse = stringToParse.replaceAll("\\[rewardCostsWallpaperFourYellow\\]",new Integer(RewardCosts.REWARD_COST_WALLPAPER_FOUR_YELLOW).toString());
		}
		
		return stringToParse;
	}
	
	private String getDaysAgoString(IDSSUserProfile idssUserProfile, DateTime lastInteraction) {
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		I18n i18n = I18nLoader.getInstance().getI18n("messages_activity_coach",
				idssUserProfile.getLocales(), true, null);
		
		int daysAgo = Days.daysBetween(lastInteraction.toLocalDate(), now.toLocalDate()).getDays();
		String daysAgoString = new Integer(daysAgo).toString();
		if(daysAgo == 0) {
			return i18n.get("today");
		} else if(daysAgo == 1) {
			return daysAgoString+" "+i18n.get("dayAgo");
		} else {
			return daysAgoString+" "+i18n.get("daysAgo");
		}

	}
	
}
