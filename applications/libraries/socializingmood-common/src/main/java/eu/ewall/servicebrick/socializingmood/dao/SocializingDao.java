package eu.ewall.servicebrick.socializingmood.dao;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import eu.ewall.servicebrick.common.dao.TimeZoneDaoSupport;
import eu.ewall.servicebrick.common.time.TimeZoneContext;
import eu.ewall.servicebrick.common.time.UserTimeZoneProvider;
import eu.ewall.servicebrick.socializingmood.model.Socializing;
import eu.ewall.servicebrick.common.model.SocializingUpdate;


/**
 * DAO that reads and writes mood data in Mongo DB.
 */
@Component
public class SocializingDao extends TimeZoneDaoSupport {
	
	@Autowired
	public SocializingDao(MongoOperations mongoOps, TimeZoneContext timeZoneCtx, 
			UserTimeZoneProvider userTimeZoneProvider) {
		super(mongoOps, timeZoneCtx, userTimeZoneProvider);
	}
	
	public List<Socializing> readEvents(String username, DateTime from, DateTime to) {
		Query query = query(where("username").is(username).andOperator(Criteria.where("timestamp").gte(from), Criteria.where("timestamp").lt(to)));
		List<Socializing> events = findWithUserTimeZone(query, Socializing.class, username);
		return events;
	}
	
	public Socializing readLastEvent(String username) {
		List<Socializing> events = readLatestEvents(username, 1);
		if(events.size()>0)
			return events.get(0);	
		return null;	 
	}
	
	public List<Socializing> readLatestEvents(String username, int latestEvents) {
		Query query = query(where("username").is(username)).with(new Sort(Sort.Direction.DESC, "timestamp")).limit(latestEvents);
		List<Socializing> events = findWithUserTimeZone(query, Socializing.class, username);
		return events;
	}
		
	public void insertEvent(SocializingUpdate event) {
		log.debug("Inserting new Socializing: " + event);
		Update SocializingWrite = new Update();
		SocializingWrite.set("timestamp", event.getTimestamp().toDate());
		SocializingWrite.set("username", event.getUsername());
		SocializingWrite.set("socializing", event.getSocializingEvent());
		mongoOps.insert(event);
	}

	public void insertEvents(ArrayList<Socializing> socializingEvents) {
		mongoOps.insert(socializingEvents, Socializing.class);
	}
	
}
