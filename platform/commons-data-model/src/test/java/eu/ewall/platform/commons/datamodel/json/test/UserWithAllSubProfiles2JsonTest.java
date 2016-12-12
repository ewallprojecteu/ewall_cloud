/*******************************************************************************
 * Copyright 2016 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.json.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import eu.ewall.platform.commons.datamodel.activity.Cooking;
import eu.ewall.platform.commons.datamodel.location.Location;
import eu.ewall.platform.commons.datamodel.message.MessageType;
import eu.ewall.platform.commons.datamodel.profile.ActivitiesSubProfile;
import eu.ewall.platform.commons.datamodel.profile.EWallSubProfile;
import eu.ewall.platform.commons.datamodel.profile.EmotionalStateCategory;
import eu.ewall.platform.commons.datamodel.profile.EmotionalStatesSubProfile;
import eu.ewall.platform.commons.datamodel.profile.FootProblemType;
import eu.ewall.platform.commons.datamodel.profile.HealthDiagnosisType;
import eu.ewall.platform.commons.datamodel.profile.HealthImpairments;
import eu.ewall.platform.commons.datamodel.profile.HealthMeasurements;
import eu.ewall.platform.commons.datamodel.profile.HealthMedications;
import eu.ewall.platform.commons.datamodel.profile.HealthSubProfile;
import eu.ewall.platform.commons.datamodel.profile.Intensity;
import eu.ewall.platform.commons.datamodel.profile.PsychologicalStageOfChangeType;
import eu.ewall.platform.commons.datamodel.profile.PsychologicalStateSubProfile;
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
 * The Class which when run as Java application prints out (in JSON) all the
 * user details including all user subprofiles (connected to user profile) 
 *
 * @author eandgrg
 */
public class UserWithAllSubProfiles2JsonTest {

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {

	////// SETTING VCardSubProfile DATA begin////////////////////////
	VCardSubProfile vCardSubProfile = new VCardSubProfile("FallTestUserNickname", "FallTestUserAddress",
		"FallTestUserCity", "FallTestUserState", "FallTestUserCountry", "Europe/Zagreb", "FallTestUserPostcode",
		new Date(1960, 2, 3), 56, null, "FallTestUserBirthplace", "some@email.com", VCardGenderType.MALE,
		"0038591123543", "00385123456", new Location("room1", System.currentTimeMillis()));

	////// SETTING HealthSubProfile DATA begin////////////////////////
	// SETTING HealthMeasurements (weight and hight)
	HealthMeasurements healthMeasurements = new HealthMeasurements(77, 177);

	// SETTING USER'S VISION AND FOOT PROBLEMS
	HealthImpairments healthImpairments = new HealthImpairments(false, false, true, false, false, Intensity.NONE,
		Intensity.NONE, Intensity.NONE, Intensity.NONE, Intensity.NONE, Intensity.NONE, Intensity.NONE,
		Intensity.NONE, Intensity.NONE, Intensity.MEDIUM, Intensity.EXTREME, false, false);
	healthImpairments.getFootProblemsList().add(FootProblemType.HALLUX_RIGIDUS);
	healthImpairments.getFootProblemsList().add(FootProblemType.HEEL_PAIN);

	// SETTING USER'S MEDICATIONS
	HealthMedications takesMedications = new HealthMedications();
	takesMedications.addMedicationToMedicatiosList("medication 1");
	takesMedications.addMedicationToMedicatiosList("medication 2");

	// SETTING USER'S HEALTH DIAGNOSES TYPES
	List<HealthDiagnosisType> healthDiagnosisTypeList = new ArrayList<HealthDiagnosisType>();
	healthDiagnosisTypeList.add(HealthDiagnosisType.COPD_GOLD2);
	healthDiagnosisTypeList.add(HealthDiagnosisType.FRAIL);
	healthDiagnosisTypeList.add(HealthDiagnosisType.MCI);

	// CREATING USER's HealthSubProfile
	HealthSubProfile healthSubProfile = new HealthSubProfile(healthMeasurements, healthImpairments,
		takesMedications, healthDiagnosisTypeList);
	////// SETTING HealthSubProfile DATA end////////////////////////

	////// SETTING UserPreferencesSubProfile data begin////////////////////
	SystemPreferences systemPreferences = new SystemPreferences();
	systemPreferences.setPreferredLanguage("hr");
	AudioPreferences audioPreferences = new AudioPreferences(Intensity.MEDIUM, VCardGenderType.FEMALE,Intensity.LOW);
	VisualPreferences visualPreferences = new VisualPreferences(Intensity.MEDIUM, Status.ON, Size.MEDIUM,
		Intensity.MEDIUM, Intensity.MEDIUM, Intensity.MEDIUM);
	UserPreferencesSubProfile userPreferencesSubProfile = new UserPreferencesSubProfile(audioPreferences,
		visualPreferences, systemPreferences);

	////// SETTING EmotionalStatesSubProfile data begin////////////////////
	EmotionalStatesSubProfile emotionalStatesSubProfile = new EmotionalStatesSubProfile();
	emotionalStatesSubProfile.addEmotionalStateToEmotionalStateCategoriesSet(EmotionalStateCategory.SOCIAL_AVOIDANCE);
	emotionalStatesSubProfile.addEmotionalStateToEmotionalStateCategoriesSet(EmotionalStateCategory.FEAR);

	////// SETTING ActivitiesSubProfile data begin////////////////////
	ActivitiesSubProfile activitiesSubProfile = new ActivitiesSubProfile();
	activitiesSubProfile.addActivityToActivitiesSubProfile(new Cooking());

	////// SETTING EWallSubProfile data begin////////////////////
	EWallSubProfile eWallSubProfile = new EWallSubProfile("someRegionName", true);
	eWallSubProfile.setRealTimeMonitoringAllowed(true);
	eWallSubProfile.getSelectedNotificationTypesList().add(MessageType.NORMAL);
	eWallSubProfile.getSelectedNotificationTypesList().add(MessageType.REMINDER);

	////// SETTING PsychologicalStateSubProfile data begin////////////////////
	PsychologicalStateSubProfile psychologicalStateSubProfile = new PsychologicalStateSubProfile(
		PsychologicalStageOfChangeType.CONTEMPLATION, PsychologicalStageOfChangeType.PREPARATION,
		PsychologicalStageOfChangeType.MAINTENANCE, PsychologicalStageOfChangeType.ACTION);


	//CONNECTING USER SUBPROFILES WITH USER PROFILE 
	UserProfile userProfile = new UserProfile(vCardSubProfile, healthSubProfile, userPreferencesSubProfile,
		emotionalStatesSubProfile, activitiesSubProfile, eWallSubProfile, psychologicalStateSubProfile);

	//CREATING USER AND CONNECTING IT WITH CREATED PROFILE (WITH SUBPROFILES)
	User user = new User("FallTestUserFirstname", "FallTestUserLastname", "FallTestUserUsername", userProfile,
		UserRole.PRIMARY_USER);

	ObjectMapper mapper = new ObjectMapper();

	// enable pretty output
	mapper.enable(SerializationFeature.INDENT_OUTPUT);

	// Use class name as root key for JSON Jackson serialization
	mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);

	try {

	    System.out.println(mapper.writeValueAsString(user));

	} catch (JsonGenerationException e) {

	    e.printStackTrace();

	} catch (JsonMappingException e) {

	    e.printStackTrace();

	} catch (IOException e) {

	    e.printStackTrace();

	}

    }
}
