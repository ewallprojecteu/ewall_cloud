/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.json.test;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.profile.UserProfile;
import eu.ewall.platform.commons.datamodel.profile.UserRole;

/**
 * The Class User2JsonTest.
 *
 * @author eandgrg
 */
public class User2JsonTest {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		User user = new User("TestFirstname", "TestLastname",
				"Testusername", new UserProfile(), UserRole.PRIMARY_USER);

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
