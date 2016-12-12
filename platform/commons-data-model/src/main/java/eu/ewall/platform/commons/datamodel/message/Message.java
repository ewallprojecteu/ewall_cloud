/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.message;

import java.util.Date;

import eu.ewall.platform.commons.datamodel.core.ieeesumo.ContentBearingObject;
import eu.ewall.platform.commons.datamodel.profile.User;

/**
 * The Class Message.
 *
 * @author eandgrg
 */
public class Message extends ContentBearingObject {

	/** The message deviceId. */
	protected int messageId;

	/** The sender. */
	protected User sender;

	/** The receiver. */
	protected User receiver;

	/** The content. */
	protected String content;

	/** The send date time. */
	protected Date sendDateTime;

	/** The received date time. */
	protected Date receivedDateTime;

	/** The date time to deliver. */
	protected Date dateTimeToDeliver;

	/** The read date time. */
	protected Date readDateTime;

	/** The is read. */
	protected boolean isRead;

	/** The message type. */
	protected MessageType messageType;

	/**
	 * The Constructor.
	 */
	public Message() {

	}

	/**
	 * The Constructor.
	 *
	 * @param messageId
	 *            the message deviceId
	 * @param sender
	 *            the sender
	 * @param receiver
	 *            the receiver
	 * @param content
	 *            the content
	 * @param sendDateTime
	 *            the send date time
	 * @param messageType
	 *            the message type
	 */
	public Message(int messageId, User sender, User receiver, String content,
			Date sendDateTime, MessageType messageType) {
		this.messageId = messageId;
		this.sender = sender;
		this.receiver = receiver;
		this.content = content;
		this.sendDateTime = sendDateTime;
		this.messageType = messageType;
	}

	/**
	 * The Constructor.
	 *
	 * @param messageId
	 *            the message deviceId
	 * @param sender
	 *            the sender
	 * @param receiver
	 *            the receiver
	 * @param content
	 *            the content
	 * @param sendDateTime
	 *            the send date time
	 * @param dateTimeToDeliver
	 *            the date time to deliver
	 * @param messageType
	 *            the message type
	 */
	public Message(int messageId, User sender, User receiver, String content,
			Date sendDateTime, Date dateTimeToDeliver, MessageType messageType) {
		this.messageId = messageId;
		this.sender = sender;
		this.receiver = receiver;
		this.content = content;
		this.sendDateTime = sendDateTime;
		this.dateTimeToDeliver = dateTimeToDeliver;
		this.messageType = messageType;
	}

	/**
	 * Gets the message deviceId.
	 *
	 * @return the messageId
	 */
	public int getMessageId() {
		return messageId;
	}

	/**
	 * Sets the message deviceId.
	 *
	 * @param messageId
	 *            the messageId to set
	 */
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	/**
	 * Gets the sender.
	 *
	 * @return the sender
	 */
	public User getSender() {
		return sender;
	}

	/**
	 * Sets the sender.
	 *
	 * @param sender
	 *            the sender to set
	 */
	public void setSender(User sender) {
		this.sender = sender;
	}

	/**
	 * Gets the receiver.
	 *
	 * @return the receiver
	 */
	public User getReceiver() {
		return receiver;
	}

	/**
	 * Sets the receiver.
	 *
	 * @param receiver
	 *            the receiver to set
	 */
	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	/**
	 * Gets the content.
	 *
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Sets the content.
	 *
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Gets the send date time.
	 *
	 * @return the sendDateTime
	 */
	public Date getSendDateTime() {
		return sendDateTime;
	}

	/**
	 * Sets the send date time.
	 *
	 * @param sendDateTime
	 *            the sendDateTime to set
	 */
	public void setSendDateTime(Date sendDateTime) {
		this.sendDateTime = sendDateTime;
	}

	/**
	 * Gets the received date time.
	 *
	 * @return the receivedDateTime
	 */
	public Date getReceivedDateTime() {
		return receivedDateTime;
	}

	/**
	 * Sets the received date time.
	 *
	 * @param receivedDateTime
	 *            the receivedDateTime to set
	 */
	public void setReceivedDateTime(Date receivedDateTime) {
		this.receivedDateTime = receivedDateTime;
	}

	/**
	 * Gets the date time to deliver.
	 *
	 * @return the dateTimeToDeliver
	 */
	public Date getDateTimeToDeliver() {
		return dateTimeToDeliver;
	}

	/**
	 * Sets the date time to deliver.
	 *
	 * @param dateTimeToDeliver
	 *            the dateTimeToDeliver to set
	 */
	public void setDateTimeToDeliver(Date dateTimeToDeliver) {
		this.dateTimeToDeliver = dateTimeToDeliver;
	}

	/**
	 * Gets the read date time.
	 *
	 * @return the readDateTime
	 */
	public Date getReadDateTime() {
		return readDateTime;
	}

	/**
	 * Sets the read date time.
	 *
	 * @param readDateTime
	 *            the readDateTime to set
	 */
	public void setReadDateTime(Date readDateTime) {
		this.readDateTime = readDateTime;
	}

	/**
	 * Checks if is read.
	 *
	 * @return the isRead
	 */
	public boolean isRead() {
		return isRead;
	}

	/**
	 * Sets the read.
	 *
	 * @param isRead
	 *            the isRead to set
	 */
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	/**
	 * Gets the message type.
	 *
	 * @return the messageType
	 */
	public MessageType getMessageType() {
		return messageType;
	}

	/**
	 * Sets the message type.
	 *
	 * @param messageType
	 *            the messageType to set
	 */
	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("messageId: ");
		buffer.append(messageId);
		buffer.append("\n");
		buffer.append("sender username: ");
		if (sender != null)
			buffer.append(sender.getUsername());
		else
			buffer.append("empty");
		buffer.append("\n");
		buffer.append("receiver username: ");
		if (receiver != null)
			buffer.append(receiver.getUsername());
		else
			buffer.append("empty");
		buffer.append("\n");
		buffer.append("sendDateTime: ");
		if (sendDateTime != null)
			buffer.append(sendDateTime.toString());
		else
			buffer.append("empty");
		buffer.append("\n");
		buffer.append("dateTimeToDeliver: ");
		if (dateTimeToDeliver != null)
			buffer.append(dateTimeToDeliver.toString());
		else
			buffer.append("empty");
		buffer.append("\n");
		buffer.append("readDateTime: ");
		if (readDateTime != null)
			buffer.append(readDateTime.toString());
		else
			buffer.append("empty");
		buffer.append("\n");
		buffer.append("isRead: ");
		buffer.append(isRead);
		buffer.append("\n");
		buffer.append("messageType: ");
		buffer.append(messageType);
		buffer.append("\n");
		buffer.append("content: ");
		buffer.append(content);
		buffer.append("\n");

		return buffer.toString();
	}

}
