/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.location;

/**
 * The Class XYcoordinates.
 *
 * @author eandgrg
 */
public class XYcoordinates extends Location {

	/** The position x in pixels. */
	private double positionX;

	/** The position y in pixels. */
	private double positionY;

	/**
	 * The Constructor.
	 */
	public XYcoordinates() {

	}

	/**
	 * The Constructor.
	 *
	 * @param positionX
	 *            the position x
	 * @param positionY
	 *            the position y
	 * @param name
	 *            the name
	 * @param timestamp
	 *            the timestamp
	 */
	public XYcoordinates(double positionX, double positionY, String name,
			long timestamp) {
		this.name = name;
		this.timestamp = timestamp;
		this.positionX = positionX;
		this.positionY = positionY;
	}

	/**
	 * Gets the position x in pixels.
	 *
	 * @return the positionX in pixels
	 */
	public double getPositionX() {
		return positionX;
	}

	/**
	 * Sets the position x in pixels.
	 *
	 * @param positionX
	 *            the positionX to set
	 */
	public void setPositionX(double positionX) {
		this.positionX = positionX;
	}

	/**
	 * Gets the position y in pixels.
	 *
	 * @return the positionY
	 */
	public double getPositionY() {
		return positionY;
	}

	/**
	 * Sets the position y in pixels.
	 *
	 * @param positionY
	 *            the positionY to set
	 */
	public void setPositionY(double positionY) {
		this.positionY = positionY;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		StringBuffer buffer = new StringBuffer();
		buffer.append("------XYcoordinates start------\n");
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
		buffer.append("------XYcoordinates end------\n");
		return buffer.toString();
	}

}
