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
import eu.ewall.servicebrick.dailyfunctioning.model.InbedEvent;

/**
 * DAO that reads and writes inactivity data in Mongo DB.
 */
@Component
public class InbedDao extends TimeZoneDaoSupport {
	
	@Autowired
	public InbedDao(MongoOperations mongoOps, TimeZoneContext timeZoneCtx, 
			UserTimeZoneProvider userTimeZoneProvider) {
		super(mongoOps, timeZoneCtx, userTimeZoneProvider);
	}
	
	public List<InbedEvent> readEvents(String username, DateTime from, DateTime to) {
		Query query = query(where("username").is(username).andOperator(Criteria.where("timestamp").gte(from), Criteria.where("timestamp").lt(to))).with(new Sort(Sort.Direction.ASC, "timestamp"));
		List<InbedEvent> events = findWithUserTimeZone(query, InbedEvent.class, username);
		return events;	 
	}
	
	public InbedEvent readLastEvent(String username) {
		List<InbedEvent> events = readLatestEvents(username, 1);
		if(events.size()>0)
			return events.get(0);	
		return null;	 
	}
	
	public List<InbedEvent> readLatestEvents(String username, int latestEvents) {
		Query query = query(where("username").is(username)).with(new Sort(Sort.Direction.DESC, "timestamp")).limit(latestEvents);
		List<InbedEvent> events = findWithUserTimeZone(query, InbedEvent.class, username);
		return events;
	}
	
	public void insertEvent(InbedEvent event) {
		InbedEvent lastEvent = readLastEvent(event.getUsername());
		if(lastEvent != null && !event.isInbed()==(lastEvent.isInbed()))
			mongoOps.insert(event);
	}

	public void insertEvents(ArrayList<InbedEvent> inbedEvents) {
		if(inbedEvents.size()>0){
			InbedEvent lastEvent = readLastEvent(inbedEvents.get(0).getUsername());
			if(lastEvent != null && inbedEvents.get(0).isInbed()==(lastEvent.isInbed())){
				inbedEvents.remove(0);
			}
			if(inbedEvents.size()>0)
				mongoOps.insert(inbedEvents, InbedEvent.class);
		}		
	}
	
}
