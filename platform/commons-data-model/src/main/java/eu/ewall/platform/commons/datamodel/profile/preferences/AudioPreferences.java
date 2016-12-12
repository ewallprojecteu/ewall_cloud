/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.profile.preferences;

import eu.ewall.platform.commons.datamodel.profile.Intensity;
import eu.ewall.platform.commons.datamodel.profile.VCardGenderType;

/**
 * The Class AudioPreferences.
 *
 * @author eandgrg
 */
public class AudioPreferences extends Preferences {

	/** The volume. */
	private Intensity volume;

	/** The voice gender. */
	private VCardGenderType voiceGender;

	/** The speech rate. */
	private Intensity speechRate;

	/**
	 * The Constructor.
	 */
	public AudioPreferences() {

	}

	/**
	 * The Constructor.
	 *
	 * @param volume
	 *            the volume
	 * @param voiceGender
	 *            the voice gender
	 * @param speechRate
	 *            the speech rate
	 */
	public AudioPreferences(Intensity volume, VCardGenderType voiceGender,
			Intensity speechRate) {
		this.volume = volume;
		this.voiceGender = voiceGender;
		this.speechRate = speechRate;

	}

	/**
	 * Gets the volume.
	 *
	 * @return the volume
	 */
	public Intensity getVolume() {
		return volume;
	}

	/**
	 * Sets the volume.
	 *
	 * @param volume
	 *            the volume to set
	 */
	public void setVolume(Intensity volume) {
		this.volume = volume;
	}

	/**
	 * Gets the voice gender.
	 *
	 * @return the voiceGender
	 */
	public VCardGenderType getVoiceGender() {
		return voiceGender;
	}

	/**
	 * Sets the voice gender.
	 *
	 * @param voiceGender
	 *            the voiceGender to set
	 */
	public void setVoiceGender(VCardGenderType voiceGender) {
		this.voiceGender = voiceGender;
	}

	/**
	 * Gets the speech rate.
	 *
	 * @return the speechRate
	 */
	public Intensity getSpeechRate() {
		return speechRate;
	}

	/**
	 * Sets the speech rate.
	 *
	 * @param speechRate
	 *            the speechRate to set
	 */
	public void setSpeechRate(Intensity speechRate) {
		this.speechRate = speechRate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("volume: ");
		buffer.append(volume);
		buffer.append("\n");
		buffer.append("voiceGender: ");
		buffer.append(voiceGender);
		buffer.append("\n");
		buffer.append("speechRate: ");
		buffer.append(speechRate);
		buffer.append("\n");

		return buffer.toString();
	}

}
