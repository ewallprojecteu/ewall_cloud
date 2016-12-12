package eu.ewall.platform.reasoner.sleep;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import eu.ewall.platform.reasoner.sleep.UserTimeZoneProvider;

/**
 * Provides validation methods for commonly used service brick input parameter combinations.
 */
public class ServiceBrickInputValidator {

	private UserTimeZoneProvider userTimeZoneProvider;
	
	public ServiceBrickInputValidator(UserTimeZoneProvider userTimeZoneProvider) {
		this.userTimeZoneProvider = userTimeZoneProvider;
	}
	
	/**
	 * Checks that parameters for a query on a given user in a given time interval are valid.
	 * Requires that the time interval is given in the same time zone stored in the user profile for consistency
	 * (e.g. when reading aggregations from the DB).
	 * 
	 * @param username the username of the user
	 * @param from the start of the time interval
	 * @param to the end of the time interval
	 * @throws IllegalArgumentException if any parameter is invalid
	 */
	public void validateTimeInterval(String username, DateTime from, DateTime to) {
		if (username == null || username.length() == 0) {
			throw new IllegalArgumentException("Invalid username: empty");
		} else if (!from.isBefore(to)) {
			throw new IllegalArgumentException("Invalid time interval: end date must be later than start date");
		}
		DateTimeZone userZone = userTimeZoneProvider.getUserTimeZone(username);
		int originalFromOffset = from.getZone().getOffset(from);
		int userFromOffset = userZone.getOffset(from);
		int originalToOffset = to.getZone().getOffset(to);
		int userToOffset = userZone.getOffset(to);
		if (originalFromOffset != userFromOffset) {
			throw new IllegalArgumentException("Invalid time interval: 'from' time zone does not match user profile");
		} else if (originalToOffset != userToOffset) {
			throw new IllegalArgumentException("Invalid time interval: 'to' time zone does not match user profile");
		}
	}
	
	/**
	 * Checks that parameters for a query on a given user for the latest events are valid.
	 * 
	 * @param username the username of the user
	 * @param latestEvents the number of latest events to retrieve
	 */
	public void validateLatestEvents(String username, int latestEvents) {
		if (username == null || username.length() == 0) {
			throw new IllegalArgumentException("Invalid username: empty");
		} else if (latestEvents < 0) {
			throw new IllegalArgumentException("Invalid number of events: must be >= 0");
		}
	}
	
}
