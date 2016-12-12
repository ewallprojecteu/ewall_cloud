package eu.ewall.platform.idss.utils.validation;

import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.ewall.platform.idss.utils.exception.ParseException;

/**
 * This class can perform various type conversions.
 * 
 * @author Dennis Hofs (RRD)
 */
public class TypeConversion {

	/**
	 * Converts the specified object to a string. If the object is null, this
	 * method returns null. Otherwise it will call toString().
	 * 
	 * @param obj the object or null
	 * @return the string or null
	 */
	public static String getString(Object obj) {
		return obj == null ? null : obj.toString();
	}

	/**
	 * Converts the specified object to an integer. If the object is null, this
	 * method returns null. If it's a Number, it will return the integer value.
	 * Otherwise it will call toString() and try to parse the string value.
	 * 
	 * @param obj the object or null
	 * @return the integer or null
	 * @throws ParseException if a string value can't be parsed as an integer
	 */
	public static Integer getInt(Object obj) throws ParseException {
		if (obj == null)
			return null;
		if (obj instanceof Number)
			return ((Number)obj).intValue();
		String s = obj.toString();
		try {
			return new Integer(s);
		} catch (NumberFormatException ex) {
			throw new ParseException(String.format("Invalid integer: %s", s));
		}
	}

	/**
	 * Converts the specified object to a long. If the object is null, this
	 * method returns null. If it's a Number, it will return the long value.
	 * Otherwise it will call toString() and try to parse the string value.
	 * 
	 * @param obj the object or null
	 * @return the long or null
	 * @throws ParseException if a string value can't be parsed as a long
	 */
	public static Long getLong(Object obj) throws ParseException {
		if (obj == null)
			return null;
		if (obj instanceof Number)
			return ((Number)obj).longValue();
		String s = obj.toString();
		try {
			return new Long(s);
		} catch (NumberFormatException ex) {
			throw new ParseException(String.format("Invalid long: %s", s));
		}
	}
	
	/**
	 * Converts an object to the specified type using the Jackson ObjectMapper.
	 * For example a Map could be converted to an object. This method does not
	 * parse JSON strings. If the object is null, this method returns null.
	 * 
	 * @param obj the object or null
	 * @param clazz the return type
	 * @return the converted object or null
	 * @throws ParseException if the object can't be converted to the specified
	 * type
	 * @param <T> the return type
	 */
	public static <T> T getJson(Object obj, Class<T> clazz)
			throws ParseException {
		if (obj == null)
			return null;
		if (clazz.isInstance(obj))
			return clazz.cast(obj);
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.convertValue(obj, clazz);
		} catch (Exception ex) {
			throw new ParseException("Invalid JSON content: " +
					ex.getMessage(), ex);
		}
	}

	/**
	 * Converts an object to the specified type using the Jackson ObjectMapper.
	 * For example a Map could be converted to an object. This method does not
	 * parse JSON strings. If the object is null, this method returns null.
	 * 
	 * <p>If you want to convert to MyObject, you can specify:<br />
	 * new TypeReference&lt;MyObject&gt;() {}</p>
	 * 
	 * @param obj the object or null
	 * @param typeRef the return type
	 * @return the converted object or null
	 * @throws ParseException if the object can't be converted to the specified
	 * type
	 * @param <T> the return type
	 */
	public static <T> T getJson(Object obj, TypeReference<T> typeRef)
			throws ParseException {
		if (obj == null)
			return null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.convertValue(obj, typeRef);
		} catch (Exception ex) {
			throw new ParseException("Invalid JSON content: " +
					ex.getMessage(), ex);
		}
	}
}
