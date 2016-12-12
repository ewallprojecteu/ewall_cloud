/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.ewallsystem;

import java.io.Serializable;

/**
 * The Class LongPollMessage.
 * 
 * @author emirmos
 */
public class LongPollMessage implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8799598627598576317L;

	/** The request ID. */
	private Long requestID;

	/** The requested action. */
	private LongPollMessageType actionType;

	/** The message data. */
	private String messageData;

	/**
	 * Instantiates a new long poll message.
	 */
	public LongPollMessage() {
	}

	/**
	 * Instantiates a new long poll message.
	 *
	 * @param requestID
	 *            the request id
	 * @param actionType
	 *            the action type
	 * @param data
	 *            the data
	 */
	public LongPollMessage(Long requestID, LongPollMessageType actionType,
			String data) {
		this.requestID = requestID;
		this.actionType = actionType;
		this.messageData = data;
	}

	/**
	 * Gets the request id.
	 *
	 * @return the request id
	 */
	public Long getRequestID() {
		return requestID;
	}

	/**
	 * Sets the request id.
	 *
	 * @param requestID
	 *            the new request id
	 */
	public void setRequestID(Long requestID) {
		this.requestID = requestID;
	}

	/**
	 * Gets the action type.
	 *
	 * @return the action type
	 */
	public LongPollMessageType getActionType() {
		return actionType;
	}

	/**
	 * Sets the action type.
	 *
	 * @param actionType
	 *            the new action type
	 */
	public void setActionType(LongPollMessageType actionType) {
		this.actionType = actionType;
	}

	/**
	 * Gets the message data.
	 *
	 * @return the message data
	 */
	public String getMessageData() {
		return messageData;
	}

	/**
	 * Sets the message data.
	 *
	 * @param messageData
	 *            the new message data
	 */
	public void setMessageData(String messageData) {
		this.messageData = messageData;
	}
}
