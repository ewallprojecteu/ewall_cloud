package eu.ewall.servicebrick.dailyfunctioning.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;
import org.joda.time.DateTime;

/**
 * Contains data about end-user physical movements in a given time frame,
 * aggregated with the given granularity.
 */
@Document(collection = "sleepHistoryEvents")
public class SleepHistory implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;
	private long timestamp;
	private DateTime time_stamp;
	private int state; // onbed, offbed, sleeping

/*	public SleepHistory(String username, long timestamp, int state) {
		this.username = username;
		this.timestamp = timestamp;
		this.state = state;
	}*/

	public SleepHistory(String username, DateTime time_stamp, int state) {
		this.username = username;
		this.time_stamp = time_stamp;
		this.state = state;
	}
	
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	
	public DateTime getTime_stamp() {
		return time_stamp;
	}

	public void setTime_stamp(DateTime time_stamp) {
		this.time_stamp = time_stamp;
	}
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
