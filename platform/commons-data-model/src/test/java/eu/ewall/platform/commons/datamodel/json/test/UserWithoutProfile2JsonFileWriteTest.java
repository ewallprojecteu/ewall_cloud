/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.json.test;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.profile.UserProfile;
import eu.ewall.platform.commons.datamodel.profile.UserRole;

/**
 * The Class UserWithoutProfile2JsonFileWriteTest.
 *
 * @author eandgrg
 */
public class UserWithoutProfile2JsonFileWriteTest {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		User user = new User("TestFirstname", "TestLastname",
				"TestUsername", new UserProfile(), UserRole.PRIMARY_USER);

		ObjectMapper mapper = new ObjectMapper();

		// enable pretty output
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		// Use class name as root key for JSON Jackson serialization
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);

		try {
			mapper.writeValue(new File("testUser2JsonFile.json"), user);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out
				.println("file sucessfully created in the project root (refresh your project if you do not see it)!");
	}
}
