package eu.ewall.servicebrick.dailyfunctioning.model;

import java.util.List;

import org.joda.time.DateTime;

import eu.ewall.servicebrick.common.model.SensorEventHistory;

/**
 * Lists illuminance events for a given user and possibly a location, either in a given time interval or the latest n 
 * events.
 */
public class IlluminanceHistory extends SensorEventHistory {

	private List<IlluminanceEvent> illuminanceEvents;

	public IlluminanceHistory(String username, String location, DateTime from, DateTime to, 
			List<IlluminanceEvent> illuminanceEvents) {
		super(username, location, from, to);
		this.illuminanceEvents = illuminanceEvents;
	}

	public IlluminanceHistory(String username, String location, int latestEvents, 
			List<IlluminanceEvent> illuminanceEvents) {
		super(username, location, latestEvents);
		this.illuminanceEvents = illuminanceEvents;
	}

	public List<IlluminanceEvent> getIlluminanceEvents() {
		return illuminanceEvents;
	}

	public void setIlluminanceEvents(List<IlluminanceEvent> illuminanceEvents) {
		this.illuminanceEvents = illuminanceEvents;
	}
	
}
