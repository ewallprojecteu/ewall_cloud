package eu.ewall.platform.idss.dao;

import eu.ewall.platform.idss.utils.ReflectionUtils;

import eu.ewall.platform.idss.utils.beans.PropertyReader;

import eu.ewall.platform.idss.utils.datetime.DateTimeUtils;

import eu.ewall.platform.idss.utils.exception.ParseException;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * This class can map between {@link DatabaseObject DatabaseObject}s and data
 * maps for database storage.
 * 
 * <p>First it maps the ID field of the database object to key "id" with a
 * string value. Then it scans the Java class for fields with annotation {@link
 * DatabaseField DatabaseField}, which specifies a database type. A field
 * should be public or the class should have a public get and set method for
 * the field. A boolean field may have an is... method instead of a get...
 * method. The data map has keys that correspond to the field names. The values
 * should only be numbers or strings. The following table describes how the
 * Java object fields are converted from and to map values.</p>
 * 
 * <style type="text/css">
 *   table.bordered {
 *     border-collapse: collapse;
 *   }
 *   table.bordered th, table.bordered td {
 *     text-align: left;
 *     border: 1px solid black;
 *     padding: 4px 8px;
 *     vertical-align: top;
 *   }
 * </style>
 * <p><table class="bordered">
 * <caption>Database types</caption>
 * <tbody><tr>
 * <th>Database type</th>
 * <th>Java field type</th>
 * <th>From map value</th>
 * <th>To map value</th>
 * </tr><tr>
 * <td>{@link DatabaseType#BYTE BYTE}</td>
 * <td>boolean (false = 0, true = 1), byte, short, int, long,
 * {@link Boolean Boolean}, {@link Byte Byte}, {@link Short Short},
 * {@link Integer Integer}, {@link Long Long}</td>
 * <td>{@link Byte Byte}, {@link Short Short}, {@link Integer Integer},
 * {@link Long Long}, {@link Float Float}, {@link Double Double}</td>
 * <td>{@link Byte Byte}</td>
 * </tr><tr>
 * <td>{@link DatabaseType#SHORT SHORT}</td>
 * <td>boolean (false = 0, true = 1), byte, short, int, long,
 * {@link Boolean Boolean}, {@link Byte Byte}, {@link Short Short},
 * {@link Integer Integer}, {@link Long Long}</td>
 * <td>{@link Byte Byte}, {@link Short Short}, {@link Integer Integer},
 * {@link Long Long}, {@link Float Float}, {@link Double Double}</td>
 * <td>{@link Byte Byte}, {@link Short Short}</td>
 * </tr><tr>
 * <td>{@link DatabaseType#INT INT}</td>
 * <td>boolean (false = 0, true = 1), byte, short, int, long,
 * {@link Boolean Boolean}, {@link Byte Byte}, {@link Short Short},
 * {@link Integer Integer}, {@link Long Long}</td>
 * <td>{@link Byte Byte}, {@link Short Short}, {@link Integer Integer},
 * {@link Long Long}, {@link Float Float}, {@link Double Double}</td>
 * <td>{@link Byte Byte}, {@link Short Short}, {@link Integer Integer}</td>
 * </tr><tr>
 * <td>{@link DatabaseType#LONG LONG}</td>
 * <td>boolean (false = 0, true = 1), byte, short, int, long,
 * {@link Boolean Boolean}, {@link Byte Byte}, {@link Short Short},
 * {@link Integer Integer}, {@link Long Long}</td>
 * <td>{@link Byte Byte}, {@link Short Short}, {@link Integer Integer},
 * {@link Long Long}, {@link Float Float}, {@link Double Double}</td>
 * <td>{@link Byte Byte}, {@link Short Short}, {@link Integer Integer},
 * {@link Long Long}</td>
 * </tr><tr>
 * <td>{@link DatabaseType#FLOAT FLOAT}</td>
 * <td>float, double, {@link Float Float}, {@link Double Double}</td>
 * <td>{@link Byte Byte}, {@link Short Short}, {@link Integer Integer},
 * {@link Long Long}, {@link Float Float}, {@link Double Double}</td>
 * <td>{@link Float Float}</td>
 * </tr><tr>
 * <td>{@link DatabaseType#DOUBLE DOUBLE}</td>
 * <td>float, double, {@link Float Float}, {@link Double Double}</td>
 * <td>{@link Byte Byte}, {@link Short Short}, {@link Integer Integer},
 * {@link Long Long}, {@link Float Float}, {@link Double Double}</td>
 * <td>{@link Float Float}, {@link Double Double}</td>
 * </tr><tr>
 * <td>{@link DatabaseType#STRING STRING} (does not support utf8mb4 characters
 * such as emoji)</td>
 * <td>{@link String String}, enum</td>
 * <td>{@link String String}</td>
 * <td>{@link String String}</td>
 * </tr><tr>
 * <td>{@link DatabaseType#TEXT TEXT} (does not support utf8mb4 characters such
 * as emoji)</td>
 * <td>{@link String String}</td>
 * <td>{@link String String}</td>
 * <td>{@link String String}</td>
 * </tr><tr>
 * <td>{@link DatabaseType#TEXT_MB4 TEXT_MB4} (supports utf8mb4 characters such
 * as emoji)</td>
 * <td>{@link String String}</td>
 * <td>{@link String String}</td>
 * <td>{@link String String}</td>
 * </tr><tr>
 * <td>{@link DatabaseType#DATE DATE}</td>
 * <td>{@link LocalDate LocalDate}</td>
 * <td>{@link String String} in format yyyy-MM-dd</td>
 * <td>{@link String String} in format yyyy-MM-dd</td>
 * </tr><tr>
 * <td>{@link DatabaseType#TIME TIME}</td>
 * <td>{@link LocalTime LocalTime}</td>
 * <td>{@link String String} in format HH:mm:ss</td>
 * <td>{@link String String} in format HH:mm:ss (milliseconds are not
 * stored)</td>
 * </tr><tr>
 * <td>{@link DatabaseType#DATETIME DATETIME}</td>
 * <td>{@link LocalDateTime LocalDateTime}</td>
 * <td>{@link String String} in format yyyy-MM-dd HH:mm:ss</td>
 * <td>{@link String String} in format yyyy-MM-dd HH:mm:ss (milliseconds are
 * not stored)</td>
 * </tr><tr>
 * <td>{@link DatabaseType#ISOTIME ISOTIME}</td>
 * <td>{@link DateTime DateTime} (preferred), {@link Calendar Calendar}
 * (preferred), long (time in milliseconds), {@link Long Long}, {@link Date
 * Date}, {@link Instant Instant}</td>
 * <td>{@link String String} in format yyyy-MM-dd'T'HH:mm:ss.SSSZ (for long,
 * Long, Date and Instant, the time is translated to UTC and the time zone is
 * lost)</td>
 * <td>{@link String String} in format yyyy-MM-dd'T'HH:mm:ss.SSSZ (long, Long,
 * Date and Instant, which don't specify a timezone, are translated to the
 * default time zone)</td>
 * </tr><tr>
 * <td>{@link DatabaseType#LIST LIST}</td>
 * <td>array or {@link List List} of elements with a valid type for the
 * database field's element type, for example int[] or List&lt;Integer&gt;,
 * value cannot be null</td>
 * <td>array or {@link List List} of elements with a valid type for the
 * database field's element type, for example int[] or List&lt;Integer&gt;</td>
 * <td>{@link List List} of elements with the type for the database field's
 * element type, for example List&lt;Integer&gt;</td>
 * </tr><tr>
 * <td>{@link DatabaseType#MAP MAP}</td>
 * <td>{@link Map Map&lt;String,?&gt;} where the keys are strings and the
 * values are a valid type for the database field's element type; value and
 * keys cannot be null</td>
 * <td>{@link Map Map&lt;String,?&gt;} where the keys are strings and the values
 * are a valid type for the database field's element type</td>
 * <td>{@link LinkedHashMap LinkedHashMap&lt;String,?&gt;} where the keys are
 * strings and the values have the type for the database field's element
 * type</td>
 * </tr><tr>
 * <td>{@link DatabaseType#OBJECT OBJECT}</td>
 * <td>{@link DatabaseObject DatabaseObject} (id is null)</td>
 * <td>{@link Map Map} where the keys are the names of the database fields in
 * the object (excluding id) and the values are valid values for the type of
 * database field</td>
 * <td>{@link LinkedHashMap LinkedHashMap} where the keys are the names of the
 * database fields in the object (excluding id) and the values are valid values
 * for the type of database field</td>
 * </tr><tr>
 * <td>{@link DatabaseType#OBJECT_LIST OBJECT_LIST}</td>
 * <td>array or {@link List List} of {@link DatabaseObject DatabaseObject}
 * (each object has id null), value cannot be null</td>
 * <td>array or {@link List List} of {@link Map Map}s where the keys are the
 * names of the database fields in the object (excluding id) and the values
 * are valid values for the type of database field</td>
 * <td>{@link List List} of {@link LinkedHashMap LinkedHashMap}s where the keys
 * are the names of the database fields in the object (excluding id) and the
 * values are valid values for the type of database field</td>
 * </tr></tbody></table></p>
 * 
 * @author Dennis Hofs (RRD)
 */
public class DatabaseObjectMapper {
	
	/**
	 * Converts the specified object to a data map with a key for each object
	 * field that is annotated with {@link DatabaseField DatabaseField}. If the
	 * object ID is not null, it also sets key "id". See the table at the top
	 * of this page for the returned values.
	 * 
	 * @param obj the database object
	 * @return the data map
	 */
	public Map<String,Object> objectToMap(DatabaseObject obj) {
		Map<String,Object> result = new LinkedHashMap<String,Object>();
		if (obj.getId() != null)
			result.put("id", obj.getId());
		List<DatabaseFieldSpec> fields = DatabaseFieldScanner.getDatabaseFields(
				obj.getClass());
		for (DatabaseFieldSpec field : fields) {
			Object value = PropertyReader.readProperty(obj,
					field.getPropSpec());
			result.put(field.getPropSpec().getName(), toDatabaseValue(value,
					field.getDbField()));
		}
		return result;
	}
	
	/**
	 * Converts the specified data map to an object of the specified class.
	 * It will read the key "id" from the map and write its string value to the
	 * database object. If there is no key "id", the ID in the object will be
	 * set to null. Then it scans the class for fields that are annotated with
	 * {@link DatabaseField DatabaseField}. For each field that occurs in the
	 * map, the value from the map will be converted and written to the object.
	 * The keys in the map may be in lower case.
	 * 
	 * @param map the data map (keys may be in lower case)
	 * @param clazz the class of database object
	 * @param <T> the type of database object
	 * @return the database object
	 */
	public <T extends DatabaseObject> T mapToObject(Map<?,?> map,
			Class<? extends T> clazz) {
		T result;
		try {
			result = clazz.newInstance();
		} catch (Exception ex) {
			throw new RuntimeException("Can't instantiate class \"" +
					clazz.getName() + "\": " + ex.getMessage(), ex);
		}
		Object mapId = map.get("id");
		result.setId(mapId != null ? mapId.toString() : null);
		List<DatabaseFieldSpec> fields =
				DatabaseFieldScanner.getDatabaseFields(clazz);
		for (DatabaseFieldSpec field : fields) {
			if (!mapContainsKeyCaseInsensitive(map,
					field.getPropSpec().getName())) {
				continue;
			}
			Object value = fromDatabaseValue(
					getMapCaseInsensitive(map, field.getPropSpec().getName()),
					field.getDbField(), field.getPropSpec().getField());
			try {
				if (field.getPropSpec().isPublic()) {
					field.getPropSpec().getField().set(result, value);
				} else {
					field.getPropSpec().getSetMethod().invoke(result, value);
				}
			} catch (Exception ex) {
				throw new RuntimeException("Can't write field \"" +
						field.getPropSpec().getName() + "\": " +
						ex.getMessage(), ex);
			}
		}
		return result;
	}

	/**
	 * Returns whether the map contains the specified key, using
	 * case-insensitive string comparison. The map should have string keys that
	 * are not null.
	 *
	 * @param map the map with string keys
	 * @param key the key
	 * @return true if the map contains the key, false otherwise
	 */
	private boolean mapContainsKeyCaseInsensitive(Map<?,?> map, String key) {
		if (map.containsKey(key))
			return true;
		for (Object mapKeyObj : map.keySet()) {
			String mapKey = (String)mapKeyObj;
			if (mapKey.toLowerCase().equals(key.toLowerCase()))
				return true;
		}
		return false;
	}

	/**
	 * Finds a key in a map using case-insensitive string comparison. The
	 * specified map should have string keys that are not null.
	 *
	 * @param map the map with string keys
	 * @param key the key
	 * @return the map value or null
	 */
	private Object getMapCaseInsensitive(Map<?,?> map, String key) {
		if (map.containsKey(key))
			return map.get(key);
		for (Object mapKeyObj : map.keySet()) {
			String mapKey = (String)mapKeyObj;
			if (mapKey.toLowerCase().equals(key.toLowerCase()))
				return map.get(mapKey);
		}
		return null;
	}

	/**
	 * Converts a value from a {@link DatabaseObject DatabaseObject} to a value
	 * that can be written to the database. The database value should be a
	 * string or a number. The conversion depends on the specified data type.
	 * See also the table at the top of this page.
	 * 
	 * @param value the value from the database object
	 * @param dbField the database field
	 * @return the database value
	 */
	public Object toDatabaseValue(Object value, DatabaseField dbField) {
		switch (dbField.value()) {
		case LIST:
			return toDatabaseList(value, dbField.elemType());
		case MAP:
			return toDatabaseMap(value, dbField.elemType());
		case OBJECT:
			return toDatabaseObject(value);
		case OBJECT_LIST:
			return toDatabaseObjectList(value);
		default:
			return toPrimitiveDatabaseValue(value, dbField.value());
		}
	}
	
	/**
	 * Converts a primitive value from a {@link DatabaseObject DatabaseObject}
	 * to a value that can be written to the database. A primitive value is a
	 * database type other than {@link DatabaseType#LIST LIST}, {@link
	 * DatabaseType#OBJECT OBJECT} or {@link DatabaseType#OBJECT_LIST
	 * OBJECT_LIST}. The returned database value should be a string or a
	 * number. The conversion depends on the specified data type. See also the
	 * table at the top of this page.
	 * 
	 * @param value the value from the database object
	 * @param dbType the database type
	 * @return the database value
	 */
	public Object toPrimitiveDatabaseValue(Object value, DatabaseType dbType) {
		if (value == null)
			return null;
		switch (dbType) {
		case BYTE:
			if (value instanceof Boolean) {
				boolean b = (Boolean)value;
				return b ? (byte)1 : (byte)0;
			} else {
				return ((Number)value).byteValue();
			}
		case SHORT:
			if (value instanceof Boolean) {
				boolean b = (Boolean)value;
				return b ? (byte)1 : (byte)0;
			} else if (value instanceof Byte) {
				return value;
			} else {
				return ((Number)value).shortValue();
			}
		case INT:
			if (value instanceof Boolean) {
				boolean b = (Boolean) value;
				return b ? (byte)1 : (byte)0;
			} else if (value instanceof Byte ||
					value instanceof Short ||
					value instanceof Integer) {
				return value;
			} else {
				return ((Number)value).intValue();
			}
		case LONG:
			if (value instanceof Boolean) {
				boolean b = (Boolean) value;
				return b ? (byte)1 : (byte)0;
			} else if (value instanceof Byte ||
					value instanceof Short ||
					value instanceof Integer ||
					value instanceof Long) {
				return value;
			} else {
				return ((Number)value).longValue();
			}
		case FLOAT:
			return ((Number)value).floatValue();
		case DOUBLE:
			if (value instanceof Float) {
				return value;
			} else {
				return ((Number)value).doubleValue();
			}
		case STRING:
		case TEXT:
		case TEXT_MB4:
			return value.toString();
		case DATE:
			DateTimeFormatter formatter = DateTimeFormat.forPattern(
					"yyyy-MM-dd");
			return formatter.print((LocalDate)value);
		case TIME:
			formatter = DateTimeFormat.forPattern("HH:mm:ss");
			return formatter.print((LocalTime)value);
		case DATETIME:
			formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
			return formatter.print((LocalDateTime)value);
		case ISOTIME:
			DateTime time = getDateTimeValue(value);
			formatter = DateTimeFormat.forPattern(
					"yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
			return formatter.print(time);
		default:
			break;
		}
		throw new RuntimeException("Unknown database type: " + dbType);
	}

	/**
	 * Takes the value for a date/time field from a {@link DatabaseObject
	 * DatabaseObject} (Long, Date, Instant, Calendar or DateTime) and returns
	 * a DateTime. Long, Date and Instant are converted to a DateTime with the
	 * default time zone.
	 * 
	 * @param value the date/time value
	 * @return the DateTime object
	 */
	private DateTime getDateTimeValue(Object value) {
		if (value instanceof Long) {
			return new DateTime(value);
		} else if (value instanceof Date) {
			return new DateTime(value);
		} else if (value instanceof Instant) {
			Instant instant = (Instant)value;
			return instant.toDateTime();
		} else if (value instanceof Calendar) {
			return new DateTime((Calendar)value);
		} else if (value instanceof DateTime) {
			return (DateTime)value;
		} else {
			throw new IllegalArgumentException("Invalid date/time value: " +
					value);
		}
	}
	
	/**
	 * Converts the value of a database field with type {@link
	 * DatabaseType#LIST LIST} to a value that can be written to the database.
	 * 
	 * @param value the value of the database field
	 * @param dbType the element type of the list
	 * @return the database value
	 */
	private List<?> toDatabaseList(Object value, DatabaseType dbType) {
		if (value == null)
			throw new IllegalArgumentException("List cannot be null");
		List<Object> result = new ArrayList<Object>();
		List<?> inputList;
		if (value.getClass().isArray()) {
			inputList = arrayToList(value);
		} else if (value instanceof List) {
			inputList = (List<?>)value;
		} else {
			throw new IllegalArgumentException(
					"Value is not an array or List: " +
					value.getClass().getName());
		}
		for (Object item : inputList) {
			result.add(toPrimitiveDatabaseValue(item, dbType));
		}
		return result;
	}
	
	/**
	 * Converts the value of a database field with type {@link DatabaseType#MAP
	 * MAP} to a value that can be written to the database.
	 * 
	 * @param value the value of the database field
	 * @param dbType the element type of the map
	 * @return the database value
	 */
	private Map<String,?> toDatabaseMap(Object value, DatabaseType dbType) {
		if (value == null)
			throw new IllegalArgumentException("Map cannot be null");
		Map<String,Object> result = new LinkedHashMap<String,Object>();
		if (!(value instanceof Map)) {
			throw new IllegalArgumentException("Value is not a Map: " +
					value.getClass().getName());
		}
		Map<?,?> inputMap = (Map<?,?>)value;
		for (Object keyObj : inputMap.keySet()) {
			if (keyObj == null)
				throw new IllegalArgumentException("Map key cannot be null");
			if (!(keyObj instanceof String)) {
				throw new IllegalArgumentException(
						"Map key is not a string: " +
						keyObj.getClass().getName());
			}
			String key = (String)keyObj;
			Object item = inputMap.get(keyObj);
			result.put(key, toPrimitiveDatabaseValue(item, dbType));
		}
		return result;
	}
	
	/**
	 * Converts the value of a database field with type {@link
	 * DatabaseType#OBJECT OBJECT} to a value that can be written to the
	 * database.
	 * 
	 * @param value the value of the database field
	 * @return the database value
	 */
	private Map<String,?> toDatabaseObject(Object value) {
		if (value == null)
			return null;
		if (!(value instanceof DatabaseObject)) {
			throw new IllegalArgumentException(
					"Value is not a DatabaseObject: " +
					value.getClass().getName());
		}
		return objectToMap((DatabaseObject)value);
	}
	
	/**
	 * Converts the value of a database field with type {@link
	 * DatabaseType#OBJECT_LIST OBJECT_LIST} to a value that can be written to
	 * the database.
	 * 
	 * @param value the value of the database field
	 * @return the database value
	 */
	private List<Map<String,?>> toDatabaseObjectList(Object value) {
		if (value == null)
			throw new IllegalArgumentException("List cannot be null");
		List<Map<String,?>> result = new ArrayList<Map<String,?>>();
		List<?> inputList;
		if (value.getClass().isArray()) {
			inputList = arrayToList(value);
		} else if (value instanceof List) {
			inputList = (List<?>)value;
		} else {
			throw new IllegalArgumentException(
					"Value is not an array or List: " +
					value.getClass().getName());
		}
		for (Object item : inputList) {
			result.add(toDatabaseObject(item));
		}
		return result;
	}

	/**
	 * Converts a value from the database to a value that is written to a
	 * {@link DatabaseObject DatabaseObject}. The database value should be a
	 * string or a number. The conversion depends on the specified data type.
	 * The table at the top of this page shows to what classes the data type
	 * can be converted. You should specify one of those classes, normally
	 * the class of the field to which the value will be written.
	 * 
	 * @param value the database value
	 * @param dbField the database field
	 * @param field the field to which the value will be written. This method
	 * does not actually write the value. It only uses this parameter to get
	 * the data type.
	 * @return the value for the database object
	 */
	public Object fromDatabaseValue(Object value, DatabaseField dbField,
			Field field) {
		switch (dbField.value()) {
		case LIST:
			return fromDatabaseList(value, dbField.elemType(), field);
		case MAP:
			return fromDatabaseMap(value, dbField.elemType(), field);
		case OBJECT:
			return fromDatabaseObject(value, field.getType());
		case OBJECT_LIST:
			return fromDatabaseObjectList(value, field);
		default:
			return fromPrimitiveDatabaseValue(value, dbField.value(),
					field.getType());
		}
	}
	
	/**
	 * Converts a primitive value from the database to a value that is written
	 * to a {@link DatabaseObject DatabaseObject}. A primitive value is a
	 * database field other than {@link DatabaseType#LIST LIST}, {@link
	 * DatabaseType#OBJECT OBJECT} or {@link DatabaseType#OBJECT_LIST
	 * OBJECT_LIST}. The database value should be a string or a number. The
	 * conversion depends on the specified data type. The table at the top of
	 * this page shows to what classes the data type can be converted. You
	 * should specify one of those classes, normally the class of the field to
	 * which the value will be written.
	 * 
	 * @param value the database value
	 * @param dbType the database type
	 * @param clazz the class of value to return
	 * @return the value for the database object
	 */
	private Object fromPrimitiveDatabaseValue(Object value,
			DatabaseType dbType, Class<?> clazz) {
		if (value == null)
			return null;
		switch (dbType) {
		case BYTE:
		case SHORT:
		case INT:
		case LONG:
			Number n = (Number)value;
			if (clazz == Boolean.TYPE || clazz == Boolean.class) {
				return n.longValue() != 0;
			} else if (clazz == Byte.TYPE || clazz == Byte.class) {
				return n.byteValue();
			} else if (clazz == Short.TYPE || clazz == Short.class) {
				return n.shortValue();
			} else if (clazz == Integer.TYPE || clazz == Integer.class) {
				return n.intValue();
			} else if (clazz == Long.TYPE || clazz == Long.class) {
				return n.longValue();
			} else {
				throw new IllegalArgumentException(
						"Invalid class for database type " + dbType + ": " +
						clazz.getName());
			}
		case FLOAT:
		case DOUBLE:
			n = (Number)value;
			if (clazz == Float.TYPE || clazz == Float.class) {
				return n.floatValue();
			} else if (clazz == Double.TYPE || clazz == Double.class) {
				return n.doubleValue();
			} else {
				throw new IllegalArgumentException(
						"Invalid class for database type " + dbType + ": " +
						clazz.getName());
			}
		case STRING:
			if (clazz.isEnum()) {
				try {
					Method method = clazz.getMethod("valueOf", String.class);
					return method.invoke(null, value.toString());
				} catch (Exception ex) {
					throw new IllegalArgumentException(
							"Invalid value for enum type " + clazz.getName() +
							": " + value.toString());
				}
			} else {
				return value.toString();
			}
		case TEXT:
		case TEXT_MB4:
			return value.toString();
		case DATE:
			DateTimeFormatter parser = DateTimeFormat.forPattern("yyyy-MM-dd");
			return parser.parseLocalDate(value.toString());
		case TIME:
			parser = DateTimeFormat.forPattern("HH:mm:ss");
			return parser.parseLocalTime(value.toString());
		case DATETIME:
			parser = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
			return parser.parseLocalDateTime(value.toString());
		case ISOTIME:
			try {
				return DateTimeUtils.parseDateTime(value.toString(), clazz);
			} catch (ParseException ex) {
				throw new IllegalArgumentException(ex.getMessage(), ex);
			}
		default:
			break;
		}
		throw new RuntimeException("Unknown database type: " + dbType);
	}
	
	/**
	 * Converts the database value from a field with type {@link
	 * DatabaseType#LIST LIST} to a value that is written to a {@link
	 * DatabaseObject DatabaseObject}.
	 * 
	 * @param value the database value
	 * @param dbType the element type of the list
	 * @param field the field to which the value will be written. This method
	 * does not actually write the value. It only uses this parameter to get
	 * the data type.
	 * @return the value for the database object
	 */
	private Object fromDatabaseList(Object value, DatabaseType dbType,
			Field field) {
		if (value == null)
			throw new IllegalArgumentException("List cannot be null");
		List<?> inputList;
		if (value.getClass().isArray()) {
			inputList = arrayToList(value);
		} else if (value instanceof List) {
			inputList = (List<?>)value;
		} else {
			throw new IllegalArgumentException(
					"Value is not an array or List: " +
					value.getClass().getName());
		}
		Class<?> resultListType = field.getType();
		Class<?> resultElemType;
		if (resultListType.isArray()) {
			resultElemType = resultListType.getComponentType();
		} else if (List.class.isAssignableFrom(resultListType)) {
			resultElemType = ReflectionUtils.getGenericListElementType(field);
		} else {
			throw new IllegalArgumentException(
					"Field type is not an array or List: " +
					resultListType.getName());
		}
		List<Object> convList = new ArrayList<Object>();
		for (Object item : inputList) {
			convList.add(fromPrimitiveDatabaseValue(item, dbType,
					resultElemType));
		}
		if (resultListType.isArray())
			return listToArray(convList, resultElemType);
		else
			return convList;
	}
	
	/**
	 * Converts the database value from a field with type {@link
	 * DatabaseType#MAP MAP} to a value that is written to a {@link
	 * DatabaseObject DatabaseObject}.
	 * 
	 * @param value the database value
	 * @param dbType the element type of the list
	 * @param field the field to which the value will be written. This method
	 * does not actually write the value. It only uses this parameter to get
	 * the data type.
	 * @return the value for the database object
	 */
	private Object fromDatabaseMap(Object value, DatabaseType dbType,
			Field field) {
		if (value == null)
			throw new IllegalArgumentException("Map cannot be null");
		if (!(value instanceof Map)) {
			throw new IllegalArgumentException("Value is not a Map: " +
					value.getClass().getName());
		}
		Map<?,?> inputMap = (Map<?,?>)value;
		Class<?> resultElemType = ReflectionUtils.getGenericMapValueType(field);
		Map<String,Object> convMap = new LinkedHashMap<String,Object>();
		for (Object keyObj : inputMap.keySet()) {
			if (keyObj == null)
				throw new IllegalArgumentException("Map key cannot be null");
			if (!(keyObj instanceof String)) {
				throw new IllegalArgumentException(
						"Map key is not a string: " +
						keyObj.getClass().getName());
			}
			String key = (String)keyObj;
			Object item = inputMap.get(keyObj);
			convMap.put(key, fromPrimitiveDatabaseValue(item, dbType,
					resultElemType));
		}
		return convMap;
	}
	
	/**
	 * Converts the database value from a field with type {@link
	 * DatabaseType#OBJECT OBJECT} to a value that is written to a {@link
	 * DatabaseObject DatabaseObject}.
	 * 
	 * @param value the database value
	 * @param objClass the expected object class
	 * @return the value for the database object
	 */
	private DatabaseObject fromDatabaseObject(Object value, Class<?> objClass) {
		Class<? extends DatabaseObject> dbObjClass;
		try {
			dbObjClass = objClass.asSubclass(DatabaseObject.class);
		} catch (ClassCastException ex) {
			throw new IllegalArgumentException(
					"Class is not a DatabaseObject: " + objClass.getName());
		}
		if (value == null)
			return null;
		if (!(value instanceof Map)) {
			throw new IllegalArgumentException("Value is not a Map");
		}
		return mapToObject((Map<?,?>)value, dbObjClass);
	}
	
	/**
	 * Converts the database value from a field with type {@link
	 * DatabaseType#OBJECT_LIST OBJECT_LIST} to a value that can be written to
	 * the database.
	 * 
	 * @param value the value of the database field
	 * @param field the field to which the value will be written. This method
	 * does not actually write the value. It only uses this parameter to get
	 * the data type.
	 * @return the value for the database object
	 */
	private Object fromDatabaseObjectList(Object value, Field field) {
		if (value == null)
			throw new IllegalArgumentException("List cannot be null");
		List<?> inputList;
		if (value.getClass().isArray()) {
			inputList = arrayToList(value);
		} else if (value instanceof List) {
			inputList = (List<?>)value;
		} else {
			throw new IllegalArgumentException(
					"Value is not an array or List: " +
					value.getClass().getName());
		}
		Class<?> resultListType = field.getType();
		Class<?> resultElemType;
		if (resultListType.isArray()) {
			resultElemType = resultListType.getComponentType();
		} else if (List.class.isAssignableFrom(resultListType)) {
			resultElemType = ReflectionUtils.getGenericListElementType(field);
		} else {
			throw new IllegalArgumentException(
					"Field type is not an array or List: " +
					resultListType.getName());
		}
		List<DatabaseObject> convList = new ArrayList<DatabaseObject>();
		for (Object item : inputList) {
			convList.add(fromDatabaseObject(item, resultElemType));
		}
		if (resultListType.isArray())
			return listToArray(convList, resultElemType);
		else
			return convList;
	}
	
	/**
	 * Converts an array to a list.
	 * 
	 * @param array the array
	 * @return the list
	 */
	private List<?> arrayToList(Object array) {
		List<Object> list = new ArrayList<Object>();
		int len = Array.getLength(array);
		for (int i = 0; i < len; i++) {
			list.add(Array.get(array, i));
		}
		return list;
	}
	
	/**
	 * Converts a list to an array whose elements have the specified type. The
	 * list elements should have the same type. If the element type is
	 * primitive, the list should have the corresponding object type.
	 * 
	 * @param list the list
	 * @param elemClass the type of elements
	 * @return the array
	 */
	private <T> Object listToArray(List<?> list, Class<T> elemClass) {
		Object array = Array.newInstance(elemClass, list.size());
		for (int i = 0; i < list.size(); i++) {
			Array.set(array, i, list.get(i));
		}
		return array;
	}
}
