package eu.ewall.servicebrick.physicalactivity.dao;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

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
import eu.ewall.servicebrick.physicalactivity.model.PhysicalActivityEvent;

/**
 * DAO that reads and writes physical activity data in Mongo DB.
 */
@Component
public class PhysicalActivityDao extends TimeZoneDaoSupport {
	
	@Autowired
	public PhysicalActivityDao(MongoOperations mongoOps, TimeZoneContext timeZoneCtx, 
			UserTimeZoneProvider userTimeZoneProvider) {
		super(mongoOps, timeZoneCtx, userTimeZoneProvider);
	}
	
	public List<PhysicalActivityEvent> readEvents(String username, DateTime from, DateTime to) {
		Query query = query(where("username").is(username).andOperator(Criteria.where("timestamp").gte(from), Criteria.where("timestamp").lt(to))).with(new Sort(Sort.Direction.ASC, "timestamp"));
		List<PhysicalActivityEvent> events = findWithUserTimeZone(query, PhysicalActivityEvent.class, username);
		return events;	 
	}
	
	public PhysicalActivityEvent readLastEvent(String username) {
		List<PhysicalActivityEvent> events = readLatestEvents(username, 1);
		if(events.size()>0)
			return events.get(0);	
		return null;	 
	}
	
	public List<PhysicalActivityEvent> readLatestEvents(String username, int latestEvents) {
		Query query = query(where("username").is(username)).with(new Sort(Sort.Direction.DESC, "timestamp")).limit(latestEvents);
		List<PhysicalActivityEvent> events = findWithUserTimeZone(query, PhysicalActivityEvent.class, username);
		return events;
	}
	
	public void insertEvent(PhysicalActivityEvent event) {
		PhysicalActivityEvent lastEvent = readLastEvent(event.getUsername());
		if(lastEvent != null && !event.getActivityType().equals(lastEvent.getActivityType()))
			mongoOps.insert(event);
	}
	
	public void insertEvents(List<PhysicalActivityEvent> events) {
		if(events.size()>0){
			PhysicalActivityEvent lastEvent = readLastEvent(events.get(0).getUsername());
			if(lastEvent != null && events.get(0).getActivityType().equals(lastEvent.getActivityType())){
				events.remove(0);
			}
			if(events.size()>0)
				mongoOps.insert(events, PhysicalActivityEvent.class);
		}
	}
	
}
