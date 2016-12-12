package eu.ewall.servicebrick.dailyfunctioning.model;

import java.util.List;

import org.joda.time.DateTime;

import eu.ewall.servicebrick.common.model.SensorEventHistory;

/**
 * Lists NG gas events for a given user and possibly a location, either in a given time interval or the latest n 
 * events.
 */
public class GasNgHistory extends SensorEventHistory {

	private List<GasNgEvent> gasNgEvents;

	public GasNgHistory(String username, String location, DateTime from, DateTime to, 
			List<GasNgEvent> gasNgEvents) {
		super(username, location, from, to);
		this.gasNgEvents = gasNgEvents;
	}

	public GasNgHistory(String username, String location, int latestEvents, 
			List<GasNgEvent> gasNgEvents) {
		super(username, location, latestEvents);
		this.gasNgEvents = gasNgEvents;
	}

	public List<GasNgEvent> getGasNgEvents() {
		return gasNgEvents;
	}

	public void setGasNgEvents(List<GasNgEvent> gasNgEvents) {
		this.gasNgEvents = gasNgEvents;
	}
	
}
