package eu.ewall.platform.commons.datamodel.measure;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * An interval of time. Note that a TimeInterval has both an extent and a
 * location on the universal timeline. Note too that a TimeInterval has no gaps,
 * i.e. this class contains only convex time intervals.
 * 
 * @author eandgrg
 */
public class TimeInterval extends ConstantQuantityMeasure {

	/** The start time in miliseconds. */
	private long startTime;

	/** The end time in miliseconds. */
	private long endTime;

	/**
	 * The Constructor.
	 */
	public TimeInterval() {

	}

	/**
	 * The Constructor.
	 *
	 * @param startTime
	 *            the start time
	 * @param endTime
	 *            the end time
	 */
	public TimeInterval(long startTime, long endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	/**
	 * Gets the start time in miliseconds.
	 *
	 * @return the startTime
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * Sets the start time in miliseconds.
	 *
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * Gets the end time in miliseconds.
	 *
	 * @return the endTime
	 */
	public long getEndTime() {
		return endTime;
	}

	/**
	 * Sets the end time in miliseconds.
	 *
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}


}