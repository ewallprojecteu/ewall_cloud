/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.resource;

import java.net.URI;

/**
 * The Class WebResource.
 *
 * @author eandgrg
 */
public class WebResource extends Resource {

	/** The uri. */
	protected URI uri;

	/**
	 * The Constructor.
	 */
	public WebResource() {
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
	public WebResource(String name, URI uri, String description) {

		this.name = name;
		this.uri = uri;
		this.description = description;
	}

	/**
	 * Gets the uri.
	 *
	 * @return the uri
	 */
	public URI getUri() {
		return uri;
	}

	/**
	 * Sets the uri.
	 *
	 * @param uri
	 *            the uri to set
	 */
	public void setUri(URI uri) {
		this.uri = uri;
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
