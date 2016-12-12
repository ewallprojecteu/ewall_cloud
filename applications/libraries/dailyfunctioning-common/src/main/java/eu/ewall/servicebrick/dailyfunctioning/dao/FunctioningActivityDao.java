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
import eu.ewall.servicebrick.dailyfunctioning.model.FunctioningActivityEvent;

/**
 * DAO that reads and writes inactivity data in Mongo DB.
 */
@Component
public class FunctioningActivityDao extends TimeZoneDaoSupport {
	
	@Autowired
	public FunctioningActivityDao(MongoOperations mongoOps, TimeZoneContext timeZoneCtx, 
			UserTimeZoneProvider userTimeZoneProvider) {
		super(mongoOps, timeZoneCtx, userTimeZoneProvider);
	}
	
	public List<FunctioningActivityEvent> readEvents(String username, DateTime from, DateTime to) {
		Query query = query(where("username").is(username).andOperator(Criteria.where("timestamp").gte(from), Criteria.where("timestamp").lt(to))).with(new Sort(Sort.Direction.ASC, "timestamp"));
		List<FunctioningActivityEvent> events = findWithUserTimeZone(query, FunctioningActivityEvent.class, username);
		return events;
	}
	
	public FunctioningActivityEvent readLastEvent(String username) {
		List<FunctioningActivityEvent> events = readLatestEvents(username, 1);
		if(events.size()>0)
			return events.get(0);	
		return null;	 
	}
	
	public List<FunctioningActivityEvent> readLatestEvents(String username, int latestEvents) {
		Query query = query(where("username").is(username)).with(new Sort(Sort.Direction.DESC, "timestamp")).limit(latestEvents);
		List<FunctioningActivityEvent> events = findWithUserTimeZone(query, FunctioningActivityEvent.class, username);
		return events;
	}
	
	public void insertEvent(FunctioningActivityEvent event) {
		FunctioningActivityEvent lastEvent = readLastEvent(event.getUsername());
		if(lastEvent != null && !event.getFunctioningActivity().equals(lastEvent.getFunctioningActivity()))
			mongoOps.insert(event);
	}

	public void insertEvents(ArrayList<FunctioningActivityEvent> functioningActivityEvents) {
		if(functioningActivityEvents.size()>0){
			FunctioningActivityEvent lastEvent = readLastEvent(functioningActivityEvents.get(0).getUsername());
			if(lastEvent != null && functioningActivityEvents.get(0).getFunctioningActivity().equals(lastEvent.getFunctioningActivity())){
				functioningActivityEvents.remove(0);
			}
			if(functioningActivityEvents.size()>0)
				mongoOps.insert(functioningActivityEvents, FunctioningActivityEvent.class);
		}
	}
	
}
