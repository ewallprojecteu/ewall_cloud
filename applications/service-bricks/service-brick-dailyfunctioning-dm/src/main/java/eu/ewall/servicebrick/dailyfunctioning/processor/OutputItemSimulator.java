package eu.ewall.servicebrick.dailyfunctioning.processor;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import eu.ewall.servicebrick.common.model.SensorEvent;
@Component
public class OutputItemSimulator {
	
	private String[] locations = new String[]{"kitchen" , "livingroom", "bathroom", "bedroom"};
	private String[] activities = new String[]{"sanitary_visits" , "cooking", "entertaining", "showering", "sleeping"};
	private boolean inBed = false;
	
	AlgoOutputItem getSimulatedData(AlgoInputItem sensorsSnapshot){
		String location = locations[((int)(Math.random()*100)%locations.length)];
		String activity = "entertaining";
		DateTime timestamp = findMostRecent(sensorsSnapshot);
		if(location.equals("kitchen")){
			activity = "cooking";
		} else if(location.equals("bathroom")){
			activity = "sanitary_visits";
		} else if(location.equals("bedroom")){
			activity = "sleeping";
			inBed = true;
		}
		return new AlgoOutputItem(timestamp, location, activity, inBed);
	}

	private DateTime findMostRecent(AlgoInputItem sensorsSnapshot) {
		DateTime timestamp = new DateTime(0);
		for(SensorEvent event : sensorsSnapshot.getEvents().values()) {
			if(event.getTimestamp().getMillis()>timestamp.getMillis()) {
				timestamp = event.getTimestamp();
			}
		}
		return timestamp;
	}
	

}
