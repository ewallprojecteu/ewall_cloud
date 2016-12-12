package eu.ewall.servicebrick.dailyfunctioning.model;

import java.util.List;

import org.joda.time.DateTime;

import eu.ewall.servicebrick.common.model.SensorEventHistory;

/**
 * Lists LPG gas events for a given user and possibly a location, either in a given time interval or the latest n 
 * events.
 */
public class GasLpgHistory extends SensorEventHistory {

	private List<GasLpgEvent> gasLpgEvents;

	public GasLpgHistory(String username, String location, DateTime from, DateTime to, 
			List<GasLpgEvent> gasLpgEvents) {
		super(username, location, from, to);
		this.gasLpgEvents = gasLpgEvents;
	}

	public GasLpgHistory(String username, String location, int latestEvents, 
			List<GasLpgEvent> gasLpgEvents) {
		super(username, location, latestEvents);
		this.gasLpgEvents = gasLpgEvents;
	}

	public List<GasLpgEvent> getGasLpgEvents() {
		return gasLpgEvents;
	}

	public void setGasLpgEvents(List<GasLpgEvent> gasLpgEvents) {
		this.gasLpgEvents = gasLpgEvents;
	}
	
}
