/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.measure;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Temperature measurement containing temperature in celsius values. 
 * Contains methods for retrival of values to/from fahrenheit scale.
 * 
 * @author eandgrg, emirmos
 */
public class TemperatureMeasurement extends IndoorMeasurement {

	/**
	 * Instantiates an empty {@code TemperatureMeasurement}.
	 */
	public TemperatureMeasurement() {
		this.constantQuantityMeasureType = ConstantQuantityMeasureType.TEMPERATURE_MEASURE_CELSIUS;
	}


	/**
	 * Instantiates a new temperature measurement.
	 *
	 * @param measuredValueInCelsius the measured value in celsius
	 * @param timestamp the timestamp
	 * @param indoorPlaceName the indoor place name
	 */
	public TemperatureMeasurement(double measuredValueInCelsius,
			long timestamp, String indoorPlaceName) {
		super(String.valueOf(measuredValueInCelsius), ConstantQuantityMeasureType.TEMPERATURE_MEASURE_CELSIUS, timestamp, indoorPlaceName);
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * Instantiates a new temperature measurement.
	 *
	 * @param measuredValueInCelsius the measured value in celsius
	 * @param timestamp the timestamp
	 */
	public TemperatureMeasurement(double measuredValueInCelsius,
			long timestamp) {
		super(String.valueOf(measuredValueInCelsius), ConstantQuantityMeasureType.TEMPERATURE_MEASURE_CELSIUS, timestamp);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new temperature measurement. The value in the
	 * {@link IndoorMeasurement} should be given in degrees Celcius.
	 *
	 * @param indoorEnvironmentalMeasurement
	 *            the simple measurement containing measured value in celsius
	 */
	public TemperatureMeasurement(IndoorMeasurement indoorEnvironmentalMeasurement) {
		this.measuredValue= indoorEnvironmentalMeasurement
				.getMeasuredValue();
		this.timestamp = indoorEnvironmentalMeasurement.getTimestamp();
		this.indoorPlaceName = indoorEnvironmentalMeasurement.getIndoorPlaceName();
		this.constantQuantityMeasureType = ConstantQuantityMeasureType.TEMPERATURE_MEASURE_CELSIUS;
		//TODO check that ConstantQuantityMeasureType is TEMPERATURE_MEASURE_CELSIUS or null

	}

	

	/**
	 * Gets the measured value in celsius.
	 * 
	 * @return the temperature in degrees Celcius.
	 */
	@JsonIgnore
	public double getMeasuredValueInCelsius() {
		double measuredValueDouble = Double.NaN;
		try {
				measuredValueDouble = Double.valueOf(measuredValue);
			} catch (NumberFormatException e) {
				//TODO add logger.warn message
				//e.printStackTrace();
			}
		
		return measuredValueDouble;
	}

	/**
	 * Sets the measured value in celsius.
	 *
	 * @param measuredValueInCelsius
	 *            the measuredValueInCelsius to set
	 */
	public void setMeasuredValueInCelsius(double measuredValueInCelsius) {
		this.measuredValue = String.valueOf(measuredValueInCelsius);
	}

	/**
	 * Gets the measured value in fahrenheit.
	 *
	 * @return the measuredValueInFahrenheit
	 */
	@JsonIgnore
	public double getMeasuredValueInFahrenheit() {
		double measuredValueInCelsius = this.getMeasuredValueInCelsius();
		return celsiusToFahrenheit(measuredValueInCelsius);
	}

	/**
	 * Sets the measured value in fahrenheits.
	 *
	 * @param measuredValueInFahrenheit
	 *            the measuredValueInFahrenheit to set
	 */
	public void setMeasuredValueInFahrenheit(double measuredValueInFahrenheit) {
		double measuredValueInCelsius = fahrenheitToCelsius(measuredValueInFahrenheit);
		this.setMeasuredValueInCelsius(measuredValueInCelsius);
	}
	
	
	/**
	 * Fahrenheit to celsius conversion.
	 *
	 * @param valueInFahrenheit
	 *            the value in fahrenheit
	 * @return the double
	 */
	private double fahrenheitToCelsius(double valueInFahrenheit) {
		return (((valueInFahrenheit - 32) * 5) / 9);
	}

	/**
	 * Celsius to fahrenheit conversion.
	 *
	 * @param valueInCelsius
	 *            the value in celsius
	 * @return the double
	 */
	private double celsiusToFahrenheit(double valueInCelsius) {
		return ((9 * valueInCelsius / 5) + 32);
	}

}
