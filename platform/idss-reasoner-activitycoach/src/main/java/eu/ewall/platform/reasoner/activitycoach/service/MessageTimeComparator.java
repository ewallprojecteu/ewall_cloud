package eu.ewall.platform.reasoner.activitycoach.service;

import eu.ewall.platform.idss.service.model.common.message.PhysicalActivityMotivationalMessage;

import java.util.Comparator;

/**
 * Comparator for messages by time. It uses {@link
 * MessageReader#getTime(PhysicalActivityMotivationalMessage) MessageReader.getTime()} to get
 * the time of a message. It uses {@link DateTimeComparator DateTimeComparator}
 * to compare the times.
 * 
 * @author Dennis Hofs
 */
public class MessageTimeComparator implements Comparator<PhysicalActivityMotivationalMessage> {
	private DateTimeComparator dateTimeComparator;
	
	/**
	 * Constructs a new comparator with ascending order.
	 */
	public MessageTimeComparator() {
		dateTimeComparator = new DateTimeComparator();
	}

	/**
	 * Constructs a new comparator.
	 * 
	 * @param ascending true for ascending order, false for descending order
	 */
	public MessageTimeComparator(boolean ascending) {
		dateTimeComparator = new DateTimeComparator(ascending);
	}

	@Override
	public int compare(PhysicalActivityMotivationalMessage o1, PhysicalActivityMotivationalMessage o2) {
		return dateTimeComparator.compare(MessageReader.getTime(o1),
				MessageReader.getTime(o2));
	}
}
