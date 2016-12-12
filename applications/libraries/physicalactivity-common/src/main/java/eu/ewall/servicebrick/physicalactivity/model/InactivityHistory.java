package eu.ewall.servicebrick.physicalactivity.model;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

/**
 * Contains data about end-user inactivity in a given time frame.
 */
public class InactivityHistory implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String username;
	private DateTime from;
	private DateTime to;
	private Integer latestEvents;
	private List<InactivityEvent> inactivityEvents;
	
	public InactivityHistory(String username, DateTime from, DateTime to, List<InactivityEvent> inactivityEvents) {
		this.username = username;
		this.from = from;
		this.to = to;
		this.inactivityEvents = inactivityEvents;
	}

	public InactivityHistory(String username, int latestEvents, 
			List<InactivityEvent> inactivityEvents) {
		this.username = username;
		this.latestEvents = latestEvents;
		this.inactivityEvents = inactivityEvents;
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

	public Integer getLatestEvents() {
		return latestEvents;
	}

	public void setLatestEvents(Integer latestEvents) {
		this.latestEvents = latestEvents;
	}
	
	public List<InactivityEvent> getInactivityEvents() {
		return inactivityEvents;
	}

	public void setInactivityEvents(List<InactivityEvent> inactivityEvents) {
		this.inactivityEvents = inactivityEvents;
	}
	
}
