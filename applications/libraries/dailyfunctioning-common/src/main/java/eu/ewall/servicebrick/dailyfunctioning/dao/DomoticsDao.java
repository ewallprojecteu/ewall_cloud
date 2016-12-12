package eu.ewall.servicebrick.dailyfunctioning.dao;

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
import eu.ewall.servicebrick.dailyfunctioning.model.DomoticsEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.DomoticsStatus;

@Component
public class DomoticsDao extends TimeZoneDaoSupport {
	
	@Autowired
	public DomoticsDao(MongoOperations mongoOps, TimeZoneContext timeZoneCtx,
			UserTimeZoneProvider userTimeZoneProvider) {
		super(mongoOps, timeZoneCtx, userTimeZoneProvider);
	}
	
	public List<DomoticsEvent> readEvents(String username, DateTime from, 
			DateTime to) {
		Query query = query(where("username").is(username).andOperator(Criteria.where("timestamp").gte(from), Criteria.where("timestamp").lt(to))).with(new Sort(Sort.Direction.ASC, "timestamp"));
		List<DomoticsEvent> events = findWithUserTimeZone(query, DomoticsEvent.class, username);
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
