package eu.ewall.servicebrick.dailyfunctioning.dao;

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
import eu.ewall.servicebrick.dailyfunctioning.model.LocationEvent;

/**
 * DAO that reads and writes inactivity data in Mongo DB.
 */
@Component
public class LocationDao extends TimeZoneDaoSupport {
	
	@Autowired
	public LocationDao(MongoOperations mongoOps, TimeZoneContext timeZoneCtx, 
			UserTimeZoneProvider userTimeZoneProvider) {
		super(mongoOps, timeZoneCtx, userTimeZoneProvider);
	}
	
	public List<LocationEvent> readEvents(String username, DateTime from, DateTime to) {
		Query query = query(where("username").is(username).andOperator(Criteria.where("timestamp").gte(from), Criteria.where("timestamp").lt(to))).with(new Sort(Sort.Direction.ASC, "timestamp"));
		List<LocationEvent> events = findWithUserTimeZone(query, LocationEvent.class, username);
		return events;
	}
	
	public LocationEvent readLastEvent(String username) {
		List<LocationEvent> events = readLatestEvents(username, 1);
		if(events.size()>0)
			return events.get(0);
		return null;
	}
	
	public List<LocationEvent> readLatestEvents(String username, int latestEvents) {
		Query query = query(where("username").is(username)).with(new Sort(Sort.Direction.DESC, "timestamp")).limit(latestEvents);
		List<LocationEvent> events = findWithUserTimeZone(query, LocationEvent.class, username);
		return events;
	}
	
	public void insertEvent(LocationEvent event) {
		LocationEvent lastEvent = readLastEvent(event.getUsername());
		if(lastEvent != null && !event.getLocation().equals(lastEvent.getLocation()))
			mongoOps.insert(event);
	}

	public void insertEvents(ArrayList<LocationEvent> locationEvents) {
		if(locationEvents.size()>0){
			LocationEvent lastEvent = readLastEvent(locationEvents.get(0).getUsername());
			if(lastEvent != null && locationEvents.get(0).getLocation().equals(lastEvent.getLocation())){
				locationEvents.remove(0);
			}
			if(locationEvents.size()>0)
				mongoOps.insert(locationEvents, LocationEvent.class);
		}
	}
	
}
