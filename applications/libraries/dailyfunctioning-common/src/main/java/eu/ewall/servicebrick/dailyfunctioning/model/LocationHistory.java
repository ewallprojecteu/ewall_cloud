package eu.ewall.servicebrick.dailyfunctioning.model;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

/**
 * Contains data about end-user physical movements in a given time frame, aggregated with the given granularity.
 */
public class LocationHistory implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String username;
	private DateTime from;
	private DateTime to;
	private Integer latestEvents;
	private List<LocationEvent> locationEvents;
	
	public LocationHistory(String username, DateTime from, DateTime to, List<LocationEvent> locationEvents) {
		this.username = username;
		this.from = from;
		this.to = to;
		this.locationEvents = locationEvents;
	}

	public LocationHistory(String username, int latestEvents, List<LocationEvent> locationEvents) {
		this.username = username;
		this.latestEvents = latestEvents;
		this.locationEvents = locationEvents;
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

	public List<LocationEvent> getLocationEvents() {
		return locationEvents;
	}

	public void setLocationEvents(List<LocationEvent> locationEvents) {
		this.locationEvents = locationEvents;
	}

	public Integer getLatestEvents() {
		return latestEvents;
	}

	public void setLatestEvents(Integer latestEvents) {
		this.latestEvents = latestEvents;
	}
	
}
