package eu.ewall.servicebrick.dailyfunctioning.processor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import eu.ewall.platform.commons.datamodel.measure.CarbonMonoxideMeasurement;
import eu.ewall.platform.commons.datamodel.measure.AccelerometerMeasurement;
import eu.ewall.platform.commons.datamodel.measure.DoorStatus;
import eu.ewall.platform.commons.datamodel.measure.HumidityMeasurement;
import eu.ewall.platform.commons.datamodel.measure.IlluminanceMeasurement;
import eu.ewall.platform.commons.datamodel.measure.LiquefiedPetroleumGasMeasurement;
import eu.ewall.platform.commons.datamodel.measure.MattressPressureSensing;
import eu.ewall.platform.commons.datamodel.measure.MovementMeasurement;
import eu.ewall.platform.commons.datamodel.measure.NaturalGasMeasurement;
import eu.ewall.platform.commons.datamodel.measure.TemperatureMeasurement;
import eu.ewall.platform.commons.datamodel.sensing.VisualSensing;
import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.servicebrick.common.dao.DataManagerUpdatesDao;
import eu.ewall.servicebrick.common.dao.ProfilingServerDao;
import eu.ewall.servicebrick.common.dao.SensorEventDao;
import eu.ewall.servicebrick.common.model.SensorEvent;
import eu.ewall.servicebrick.common.model.SensorsUpdates;
import eu.ewall.servicebrick.dailyfunctioning.dao.FunctioningActivityDao;
import eu.ewall.servicebrick.dailyfunctioning.dao.InbedDao;
import eu.ewall.servicebrick.dailyfunctioning.dao.LocationDao;
import eu.ewall.servicebrick.dailyfunctioning.dao.SleepDao;
import eu.ewall.servicebrick.dailyfunctioning.model.DoorEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.FunctioningActivityEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.GasCoEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.GasEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.GasLpgEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.GasNgEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.HumidityEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.IlluminanceEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.InbedEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.LocationEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.MattressSensorEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.PirMovementEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.SleepHistory;
import eu.ewall.servicebrick.dailyfunctioning.model.TemperatureEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.VisualSensingEvent;
/**
 * Reads data from accelerometers at regular intervals and applies algorithms to determine physical activity info.
 * The extrapolated data is saved on the DB for further retrieval by the service brick front end.
 */
@Component
public class SensorsProcessor {

	private static final Logger log = LoggerFactory.getLogger(SensorsProcessor.class);
	private static final int DEFAULT_MAX_DAYS_PER_REQUEST = 20;
	@Autowired
	private ProfilingServerDao profilingServerDao;
	@Autowired
	private FunctioningActivityDao functioningActivityDao;
	@Autowired
	private InbedDao inbedDao;
	@Autowired
	private LocationDao locationDao;
	@Autowired
	private DataManagerUpdatesDao dataManagerUpdatesDao;
	@Autowired
	private SensorEventDao sensorEventDao;
	@Autowired
	private EventSplitter splitter;
	@Autowired
	private DailyFunctioningDetector dailyFunctioningDetector;
	@Autowired
	private SleepDao sleepDao;
	private List<DailyFunctioningDetector> multiUserDFM;
	
	@Value("${maxDaysPerRequest}")
	private String maxDaysPerRequestStr;

	@Value("${processing.startDateYYYYMMdd}")
	private String startDateYYYYMMdd;
	
    private long dfmTime = 0;
    private List<RoomSensing> rooms;

	private void manageFunctioningActivityUpdates(ArrayList<FunctioningActivityEvent> activityEvents) {
		functioningActivityDao.insertEvents(activityEvents);
	}

	private void manageInbedUpdates(ArrayList<InbedEvent> inbedEvents) {
		inbedDao.insertEvents(inbedEvents);
	}

	private void manageLocationUpdates(ArrayList<LocationEvent> locationEvents) {
		locationDao.insertEvents(locationEvents);
	}

	private void manageSensorUpdates(ArrayList<SensorEvent> sensorEvents) {
		sensorEventDao.insertEvents(sensorEvents);
	}

	public SensorsProcessor() {
		DateTime dt = new DateTime();
		dfmTime = dt.getMillis();
		rooms = new ArrayList<RoomSensing>();
		multiUserDFM = new ArrayList<DailyFunctioningDetector>();
	}
	
	@Scheduled(initialDelayString="${processing.delay}", fixedRateString="${processing.interval}")
	public void process() {
//		log.debug("Starting processing of sensors data");
		long start;
		
		
		//Step 1: get primary users
		User[] primaryUsers = profilingServerDao.getPrimaryUsers();
		
//		int nrOfUsers = primaryUsers.length;
		long nrOfMeasurements = 0;
    	int maxDaysPerRequest;
		
		try {
			if(maxDaysPerRequestStr==null || Integer.parseInt(maxDaysPerRequestStr)<=0) {
				maxDaysPerRequest = DEFAULT_MAX_DAYS_PER_REQUEST;
			} else {
				maxDaysPerRequest = Integer.parseInt(maxDaysPerRequestStr);
			}
		} catch (NumberFormatException e1) {
			log.error("Wrong configuration value for maxDaysPerRequest: " + e1.getMessage());
			maxDaysPerRequest = DEFAULT_MAX_DAYS_PER_REQUEST;
		}
		
	    SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd, HH:mm");
		DateTime now = new DateTime();
/*
		log.info("Waiting for " + sdf.format(new Date(dfmTime)));
		while (dfmTime > now.getMillis()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			now = new DateTime();
		}
*/


		//Step 2: cycle on primary users
		// Up to one minute from now
		Date to = new Date();
		to.setTime(dfmTime - 60*1000);
		// From eleven minutes ago
		Date from = new Date();
		from.setTime(dfmTime - (11*60*1000-1));
		log.info("*************************");
		log.info("*");
		log.info("* DFM for " + sdf.format(from));
		log.info("*");
		log.info("*************************");
		for(User primaryUser : primaryUsers){
			try {
				AlgoOutputItem lastAlgoOutputItem = null;
				start = System.currentTimeMillis();
				SensorsUpdates sensorsUpdates = dataManagerUpdatesDao.getSensorsReadingByUsername(primaryUser.getUsername());
				log.info(primaryUser.getUsername());
				boolean userInit = false;
				for (RoomSensing room : rooms) {
					if (room.getUserName().equals(primaryUser.getUsername())) {
						userInit = true;
						break;
					}
				}
				if (userInit == false) {
					rooms.add(new RoomSensing(primaryUser.getUsername(), "bedroom"));
					rooms.add(new RoomSensing(primaryUser.getUsername(), "livingroom"));
					rooms.add(new RoomSensing(primaryUser.getUsername(), "kitchen"));
					rooms.add(new RoomSensing(primaryUser.getUsername(), "bathroom"));
					multiUserDFM.add(new DailyFunctioningDetector(primaryUser.getUsername()));
				}
				//DailyFunctioningDetector dailyFunctioningDetector = null;
				for (int i = 0; i < multiUserDFM.size(); i++) {
					if (multiUserDFM.get(i).getUserName().equals(primaryUser.getUsername())) {
						dailyFunctioningDetector = multiUserDFM.get(i);
						break;
					}
				}
				if(sensorsUpdates!=null){
					log.info("    Got sensors reading for user " + sensorsUpdates.getUsername());
				}
				else{
					log.info("    Starting date for indexing: " + now);
					sensorsUpdates = new SensorsUpdates(primaryUser.getUsername(), now);
				}
				
				AlgoInputItem inputItem = new AlgoInputItem();
				
				for(String location : sensorsUpdates.getLastMattressUpdates().keySet()){
					String value = sensorsUpdates.getLastMattressUpdates().get(location);
					String key = MattressSensorEvent.class.getSimpleName()+"-"+location;
					inputItem.putEvent(key, new MattressSensorEvent(primaryUser.getUsername(), sensorsUpdates.getLastMattressUpdateTimestamp(), Boolean.parseBoolean(value), 0, location));					
				}
				for(String location : sensorsUpdates.getLastPirMovementUpdates().keySet()){
					String value = sensorsUpdates.getLastPirMovementUpdates().get(location);
					String key = PirMovementEvent.class.getSimpleName()+"-"+location;
					inputItem.putEvent(key, new PirMovementEvent(primaryUser.getUsername(), sensorsUpdates.getLastPirMovementUpdateTimestamp(), Boolean.parseBoolean(value), location));					
				}
				for(String location : sensorsUpdates.getLastHumidityUpdates().keySet()){
					String value = sensorsUpdates.getLastHumidityUpdates().get(location);
					String key = HumidityEvent.class.getSimpleName()+"-"+location;
					inputItem.putEvent(key, new HumidityEvent(primaryUser.getUsername(), sensorsUpdates.getLastHumidityUpdateTimestamp(), Double.parseDouble(value), location));					
				}
				for(String location : sensorsUpdates.getLastIlluminanceUpdates().keySet()){
					String value = sensorsUpdates.getLastIlluminanceUpdates().get(location);
					String key = IlluminanceEvent.class.getSimpleName()+"-"+location;
					inputItem.putEvent(key, new IlluminanceEvent(primaryUser.getUsername(), sensorsUpdates.getLastIlluminanceUpdateTimestamp(), Double.parseDouble(value), location));					
				}
				for(String location : sensorsUpdates.getLastTemperatureUpdates().keySet()){
					String value = sensorsUpdates.getLastTemperatureUpdates().get(location);
					String key = TemperatureEvent.class.getSimpleName()+"-"+location;
					inputItem.putEvent(key, new TemperatureEvent(primaryUser.getUsername(), sensorsUpdates.getLastTemperatureUpdateTimestamp(), Double.parseDouble(value), location));					
				}
				for(String location : sensorsUpdates.getLastDoorUpdates().keySet()){
					String value = sensorsUpdates.getLastDoorUpdates().get(location);
					String key = DoorEvent.class.getSimpleName()+"-"+location;
					inputItem.putEvent(key, new DoorEvent(primaryUser.getUsername(), sensorsUpdates.getLastDoorUpdateTimestamp(), Boolean.parseBoolean(value), location));					
				}
				for(String location : sensorsUpdates.getLastGasLpgUpdates().keySet()){
					String value = sensorsUpdates.getLastGasLpgUpdates().get(location);
					String key = GasEvent.class.getSimpleName()+"-"+location;
					inputItem.putEvent(key, new GasLpgEvent(primaryUser.getUsername(), sensorsUpdates.getLastGasLpgUpdateTimestamp(), Double.parseDouble(value), location));					
				}
				for(String location : sensorsUpdates.getLastGasNgUpdates().keySet()){
					String value = sensorsUpdates.getLastGasNgUpdates().get(location);
					String key = GasEvent.class.getSimpleName()+"-"+location;
					inputItem.putEvent(key, new GasNgEvent(primaryUser.getUsername(), sensorsUpdates.getLastGasNgUpdateTimestamp(), Double.parseDouble(value), location));					
				}
				for(String location : sensorsUpdates.getLastGasCoUpdates().keySet()){
					String value = sensorsUpdates.getLastGasCoUpdates().get(location);
					String key = GasEvent.class.getSimpleName()+"-"+location;
					inputItem.putEvent(key, new GasCoEvent(primaryUser.getUsername(), sensorsUpdates.getLastGasCoUpdateTimestamp(), Double.parseDouble(value), location));					
				}
				for(String location : sensorsUpdates.getLastVisualSensingUpdates().keySet()){
					String value = sensorsUpdates.getLastVisualSensingUpdates().get(location);
					String key = VisualSensing.class.getSimpleName()+"-"+location;
					inputItem.putEvent(key, new VisualSensingEvent(primaryUser.getUsername(), sensorsUpdates.getLastVisualSensingUpdateTimestamp(), Double.parseDouble(value), location));					
				}

				// Get all data in this interval from profiling server
				MovementMeasurement[] pirMovementMeasurements = profilingServerDao.getPirMovementData(primaryUser.getUsername(), from, to, DEFAULT_MAX_DAYS_PER_REQUEST);
				HumidityMeasurement[] humidityMeasurements = profilingServerDao.getHumidityData(primaryUser.getUsername(), from, to, DEFAULT_MAX_DAYS_PER_REQUEST);
				MattressPressureSensing[] mattressPressureMeasurements = profilingServerDao.getMattressPressureData(primaryUser.getUsername(), from, to, DEFAULT_MAX_DAYS_PER_REQUEST);
				IlluminanceMeasurement[] illuminanceMeasurements = profilingServerDao.getIlluminanceData(primaryUser.getUsername(), from, to, DEFAULT_MAX_DAYS_PER_REQUEST);
				TemperatureMeasurement[] temperatureMeasurements = profilingServerDao.getTemperatureData(primaryUser.getUsername(), from, to, DEFAULT_MAX_DAYS_PER_REQUEST);
				DoorStatus[] doorMeasurements = profilingServerDao.getDoorData(primaryUser.getUsername(), from, to, DEFAULT_MAX_DAYS_PER_REQUEST);
				VisualSensing[] visualSensing = profilingServerDao.getMoodData(primaryUser.getUsername(), from, to, DEFAULT_MAX_DAYS_PER_REQUEST);;
				LiquefiedPetroleumGasMeasurement[] gasLpgMeasurements = new LiquefiedPetroleumGasMeasurement[]{};
				NaturalGasMeasurement[] gasNgMeasurements = new NaturalGasMeasurement[]{};
				CarbonMonoxideMeasurement[] gasCoMeasurements = new CarbonMonoxideMeasurement[]{};
				try {
					gasLpgMeasurements = profilingServerDao.getGasLpgData(primaryUser.getUsername(), from, to, DEFAULT_MAX_DAYS_PER_REQUEST);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					gasNgMeasurements = profilingServerDao.getGasNgData(primaryUser.getUsername(), from, to, DEFAULT_MAX_DAYS_PER_REQUEST);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					gasCoMeasurements = profilingServerDao.getGasCoData(primaryUser.getUsername(), from, to, DEFAULT_MAX_DAYS_PER_REQUEST);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				nrOfMeasurements = (pirMovementMeasurements.length+humidityMeasurements.length+mattressPressureMeasurements.length+illuminanceMeasurements.length+temperatureMeasurements.length+doorMeasurements.length+gasLpgMeasurements.length+gasNgMeasurements.length+gasCoMeasurements.length);
				log.info("    Fetching " + String.valueOf(nrOfMeasurements) + " measurements in " + String.valueOf((System.currentTimeMillis() - start)/1000.0) + "sec");

				// Handle 10-min worth of data for current user
				//Step 2c: Process data and update MongoDB with new aggregated/extrapolated values using the DAOs
				//2.c.1: Remove duplicates
				ArrayList<SensorEvent> mattressEvents = splitter.splitMattressMeasurements(mattressPressureMeasurements, primaryUser, sensorsUpdates.getLastMattressUpdates());
				ArrayList<SensorEvent> humidityEvents = splitter.splitHumidityMeasurements(humidityMeasurements, primaryUser, sensorsUpdates.getLastHumidityUpdates());
				ArrayList<SensorEvent> pirMovementEvents = splitter.splitPirMovementMeasurements(pirMovementMeasurements, primaryUser, sensorsUpdates.getLastPirMovementUpdates());
				ArrayList<SensorEvent> illuminanceEvents = splitter.splitIlluminanceMeasurements(illuminanceMeasurements, primaryUser, sensorsUpdates.getLastIlluminanceUpdates());
				ArrayList<SensorEvent> temperatureEvents = splitter.splitTemperatureMeasurements(temperatureMeasurements, primaryUser, sensorsUpdates.getLastTemperatureUpdates());
				ArrayList<SensorEvent> doorEvents = splitter.splitDoorMeasurements(doorMeasurements, primaryUser, sensorsUpdates.getLastDoorUpdates());
				ArrayList<SensorEvent> gasLpgEvents = splitter.splitGasMeasurements(gasLpgMeasurements, primaryUser, sensorsUpdates.getLastGasLpgUpdates());
				ArrayList<SensorEvent> gasNgEvents = splitter.splitGasMeasurements(gasNgMeasurements, primaryUser, sensorsUpdates.getLastGasNgUpdates());
				ArrayList<SensorEvent> gasCoEvents = splitter.splitGasMeasurements(gasCoMeasurements, primaryUser, sensorsUpdates.getLastGasCoUpdates());
				ArrayList<SensorEvent> visualSensingEvents = splitter.splitVisualSensing(visualSensing, primaryUser, sensorsUpdates.getLastVisualSensingUpdates());
				
				//I have the events and must apply my ordering algorithm to group them into arrays
				//1: merge events
				ArrayList<SensorEvent> mergedEvents = new ArrayList<SensorEvent>();
				mergedEvents.addAll(mattressEvents);
				mergedEvents.addAll(humidityEvents);
				mergedEvents.addAll(pirMovementEvents);
				mergedEvents.addAll(illuminanceEvents);
				mergedEvents.addAll(temperatureEvents);
				mergedEvents.addAll(doorEvents);
				mergedEvents.addAll(visualSensingEvents);
				mergedEvents.addAll(gasLpgEvents);
				mergedEvents.addAll(gasNgEvents);
				mergedEvents.addAll(gasCoEvents);
				
				//2: sort events
				Collections.sort(mergedEvents, new Comparator<SensorEvent>() {
					  public int compare(SensorEvent m1, SensorEvent m2) {
						       if(m1.getTimestamp().compareTo(m2.getTimestamp())>0){
						    	   return 1;
						       } else if(m1.getTimestamp().compareTo(m2.getTimestamp())<0) {
						    	   return -1;
						       } else {
						    	   return 0;
						       }
					  };
				});
				
				// Find events in current 10 sec interval
				Date from10sec = from;
				Date to10sec = new Date(from10sec.getTime() + 9999);
				int idx = 0;
				ArrayList<FunctioningActivityEvent> functioningActivityEvents = new ArrayList<FunctioningActivityEvent>();
				ArrayList<InbedEvent> inbedEvents = new ArrayList<InbedEvent>();
				ArrayList<LocationEvent> locationEvents = new ArrayList<LocationEvent>();
				while (from10sec.before(to)) {
					for (int i = idx; i < mergedEvents.size(); i++) {
						SensorEvent event =  mergedEvents.get(i);
						if (event.getTimestamp().toDate().before(to10sec)) {
							String location = event.getLocation();
							// Find room to change
							for (RoomSensing room : rooms)
								if (room.getUserName().equals(primaryUser.getUsername()) && room.getRoomName().equals(location)) {
									// Find type of event for modification of room
									if (event instanceof HumidityEvent)
										room.setHumidity(((HumidityEvent)event).getHumidity());
									else if (event instanceof TemperatureEvent)
										room.setTemp(((TemperatureEvent)event).getTemperature());
									else if (event instanceof PirMovementEvent)
										room.setPresence(((PirMovementEvent)event).getMovementDetected());
									else if (event instanceof MattressSensorEvent)
										room.setBedPressure(((MattressSensorEvent)event).getPressure());
									else if (event instanceof DoorEvent)
										room.setDoorOpen(((DoorEvent)event).getDoorStatus());
									else if (event instanceof VisualSensingEvent)
										room.setFaceNo((int)((VisualSensingEvent)event).getTrackedFaces());
									else if (event instanceof GasCoEvent)
										room.setCo((int)((GasCoEvent)event).getGasValue());
									else if (event instanceof GasNgEvent)
										room.setNg((int)((GasNgEvent)event).getGasValue());
									break;
								}
						}
						else {
							idx = i;
							break;
						}
					}
					// 10 sec have elapsed and any new events have updated the status of the rooms
					// First clone the rooms of the particular user's home in List<RoomSensing> userRooms
					List<RoomSensing> userRooms = new ArrayList<>();
					for (int i = 0; i < 4; i++)
						userRooms.add(null);
					for (RoomSensing room : rooms) {
						if (room.getUserName().equals(primaryUser.getUsername()) && room.getRoomName().equals("bedroom"))
							userRooms.set(0, room);
						if (room.getUserName().equals(primaryUser.getUsername()) && room.getRoomName().equals("livingroom"))
							userRooms.set(1, room);
						if (room.getUserName().equals(primaryUser.getUsername()) && room.getRoomName().equals("kitchen"))
							userRooms.set(2, room);
						if (room.getUserName().equals(primaryUser.getUsername()) && room.getRoomName().equals("bathroom"))
							userRooms.set(3, room);
					}
					dailyFunctioningDetector.reasonTempHumidity(userRooms);
					dailyFunctioningDetector.recentPIR(userRooms, 0.9);
					dailyFunctioningDetector.reasonRoom(userRooms);
					dailyFunctioningDetector.reasonActivities(userRooms);
					DateTime dt = new DateTime(to10sec);
					AlgoOutputItem algoOutputItem = dailyFunctioningDetector.prepareOutput(dt);
					//dailyFunctioningDetector.writeOutput(dt);
					boolean changed = false;
					if(lastAlgoOutputItem==null || !lastAlgoOutputItem.getActivity().equals(algoOutputItem.getActivity())){
						FunctioningActivityEvent functioningActivityEvent = new FunctioningActivityEvent(primaryUser.getUsername(), algoOutputItem.getTimestamp(), algoOutputItem.getActivity());
						functioningActivityEvents.add(functioningActivityEvent);
						changed = true;
					}
					if(lastAlgoOutputItem==null || lastAlgoOutputItem.isInBed()!=(algoOutputItem.isInBed())){
						InbedEvent inBedEvent = new InbedEvent(primaryUser.getUsername(), algoOutputItem.getTimestamp(), algoOutputItem.isInBed());
						inbedEvents.add(inBedEvent);
						changed = true;
					}
					if(lastAlgoOutputItem==null || !lastAlgoOutputItem.getLocation().equals(algoOutputItem.getLocation())){
						LocationEvent locationEvent = new LocationEvent(primaryUser.getUsername(), algoOutputItem.getTimestamp(), algoOutputItem.getLocation());
						locationEvents.add(locationEvent);
						changed = true;
					}
					if (changed)
						dailyFunctioningDetector.report();

					lastAlgoOutputItem = (AlgoOutputItem) algoOutputItem.clone();
					// Update from/to for next iteration
					from10sec = new Date(from10sec.getTime() + 10000);
					to10sec = new Date(from10sec.getTime() + 9999);
				}
				inbedDao.insertEvents(inbedEvents);
				functioningActivityDao.insertEvents(functioningActivityEvents);
				locationDao.insertEvents(locationEvents);
				
				manageSensorUpdates(mattressEvents);
				manageSensorUpdates(humidityEvents);
				manageSensorUpdates(pirMovementEvents);
				manageSensorUpdates(illuminanceEvents);
				manageSensorUpdates(temperatureEvents);
				manageSensorUpdates(doorEvents);
				manageSensorUpdates(gasLpgEvents);
				manageSensorUpdates(gasNgEvents);
				manageSensorUpdates(gasCoEvents);
				manageSensorUpdates(visualSensingEvents);

				DateTime timestamp;
				boolean changed = false;
				if(pirMovementMeasurements.length > 0){
					timestamp = new DateTime(pirMovementMeasurements[pirMovementMeasurements.length-1].getTimestamp());
					sensorsUpdates.setLastPirMovementUpdateTimestamp(timestamp);
					changed = true;
				}
				if(humidityMeasurements.length > 0){
					timestamp = new DateTime(humidityMeasurements[humidityMeasurements.length-1].getTimestamp());
					sensorsUpdates.setLastHumidityUpdateTimestamp(timestamp);
					changed = true;
				}
				if(mattressPressureMeasurements.length > 0){
					timestamp = new DateTime(mattressPressureMeasurements[mattressPressureMeasurements.length-1].getTimestamp());
					sensorsUpdates.setLastMattressUpdateTimestamp(timestamp);
					changed = true;
				}
				if(illuminanceMeasurements.length > 0){
					timestamp = new DateTime(illuminanceMeasurements[illuminanceMeasurements.length-1].getTimestamp());
					sensorsUpdates.setLastIlluminanceUpdateTimestamp(timestamp);
					changed = true;
				}
				if(temperatureMeasurements.length > 0){
					timestamp = new DateTime(temperatureMeasurements[temperatureMeasurements.length-1].getTimestamp());
					sensorsUpdates.setLastTemperatureUpdateTimestamp(timestamp);
					changed = true;
				}
				if(doorMeasurements.length > 0){
					timestamp = new DateTime(doorMeasurements[doorMeasurements.length-1].getTimestamp());
					sensorsUpdates.setLastDoorUpdateTimestamp(timestamp);
					changed = true;
				}
				if(gasLpgMeasurements.length > 0){
					timestamp = new DateTime(gasLpgMeasurements[gasLpgMeasurements.length-1].getTimestamp());
					sensorsUpdates.setLastGasLpgUpdateTimestamp(timestamp);
					changed = true;
				}
				if(gasNgMeasurements.length > 0){
					timestamp = new DateTime(gasNgMeasurements[gasNgMeasurements.length-1].getTimestamp());
					sensorsUpdates.setLastGasNgUpdateTimestamp(timestamp);
					changed = true;
				}
				if(gasCoMeasurements.length > 0){
					timestamp = new DateTime(gasCoMeasurements[gasCoMeasurements.length-1].getTimestamp());
					sensorsUpdates.setLastGasCoUpdateTimestamp(timestamp);
					changed = true;
				}
				if(visualSensing.length > 0){
					timestamp = new DateTime(visualSensing[visualSensing.length-1].getTimestamp());
					sensorsUpdates.setLastVisualSensingUpdateTimestamp(timestamp);
					changed = true;
				}
				
				if(changed)
					dataManagerUpdatesDao.updateSensorsReading(sensorsUpdates);
				
				
				// Process Sleep Activity
				//processSleepActivity(primaryUser.getUsername(), maxDaysPerRequest);
				SleepActivityDetector sad = new SleepActivityDetector();
				 log.info("Sleep Activity Monitoring for user: ",primaryUser.getUsername()); 
				// Fetch mattress pressure data
			//	MattressPressureSensing[] mattressPressureSensings = profilingServerDao.getMattressPressureData(username, DateTime.now().minusDays(1).toDate(), DateTime.now().toDate(), maxDaysPerRequest);
				 MattressPressureSensing[] mattressPressureSensings = profilingServerDao.getMattressPressureData(primaryUser.getUsername(),sensorsUpdates.getLastMattressUpdateTimestamp().toDate(), new Date(), maxDaysPerRequest);
				 
				// Fetch accelerometer data
				AccelerometerMeasurement[] accelerometerMeasurements = profilingServerDao.getAccelerometerData(primaryUser.getUsername(),sensorsUpdates.getLastMattressUpdateTimestamp().minusSeconds(15).toDate(), new Date(), maxDaysPerRequest);

				// Return if null or empty
				if (mattressPressureSensings == null || accelerometerMeasurements == null || mattressPressureSensings.length == 0 || accelerometerMeasurements.length == 0) {
					
				}
				else
				{
				// Obtain last entries
				MattressPressureSensing lastMattressSensing = mattressPressureSensings[mattressPressureSensings.length - 1];
				AccelerometerMeasurement lastAccelerometerMeasurement = accelerometerMeasurements[accelerometerMeasurements.length - 1];

				// Return if null
				if (lastMattressSensing == null || lastAccelerometerMeasurement == null) {
					
				
				}
				// Obtain result
				double bedPressure=0;
				
				if(lastMattressSensing.isPressure())
					bedPressure=0;
				else
					bedPressure=1;
				
				int result = sad.getBestPath(bedPressure, lastAccelerometerMeasurement.getImaValue());
				
				// Obtain last reading of the SleepHistory
				SleepHistory lastHistory = sleepDao.readLastSleepHistory(primaryUser.getUsername());
				
				DateTime time_stamp = new DateTime(mattressPressureMeasurements[mattressPressureMeasurements.length - 1].getTimestamp());
				
			//	String timeString = String.valueOf(lastMattressSensing.getTimestamp());
			//	Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
			//			.parse(timeString);
				
			//	DateTime time_stamp = new DateTime(date);
				
				if (lastHistory == null) {
					SleepHistory newHistory = new SleepHistory(primaryUser.getUsername(), time_stamp, result);
					sleepDao.insertSleepHistory(newHistory);
				}
		      
				// If different, post new entry
				if (result != lastHistory.getState()) {
					//log.info("SleepActivity: "+bedPressure+" " +lastAccelerometerMeasurement.getImaValue()+ " "+ result); 
					SleepHistory newHistory = new SleepHistory(primaryUser.getUsername(), time_stamp, result);
					sleepDao.insertSleepHistory(newHistory);
					
				}
				}

			} catch (Exception e) {
				if(e instanceof HttpClientErrorException) {
					HttpStatus status = ((HttpClientErrorException)e).getStatusCode();
					if(status.equals(HttpStatus.NOT_FOUND)){
						log.error("    HttpClientErrorException: User " + primaryUser.getUsername() + " has no sensing environment associated. Cannot index any data");		
					} else {
						log.error("    HttpClientErrorException: " + e.getMessage());
					}
				} else {
					log.error("    Exception: " + e.getMessage(), e);
				}
			}
		}
		// DFM time forward by 10 min for next time process() is called
		dfmTime += 10*60*1000;
	}

}