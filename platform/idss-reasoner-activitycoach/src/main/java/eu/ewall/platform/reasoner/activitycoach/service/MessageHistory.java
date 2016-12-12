package eu.ewall.platform.reasoner.activitycoach.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Minutes;

import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.message.PhysicalActivityMotivationalMessage;
import eu.ewall.platform.idss.service.model.common.message.content.FeedbackContent;
import eu.ewall.platform.idss.service.model.common.message.content.MessageContent;
import eu.ewall.platform.idss.service.model.common.message.timing.MessageTiming;

/**
 * This class contains the history of motivational messages for a user, as well
 * as a number of convenience functions for accessing this history.
 * 
 * @author Dennis Hofs (RRD)
 * @author Harm op den Akker (RRD)
 */
public class MessageHistory {
	private IDSSUserProfile user;
	private List<PhysicalActivityMotivationalMessage> history;
	
	// ---------- CONSTRUCTORS ---------- //
	
	/**
	 * Constructs a new interaction model. The messages should be ordered by
	 * time using the {@link MessageTimeComparator MessageTimeComparator}.
	 * 
	 * @param user the user
	 * @param history the history of motivational messages
	 */
	public MessageHistory(IDSSUserProfile user,
			List<PhysicalActivityMotivationalMessage> history) {
		this.user = user;
		this.history = history;
	}
	
	// ---------- GETTERS ---------- //
	
	/**
	 * Returns the user.
	 * 
	 * @return the user
	 */
	public IDSSUserProfile getUser() {
		return user;
	}
	
	/**
	 * Returns the history of motivational messages. The messages should be
	 * ordered by time using the {@link MessageTimeComparator
	 * MessageTimeComparator}.
	 * 
	 * @return the history of motivational messages
	 */
	public List<PhysicalActivityMotivationalMessage> getHistory() {
		return history;
	}
	
	public PhysicalActivityMotivationalMessage getMessageById(String messageId) {
		for(PhysicalActivityMotivationalMessage pamm : history) {
			if(pamm.getId().equals(messageId)) {
				return pamm;
			}
		}
		return null;
	}
	
	public void removeMessageById(String messageId) {
		for(PhysicalActivityMotivationalMessage pamm : history) {
			if(pamm.getId().equals(messageId)) {
				history.remove(pamm);
				return;
			}
		}
	}
	
	public void addMessage(PhysicalActivityMotivationalMessage pamm) {
		history.add(pamm);
	}
	
	/**
	 * Returns a list of all messages that were observed at some point in time by the user.
	 * @return a list of all messages that were observed by the user.
	 */
	public List<PhysicalActivityMotivationalMessage> getObservedHistory() {
		List<PhysicalActivityMotivationalMessage> results = new ArrayList<PhysicalActivityMotivationalMessage>();
		for(PhysicalActivityMotivationalMessage motivationalMessage : history) {
			if(isObserved(motivationalMessage)) results.add(motivationalMessage);
		}		
		return results;
	}
	
	/**
	 * Returns the history of motivational messages that were generated today as a list. 
	 * If no motivational messages have been stored for today, this will return an empty list.
	 * @return the history of generated motivational messages from today.
	 */
	public List<PhysicalActivityMotivationalMessage> getHistoryToday() {
		DateTime now = new DateTime();
		LocalDate today = now.toLocalDate();
		LocalDate tomorrow = today.plusDays(1);

		DateTime startOfToday = today.toDateTimeAtStartOfDay(now.getZone());
		DateTime startOfTomorrow = tomorrow.toDateTimeAtStartOfDay(now.getZone());
		
		List<PhysicalActivityMotivationalMessage> results = new ArrayList<PhysicalActivityMotivationalMessage>();
		
		for(PhysicalActivityMotivationalMessage motivationalMessage : history) {
			MessageTiming messageTiming = motivationalMessage.getTiming();
			DateTime creationTime = messageTiming.getUserNotifiedTime();
			
			if(creationTime.isAfter(startOfToday) && creationTime.isBefore(startOfTomorrow)) {
				//logger.debug("Creation time: "+creationTime.toString()+" occured today ("+today.toString()+").");
				results.add(motivationalMessage);
			}
		}		
		return results;
	}
	
	/**
	 * Returns a list of {@link PhysicalActivityMotivationalMessage}s that were observed by the user <b>today</b>.
	 * @return a list of {@link PhysicalActivityMotivationalMessage}s that were observed by the user <b>today</b>.
	 */
	public List<PhysicalActivityMotivationalMessage> getObservedHistoryToday() {
		List<PhysicalActivityMotivationalMessage> observedHistory = getObservedHistory();
		
		DateTime now = new DateTime();
		LocalDate today = now.toLocalDate();
		LocalDate tomorrow = today.plusDays(1);

		DateTime startOfToday = today.toDateTimeAtStartOfDay(now.getZone());
		DateTime startOfTomorrow = tomorrow.toDateTimeAtStartOfDay(now.getZone());
		
		List<PhysicalActivityMotivationalMessage> results = new ArrayList<PhysicalActivityMotivationalMessage>();
		
		for(PhysicalActivityMotivationalMessage motivationalMessage : observedHistory) {
			
			MessageTiming messageTiming = motivationalMessage.getTiming();
			DateTime observedTime = messageTiming.getUserObservedTime();
			
			if(observedTime.isAfter(startOfToday) && observedTime.isBefore(startOfTomorrow)) {
				results.add(motivationalMessage);
			}
			
		}		
		return results;
	}
	
	/**
	 * Returns the last {@link PhysicalActivityMotivationalMessage} observed <b>today</b> in which
	 * a {@link FeedbackContent} was present that included goal setting.
	 * @return the last {@link PhysicalActivityMotivationalMessage} observed <b>today</b> in which
	 * a {@link FeedbackContent} was present that included goal setting.
	 */
	public PhysicalActivityMotivationalMessage lastObservedMessageWithGoalSetting() {
		List<PhysicalActivityMotivationalMessage> observedHistoryToday = getObservedHistoryToday();
		
		//logger.info("Number of observed messages today: "+observedHistoryToday.size()+".");
		
		if(observedHistoryToday.isEmpty()) {
			return null;
		} else {
			for(int i=observedHistoryToday.size()-1; i>=0; i--) {
				PhysicalActivityMotivationalMessage motivationalMessage = observedHistoryToday.get(i);
				//logger.info("Motivational Message: "+motivationalMessage.toString());
				MessageContent messageContent = motivationalMessage.getContent();
				if(messageContent.getFeedbackContent() != null) {
					if(messageContent.getFeedbackContent().getHasGoalSetting()) {
						return motivationalMessage;
					}
				}				
			}
		}
		return null;
	}
	
	/**
	 * Returns {@code true} if the given {@link PhysicalActivityMotivationalMessage} was observed,
	 * {@code false} otherwise.
	 * @param message  the {@link PhysicalActivityMotivationalMessage} for which to check whether it was observed.
	 * @return  {@code true} if the given {@link PhysicalActivityMotivationalMessage} was observed,
	 * {@code false} otherwise.
	 */
	public boolean isObserved(PhysicalActivityMotivationalMessage message) {
		if(message.getTiming().getUserObservedTime() == null) return false;
		else return true;
	}
	
	/**
	 * Returns how many minutes ago the given message was observed or null if the
	 * message was never observed.
	 * @param message the {@link PhysicalActivityMotivationalMessage} for which to check the time in minutes
	 * between the observed time of the message and <i>now</i>.
	 * @return how many minutes ago the given message was observed or null if the
	 * message was never observed.
	 */
	public Integer minutesAgoObserved(PhysicalActivityMotivationalMessage message) {
		if(!isObserved(message)) { 
			return null;
		} else {
			DateTime observedTime = message.getTiming().getUserObservedTime();
			DateTime now = new DateTime();
			int result = Minutes.minutesBetween(observedTime, now).getMinutes();
			//logger.info("There are '"+result+"' minutes between '"+observedTime.toString()+"' and '"+now.toString()+" (now).");
			return result;
		}
	}
}
