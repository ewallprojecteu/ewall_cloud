/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.activity;

import eu.ewall.platform.commons.datamodel.core.ieeesumo.Entity;
import eu.ewall.platform.commons.datamodel.measure.TimeInterval;

/**
 * The Class Activity.
 *
 * @author eandgrg, emirmos
 */
public abstract class Activity extends Entity {
	/** The time interval. */
	protected TimeInterval timeInterval;

	/**  The type of activity. */
	protected ActivityType activityType;

	// /** The location. */
	// TODO For now, we will not use activity location (only implicitly by
	// sensing environment)
	// protected Location location;

	/**
	 * The Constructor.
	 */
	public Activity() {
		this.activityType = ActivityType.UNKNOWN;

	}

	/**
	 * Instantiates a new activity.
	 *
	 * @param timeInterval the time interval
	 * @param activityType the activity type
	 */
	public Activity(TimeInterval timeInterval, ActivityType activityType) {
		super();
		this.timeInterval = timeInterval;
		this.activityType = activityType;
	}

	/**
	 * Instantiates a new activity.
	 *
	 * @param startTime the start time
	 * @param endTime the end time
	 * @param activityType the activity type
	 */
	public Activity(long startTime, long endTime, ActivityType activityType) {
		super();
		this.timeInterval = new TimeInterval(startTime, endTime);
		this.activityType = activityType;
	}

	/**
	 * Gets the time interval.
	 *
	 * @return the timeInterval
	 */
	public TimeInterval getTimeInterval() {
		return timeInterval;
	}

	/**
	 * Sets the time interval.
	 *
	 * @param timeInterval
	 *            the timeInterval to set
	 */
	public void setTimeInterval(TimeInterval timeInterval) {
		this.timeInterval = timeInterval;
	}

	/**
	 * Gets the activity type.
	 *
	 * @return the activityType
	 */
	public ActivityType getActivityType() {
		return activityType;
	}

	/**
	 * Sets the activity type.
	 *
	 * @param activityType            the activityType to set
	 */
	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	// /**
	// * Gets the location.
	// *
	// * @return the location
	// */
	// public Location getLocation() {
	// return location;
	// }

//	/**
//	 * Sets the location.
//	 *
//	 * @param location
//	 *            the location to set
//	 */
	// public void setLocation(Location location) {
	// this.location = location;
	// }

}
