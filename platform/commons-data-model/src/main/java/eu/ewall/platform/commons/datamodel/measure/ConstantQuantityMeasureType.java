/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.measure;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 *******************************************************************************/

/**
 * ConstantQuantityMeasure Types
 * 
 * @author eandgrg
 */
public enum ConstantQuantityMeasureType {
	/**
	 * Measures of the amount of space in two dimensions.
	 */
	AREA_MEASURE,

	/** accelerometer measure. */
	ACCELEROMETER_MEASURE,
	/** The humidity measure. */
	BED_PRESSURE,

	/** The humidity measure. */
	HUMIDITY_MEASURE,
	/**
	 * Includes all standard measures of monetary value, including
	 * UnitedStatesDollar, UnitedStatesCent, Lire, Yen, etc.
	 */
	CURRENCY_MEASURE,
	/** The length measure. */
	LENGTH_MEASURE,
	/**
	 * Measures of the amount of information. Includes Bit, Byte, and multiples
	 * of these, e.g. KiloByte and MegaByte.
	 */
	INFORMATION_MEASURE,
	/**
	 * Luminous flux or luminous power is the measure of the perceived power of
	 * light. It differs from radiant flux, the measure of the total power of
	 * electromagnetic radiation (including infrared, ultraviolet, and visible
	 * light), in that luminous flux is adjusted to reflect the varying
	 * sensitivity of the human eye to different wavelengths of light.
	 */
	LUMINOUS_FLUX_MEASURE,
	
	/** The measure of illuminance. */
	ILLUMINANCE_MEASURE,
	
	/** The mass measure. */
	MASS_MEASURE,

	/** The movement measure. */
	MOVEMENT_MEASURE,
	/** The precipitation measure. */
	PRECIPITATION_MEASURE,
	/** The pressure measure. */
	PRESSURE_MEASURE,
	/** The temperature measure. */
	TEMPERATURE_MEASURE,

	/** The temperature measure in celsius. */
	TEMPERATURE_MEASURE_CELSIUS,

	/** The temperature measure in fahrenheit. */
	TEMPERATURE_MEASURE_FAHRENHEIT,

	/** The time duration. */
	TIME_DURATION,
	/**
	 * Streamflow, or channel runoff, is the flow of water in streams, rivers,
	 * and other channels, and is a major element of the water cycle. .
	 */
	STREAMFLOW_MEASURE,
	/**
	 * Measures of the amount of space in three dimensions.
	 */
	VOLUME_MEASURE, CARBON_MONOXIDE_MEASUREMENT, LIQUIFIED_PETROLEUM_GAS_MEASUREMENT, NATURAL_GAS_MEASUREMENT, DOOR_STATUS_OPEN, MATTRESS_PRESSURE_SENSING, HEART_RATE, BLOOD_PRESSURE, OXYGEN_SATURATION,
}