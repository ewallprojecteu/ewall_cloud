/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.json.test;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import eu.ewall.platform.commons.datamodel.activity.ActivityType;
import eu.ewall.platform.commons.datamodel.core.ieeesumo.Manufacturer;
import eu.ewall.platform.commons.datamodel.device.Device;
import eu.ewall.platform.commons.datamodel.device.DeviceType;
import eu.ewall.platform.commons.datamodel.measure.AccelerometerMeasurement;

/**
 * The Class Measurement2JsonTest.
 *
 * @author eandgrg
 */
public class DeviceWithAccelerometerMeasurements2JsonTest {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		AccelerometerMeasurement measurement1 = new AccelerometerMeasurement(
				11.5, 9.8, 55, ActivityType.WALKING,
				System.currentTimeMillis());

		AccelerometerMeasurement measurement2 = new AccelerometerMeasurement(
				10.5, 4.5, 56, ActivityType.WALKING,
				System.currentTimeMillis());

		Manufacturer manufacturer = new Manufacturer(
				"accelerometer manufacturer", "good manufacturer");

		Device ewallDevice =  new Device("testDeviceName", DeviceType.ENVIRONMENTAL_SENSOR, "livingroom", true, false, "0000");;

		

		ObjectMapper mapper = new ObjectMapper();

		// enable pretty output
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		try {
			System.out.println(mapper.writeValueAsString(ewallDevice));
		} catch (JsonGenerationException e) {

			e.printStackTrace();

		} catch (JsonMappingException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}
}
