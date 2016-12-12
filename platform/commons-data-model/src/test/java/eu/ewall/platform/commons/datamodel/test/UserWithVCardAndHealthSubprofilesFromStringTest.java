/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.test;

import java.util.ArrayList;
import java.util.Date;

import eu.ewall.platform.commons.datamodel.location.Location;
import eu.ewall.platform.commons.datamodel.marshalling.json.DM2JsonObjectMapper;
import eu.ewall.platform.commons.datamodel.profile.HealthDiagnosisType;
import eu.ewall.platform.commons.datamodel.profile.HealthImpairments;
import eu.ewall.platform.commons.datamodel.profile.HealthMeasurements;
import eu.ewall.platform.commons.datamodel.profile.HealthMedications;
import eu.ewall.platform.commons.datamodel.profile.HealthSubProfile;
import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.profile.UserProfile;
import eu.ewall.platform.commons.datamodel.profile.UserRole;
import eu.ewall.platform.commons.datamodel.profile.VCardGenderType;
import eu.ewall.platform.commons.datamodel.profile.VCardSubProfile;

/**
 * The Class UserWithVCardAndHealthSubprofilesToStringTest.
 *
 * @author eandgrg
 */
public class UserWithVCardAndHealthSubprofilesFromStringTest {

  /**
   * The main method.
   *
   * @param args
   *          the arguments
   */
  public static void main(String[] args) {

    VCardSubProfile vCardSubProfile = new VCardSubProfile("somenickname", "someaddress",
        "somecity", "somestate", "somecountry", "Europe/Zagreb", "somepostcode", new Date(1999, 2, 3), 15, null,
        "somebirthplace", "some@email.com", VCardGenderType.MALE, "0038591123543", "00385123456",
        new Location("room1", System.currentTimeMillis()));

    HealthMeasurements healthMeasurements = new HealthMeasurements(186, 77.6);

    HealthImpairments healthImpairments = new HealthImpairments(false, false, false, true, false);

    HealthMedications takesMedications = new HealthMedications();

    HealthSubProfile healthSubProfile = new HealthSubProfile(healthMeasurements, healthImpairments,
        takesMedications, new ArrayList<HealthDiagnosisType>());

    UserProfile userProfile = new UserProfile(vCardSubProfile, healthSubProfile, null, null, null,
        null, null);

    User user = new User("user12firstname", "user1Lastname", "user1Username", userProfile,
        UserRole.PRIMARY_USER);

    // first write to jsonstring
    String jsonstring = DM2JsonObjectMapper.writeValueAsString(user);

    // and then test read from jsonstring
    System.out.println(DM2JsonObjectMapper.readValueAsString(jsonstring, User.class));

  }
}
