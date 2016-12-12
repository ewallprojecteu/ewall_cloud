package eu.ewall.platform.idss.utils.validation;

import com.fasterxml.jackson.core.type.TypeReference;

import eu.ewall.platform.idss.utils.exception.ParseException;

import java.util.Map;

/**
 * This class can read values from a map with string keys and perform
 * validations and type conversions.
 * 
 * @author Dennis Hofs (RRD)
 */
public class MapReader {
	private Map<?,?> map;
	
	/**
	 * Constructs a new reader that will read from the specified map.
	 * 
	 * @param map the map
	 */
	public MapReader(Map<?,?> map) {
		this.map = map;
	}

	/**
	 * Reads a value as an object. If the key is not defined or the value is
	 * null, this method throws an exception.
	 * 
	 * @param key the key
	 * @return the value
	 * @throws ParseException if the key is not defined or the value is null
	 */
	public Object readObject(String key) throws ParseException {
		Object obj = map.get(key);
		if (obj == null) {
			throw new ParseException(String.format("Key \"%s\" not defined",
					key));
		}
		return obj;
	}
	
	/**
	 * Reads a value as an object. If the key is not defined or the value is
	 * null, this method returns the default value.
	 * 
	 * @param key the key
	 * @param defaultVal the default value (can be null)
	 * @return the value (can only be null if defaultVal is null)
	 */
	public Object readObject(String key, Object defaultVal) {
		Object obj = map.get(key);
		return obj == null ? defaultVal : obj;
	}

	/**
	 * Reads a value as a string. It calls toString() on any object. If the key
	 * is not defined or the value is null, this method throws an exception.
	 * 
	 * @param key the key
	 * @return the value
	 * @throws ParseException if the key is not defined or the value is null
	 */
	public String readString(String key) throws ParseException {
		return TypeConversion.getString(readObject(key));
	}
	
	/**
	 * Reads a value as a string. It calls toString() on any object. If the key
	 * is not defined or the value is null, this method returns the default
	 * value.
	 * 
	 * @param key they key
	 * @param defaultVal the default value (can be null)
	 * @return the value (can only be null if defaultVal is null)
	 */
	public String readString(String key, String defaultVal) {
		return TypeConversion.getString(readObject(key, defaultVal));
	}
	
	/**
	 * Reads a value as a string and validates the string length. It calls
	 * toString() on any object. If the key is not defined or the value is
	 * null, this method throws an exception.
	 * 
	 * @param key the key
	 * @param minLen the minimum length (&lt;= 0 if no minimum)
	 * @param maxLen the maximum length (&lt;= 0 if no maximum)
	 * @return the value
	 * @throws ParseException if the key is not defined or the value is null
	 * or the string length is invalid
	 */
	public String readStringLength(String key, int minLen, int maxLen)
			throws ParseException {
		try {
			return Validation.validateStringLength(readString(key), minLen,
					maxLen);
		} catch (ValidationException ex) {
			throw new ParseException(ex.getMessage(), ex);
		}
	}

	/**
	 * Reads a value as a string and validates the string length (if not null).
	 * It calls toString() on any object. If the key is not defined or the
	 * value is null, this method returns the default value.
	 * 
	 * @param key the key
	 * @param minLen the minimum length (&lt;= 0 if no minimum)
	 * @param maxLen the maximum length (&lt;= 0 if no maximum)
	 * @param defaultVal the default value (can be null)
	 * @return the value (can only be null if defaultVal is null)
	 * @throws ParseException if the value is not null and the string length is
	 * invalid
	 */
	public String readStringLength(String key, int minLen, int maxLen,
			String defaultVal) throws ParseException {
		String s = readString(key, defaultVal);
		if (s == null)
			return null;
		try {
			return Validation.validateStringLength(s, minLen, maxLen);
		} catch (ValidationException ex) {
			throw new ParseException(ex.getMessage(), ex);
		}
	}
	
	/**
	 * Reads a value as a string and validates it against a regular expression.
	 * It calls toString() on any object. If the key is not defined or the
	 * value is null, this method throws an exception.
	 * 
	 * @param key the key
	 * @param regex the regular expression
	 * @return the value
	 * @throws ParseException if the key is not defined or the value is null
	 * or it does not match the regular expression
	 */
	public String readStringRegex(String key, String regex)
			throws ParseException {
		try {
			return Validation.validateStringRegex(readString(key), regex);
		} catch (ValidationException ex) {
			throw new ParseException(ex.getMessage(), ex);
		}
	}
	
	/**
	 * Reads a value as a string and validates it against a regular expression
	 * (if not null). It calls toString() on any object. If the key is not
	 * defined or the value is null, this method returns the default value.
	 * 
	 * @param key the key
	 * @param regex the regular expression
	 * @param defaultVal the default value (can be null)
	 * @return the value (can only be null if defaultVal is null)
	 * @throws ParseException if the value is not null and it does not match
	 * the regular expression
	 */
	public String readStringRegex(String key, String regex, String defaultVal)
			throws ParseException {
		String s = readString(key, defaultVal);
		if (s == null)
			return null;
		try {
			return Validation.validateStringRegex(s, regex);
		} catch (ValidationException ex) {
			throw new ParseException(ex.getMessage(), ex);
		}
	}
	
	/**
	 * Reads a value as an integer. If the value is a Number, it will get the
	 * integer value. Otherwise it will call toString() and try to parse the
	 * string value. If the key is not defined or the value is null, this
	 * method throws an exception.
	 * 
	 * @param key the key
	 * @return the value
	 * @throws ParseException if the key is not defined, or the value is null,
	 * or it can't be converted to an integer
	 */
	public int readInt(String key) throws ParseException {
		return TypeConversion.getInt(readObject(key));
	}
	
	/**
	 * Reads a value as an integer. If the value is a Number, it will get the
	 * integer value. Otherwise it will call toString() and try to parse the
	 * string value. If the key is not defined or the value is null, this
	 * method returns the default value.
	 * 
	 * @param key the key
	 * @param defaultVal the default value (can be null)
	 * @return the value (can only be null if defaultVal is null)
	 * @throws ParseException if the value is not null and can't be converted
	 * to an integer
	 */
	public Integer readInt(String key, Integer defaultVal)
			throws ParseException {
		Object obj = readObject(key, defaultVal);
		if (obj == null)
			return null;
		return TypeConversion.getInt(obj);
	}
	
	/**
	 * Reads a value as an integer and validates that it's in the specified
	 * range. If the value is a Number, it will get the integer value.
	 * Otherwise it will call toString() and try to parse the string value.
	 * If the key is not defined or the value is null, this method throws an
	 * exception.
	 * 
	 * @param key the key
	 * @param min the minimum value (null if no minimum)
	 * @param max the maximum value (null if no maximum)
	 * @return the value
	 * @throws ParseException if the key is not defined, or the value is null,
	 * or it can't be converted to an integer, or it's not in the specified
	 * range
	 */
	public int readIntRange(String key, Integer min, Integer max)
			throws ParseException {
		try {
			return Validation.validateIntRange(readInt(key), min, max);
		} catch (ValidationException ex) {
			throw new ParseException(ex.getMessage(), ex);
		}
	}
	
	/**
	 * Reads a value as an integer and validates that it's in the specified
	 * range (if not null). If the value is a Number, it will get the integer
	 * value. Otherwise it will call toString() and try to parse the string
	 * value. If the key is not defined or the value is null, this method
	 * returns the default value.
	 * 
	 * @param key the key
	 * @param min the minimum value (null if no minimum)
	 * @param max the maximum value (null if no maximum)
	 * @param defaultVal the default value (can be null)
	 * @return the value (can only be null if defaultVal is null)
	 * @throws ParseException if the value is not null and it can't be
	 * converted to an integer or it's not in the specified range
	 */
	public Integer readIntRange(String key, Integer min, Integer max,
			Integer defaultVal) throws ParseException {
		Integer n = readInt(key, defaultVal);
		if (n == null)
			return null;
		try {
			return Validation.validateIntRange(n, min, max);
		} catch (ValidationException ex) {
			throw new ParseException(ex.getMessage(), ex);
		}
	}
	
	/**
	 * Reads a value as a long. If the value is a Number, it will get the long
	 * value. Otherwise it will call toString() and try to parse the string
	 * value. If the key is not defined or the value is null, this method
	 * throws an exception.
	 * 
	 * @param key the key
	 * @return the value
	 * @throws ParseException if the key is not defined, or the value is null,
	 * or it can't be converted to a long
	 */
	public long readLong(String key) throws ParseException {
		return TypeConversion.getLong(readObject(key));
	}
	
	/**
	 * Reads a value as a long. If the value is a Number, it will get the long
	 * value. Otherwise it will call toString() and try to parse the string
	 * value. If the key is not defined or the value is null, this method
	 * returns the default value.
	 * 
	 * @param key the key
	 * @param defaultVal the default value (can be null)
	 * @return the value (can only be null if defaultVal is null)
	 * @throws ParseException if the value is not null and can't be converted
	 * to a long
	 */
	public Long readLong(String key, Long defaultVal)
			throws ParseException {
		Object obj = readObject(key, defaultVal);
		if (obj == null)
			return null;
		return TypeConversion.getLong(obj);
	}
	
	/**
	 * Reads a value as a long and validates that it's in the specified range.
	 * If the value is a Number, it will get the long value. Otherwise it will
	 * call toString() and try to parse the string value. If the key is not
	 * defined or the value is null, this method throws an exception.
	 * 
	 * @param key the key
	 * @param min the minimum value (null if no minimum)
	 * @param max the maximum value (null if no maximum)
	 * @return the value
	 * @throws ParseException if the key is not defined, or the value is null,
	 * or it can't be converted to a long, or it's not in the specified range
	 */
	public long readLongRange(String key, Long min, Long max)
			throws ParseException {
		try {
			return Validation.validateLongRange(readLong(key), min, max);
		} catch (ValidationException ex) {
			throw new ParseException(ex.getMessage(), ex);
		}
	}
	
	/**
	 * Reads a value as a long and validates that it's in the specified range
	 * (if not null). If the value is a Number, it will get the long value.
	 * Otherwise it will call toString() and try to parse the string value. If
	 * the key is not defined or the value is null, this method returns the
	 * default value.
	 * 
	 * @param key the key
	 * @param min the minimum value (null if no minimum)
	 * @param max the maximum value (null if no maximum)
	 * @param defaultVal the default value (can be null)
	 * @return the value (can only be null if defaultVal is null)
	 * @throws ParseException if the value is not null and it can't be
	 * converted to a long or it's not in the specified range
	 */
	public Long readLongRange(String key, Long min, Long max, Long defaultVal)
			throws ParseException {
		Long n = readLong(key, defaultVal);
		if (n == null)
			return null;
		try {
			return Validation.validateLongRange(n, min, max);
		} catch (ValidationException ex) {
			throw new ParseException(ex.getMessage(), ex);
		}
	}
	
	/**
	 * Reads a value and converts it to the specified class using the Jackson
	 * ObjectMapper. For example a Map could be converted to an object. This
	 * method does not parse JSON strings. If the key is not defined or the
	 * value is null, this method throws an exception.
	 * 
	 * @param key the key
	 * @param clazz the return type
	 * @return the value
	 * @throws ParseException if the key is not defined, or the value is null,
	 * or it can't be converted to the specified type
	 * @param <T> the return type
	 */
	public <T> T readJson(String key, Class<T> clazz) throws ParseException {
		return TypeConversion.getJson(readObject(key), clazz);
	}
	
	/**
	 * Reads a value and converts it to the specified class using the Jackson
	 * ObjectMapper. For example a Map could be converted to an object. This
	 * method does not parse JSON strings. If the key is not defined or the
	 * value is null, this method returns the default value.
	 * 
	 * @param key the key
	 * @param clazz the return type
	 * @param defaultVal the default value (can be null)
	 * @return the value (can only be null if defaultVal is null)
	 * @throws ParseException if the value is not null and can't be converted
	 * to the specified type
	 * @param <T> the return type
	 */
	public <T> T readJson(String key, Class<T> clazz, T defaultVal)
			throws ParseException {
		return TypeConversion.getJson(readObject(key, defaultVal), clazz);
	}
	
	/**
	 * Reads a value and converts it to the specified type using the Jackson
	 * ObjectMapper. For example a Map could be converted to an object. This
	 * method does not parse JSON strings. If the key is not defined or the
	 * value is null, this method throws an exception.
	 * 
	 * <p>If you want to convert to MyObject, you can specify:<br />
	 * new TypeReference&lt;MyObject&gt;() {}</p>
	 * 
	 * @param key the key
	 * @param typeRef the return type
	 * @return the value
	 * @throws ParseException if the key is not defined, or the value is null,
	 * or it can't be converted to the specified type
	 * @param <T> the return type
	 */
	public <T> T readJson(String key, TypeReference<T> typeRef)
			throws ParseException {
		return TypeConversion.getJson(readObject(key), typeRef);
	}
	
	/**
	 * Reads a value and converts it to the specified type using the Jackson
	 * ObjectMapper. For example a Map could be converted to an object. This
	 * method does not parse JSON strings. If the key is not defined or the
	 * value is null, this method returns the default value.
	 * 
	 * <p>If you want to convert to MyObject, you can specify:<br />
	 * new TypeReference&lt;MyObject&gt;() {}</p>
	 * 
	 * @param key the key
	 * @param typeRef the return type
	 * @param defaultVal the default value (can be null)
	 * @return the value (can only be null if defaultVal is null)
	 * @throws ParseException if the value is not null and can't be converted
	 * to the specified type
	 * @param <T> the return type
	 */
	public <T> T readJson(String key, TypeReference<T> typeRef, T defaultVal)
			throws ParseException {
		return TypeConversion.getJson(readObject(key, defaultVal), typeRef);
	}
}
