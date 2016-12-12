package eu.ewall.servicebrick.dailyfunctioning.model;

import java.util.List;

import org.joda.time.DateTime;

import eu.ewall.servicebrick.common.model.SensorEventHistory;

/**
 * Lists temperature events for a given user and possibly a location, either in a given time interval or the latest n 
 * events.
 */
public class TemperatureHistory extends SensorEventHistory {

	private List<TemperatureEvent> temperatureEvents;

	public TemperatureHistory(String username, String location, DateTime from, DateTime to, 
			List<TemperatureEvent> temperatureEvents) {
		super(username, location, from, to);
		this.temperatureEvents = temperatureEvents;
	}

	public TemperatureHistory(String username, String location, int latestEvents, 
			List<TemperatureEvent> temperatureEvents) {
		super(username, location, latestEvents);
		this.temperatureEvents = temperatureEvents;
	}

	public List<TemperatureEvent> getTemperatureEvents() {
		return temperatureEvents;
	}

	public void setTemperatureEvents(List<TemperatureEvent> temperatureEvents) {
		this.temperatureEvents = temperatureEvents;
	}
	
}
