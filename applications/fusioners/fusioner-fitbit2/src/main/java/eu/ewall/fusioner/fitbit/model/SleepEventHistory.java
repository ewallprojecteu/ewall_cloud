package eu.ewall.fusioner.fitbit.model;

import org.joda.time.DateTime;

/**
 * Parent class for creating containers of historical sleep data.
 */
public abstract class SleepEventHistory {

	private String username;
	private String from;
	private String to;

	
	public SleepEventHistory(String username, String from, String to) {
		this.username = username;
		this.from = from;
		this.to = to;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	
}
