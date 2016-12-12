/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.test;

import java.util.ArrayList;
import java.util.Date;

import eu.ewall.platform.commons.datamodel.activity.Activity;
import eu.ewall.platform.commons.datamodel.activity.GamingActivity;
import eu.ewall.platform.commons.datamodel.activity.MediaConsumingActivity;
import eu.ewall.platform.commons.datamodel.activity.SkypeCall;
import eu.ewall.platform.commons.datamodel.location.IndoorPlace;
import eu.ewall.platform.commons.datamodel.location.IndoorPlaceArea;
import eu.ewall.platform.commons.datamodel.location.Location;
import eu.ewall.platform.commons.datamodel.measure.TimeInterval;
import eu.ewall.platform.commons.datamodel.profile.ActivitiesSubProfile;
import eu.ewall.platform.commons.datamodel.profile.EmotionalStateCategory;
import eu.ewall.platform.commons.datamodel.profile.EmotionalStatesSubProfile;
import eu.ewall.platform.commons.datamodel.profile.HealthDiagnosisType;
import eu.ewall.platform.commons.datamodel.profile.HealthImpairments;
import eu.ewall.platform.commons.datamodel.profile.HealthMeasurements;
import eu.ewall.platform.commons.datamodel.profile.HealthMedications;
import eu.ewall.platform.commons.datamodel.profile.HealthSubProfile;
import eu.ewall.platform.commons.datamodel.profile.Intensity;
import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.profile.UserPreferencesSubProfile;
import eu.ewall.platform.commons.datamodel.profile.UserProfile;
import eu.ewall.platform.commons.datamodel.profile.UserRole;
import eu.ewall.platform.commons.datamodel.profile.VCardGenderType;
import eu.ewall.platform.commons.datamodel.profile.VCardSubProfile;
import eu.ewall.platform.commons.datamodel.profile.preferences.AudioPreferences;

/**
 * The Class ToStringMethodsTest.
 *
 * @author eandgrg
 */
public class UserHasCaregiversAndApplicationsTest {

  /**
   * The main method.
   *
   * @param args
   *          the arguments
   */
  @SuppressWarnings("deprecation")
  public static void main(String[] args) {

    User testUser = new User();

    testUser.setFirstName("TestUserName");
    testUser.setLastName("TestLastName");
    testUser.setUsername("testUsername");

    VCardSubProfile vCardSubProfile = new VCardSubProfile("testNickname", "testAddress",
        "testCity", "testState", "testCountry", "Europe/Zagreb", "10000", new Date(2014, 6, 6), 63, new Date(2014,
            6, 6), "testBirthPlace", "testEmail", VCardGenderType.MALE, "00385911231231",
        "0038515675675", new IndoorPlace("testIndoorPlaceName", IndoorPlaceArea.LIVING_ROOM,
            System.currentTimeMillis()));
    UserProfile userProfile = new UserProfile(vCardSubProfile);

    ActivitiesSubProfile activitiesSubProfile = new ActivitiesSubProfile();

    Activity gamingActivity = new GamingActivity();
    Location indoorPlaceLoc = new IndoorPlace("testIndoorLocationName",
        IndoorPlaceArea.LIVING_ROOM, System.currentTimeMillis());

    TimeInterval testTimeInterval = new TimeInterval(new Date(2014, 8, 26, 10, 45).getTime(),
        new Date(2014, 8, 27, 11, 12).getTime());
    gamingActivity.setTimeInterval(testTimeInterval);

    activitiesSubProfile.addActivityToActivitiesSubProfile(gamingActivity);
    activitiesSubProfile.addActivityToActivitiesSubProfile(new SkypeCall(testTimeInterval));
    activitiesSubProfile.addActivityToActivitiesSubProfile(new MediaConsumingActivity(
        testTimeInterval, 11, "test media desc"));

    userProfile.setActivitiesSubProfile(activitiesSubProfile);

    EmotionalStatesSubProfile testEmotionalStatesSubProfile = new EmotionalStatesSubProfile();
    testEmotionalStatesSubProfile
        .addEmotionalStateToEmotionalStateCategoriesSet(EmotionalStateCategory.JOY);
    testEmotionalStatesSubProfile
        .addEmotionalStateToEmotionalStateCategoriesSet(EmotionalStateCategory.INTEREST);

    userProfile.setEmotionalStatesSubProfile(testEmotionalStatesSubProfile);

    HealthMeasurements testHealthMeasurements = new HealthMeasurements();
    testHealthMeasurements.setHeight(172);
    testHealthMeasurements.setWeight(75);

    HealthMedications testHealthMedications = new HealthMedications();
    testHealthMedications.addMedicationToMedicatiosList("some medication name");

    HealthSubProfile healthSubProfile = new HealthSubProfile(testHealthMeasurements,
        new HealthImpairments(false, true, false, false, false), testHealthMedications,  new ArrayList<HealthDiagnosisType>());
    userProfile.setHealthSubProfile(healthSubProfile);

    AudioPreferences testAudioPreferences = new AudioPreferences(Intensity.MEDIUM,
        VCardGenderType.FEMALE, Intensity.LOW);
    userProfile.setUserPreferencesSubProfile(new UserPreferencesSubProfile(testAudioPreferences,
        null, null));

    testUser.setUserProfile(userProfile);
    testUser.setUserRole(UserRole.PRIMARY_USER);


    testUser.addCaregiverUsernameToCaregiversUsernamesList("testcaregiverusername");
    testUser.addCaregiverUsernameToCaregiversUsernamesList("testcaregiverusernameB");
    testUser.addCaregiverUsernameToCaregiversUsernamesList("testcaregiverusernameC");

    testUser.addApplicationNameToApplicationNamesList("appName");
    testUser.addApplicationNameToApplicationNamesList("appName2");
    testUser.addApplicationNameToApplicationNamesList("appName3");
    
    System.out.println("User with all subprofiles applications and caregivers:\n"
        + testUser.toString());

  }

}
