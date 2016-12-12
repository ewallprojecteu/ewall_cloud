package eu.ewall.platform.reasoner.activitycoach.service.dialogue;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import eu.ewall.platform.commons.datamodel.profile.RewardType;
import eu.ewall.platform.idss.service.model.common.dialogue.DialogueBasicReply;
import eu.ewall.platform.idss.service.model.common.dialogue.DialogueCondition;
import eu.ewall.platform.idss.service.model.common.dialogue.DialogueConstants;
import eu.ewall.platform.idss.service.model.common.dialogue.DialogueReply;
import eu.ewall.platform.idss.service.model.common.dialogue.DialogueStep;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.reasoner.activitycoach.service.VCDataCollection;

public class DialogueConditionParser {
	
	private static final String LOGTAG = "DialogueConditionParser";
	private VCDataCollection vcDataCollection;
	private Logger logger;
	
	public DialogueConditionParser(VCDataCollection vcDataCollection) {
		this.vcDataCollection = vcDataCollection;
		logger = AppComponents.getLogger(LOGTAG);
	}
	
	public DialogueStep parse(DialogueStep dialogueStep) {
		String username = vcDataCollection.getUsername();
		
		List<DialogueReply> dialogueReplies = dialogueStep.getDialogueReplies();
		List<DialogueReply> parsedDialogueReplies = new ArrayList<DialogueReply>();
		
		for(DialogueReply dialogueReply : dialogueReplies) {
			
			String replyStatement = null;
			if(dialogueReply instanceof DialogueBasicReply) {
				replyStatement = ((DialogueBasicReply)dialogueReply).getStatement();
			}
			
			boolean removeDialogueReply = false;
			List<DialogueCondition> conditions = dialogueReply.getConditions();
			if((conditions != null) && (!conditions.isEmpty())) {
				for(int i=0; i<conditions.size(); i++) {
					DialogueCondition condition = conditions.get(i);
					String conditionType = condition.getConditionType();
					List<String> parameters = condition.getParameters();
					
					if(conditionType.equals(DialogueConstants.DC_TOTAL_COINS_MINIMUM)) {
						
						try {
							Integer totalCoins = vcDataCollection.getTotalCoins();
							Integer coinParameter = new Integer(parameters.get(0));
							if(totalCoins < coinParameter) {
								removeDialogueReply = true;
								logger.info("["+username+"] Removing Dialogue Reply option '"+replyStatement+"', reason: lack of coins ("+totalCoins+"/"+coinParameter+")");
							}
							
						} catch(Exception e) {
							logger.error("["+username+"] Error parsing DialogueStep, unable to retrieve total coins for user: "+e.getMessage());
						}
						
					} else if(conditionType.equals(DialogueConstants.DC_TOTAL_COINS_MAXIMUM)) {
						try {
							Integer totalCoins = vcDataCollection.getTotalCoins();
							Integer coinParameter = new Integer(parameters.get(0));
							if(totalCoins >= coinParameter) {
								removeDialogueReply = true;
								logger.info("["+username+"] Removing Dialogue Reply option '"+replyStatement+"', reason: too much coins ("+totalCoins+"/"+coinParameter+")");
							}
							
						} catch(Exception e) {
							logger.error("["+username+"] Error parsing DialogueStep, unable to retrieve total coins for user.");
						}
					
					} else if(conditionType.equals(DialogueConstants.DC_REWARD_AVAILABLE)) {
						try {
							String rewardParameter = parameters.get(0);
							List<String> unlockedRewards = vcDataCollection.getUnlockedRewards();
							
							// Remove dialogue reply if no rewards are available
							if(rewardParameter.equals("ANY")) {
								if(unlockedRewards.size() >= (RewardType.values().length - 3)) {
									removeDialogueReply = true;
									logger.info("["+username+"] Removing Dialogue Reply option '"+replyStatement+"', reason: all rewards already unlocked.");
								}
							
							// Remove dialogue reply if any rewards are available
							} else if(rewardParameter.equals("NONE")) {
								if(unlockedRewards.size() < (RewardType.values().length - 3)) {
									removeDialogueReply = true;
									logger.info("["+username+"] Removing Dialogue Reply option '"+replyStatement+"', reason: unlocked rewards still available.");
								}
							
							// Remove dialogue reply if given reward is already unlocked
							} else if(unlockedRewards.contains(rewardParameter)) {
								removeDialogueReply = true;
								logger.info("["+username+"] Removing Dialogue Reply option '"+replyStatement+"', reason: reward ("+rewardParameter+") already unlocked.");
							}
						} catch(Exception e) {
							logger.error("["+username+"] Error parsing DialogueStep, unable to retrieve unlocked rewards for user.");
						}
					}
					
				}
			}
			
			if(!removeDialogueReply) {
				parsedDialogueReplies.add(dialogueReply);
			}
		}
		
		dialogueStep.setDialogueReplies(parsedDialogueReplies);		
		return dialogueStep;
	}
	
}
