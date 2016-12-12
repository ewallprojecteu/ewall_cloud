package eu.ewall.servicebrick.dailyfunctioning.model;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

/**
 * Contains data about end-user functioning activity in a given time frame.
 */
public class FunctioningActivityHistory implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String username;
	private DateTime from;
	private DateTime to;
	private Integer latestEvents;
	private List<FunctioningActivityEvent> functioningActivityEvents;
	
	public FunctioningActivityHistory(String username, DateTime from, DateTime to, 
			List<FunctioningActivityEvent> functioningActivityEvents) {
		this.username = username;
		this.from = from;
		this.to = to;
		this.functioningActivityEvents = functioningActivityEvents;
	}

	public FunctioningActivityHistory(String username, int latestEvents, 
			List<FunctioningActivityEvent> functioningActivityEvents) {
		this.username = username;
		this.latestEvents = latestEvents;
		this.functioningActivityEvents = functioningActivityEvents;
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

	public List<FunctioningActivityEvent> getFunctioningActivityEvents() {
		return functioningActivityEvents;
	}

	public void setFunctioningActivityEvents(List<FunctioningActivityEvent> functioningActivityEvents) {
		this.functioningActivityEvents = functioningActivityEvents;
	}

	public Integer getLatestEvents() {
		return latestEvents;
	}

	public void setLatestEvents(Integer latestEvents) {
		this.latestEvents = latestEvents;
	}
	
}
