/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.test;

import eu.ewall.platform.commons.datamodel.measure.TemperatureMeasurement;

/**
 * The Class TemperatureMeasurementTest.
 * 
 * To compare the test result check the conversions online, e.g. <a href=
 * "http://www.metric-conversions.org/temperature/celsius-to-fahrenheit.htm"
 * >http://www.metric-conversions.org/temperature/celsius-to-fahrenheit.htm</a>
 * 
 * 
 * @author eandgrg
 */
public class TemperatureMeasurementTest {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		TemperatureMeasurement temperatureMeasurement = new TemperatureMeasurement(
				10, System.currentTimeMillis());

		System.out.println("temp in celsius: "
				+ temperatureMeasurement.getMeasuredValueInCelsius());
		System.out.println("temp in fahrenheit: "
				+ temperatureMeasurement.getMeasuredValueInFahrenheit());
	}

}