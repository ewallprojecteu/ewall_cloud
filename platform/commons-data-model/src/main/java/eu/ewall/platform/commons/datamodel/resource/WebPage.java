/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.resource;

import java.net.URI;

/**
 * @author eandgrg
 *
 */
public class WebPage extends WebResource {

	/**
	 * The Constructor.
	 */
	public WebPage() {
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
	public WebPage(String name, URI uri, String description) {

		this.name = name;
		this.uri = uri;
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
		buffer.append("uri: ");
		buffer.append(uri);
		buffer.append("\n");

		return buffer.toString();
	}

}
