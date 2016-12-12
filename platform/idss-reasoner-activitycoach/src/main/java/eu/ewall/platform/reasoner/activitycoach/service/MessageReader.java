package eu.ewall.platform.reasoner.activitycoach.service;

import eu.ewall.platform.idss.service.model.common.message.PhysicalActivityMotivationalMessage;
import eu.ewall.platform.idss.service.model.common.message.timing.MessageTiming;

import org.joda.time.DateTime;

/**
 * This class can read derived data from {@link PhysicalActivityMotivationalMessage
 * MotivationalMessage} objects.
 * 
 * @author Dennis Hofs
 */
public class MessageReader {
	
	/**
	 * Returns the time of the specified motivational message. It tries the
	 * user observed time, and user notified time, in that order.
	 * If the message doesn't have a timing component or none of the times are
	 * defined, this method returns null.
	 * 
	 * @param msg the message
	 * @return the time or null
	 */
	public static DateTime getTime(PhysicalActivityMotivationalMessage msg) {
		MessageTiming timing = msg.getTiming();
		if (timing == null)
			return null;
		if (timing.getUserObservedTime() != null)
			return timing.getUserObservedTime();
		else 
			return timing.getUserNotifiedTime();
	}
}
