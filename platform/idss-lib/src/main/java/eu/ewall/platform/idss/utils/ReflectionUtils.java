package eu.ewall.platform.idss.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

/**
 * This class contains utility methods related to Java reflection.
 *
 * @author Dennis Hofs (RRD)
 */
public class ReflectionUtils {

	/**
	 * Returns the class of elements in a List field. If the field has the raw
	 * List class, this method returns Object. Otherwise it gets the raw type
	 * from the bounded parameter type using {@link
	 * #getGenericTypeClass(java.lang.reflect.Type) getGenericTypeClass()}.
	 *
	 * @param field the field
	 * @return the element type
	 */
	public static Class<?> getGenericListElementType(Field field) {
		Type genericType = field.getGenericType();
		if (genericType instanceof Class)
			return Object.class;
		if (!(genericType instanceof ParameterizedType)) {
			throw new IllegalArgumentException(
					"List field type is not a Class or ParameterizedType: " +
							genericType.getClass().getName());
		}
		Type prmType = ((ParameterizedType)genericType)
				.getActualTypeArguments()[0];
		return getGenericTypeClass(prmType);
	}

	/**
	 * Returns the class of values in a Map field. If the field has the raw
	 * Map class, this method returns Object. Otherwise it gets the raw type
	 * from the bounded parameter type using {@link #getGenericTypeClass(Type)
	 * getGenericTypeClass()}.
	 *
	 * @param field the field
	 * @return the element type
	 */
	public static Class<?> getGenericMapValueType(Field field) {
		Type genericType = field.getGenericType();
		if (genericType instanceof Class)
			return Object.class;
		if (!(genericType instanceof ParameterizedType)) {
			throw new IllegalArgumentException(
					"Map field type is not a Class or ParameterizedType: " +
							genericType.getClass().getName());
		}
		Type prmType = ((ParameterizedType)genericType)
				.getActualTypeArguments()[1];
		return getGenericTypeClass(prmType);
	}

	/**
	 * Returns the raw class of the specified generic type. It supports the
	 * following possibilities:
	 *
	 * <p><ul>
	 * <li>Type: returns Type</li>
	 * <li>Type&lt;...&gt;: returns Type</li>
	 * <li>?: returns Object</li>
	 * <li>? extends Type: returns Type</li>
	 * <li>? extends Type&lt;...&gt;: returns Type</li>
	 * </ul></p>
	 *
	 * @param type the generic type
	 * @return the raw class
	 */
	public static Class<?> getGenericTypeClass(Type type) {
		if (type instanceof Class) {
			// Type
			return (Class<?>)type;
		} else if (type instanceof ParameterizedType) {
			// Type<...>
			ParameterizedType prmType = (ParameterizedType)type;
			return (Class<?>)prmType.getRawType();
		} else if (type instanceof WildcardType) {
			// ?
			// ? extends Type
			// ? extends Type<...>
			WildcardType wildcardType = (WildcardType)type;
			Type upperType = wildcardType.getUpperBounds()[0];
			return getGenericTypeClass(upperType);
		} else {
			throw new IllegalArgumentException(
					"Bounded type parameter is not a Class, ParameterizedType or WildcardType: " +
					type.getClass().getName());
		}
	}
}
