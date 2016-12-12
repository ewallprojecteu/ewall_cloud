package eu.ewall.servicebrick.domotics.model;

import java.util.List;

import org.joda.time.DateTime;

import eu.ewall.servicebrick.common.model.SensorEventHistory;

/**
 * Lists illuminance events for a given user and possibly a location, either in a given time interval or the latest n 
 * events.
 */
public class IlluminanceHistoryDomotics extends SensorEventHistory {

	private List<IlluminanceEventDomotics> illuminanceEvents;
	

	public IlluminanceHistoryDomotics(String username, String location, DateTime from, DateTime to, 
			List<IlluminanceEventDomotics> illuminanceEvents) {
		super(username, location, from, to);
		this.illuminanceEvents = illuminanceEvents;
	}

	public IlluminanceHistoryDomotics(String username, String location, int latestEvents, 
			List<IlluminanceEventDomotics> illuminanceEvents) {
		super(username, location, latestEvents);
		this.illuminanceEvents = illuminanceEvents;
	}

	public List<IlluminanceEventDomotics> getIlluminanceEvents() {
		return illuminanceEvents;
	}

	public void setIlluminanceEvents(List<IlluminanceEventDomotics> illuminanceEvents) {
		this.illuminanceEvents = illuminanceEvents;
	}
	
}
