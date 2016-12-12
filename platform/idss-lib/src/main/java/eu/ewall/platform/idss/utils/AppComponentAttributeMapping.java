package eu.ewall.platform.idss.utils;

import eu.ewall.platform.idss.utils.exception.ParseException;

/**
 * An attribute mapping can be added to {@link AppComponents AppComponents}
 * to register custom attribute types. It is used when a component is built
 * from XML. If an attribute has a matching class, then the mapping will be
 * used to parse the string value of the attribute.
 *
 * @author Dennis Hofs (RRD)
 * @param <T> the attribute class
 */
public interface AppComponentAttributeMapping<T> {

	/**
	 * Returns the attribute class.
	 *
	 * @return the attribute class
	 */
	Class<T> getAttributeClass();

	/**
	 * Parses the string representation of the attribute value.
	 *
	 * @param value string representation of the attribute value
	 * @return the attribute value
	 * @throws ParseException if the string format is invalid
	 */
	T parseValue(String value) throws ParseException;
}
