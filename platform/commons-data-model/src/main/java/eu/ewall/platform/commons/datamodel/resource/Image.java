/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.resource;

import java.net.URI;

import eu.ewall.platform.commons.datamodel.location.XYcoordinates;

/**
 * The Class Image.
 *
 * @author eandgrg
 */
public class Image extends WebResource {

	/** The hight. */
	protected int hight;

	/** The width. */
	protected int width;

	/** The xy coordinates. */
	protected XYcoordinates xyCoordinates;

	/**
	 * The Constructor.
	 */
	public Image() {
	}

	/**
	 * The Constructor.
	 *
	 * @param name
	 *            the name
	 * @param uri
	 *            the uri
	 * @param description
	 *            the description
	 */
	public Image(String name, URI uri, String description) {

		this.name = name;
		this.uri = uri;
		this.description = description;
	}

	/**
	 * Gets the hight.
	 *
	 * @return the hight
	 */
	public int getHight() {
		return hight;
	}

	/**
	 * Sets the hight.
	 *
	 * @param hight
	 *            the hight to set
	 */
	public void setHight(int hight) {
		this.hight = hight;
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the width.
	 *
	 * @param width
	 *            the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Gets the xy coordinates.
	 *
	 * @return the xyCoordinates
	 */
	public XYcoordinates getXyCoordinates() {
		return xyCoordinates;
	}

	/**
	 * Sets the xy coordinates.
	 *
	 * @param xyCoordinates
	 *            the xyCoordinates to set
	 */
	public void setXyCoordinates(XYcoordinates xyCoordinates) {
		this.xyCoordinates = xyCoordinates;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("name: ");
		buffer.append(name);
		buffer.append("\n");
		buffer.append("description: ");
		buffer.append(description);
		buffer.append("\n");
		buffer.append("uri: ");
		buffer.append(uri);
		buffer.append("\n");
		buffer.append("hight: ");
		buffer.append(hight);
		buffer.append("\n");
		buffer.append("width: ");
		buffer.append(width);
		buffer.append("\n");
		if (xyCoordinates != null)
			buffer.append(xyCoordinates.toString());
		else
			buffer.append("empty");
		buffer.append("\n");

		return buffer.toString();
	}

}
