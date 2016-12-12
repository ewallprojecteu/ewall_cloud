package eu.ewall.servicebrick.dailyfunctioning.processor;

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
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import eu.ewall.servicebrick.common.dao.TimeZoneDaoSupport;
import eu.ewall.servicebrick.common.time.TimeZoneContext;
import eu.ewall.servicebrick.common.time.UserTimeZoneProvider;

@Component
public class RoomsDao extends TimeZoneDaoSupport {

	@Autowired
	public RoomsDao(MongoOperations mongoOps, TimeZoneContext timeZoneCtx, 
			UserTimeZoneProvider userTimeZoneProvider) {
		super(mongoOps, timeZoneCtx, userTimeZoneProvider);
	}
	
	public List<Rooms> readEvents(String username, DateTime from, DateTime to) {
		Query query = query(where("username").is(username).andOperator(Criteria.where("timestamp").gte(from), Criteria.where("timestamp").lte(to)));
		List<Rooms> events = findWithUserTimeZone(query, Rooms.class, username);
		return events;
	}
	
	public Rooms readLastEvent(String username) {
		List<Rooms> events = readLatestEvents(username, 1);
		if(events.size()>0)
			return events.get(0);	
		return null;	 
	}
	
	public List<Rooms> readLatestEvents(String username, int latestEvents) {
		Query query = query(where("username").is(username)).with(new Sort(Sort.Direction.DESC, "timestamp")).limit(latestEvents);
		List<Rooms> events = findWithUserTimeZone(query, Rooms.class, username);
		return events;
	}
		
	public void insertEvent(Rooms event) {
		log.debug("Inserting new affect: " + event);
		Update AffectWrite = new Update();
		AffectWrite.set("timestamp", event.getTimestamp().toDate());
		AffectWrite.set("username", event.getUsername());
		AffectWrite.set("roomEvidence", event.getRoomEvidence());
		AffectWrite.set("roomWeights", event.getRoomWeights());
		AffectWrite.set("activityWeights", event.getActivityWeights());
		AffectWrite.set("pirAvg", event.getPirAvg());
		AffectWrite.set("tempAvg", event.getTempAvg());
		AffectWrite.set("tempStd", event.getTempStd());
		AffectWrite.set("tempSq", event.getTempSq());
		AffectWrite.set("humidityAvg", event.getHumidityAvg());
		AffectWrite.set("humidityStd", event.getHumidityStd());
		AffectWrite.set("humiditySq", event.getHumiditySq());
		AffectWrite.set("tempLargeVariation", event.getTempLargeVariation());
		AffectWrite.set("humidityLargeVariation", event.getHumidityLargeVariation());
		AffectWrite.set("frontalDoorState", event.getFrontalDoorState());
		AffectWrite.set("reasonedRoom", event.getReasonedRoom());
		AffectWrite.set("reasonedActivity", event.getReasonedActivity());
		mongoOps.upsert(query(where("username").is(event.getUsername())),AffectWrite,Rooms.class);
	}

	public void insertEvents(ArrayList<Rooms> moodEvents) {
		mongoOps.insert(moodEvents, Rooms.class);
	}
}
