/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.resource;

/**
 * @author eandgrg
 *
 */
public class VideoMediaResource extends MediaResource {
	/**
	 * The Constructor.
	 *
	 */
	public VideoMediaResource() {

	}

	/**
	 * The Constructor.
	 *
	 * @param name
	 *            the name
	 * @param description
	 *            the description
	 */
	public VideoMediaResource(String name, String description) {

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
