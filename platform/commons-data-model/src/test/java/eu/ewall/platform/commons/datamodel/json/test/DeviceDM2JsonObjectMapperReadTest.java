/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.json.test;

import eu.ewall.platform.commons.datamodel.device.Device;
import eu.ewall.platform.commons.datamodel.marshalling.json.DM2JsonObjectMapper;

/**
 * The Class UserWithoutProfile2JsonFileReadTest.
 *
 * @author eandgrg
 */
public class DeviceDM2JsonObjectMapperReadTest {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		Object obtainedObjectFromString;
		obtainedObjectFromString = DM2JsonObjectMapper
				.readValuesAsStringToSet(
						"{\r\n  \"HashSet\" : [ {\r\n    \"deviceId\" : \"00000000-0000-0000-0000-000000000000\",\r\n    \"serialNumber\" : 123.0,\r\n    \"manufacturer\" : {\r\n      \"name\" : \"manName\",\r\n      \"description\" : \"manDescription\",\r\n      \"links\" : [ ]\r\n    },\r\n    \"sensorsList\" : [ ],\r\n    \"actuatorsList\" : [ ],\r\n    \"measurementsList\" : [ {\r\n      \"measurementId\" : null,\r\n      \"accuracyDegree\" : 12.0,\r\n      \"absoluteError\" : 12.0,\r\n      \"constantQuantityMeasureType\" : \"LENGTH_MEASURE\",\r\n      \"maxValue\" : 20.0,\r\n      \"measuredValue\" : 12.0,\r\n      \"minValue\" : 4.0,\r\n      \"onLocation\" : {\r\n        \"timestamp\" : 1411052042175,\r\n        \"name\" : \"somename\",\r\n        \"indoorPlaceArea\" : \"BATH_ROOM\",\r\n        \"links\" : [ ]\r\n      },\r\n      \"timestamp\" : 1411052042175,\r\n      \"description\" : \"somedescription\",\r\n      \"links\" : [ ]\r\n    } ],\r\n    \"wearable\" : true,\r\n    \"links\" : [ ]\r\n  } ]\r\n}",
						Device.class);

		System.out.println("obtainedObjectFromString is: \n"
				+ obtainedObjectFromString);

	}
}
