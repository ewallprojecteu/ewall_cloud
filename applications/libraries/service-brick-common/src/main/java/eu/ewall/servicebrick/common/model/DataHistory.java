package eu.ewall.servicebrick.common.model;

import org.joda.time.DateTime;

/**
 * Parent class for creating containers of historical data returned by service bricks.
 */
public abstract class DataHistory {

	private String username;
	private String location;
	private DateTime from;
	private DateTime to;
	private Integer latestEvents;
	
	public DataHistory(String username, String location, DateTime from, DateTime to) {
		this.username = username;
		this.location = location;
		this.from = from;
		this.to = to;
	}

	public DataHistory(String username, String location, int latestEvents) {
		this.username = username;
		this.location = location;
		this.latestEvents = latestEvents;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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
	
}
