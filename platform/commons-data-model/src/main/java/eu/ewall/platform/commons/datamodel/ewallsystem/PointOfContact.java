/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.ewallsystem;

import java.io.Serializable;
import java.net.URI;

/**
 * The Class PointOfContact.
 *
 * @author emirmos
 */
public class PointOfContact implements Serializable {

	private static final long serialVersionUID = 1L;

	/** The contact uri. */
	private URI contactURI;

	/** The expiration time. */
	private long expirationTime;

	/** The proxy status. */
	private ProxyStatus proxyStatus;

	/** The creation time. */
	private long creationTime;

	/** The last modified time. */
	private long lastModifiedTime;

	/** The local platform version. */
	private String localPlatformVersion;

	/**
	 * Instantiates a new point of contact.
	 */
	public PointOfContact() {
	}

	/**
	 * Gets the contact uri.
	 *
	 * @return the contactURI
	 */
	public URI getContactURI() {
		return contactURI;
	}

	/**
	 * Sets the contact uri.
	 *
	 * @param contactURI
	 *            the contactURI to set
	 */
	public void setContactURI(URI contactURI) {
		this.contactURI = contactURI;
	}

	/**
	 * Gets the expiration time.
	 *
	 * @return the expirationTime
	 */
	public long getExpirationTime() {
		return expirationTime;
	}

	/**
	 * Sets the expiration time.
	 *
	 * @param expirationTime
	 *            the expirationTime to set
	 */
	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
	}

	/**
	 * Gets the proxy status.
	 *
	 * @return the proxy status
	 */
	public ProxyStatus getProxyStatus() {
		return proxyStatus;
	}

	/**
	 * Sets the proxy status.
	 *
	 * @param proxyStatus
	 *            the new proxy status
	 */
	public void setProxyStatus(ProxyStatus proxyStatus) {
		this.proxyStatus = proxyStatus;
	}

	/**
	 * Gets the creation time.
	 *
	 * @return the creationTime
	 */
	public long getCreationTime() {
		return creationTime;
	}

	/**
	 * Sets the creation time.
	 *
	 * @param creationTime
	 *            the creationTime to set
	 */
	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * Gets the last modified time.
	 *
	 * @return the lastModifiedTime
	 */
	public long getLastModifiedTime() {
		return lastModifiedTime;
	}

	/**
	 * Sets the last modified time.
	 *
	 * @param lastModifiedTime
	 *            the lastModifiedTime to set
	 */
	public void setLastModifiedTime(long lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	/**
	 * Gets the local platform version.
	 *
	 * @return the local platform version
	 */
	public String getLocalPlatformVersion() {
		return localPlatformVersion;
	}

	/**
	 * Sets the local platform version.
	 *
	 * @param localPlatformVersion
	 *            the new local platform version
	 */
	public void setLocalPlatformVersion(String localPlatformVersion) {
		this.localPlatformVersion = localPlatformVersion;
	}

}
