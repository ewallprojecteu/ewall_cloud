package eu.ewall.platform.commons.datamodel.sensing;

/**
 * The Class Sensing.
 * 
 * @author emirmos
 */
public abstract class Sensing {

	/** The timestamp. */
	protected long timestamp;

	/**
	 * Gets the timestamp.
	 *
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the timestamp.
	 *
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
