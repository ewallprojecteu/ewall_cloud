package eu.ewall.servicebrick.domotics.processor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import eu.ewall.servicebrick.common.dao.DataManagerUpdatesDao;
import eu.ewall.servicebrick.common.model.SensorEvent;
import eu.ewall.servicebrick.common.model.SensorsUpdates;
import eu.ewall.servicebrick.domotics.model.DomoticsEvent;

import eu.ewall.servicebrick.domotics.model.HumidityEvent;
import eu.ewall.servicebrick.domotics.model.IlluminanceEvent;
import eu.ewall.servicebrick.domotics.model.TemperatureEvent;

public class DomoticsStatus {
	private static final Logger log = LoggerFactory.getLogger(DomoticsStatus.class);
	
//	@Autowired
//	private ProfilingServerDao profilingServerDao;
	@Autowired
	private DataManagerUpdatesDao dataManagerUpdatesDao;
//	@Autowired
//	private SensorEventDao sensorEventDao;
	
	@Value("${processing.startDateYYYYMMdd}")
	private String startDateYYYYMMdd;
	
    SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMdd");
    
    public List<DomoticsEvent> processDomotics(String theuser){
    	log.debug("Starting processing of sensors data");
//		long start = System.currentTimeMillis();
		
		DateTime dt = new DateTime();
		try {
			dt = new DateTime(sdf.parse(startDateYYYYMMdd));
		} catch (ParseException e1) {
			log.error("Error in configured starting date 'processing.startDateYYYYMMdd'. Setting to default (now)");
		}
		
		SensorsUpdates sensorsUpdates = null;
		try {
			sensorsUpdates = dataManagerUpdatesDao.getSensorsReadingByUsername(theuser);
			if(sensorsUpdates!=null){
				log.debug("Got sensors reading for user " + sensorsUpdates.getUsername());
			}
		} catch (Exception e) {
			log.debug("Exception in getting sensors reading for user " + theuser, e);
		} 
		if(sensorsUpdates==null){
			log.info("Starting date for indexing: " + dt);
			sensorsUpdates = new SensorsUpdates(theuser, dt);
		}
		
		// the aggregated last sensor events
		Map<String, SensorEvent> aggrLastSensorEvents = new HashMap<String, SensorEvent>();
		
		
		
		for(String location : sensorsUpdates.getLastHumidityUpdates().keySet()){
			String value = sensorsUpdates.getLastHumidityUpdates().get(location);
			String key = HumidityEvent.class.getSimpleName()+"-"+location;
			aggrLastSensorEvents.put(key, new HumidityEvent(theuser, sensorsUpdates.getLastHumidityUpdateTimestamp(), Double.parseDouble(value), location));			
		}
		
		for(String location : sensorsUpdates.getLastIlluminanceUpdates().keySet()){
			String value = sensorsUpdates.getLastIlluminanceUpdates().get(location);
			String key = IlluminanceEvent.class.getSimpleName()+"-"+location;
			aggrLastSensorEvents.put(key, new IlluminanceEvent(theuser, sensorsUpdates.getLastIlluminanceUpdateTimestamp(), Double.parseDouble(value), location));				
		}
		
		for(String location : sensorsUpdates.getLastTemperatureUpdates().keySet()){
			String value = sensorsUpdates.getLastTemperatureUpdates().get(location);
			String key = TemperatureEvent.class.getSimpleName()+"-"+location;
			aggrLastSensorEvents.put(key, new TemperatureEvent(theuser, sensorsUpdates.getLastTemperatureUpdateTimestamp(), Double.parseDouble(value), location));					
		}
		
		
		// now group the sensors of the same location/room into the same DomoticsRoom class, and then put all those classes into a list
		DomoticsEvent domoticsBedroom = new DomoticsEvent();
		DomoticsEvent domoticsBathroom = new DomoticsEvent();
		DomoticsEvent domoticsKitchen = new DomoticsEvent();
		DomoticsEvent domoticsLivingroom = new DomoticsEvent();
		
		for(String key : aggrLastSensorEvents.keySet()){
			// split the key --> [0]: sensor event name ; [1]: location
			SensorEvent sensorEvent = aggrLastSensorEvents.get(key);
			String[] sensorLocation = key.split("-");
			if(sensorLocation[1].equals("bedroom")){
				if(domoticsBedroom.getLocation() == null)
					domoticsBedroom.setLocation("bedroom");
				if(domoticsBedroom.getUsername() == null)
					domoticsBedroom.setUsername(sensorEvent.getUsername());
				domoticsBedroom = rearrangeSensorEvent(domoticsBedroom, sensorEvent);
			}
			else if(sensorLocation[1].equals("bathroom")){
				if(domoticsBathroom.getLocation() == null)
					domoticsBathroom.setLocation("bathroom");
				if(domoticsBathroom.getUsername() == null)
					domoticsBathroom.setUsername(sensorEvent.getUsername());
				domoticsBathroom = rearrangeSensorEvent(domoticsBathroom, sensorEvent);
			}
			else if(sensorLocation[1].equals("kitchen")){
				if(domoticsKitchen.getLocation() == null)
					domoticsKitchen.setLocation("kitchen");
				if(domoticsKitchen.getUsername() == null)
					domoticsKitchen.setUsername(sensorEvent.getUsername());
				domoticsKitchen = rearrangeSensorEvent(domoticsKitchen, sensorEvent);
			}
			else if(sensorLocation[1].equals("livingroom")){
				if(domoticsLivingroom.getLocation() == null)
					domoticsLivingroom.setLocation("livingroom");
				if(domoticsLivingroom.getUsername() == null)
					domoticsLivingroom.setUsername(sensorEvent.getUsername());
				domoticsLivingroom = rearrangeSensorEvent(domoticsLivingroom, sensorEvent);
			}
		}
		
		List<DomoticsEvent> domoticsRooms = new ArrayList<DomoticsEvent>();
		domoticsRooms.add(domoticsBedroom);
		domoticsRooms.add(domoticsBathroom);
		domoticsRooms.add(domoticsKitchen);
		domoticsRooms.add(domoticsLivingroom);
		
		return domoticsRooms;
    }
    
    private DomoticsEvent rearrangeSensorEvent(DomoticsEvent domotics, SensorEvent event){
    	if(event instanceof HumidityEvent){
    		double value = ((HumidityEvent)event).getHumidity();
    		domotics.setHumidity(value);
    	}
    	else if(event instanceof TemperatureEvent){
    		double value = ((TemperatureEvent)event).getTemperature();
    		domotics.setTemperature(value);
    	}
    	else if(event instanceof IlluminanceEvent){
    		double value = ((IlluminanceEvent)event).getIlluminance();
    		domotics.setIlluminance(value);
    	}
 
    	
    	return domotics;
    }
}
