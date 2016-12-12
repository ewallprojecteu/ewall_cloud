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
import eu.ewall.servicebrick.socializingmood.model.MoodTwitter;


/**
 * DAO that reads and writes mood data in Mongo DB.
 */
@Component
public class MoodTwitterDao extends TimeZoneDaoSupport {
	
	@Autowired
	public MoodTwitterDao(MongoOperations mongoOps, TimeZoneContext timeZoneCtx, 
			UserTimeZoneProvider userTimeZoneProvider) {
		super(mongoOps, timeZoneCtx, userTimeZoneProvider);
	}
	
	public MoodTwitter readLastEvent(String username) {
		List<MoodTwitter> events = readLatestEvents(username, 1);
		if(events.size()>0)
			return events.get(0);	
		return null;	 
	}
	
	public List<MoodTwitter> readLatestEvents(String username, int latestEvents) {
		Query query = query(where("username").is(username)).with(new Sort(Sort.Direction.DESC, "timestamp")).limit(latestEvents);
		List<MoodTwitter> events = findWithUserTimeZone(query, MoodTwitter.class, username);
		return events;
	}
	
	
		
	public void insertEvent(MoodTwitter event) {
		log.debug("Inserting new affect: " + event);
		Update AffectWrite = new Update();
		AffectWrite.set("timestamp", event.getTimestamp());
		AffectWrite.set("username", event.getUsername());
		AffectWrite.set("mood", event.getSocializing());
		mongoOps.insert(event);
	}

	public void insertEvents(ArrayList<MoodTwitter> moodEvents) {
		mongoOps.insert(moodEvents, MoodTwitter.class);
	}

	public List<MoodTwitter> readEvents(String username, DateTime from, DateTime to) {
		Query query = query(where("username").is(username).andOperator(Criteria.where("timestamp").gte(from), Criteria.where("timestamp").lt(to)));
		List<MoodTwitter> events = findWithUserTimeZone(query, MoodTwitter.class, username);
		return events;
	}
	
}
