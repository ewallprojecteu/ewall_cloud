/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.location;

/**
 * The Class XYZcoordinates.
 *
 * @author eandgrg
 */
public class XYZcoordinates extends Location {

	/** The position x in centimeters. */
	private double positionX;

	/** The position y in centimeters. */
	private double positionY;

	/** The position z in centimeters. */
	private double positionZ;

	/**
	 * The Constructor.
	 */
	public XYZcoordinates() {

	}

	/**
	 * The Constructor.
	 *
	 * @param positionX            the position x
	 * @param positionY            the position y
	 * @param positionZ the position z
	 * @param name            the name
	 * @param timestamp            the timestamp
	 */
	public XYZcoordinates(double positionX, double positionY, double positionZ,
			String name, long timestamp) {
		this.name = name;
		this.timestamp = timestamp;
		this.positionX = positionX;
		this.positionY = positionY;
		this.positionZ = positionZ;

	}

	/**
	 * Gets the position x in centimeters.
	 *
	 * @return the positionX in centimeters
	 */
	public double getPositionX() {
		return positionX;
	}

	/**
	 * Sets the position x in centimeters.
	 *
	 * @param positionX
	 *            the positionX to set
	 */
	public void setPositionX(double positionX) {
		this.positionX = positionX;
	}

	/**
	 * Gets the position y in centimeters.
	 *
	 * @return the positionY
	 */
	public double getPositionY() {
		return positionY;
	}

	/**
	 * Sets the position y in centimeters.
	 *
	 * @param positionY
	 *            the positionY to set
	 */
	public void setPositionY(double positionY) {
		this.positionY = positionY;
	}

	/**
	 * Gets the position z in centimeters.
	 *
	 * @return the positionZ
	 */
	public double getPositionZ() {
		return positionZ;
	}

	/**
	 * Sets the position z in centimeters.
	 *
	 * @param positionZ
	 *            the positionZ to set
	 */
	public void setPositionZ(double positionZ) {
		this.positionZ = positionZ;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		StringBuffer buffer = new StringBuffer();
		buffer.append("------XYZcoordinates start------\n");
		buffer.append("name: ");
		buffer.append(name);
		buffer.append("\n");
		buffer.append("timestamp: ");
		buffer.append(timestamp);
		buffer.append("\n");
		buffer.append("positionX: ");
		buffer.append(positionX);
		buffer.append("\n");
		buffer.append("positionY: ");
		buffer.append(positionY);
		buffer.append("\n");
		buffer.append("positionZ: ");
		buffer.append(positionZ);
		buffer.append("\n");
		buffer.append("------XYZcoordinates end------\n");
		return buffer.toString();
	}

}
