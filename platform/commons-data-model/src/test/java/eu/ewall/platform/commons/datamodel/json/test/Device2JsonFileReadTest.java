/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.json.test;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import eu.ewall.platform.commons.datamodel.device.Device;

/**
 * The Class UserWithoutProfile2JsonFileReadTest.
 *
 * @author eandgrg
 */
public class Device2JsonFileReadTest {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		Device eWallDevice = null;
		ObjectMapper mapper = new ObjectMapper();

		// enable pretty output
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		// ignore parent tag from json
		mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);

		try {
			eWallDevice = mapper.readValue(new File(
					"testDeviceAsObject2JsonFile.json"), Device.class);

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

		System.out.println(eWallDevice.toString());
	}
}
