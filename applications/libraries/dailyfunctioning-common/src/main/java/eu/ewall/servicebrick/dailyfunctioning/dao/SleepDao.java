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
import eu.ewall.servicebrick.dailyfunctioning.model.SleepHistory;

@Component
public class SleepDao extends TimeZoneDaoSupport {

	@Autowired
	public SleepDao(MongoOperations mongoOps, TimeZoneContext timeZoneCtx, UserTimeZoneProvider userTimeZoneProvider) {
		super(mongoOps, timeZoneCtx, userTimeZoneProvider);
	}

	public List<SleepHistory> readHistory(String username, DateTime from, DateTime to) {
		Query query = query(where("username").is(username).andOperator(Criteria.where("timestamp").gte(from),
				Criteria.where("timestamp").lt(to))).with(new Sort(Sort.Direction.ASC, "timestamp"));
		List<SleepHistory> sleepHist = findWithUserTimeZone(query, SleepHistory.class, username);
		return sleepHist;
	}

	public SleepHistory readLastSleepHistory(String username) {
		List<SleepHistory> sleepHist = readLatestSleepHistory(username, 1);
		if (sleepHist.size() > 0)
			return sleepHist.get(0);
		return null;
	}

	public List<SleepHistory> readLatestSleepHistory(String username, int latestEvents) {
		Query query = query(where("username").is(username)).with(new Sort(Sort.Direction.DESC, "timestamp"))
				.limit(latestEvents);
		List<SleepHistory> events = findWithUserTimeZone(query, SleepHistory.class, username);
		return events;
	}

	public void insertSleepHistory(SleepHistory event) {
		//SleepHistory lastEvent = readLastSleepHistory(event.getUsername());
		//if (lastEvent != null && event.getState()!=lastEvent.getState())
			mongoOps.insert(event);
	}

	public void insertEvents(ArrayList<SleepHistory> sleepHistoryEvents) {
		if (sleepHistoryEvents.size() > 0) {
			SleepHistory lastEvent = readLastSleepHistory(sleepHistoryEvents.get(0).getUsername());
			if (lastEvent != null && sleepHistoryEvents.get(0).getState()==lastEvent.getState()) {
				sleepHistoryEvents.remove(0);
			}
			if (sleepHistoryEvents.size() > 0)
				mongoOps.insert(sleepHistoryEvents, SleepHistory.class);
		}
	}

}