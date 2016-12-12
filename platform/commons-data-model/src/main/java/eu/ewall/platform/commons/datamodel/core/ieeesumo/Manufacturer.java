/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.core.ieeesumo;

/**
 * Any Corporation which manufactures Products.
 * 
 * @author eandgrg
 */
public class Manufacturer extends Corporation {

	/** The name. */
	private String name;

	/** The description. */
	private String description;

	/**
	 * The Constructor.
	 */
	public Manufacturer() {

	}

	/**
	 * Instantiates a new manufacturer.
	 *
	 * @param name
	 *            the name
	 * @param description
	 *            the description
	 */
	public Manufacturer(String name, String description) {
		this.name = name;
		this.description = description;
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
	 *            the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description
	 *            the description
	 */
	public void setDescription(String description) {
		this.description = description;
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
		return buffer.toString();
	}

}