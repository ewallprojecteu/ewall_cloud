/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.json.test;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import eu.ewall.platform.commons.datamodel.location.IndoorPlace;
import eu.ewall.platform.commons.datamodel.location.IndoorPlaceArea;
import eu.ewall.platform.commons.datamodel.measure.ConstantQuantityMeasureType;
import eu.ewall.platform.commons.datamodel.measure.DetailedMeasurement;
import eu.ewall.platform.commons.datamodel.measure.Measurement;

/**
 * The Class Measurement2JsonTest.
 *
 * @author eandgrg
 */
public class Measurement2JsonTest {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		Measurement measurement = new DetailedMeasurement("22.5", -30, 50,
				ConstantQuantityMeasureType.TEMPERATURE_MEASURE, 1, 0.5,
				new IndoorPlace("indoorPlaceName",
						IndoorPlaceArea.BEDROOM, System
								.currentTimeMillis()),
				System.currentTimeMillis(),
				"some description of the measurement");

		ObjectMapper mapper = new ObjectMapper();

		// enable pretty output
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		try {

			System.out.println(mapper.writeValueAsString(measurement));

		} catch (JsonGenerationException e) {

			e.printStackTrace();

		} catch (JsonMappingException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}
}
