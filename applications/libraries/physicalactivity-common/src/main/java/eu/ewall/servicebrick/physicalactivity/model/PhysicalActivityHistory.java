package eu.ewall.servicebrick.physicalactivity.model;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

/**
 * Contains data about end-user physical activity in a given time frame.
 */
public class PhysicalActivityHistory implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String username;
	private DateTime from;
	private DateTime to;
	private Integer latestEvents;
	private List<PhysicalActivityEvent> physicalActivityEvents;
	
	public PhysicalActivityHistory(String username, DateTime from, DateTime to, 
			List<PhysicalActivityEvent> physicalActivityEvents) {
		this.username = username;
		this.from = from;
		this.to = to;
		this.physicalActivityEvents = physicalActivityEvents;
	}

	public PhysicalActivityHistory(String username, int latestEvents, 
			List<PhysicalActivityEvent> physicalActivityEvents) {
		this.username = username;
		this.latestEvents = latestEvents;
		this.physicalActivityEvents = physicalActivityEvents;
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

	public List<PhysicalActivityEvent> getPhysicalActivityEvents() {
		return physicalActivityEvents;
	}

	public void setPhysicalActivityEvents(List<PhysicalActivityEvent> physicalActivityEvents) {
		this.physicalActivityEvents = physicalActivityEvents;
	}

	public Integer getLatestEvents() {
		return latestEvents;
	}

	public void setLatestEvents(Integer latestEvents) {
		this.latestEvents = latestEvents;
	}
	
}
