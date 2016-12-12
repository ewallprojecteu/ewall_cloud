package eu.ewall.platform.idss.service.model.common.dialogue;

/**
 * A set of constant variables that identify names of
 * dialogueTypeId's and specific dialogue actions.
 * 
 * @author Harm op den Akker (Roessingh Research and Development)
 */
public class DialogueConstants {
	
	// Dialogue Type ids:
	public static final String DT_PHYSICAL_ACTIVITY_STAGE_OF_CHANGE = "PhysicalActivityStageOfChange";
	public static final String DT_HELLO_ROBIN = "HelloRobin";
	public static final String DT_GOOD_MORNING = "GoodMorning";
	public static final String DT_USE_SLEEP_APPLICATION = "UseSleepApplication";
	public static final String DT_USE_VIDEO_EXERCISE_APPLICATION = "UseVideoExerciseApplication";
	public static final String DT_USE_CALENDAR_APPLICATION = "UseCalendarApplication";
	public static final String DT_USE_COGNITIVE_EXERCISE_APPLICATION = "UseCognitiveExerciseApplication";
	public static final String DT_USE_DOMOTICS_APPLICATION = "UseDomoticsApplication";
	public static final String DT_USE_ACTIVITY_APPLICATION = "UseActivityApplication";
	public static final String DT_USE_HEALTH_APPLICATION = "UseHealthApplication";
	public static final String DT_REWARDS = "Rewards";

	// DialogueAction types:
	public static final String DA_STAGE_OF_CHANGE_UPDATE = "stageOfChangeUpdate";
	public static final String DA_START_DIALOGUE = "startDialogue";
	public static final String DA_OPEN_APPLICATION = "openApplication";
	public static final String DA_POSTPONE_REMINDER = "postponeReminder";
	public static final String DA_UNLOCK_REWARD = "unlockReward";
	
	// DialogueCondition types:
	public static final String DC_TOTAL_COINS_MINIMUM = "totalCoinsMinimum";
	public static final String DC_TOTAL_COINS_MAXIMUM = "totalCoinsMaximum";
	public static final String DC_REWARD_AVAILABLE = "rewardAvailable";
	
}