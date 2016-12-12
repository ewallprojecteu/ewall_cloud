/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.device;

import eu.ewall.platform.commons.datamodel.core.ieeesumo.Manufacturer;
import eu.ewall.platform.commons.datamodel.location.Location;

/**
 * The Class Loudspeaker.
 *
 * @author eandgrg
 */
public class Loudspeaker extends UIDevice {

	/** The location. */
	protected Location location;

	/**
	 * The Constructor.
	 */
	public Loudspeaker() {
	}


	/**
	 * Instantiates a new loudspeaker.
	 *
	 * @param deviceName
	 *            the device name
	 * @param manufacturer
	 *            the manufacturer
	 * @param serialNumber
	 *            the serial number
	 * @param location
	 *            the location
	 */
	public Loudspeaker(String deviceName, Manufacturer manufacturer, float serialNumber,
			Location location) {
		this.name = deviceName;
		this.manufacturer = manufacturer;
		this.serialNumber = serialNumber;
		this.location = location;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("deviceName: ");
		buffer.append(name);
		buffer.append("\n");

		buffer.append("manufacturer:\n");
		if (manufacturer != null)
			buffer.append(manufacturer.toString());
		else
			buffer.append("empty");
		buffer.append("\n");

		buffer.append("serialNumber: ");
		buffer.append(serialNumber);
		buffer.append("\n");

		buffer.append("location:\n");
		if (location != null)
			buffer.append(location.toString());
		else
			buffer.append("empty");
		buffer.append("\n");

		return buffer.toString();
	}

}
