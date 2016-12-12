package eu.ewall.platform.idss.service.model.common.message;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.service.model.common.message.content.MessageContent;
import eu.ewall.platform.idss.service.model.common.message.intention.MessageIntention;
import eu.ewall.platform.idss.service.model.common.message.representation.MessageRepresentation;
import eu.ewall.platform.idss.service.model.common.message.timing.MessageTiming;

/**
 * This class models a motivational message.
 * 
 * @author Dennis Hofs (RRD)
 * @author Harm op den Akker (RRD)
 */
public class PhysicalActivityMotivationalMessage extends AbstractDatabaseObject {
	
	@DatabaseField(value= DatabaseType.STRING)
	private String user;
	
	@DatabaseField(value=DatabaseType.OBJECT)
	private MessageTiming timing = null;

	@DatabaseField(value=DatabaseType.OBJECT)
	private MessageIntention intention = null;

	@DatabaseField(value=DatabaseType.OBJECT)
	private MessageContent content = null;

	@DatabaseField(value=DatabaseType.OBJECT)
	private MessageRepresentation representation = null;
	

	// ----------------------------- //
	// ---------- GETTERS ---------- //
	// ----------------------------- //
	
	/**
	 * Returns the user name to which the message was sent.
	 * 
	 * @return the user name
	 */
	public String getUser() {
		return user;
	}
	
	/**
	 * Returns the timing component of the message. This may be null if the
	 * message is still being built.
	 * 
	 * @return the timing component or null
	 */
	public MessageTiming getTiming() {
		return timing;
	}
	
	/**
	 * Returns the intention component of the message. This may be null if the
	 * message is still being built.
	 * 
	 * @return the intention component or null
	 */
	public MessageIntention getIntention() {
		return intention;
	}
	
	/**
	 * Returns the content component of the message. This may be null if the
	 * message is still being built.
	 * 
	 * @return the content component or null
	 */
	public MessageContent getContent() {
		return content;
	}
	
	/**
	 * Returns the representation component of the message. This may be null if
	 * the message is still being built.
	 * 
	 * @return the representation component or null
	 */
	public MessageRepresentation getRepresentation() {
		return representation;
	}
	
	// ----------------------------- //
	// ---------- SETTERS ---------- //
	// ----------------------------- //
	
	/**
	 * Sets the user name to which the message was sent.
	 * 
	 * @param user the user name
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Sets the timing component of the message.
	 * 
	 * @param timing the timing component
	 */
	public void setTiming(MessageTiming timing) {
		this.timing = timing;
	}

	/**
	 * Sets the intention component of the message.
	 * 
	 * @param intention the intention component
	 */
	public void setIntention(MessageIntention intention) {
		this.intention = intention;
	}
	
	/**
	 * Sets the content component of the message.
	 * 
	 * @param content the content component
	 */
	public void setContent(MessageContent content) {
		this.content = content;
	}

	/**
	 * Sets the representation component of the message.
	 * 
	 * @param representation the representation component
	 */
	public void setRepresentation(MessageRepresentation representation) {
		this.representation = representation;
	}
}
