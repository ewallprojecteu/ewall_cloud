package eu.ewall.servicebrick.dailyfunctioning.model;

import java.util.List;

import org.joda.time.DateTime;

import eu.ewall.servicebrick.common.model.SensorEventHistory;

/**
 * Lists door events for a given user and possibly a location, either in a given time interval or the latest n 
 * events.
 */
public class DoorHistory extends SensorEventHistory {

	private List<DoorEvent> doorEvents;

	public DoorHistory(String username, String location, DateTime from, DateTime to, List<DoorEvent> doorEvents) {
		super(username, location, from, to);
		this.doorEvents = doorEvents;
	}

	public DoorHistory(String username, String location, int latestEvents, List<DoorEvent> doorEvents) {
		super(username, location, latestEvents);
		this.doorEvents = doorEvents;
	}

	public List<DoorEvent> getDoorEvents() {
		return doorEvents;
	}

	public void setDoorEvents(List<DoorEvent> doorEvents) {
		this.doorEvents = doorEvents;
	}
	
}
