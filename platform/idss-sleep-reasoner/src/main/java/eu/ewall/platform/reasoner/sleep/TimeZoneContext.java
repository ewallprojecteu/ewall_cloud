package eu.ewall.platform.reasoner.sleep;

import org.joda.time.DateTimeZone;

import eu.ewall.platform.reasoner.sleep.DateTimeReadConverter;

/**
 * Holds time zone information for the current thread using thread-local variables. This allows using a different
 * time zone for each request, e.g. depending on the user profile. Supported in classes such as DateTimeReadConverter.
 * 
 * @see DateTimeReadConverter
 *
 */
public class TimeZoneContext {

	private static final ThreadLocal<DateTimeZone> threadZone = new ThreadLocal<DateTimeZone>();
	
	/**
	 * Returns the time zone set for the current thread.
	 * 
	 * @return the time zone for the current thread or null if not set
	 */
	public DateTimeZone getTimeZone() {
		return threadZone.get();
	}
	
	/**
	 * Sets the time zone for the current thread.
	 * 
	 * @param timeZone the time zone to set
	 */
	public void setTimeZone(DateTimeZone timeZone) {
		threadZone.set(timeZone);
	}
	
}
