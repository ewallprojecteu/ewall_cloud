/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.profile.preferences;

/**
 * The Enum UnitSystem.
 * 
 * There are two main systems for measuring distances and weight, the Imperial
 * System of Measurement and the Metric System of Measurement. Most countries
 * use the Metric System, which uses the measuring units such as meters and
 * grams and adds prefixes like kilo, milli and centi to count orders of
 * magnitude. In the United States, older Imperial system, where things are
 * measured in feet, inches and pounds is used.
 *
 * @author eandgrg
 */
public enum UnitSystem {

    /**
     * The metric system uses the following base units: length (meter, m), mass
     * (gram, g), volume (liter, L)
     */
    METRIC,
    /**
     * The imperial also known as British Imperial. because it came from the
     * British Empire that ruled many parts of the world from the 16th to the
     * 19th century. After the U.S gained independence from Britain, the new
     * American government decided to keep this type of measurement, even though
     * the metric system was gaining in popularity at the time
     */
    IMPERIAL,
    /**
     * The metric-based Système International or SI units is a standard system
     * uses the following 7 base units: length (meter, m), mass (kilogram, kg),
     * temperature (kelvin, K), time (second, s), amount of substance (mole,
     * mol), electric current (ampere, A), luminous intensity (candela, cd)
     */
    SI_STANDARD

}
