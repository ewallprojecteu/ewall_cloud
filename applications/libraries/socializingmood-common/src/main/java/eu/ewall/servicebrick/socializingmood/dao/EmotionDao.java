package eu.ewall.servicebrick.socializingmood.dao;

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
import eu.ewall.servicebrick.socializingmood.model.VisualSensingEvent;

/**
 * DAO that reads and writes mood data in Mongo DB.
 */
@Component
public class EmotionDao extends TimeZoneDaoSupport {
	
	@Autowired
	public EmotionDao(MongoOperations mongoOps, TimeZoneContext timeZoneCtx, 
			UserTimeZoneProvider userTimeZoneProvider) {
		super(mongoOps, timeZoneCtx, userTimeZoneProvider);
	}
	
	public List<VisualSensingEvent> readEvents(String username, DateTime from, DateTime to) {
		Query query = query(where("username").is(username).andOperator(Criteria.where("timestamp").gte(from), Criteria.where("timestamp").lt(to)));
		List<VisualSensingEvent> events = findWithUserTimeZone(query, VisualSensingEvent.class, username);
		return events;
	}
	
	public VisualSensingEvent readLastEvent(String username) {
		List<VisualSensingEvent> events = readLatestEvents(username, 1);
		if(events.size()>0)
			return events.get(0);	
		return null;	 
	}
	
	public List<VisualSensingEvent> readLatestEvents(String username, int latestEvents) {
		Query query = query(where("username").is(username)).with(new Sort(Sort.Direction.DESC, "timestamp")).limit(latestEvents);
		List<VisualSensingEvent> events = findWithUserTimeZone(query, VisualSensingEvent.class, username);
		return events;
	}
	
	public void insertEvent(VisualSensingEvent event) {
		log.debug("Inserting new VisualSensingEvent: " + event);
		mongoOps.insert(event);
	}

	public void insertEvents(ArrayList<VisualSensingEvent> visualSensingEvents) {
		mongoOps.insert(visualSensingEvents, VisualSensingEvent.class);
	}
	
}
