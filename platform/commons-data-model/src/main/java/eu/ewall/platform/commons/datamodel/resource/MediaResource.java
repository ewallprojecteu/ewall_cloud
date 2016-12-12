/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.resource;

/**
 * The Class WebResource.
 *
 * @author eandgrg
 */
public class MediaResource extends Resource {

	/**
	 * The Constructor.
	 */
	public MediaResource() {
	}

	/**
	 * Instantiates a new media resource.
	 *
	 * @param name
	 *            the name
	 * @param description
	 *            the description
	 */
	public MediaResource(String name, String description) {
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
