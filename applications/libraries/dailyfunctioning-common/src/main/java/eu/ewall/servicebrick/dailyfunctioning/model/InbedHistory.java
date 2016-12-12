package eu.ewall.servicebrick.dailyfunctioning.model;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

/**
 * Contains data about end-user inactivity in a given time frame.
 */
public class InbedHistory implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String username;
	private DateTime from;
	private DateTime to;
	private Integer latestEvents;
	private List<InbedEvent> inbedEvents;
	
	public InbedHistory(String username, DateTime from, DateTime to, List<InbedEvent> inbedEvents) {
		this.username = username;
		this.from = from;
		this.to = to;
		this.inbedEvents = inbedEvents;
	}

	public InbedHistory(String username, int latestEvents, 
			List<InbedEvent> inbedEvents) {
		this.username = username;
		this.latestEvents = latestEvents;
		this.inbedEvents = inbedEvents;
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

	public List<InbedEvent> getInbedEvents() {
		return inbedEvents;
	}

	public void setInbedEvents(List<InbedEvent> inbedEvents) {
		this.inbedEvents = inbedEvents;
	}

	public Integer getLatestEvents() {
		return latestEvents;
	}

	public void setLatestEvents(Integer latestEvents) {
		this.latestEvents = latestEvents;
	}
	
}
