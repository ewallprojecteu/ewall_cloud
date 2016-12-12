/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.profilingserver.test.calls.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import eu.ewall.platform.commons.datamodel.location.Location;
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
 * The Class StoreUserWithVCardandHealthSubProfileCall.
 */
public class StoreUserWithVCardandHealthSubProfileCall {

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

		HealthMeasurements healthMeasurements = new HealthMeasurements(177, 77);

		HealthImpairments healthImpairments = new HealthImpairments(false,
				false, false, true, false);

		HealthMedications takesMedications = new HealthMedications();

		List<HealthDiagnosisType> healthDiagnosisTypeList = new ArrayList<HealthDiagnosisType>();
		healthDiagnosisTypeList.add(HealthDiagnosisType.COPD_GOLD2);
		healthDiagnosisTypeList.add(HealthDiagnosisType.FRAIL);
		healthDiagnosisTypeList.add(HealthDiagnosisType.MCI);
		
		HealthSubProfile healthSubProfile = new HealthSubProfile(
				healthMeasurements, healthImpairments, takesMedications, healthDiagnosisTypeList);

		UserProfile userProfile = new UserProfile(vCardSubProfile,
				healthSubProfile, null, null, null, null, null);

		User user = new User("7may", "7mayLastname",
				"7may2015Username", userProfile, UserRole.PRIMARY_USER);

		// String userAsJsonString =
		// DM2JsonObjectMapper.writeValueAsString(user);
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<User> entity = new HttpEntity<User>(user, headers);

		ResponseEntity<String> out = restTemplate.exchange(
				"http://localhost:8080/profiling-server/users/",
				HttpMethod.POST, entity, String.class);

		System.out.println("response:" + out.toString());

	}
}
