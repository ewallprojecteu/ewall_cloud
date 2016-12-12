package eu.ewall.platform.reasoner.sleep;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.reasoner.sleep.ProfilingServerDao;

/**
 * Provider of time zones associated to users in their profile. When the time zone for a user is requested for the first
 * time, the provider queries the profiling server to get the user profile and read the time zone. The time zone is then
 * cached for future requests. Cache items have a configurable time to live after which the profiling server is queried
 * again to update the time zone value.
 */
public class UserTimeZoneProvider {
	
	private static final Logger log = LoggerFactory.getLogger(UserTimeZoneProvider.class);
	
	public static final int DEFAULT_CACHE_TTL = 300;
	
	private ProfilingServerDao profilingServerDao;
	private Map<String, TimeZoneCacheItem> timeZoneCache = new HashMap<>();
	private int cacheTtl = DEFAULT_CACHE_TTL;
	
	public UserTimeZoneProvider(ProfilingServerDao profilingServerDao) {
		this.profilingServerDao = profilingServerDao;
	}
	
	/**
	 * Sets the time to live for items in the time zone cache. The default value is 
	 * {@value UserTimeZoneProvider#DEFAULT_CACHE_TTL}}.
	 * 
	 * @param cacheTtl the TTL in seconds; 0 to disable caching
	 */
	public void setCacheTtl(int cacheTtl) {
		if (cacheTtl < 0) {
			throw new IllegalArgumentException("Negative cacheTtl is not supported");
		}
		this.cacheTtl = cacheTtl;
	}
	
	/**
	 * Returns the time zone of the given user. Returns null if the user does not exist. As a temporary compatibility
	 * feature, returns the Europe/Rome time zone for users that don't have a time zone set in their profile. This will
	 * be changed to return null as soon as setting the time zone in the profile becomes standard in eWall.
	 * 
	 * @param username the user to look for
	 * @return the user time zone or null
	 */
	public DateTimeZone getUserTimeZone(String username) {
		if (cacheTtl == 0) {
			return getTimeZoneFromProfile(username);
		}
		DateTimeZone timeZone = getTimeZoneFromCache(username);
		if (timeZone == null) {
			timeZone = getTimeZoneFromProfile(username);
			if (timeZone != null) {
				timeZoneCache.put(username, new TimeZoneCacheItem(System.currentTimeMillis(), timeZone));
			}
		}
		return timeZone;
	}
	
	private DateTimeZone getTimeZoneFromProfile(String username) {
		User user = profilingServerDao.getUser(username);
		if (user == null) {
			return null;
		}
		String zoneId = user.getUserProfile().getvCardSubProfile().getTimezoneid();
		if (zoneId != null) {
			return DateTimeZone.forID(zoneId);
		} else {
			// This is only temporary until all user profiles are updated and will be changed to return null in the
			// future.
			log.warn("Time Zone ID not set in user profile for " + username + ", using Europe/Rome");
			return DateTimeZone.forID("Europe/Rome");
		}
	}
	
	private DateTimeZone getTimeZoneFromCache(String username) {
		TimeZoneCacheItem cacheItem = timeZoneCache.get(username);
		if (cacheItem == null) {
			return null;
		}
		long age = System.currentTimeMillis() - cacheItem.timestamp;
		if (age <= cacheTtl * 1000) {
			return cacheItem.timeZone;
		} else {
			timeZoneCache.remove(username);
			return null;
		}
	}
	
	private static class TimeZoneCacheItem {
		
		public long timestamp;
		public DateTimeZone timeZone;
		
		public TimeZoneCacheItem(long timestamp, DateTimeZone timeZone) {
			this.timestamp = timestamp;
			this.timeZone = timeZone;
		}
		
	}
	
}
