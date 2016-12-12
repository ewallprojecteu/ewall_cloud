package eu.ewall.servicebrick.dailyfunctioning.processor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.ewall.platform.commons.datamodel.measure.CarbonMonoxideMeasurement;
import eu.ewall.platform.commons.datamodel.measure.DoorStatus;
import eu.ewall.platform.commons.datamodel.measure.HumidityMeasurement;
import eu.ewall.platform.commons.datamodel.measure.IlluminanceMeasurement;
import eu.ewall.platform.commons.datamodel.measure.IndoorMeasurement;
import eu.ewall.platform.commons.datamodel.measure.LiquefiedPetroleumGasMeasurement;
import eu.ewall.platform.commons.datamodel.measure.MattressPressureSensing;
import eu.ewall.platform.commons.datamodel.measure.MovementMeasurement;
import eu.ewall.platform.commons.datamodel.measure.NaturalGasMeasurement;
import eu.ewall.platform.commons.datamodel.measure.TemperatureMeasurement;
import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.sensing.VisualSensing;
import eu.ewall.servicebrick.common.dao.SensorEventDao;
import eu.ewall.servicebrick.common.model.SensorEvent;
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
import eu.ewall.servicebrick.dailyfunctioning.model.VisualSensingEvent;

@Component
public class EventSplitter {
	private static final Logger log = LoggerFactory.getLogger(EventSplitter.class);

	@Autowired
	private SensorEventDao sensorEventDao;

	public ArrayList<SensorEvent> splitHumidityMeasurements(HumidityMeasurement[] measurements, User primaryUser, HashMap<String,String> lastUpdates) {
		 ArrayList<SensorEvent> sensorEvents = new ArrayList<SensorEvent>();
		 if(measurements.length==0){
			 return sensorEvents;
		 }
		
		//First item, to take the reference 
		HumidityMeasurement humidityMeasurement = measurements[0];
		String location = humidityMeasurement.getIndoorPlaceName();
		if(location == null || location.isEmpty()){
			location = SensorEvent.UNDEFINED;
		}
		HumidityEvent lastSensorEvent = new HumidityEvent(primaryUser.getUsername(), new DateTime(humidityMeasurement.getTimestamp()), Double.parseDouble(humidityMeasurement.getMeasuredValue()), humidityMeasurement.getIndoorPlaceName());
		
		//We consider it as a change only if humidity grows up by at least 0.5 % // changed to 5% 
		if(!lastUpdates.containsKey(location) || Math.abs(lastSensorEvent.getHumidity()-Double.parseDouble(lastUpdates.get(location))) > 5 /*0.5*/){
			sensorEvents.add(lastSensorEvent);
			log.debug("adding element for location " + location);
			lastUpdates.put(location, String.valueOf(lastSensorEvent.getHumidity()));
		}
		
		
		for(HumidityMeasurement measurement : measurements){
			location = measurement.getIndoorPlaceName();
			if(location == null || location.isEmpty()){
				location = SensorEvent.UNDEFINED;
			}			
			//We consider it as a change only if humidity grows up by at least 0.5 %
			if(!lastUpdates.containsKey(location) ||  Math.abs(Double.parseDouble(measurement.getMeasuredValue())-Double.parseDouble(lastUpdates.get(location))) > 0.5){				
				lastSensorEvent = new HumidityEvent(primaryUser.getUsername(), new DateTime(measurement.getTimestamp()), Double.parseDouble(measurement.getMeasuredValue()), measurement.getIndoorPlaceName());
				sensorEvents.add(lastSensorEvent);	
				log.debug("adding element for location " + location);
				lastUpdates.put(location, String.valueOf(lastSensorEvent.getHumidity()));
			}				
		}
		return sensorEvents;
	}
	
	public ArrayList<SensorEvent> splitTemperatureMeasurements(TemperatureMeasurement[] measurements, User primaryUser, HashMap<String,String> lastUpdates) {
		 ArrayList<SensorEvent> sensorEvents = new ArrayList<SensorEvent>();
		 if(measurements.length==0){
			 return sensorEvents;
		 }
		
		//First item, to take the reference
		TemperatureMeasurement temperatureMeasurement = measurements[0];
		double measured = 0.0;
		try{
			measured = Double.parseDouble(temperatureMeasurement.getMeasuredValue());
		} catch (Exception e){
			//log.error("Wrong value in temperature measurement, setting to default");
		}
		String location = temperatureMeasurement.getIndoorPlaceName();
		if(location == null || location.isEmpty()){
			location = SensorEvent.UNDEFINED;
		}
		TemperatureEvent lastSensorEvent = new TemperatureEvent(primaryUser.getUsername(), new DateTime(temperatureMeasurement.getTimestamp()), measured, temperatureMeasurement.getIndoorPlaceName());

		//We consider it as a change only if temperature  changes for more than 0.5 unit
		if(!lastUpdates.containsKey(location) || Math.abs(lastSensorEvent.getTemperature()-Double.parseDouble(lastUpdates.get(location))) > 0.5){
			sensorEvents.add(lastSensorEvent);
			log.debug("adding element for location " + location);
			lastUpdates.put(location, String.valueOf(lastSensorEvent.getTemperature()));
		}
		
		
		for(TemperatureMeasurement measurement : measurements){
			measured = 0.0;
			try{
				measured = Double.parseDouble(measurement.getMeasuredValue());
			} catch (Exception e){
				//log.error("Wrong value in temperature measurement, setting to default");
			}
			location = measurement.getIndoorPlaceName();
			if(location == null || location.isEmpty()){
				location = SensorEvent.UNDEFINED;
			}			
			//We consider it as a change only if temperature changes for more than 0.5 unit
			if(!lastUpdates.containsKey(location) ||  Math.abs(measured-Double.parseDouble(lastUpdates.get(location))) > 0.5){				
				lastSensorEvent = new TemperatureEvent(primaryUser.getUsername(), new DateTime(measurement.getTimestamp()), measured, measurement.getIndoorPlaceName());
				sensorEvents.add(lastSensorEvent);	
				log.debug("adding element for location " + location);
				lastUpdates.put(location, String.valueOf(lastSensorEvent.getTemperature()));
			}				
		}
		
		return sensorEvents;
	}

	public ArrayList<SensorEvent> splitIlluminanceMeasurements(IlluminanceMeasurement[] measurements, User primaryUser, HashMap<String,String> lastUpdates) {
		 ArrayList<SensorEvent> sensorEvents = new ArrayList<SensorEvent>();
		 if(measurements.length==0){
			 return sensorEvents;
		 }
		
		//First item, to take the reference hour
		IlluminanceMeasurement illuminanceMeasurement = measurements[0];
		String location = illuminanceMeasurement.getIndoorPlaceName();
		if(location == null || location.isEmpty()){
			location = SensorEvent.UNDEFINED;
		}
		IlluminanceEvent lastSensorEvent = new IlluminanceEvent(primaryUser.getUsername(), new DateTime(illuminanceMeasurement.getTimestamp()), Double.parseDouble(illuminanceMeasurement.getMeasuredValue()), illuminanceMeasurement.getIndoorPlaceName());
		
		//We consider it as a change only if illuminance change for more than 2 units (TODO: verify this)
		if(!lastUpdates.containsKey(location) || Math.abs(lastSensorEvent.getIlluminance()-Double.parseDouble(lastUpdates.get(location))) > 2){
			sensorEvents.add(lastSensorEvent);
			log.debug("adding element for location " + location);
			lastUpdates.put(location, String.valueOf(lastSensorEvent.getIlluminance()));
		}
		
		for(IlluminanceMeasurement measurement : measurements){
			location = measurement.getIndoorPlaceName();
			if(location == null || location.isEmpty()){
				location = SensorEvent.UNDEFINED;
			}			
			//We consider it as a change only if illuminance change for more than 2 units (TODO: verify this)
			if(!lastUpdates.containsKey(location) ||  Math.abs(Double.parseDouble(measurement.getMeasuredValue())-Double.parseDouble(lastUpdates.get(location))) > 2){				
				lastSensorEvent = new IlluminanceEvent(primaryUser.getUsername(), new DateTime(measurement.getTimestamp()), Double.parseDouble(measurement.getMeasuredValue()), measurement.getIndoorPlaceName());
				sensorEvents.add(lastSensorEvent);	
				log.debug("adding element for location " + location);
				lastUpdates.put(location, String.valueOf(lastSensorEvent.getIlluminance()));
			}				
		}
		return sensorEvents;
	}


	public ArrayList<SensorEvent> splitMattressMeasurements(MattressPressureSensing[] measurements, User primaryUser, HashMap<String,String> lastUpdates) {
		 ArrayList<SensorEvent> sensorEvents = new ArrayList<SensorEvent>();
		 if(measurements.length==0){
			 return sensorEvents;
		 }
		//First item, to take the reference hour
		MattressPressureSensing mattressPressureMeasurement = measurements[0];
		String location = mattressPressureMeasurement.getIndoorPlaceName();
		if(location == null || location.isEmpty()){
			location = SensorEvent.UNDEFINED;
		}
		MattressSensorEvent lastSensorEvent = new MattressSensorEvent(primaryUser.getUsername(), new DateTime(mattressPressureMeasurement.getTimestamp()), mattressPressureMeasurement.isPressure(), mattressPressureMeasurement.getImaValue(), mattressPressureMeasurement.getIndoorPlaceName());
		
		//We consider it as a change only if status is different from last known
		if(!lastUpdates.containsKey(location) || (lastSensorEvent.getPressure()!= Boolean.parseBoolean(lastUpdates.get(location)))){
			sensorEvents.add(lastSensorEvent);
			log.debug("adding element for location " + location);
			
			lastUpdates.put(location, String.valueOf(lastSensorEvent.getPressure()));
		}
		
		for(MattressPressureSensing measurement : measurements){
			location = measurement.getIndoorPlaceName();
			if(location == null || location.isEmpty()){
				location = SensorEvent.UNDEFINED;
			}			
			//We consider it as a change only if status is different from last known
			if(!lastUpdates.containsKey(location) || (measurement.isPressure()!= Boolean.parseBoolean(lastUpdates.get(location)))){				
				lastSensorEvent = new MattressSensorEvent(primaryUser.getUsername(), new DateTime(measurement.getTimestamp()),  measurement.isPressure(), measurement.getImaValue(), measurement.getIndoorPlaceName());
				sensorEvents.add(lastSensorEvent);	
				log.debug("adding element for location " + location);
				
				lastUpdates.put(location, String.valueOf(lastSensorEvent.getPressure()));
			}				
		}
		return sensorEvents;
	}

	public ArrayList<SensorEvent> splitPirMovementMeasurements(MovementMeasurement[] measurements, User primaryUser, HashMap<String,String> lastUpdates) {
		 ArrayList<SensorEvent> sensorEvents = new ArrayList<SensorEvent>();
		 if(measurements.length==0){
			 return sensorEvents;
		 }
		
		//First item, to take the reference hour
		MovementMeasurement pirMovementMeasurement = measurements[0];
		String location = pirMovementMeasurement.getIndoorPlaceName();
		if(location == null || location.isEmpty()){
			location = SensorEvent.UNDEFINED;
		}
		PirMovementEvent lastSensorEvent = new PirMovementEvent(primaryUser.getUsername(), new DateTime(pirMovementMeasurement.getTimestamp()), Boolean.parseBoolean(pirMovementMeasurement.getMeasuredValue()), pirMovementMeasurement.getIndoorPlaceName());
		
		//We consider it as a change only if status is different from last known
		if(!lastUpdates.containsKey(location) || (lastSensorEvent.getMovementDetected()!= Boolean.parseBoolean(lastUpdates.get(location)))){
			sensorEvents.add(lastSensorEvent);
			log.debug("adding element for location " + location);
			
			lastUpdates.put(location, String.valueOf(lastSensorEvent.getMovementDetected()));
		}
		
		for(MovementMeasurement measurement : measurements){
			location = measurement.getIndoorPlaceName();
			if(location == null || location.isEmpty()){
				location = SensorEvent.UNDEFINED;
			}			
			//We consider it as a change only if status is different from last known
			if(!lastUpdates.containsKey(location) || (Boolean.parseBoolean(measurement.getMeasuredValue()) != Boolean.parseBoolean(lastUpdates.get(location)))){	
				lastSensorEvent = new PirMovementEvent(primaryUser.getUsername(), new DateTime(measurement.getTimestamp()), Boolean.parseBoolean(measurement.getMeasuredValue()), measurement.getIndoorPlaceName());
				sensorEvents.add(lastSensorEvent);	
				log.debug("adding element for location " + location);
				
				lastUpdates.put(location, String.valueOf(lastSensorEvent.getMovementDetected()));				
			}
		}
		return sensorEvents;
	}

	public ArrayList<SensorEvent> splitDoorMeasurements(DoorStatus[] measurements, User primaryUser, HashMap<String,String> lastUpdates) {
		 ArrayList<SensorEvent> sensorEvents = new ArrayList<SensorEvent>();
		 if(measurements.length==0){
			 return sensorEvents;
		 }
		//First item, to take the reference hour
		DoorStatus doorMeasurement = measurements[0];
		String location = doorMeasurement.getIndoorPlaceName();
		if(location == null || location.isEmpty()){
			location = SensorEvent.UNDEFINED;
		}
		DoorEvent lastSensorEvent = new DoorEvent(primaryUser.getUsername(), new DateTime(doorMeasurement.getTimestamp()), Boolean.parseBoolean(doorMeasurement.getMeasuredValue()), doorMeasurement.getIndoorPlaceName());
		
		//We consider it as a change only if status is different from last known
		if(!lastUpdates.containsKey(location) || (lastSensorEvent.getDoorStatus()!= Boolean.parseBoolean(lastUpdates.get(location)))){
			sensorEvents.add(lastSensorEvent);
			log.debug("adding element for location " + location);
			lastUpdates.put(location, String.valueOf(lastSensorEvent.getDoorStatus()));
		}
		
		for(DoorStatus measurement : measurements){
			location = measurement.getIndoorPlaceName();
			if(location == null || location.isEmpty()){
				location = SensorEvent.UNDEFINED;
			}			
			//We consider it as a change only if status is different from last known
			if(!lastUpdates.containsKey(location) || (Boolean.parseBoolean(measurement.getMeasuredValue())!= Boolean.parseBoolean(lastUpdates.get(location)))){				
				lastSensorEvent = new DoorEvent(primaryUser.getUsername(), new DateTime(measurement.getTimestamp()),  Boolean.parseBoolean(measurement.getMeasuredValue()), measurement.getIndoorPlaceName());
				sensorEvents.add(lastSensorEvent);	
				log.debug("adding element for location " + location);
				lastUpdates.put(location, String.valueOf(lastSensorEvent.getDoorStatus()));
			}				
		}
		return sensorEvents;
	}

	public ArrayList<SensorEvent> splitVisualSensing(VisualSensing[] measurements, User primaryUser, HashMap<String,String> lastUpdates) {
		 ArrayList<SensorEvent> sensorEvents = new ArrayList<SensorEvent>();
		 if(measurements.length==0){
			 return sensorEvents;
		 }
		
		//First item, to take the reference 
		 VisualSensing visualSensing = measurements[0];
		String location = "livingroom";
		if(location == null || location.isEmpty()){
			location = SensorEvent.UNDEFINED;
		}
		VisualSensingEvent lastSensorEvent = new VisualSensingEvent(primaryUser.getUsername(), new DateTime(visualSensing.getTimestamp()), visualSensing.getX());
		lastSensorEvent.setTrackedFaces(visualSensing.getPositionConf());
		lastSensorEvent.setUsername(primaryUser.getUsername());
		lastSensorEvent.setLocation(location);
		//We consider it as a change only if humidity grows up by at least 0.5 % // changed to 5% 
		if(!lastUpdates.containsKey(location) ||(lastSensorEvent.getTrackedFaces()!=Double.parseDouble(lastUpdates.get(location)))){
			sensorEvents.add(lastSensorEvent);
			log.debug("adding element for location " + location);
			lastUpdates.put(location, String.valueOf(lastSensorEvent.getTrackedFaces()));
		}
		
		
		for(VisualSensing vSensing : measurements){
			location = "livingroom";
			if(location == null || location.isEmpty()){
				location = SensorEvent.UNDEFINED;
			}			
			//We consider it as a change only if humidity grows up by at least 0.5 %
			if(!lastUpdates.containsKey(location) ||(lastSensorEvent.getTrackedFaces()!=Double.parseDouble(lastUpdates.get(location)))){				
				lastSensorEvent = new VisualSensingEvent(primaryUser.getUsername(), new DateTime(vSensing.getTimestamp()), vSensing.getX());
				lastSensorEvent.setTrackedFaces(vSensing.getPositionConf());
				lastSensorEvent.setUsername(primaryUser.getUsername());
				lastSensorEvent.setLocation(location);
				sensorEvents.add(lastSensorEvent);	
				log.debug("adding element for location " + location);
				lastUpdates.put(location, String.valueOf(lastSensorEvent.getTrackedFaces()));
			}				
		}
		return sensorEvents;
	}
	
	public <T extends IndoorMeasurement, B extends GasEvent> ArrayList<SensorEvent> splitGasMeasurements(T measurements[], User primaryUser, HashMap<String,String> lastUpdates) {
		 ArrayList<SensorEvent> sensorEvents;
		try {
			sensorEvents = new ArrayList<SensorEvent>();
			 if(measurements.length==0){
				 return sensorEvents;
			 }
			 
			 
			//First item, to take the reference
			
			T gasMeasurement = measurements[0];
			double measured = 0.0;
			try{
				measured = Double.parseDouble(gasMeasurement.getMeasuredValue());
			} catch (Exception e){
				//log.error("Wrong value in gas measurement, setting to default");
			}
			String location = gasMeasurement.getIndoorPlaceName();
			if(location == null || location.isEmpty()){
				location = SensorEvent.UNDEFINED;
			}
			GasEvent lastSensorEvent = null; 
			Class<B> clazz = null;
			if(gasMeasurement instanceof LiquefiedPetroleumGasMeasurement){
				clazz = (Class<B>) Class.forName(GasLpgEvent.class.getName());
			} else if(gasMeasurement instanceof NaturalGasMeasurement){
				clazz = (Class<B>) Class.forName(GasNgEvent.class.getName());
			} else if(gasMeasurement instanceof CarbonMonoxideMeasurement){
				clazz = (Class<B>) Class.forName(GasCoEvent.class.getName());
			}
			Constructor<?> constructor = clazz.getConstructor(String.class, DateTime.class, double.class, String.class);
			lastSensorEvent = (GasEvent) constructor.newInstance(primaryUser.getUsername(), new DateTime(gasMeasurement.getTimestamp()), measured, gasMeasurement.getIndoorPlaceName());
			System.out.println("Clazz: " + lastSensorEvent.getClass().getName());
			
			//We consider it as a change only if gas value  changes for at least 1 unit
			if(!lastUpdates.containsKey(location) || Math.abs(((GasEvent)lastSensorEvent).getGasValue()-Double.parseDouble(lastUpdates.get(location))) >= 1){
				sensorEvents.add(lastSensorEvent);
				log.debug("adding element for location " + location);
				lastUpdates.put(location, String.valueOf(lastSensorEvent.getGasValue()));
			}
			
			
			for(T measurement : measurements){
				measured = 0.0;
				try{
					measured = Double.parseDouble(measurement.getMeasuredValue());
				} catch (Exception e){
					//log.error("Wrong value in temperature measurement, setting to default");
				}
				location = measurement.getIndoorPlaceName();
				if(location == null || location.isEmpty()){
					location = SensorEvent.UNDEFINED;
				}			
				//We consider it as a change only if temperature changes for at least 1 unit
				if(!lastUpdates.containsKey(location) ||  Math.abs(measured-Double.parseDouble(lastUpdates.get(location))) >= 1){				
					lastSensorEvent = (GasEvent) constructor.newInstance(primaryUser.getUsername(), new DateTime(measurement.getTimestamp()), measured, measurement.getIndoorPlaceName());
					sensorEvents.add(lastSensorEvent);	
					log.debug("adding element for location " + location);
					lastUpdates.put(location, String.valueOf(lastSensorEvent.getGasValue()));
				}				
			}
			return sensorEvents;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	

}
