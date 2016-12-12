package eu.ewall.servicebrick.socializingmood.model;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

/**
 * Contains data about end-user Socializing in a given time frame.
 */
public class SocializingHistory implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String username;
	private DateTime from;
	private DateTime to;
	private Integer latestEvents;
	private List<Socializing> socializingEvents;
	
	public SocializingHistory(String username, DateTime from, DateTime to, 
			List<Socializing> socializingEvents) {
		this.username = username;
		this.from = from;
		this.to = to;
		this.socializingEvents = socializingEvents;
	}

	public SocializingHistory(String username, int latestEvents, 
			List<Socializing> socializingEvents) {
		this.username = username;
		this.latestEvents = latestEvents;
		this.socializingEvents = socializingEvents;
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

	public List<Socializing> getSocializingEvents() {
		return socializingEvents;
	}

	public void setSocializingEvents(List<Socializing> socializingEvents) {
		this.socializingEvents = socializingEvents;
	}

	public Integer getLatestEvents() {
		return latestEvents;
	}

	public void setLatestEvents(Integer latestEvents) {
		this.latestEvents = latestEvents;
	}
	
}
