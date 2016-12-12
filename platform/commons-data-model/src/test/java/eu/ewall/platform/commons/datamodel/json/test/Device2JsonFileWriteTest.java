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

import eu.ewall.platform.commons.datamodel.core.ieeesumo.IEEEDevice;
import eu.ewall.platform.commons.datamodel.core.ieeesumo.Manufacturer;
import eu.ewall.platform.commons.datamodel.device.Device;
import eu.ewall.platform.commons.datamodel.device.DeviceType;

/**
 * The Class UserWithoutProfile2JsonFileWriteTest.
 *
 * @author eandgrg
 */
public class Device2JsonFileWriteTest {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		Manufacturer manufacturer = new Manufacturer("manName",
				"manDescription");
		IEEEDevice device = new Device("testDeviceName", DeviceType.ENVIRONMENTAL_SENSOR, "livingroom", true, false, "0000");

		ObjectMapper mapper = new ObjectMapper();

		// enable pretty output
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		// Use class name as root key for JSON Jackson serialization
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);

		try {
			String tmp = mapper.writeValueAsString(device);
			System.out.println(tmp);
			mapper.writeValue(new File("testDeviceAsString2JsonFile.json"), tmp);
			mapper.writeValue(new File("testDeviceAsObject2JsonFile.json"), device);
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
				.println("Files sucessfully created in the project root (refresh your project if you do not see it)!");
	}
}
