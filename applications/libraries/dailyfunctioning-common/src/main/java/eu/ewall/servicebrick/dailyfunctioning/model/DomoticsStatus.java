package eu.ewall.servicebrick.dailyfunctioning.model;

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
import eu.ewall.servicebrick.dailyfunctioning.model.DomoticsEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.DoorEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.GasCoEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.GasEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.GasLpgEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.GasNgEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.HumidityEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.IlluminanceEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.MattressSensorEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.PirMovementEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.TemperatureEvent;

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
		
		for(String location : sensorsUpdates.getLastMattressUpdates().keySet()){
			String value = sensorsUpdates.getLastMattressUpdates().get(location);
			String key = MattressSensorEvent.class.getSimpleName()+"-"+location;
			aggrLastSensorEvents.put(key, new MattressSensorEvent(theuser, sensorsUpdates.getLastMattressUpdateTimestamp(), Boolean.parseBoolean(value), 0, location));					
		}
		
		for(String location : sensorsUpdates.getLastPirMovementUpdates().keySet()){
			String value = sensorsUpdates.getLastPirMovementUpdates().get(location);
			String key = PirMovementEvent.class.getSimpleName()+"-"+location;
			aggrLastSensorEvents.put(key, new PirMovementEvent(theuser, sensorsUpdates.getLastPirMovementUpdateTimestamp(), Boolean.parseBoolean(value), location));					
		}
		
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
		
		for(String location : sensorsUpdates.getLastDoorUpdates().keySet()){
			String value = sensorsUpdates.getLastDoorUpdates().get(location);
			String key = DoorEvent.class.getSimpleName()+"-"+location;
			aggrLastSensorEvents.put(key, new DoorEvent(theuser, sensorsUpdates.getLastDoorUpdateTimestamp(), Boolean.parseBoolean(value), location));			
		}
		
		for(String location : sensorsUpdates.getLastGasLpgUpdates().keySet()){
			String value = sensorsUpdates.getLastGasLpgUpdates().get(location);
			String key = GasEvent.class.getSimpleName()+"-"+location;
			aggrLastSensorEvents.put(key, new GasLpgEvent(theuser, sensorsUpdates.getLastGasLpgUpdateTimestamp(), Double.parseDouble(value), location));					
		}
		
		for(String location : sensorsUpdates.getLastGasNgUpdates().keySet()){
			String value = sensorsUpdates.getLastGasNgUpdates().get(location);
			String key = GasEvent.class.getSimpleName()+"-"+location;
			aggrLastSensorEvents.put(key, new GasNgEvent(theuser, sensorsUpdates.getLastGasNgUpdateTimestamp(), Double.parseDouble(value), location));				
		}
		
		for(String location : sensorsUpdates.getLastGasCoUpdates().keySet()){
			String value = sensorsUpdates.getLastGasCoUpdates().get(location);
			String key = GasEvent.class.getSimpleName()+"-"+location;
			aggrLastSensorEvents.put(key, new GasCoEvent(theuser, sensorsUpdates.getLastGasCoUpdateTimestamp(), Double.parseDouble(value), location));					
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
    	else if(event instanceof DoorEvent){
    		boolean value = ((DoorEvent)event).getDoorStatus();
    		domotics.setDoor(value);
    	}
    	else if(event instanceof MattressSensorEvent){
    		boolean value = ((MattressSensorEvent)event).getPressure();
    		domotics.setMattress(value);
    	}
    	else if(event instanceof PirMovementEvent){
    		boolean value = ((PirMovementEvent)event).getMovementDetected();
    		domotics.setPirMovement(value);
    	}
    	else if(event instanceof GasCoEvent){
    		double value = ((GasCoEvent)event).getGasValue();
    		domotics.setGasCO(value);
    	}
    	
    	return domotics;
    }
}
