package eu.ewall.servicebrick.dailyfunctioning.model;

import java.util.List;

import org.joda.time.DateTime;

import eu.ewall.servicebrick.common.model.SensorEventHistory;

/**
 * Lists CO gas events for a given user and possibly a location, either in a given time interval or the latest n 
 * events.
 */
public class GasCoHistory extends SensorEventHistory {

	private List<GasCoEvent> gasCoEvents;

	public GasCoHistory(String username, String location, DateTime from, DateTime to, 
			List<GasCoEvent> gasCoEvents) {
		super(username, location, from, to);
		this.gasCoEvents = gasCoEvents;
	}

	public GasCoHistory(String username, String location, int latestEvents, 
			List<GasCoEvent> gasCoEvents) {
		super(username, location, latestEvents);
		this.gasCoEvents = gasCoEvents;
	}

	public List<GasCoEvent> getGasCoEvents() {
		return gasCoEvents;
	}

	public void setGasCoEvents(List<GasCoEvent> gasCoEvents) {
		this.gasCoEvents = gasCoEvents;
	}
	
}
