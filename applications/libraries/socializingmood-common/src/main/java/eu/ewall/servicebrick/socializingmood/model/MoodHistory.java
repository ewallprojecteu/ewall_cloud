package eu.ewall.servicebrick.socializingmood.model;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

/**
 * Contains data about end-user Mood in a given time frame.
 */
public class MoodHistory implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String username;
	private DateTime from;
	private DateTime to;
	private Integer latestEvents;
	private List<Mood> moodActivityEvents;
	
	public MoodHistory(String username, DateTime from, DateTime to, 
			List<Mood> moodActivityEvents) {
		this.username = username;
		this.from = from;
		this.to = to;
		this.moodActivityEvents = moodActivityEvents;
	}

	public MoodHistory(String username, int latestEvents, 
			List<Mood> moodActivityEvents) {
		this.username = username;
		this.latestEvents = latestEvents;
		this.moodActivityEvents = moodActivityEvents;
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

	public List<Mood> getMoodActivityEvents() {
		return moodActivityEvents;
	}

	public void setMoodActivityEvents(List<Mood> moodActivityEvents) {
		this.moodActivityEvents = moodActivityEvents;
	}

	public Integer getLatestEvents() {
		return latestEvents;
	}

	public void setLatestEvents(Integer latestEvents) {
		this.latestEvents = latestEvents;
	}
	
}
