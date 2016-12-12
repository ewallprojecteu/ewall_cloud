package eu.ewall.platform.commons.datamodel.streaming;

import java.net.URI;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * @author eandgrg
 */
public class AudioStream extends Stream {

	/** The bit rate. */
	private double bitRate;

	/** The has codec. */
	private AudioCodec audioCodec;

	/** The num of channels. */
	private int numOfChannels;

	/** The sample rate. */
	private double sampleRate;

	/**
	 * The Constructor.
	 */
	public AudioStream() {

	}

	/**
	 * The Constructor.
	 *
	 * @param id the id
	 * @param srcURI the src uri
	 * @param destURI the dest uri
	 * @param port the port
	 * @param streamingProtocol the streaming protocol
	 * @param bitRate the bit rate
	 * @param sampleRate the sample rate
	 * @param audioCodec the audio codec
	 * @param numOfChannels the num of channels
	 */
	public AudioStream(String id, URI srcURI, URI destURI, int port,
			StreamingProtocol streamingProtocol, double bitRate,
			double sampleRate, AudioCodec audioCodec, int numOfChannels) {

		this.streamId = id;
		this.destPort = port;
		this.srcURI = srcURI;
		this.destURI = destURI;
		this.streamingProtocol = streamingProtocol;
		this.bitRate = bitRate;
		this.sampleRate = sampleRate;
		this.audioCodec = audioCodec;
		this.numOfChannels = numOfChannels;

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
	 * Gets the audio codec.
	 *
	 * @return the audioCodec
	 */
	public AudioCodec getAudioCodec() {
		return audioCodec;
	}

	/**
	 * Sets the audio codec.
	 *
	 * @param audioCodec
	 *            the audioCodec to set
	 */
	public void setAudioCodec(AudioCodec audioCodec) {
		this.audioCodec = audioCodec;
	}

	/**
	 * Gets the num of channels.
	 *
	 * @return the numOfChannels
	 */
	public int getNumOfChannels() {
		return numOfChannels;
	}

	/**
	 * Sets the num of channels.
	 *
	 * @param numOfChannels
	 *            the numOfChannels to set
	 */
	public void setNumOfChannels(int numOfChannels) {
		this.numOfChannels = numOfChannels;
	}

	/**
	 * Gets the sample rate.
	 *
	 * @return the sampleRate
	 */
	public double getSampleRate() {
		return sampleRate;
	}

	/**
	 * Sets the sample rate.
	 *
	 * @param sampleRate
	 *            the sampleRate to set
	 */
	public void setSampleRate(double sampleRate) {
		this.sampleRate = sampleRate;
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
		buffer.append("sampleRate: ");
		buffer.append(sampleRate);
		buffer.append("\n");
		buffer.append("audioCodec: ");
		buffer.append(audioCodec);
		buffer.append("\n");
		buffer.append("numOfChannels: ");
		buffer.append(numOfChannels);
		buffer.append("\n");

		return buffer.toString();
	}

}