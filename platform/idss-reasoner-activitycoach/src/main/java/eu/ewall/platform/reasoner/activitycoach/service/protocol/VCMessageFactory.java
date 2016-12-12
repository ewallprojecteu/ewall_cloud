package eu.ewall.platform.reasoner.activitycoach.service.protocol;

import java.util.List;

import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.dialogue.DialogueAction;
import eu.ewall.platform.idss.service.model.common.dialogue.DialogueBasicReply;
import eu.ewall.platform.idss.service.model.common.dialogue.DialogueConstants;
import eu.ewall.platform.idss.service.model.common.dialogue.DialogueReply;
import eu.ewall.platform.idss.service.model.common.dialogue.DialogueStep;
import eu.ewall.platform.idss.utils.i18n.I18n;
import eu.ewall.platform.idss.utils.i18n.I18nLoader;
import eu.ewall.platform.reasoner.activitycoach.service.VCConfiguration;

public class VCMessageFactory {
	
	private VCConfiguration config;
	
	public VCMessageFactory(VCConfiguration config) {
		this.config = config;
	}
	
	public VCMessageAvailable generatePAMMAvailable(IDSSUserProfile idssUserProfile) {
		
		// Define the content of the pamm-available message
		I18n i18n = I18nLoader.getInstance().getI18n("messages_activity_coach",
				idssUserProfile.getLocales(), true, null); // TODO: Automatically set these 'honorifics' (now: true)
		String pammAvailableContent = i18n.get("pamm_available");
		
		VCMessageAvailable vcMessage = new VCMessageAvailable();
		vcMessage.setType(VCMessageConstants.MESSAGE_TYPE_AVAILABLE);
		vcMessage.setSubType(VCMessageConstants.MESSAGE_SUBTYPE_PAMM);
		vcMessage.setContent(pammAvailableContent);
		vcMessage.setCallbackURL(config.getServiceBaseUrl()+"/generatePAMM?username="+idssUserProfile.getUsername());
		return vcMessage;
	}
	
	public VCMessageAvailable generateDialogueAvailable(IDSSUserProfile idssUserProfile, String dialogueTypeId) {
		I18n i18n = I18nLoader.getInstance().getI18n("messages_activity_coach",
				idssUserProfile.getLocales(), true, null); // TODO: Automatically set these 'honorifics' (now: true)
		
		VCMessageAvailable vcMessage = new VCMessageAvailable();
		vcMessage.setType(VCMessageConstants.MESSAGE_TYPE_AVAILABLE);
		vcMessage.setSubType(VCMessageConstants.MESSAGE_SUBTYPE_DIALOGUE);
		
		String content = "";
		if(dialogueTypeId.equals(DialogueConstants.DT_PHYSICAL_ACTIVITY_STAGE_OF_CHANGE)) {
			 content = i18n.get("stageofchange_pa_available");
		} else if(dialogueTypeId.equals(DialogueConstants.DT_GOOD_MORNING)) {
			content = i18n.get("dt_goodmorning_available");
		} else if(dialogueTypeId.equals(DialogueConstants.DT_USE_SLEEP_APPLICATION)) {
			content = i18n.get("dt_useapplication_available");
		} else if(dialogueTypeId.equals(DialogueConstants.DT_USE_ACTIVITY_APPLICATION)) {
			content = i18n.get("dt_useapplication_available");
		} else if(dialogueTypeId.equals(DialogueConstants.DT_USE_CALENDAR_APPLICATION)) {
			content = i18n.get("dt_useapplication_available");
		} else if(dialogueTypeId.equals(DialogueConstants.DT_USE_COGNITIVE_EXERCISE_APPLICATION)) {
			content = i18n.get("dt_useapplication_available");
		} else if(dialogueTypeId.equals(DialogueConstants.DT_USE_DOMOTICS_APPLICATION)) {
			content = i18n.get("dt_useapplication_available");
		} else if(dialogueTypeId.equals(DialogueConstants.DT_USE_HEALTH_APPLICATION)) {
			content = i18n.get("dt_useapplication_available");
		} else if(dialogueTypeId.equals(DialogueConstants.DT_USE_VIDEO_EXERCISE_APPLICATION)) {
			content = i18n.get("dt_useapplication_available");
		}
		
		vcMessage.setContent(content);
		vcMessage.setCallbackURL(config.getServiceBaseUrl()+"/startDialogue?username="+idssUserProfile.getUsername()+"&dialogueTypeId="+dialogueTypeId);
		
		return vcMessage;		
	}
	
	public VCMessageContent generateVCMessageContent(DialogueStep dialogueStep, IDSSUserProfile idssUserProfile) {
		
		VCMessageContent vcMessage = new VCMessageContent();
		vcMessage.setType(VCMessageConstants.MESSAGE_TYPE_CONTENT);
		vcMessage.setSubType(VCMessageConstants.MESSAGE_SUBTYPE_DIALOGUE);
		
		VCStatement[] statements = new VCStatement[1];
		VCStatement statement = new VCStatement();
		statement.setType(VCMessageConstants.STATEMENT_TYPE_TEXT);
		statement.setValue(dialogueStep.getStatement());
		statements[0] = statement;
		vcMessage.setStatements(statements);
		
		VCResponse[] responses = new VCResponse[dialogueStep.getDialogueReplies().size()];
		int index = 0;
		for(DialogueReply dialogueReply : dialogueStep.getDialogueReplies()) {
			VCResponse response = new VCResponse();
						
			if(dialogueReply instanceof DialogueBasicReply) {
				DialogueBasicReply dialogueBasicReply = (DialogueBasicReply)dialogueReply;
				response.setType(VCMessageConstants.RESPONSE_TYPE_BASIC);
				response.setLabel(dialogueBasicReply.getStatement());
				
				String responseAction = VCMessageConstants.ACTION_FINISHED; // by default, assume finished
				
				if(dialogueBasicReply.getNextStepId() != null) {
					responseAction = VCMessageConstants.ACTION_DIALOGUE_CONTINUE;
				} else {
					List<DialogueAction> actions = dialogueBasicReply.getActions();
					for(DialogueAction action : actions) {
						if(action.getActionType().equals(DialogueConstants.DA_START_DIALOGUE)) {
							responseAction = VCMessageConstants.ACTION_DIALOGUE_CONTINUE;
						}						
						if(action.getActionType().equals(DialogueConstants.DA_OPEN_APPLICATION)) {
							responseAction = VCMessageConstants.ACTION_OPEN_APPLICATION+":"+action.getParameters().get(0);
						}
						
						// If the action has a "nextStepIdSuccess" (should then also have "nextStepIdFailure"),
						// there is no explicit nextStepId set, but there will be one, so CONTINUE
						if(action.getNextStepIdSuccess() != null) {
							responseAction = VCMessageConstants.ACTION_DIALOGUE_CONTINUE;
						}
					}
				}
				response.setAction(responseAction);
				response.setUrl(config.getServiceBaseUrl()+"/progressDialogue?username="+idssUserProfile.getUsername()+"&replyId="+dialogueBasicReply.getReplyId());
				responses[index] = response;
			}
			index++;
		}
		vcMessage.setResponses(responses);
		
		return vcMessage;
	}

}
