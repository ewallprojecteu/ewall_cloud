package eu.ewall.platform.reasoner.sleep;

import java.util.List;

import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import eu.ewall.platform.reasoner.sleep.TimeZoneContext;
import eu.ewall.platform.reasoner.sleep.UserTimeZoneProvider;

/**
 * Base class for DAOs that need to work with MongoDB using an explicit time zone instead of the default one.
 * For example normally a query on MongoDB will return dates in the default time zone, while using this DAO it is
 * possible to get dates in a given time zone. 
 * 
 * @see TimeZoneContext
 * @see UserTimeZoneProvider
 *
 */
public abstract class TimeZoneDaoSupport {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	protected MongoOperations mongoOps;
	private TimeZoneContext timeZoneCtx;
	private UserTimeZoneProvider userTimeZoneProvider;
	
	public TimeZoneDaoSupport(MongoOperations mongoOps, TimeZoneContext timeZoneCtx, 
			UserTimeZoneProvider userTimeZoneProvider) {
		this.mongoOps = mongoOps;
		this.timeZoneCtx = timeZoneCtx;
		this.userTimeZoneProvider = userTimeZoneProvider;
	}
	
	/**
	 * Executes the given query using the given time zone for creating any output date objects.
	 * 
	 * @param query the query to execute
	 * @param clazz the class of result items
	 * @param timeZone the time zone to use for dates
	 * @return the list with results
	 */
	protected <T> List<T> findWithTimeZone(Query query, Class<T> clazz, DateTimeZone timeZone) {
		log.debug("Searching for " + clazz + " with query " + query + " in time zone " + timeZone);
		if (timeZone == null) {
			return mongoOps.find(query, clazz);
		} else {
			DateTimeZone previousZone = timeZoneCtx.getTimeZone();
			timeZoneCtx.setTimeZone(timeZone);
			try {
				return mongoOps.find(query, clazz);
			} finally {
				timeZoneCtx.setTimeZone(previousZone);
			}
		}
	}
	
	/**
	 * Executes the given query using the time zone associated to the given user for creating any output date objects.
	 * The time zone is resolved using the UserTimeZoneProvider.
	 * 
	 * @param query the query to execute
	 * @param clazz the class of result items
	 * @param username the username for resolving the time zone
	 * @return the list with results
	 */
	protected <T> List<T> findWithUserTimeZone(Query query, Class<T> clazz, String username) {
		DateTimeZone timeZone = userTimeZoneProvider.getUserTimeZone(username);
		log.debug("Resolved time zone for user " + username + ": " + timeZone);
		if (timeZone == null) {
			throw new RuntimeException("Undefined time zone for user " + username);
		}
		return findWithTimeZone(query, clazz, timeZone);
	}
	
}
