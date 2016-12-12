package eu.ewall.platform.idss.service.model.common;

/**
 * Possible sleep measures for which a goal is calculated by the Automatic Goal
 * Setting.
 * 
 * @author Dennis Hofs (RRD)
 */
public enum SleepGoalMeasure {
	MINIMUM_MINUTES_SLEPT,
	MAXIMUM_MINUTES_SLEPT,
	SLEEP_EFFICIENCY,
	GOING_TO_SLEEP_TIME,
	WAKING_UP_TIME
}
