package eu.ewall.platform.idss.utils.xml;

import eu.ewall.platform.idss.utils.exception.ParseException;

/**
 * Interface for classes that can parse string values from an XML document and
 * convert them to Java objects.
 * 
 * @author Dennis Hofs (RRD)
 *
 * @param <T> the class of Java object
 */
public interface XMLValueParser<T> {
	
	/**
	 * Parses the specified string value.
	 * 
	 * @param xml the string value
	 * @return the Java object
	 * @throws ParseException if the string value is invalid
	 */
	public T parse(String xml) throws ParseException;
}
