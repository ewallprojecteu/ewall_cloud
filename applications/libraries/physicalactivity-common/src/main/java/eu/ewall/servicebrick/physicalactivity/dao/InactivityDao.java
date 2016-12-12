package eu.ewall.servicebrick.physicalactivity.dao;

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
import eu.ewall.servicebrick.physicalactivity.model.InactivityEvent;

/**
 * DAO that reads and writes inactivity data in Mongo DB.
 */
@Component
public class InactivityDao extends TimeZoneDaoSupport {
	
	@Autowired
	public InactivityDao(MongoOperations mongoOps, TimeZoneContext timeZoneCtx, 
			UserTimeZoneProvider userTimeZoneProvider) {
		super(mongoOps, timeZoneCtx, userTimeZoneProvider);
	}
	
	public List<InactivityEvent> readEvents(String username, DateTime from, DateTime to) {
		Query query = query(where("username").is(username).andOperator(Criteria.where("timestamp").gte(from), Criteria.where("timestamp").lt(to))).with(new Sort(Sort.Direction.ASC, "timestamp"));
		List<InactivityEvent> events = findWithUserTimeZone(query, InactivityEvent.class, username);
		return events;
	}
	
	public InactivityEvent readLastEvent(String username) {
		List<InactivityEvent> events = readLatestEvents(username, 1);
		if(events.size()>0)
			return events.get(0);	
		return null;	 
	}
	
	public List<InactivityEvent> readLatestEvents(String username, int latestEvents) {
		Query query = query(where("username").is(username)).with(new Sort(Sort.Direction.DESC, "timestamp")).limit(latestEvents);
		List<InactivityEvent> events = findWithUserTimeZone(query, InactivityEvent.class, username);
		return events;
	}
	
	public void insertEvent(InactivityEvent event) {
		InactivityEvent lastEvent = readLastEvent(event.getUsername());
		if(lastEvent != null && !event.isInactive()==(lastEvent.isInactive()))
			mongoOps.insert(event);
	}

	public void insertEvents(ArrayList<InactivityEvent> inactivityEvents) {
		if(inactivityEvents.size()>0){
			InactivityEvent lastEvent = readLastEvent(inactivityEvents.get(0).getUsername());
			if(lastEvent != null && inactivityEvents.get(0).isInactive()==(lastEvent.isInactive())){
				inactivityEvents.remove(0);
			}
			if(inactivityEvents.size()>0)
				mongoOps.insert(inactivityEvents, InactivityEvent.class);
		}
	}
	
}
