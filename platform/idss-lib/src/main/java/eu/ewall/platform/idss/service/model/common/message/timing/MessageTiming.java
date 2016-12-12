package eu.ewall.platform.idss.service.model.common.message.timing;

import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.service.model.common.message.PhysicalActivityMotivationalMessage;
import eu.ewall.platform.idss.utils.json.DateTimeFromIsoDateTimeDeserializer;
import eu.ewall.platform.idss.utils.json.IsoDateTimeSerializer;

/**
 * The timing component of a {@link PhysicalActivityMotivationalMessage}.
 * 
 * @author Dennis Hofs (RRD)
 * @author Harm op den Akker (RRD)
 */
public class MessageTiming extends AbstractDatabaseObject {
	
	@DatabaseField(value=DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime userNotifiedTime = null;
	
	@DatabaseField(value=DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime contentGeneratedTime = null;
	
	@DatabaseField(value=DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime userObservedTime = null;
	
	@DatabaseField(value=DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime expirationTime = null;
	
	@DatabaseField(value=DatabaseType.INT)
	private boolean systemInitiated = true;
	
	@DatabaseField(value=DatabaseType.INT)
	private boolean errorRecovery = false;
	
	@DatabaseField(value=DatabaseType.INT)
	private boolean requireGreeting = false;
	
	@DatabaseField(value=DatabaseType.INT)
	private boolean requireFeedback = false;

	@DatabaseField(value=DatabaseType.INT)
	private boolean requireArgument = false;

	@DatabaseField(value=DatabaseType.INT)
	private boolean requireSuggestion = false;
	
	@DatabaseField(value=DatabaseType.INT)
	private boolean requireReinforcement = false;
	
	@DatabaseField(value=DatabaseType.INT)
	private boolean requireSynchronizeSensor = false;
	
	// ---------- Getters ---------- //
	
	/**
	 * Returns the time when the user was notified of the message, in the time
	 * zone of the user. This is set to the creation time of the {@link MessageTiming} object.
	 * 
	 * @return the time when the user was notified of the message or null
	 */
	public DateTime getUserNotifiedTime() {
		return userNotifiedTime;
	}
	
	/**
	 * Returns the time at which the intention, content and representation parts of 
	 * this {@link PhysicalActivityMotivationalMessage} were generated.
	 * @return the time at which the intention, content and representation parts of 
	 * this {@link PhysicalActivityMotivationalMessage} were generated.
	 */
	public DateTime getContentGeneratedTime() {
		return contentGeneratedTime;
	}
	
	/**
	 * Returns the time when the user observed the message, in the time zone of
	 * the user. This is null if the user has not observed it yet.
	 * 
	 * @return the time when the user observed the message
	 */
	public DateTime getUserObservedTime() {
		return userObservedTime;
	}
	
	/**
	 * Returns the time at which the 'available' notification for this 
	 * {@link PhysicalActivityMotivationalMessage} should expire.
	 * @return the time at which the 'available' notification for this 
	 * {@link PhysicalActivityMotivationalMessage} should expire.
	 */
	public DateTime getExpirationTime() {
		return expirationTime;
	}
	
	/**
	 * Returns whether the message was system-initiated or user-initiated.
	 * 
	 * @return true if the message was system-initiated, false if it was
	 * user-initiated
	 */
	public boolean isSystemInitiated() {
		return systemInitiated;
	}
	
	/**
	 * When the {@link MessageTiming#isSystemInitiated()} method returns {@code true}, this implies
	 * that the message was automatically generated by a system feature. However, in some cases,
	 * the automated request may have originated from an erroneous system state. In this case, this parameter
	 * will return {@code true}.
	 * <br><br>
	 * This can occur due to the two-step nature of pamm-generation where in the first step only the
	 * {@link MessageTiming} element is generated, and in a second step (upon user interaction) the message
	 * intention, content and representation are generated. If the initially generated timing element is lost
	 * due to a component crash or network failure and the UI element requests to "finish" this message, a new
	 * message with new timing is generated and this parameter set to {@code true}.
	 * 
	 * @return {@code true} if this message was generated to prevent an error state, {@code false} otherwise.
	 */
	public boolean isErrorRecovery() {
		return errorRecovery;
	}
	
	/**
	 * For user-initiated messages, this method returns whether the user wants
	 * to receive a greeting intention from the message. For system-initiated messages
	 * this should be false.
	 * 
	 * @return true if the user wants a greeting intention from the message.
	 */
	public boolean isRequireGreeting() {
		return requireGreeting;
	}
	
	/**
	 * For user-initiated messages, this method returns whether the user wants
	 * a feedback intention from the message. For system-initiated messages
	 * this should be false.
	 * 
	 * @return true if the user wants a feedback intention from the message
	 */
	public boolean isRequireFeedback() {
		return requireFeedback;
	}
	
	/**
	 * For user-initiated messages, this method returns whether the user wants
	 * an argument intention from the message. For system-initiated messages
	 * this should be false.
	 * 
	 * @return true if the user wants an argument intention from the message
	 */
	public boolean isRequireArgument() {
		return requireArgument;
	}
	
	/**
	 * For user-initiated messages, this method returns whether the user wants
	 * a suggestion intention from the message. For system-initiated messages this 
	 * should be false.
	 * 
	 * @return true if the user wants a suggestion intention.
	 * from the message
	 */
	public boolean isRequireSuggestion() {
		return requireSuggestion;
	}
	
	/**
	 * For user-initiated messages, this method returns whether the user wants
	 * a reinforcement intention from the message. For system-initiated messages this
	 * should be false.
	 * 
	 * @return true if the user wants a reinforcement intention from the message.
	 */
	public boolean isRequireReinforcement() {
		return requireReinforcement;
	}
	
	/**
	 * For user-initiated messages, this method returns whether the user wants
	 * a synchronizeSensor intention from the message. For system-initiated messages this
	 * should be false.
	 * 
	 * @return true if the user wants a synchronizeSensor intention from the message.
	 */
	public boolean isRequireSynchronizeSensor() {
		return requireSynchronizeSensor;
	}

	// ---------- Setters ---------- //
	
	/**
	 * Sets the time when the user was notified of the message, in the time
	 * zone of the user. This is set to the creation time of the {@link MessageTiming} object.
	 * 
	 * @param userNotifiedTime the time when the user was notified of the
	 * message
	 */
	public void setUserNotifiedTime(DateTime userNotifiedTime) {
		this.userNotifiedTime = userNotifiedTime;
	}
		
	/**
	 * Sets the time at which the intention, content and representation parts of 
	 * this {@link PhysicalActivityMotivationalMessage} were generated.
	 * @param contentGeneratedTime the time at which the intention, content and representation parts of 
	 * this {@link PhysicalActivityMotivationalMessage} were generated.
	 */
	public void setContentGeneratedTime(DateTime contentGeneratedTime) {
		this.contentGeneratedTime = contentGeneratedTime;
	}

	/**
	 * Sets the time when the user observed the message, in the time zone of
	 * the user.
	 * 
	 * @param userObservedTime the time when the user observed the message
	 */
	public void setUserObservedTime(DateTime userObservedTime) {
		this.userObservedTime = userObservedTime;
	}
		
	/**
	 * Sets the time at which the 'available' notification for this 
	 * {@link PhysicalActivityMotivationalMessage} should expire.
	 * @param expirationTime the time at which the 'available' notification for this 
	 * {@link PhysicalActivityMotivationalMessage} should expire.
	 */
	public void setExpirationTime(DateTime expirationTime) {
		this.expirationTime = expirationTime;
	}
	
	/**
	 * Sets whether the message was system-initiated or user-initiated.
	 * 
	 * @param systemInitiated true if the message was system-initiated, false
	 * if it was user-initiated
	 */
	public void setSystemInitiated(boolean systemInitiated) {
		this.systemInitiated = systemInitiated;
	}
		
	/**
	 * Sets whether or not this message was generated to prevent an error state in the system, see
	 * {@link MessageTiming#isErrorRecovery()} for a more detailed explanation.
	 * @param errorRecovery whether or not this message was generated to prevent an error state in the system.
	 */
	public void setErrorRecovery(boolean errorRecovery) {
		this.errorRecovery = errorRecovery;
	}
		
	/**
	 * This method should only be called if the message is user-initiated. It
	 * sets whether the user wants a greeting intention from the message.
	 * 
	 * @param requireGreeting true if the user wants a greeting intention from
	 * the message.
	 */
	public void setRequireGreeting(boolean requireGreeting) {
		this.requireGreeting = requireGreeting;
	}
	
	/**
	 * This method should only be called if the message is user-initiated. It
	 * sets whether the user wants a feedback intention from the message.
	 * 
	 * @param requireFeedback true if the user wants a feedback intention from
	 * the message
	 */
	public void setRequireFeedback(boolean requireFeedback) {
		this.requireFeedback = requireFeedback;
	}

	/**
	 * This method should only be called if the message is user-initiated. It
	 * sets whether the user wants an argument intention from the message.
	 * 
	 * @param requireArgument true if the user wants an argument intention from
	 * the message
	 */
	public void setRequireArgument(boolean requireArgument) {
		this.requireArgument = requireArgument;
	}

	/**
	 * This method should only be called if the message is user-initiated. It
	 * sets whether the user wants a suggestion or reinforcement intention from
	 * the message.
	 * 
	 * @param requireSuggestion true if the user wants a suggestion or
	 * reinforcement intention from the message
	 */
	public void setRequireSuggestion(boolean requireSuggestion) {
		this.requireSuggestion = requireSuggestion;
	}
	
	/**
	 * This method should only be called if the message is user-initiated. It
	 * sets whether the user wants a reinforcement intention from the message.
	 * 
	 * @param requireReinforcement true if the user wants a reinforcement intention 
	 * from the message.
	 */
	public void setRequireReinforcement(boolean requireReinforcement) {
		this.requireReinforcement = requireReinforcement;
	}
	
	/**
	 * This method should only be called if the message is user-initiated. It
	 * sets whether the user wants a synchronizeSensor intention from the message.
	 * 
	 * @param requireSynchronizeSensor true if the user wants a synchronizeSensor intention 
	 * from the message.
	 */
	public void setRequireSynchronizeSensor(boolean requireSynchronizeSensor) {
		this.requireSynchronizeSensor = requireSynchronizeSensor;
	}
}
