package eu.ewall.servicebrick.domotics.model;

import java.util.List;

import org.joda.time.DateTime;

import eu.ewall.servicebrick.common.model.SensorEventHistory;

/**
 * Lists humidity events for a given user and possibly a location, either in a given time interval or the latest n 
 * events.
 */
public class HumidityHistoryDomotics extends SensorEventHistory {

	private List<HumidityEventDomotics> humidityEvents;

	public HumidityHistoryDomotics(String username, String location, DateTime from, DateTime to, 
			List<HumidityEventDomotics> humidityEvents) {
		super(username, location, from, to);
		this.humidityEvents = humidityEvents;
	}

	public HumidityHistoryDomotics(String username, String location, int latestEvents, 
			List<HumidityEventDomotics> humidityEvents) {
		super(username, location, latestEvents);
		this.humidityEvents = humidityEvents;
	}

	public List<HumidityEventDomotics> getHumidityEvents() {
		return humidityEvents;
	}

	public void setHumidityEvents(List<HumidityEventDomotics> humidityEvents) {
		this.humidityEvents = humidityEvents;
	}
	
}
