/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.json.test;

import java.util.ArrayList;

import eu.ewall.platform.commons.datamodel.device.Device;
import eu.ewall.platform.commons.datamodel.marshalling.json.DM2JsonObjectMapper;

/**
 * The Class UserWithoutProfile2JsonFileReadTest.
 *
 * @author eandgrg
 */
public class DevicesDM2JsonObjectMapperReadTest {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		ArrayList<Object> obtainedObjectsSet;
		obtainedObjectsSet = DM2JsonObjectMapper
				.readValuesAsStringToArrayList(
						"{\r\n  \"ArrayList\" : [ {\r\n    \"deviceId\" : \"00000000-0000-0000-0000-000000000000\",\r\n    \"serialNumber\" : 345.0,\r\n    \"manufacturer\" : {\r\n      \"name\" : \"manName\",\r\n      \"description\" : \"manDescription\",\r\n      \"links\" : [ ]\r\n    },\r\n    \"sensorsList\" : [ ],\r\n    \"actuatorsList\" : [ ],\r\n    \"measurementsList\" : [ ],\r\n    \"wearable\" : true,\r\n    \"links\" : [ ]\r\n  }, {\r\n    \"deviceId\" : \"00000000-0000-0000-0000-000000000001\",\r\n    \"serialNumber\" : 345.0,\r\n    \"manufacturer\" : {\r\n      \"name\" : \"manName\",\r\n      \"description\" : \"manDescription\",\r\n      \"links\" : [ ]\r\n    },\r\n    \"sensorsList\" : [ ],\r\n    \"actuatorsList\" : [ ],\r\n    \"measurementsList\" : [ ],\r\n    \"wearable\" : false,\r\n    \"links\" : [ ]\r\n  } ]\r\n}",
						Device.class);

		System.out.println("obtainedObjectsFromString are: \n"
				+ obtainedObjectsSet.size());

	}
}
