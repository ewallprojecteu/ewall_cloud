/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.profilingserver.validation;

/**
 * The Class ProfilingServerInputValidator.
 * 
 * @author EMIRMOS
 */
public class ProfilingServerInputValidator {

	/**
	 * Validate latest events.
	 *
	 * @param latestEvents
	 *            the latest events
	 */
	public void validateLatestEvents(Integer latestEvents) {
		if (latestEvents != null) {
			if (latestEvents < 0) {
				throw new IllegalArgumentException(
						"Invalid number of events: must be >= 0");
			}
		}
	}

	/**
	 * Validate timestamps.
	 *
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 */
	public void validateTimestamps(Long from, Long to) {
		if (from != null && to != null) {
			if (to < from) {
				throw new IllegalArgumentException(
						"Invalid time interval: to timestamp must be greater than from timestamp");
			} 
		}
	}

}
