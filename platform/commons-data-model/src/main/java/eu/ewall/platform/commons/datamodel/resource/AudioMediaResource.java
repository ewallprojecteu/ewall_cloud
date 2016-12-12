/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.resource;

/**
 * @author eandgrg
 *
 */
public class AudioMediaResource extends MediaResource {

	/**
	 * The Constructor.
	 *
	 */
	public AudioMediaResource() {

	}

	/**
	 * The Constructor.
	 *
	 * @param name
	 *            the name
	 * @param description
	 *            the description
	 */
	public AudioMediaResource(String name, String description) {

		this.name = name;
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
