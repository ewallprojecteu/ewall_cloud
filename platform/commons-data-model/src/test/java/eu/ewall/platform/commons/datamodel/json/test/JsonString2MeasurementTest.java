/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.json.test;

import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import eu.ewall.platform.commons.datamodel.location.IndoorPlace;
import eu.ewall.platform.commons.datamodel.location.IndoorPlaceArea;
import eu.ewall.platform.commons.datamodel.marshalling.json.DM2JsonObjectMapper;
import eu.ewall.platform.commons.datamodel.measure.ConstantQuantityMeasureType;
import eu.ewall.platform.commons.datamodel.measure.DetailedMeasurement;
import eu.ewall.platform.commons.datamodel.measure.Measurement;

/**
 * The Class JsonString2MeasurementTest.
 */
public class JsonString2MeasurementTest {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		try {

			Measurement measurement = new DetailedMeasurement("22.5", -30, 50,
					ConstantQuantityMeasureType.TEMPERATURE_MEASURE, 1, 0.5,
					new IndoorPlace("indoorPlaceName",
							IndoorPlaceArea.BEDROOM, System
									.currentTimeMillis()),
					System.currentTimeMillis(),
					"some description of the measurement");

			String measurementString = DM2JsonObjectMapper
					.writeValueAsString(measurement);

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);

			// enable pretty output
			// mapper.enable(SerializationFeature.INDENT_OUTPUT);

			// String measurementString =
			// mapper.writeValueAsString(measurement);

			System.out.println(measurementString);

			ObjectMapper mapper2 = new ObjectMapper();

			mapper2.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);

			mapper2.configure(
					DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			Measurement measurement2 = mapper2.readValue(measurementString,
					Measurement.class);
			System.out.println(measurement2);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
