package eu.ewall.platform.idss.service.model.state.interaction;

import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.service.model.state.StateModel;
import eu.ewall.platform.idss.utils.json.DateTimeFromIsoDateTimeDeserializer;
import eu.ewall.platform.idss.utils.json.IsoDateTimeSerializer;

public class InteractionModel extends StateModel {
			
	@DatabaseField(DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime lastInteractionGoodMorning;
	
	@DatabaseField(DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime lastInteractionCalendarApplication;
	
	@DatabaseField(DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime lastInteractionCognitiveExerciseApplication;
	
	@DatabaseField(DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime lastInteractionDomoticsApplication;
	
	@DatabaseField(DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime lastInteractionMainScreen;
	
	@DatabaseField(DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime lastInteractionMyActivityApplication;
	
	@DatabaseField(DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime lastInteractionMyHealthApplication;
	
	@DatabaseField(DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime lastInteractionSleepApplication;
	
	@DatabaseField(DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime lastInteractionVideoExerciseApplication;
	
	@DatabaseField(DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime postponeCalendarApplicationReminder;
	
	@DatabaseField(DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime postponeCognitiveExerciseApplicationReminder;
	
	@DatabaseField(DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime postponeDomoticsApplicationReminder;
	
	@DatabaseField(DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime postponeActivityApplicationReminder;
	
	@DatabaseField(DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime postponeHealthApplicationReminder;
	
	@DatabaseField(DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime postponeSleepApplicationReminder;
	
	@DatabaseField(DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime postponeVideoExerciseApplicationReminder;
	
	@DatabaseField(DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime postponePAStageOfChangeReminder;

	// ----------------------------- //
	// ---------- Getters ---------- //
	// ----------------------------- //
	
	/**
	 * Returns the date and time as {@link DateTime} object at which the last interaction
	 * of type "GoodMorning" took place for the user.
	 * @return the date and time as {@link DateTime} object at which the last interaction
	 * of type "GoodMorning" took place for the user.
	 */
	public DateTime getLastInteractionGoodMorning() {
		return lastInteractionGoodMorning;
	}
	
	/**
	 * Returns the date and time as {@link DateTime} object at which the last interaction
	 * with the calendar application took place.
	 * @return the date and time as {@link DateTime} object at which the last interaction
	 * with the calendar application took place.
	 */
	public DateTime getLastInteractionCalendarApplication() {
		return lastInteractionCalendarApplication;
	}
	
	/**
	 * Returns the date and time as {@link DateTime} object at which the last interaction
	 * with the cognitive games application took place.
	 * @return the date and time as {@link DateTime} object at which the last interaction
	 * with the cognitive games application took place.
	 */
	public DateTime getLastInteractionCognitiveExerciseApplication() {
		return lastInteractionCognitiveExerciseApplication;
	}
	
	/**
	 * Returns the date and time as {@link DateTime} object at which the last interaction
	 * with the domotics application took place.
	 * @return the date and time as {@link DateTime} object at which the last interaction
	 * with the domotics application took place.
	 */
	public DateTime getLastInteractionDomoticsApplication() {
		return lastInteractionDomoticsApplication;
	}
	
	/**
	 * Returns the date and time as {@link DateTime} object at which the last interaction
	 * with the main screen took place.
	 * @return the date and time as {@link DateTime} object at which the last interaction
	 * with the main screen took place.
	 */
	public DateTime getLastInteractionMainScreen() {
		return lastInteractionMainScreen;
	}
	
	/**
	 * Returns the date and time as {@link DateTime} object at which the last interaction
	 * with the my activity application took place.
	 * @return the date and time as {@link DateTime} object at which the last interaction
	 * with the my activity application took place.
	 */
	public DateTime getLastInteractionMyActivityApplication() {
		return lastInteractionMyActivityApplication;
	}
	
	/**
	 * Returns the date and time as {@link DateTime} object at which the last interaction
	 * with the my health application took place.
	 * @return the date and time as {@link DateTime} object at which the last interaction
	 * with the my health application took place.
	 */
	public DateTime getLastInteractionMyHealthApplication() {
		return lastInteractionMyHealthApplication;
	}
	
	/**
	 * Returns the date and time as {@link DateTime} object at which the last interaction
	 * with the sleep application took place.
	 * @return the date and time as {@link DateTime} object at which the last interaction
	 * with the sleep application took place.
	 */
	public DateTime getLastInteractionSleepApplication() {
		return lastInteractionSleepApplication;
	}
	
	/**
	 * Returns the date and time as {@link DateTime} object at which the last interaction
	 * with the video trainer application took place.
	 * @return the date and time as {@link DateTime} object at which the last interaction
	 * with the video trainer application took place.
	 */
	public DateTime getLastInteractionVideoExerciseApplication() {
		return lastInteractionVideoExerciseApplication;
	}
	
	/**
	 * Returns the date and time until which the user should not receive reminders
	 * to use the calendar application.
	 * @return the date and time until which the user should not receive reminders
	 * to use the calendar application.
	 */
	public DateTime getPostponeCalendarApplicationReminder() {
		return postponeCalendarApplicationReminder;
	}
	
	/**
	 * Returns the date and time until which the user should not receive reminders
	 * to use the cognitive exercise application.
	 * @return the date and time until which the user should not receive reminders
	 * to use the cognitive exercise application.
	 */
	public DateTime getPostponeCognitiveExerciseApplicationReminder() {
		return postponeCognitiveExerciseApplicationReminder;
	}
	
	/**
	 * Returns the date and time until which the user should not receive reminders
	 * to use the domotics application.
	 * @return the date and time until which the user should not receive reminders
	 * to use the domotics application.
	 */
	public DateTime getPostponeDomoticsApplicationReminder() {
		return postponeDomoticsApplicationReminder;
	}
	
	/**
	 * Returns the date and time until which the user should not receive reminders
	 * to use the activity application.
	 * @return the date and time until which the user should not receive reminders
	 * to use the activity application.
	 */
	public DateTime getPostponeActivityApplicationReminder() {
		return postponeActivityApplicationReminder;
	}
	
	/**
	 * Returns the date and time until which the user should not receive reminders
	 * to use the health application.
	 * @return the date and time until which the user should not receive reminders
	 * to use the health application.
	 */
	public DateTime getPostponeHealthApplicationReminder() {
		return postponeHealthApplicationReminder;
	}
	
	/**
	 * Returns the date and time until which the user should not receive reminders
	 * to use the sleep application.
	 * @return the date and time until which the user should not receive reminders
	 * to use the sleep application.
	 */
	public DateTime getPostponeSleepApplicationReminder() {
		return postponeSleepApplicationReminder;
	}
	
	/**
	 * Returns the date and time until which the user should not receive reminders
	 * to use the video exercise application.
	 * @return the date and time until which the user should not receive reminders
	 * to use the video exercise application.
	 */
	public DateTime getPostponeVideoExerciseApplicationReminder() {
		return postponeVideoExerciseApplicationReminder;
	}
	
	/**
	 * Returns the time as {@link DateTime} object before which the user should not be 
	 * bothered with a "PA Stage of Change" dialogue.
	 * @return the time as {@link DateTime} object before which the user should not be 
	 * bothered with a "PA Stage of Change" dialogue.
	 */
	public DateTime getPostponePAStageOfChangeReminder() {
		return postponePAStageOfChangeReminder;
	}
	
	// ----------------------------- //
	// ---------- Setters ---------- //
	// ----------------------------- //
	
	/**
	 * Sets the time as {@link DateTime} object before which the user should not be 
	 * bothered with a "PA Stage of Change" dialogue.
	 * @param postponePAStageOfChangeReminder the time as {@link DateTime} object before which the user should not be 
	 * bothered with a "PA Stage of Change" dialogue.
	 */
	public void setPostponePAStageOfChangeReminder(DateTime postponePAStageOfChangeReminder) {
		this.postponePAStageOfChangeReminder = postponePAStageOfChangeReminder;
	}
	
	/**
	 * Sets the date and time as {@link DateTime} object at which the last interaction
	 * of type "GoodMorning" took place for the user.
	 * @param lastInteractionGoodMorning the date and time as {@link DateTime} object at which the last interaction
	 * of type "GoodMorning" took place for the user.
	 */
	public void setLastInteractionGoodMorning(DateTime lastInteractionGoodMorning) {
		this.lastInteractionGoodMorning = lastInteractionGoodMorning;
	}
	
	/**
	 * Sets the date and time as {@link DateTime} object at which the last interaction
	 * with the calendar application took place.
	 * @param lastInteractionCalendarApplication the date and time as {@link DateTime} object at which the last interaction
	 * with the calendar application took place.
	 */
	public void setLastInteractionCalendarApplication(DateTime lastInteractionCalendarApplication) {
		this.lastInteractionCalendarApplication = lastInteractionCalendarApplication;
	}
	
	/**
	 * Sets the date and time as {@link DateTime} object at which the last interaction
	 * with the cognitive games application took place.
	 * @param lastInteractionCognitiveExerciseApplication the date and time as {@link DateTime} object at which the last interaction
	 * with the cognitive games application took place.
	 */
	public void setLastInteractionCognitiveExerciseApplication(DateTime lastInteractionCognitiveExerciseApplication) {
		this.lastInteractionCognitiveExerciseApplication = lastInteractionCognitiveExerciseApplication;
	}
	
	/**
	 * Sets the date and time as {@link DateTime} object at which the last interaction
	 * with the domotics application took place.
	 * @param lastInteractionDomoticsApplication the date and time as {@link DateTime} object at which the last interaction
	 * with the domotics application took place.
	 */
	public void setLastInteractionDomoticsApplication(DateTime lastInteractionDomoticsApplication) {
		this.lastInteractionDomoticsApplication = lastInteractionDomoticsApplication;
	}
	
	/**
	 * Sets the date and time as {@link DateTime} object at which the last interaction
	 * with the main screen took place.
	 * @param lastInteractionMainScreen the date and time as {@link DateTime} object at which the last interaction
	 * with the main screen took place.
	 */
	public void setLastInteractionMainScreen(DateTime lastInteractionMainScreen) {
		this.lastInteractionMainScreen = lastInteractionMainScreen;
	}
	
	/**
	 * Sets the date and time as {@link DateTime} object at which the last interaction
	 * with the my activity application took place.
	 * @param lastInteractionMyActivityApplication the date and time as {@link DateTime} object at which the last interaction
	 * with the my activity application took place.
	 */
	public void setLastInteractionMyActivityApplication(DateTime lastInteractionMyActivityApplication) {
		this.lastInteractionMyActivityApplication = lastInteractionMyActivityApplication;
	}
	
	/**
	 * Sets the date and time as {@link DateTime} object at which the last interaction
	 * with the my health application took place.
	 * @param lastInteractionMyHealthApplication the date and time as {@link DateTime} object at which the last interaction
	 * with the my health application took place.
	 */
	public void setLastInteractionMyHealthApplication(DateTime lastInteractionMyHealthApplication) {
		this.lastInteractionMyHealthApplication = lastInteractionMyHealthApplication;
	}
	
	/**
	 * Sets the date and time as {@link DateTime} object at which the last interaction
	 * with the sleep application took place.
	 * @param lastInteractionSleepApplication the date and time as {@link DateTime} object at which the last interaction
	 * with the sleep application took place.
	 */
	public void setLastInteractionSleepApplication(DateTime lastInteractionSleepApplication) {
		this.lastInteractionSleepApplication = lastInteractionSleepApplication;
	}
	
	/**
	 * Sets the date and time as {@link DateTime} object at which the last interaction
	 * with the video trainer application took place.
	 * @param lastInteractionVideoExerciseApplication the date and time as {@link DateTime} object at which the last interaction
	 * with the video trainer application took place.
	 */
	public void setLastInteractionVideoExerciseApplication(DateTime lastInteractionVideoExerciseApplication) {
		this.lastInteractionVideoExerciseApplication = lastInteractionVideoExerciseApplication;
	}
	
	/**
	 * Sets the date and time until which the user should not receive reminders
	 * to use the sleep application.
	 * @param postponeSleepApplicationReminder the date and time until which the user should not receive reminders
	 * to use the sleep application.
	 */
	public void setPostponeSleepApplicationReminder(DateTime postponeSleepApplicationReminder) {
		this.postponeSleepApplicationReminder = postponeSleepApplicationReminder;
	}
	
	/**
	 * Sets the date and time until which the user should not receive reminders
	 * to use the calendar application.
	 * @param postponeCalendarApplicationReminder the date and time until which the user should not receive reminders
	 * to use the calendar application.
	 */
	public void setPostponeCalendarApplicationReminder(DateTime postponeCalendarApplicationReminder) {
		this.postponeCalendarApplicationReminder = postponeCalendarApplicationReminder;
	}
	
	/**
	 * Sets the date and time until which the user should not receive reminders
	 * to use the cognitive exercise application.
	 * @param postponeCognitiveExerciseApplicationReminder the date and time until which the user should not receive reminders
	 * to use the cognitive exercise application.
	 */
	public void setPostponeCognitiveExerciseApplicationReminder(DateTime postponeCognitiveExerciseApplicationReminder) {
		this.postponeCognitiveExerciseApplicationReminder = postponeCognitiveExerciseApplicationReminder;
	}
	
	/**
	 * Sets the date and time until which the user should not receive reminders
	 * to use the domotics application.
	 * @param postponeDomoticsApplicationReminder the date and time until which the user should not receive reminders
	 * to use the domotics application.
	 */
	public void setPostponeDomoticsApplicationReminder(DateTime postponeDomoticsApplicationReminder) {
		this.postponeDomoticsApplicationReminder = postponeDomoticsApplicationReminder;
	}
	
	/**
	 * Sets the date and time until which the user should not receive reminders
	 * to use the activity application.
	 * @param postponeActivityApplicationReminder the date and time until which the user should not receive reminders
	 * to use the activity application.
	 */
	public void setPostponeActivityApplicationReminder(DateTime postponeActivityApplicationReminder) {
		this.postponeActivityApplicationReminder = postponeActivityApplicationReminder;
	}
	
	/**
	 * Sets the date and time until which the user should not receive reminders
	 * to use the health application.
	 * @param postponeHealthApplicationReminder the date and time until which the user should not receive reminders
	 * to use the health application.
	 */
	public void setPostponeHealthApplicationReminder(DateTime postponeHealthApplicationReminder) {
		this.postponeHealthApplicationReminder = postponeHealthApplicationReminder;
	}
	
	/**
	 * Sets the date and time until which the user should not receive reminders
	 * to use the video exercise application.
	 * @param postponeVideoExerciseApplicationReminder the date and time until which the user should not receive reminders
	 * to use the video exercise application.
	 */
	public void setPostponeVideoExerciseApplicationReminder(DateTime postponeVideoExerciseApplicationReminder) {
		this.postponeVideoExerciseApplicationReminder = postponeVideoExerciseApplicationReminder;
	}
	
}
