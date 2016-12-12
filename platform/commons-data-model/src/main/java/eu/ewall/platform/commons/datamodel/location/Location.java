package eu.ewall.platform.commons.datamodel.location;

import java.io.Serializable;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * Instead of instantiating this class classes that extend it should be used:
 * {@link OutdoorPlace}, {@link IndoorPlace}, {@link GPScoordinates},
 * {@link XYcoordinates}, {@link XYZcoordinates}.
 * 
 * @author eandgrg
 */
public class Location implements Serializable {

	private static final long serialVersionUID = 1L;

	/** The timestamp. */
	protected long timestamp;

	/** The name. */
	protected String name;

	/**
	 * The Constructor.
	 */
	public Location() {

	}

	/**
	 * The Constructor.
	 *
	 * @param name the name
	 * @param timestamp the timestamp
	 */
	public Location(String name, long timestamp) {
		this.name = name;
		this.timestamp = timestamp;
	}

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
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		StringBuffer buffer = new StringBuffer();
		buffer.append("------Location start------\n");
		buffer.append("name: ");
		buffer.append(name);
		buffer.append("\n");
		buffer.append("timestamp: ");
		buffer.append(timestamp);
		buffer.append("\n");
		buffer.append("------Location end------\n");
		return buffer.toString();
	}

}
