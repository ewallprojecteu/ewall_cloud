package eu.ewall.servicebrick.socializingmood.model;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

/**
 * Contains data about end-user Socializing in a given time frame.
 */
public class MoodTwitterHistory implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String username;
	private DateTime from;
	private DateTime to;
	private List<MoodTwitter> moodTwitterEvents;
	
	public MoodTwitterHistory(String username, DateTime from, DateTime to, 
			List<MoodTwitter> moodTwitterEvents) {
		this.username = username;
		this.from = from;
		this.to = to;
		this.moodTwitterEvents = moodTwitterEvents;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public DateTime getFrom() {
		return from;
	}

	public void setFrom(DateTime from) {
		this.from = from;
	}

	public DateTime getTo() {
		return to;
	}

	public void setTo(DateTime to) {
		this.to = to;
	}

	public List<MoodTwitter> getSocializingEvents() {
		return moodTwitterEvents;
	}

	public void setSocializingEvents(List<MoodTwitter> moodTwitterEvents) {
		this.moodTwitterEvents = moodTwitterEvents;
	}
	
}
