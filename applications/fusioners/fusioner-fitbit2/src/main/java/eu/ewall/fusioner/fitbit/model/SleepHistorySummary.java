package eu.ewall.fusioner.fitbit.model;

import java.util.List;

import org.joda.time.DateTime;

import eu.ewall.fusioner.fitbit.model.SleepEventHistory;
import eu.ewall.fusioner.fitbit.model.SleepSummary;
import eu.ewall.fusioner.fitbit.model.FitBitEvent;

/**
 * Lists sleep events for a given user.
 */
public class SleepHistorySummary extends SleepEventHistory {

	private List<FitBitEvent> sleepEvents;

	public SleepHistorySummary(String username,String from, 
			String to, List<FitBitEvent> sleepEvents) {
		super(username, from, to);
		this.sleepEvents = sleepEvents;
	}

	
	public List<FitBitEvent> getSleepEvents() {
		return sleepEvents;
	}

	public void setSleepEvents(List<FitBitEvent> sleepEvents) {
		this.sleepEvents = sleepEvents;
	}
	
}
