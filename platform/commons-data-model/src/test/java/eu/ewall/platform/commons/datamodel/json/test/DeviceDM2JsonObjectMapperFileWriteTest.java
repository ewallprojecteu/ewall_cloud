/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.json.test;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.ewall.platform.commons.datamodel.core.ieeesumo.Manufacturer;
import eu.ewall.platform.commons.datamodel.device.Device;
import eu.ewall.platform.commons.datamodel.device.DeviceType;
import eu.ewall.platform.commons.datamodel.marshalling.json.DM2JsonObjectMapper;

/**
 * The Class Device2JsonTest.
 *
 * @author eandgrg
 */
public class DeviceDM2JsonObjectMapperFileWriteTest {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		Manufacturer manufacturer = new Manufacturer("manName",
				"manDescription");
		Device ewallDevice =  new Device("testDeviceName", DeviceType.ENVIRONMENTAL_SENSOR, "livingroom", true, false, "0000");;
		

		ObjectMapper mapper = new ObjectMapper();

		try {
			String tmp = DM2JsonObjectMapper.writeValueAsString(ewallDevice);
			System.out.println(tmp);
			mapper.writeValue(new File(
					"Device2JsonWithDM2JsonObjectMapperFile.json"), tmp);
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
				.println("File sucessfully created in the project root (refresh your project if you do not see it)!");
	}

}
