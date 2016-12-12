/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.streaming;

import java.net.URI;

import eu.ewall.platform.commons.datamodel.profile.Intensity;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * @author eandgrg
 */
public class VideoStream extends Stream {

	/** The bit rate. */
	private double bitRate;

	/** The frame rate. */
	private long frameRate;

	/** The has codec. */
	private VideoCodec videoCodec;

	/** The quality. */
	private Intensity quality;

	/**
	 * Instantiates a new video stream.
	 *
	 * @param streamId
	 *            the stream id
	 * @param srcURI
	 *            the src uri
	 * @param destURI
	 *            the dest uri
	 * @param streamingProtocol
	 *            the streaming protocol
	 * @param bitRate
	 *            the bit rate
	 * @param destPort
	 *            the dest port
	 * @param frameRate
	 *            the frame rate
	 * @param videoCodec
	 *            the video codec
	 * @param quality
	 *            the quality
	 */
	public VideoStream(String streamId, URI srcURI, URI destURI,
			StreamingProtocol streamingProtocol, double bitRate, int destPort,
			long frameRate, VideoCodec videoCodec, Intensity quality) {
		this.srcURI = srcURI;
		this.destURI = destURI;
		this.streamId = streamId;
		this.destPort = destPort;
		this.streamingProtocol = streamingProtocol;
		this.bitRate = bitRate;
		this.frameRate = frameRate;
		this.videoCodec = videoCodec;
		this.quality = quality;
	}

	/**
	 * Gets the bit rate.
	 *
	 * @return the bitRate
	 */
	public double getBitRate() {
		return bitRate;
	}

	/**
	 * Sets the bit rate.
	 *
	 * @param bitRate
	 *            the bitRate to set
	 */
	public void setBitRate(double bitRate) {
		this.bitRate = bitRate;
	}

	/**
	 * Gets the frame rate.
	 *
	 * @return the frameRate
	 */
	public long getFrameRate() {
		return frameRate;
	}

	/**
	 * Sets the frame rate.
	 *
	 * @param frameRate
	 *            the frameRate to set
	 */
	public void setFrameRate(long frameRate) {
		this.frameRate = frameRate;
	}

	/**
	 * Gets the video codec.
	 *
	 * @return the videoCodec
	 */
	public VideoCodec getVideoCodec() {
		return videoCodec;
	}

	/**
	 * Sets the video codec.
	 *
	 * @param videoCodec
	 *            the videoCodec to set
	 */
	public void setVideoCodec(VideoCodec videoCodec) {
		this.videoCodec = videoCodec;
	}

	/**
	 * Gets the quality.
	 *
	 * @return the quality
	 */
	public Intensity getQuality() {
		return quality;
	}

	/**
	 * Sets the quality.
	 *
	 * @param quality
	 *            the quality to set
	 */
	public void setQuality(Intensity quality) {
		this.quality = quality;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("streamId: ");
		buffer.append(streamId);
		buffer.append("\n");
		buffer.append("srcURI: ");
		buffer.append(srcURI);
		buffer.append("\n");
		buffer.append("destURI: ");
		buffer.append(destURI);
		buffer.append("\n");
		buffer.append("destPort: ");
		buffer.append(destPort);
		buffer.append("\n");
		buffer.append("streamingProtocol: ");
		buffer.append(streamingProtocol);
		buffer.append("\n");
		buffer.append("bitRate: ");
		buffer.append(bitRate);
		buffer.append("\n");
		buffer.append("frameRate: ");
		buffer.append(frameRate);
		buffer.append("\n");
		buffer.append("videoCodec: ");
		buffer.append(videoCodec);
		buffer.append("\n");
		buffer.append("quality: ");
		buffer.append(quality);
		buffer.append("\n");

		return buffer.toString();
	}
}