package eu.ewall.servicebrick.dailyfunctioning.processor;

import java.util.HashMap;

import eu.ewall.servicebrick.common.model.SensorEvent;

/**
 * Class which contains input for algorithm that detects daily functioning activity, 
 * location and in bed status based on a snapshot of the sensing environment at a 
 * certain instant in time
 * 
 * @author baronepa
 *
 */
class AlgoInputItem implements Cloneable {
	/**
	 * Events collected 
	 */
	private HashMap<String, SensorEvent> events = new HashMap<String, SensorEvent>();
	
	public void putEvent(String location, SensorEvent e) {		
		events.put(e.getClass().getSimpleName()+"-"+location, e);
	}
	
	/**
	 * Gets all events in a HashMap, where the key is the location name (room name)
	 * @return
	 */
	public HashMap<String, SensorEvent> getEvents() {
		return events;
	}

	public void setEvents(HashMap<String, SensorEvent> events) {
		this.events = events;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
	    return super.clone();
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(SensorEvent e : events.values()){
			sb.append("\n\t"+e.getClass().getSimpleName() + " : " + e.toString());
		}
		return sb.toString();
	}
}

