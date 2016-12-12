package eu.ewall.servicebrick.domotics.dao;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;


import eu.ewall.servicebrick.common.dao.TimeZoneDaoSupport;
import eu.ewall.servicebrick.common.time.TimeZoneContext;
import eu.ewall.servicebrick.common.time.UserTimeZoneProvider;
import eu.ewall.servicebrick.domotics.model.DomoticsEvent;
import eu.ewall.servicebrick.domotics.model.TemperatureEventDomotics;
import eu.ewall.servicebrick.domotics.model.HumidityEventDomotics;
import eu.ewall.servicebrick.domotics.model.IlluminanceEventDomotics;
import eu.ewall.servicebrick.domotics.processor.DomoticsStatus;




@Component
public class DomoticsDao extends TimeZoneDaoSupport {
	
	@Autowired
	public DomoticsDao(MongoOperations mongoOps, TimeZoneContext timeZoneCtx,
			UserTimeZoneProvider userTimeZoneProvider) {
		super(mongoOps, timeZoneCtx, userTimeZoneProvider);
	}
	
	public <T> List<T> readEvents(String username, DateTime from, 
			DateTime to, String aggregation, String location,Class<T> clazz) {
		Query query = query(where("username").is(username).and("from").gte(from).and("to").lte(to)
				.and("aggregation").is(aggregation).and("location").is(location)).with(new Sort(Sort.Direction.ASC, "from"));
		List<T> events = findWithUserTimeZone(query, clazz, username);
		return events;
	}
	
	public List<DomoticsEvent> readLastEvent(String username) {
		// get the DomoticsEvent list by grouping sensors info into similar room
		DomoticsStatus ds = new DomoticsStatus();
		List<DomoticsEvent> events = ds.processDomotics(username);
		if(events.size() > 0)
			return events;
		return null;
	}
	
	public List<DomoticsEvent> readLatestEvents(String username, int latestEvents){
		Query query = query(where("username").is(username)).with(new Sort(Sort.Direction.DESC, "timestamp")).limit(latestEvents);
		List<DomoticsEvent> events = findWithUserTimeZone(query, DomoticsEvent.class, username);
		return events;
	}
	
	public TemperatureEventDomotics readTemperatureEvent(String username, DateTime from, DateTime to, String aggregation, String location) {
		List<TemperatureEventDomotics> temperature = readEvents(username, from, to, aggregation, location, TemperatureEventDomotics.class);
		if (temperature.size() == 0) {
			return null;
		} else if (temperature.size() == 1) {
			return temperature.get(0);
		} else {
			log.error("Multiple Temperature records found for user = " + username + ", from = " + from + ", to = " + to + ", aggregation = " + aggregation);
			throw new RuntimeException("Multiple records found");
		}
	}
	public void insertTemperatures(ArrayList<TemperatureEventDomotics> temperature) {
		log.debug("Inserting " + temperature.size() + " new temperatures");
		mongoOps.insert(temperature, TemperatureEventDomotics.class);
	}
	
	public void insertTemperature(TemperatureEventDomotics temperature) {
		log.debug("Inserting new Temperature " + temperature);
		mongoOps.insert(temperature);
	}
	
	public void updateTemperature (TemperatureEventDomotics temperature) {
		log.debug("Updating Temperature: " + temperature);
		mongoOps.save(temperature);
	}
	
	public HumidityEventDomotics readHumidityEvent(String username, DateTime from, DateTime to, String aggregation, String location) {
		List<HumidityEventDomotics> humidity = readEvents(username, from, to, aggregation, location, HumidityEventDomotics.class);
		if (humidity.size() == 0) {
			return null;
		} else if (humidity.size() == 1) {
			return humidity.get(0);
		} else {
			log.error("Multiple Humidity records found for user = " + username + ", from = " + from + ", to = " + to + ", aggregation = " + aggregation);
			throw new RuntimeException("Multiple records found");
		}
	}
	public void insertHumidities(ArrayList<HumidityEventDomotics> humidity) {
		log.debug("Inserting " + humidity.size() + " new humidities");
		mongoOps.insert(humidity, HumidityEventDomotics.class);
	}
	
	public void insertHumidity(HumidityEventDomotics humidity) {
		log.debug("Inserting new Humidity " + humidity);
		mongoOps.insert(humidity);
	}
	
	public void updateHumidity (HumidityEventDomotics humidity) {
		log.debug("Updating Humidity: " + humidity);
		mongoOps.save(humidity);
	}
	
	public IlluminanceEventDomotics readIlluminanceEvent(String username, DateTime from, DateTime to, String aggregation, String location) {
		List<IlluminanceEventDomotics> illuminance = readEvents(username, from, to, aggregation, location, IlluminanceEventDomotics.class);
		if (illuminance.size() == 0) {
			return null;
		} else if (illuminance.size() == 1) {
			return illuminance.get(0);
		} else {
			log.error("Multiple Illuminance record found for user = " + username + ", from = " + from + ", to = " + to + ", aggregation = " + aggregation);
			throw new RuntimeException("Multiple records found");
		}
	}
	public void insertIlluminancies(ArrayList<IlluminanceEventDomotics> illuminance) {
		log.debug("Inserting " + illuminance.size() + " new illuminancies");
		mongoOps.insert(illuminance, IlluminanceEventDomotics.class);
	}
	
	public void insertIlluminance(IlluminanceEventDomotics illuminance) {
		log.debug("Inserting new Illuminance " + illuminance);
		mongoOps.insert(illuminance);
	}
	
	public void updateIlluminance (IlluminanceEventDomotics illuminance) {
		log.debug("Updating Illuminance: " + illuminance);
		mongoOps.save(illuminance);
	}
//	public void insertEvent(DomoticsEvent event) {
//		DomoticsEvent lastEvent = readLastEvent(event.getUsername());
//		if(lastEvent != null && !event.getDomotics().equals(lastEvent.getDomotics()))
//			mongoOps.insert(event);
//	}
//	
//	public void insertEvents(ArrayList<DomoticsEvent> functioningActivityEvents) {
//		if(functioningActivityEvents.size()>0){
//			DomoticsEvent lastEvent = readLastEvent(functioningActivityEvents.get(0).getUsername());
//			if(lastEvent != null && functioningActivityEvents.get(0).getDomotics().equals(lastEvent.getDomotics())){
//				functioningActivityEvents.remove(0);
//			}
//			if(functioningActivityEvents.size()>0)
//				mongoOps.insert(functioningActivityEvents, DomoticsEvent.class);
//		}
//	}
}
