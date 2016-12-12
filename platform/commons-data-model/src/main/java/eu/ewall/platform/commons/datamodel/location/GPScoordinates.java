package eu.ewall.platform.commons.datamodel.location;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * @author eandgrg
 */
public class GPScoordinates extends Location {

	/** in decimal degrees. */
	private double latitude;

	/** in decimal degrees. */
	private double longitude;

	/**
	 * The Constructor.
	 */
	public GPScoordinates() {

	}

	/**
	 * The Constructor.
	 *
	 * @param name the name
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param timestamp the timestamp
	 */
	public GPScoordinates(String name, double latitude, double longitude,
			long timestamp) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.timestamp = timestamp;
	}

	/**
	 * Gets the latitude.
	 *
	 * @return latitude in degrees
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Sets the latitude.
	 *
	 * @param latitude
	 *            in degrees
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * Gets the longitude.
	 *
	 * @return longitude in degrees
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Sets the longitude.
	 *
	 * @param longitude
	 *            in degrees
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		StringBuffer buffer = new StringBuffer();
		buffer.append("------GPScoordinates start------\n");
		buffer.append("name: ");
		buffer.append(name);
		buffer.append("\n");
		buffer.append("timestamp: ");
		buffer.append(timestamp);
		buffer.append("\n");
		buffer.append("latitude: ");
		buffer.append(latitude);
		buffer.append("\n");
		buffer.append("longitude: ");
		buffer.append(longitude);
		buffer.append("\n");
		buffer.append("------GPScoordinates end------\n");

		return buffer.toString();
	}

}