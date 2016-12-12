package eu.ewall.platform.commons.datamodel.profile;

import eu.ewall.platform.commons.datamodel.profile.preferences.AudioPreferences;
import eu.ewall.platform.commons.datamodel.profile.preferences.Preferences;
import eu.ewall.platform.commons.datamodel.profile.preferences.SystemPreferences;
import eu.ewall.platform.commons.datamodel.profile.preferences.VisualPreferences;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * @author eandgrg
 */
public class UserPreferencesSubProfile extends UserSubProfile {

	/** The audio preferences. */
	private AudioPreferences audioPreferences;

	/** The visual preferences. */
	private VisualPreferences visualPreferences;

	/** The system preferences. */
	private SystemPreferences systemPreferences;

	/**
	 * The Constructor.
	 */
	public UserPreferencesSubProfile() {

	}

	/**
	 * The Constructor.
	 *
	 * @param audioPreferences
	 *            the audio preferences
	 * @param visualPreferences
	 *            the visual preferences
	 * @param systemPreferences
	 *            the system preferences
	 */
	public UserPreferencesSubProfile(AudioPreferences audioPreferences,
			VisualPreferences visualPreferences, SystemPreferences systemPreferences) {
		this.systemPreferences = systemPreferences;
		this.audioPreferences = audioPreferences;
		this.visualPreferences = visualPreferences;

	}

	/**
	 * Gets the audio preferences.
	 *
	 * @return the audioPreferences
	 */
	public Preferences getAudioPreferences() {
		return audioPreferences;
	}

	/**
	 * Sets the audio preferences.
	 *
	 * @param audioPreferences
	 *            the audioPreferences to set
	 */
	public void setAudioPreferences(AudioPreferences audioPreferences) {
		this.audioPreferences = audioPreferences;
	}

	/**
	 * Gets the visual preferences.
	 *
	 * @return the visualPreferences
	 */
	public Preferences getVisualPreferences() {
		return visualPreferences;
	}

	/**
	 * Sets the visual preferences.
	 *
	 * @param visualPreferences
	 *            the visualPreferences to set
	 */
	public void setVisualPreferences(VisualPreferences visualPreferences) {
		this.visualPreferences = visualPreferences;
	}

	/**
	 * Gets the system preferences.
	 *
	 * @return the systemPreferences
	 */
	public Preferences getSystemPreferences() {
		return systemPreferences;
	}

	/**
	 * Sets the system preferences.
	 *
	 * @param systemPreferences
	 *            the systemPreferences to set
	 */
	public void setSystemPreferences(SystemPreferences systemPreferences) {
		this.systemPreferences = systemPreferences;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		StringBuffer buffer = new StringBuffer();
		buffer.append("******UserPreferencesSubProfile start******\n");
		buffer.append("audioPreferences:\n");
		if (audioPreferences != null)
			buffer.append(audioPreferences.toString());
		else
			buffer.append("empty");
		buffer.append("\n");

		buffer.append("visualPreferences:\n");
		if (visualPreferences != null)
			buffer.append(visualPreferences.toString());
		else
			buffer.append("empty");
		buffer.append("\n");

		buffer.append("systemPreferences:\n");
		if (systemPreferences != null)
			buffer.append(systemPreferences.toString());
		else
			buffer.append("empty");
		buffer.append("\n");
		buffer.append("******UserPreferencesSubProfile end******\n");
		return buffer.toString();
	}
}