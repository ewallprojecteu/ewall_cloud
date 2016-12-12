package eu.ewall.servicebrick.physicalactivity.processor;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;



import eu.ewall.platform.commons.datamodel.measure.AccelerometerMeasurement;
import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.servicebrick.common.AggregationUnit;
import eu.ewall.servicebrick.common.AggregationPeriod;
import eu.ewall.servicebrick.common.dao.DataManagerUpdatesDao;
import eu.ewall.servicebrick.common.dao.ProfilingServerDao;
import eu.ewall.servicebrick.common.model.AccelerometerUpdate;
import eu.ewall.servicebrick.physicalactivity.dao.InactivityDao;
import eu.ewall.servicebrick.physicalactivity.dao.MovementDao;
import eu.ewall.servicebrick.physicalactivity.dao.FitBitMovementDao;
import eu.ewall.servicebrick.physicalactivity.dao.PhysicalActivityDao;
import eu.ewall.servicebrick.physicalactivity.model.InactivityEvent;
import eu.ewall.servicebrick.physicalactivity.model.Movement;
import eu.ewall.servicebrick.physicalactivity.model.MovementUpdate;
import eu.ewall.servicebrick.physicalactivity.model.FitBitMovement;
import eu.ewall.servicebrick.physicalactivity.model.PhysicalActivityEvent;

import eu.ewall.servicebrick.common.model.FitBitActivityUpdate;


import org.springframework.data.mongodb.core.MongoOperations;



/**
 * Reads data from accelerometers at regular intervals and applies algorithms to determine physical activity info.
 * The extrapolated data is saved on the DB for further retrieval by the service brick front end.
 */

@RestController
@PropertySource("classpath:/service-brick-physicalactivity-dm-${ewall.env:local}.properties")
@Component
public class AccelerometerProcessor {

	private static final Logger log = LoggerFactory.getLogger(AccelerometerProcessor.class);
	
	private static final int DEFAULT_MAX_DAYS_PER_REQUEST = 20;
	
	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;
	@Autowired
	private ProfilingServerDao profilingServerDao;
	@Autowired
	private MovementDao movementDao;
	@Autowired
	private FitBitMovementDao fitBitMovementDao;
	@Autowired
	private PhysicalActivityDao physicalActivityDao;
	@Autowired
	private InactivityDao inactivityDao;
	@Autowired
	private DataManagerUpdatesDao dataManagerUpdatesDao;
	@Autowired
	Aggregator aggregator;
	@Autowired
	protected MongoOperations mongoOps;
	
	@Value("${maxDaysPerRequest}")
	private String maxDaysPerRequestStr;

	@Value("${processing.startDateYYYYMMdd}")
	private String startDateYYYYMMdd;
	
	@Value("${fitbitMovement.url}")
	private String fitbitMovementUrl;
	
	@Value("${fitbitMovement.url1}")
	private String fitbitMovementUrl1;
	
    SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMdd");

	
	private void manageMovementUpdates(Movement[] aggregated){
		ArrayList<Movement> batchInsertMovements = new ArrayList<Movement>(); 
		for(Movement movement : aggregated){
			log.debug("sim"+movement.getUsername() + " " + movement.getFrom() + " " + movement.getTo() + " " +  movement.getAggregation());
			log.debug("[" + movement.getAggregation() + "]	steps: " + movement.getSteps() + " kms: " + movement.getKilometers() + " kCal: " + movement.getBurnedCalories());
			Movement partialMovement = movementDao.readMovement(movement.getUsername() , movement.getFrom(),  movement.getTo(), movement.getAggregation());
			if(partialMovement!=null){
				partialMovement.addBurnedCalories(movement.getBurnedCalories());
				partialMovement.addKilometers(movement.getKilometers());
				partialMovement.addSteps(movement.getSteps());
				movementDao.updateMovement(partialMovement);
			} else {
				batchInsertMovements.add(movement);						
			}
		}
		if(batchInsertMovements.size()>0){
			movementDao.insertMovements(batchInsertMovements);
		}
	}
	
	private void manageMovementUpdatesFitBit(FitBitMovement[] aggregated){
		ArrayList<FitBitMovement> fitbitbatchInsertMovements = new ArrayList<FitBitMovement>(); 
		for(FitBitMovement fitbitmovement : aggregated){
			log.debug("fitbit"+fitbitmovement.getUsername() + " " + fitbitmovement.getFrom() + " " + fitbitmovement.getTo() + " " +  fitbitmovement.getAggregation());
			log.debug("[" + fitbitmovement.getAggregation() + "]	steps: " + fitbitmovement.getSteps() + " kms: " + fitbitmovement.getKilometers() + " kCal: " + fitbitmovement.getBurnedCalories());
			FitBitMovement fitbitpartialMovement = fitBitMovementDao.readMovement(fitbitmovement.getUsername() , fitbitmovement.getFrom(),  fitbitmovement.getTo(), fitbitmovement.getAggregation());
			if(fitbitpartialMovement!=null){
				fitbitpartialMovement.addBurnedCalories(fitbitmovement.getBurnedCalories());
				fitbitpartialMovement.addKilometers(fitbitmovement.getKilometers());
				fitbitpartialMovement.addSteps(fitbitmovement.getSteps());
				fitBitMovementDao.updateMovement(fitbitpartialMovement);
			} else {
				fitbitbatchInsertMovements.add(fitbitmovement);						
			}
		}
		if(fitbitbatchInsertMovements.size()>0){
			fitBitMovementDao.insertMovements(fitbitbatchInsertMovements);
		}
	}
	/**
	 * Updates physical activities in the related collection.
	 * Keeps track of the last activity.
	 * @param pau
	 * @param activityEvents
	 */
	private void managePhysicalActivityUpdates(ArrayList<PhysicalActivityEvent> activityEvents) {
		physicalActivityDao.insertEvents(activityEvents);
	}

	private void manageInactivityUpdates(ArrayList<InactivityEvent> inactivityEvents) {
		inactivityDao.insertEvents(inactivityEvents);
	}

	@Scheduled(initialDelayString="${processing.delay}", fixedRateString="${processing.interval}")
	public void process() {
		log.debug("Starting processing of accelerometer data");
		long start = System.currentTimeMillis();
		
		
		//Step 1: get primary users
		User[] primaryUsers = profilingServerDao.getPrimaryUsers();
		
		int nrOfUsers = primaryUsers.length;
		long nrOfMeasurements = 0;
		long nrOfAggregatedByHour = 0;
		long nrOfAggregatedByDay = 0;
		long nrOfAggregatedByWeek = 0;
		long nrOfAggregatedByMonth = 0;
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
		DateTime dt = new DateTime();
		try {
			dt = new DateTime(sdf.parse(startDateYYYYMMdd));
		} catch (ParseException e1) {
			log.error("Error in configured starting date 'processing.startDateYYYYMMdd'. Setting to default (now)");
		}
		//log.info("Physical Activity service brick: issuing requests for periods of " + maxDaysPerRequest + " days");
		log.info("Physical Activity service brick: Requests for FitBit data");
		
		//Step 2: cycle on primary users
		for(User primaryUser : primaryUsers){
		/*	try {
				//Step 2a: for each one check last timestamp update
				AccelerometerUpdate pau = dataManagerUpdatesDao.getAccelerometerReadingByUsername(primaryUser.getUsername());
				try {
					
					if(pau!=null){
						log.debug("Got accelerometer reading for user " + pau.getUsername() +  " - last update was: " + pau.getLastAccelerometerUpdate());
					}
				} catch (Exception e) {
					log.debug("Exception in getting accelerometer reading for user " + primaryUser.getUsername(), e);
				}
				if(pau==null){
					log.info("Starting date for indexing: " + dt);		
					pau = new AccelerometerUpdate(primaryUser.getUsername(), dt);
				}
				String lastActivityType = pau.getLastActivityType();
				boolean lastInactivityState = pau.getLastInactivityState();
				long lastStepsValue = pau.getLastStepsValue();

				AccelerometerMeasurement[] accelerometerMeasurements = profilingServerDao.getAccelerometerData(primaryUser.getUsername(), pau.getLastAccelerometerUpdate().toDate(), new Date(), maxDaysPerRequest);
				nrOfMeasurements += accelerometerMeasurements.length;
				
				//Step 2c: Process data and update MongoDB with new aggregated/extrapolated values using the DAOs
				//2.c.1:steps
				ArrayList<PhysicalActivityEvent> activityEvents = new ArrayList<PhysicalActivityEvent>();
				ArrayList<InactivityEvent> inactivityEvents = new ArrayList<InactivityEvent>();
				
				Movement[] aggregatedByHour = aggregator.aggregateByHour(
						accelerometerMeasurements, 
						primaryUser, 
						activityEvents, 
						lastActivityType,
						inactivityEvents,
						lastInactivityState,
						lastStepsValue);
				Movement[] aggregatedByDay = aggregator.aggregateByDay(aggregatedByHour, primaryUser);
				Movement[] aggregatedByWeek = aggregator.aggregateByWeek(aggregatedByDay, primaryUser);
				Movement[] aggregatedByMonth = aggregator.aggregateByMonth(aggregatedByDay, primaryUser);
				
				nrOfAggregatedByHour += aggregatedByHour.length;
				nrOfAggregatedByDay += aggregatedByDay.length;
				nrOfAggregatedByWeek += aggregatedByWeek.length;
				nrOfAggregatedByMonth += aggregatedByMonth.length;
				
				manageMovementUpdates(aggregatedByHour);
				manageMovementUpdates(aggregatedByDay);
				manageMovementUpdates(aggregatedByWeek);
				manageMovementUpdates(aggregatedByMonth);
				managePhysicalActivityUpdates(activityEvents);
				manageInactivityUpdates(inactivityEvents);
				
				DateTime timestamp;
				if(accelerometerMeasurements.length > 0){
					timestamp = new DateTime(accelerometerMeasurements[accelerometerMeasurements.length-1].getTimestamp());
					//Save the last activity type
					if(activityEvents.size()>0){
						lastActivityType = activityEvents.get(activityEvents.size()-1).getActivityType().name();
					}
					lastStepsValue = accelerometerMeasurements[accelerometerMeasurements.length-1].getSteps();
					pau.setLastAccelerometerUpdate(timestamp);
					pau.setLastStepsValue(lastStepsValue);
					pau.setLastActivityType(lastActivityType);
					pau.setLastInactivityState(lastInactivityState);
					dataManagerUpdatesDao.updateAccelerometerReading(pau);
				}
			} catch (Exception e) {
				if(e instanceof HttpClientErrorException) {
					HttpStatus status = ((HttpClientErrorException)e).getStatusCode();
					if(status.equals(HttpStatus.NOT_FOUND)){
						log.error("HttpClientErrorException: User " + primaryUser.getUsername() + " has no sensing environment associated. Cannot index any data");		
					} else {
						log.error("HttpClientErrorException: " + e.getMessage());
					}
				} else {
					log.error("Exception: " + e.getMessage(), e);
				}
			} */
			
			try {
				
				//FitBit check and update
				FitBitActivityUpdate fbau = null;
				int lastSteps = 0;
				int lastCalories= 0;
				double lastDistance=0;
			
				
				String url = fitbitMovementUrl+"?username="+primaryUser.getUsername();   
				
				
				MovementUpdate fitbit_updates = new MovementUpdate();
				MovementUpdate previouse_day = null;
				try
				{
				     fitbit_updates = ewallClient.getForObject(url, MovementUpdate.class);
				}
				catch (Exception e) {
					if(e instanceof HttpClientErrorException) {
						HttpStatus status = ((HttpClientErrorException)e).getStatusCode();
						if(status.equals(HttpStatus.NOT_FOUND)){
							log.error("HttpClientErrorException: User " + primaryUser.getUsername() + " has no FitBit associated. Cannot index any data");		
						} else {
							log.error("HttpClientErrorException: " + e.getMessage());
						}
					} 
				
				}
			
				DateTimeZone timeZone = DateTimeZone.forID( primaryUser.getUserProfile().getvCardSubProfile().getTimezoneid() );
	
				
				DateTime lastRetrieveTime = new DateTime(fitbit_updates.getLastRetrieveTime(),timeZone);
				DateTime lastTrackerSyncTime = new DateTime(fitbit_updates.getLastTrackerSyncTime(),timeZone);	
				DateTime updateTrackerSyncTime = new DateTime(timeZone);	
			    Boolean newDay = false; 
					try {
						fbau = dataManagerUpdatesDao.getFitBitActivityReadingByUsername(primaryUser.getUsername());
						if(fbau!=null){
							log.debug("Got FitBit reading for user " + fbau.getUsername() +  " - last update was: " + fbau.getLastTrackerSyncTime());
							lastSteps = fbau.getLastSteps();
							lastCalories = fbau.getLastCalories();
							lastDistance = fbau.getLastDistance();
							//updateTrackerSyncTime = new DateTime(fbau.getLastTrackerSyncTime(),timeZone);	
						}
					} catch (Exception e) {
						log.debug("Exception in getting FitBit update reading for user " + primaryUser.getUsername(), e);
					}
					
					if(fitbit_updates!=null)
					if(fbau==null){
						
						log.debug("Starting date for FitBit indexing: " + dt +primaryUser.getUsername() );		
						fbau = new FitBitActivityUpdate(primaryUser.getUsername(), dt);
						
					}
				
				
				updateTrackerSyncTime = new DateTime(fbau.getLastTrackerSyncTime(),timeZone);	
			//	log.info("username "+primaryUser.getUsername());
			 //   log.info("updateTrackerSyncTime "+updateTrackerSyncTime + "  "+updateTrackerSyncTime.dayOfYear().get());
			//    log.info("lastTrackerSyncTime "+lastTrackerSyncTime + "  "+lastTrackerSyncTime.dayOfYear().get());
			    
				if(fitbit_updates.getUsername()!=null)
				if(updateTrackerSyncTime.isBefore(lastTrackerSyncTime))
				{
				//	log.info("0");
					FitBitMovement[] fitbitaggregatedByHour;
				//	log.info("username "+primaryUser.getUsername());
				//	 log.info("updateTrackerSyncTimeDayOfYear "+ updateTrackerSyncTime.dayOfYear().get());
				//	 log.info("lastTrackerSyncTimeDayOfYear "+ lastTrackerSyncTime.dayOfYear().get());
					
					if(updateTrackerSyncTime.dayOfYear().get()!=lastTrackerSyncTime.dayOfYear().get())
					{
						int difference = lastTrackerSyncTime.dayOfYear().get() -updateTrackerSyncTime.dayOfYear().get();
						  int flag_null =0;
						    int flag_null1=0;
						if(difference>3)
							difference=3;
						
						if(difference>1)
						{
					//		log.info("00");
					//	log.info("difference "+difference);
						while (difference>1)
						{
					//		log.info("000");
						
						DateTime from = new DateTime(lastTrackerSyncTime,timeZone);
						
						from = from.minusDays(difference);
						from = from.withHourOfDay(0);
						from = from.withMinuteOfHour(0);
						from = from.withSecondOfMinute(0);
						from = from.withMillisOfSecond(0);
						
						DateTime to = new DateTime(from,timeZone);
						to=to.plusDays(1);
					//	log.info("from "+ from);
					//	log.info("to "+ to);
					//	log.info("lastTrackerSyncTimeDayOfYear "+lastTrackerSyncTime.dayOfYear().get());
					    AggregationPeriod agr = new AggregationPeriod(1,AggregationUnit.DAY);
					  
					    FitBitMovement dayMovement = null;
					   if(fitBitMovementDao.readMovement(primaryUser.getUsername(), from, to, agr)!=null)
					 
					     dayMovement = fitBitMovementDao.readMovement(primaryUser.getUsername(), from, to, agr);
					     else
					     
							flag_null=1;
								
						
					    
					  /*  else
					     flag_null=1;*/	
						
					//	log.info("dayMovement "+ /*flag_null + */" " + dayMovement);
						
						String url1 = fitbitMovementUrl1+"?username="+primaryUser.getUsername()+"&day="+from.getDayOfMonth()+
								"&month="+from.getMonthOfYear()+"&year="+from.getYear(); 
						
						 if(ewallClient.getForObject(url1, MovementUpdate.class)!=null)
						   previouse_day = ewallClient.getForObject(url1, MovementUpdate.class);
						 else
							 flag_null1=1; 
					
			
					//	log.info("flag_null "+flag_null);
					//	log.info("flag_null1 "+flag_null1);
					//	log.info("name "+ dayMovement.getUsername());
					//	log.info("lastSteps "+ dayMovement.getSteps());
						
						if(flag_null1==0)
						{
							//log.info("flag_null1==0");
							
						if(flag_null==1)
						{
						//	log.info("1 ");
							//log.info("flag_null==1");
								lastSteps=0;	
								lastCalories=0;
								lastDistance=0;
						
							DateTime temp_time = new DateTime(from,timeZone);
							temp_time=temp_time.withHourOfDay(11);
							temp_time=temp_time.withMinuteOfHour(00);
						//	log.info("temp_time "+temp_time);
							previouse_day.setLastTrackerSyncTime(temp_time.toString());
							
							temp_time=temp_time.minusHours(1);
						newDay = true;
						fitbitaggregatedByHour = aggregator.aggregateByHourFitBit(
								previouse_day, 
								primaryUser.getUsername(), 
								timeZone,
								lastSteps, 
								lastCalories,
								lastDistance,
								temp_time,
								newDay);
						
						FitBitMovement[] fitbitaggregatedByDay = aggregator.aggregateByDayFitBit(fitbitaggregatedByHour, primaryUser.getUsername(),timeZone);
						FitBitMovement[] fitbitaggregatedByWeek = aggregator.aggregateByWeekFitBit(fitbitaggregatedByDay, primaryUser.getUsername(),timeZone);
						FitBitMovement[] fitbitaggregatedByMonth = aggregator.aggregateByMonthFitBit(fitbitaggregatedByDay, primaryUser.getUsername(),timeZone);
							
						nrOfAggregatedByHour += fitbitaggregatedByHour.length;
						nrOfAggregatedByDay += fitbitaggregatedByDay.length;
						nrOfAggregatedByWeek += fitbitaggregatedByWeek.length;
						nrOfAggregatedByMonth += fitbitaggregatedByMonth.length;
						
						manageMovementUpdatesFitBit(fitbitaggregatedByHour);
						manageMovementUpdatesFitBit(fitbitaggregatedByDay);
						manageMovementUpdatesFitBit(fitbitaggregatedByWeek);
						manageMovementUpdatesFitBit(fitbitaggregatedByMonth);
								    
						/*	fbau.setLastRetrieveTime(lastRetrieveTime);
							fbau.setLastSteps(fitbit_updates.getSteps());
							fbau.setLastCalories(fitbit_updates.getActivityCalories());
							fbau.setLastDistance(fitbit_updates.getDistance());
							temp_time=temp_time.plusMinutes(3);
						
							fbau.setLastTrackerSyncTime(temp_time);
							
							dataManagerUpdatesDao.updateFitBitReading(fbau);*/
						}	
						else
						{
						//	log.info("2");
						//	log.info("flag_null=0 ");
							
						//	log.info("previose day steps " + previouse_day.getSteps() /*+ " dayMovement" + dayMovement.getSteps()*/);
							
							if((previouse_day.getSteps()>dayMovement.getSteps()))
							{
								
							
								DateTime temp_time = new DateTime(from,timeZone);
								temp_time=temp_time.withHourOfDay(11);
								temp_time=temp_time.withMinuteOfHour(00);
							//	log.info("temp_time "+temp_time);
								previouse_day.setLastTrackerSyncTime(temp_time.toString());
								
								temp_time=temp_time.minusHours(1);
							newDay = true;
							fitbitaggregatedByHour = aggregator.aggregateByHourFitBit(
									previouse_day, 
									primaryUser.getUsername(), 
									timeZone,
									lastSteps, 
									lastCalories,
									lastDistance,
									temp_time,
									newDay);
							
							FitBitMovement[] fitbitaggregatedByDay = aggregator.aggregateByDayFitBit(fitbitaggregatedByHour, primaryUser.getUsername(),timeZone);
							FitBitMovement[] fitbitaggregatedByWeek = aggregator.aggregateByWeekFitBit(fitbitaggregatedByDay, primaryUser.getUsername(),timeZone);
							FitBitMovement[] fitbitaggregatedByMonth = aggregator.aggregateByMonthFitBit(fitbitaggregatedByDay, primaryUser.getUsername(),timeZone);
								
							nrOfAggregatedByHour += fitbitaggregatedByHour.length;
							nrOfAggregatedByDay += fitbitaggregatedByDay.length;
							nrOfAggregatedByWeek += fitbitaggregatedByWeek.length;
							nrOfAggregatedByMonth += fitbitaggregatedByMonth.length;
							
							manageMovementUpdatesFitBit(fitbitaggregatedByHour);
							manageMovementUpdatesFitBit(fitbitaggregatedByDay);
							manageMovementUpdatesFitBit(fitbitaggregatedByWeek);
							manageMovementUpdatesFitBit(fitbitaggregatedByMonth);
									    
							/*	fbau.setLastRetrieveTime(lastRetrieveTime);
								fbau.setLastSteps(fitbit_updates.getSteps());
								fbau.setLastCalories(fitbit_updates.getActivityCalories());
								fbau.setLastDistance(fitbit_updates.getDistance());
								temp_time=temp_time.plusMinutes(3);
							
								fbau.setLastTrackerSyncTime(temp_time);
								
								dataManagerUpdatesDao.updateFitBitReading(fbau);*/	
						}
						
						
						}
						}
						else
						{
						//	log.info("3");
							fbau = new FitBitActivityUpdate(primaryUser.getUsername(), from);
							fbau.setLastRetrieveTime(from);
							fbau.setLastSteps(0);
							fbau.setLastCalories(0);
							fbau.setLastDistance(0);
							fbau.setLastTrackerSyncTime(from);
							dataManagerUpdatesDao.updateFitBitReading(fbau);
						}
						
						difference--;
						
						}
						
						
						lastTrackerSyncTime = lastTrackerSyncTime.minusDays(1);
						
					//	log.info("lastTrackerSyncTimeDayOfYear :"+lastTrackerSyncTime.dayOfYear().get());
					
						String url1 = fitbitMovementUrl1+"?username="+primaryUser.getUsername()+"&day="+lastTrackerSyncTime.getDayOfMonth()+
								"&month="+lastTrackerSyncTime.getMonthOfYear()+"&year="+lastTrackerSyncTime.getYear(); 
						
						flag_null1=0;
						if(ewallClient.getForObject(url1, MovementUpdate.class)!=null)
						previouse_day = ewallClient.getForObject(url1, MovementUpdate.class);
						else
						flag_null1=1; 
						
						//log.info("previouse_day.getSteps "+previouse_day.getSteps());
						//log.info("lastSteps "+lastSteps);
						
						
						if(flag_null1==1)
						{
						//	log.info("4");
							//lastTrackerSyncTime = lastTrackerSyncTime;
							fbau = new FitBitActivityUpdate(primaryUser.getUsername(), lastTrackerSyncTime);
							fbau.setLastRetrieveTime(lastTrackerSyncTime);
							fbau.setLastSteps(0);
							fbau.setLastCalories(0);
							fbau.setLastDistance(0);
							fbau.setLastTrackerSyncTime(lastTrackerSyncTime);
							dataManagerUpdatesDao.updateFitBitReading(fbau);
						}
						else	
						if(previouse_day.getSteps()>=lastSteps)
						{
						//	log.info("5");
							if(previouse_day.getSteps()==lastSteps)
							{
								previouse_day.setSteps(1);
							}
							DateTime temp_time = new DateTime(lastTrackerSyncTime,timeZone);
							temp_time=temp_time.withHourOfDay(23);
							temp_time=temp_time.withMinuteOfHour(59);
						//	log.info("temp_time "+temp_time);
							previouse_day.setLastTrackerSyncTime(temp_time.toString());
							temp_time=temp_time.withMinuteOfHour(58);
						newDay = true;
						fitbitaggregatedByHour = aggregator.aggregateByHourFitBit(
								previouse_day, 
								primaryUser.getUsername(), 
								timeZone,
								lastSteps, 
								lastCalories,
								lastDistance,
								temp_time,
								newDay);
						
						FitBitMovement[] fitbitaggregatedByDay = aggregator.aggregateByDayFitBit(fitbitaggregatedByHour, primaryUser.getUsername(),timeZone);
						FitBitMovement[] fitbitaggregatedByWeek = aggregator.aggregateByWeekFitBit(fitbitaggregatedByDay, primaryUser.getUsername(),timeZone);
						FitBitMovement[] fitbitaggregatedByMonth = aggregator.aggregateByMonthFitBit(fitbitaggregatedByDay, primaryUser.getUsername(),timeZone);
							
						nrOfAggregatedByHour += fitbitaggregatedByHour.length;
						nrOfAggregatedByDay += fitbitaggregatedByDay.length;
						nrOfAggregatedByWeek += fitbitaggregatedByWeek.length;
						nrOfAggregatedByMonth += fitbitaggregatedByMonth.length;
						
						manageMovementUpdatesFitBit(fitbitaggregatedByHour);
						manageMovementUpdatesFitBit(fitbitaggregatedByDay);
						manageMovementUpdatesFitBit(fitbitaggregatedByWeek);
						manageMovementUpdatesFitBit(fitbitaggregatedByMonth);
								    
							fbau.setLastRetrieveTime(lastRetrieveTime);
						//	fbau.setLastSteps(fitbit_updates.getSteps());
						//	fbau.setLastCalories(fitbit_updates.getActivityCalories());
						//	fbau.setLastDistance(fitbit_updates.getDistance());
							fbau.setLastSteps(0);
							fbau.setLastCalories(0);
							fbau.setLastDistance(0);
								
							temp_time=temp_time.plusMinutes(3);
					//		log.info("temp_time "+temp_time);
							fbau.setLastTrackerSyncTime(temp_time);
							
							dataManagerUpdatesDao.updateFitBitReading(fbau);
						}
						}
						else
						{
						//	log.info("6");
						lastTrackerSyncTime = lastTrackerSyncTime.minusDays(1);
						
					//	log.info("lastTrackerSyncTimeDayOfMonth "+lastTrackerSyncTime.dayOfMonth().get());
					
						String url1 = fitbitMovementUrl1+"?username="+primaryUser.getUsername()+"&day="+lastTrackerSyncTime.getDayOfMonth()+
								"&month="+lastTrackerSyncTime.getMonthOfYear()+"&year="+lastTrackerSyncTime.getYear(); 
						
						flag_null1=0;
						if(ewallClient.getForObject(url1, MovementUpdate.class)!=null)
						previouse_day = ewallClient.getForObject(url1, MovementUpdate.class);
						else
						flag_null1=1; 
						
						//log.info("previouse_day.getSteps "+previouse_day.getSteps());
						//log.info("lastSteps "+lastSteps);
						
						if(flag_null1==1)
						{
							lastTrackerSyncTime = lastTrackerSyncTime.plusDays(1);
							fbau = new FitBitActivityUpdate(primaryUser.getUsername(), lastTrackerSyncTime);
							fbau.setLastRetrieveTime(lastTrackerSyncTime);
							fbau.setLastSteps(0);
							fbau.setLastCalories(0);
							fbau.setLastDistance(0);
							fbau.setLastTrackerSyncTime(lastTrackerSyncTime);
							dataManagerUpdatesDao.updateFitBitReading(fbau);
						}
						else	
						if(previouse_day.getSteps()>lastSteps)
						{
						//	log.info("7");
							DateTime temp_time = new DateTime(fbau.getLastTrackerSyncTime(),timeZone);
							temp_time=temp_time.withHourOfDay(23);
							temp_time=temp_time.withMinuteOfHour(59);
						//	log.info("temp_time "+temp_time);
							previouse_day.setLastTrackerSyncTime(temp_time.toString());
							temp_time=temp_time.withMinuteOfHour(58);
						newDay = true;
						fitbitaggregatedByHour = aggregator.aggregateByHourFitBit(
								previouse_day, 
								primaryUser.getUsername(), 
								timeZone,
								lastSteps, 
								lastCalories,
								lastDistance,
								temp_time,
								newDay);
						
						FitBitMovement[] fitbitaggregatedByDay = aggregator.aggregateByDayFitBit(fitbitaggregatedByHour, primaryUser.getUsername(),timeZone);
						FitBitMovement[] fitbitaggregatedByWeek = aggregator.aggregateByWeekFitBit(fitbitaggregatedByDay, primaryUser.getUsername(),timeZone);
						FitBitMovement[] fitbitaggregatedByMonth = aggregator.aggregateByMonthFitBit(fitbitaggregatedByDay, primaryUser.getUsername(),timeZone);
							
						nrOfAggregatedByHour += fitbitaggregatedByHour.length;
						nrOfAggregatedByDay += fitbitaggregatedByDay.length;
						nrOfAggregatedByWeek += fitbitaggregatedByWeek.length;
						nrOfAggregatedByMonth += fitbitaggregatedByMonth.length;
						
						manageMovementUpdatesFitBit(fitbitaggregatedByHour);
						manageMovementUpdatesFitBit(fitbitaggregatedByDay);
						manageMovementUpdatesFitBit(fitbitaggregatedByWeek);
						manageMovementUpdatesFitBit(fitbitaggregatedByMonth);
								    
							fbau.setLastRetrieveTime(lastRetrieveTime);
						//	fbau.setLastSteps(fitbit_updates.getSteps());
						//	fbau.setLastCalories(fitbit_updates.getActivityCalories());
						//	fbau.setLastDistance(fitbit_updates.getDistance());
							fbau.setLastSteps(0);
							fbau.setLastCalories(0);
							fbau.setLastDistance(0);
							temp_time=temp_time.plusMinutes(3);
						//	log.info("temp_time "+temp_time);
							fbau.setLastTrackerSyncTime(temp_time);
							
							dataManagerUpdatesDao.updateFitBitReading(fbau);
						}
						
						else
						{
						//	log.info("8");
							
							lastTrackerSyncTime = lastTrackerSyncTime.plusDays(1);    
							fbau.setLastRetrieveTime(lastRetrieveTime);
							fbau.setLastTrackerSyncTime(lastTrackerSyncTime);
							
							dataManagerUpdatesDao.updateFitBitReading(fbau);	
						}
					
						}
					}
					else
					{
					//	log.info("9");
						newDay = false;
					fitbitaggregatedByHour = aggregator.aggregateByHourFitBit(
						fitbit_updates, 
						primaryUser.getUsername(), 
						timeZone,
						lastSteps, 
						lastCalories,
						lastDistance,
						updateTrackerSyncTime,
						//fbau.getLastTrackerSyncTime(),
						newDay);
					
					
				FitBitMovement[] fitbitaggregatedByDay = aggregator.aggregateByDayFitBit(fitbitaggregatedByHour, primaryUser.getUsername(),timeZone);
				FitBitMovement[] fitbitaggregatedByWeek = aggregator.aggregateByWeekFitBit(fitbitaggregatedByDay, primaryUser.getUsername(),timeZone);
				FitBitMovement[] fitbitaggregatedByMonth = aggregator.aggregateByMonthFitBit(fitbitaggregatedByDay, primaryUser.getUsername(),timeZone);
					
				nrOfAggregatedByHour += fitbitaggregatedByHour.length;
				nrOfAggregatedByDay += fitbitaggregatedByDay.length;
				nrOfAggregatedByWeek += fitbitaggregatedByWeek.length;
				nrOfAggregatedByMonth += fitbitaggregatedByMonth.length;
				
				manageMovementUpdatesFitBit(fitbitaggregatedByHour);
				manageMovementUpdatesFitBit(fitbitaggregatedByDay);
				manageMovementUpdatesFitBit(fitbitaggregatedByWeek);
				manageMovementUpdatesFitBit(fitbitaggregatedByMonth);
						    
					fbau.setLastRetrieveTime(lastRetrieveTime);
					fbau.setLastSteps(fitbit_updates.getSteps());
					fbau.setLastCalories(fitbit_updates.getActivityCalories());
					fbau.setLastDistance(fitbit_updates.getDistance());
					fbau.setLastTrackerSyncTime(lastTrackerSyncTime);
					
					dataManagerUpdatesDao.updateFitBitReading(fbau);
				}
				}
		
				
			} catch (Exception e) {
				if(e instanceof HttpClientErrorException) {
					HttpStatus status = ((HttpClientErrorException)e).getStatusCode();
					if(status.equals(HttpStatus.NOT_FOUND)){
						log.error("HttpClientErrorException: User " + primaryUser.getUsername() + " has no sensing environment associated. Cannot index any data");		
					} else {
						log.error("HttpClientErrorException: " + e.getMessage());
					}
				} else {
						log.error("Exception: " + e.getMessage());
				}
			}
			
		}
	//	log.info("Stats for physical activity data processor: duration " + ((double)(System.currentTimeMillis()-start)/1000)+ " ms.; managed " + nrOfUsers + " users; " + nrOfMeasurements + " accelerometerMeasurements; " + nrOfAggregatedByHour + " aggregatedByHour; " + nrOfAggregatedByDay + " aggregatedByDay; " + nrOfAggregatedByWeek + " aggregatedByWeek; " + nrOfAggregatedByMonth + " aggregatedByMonth;" );
		log.info("Stats for physical activity data processor: duration " + ((double)(System.currentTimeMillis()-start)/1000)+ " ms.; managed " + nrOfUsers + " users; " + nrOfAggregatedByHour + " aggregatedByHour; " + nrOfAggregatedByDay + " aggregatedByDay; " + nrOfAggregatedByWeek + " aggregatedByWeek; " + nrOfAggregatedByMonth + " aggregatedByMonth;" );
		
			
	}

	
}
