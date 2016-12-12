/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.json.customserializer.test;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.profile.UserProfile;
import eu.ewall.platform.commons.datamodel.profile.UserRole;
import eu.ewall.platform.commons.datamodel.profile.VCardSubProfile;

/**
 * The Class CustomUserSerializerTest.
 */
public class CustomUserSerializerTest {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		UserProfile userProfile = new UserProfile();
		userProfile.setvCardSubProfile(new VCardSubProfile());
		userProfile.getvCardSubProfile().setAddress("some test address");
		userProfile.getvCardSubProfile().setNickname("testNickname");

		User user = new User("TestFirstname", "TestLastname",
				"testusername", userProfile, UserRole.PRIMARY_USER);

		ObjectMapper mapper = new ObjectMapper();

		SimpleModule module = new SimpleModule();
		module.addSerializer(User.class, new CustomUserSerializer());
		mapper.registerModule(module);
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