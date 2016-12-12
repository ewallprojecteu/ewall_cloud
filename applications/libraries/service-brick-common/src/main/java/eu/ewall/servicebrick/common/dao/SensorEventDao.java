package eu.ewall.servicebrick.common.dao;

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

import eu.ewall.servicebrick.common.model.SensorEvent;
import eu.ewall.servicebrick.common.time.TimeZoneContext;
import eu.ewall.servicebrick.common.time.UserTimeZoneProvider;

/**
 * DAO that reads and writes sensor data in Mongo DB.
 */
@Component
public class SensorEventDao extends TimeZoneDaoSupport {
	
	@Autowired
	public SensorEventDao(MongoOperations mongoOps, TimeZoneContext timeZoneCtx, 
			UserTimeZoneProvider userTimeZoneProvider) {
		super(mongoOps, timeZoneCtx, userTimeZoneProvider);
	}
	
	public <T extends SensorEvent> List<T> readEvents(Class<T> eventClass, String username,  
			DateTime from, DateTime to) {
		return readEvents(eventClass, username, null, from, to);
	}

	public <T extends SensorEvent> List<T> readEvents(Class<T> eventClass, String username, String location, 
			DateTime from, DateTime to) {
		Criteria criteria = where("username").is(username);
		if (location != null) {
			criteria = criteria.and("location").is(location);
		}
		criteria = criteria.andOperator(Criteria.where("timestamp").gte(from), Criteria.where("timestamp").lt(to));
		Query query = query(criteria).with(new Sort(Sort.Direction.ASC, "timestamp"));
		return findWithUserTimeZone(query, eventClass, username);
	}
	
	public <T extends SensorEvent> List<T> readDomoticsEvents(Class<T> eventClass, String username, String location, 
			DateTime from, DateTime to, String aggregation) {
		Criteria criteria = where("username").is(username);
		if (location != null) {
			criteria = criteria.and("location").is(location);
		}
		criteria = criteria.andOperator(Criteria.where("from").gte(from), Criteria.where("from").lt(to), Criteria.where("aggregation").is(aggregation));
		Query query = query(criteria).with(new Sort(Sort.Direction.ASC, "from"));
		return findWithUserTimeZone(query, eventClass, username);
	}
	
	public <T extends SensorEvent> T readLastEvent(Class<T> eventClass, String username) {
		return readLastEvent(eventClass, username, null);
	}
	
	public <T extends SensorEvent> T readLastEvent(Class<T> eventClass, String username, String location) {
		List<T> events = readLatestEvents(eventClass, username, location, 1);
		if(events.size()>0)
			return events.get(0);	
		return null;	 
	}
	
	public <T extends SensorEvent> List<T> readLatestEvents(Class<T> eventClass, String username, String location,
			int latestEvents) {
		Criteria criteria = where("username").is(username);
		if (location != null) {
			criteria = criteria.and("location").is(location);
		}
		Query query = query(criteria).with(new Sort(Sort.Direction.DESC, "timestamp")).limit(latestEvents);
		List<T> events = findWithUserTimeZone(query, eventClass, username);
		return events;
	}
	
	public <T extends SensorEvent> List<T> readLatestDomoticsEvents(Class<T> eventClass, String username, String location,
			String aggregation, int latestEvents) {
		Criteria criteria = where("username").is(username);
		if (location != null) {
			criteria = criteria.and("location").is(location);
		}
		if (aggregation != null) {
			criteria = criteria.and("aggregation").is(aggregation);
		}
		Query query = query(criteria).with(new Sort(Sort.Direction.DESC, "to")).limit(latestEvents);
		List<T> events = findWithUserTimeZone(query, eventClass, username);
		return events;
	}
	
	public <T extends SensorEvent> void insertEvent(T event) {
		if(event!=null)
			mongoOps.insert(event);
	}

	public <T extends SensorEvent> void insertEvents(ArrayList<T> events) {
		if(events.size()>0){
			mongoOps.insert(events, events.get(0).getClass());
		}
	}
	
}
