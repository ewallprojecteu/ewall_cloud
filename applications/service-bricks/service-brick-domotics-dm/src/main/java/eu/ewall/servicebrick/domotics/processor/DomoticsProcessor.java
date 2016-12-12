package eu.ewall.servicebrick.domotics.processor;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import org.joda.time.DateTime;
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



import eu.ewall.platform.commons.datamodel.measure.HumidityMeasurement;
import eu.ewall.platform.commons.datamodel.measure.IlluminanceMeasurement;
import eu.ewall.platform.commons.datamodel.measure.TemperatureMeasurement;

import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.servicebrick.common.dao.DataManagerUpdatesDao;
import eu.ewall.servicebrick.common.dao.ProfilingServerDao;

import eu.ewall.servicebrick.common.model.DomoticsUpdates;

import eu.ewall.servicebrick.domotics.dao.DomoticsDao;

import eu.ewall.servicebrick.domotics.model.HumidityEventDomotics;
import eu.ewall.servicebrick.domotics.model.IlluminanceEventDomotics;
import eu.ewall.servicebrick.domotics.model.TemperatureEventDomotics;


import org.springframework.data.mongodb.core.MongoOperations;



/**
 * Reads data from accelerometers at regular intervals and applies algorithms to determine physical activity info.
 * The extrapolated data is saved on the DB for further retrieval by the service brick front end.
 */

@RestController
@PropertySource("classpath:/service-brick-domotics-dm-${ewall.env:local}.properties")
@Component
public class DomoticsProcessor {

	private static final Logger log = LoggerFactory.getLogger(DomoticsProcessor.class);
	
	private static final int DEFAULT_MAX_DAYS_PER_REQUEST = 20;
	
	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;
	@Autowired
	private ProfilingServerDao profilingServerDao;
	@Autowired
	private DataManagerUpdatesDao dataManagerUpdatesDao;
	@Autowired
	private DomoticsDao domoticsDao;
	@Autowired
	DomoticsAverage domoticsAverage;
	@Autowired
	protected MongoOperations mongoOps;
	
	@Value("${maxDaysPerRequest}")
	private String maxDaysPerRequestStr;

	@Value("${processing.startDateYYYYMMdd}")
	private String startDateYYYYMMdd;
	
	
    SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMdd");

	
	private void manageTemperatureUpdates(TemperatureEventDomotics[] aggregated){
		ArrayList<TemperatureEventDomotics> batchInsertTemperature = new ArrayList<TemperatureEventDomotics>();
		double temp;
		for(TemperatureEventDomotics temperature : aggregated){
			TemperatureEventDomotics partialTemperature = domoticsDao.readTemperatureEvent(temperature.getUsername() , temperature.getFrom(),  temperature.getTo(), temperature.getAggregation(), temperature.getLocation());
			if(partialTemperature!=null){
				temp = partialTemperature.getTemperature();
				temp = temp + temperature.getTemperature();
				temp /=2;
				partialTemperature.setTemperature(temp);
				partialTemperature.setUsername(temperature.getUsername());
				domoticsDao.updateTemperature(partialTemperature);
			} else {
				batchInsertTemperature.add(temperature);						
			}
		}
		if(batchInsertTemperature.size()>0){
			domoticsDao.insertTemperatures(batchInsertTemperature);
		}
	}
	
	private void manageHumidityUpdates(HumidityEventDomotics[] aggregated){
		ArrayList<HumidityEventDomotics> batchInsertHumidity = new ArrayList<HumidityEventDomotics>();
		double hmdt;
		for(HumidityEventDomotics humidity : aggregated){
			HumidityEventDomotics partialHumidity = domoticsDao.readHumidityEvent(humidity.getUsername() , humidity.getFrom(),  humidity.getTo(), humidity.getAggregation(), humidity.getLocation());
			if(partialHumidity!=null){
				hmdt = partialHumidity.getHumidity();
				hmdt = hmdt + humidity.getHumidity();
				hmdt /=2;
				partialHumidity.setHumidity(hmdt);
				partialHumidity.setUsername(humidity.getUsername());
				domoticsDao.updateHumidity(partialHumidity);
			} else {
				batchInsertHumidity.add(humidity);						
			}
		}
		if(batchInsertHumidity.size()>0){
			domoticsDao.insertHumidities(batchInsertHumidity);
		}
	}
	
	private void manageIlluminanceUpdates(IlluminanceEventDomotics[] aggregated){
		ArrayList<IlluminanceEventDomotics> batchInsertIlluminance = new ArrayList<IlluminanceEventDomotics>();
		double ilm;
		for(IlluminanceEventDomotics illuminance : aggregated){
			IlluminanceEventDomotics partialIlluminance = domoticsDao.readIlluminanceEvent(illuminance.getUsername() , illuminance.getFrom(),  illuminance.getTo(), illuminance.getAggregation(), illuminance.getLocation());
			if(partialIlluminance!=null){
				ilm = partialIlluminance.getIlluminance();
				ilm = ilm + illuminance.getIlluminance();
				ilm /=2;
				partialIlluminance.setIlluminance(ilm);
				partialIlluminance.setUsername(illuminance.getUsername());
				domoticsDao.updateIlluminance(partialIlluminance);
			} else {
				batchInsertIlluminance.add(illuminance);						
			}
		}
		if(batchInsertIlluminance.size()>0){
			domoticsDao.insertIlluminancies(batchInsertIlluminance);
		}
	}
	/**
	 * Updates domotics in the related collection.
	
	 */

	@Scheduled(initialDelayString="${processing.delay}", fixedRateString="${processing.interval}")
	public void process() {
		log.debug("Starting processing of domotics data");
		long start = System.currentTimeMillis();
		
		
		//Step 1: get primary users
		User[] primaryUsers = profilingServerDao.getPrimaryUsers();
		
		int nrOfUsers = primaryUsers.length;
		long nrOfMeasurements = 0;
		long nrOfAggregatedByHour = 0;
		long nrOfAggregatedByDay = 0;
	
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
		log.info("Service brick domotics-dm: issuing requests for periods of " + maxDaysPerRequest + " days");
		
		//Step 2: cycle on primary users
		for(User primaryUser : primaryUsers){
			try {
				//Step 2a: for each one check last timestamp update

				DomoticsUpdates domoticsUpdates = dataManagerUpdatesDao.getDomoticsReadingByUsername(primaryUser.getUsername());
				try {
					
					if(domoticsUpdates!=null){
						log.debug("Got domotics reading for user " + domoticsUpdates.getUsername() +  " - last update was: " + domoticsUpdates.getLastTemperatureUpdateTimestampLivingroom());
					}
				} catch (Exception e) {
					log.debug("Exception in getting domotics reading for user " + primaryUser.getUsername(), e);
				}
				if(domoticsUpdates==null){
					log.info("Starting date for indexing: " + dt);		
					domoticsUpdates = new DomoticsUpdates(primaryUser.getUsername(), dt);
				}
				String room = "livingroom";
				
				TemperatureMeasurement[] temperatureMeasurements = profilingServerDao.getTemperatureDataByRoom(primaryUser.getUsername(), domoticsUpdates.getLastTemperatureUpdateTimestampLivingroom().toDate(), new Date(),room , maxDaysPerRequest);;
				HumidityMeasurement[] humidityMeasurements = profilingServerDao.getHumidityDataByRoom(primaryUser.getUsername(), domoticsUpdates.getLastHumidityUpdateTimestampLivingroom().toDate(), new Date(),room , maxDaysPerRequest);;
				IlluminanceMeasurement[] illuminanceMeasurements = profilingServerDao.getIlluminanceDataByRoom(primaryUser.getUsername(), domoticsUpdates.getLastIlluminanceUpdateTimestampLivingroom().toDate(), new Date(),room , maxDaysPerRequest);;
				
				
				
				for(int location=0;location<4;location++)
				{
					if(location==0)
					{
						room="livingroom";
						temperatureMeasurements = profilingServerDao.getTemperatureDataByRoom(primaryUser.getUsername(), domoticsUpdates.getLastTemperatureUpdateTimestampLivingroom().toDate(), new Date(),room , maxDaysPerRequest);
						humidityMeasurements = profilingServerDao.getHumidityDataByRoom(primaryUser.getUsername(), domoticsUpdates.getLastHumidityUpdateTimestampLivingroom().toDate(), new Date(),room , maxDaysPerRequest);
						illuminanceMeasurements = profilingServerDao.getIlluminanceDataByRoom(primaryUser.getUsername(), domoticsUpdates.getLastIlluminanceUpdateTimestampLivingroom().toDate(), new Date(),room , maxDaysPerRequest);
					
					}
					
					if(location==1)
					{	
						room="kitchen";
						temperatureMeasurements = profilingServerDao.getTemperatureDataByRoom(primaryUser.getUsername(), domoticsUpdates.getLastTemperatureUpdateTimestampKitchen().toDate(), new Date(),room , maxDaysPerRequest);
						humidityMeasurements = profilingServerDao.getHumidityDataByRoom(primaryUser.getUsername(), domoticsUpdates.getLastHumidityUpdateTimestampKitchen().toDate(), new Date(),room , maxDaysPerRequest);
						illuminanceMeasurements = profilingServerDao.getIlluminanceDataByRoom(primaryUser.getUsername(), domoticsUpdates.getLastIlluminanceUpdateTimestampKitchen().toDate(), new Date(),room , maxDaysPerRequest);
					
					}
					
					if(location==2)
					{	
						room="bedroom";
						temperatureMeasurements = profilingServerDao.getTemperatureDataByRoom(primaryUser.getUsername(), domoticsUpdates.getLastTemperatureUpdateTimestampBedroom().toDate(), new Date(),room , maxDaysPerRequest);
						humidityMeasurements = profilingServerDao.getHumidityDataByRoom(primaryUser.getUsername(), domoticsUpdates.getLastHumidityUpdateTimestampBedroom().toDate(), new Date(),room , maxDaysPerRequest);
						illuminanceMeasurements = profilingServerDao.getIlluminanceDataByRoom(primaryUser.getUsername(), domoticsUpdates.getLastIlluminanceUpdateTimestampBedroom().toDate(), new Date(),room , maxDaysPerRequest);
					
					}	
					
					if(location==3)
					{	
						room="bathroom";
						temperatureMeasurements = profilingServerDao.getTemperatureDataByRoom(primaryUser.getUsername(), domoticsUpdates.getLastTemperatureUpdateTimestampBathroom().toDate(), new Date(),room , maxDaysPerRequest);
						humidityMeasurements = profilingServerDao.getHumidityDataByRoom(primaryUser.getUsername(), domoticsUpdates.getLastHumidityUpdateTimestampBathroom().toDate(), new Date(),room , maxDaysPerRequest);
						illuminanceMeasurements = profilingServerDao.getIlluminanceDataByRoom(primaryUser.getUsername(), domoticsUpdates.getLastIlluminanceUpdateTimestampBathroom().toDate(), new Date(),room , maxDaysPerRequest);
					
					}
			
				
				 
				nrOfMeasurements += temperatureMeasurements.length;
				nrOfMeasurements += humidityMeasurements.length;
				nrOfMeasurements += illuminanceMeasurements.length;
			
				TemperatureEventDomotics[] aggregatedByHourT = domoticsAverage.aggregateByHourT(
						temperatureMeasurements, 
						primaryUser, 
						room,
						domoticsUpdates);
				TemperatureEventDomotics[] aggregatedByDayT = domoticsAverage.aggregateByDayT(aggregatedByHourT, primaryUser, room);
				manageTemperatureUpdates(aggregatedByHourT);
				manageTemperatureUpdates(aggregatedByDayT);
				
				HumidityEventDomotics[] aggregatedByHourH = domoticsAverage.aggregateByHourH(
						humidityMeasurements, 
						primaryUser, 
						room,
						domoticsUpdates);
				HumidityEventDomotics[] aggregatedByDayH = domoticsAverage.aggregateByDayH(aggregatedByHourH, primaryUser, room);
				manageHumidityUpdates(aggregatedByHourH);
				manageHumidityUpdates(aggregatedByDayH);
				
				IlluminanceEventDomotics[] aggregatedByHourI = domoticsAverage.aggregateByHourI(
						illuminanceMeasurements, 
						primaryUser, 
						room,
						domoticsUpdates);			
				IlluminanceEventDomotics[] aggregatedByDayI = domoticsAverage.aggregateByDayI(aggregatedByHourI, primaryUser, room);	
				manageIlluminanceUpdates(aggregatedByHourI);
				manageIlluminanceUpdates(aggregatedByDayI);
			
				nrOfAggregatedByHour += aggregatedByHourT.length;
				nrOfAggregatedByDay += aggregatedByDayT.length;
				nrOfAggregatedByHour += aggregatedByHourH.length;
				nrOfAggregatedByDay += aggregatedByDayH.length;
				nrOfAggregatedByHour += aggregatedByHourI.length;
				nrOfAggregatedByDay += aggregatedByDayI.length;
				
				DateTime timestamp;
				
				if(location==0)
				{
				if(temperatureMeasurements.length > 0 || humidityMeasurements.length > 0 || illuminanceMeasurements.length > 0){
					
					if(temperatureMeasurements.length > 0) {
					timestamp = new DateTime(temperatureMeasurements[temperatureMeasurements.length-1].getTimestamp());
					domoticsUpdates.setLastTemperatureUpdateTimestampLivingroom(timestamp);
					domoticsUpdates.setLastTemperatureLivingroom(temperatureMeasurements[temperatureMeasurements.length-1].getMeasuredValueInCelsius());
					}
					
					if(humidityMeasurements.length > 0) {
						timestamp = new DateTime(humidityMeasurements[humidityMeasurements.length-1].getTimestamp());	
						domoticsUpdates.setLastHumidityUpdateTimestampLivingroom(timestamp);
						domoticsUpdates.setLastHumidityLivingroom(humidityMeasurements[humidityMeasurements.length-1].getMeasuredValueDouble());
						}
					
					if(illuminanceMeasurements.length > 0) {
						timestamp = new DateTime(illuminanceMeasurements[illuminanceMeasurements.length-1].getTimestamp());
						domoticsUpdates.setLastIlluminanceUpdateTimestampLivingroom(timestamp);
						domoticsUpdates.setLastIlluminanceLivingroom(illuminanceMeasurements[illuminanceMeasurements.length-1].getMeasuredValueDouble());
						}
					
					//domoticsUpdates.setLastTemperature(aggregatedByHourT[aggregatedByHourT.length -1].getTemperature());
					//domoticsUpdates.setLastHumidity(aggregatedByHourH[aggregatedByHourH.length -1].getHumidity());
					//domoticsUpdates.setLastIlluminance(aggregatedByHourI[aggregatedByHourI.length -1].getIlluminance());
					
					dataManagerUpdatesDao.updateDomoticsReading(domoticsUpdates);
				}
				}
				
				
				if(location==1)
				{
				if(temperatureMeasurements.length > 0 || humidityMeasurements.length > 0 || illuminanceMeasurements.length > 0){
					if(temperatureMeasurements.length > 0) {
					timestamp = new DateTime(temperatureMeasurements[temperatureMeasurements.length-1].getTimestamp());
					domoticsUpdates.setLastTemperatureUpdateTimestampKitchen(timestamp);
					domoticsUpdates.setLastTemperatureKitchen(temperatureMeasurements[temperatureMeasurements.length-1].getMeasuredValueInCelsius());
					
					}
					
					if(humidityMeasurements.length > 0) {
						timestamp = new DateTime(humidityMeasurements[humidityMeasurements.length-1].getTimestamp());
						domoticsUpdates.setLastHumidityUpdateTimestampKitchen(timestamp);
						domoticsUpdates.setLastHumidityKitchen(humidityMeasurements[humidityMeasurements.length-1].getMeasuredValueDouble());
						
						
						}
					
					if(illuminanceMeasurements.length > 0) {
						timestamp = new DateTime(illuminanceMeasurements[illuminanceMeasurements.length-1].getTimestamp());
						domoticsUpdates.setLastIlluminanceUpdateTimestampKitchen(timestamp);
						domoticsUpdates.setLastIlluminanceKitchen(illuminanceMeasurements[illuminanceMeasurements.length-1].getMeasuredValueDouble());
						}
					
					//domoticsUpdates.setLastTemperature(aggregatedByHourT[aggregatedByHourT.length -1].getTemperature());
					//domoticsUpdates.setLastHumidity(aggregatedByHourH[aggregatedByHourH.length -1].getHumidity());
					//domoticsUpdates.setLastIlluminance(aggregatedByHourI[aggregatedByHourI.length -1].getIlluminance());
					
					dataManagerUpdatesDao.updateDomoticsReading(domoticsUpdates);
				}
				}
				
				if(location==2)
				{
				if(temperatureMeasurements.length > 0 || humidityMeasurements.length > 0 || illuminanceMeasurements.length > 0){
					if(temperatureMeasurements.length > 0) {
					timestamp = new DateTime(temperatureMeasurements[temperatureMeasurements.length-1].getTimestamp());
					domoticsUpdates.setLastTemperatureUpdateTimestampBedroom(timestamp);
					domoticsUpdates.setLastTemperatureBedroom(temperatureMeasurements[temperatureMeasurements.length-1].getMeasuredValueInCelsius());
					}
					
					if(humidityMeasurements.length > 0) {
						timestamp = new DateTime(humidityMeasurements[humidityMeasurements.length-1].getTimestamp());
						domoticsUpdates.setLastHumidityUpdateTimestampBedroom(timestamp);
						domoticsUpdates.setLastHumidityBedroom(humidityMeasurements[humidityMeasurements.length-1].getMeasuredValueDouble());
						}
					
					if(illuminanceMeasurements.length > 0) {
						timestamp = new DateTime(illuminanceMeasurements[illuminanceMeasurements.length-1].getTimestamp());
						domoticsUpdates.setLastIlluminanceUpdateTimestampBedroom(timestamp);
						domoticsUpdates.setLastIlluminanceBedroom(illuminanceMeasurements[illuminanceMeasurements.length-1].getMeasuredValueDouble());
						}
					
										
					dataManagerUpdatesDao.updateDomoticsReading(domoticsUpdates);
				}
				}
				
				if(location==3)
				{
				if(temperatureMeasurements.length > 0 || humidityMeasurements.length > 0 || illuminanceMeasurements.length > 0){
					if(temperatureMeasurements.length > 0) {
					timestamp = new DateTime(temperatureMeasurements[temperatureMeasurements.length-1].getTimestamp());
					domoticsUpdates.setLastTemperatureUpdateTimestampBathroom(timestamp);
					domoticsUpdates.setLastTemperatureBathroom(temperatureMeasurements[temperatureMeasurements.length-1].getMeasuredValueInCelsius());
					}
					
					if(humidityMeasurements.length > 0) {
						timestamp = new DateTime(humidityMeasurements[humidityMeasurements.length-1].getTimestamp());
						domoticsUpdates.setLastHumidityUpdateTimestampBathroom(timestamp);
						domoticsUpdates.setLastHumidityBathroom(humidityMeasurements[humidityMeasurements.length-1].getMeasuredValueDouble());
						}
					
					if(illuminanceMeasurements.length > 0) {
						timestamp = new DateTime(illuminanceMeasurements[illuminanceMeasurements.length-1].getTimestamp());
						domoticsUpdates.setLastIlluminanceUpdateTimestampBathroom(timestamp);
						domoticsUpdates.setLastIlluminanceBathroom(illuminanceMeasurements[illuminanceMeasurements.length-1].getMeasuredValueDouble());
						}
					
								
					dataManagerUpdatesDao.updateDomoticsReading(domoticsUpdates);
				}
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
					log.error("Exception: " + e.getMessage(), e);
				}
			}
			
			
		}
		log.info("Stats for domotics data processor: duration " + ((double)(System.currentTimeMillis()-start)/1000)+ " ms.; managed " + nrOfUsers + " users; " + nrOfMeasurements + " domoticsMeasurements; " + nrOfAggregatedByHour + " aggregatedByHour; " + nrOfAggregatedByDay + " aggregatedByDay; ");
		
			
	}

	
}
