package eu.ewall.servicebrick.dailyfunctioning.model;

import java.util.List;

import org.joda.time.DateTime;

import eu.ewall.servicebrick.common.model.SensorEventHistory;

/**
 * Lists humidity events for a given user and possibly a location, either in a given time interval or the latest n 
 * events.
 */
public class HumidityHistory extends SensorEventHistory {

	private List<HumidityEvent> humidityEvents;

	public HumidityHistory(String username, String location, DateTime from, DateTime to, 
			List<HumidityEvent> humidityEvents) {
		super(username, location, from, to);
		this.humidityEvents = humidityEvents;
	}

	public HumidityHistory(String username, String location, int latestEvents, 
			List<HumidityEvent> humidityEvents) {
		super(username, location, latestEvents);
		this.humidityEvents = humidityEvents;
	}

	public List<HumidityEvent> getHumidityEvents() {
		return humidityEvents;
	}

	public void setHumidityEvents(List<HumidityEvent> humidityEvents) {
		this.humidityEvents = humidityEvents;
	}
	
}
