package eu.ewall.servicebrick.common.model;

import org.joda.time.DateTime;

/**
 * Parent class for measurements returned by service brick APIs. Uses a DateTime instead of a long timestamp as in
 * measurements returned by the profiling server.
 */
public abstract class SbMeasurement {

	private DateTime timestamp;
	
	public SbMeasurement(DateTime timestamp) {
		this.timestamp = timestamp;
	}

	public DateTime getTimestamp() {
		return timestamp;
	}
	
}
