package eu.ewall.servicebrick.domotics.model;

import java.util.List;

import org.joda.time.DateTime;

import eu.ewall.servicebrick.common.model.SensorEventHistory;

/**
 * Lists temperature events for a given user and possibly a location, either in a given time interval or the latest n 
 * events.
 */
public class TemperatureHistoryDomotics extends SensorEventHistory {

	private List<TemperatureEventDomotics> temperatureEvents;

	public TemperatureHistoryDomotics(String username, String location, DateTime from, 
			DateTime to, List<TemperatureEventDomotics> temperatureEvents) {
		super(username, location, from, to);
		this.temperatureEvents = temperatureEvents;
	}

	public TemperatureHistoryDomotics(String username, String location, 
			int latestEvents, List<TemperatureEventDomotics> temperatureEvents) {
		super(username, location, latestEvents);
		this.temperatureEvents = temperatureEvents;
	}

	public List<TemperatureEventDomotics> getTemperatureEvents() {
		return temperatureEvents;
	}

	public void setTemperatureEvents(List<TemperatureEventDomotics> temperatureEvents) {
		this.temperatureEvents = temperatureEvents;
	}
	
}
