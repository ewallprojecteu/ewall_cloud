/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.streaming;

import java.net.URI;

import eu.ewall.platform.commons.datamodel.core.ieeesumo.Motion;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * @author eandgrg
 */
public abstract class Stream extends Motion {

	/** The deviceId. */
	protected String streamId;

	/** The src uri. */
	protected URI srcURI;

	/** The dest uri. */
	protected URI destURI;

	/** The destPort. */
	protected int destPort;

	/** The streamingProtocol. */
	protected StreamingProtocol streamingProtocol;

	/**
	 * The Constructor.
	 */
	public Stream() {

	}

	/**
	 * Gets the src uri.
	 *
	 * @return the srcURI
	 */
	public URI getSrcURI() {
		return srcURI;
	}

	/**
	 * Sets the src uri.
	 *
	 * @param srcURI
	 *            the srcURI to set
	 */
	public void setSrcURI(URI srcURI) {
		this.srcURI = srcURI;
	}

	/**
	 * Gets the dest uri.
	 *
	 * @return the destURI
	 */
	public URI getDestURI() {
		return destURI;
	}

	/**
	 * Sets the dest uri.
	 *
	 * @param destURI
	 *            the destURI to set
	 */
	public void setDestURI(URI destURI) {
		this.destURI = destURI;
	}


	/**
	 * Gets the stream id.
	 *
	 * @return the stream id
	 */
	public String getStreamId() {
		return streamId;
	}

	/**
	 * Sets the stream id.
	 *
	 * @param streamid
	 *            the new stream id
	 */
	public void setStreamId(String streamid) {
		this.streamId = streamid;
	}

	/**
	 * Gets the destPort.
	 *
	 * @return the destPort
	 */
	public int getDestPort() {
		return destPort;
	}

	/**
	 * Sets the destPort.
	 *
	 * @param port
	 *            the new dest port
	 */
	public void setDestPort(int port) {
		this.destPort = port;
	}

	/**
	 * Gets the streamingProtocol.
	 *
	 * @return the streamingProtocol
	 */
	public StreamingProtocol getProtocol() {
		return streamingProtocol;
	}

	/**
	 * Sets the streamingProtocol.
	 *
	 * @param protocol
	 *            the new protocol
	 */
	public void setProtocol(StreamingProtocol protocol) {
		this.streamingProtocol = protocol;
	}

}