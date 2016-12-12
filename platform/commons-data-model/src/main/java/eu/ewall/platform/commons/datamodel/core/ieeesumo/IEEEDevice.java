/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.core.ieeesumo;


/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * A Device is an Artifact whose purpose is to serve as an instrument in a
 * specific subclass of Process.
 * 
 * @author eandgrg
 */
public abstract class IEEEDevice extends Artifact {

	/** name */
	protected String name;

	/** The serial number. */
	protected float serialNumber;

	/** The manufacturer. */
	protected Manufacturer manufacturer;

	/**
	 * The Constructor.
	 */
	public IEEEDevice() {
		super();
	}

	/**
	 * The Constructor.
	 *
	 * @param name
	 *            the device name
	 */
	public IEEEDevice(String name) {
		super();
		this.name = name;
	}

	/**
	 * Gets the device name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * Sets the device name.
	 *
	 * @param name
	 *            the new device name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the manufacturer.
	 *
	 * @return the manufacturer
	 */
	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	/**
	 * Sets the manufacturer.
	 *
	 * @param manufacturer
	 *            the manufacturer
	 */
	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	/**
	 * Gets the serial number.
	 *
	 * @return the serial number
	 */
	public float getSerialNumber() {
		return serialNumber;
	}

	/**
	 * Sets the serial number.
	 *
	 * @param serialNumber
	 *            the serial number
	 */
	public void setSerialNumber(float serialNumber) {
		this.serialNumber = serialNumber;
	}

}