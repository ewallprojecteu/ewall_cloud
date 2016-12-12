/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.profile;

import java.io.Serializable;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * User profile containing references to different subprofile types.
 * 
 * @author eandgrg
 */
public class UserProfile implements Serializable {

	/**
	 * Default serial version uid, will be inherited in all (sub)classes.
	 */
	private static final long serialVersionUID = -4820552310137827397L;

	/** The health sub profile. */
	private HealthSubProfile healthSubProfile;

	/** The userPreferencesSubProfile. */
	private UserPreferencesSubProfile userPreferencesSubProfile;

	/** The emotional states sub profile. */
	private EmotionalStatesSubProfile emotionalStatesSubProfile;

	/** The v card sub profile. */
	private VCardSubProfile vCardSubProfile;

	/** The activities sub profile. */
	private ActivitiesSubProfile activitiesSubProfile;

	/** The eWall specific sub profile. */
	private EWallSubProfile eWallSubProfile;

	/** The psychological state sub profile. */
	private PsychologicalStateSubProfile psychologicalStateSubProfile;

	/**
	 * The Constructor.
	 */
	public UserProfile() {

	}

	/**
	 * The Constructor.
	 *
	 * @param vCardSubProfile
	 *            the v card sub profile
	 */
	public UserProfile(VCardSubProfile vCardSubProfile) {

		this.vCardSubProfile = vCardSubProfile;

	}

	/**
	 * Instantiates a new user profile.
	 *
	 * @param vCardSubProfile
	 *            the v card sub profile
	 * @param healthSubProfile
	 *            the health sub profile
	 * @param preferences
	 *            the preferences
	 * @param emotionalStatesSubProfile
	 *            the emotional states sub profile
	 * @param activitiesSubProfile
	 *            the activities sub profile
	 * @param eWallSubProfile
	 *            the e wall sub profile
	 * @param psychologicalStateSubProfile
	 *            the psychological state sub profile
	 */
	public UserProfile(VCardSubProfile vCardSubProfile,
			HealthSubProfile healthSubProfile,
			UserPreferencesSubProfile preferences,
			EmotionalStatesSubProfile emotionalStatesSubProfile,
			ActivitiesSubProfile activitiesSubProfile,
			EWallSubProfile eWallSubProfile,
			PsychologicalStateSubProfile psychologicalStateSubProfile) {

		this.vCardSubProfile = vCardSubProfile;
		this.healthSubProfile = healthSubProfile;
		this.userPreferencesSubProfile = preferences;
		this.emotionalStatesSubProfile = emotionalStatesSubProfile;
		this.activitiesSubProfile = activitiesSubProfile;
		this.eWallSubProfile = eWallSubProfile;
		this.psychologicalStateSubProfile = psychologicalStateSubProfile;

	}

	/**
	 * Gets the health sub profile.
	 *
	 * @return the healthSubProfile
	 */
	public HealthSubProfile getHealthSubProfile() {
		return healthSubProfile;
	}

	/**
	 * Sets the health sub profile.
	 *
	 * @param healthSubProfile
	 *            the healthSubProfile to set
	 */
	public void setHealthSubProfile(HealthSubProfile healthSubProfile) {
		this.healthSubProfile = healthSubProfile;
	}

	/**
	 * Gets the userPreferencesSubProfile.
	 *
	 * @return the userPreferencesSubProfile
	 */
	public UserPreferencesSubProfile getUserPreferencesSubProfile() {
		return userPreferencesSubProfile;
	}

	/**
	 * Sets the userPreferencesSubProfile.
	 *
	 * @param userPreferencesSubProfile
	 *            the userPreferencesSubProfile to set
	 */
	public void setUserPreferencesSubProfile(
			UserPreferencesSubProfile userPreferencesSubProfile) {
		this.userPreferencesSubProfile = userPreferencesSubProfile;
	}

	/**
	 * Gets the emotional states sub profile.
	 *
	 * @return the emotionalStatesSubProfile
	 */
	public EmotionalStatesSubProfile getEmotionalStatesSubProfile() {
		return emotionalStatesSubProfile;
	}

	/**
	 * Sets the emotional states sub profile.
	 *
	 * @param emotionalStatesSubProfile
	 *            the emotionalStatesSubProfile to set
	 */
	public void setEmotionalStatesSubProfile(
			EmotionalStatesSubProfile emotionalStatesSubProfile) {
		this.emotionalStatesSubProfile = emotionalStatesSubProfile;
	}

	/**
	 * Getv card sub profile.
	 *
	 * @return the vCardSubProfile
	 */
	public VCardSubProfile getvCardSubProfile() {
		return vCardSubProfile;
	}

	/**
	 * Setv card sub profile.
	 *
	 * @param vCardSubProfile
	 *            the vCardSubProfile to set
	 */
	public void setvCardSubProfile(VCardSubProfile vCardSubProfile) {
		this.vCardSubProfile = vCardSubProfile;
	}

	/**
	 * Gets the activities sub profile.
	 *
	 * @return the activitiesSubProfile
	 */
	public ActivitiesSubProfile getActivitiesSubProfile() {
		return activitiesSubProfile;
	}

	/**
	 * Sets the activities sub profile.
	 *
	 * @param activitiesSubProfile
	 *            the activitiesSubProfile to set
	 */
	public void setActivitiesSubProfile(
			ActivitiesSubProfile activitiesSubProfile) {
		this.activitiesSubProfile = activitiesSubProfile;
	}

	/**
	 * Gets the e wall sub profile.
	 *
	 * @return the e wall sub profile
	 */
	public EWallSubProfile geteWallSubProfile() {
		return eWallSubProfile;
	}

	/**
	 * Sets the e wall sub profile.
	 *
	 * @param eWallSubProfile the new e wall sub profile
	 */
	public void seteWallSubProfile(EWallSubProfile eWallSubProfile) {
		this.eWallSubProfile = eWallSubProfile;
	}

	/**
	 * Gets the psychological state sub profile.
	 *
	 * @return the psychological state sub profile
	 */
	public PsychologicalStateSubProfile getPsychologicalStateSubProfile() {
		return psychologicalStateSubProfile;
	}

	/**
	 * Sets the psychological state sub profile.
	 *
	 * @param psychologicalStateSubProfile the new psychological state sub profile
	 */
	public void setPsychologicalStateSubProfile(
			PsychologicalStateSubProfile psychologicalStateSubProfile) {
		this.psychologicalStateSubProfile = psychologicalStateSubProfile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		StringBuffer buffer = new StringBuffer();
		buffer.append("------UserProfile start------\n");
		buffer.append("healthSubProfile:\n ");

		if (healthSubProfile != null)
			buffer.append(healthSubProfile.toString());
		else
			buffer.append("empty");

		buffer.append("\n");
		buffer.append("preferencesSubProfile:\n ");

		if (userPreferencesSubProfile != null)
			buffer.append(userPreferencesSubProfile.toString());
		else
			buffer.append("empty");

		buffer.append("\n");
		buffer.append("emotionalStatesSubProfile:\n ");

		if (emotionalStatesSubProfile != null)
			buffer.append(emotionalStatesSubProfile.toString());
		else
			buffer.append("empty");
		buffer.append("\n");
		buffer.append("vCardSubProfile:\n");

		if (vCardSubProfile != null)
			buffer.append(vCardSubProfile.toString());
		else
			buffer.append("empty");
		buffer.append("\n");
		buffer.append("activitiesSubProfile:\n");

		if (activitiesSubProfile != null)
			buffer.append(activitiesSubProfile.toString());
		else
			buffer.append("empty");
		buffer.append("\n");

		buffer.append("eWallSubProfile:\n");

		if (eWallSubProfile != null)
			buffer.append(eWallSubProfile.toString());
		else
			buffer.append("empty");
		if (psychologicalStateSubProfile != null)
			buffer.append(psychologicalStateSubProfile.toString());
		else
			buffer.append("empty");
		buffer.append("\n");
		buffer.append("------UserProfile end------\n");

		return buffer.toString();
	}

}