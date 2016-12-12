/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.test;

import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import eu.ewall.platform.commons.datamodel.location.Location;
import eu.ewall.platform.commons.datamodel.profile.Intensity;
import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.profile.UserPreferencesSubProfile;
import eu.ewall.platform.commons.datamodel.profile.UserProfile;
import eu.ewall.platform.commons.datamodel.profile.UserRole;
import eu.ewall.platform.commons.datamodel.profile.VCardGenderType;
import eu.ewall.platform.commons.datamodel.profile.VCardSubProfile;
import eu.ewall.platform.commons.datamodel.profile.preferences.AudioPreferences;
import eu.ewall.platform.commons.datamodel.profile.preferences.Size;
import eu.ewall.platform.commons.datamodel.profile.preferences.Status;
import eu.ewall.platform.commons.datamodel.profile.preferences.SystemPreferences;
import eu.ewall.platform.commons.datamodel.profile.preferences.VisualPreferences;

/**
 * The Class UserPreferencesTest.
 */
public class UserPreferencesTest {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		VCardSubProfile vCardSubProfile = new VCardSubProfile("somenickname",
				"someaddress", "somecity", "somestate", "somecountry", "Europe/Zagreb",
				"somepostcode", new Date(1999, 2, 3), 15, null,
				"somebirthplace", "some@email.com", VCardGenderType.MALE,
				"0038591123543", "00385123456", new Location("room1",
						System.currentTimeMillis()));

		SystemPreferences systemPreferences = new SystemPreferences();

		// this should be set since it is a valid ISO language code
		systemPreferences.setPreferredLanguage("hr");

		// this should not be set since it is not a valid ISO language code
		systemPreferences.setSecondaryLanguage("xx");

		AudioPreferences audioPreferences = new AudioPreferences(
				Intensity.MEDIUM, VCardGenderType.FEMALE, Intensity.LOW);

		VisualPreferences visualPreferences = new VisualPreferences(
				Intensity.MEDIUM, Status.ON, Size.MEDIUM, Intensity.MEDIUM,
				Intensity.MEDIUM, Intensity.MEDIUM);

		UserPreferencesSubProfile userPreferencesSubProfile = new UserPreferencesSubProfile(
				audioPreferences, visualPreferences, systemPreferences);

		UserProfile userProfile = new UserProfile(vCardSubProfile, null,
				userPreferencesSubProfile, null, null, null, null);

		User user = new User("userWithPrefsfirstname",
				"userWithPrefsLastname", "userWithPrefsUsername", userProfile,
				UserRole.PRIMARY_USER);

		System.out.println("All ISO language codes are: "
				+ Arrays.asList(Locale.getISOLanguages()));

		System.out
				.println("User with preferences profiles: " + user.toString());

	}
}
