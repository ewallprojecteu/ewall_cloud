package eu.ewall.fusioner.fitbit.utils.beans;

/**
 * This class can read the value of a property from a JavaBeans-like object. A
 * property may be accessed by a public field or getter and setter methods.
 * 
 * @see PropertyScanner
 * @author Dennis Hofs (RRD)
 */
public class PropertyReader {
	
	/**
	 * Reads the value of the specified property.
	 * 
	 * @param obj the object
	 * @param property the property name
	 * @return the property value
	 */
	public static Object readProperty(Object obj, String property) {
		PropertySpec propSpec = PropertyScanner.getProperty(obj.getClass(),
				property);
		return readProperty(obj, propSpec);
	}

	/**
	 * Reads the value of the specified property.
	 * 
	 * @param obj the object
	 * @param propSpec the property specification
	 * @return the property value
	 */
	public static Object readProperty(Object obj, PropertySpec propSpec) {
		Object value;
		try {
			if (propSpec.isPublic()) {
				value = propSpec.getField().get(obj);
			} else {
				value = propSpec.getGetMethod().invoke(obj);
			}
		} catch (Exception ex) {
			throw new RuntimeException("Can't read property \"" +
					propSpec.getName() + "\": " + ex.getMessage(), ex);
		}
		return value;
	}
}
