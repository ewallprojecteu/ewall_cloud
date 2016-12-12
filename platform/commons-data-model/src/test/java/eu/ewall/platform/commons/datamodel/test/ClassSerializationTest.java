/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.test;

import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

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
 * The Class ClassSerializationTest.
 * 
 * @author eandgrg
 */
public class ClassSerializationTest {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		System.out.println("Is Hello string serializable: "
				+ isSerializable("Hello"));

		System.out.println("Is User class serializable: "
				+ isSerializable(createTestUser()));
	}

	/**
	 * Creates the test user.
	 *
	 * @return the user
	 */
	private static User createTestUser() {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 1999);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date dateRepresentation = cal.getTime();

		VCardSubProfile vCardSubProfile = new VCardSubProfile("somenickname",
				"someaddress", "somecity", "somestate", "somecountry", "Europe/Zagreb",
				"somepostcode", dateRepresentation, 15, null, "somebirthplace",
				"some@email.com", VCardGenderType.MALE, "0038591123543",
				"00385123456",
				new Location("room1", System.currentTimeMillis()));

		SystemPreferences systemPreferences = new SystemPreferences();

		// this should be set since it is a valid ISO language code
		systemPreferences.setPreferredLanguage("hr");

		AudioPreferences audioPreferences = new AudioPreferences(
				Intensity.MEDIUM, VCardGenderType.FEMALE, Intensity.LOW);

		VisualPreferences visualPreferences = new VisualPreferences(
				Intensity.MEDIUM, Status.ON, Size.MEDIUM, Intensity.MEDIUM,
				Intensity.MEDIUM, Intensity.MEDIUM);

		UserPreferencesSubProfile userPreferencesSubProfile = new UserPreferencesSubProfile(
				audioPreferences, visualPreferences, systemPreferences);

		UserProfile userProfile = new UserProfile(vCardSubProfile, null,
				userPreferencesSubProfile, null, null, null, null);
		User user = new User("user12firstname", "user1Lastname",
				"user1Username", userProfile, UserRole.PRIMARY_USER);
		return user;
	}

	/**
	 * Checks if is serializable.
	 *
	 * @param o
	 *            the object
	 * @return true, if is serializable
	 */
	public static boolean isSerializable(final Object o) {
		final boolean retVal;

		if (isImplementingInterface(o)) {
			retVal = attemptToSerialize(o);
		} else {
			retVal = false;
		}

		return (retVal);
	}

	/**
	 * Implements interface.
	 *
	 * @param o
	 *            the object
	 * @return true, if successful
	 */
	private static boolean isImplementingInterface(final Object o) {
		final boolean retVal;

		retVal = ((o instanceof Serializable) || (o instanceof Externalizable));

		return retVal;
	}

	/**
	 * Attempt to serialize.
	 *
	 * @param o
	 *            the object
	 * @return true, if successful
	 */
	private static boolean attemptToSerialize(final Object o) {
		final OutputStream sink;
		ObjectOutputStream stream;

		stream = null;

		try {
			sink = new ByteArrayOutputStream();
			stream = new ObjectOutputStream(sink);
			stream.writeObject(o);
		} catch (final IOException ex) {
			return (false);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (final IOException ex) {
					// should not happen
				}
			}
		}

		return (true);
	}
}