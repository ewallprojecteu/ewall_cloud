/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.servicebrick.calendar.validation;

import eu.ewall.platform.commons.datamodel.calendar.Event;

/**
 * The Class EventInputValidator.
 */
public class EventInputValidator {

	/**
	 * Validate latest events.
	 *
	 * @param latestEvents
	 *            the latest events
	 */
	public void validateLatestEvents(Integer latestEvents) {
		if (latestEvents != null) {
			if (latestEvents.intValue() < 0) {
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

	/**
	 * Validate event.
	 *
	 * @param event the event
	 */
	public void validateEvent(Event event) {
		if (event == null)
			throw new IllegalArgumentException("Invalid event: empty");

		if (event.getRepeatTimes() == null)
			throw new IllegalArgumentException(
					"Invalid event: repeat times is not set");

		if (event.getRepeatTimes().intValue() < 1
				|| event.getRepeatTimes().intValue() > 365)
			throw new IllegalArgumentException(
					"Invalid event: repeat times must be between 1 and 365 (days)");

		if (event.getRepeatInterval() == null)
			throw new IllegalArgumentException(
					"Invalid event: repeat interval is not set");
		
		if (event.getReminderTime() == null)
			throw new IllegalArgumentException(
					"Invalid event: reminder time is not set");

		if (event.getReminderTime().intValue() < 10
				|| event.getReminderTime().intValue() > 1440)
			throw new IllegalArgumentException(
					"Invalid event: reminder time must be between 10 and 1440 (minutes)");

		if (event.getUsername() == null || event.getUsername().isEmpty()) {
			throw new IllegalArgumentException(
					"Invalid event: username is null or empty");
		}
	}

	/**
	 * Validate event id.
	 *
	 * @param event the event
	 * @param id the id
	 */
	public void validateEventId(Event event, String id) {
		if (event == null || id == null)
			throw new IllegalArgumentException(
					"Invalid event id: empty event or id");

		if (event.getId() == null || event.getId().isEmpty())
			throw new IllegalArgumentException("Invalid event id: empty");

		if (!id.equals(event.getId()))
			throw new IllegalArgumentException(
					"Event id does not match update id paramater");

	}

}
