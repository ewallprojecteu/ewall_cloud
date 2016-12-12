package eu.ewall.platform.idss.utils.beans;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import java.util.ArrayList;
import java.util.List;

/**
 * This scanner can get the specification of a property in a JavaBeans-like
 * class. A property may be accessed by a public field or getter and setter
 * methods. The possible getter and setter methods depend on the field type
 * and name.
 * 
 * <p><ul>
 * <li>Property "prop", any type: getProp(), setProp()</li>
 * <li>Property "prop", boolean: getProp(), isProp(), setProp()</li>
 * <li>Property "isProp", boolean: getIsProp(), isIsProp(), isProp(),
 * setIsProp(), setProp()</li>
 * </ul></p>
 * 
 * @author Dennis Hofs (RRD)
 */
public class PropertyScanner {
	
	/**
	 * Gets a specification of the property with the specified name from the
	 * specified class.
	 * 
	 * @param clazz the class
	 * @param name the property name
	 * @return the property specification
	 * @throws RuntimeException if the property doesn't exist or can't be read
	 * or written
	 */
	public static PropertySpec getProperty(Class<?> clazz, String name) {
		Field field;
		try {
			field = clazz.getDeclaredField(name);
		} catch (Exception ex) {
			throw new RuntimeException(String.format(
					"Can't find property \"%s\" in class %s", name,
					clazz.getName()));
		}
		PropertySpec spec = new PropertySpec();
		spec.setName(field.getName());
		spec.setField(field);
		spec.setPublic((field.getModifiers() & Modifier.PUBLIC) != 0);
		if (!spec.isPublic()) {
			String capName = spec.getName().substring(0, 1).toUpperCase() +
					spec.getName().substring(1);
			// find get method
			List<String> getNames = new ArrayList<String>();
			if (field.getType() == Boolean.TYPE ||
					field.getType() == Boolean.class) {
				if (spec.getName().matches("is[A-Z0-9_].*"))
					getNames.add(spec.getName());
				getNames.add("is" + capName);
			}
			getNames.add("get" + capName);
			StringBuilder logGets = new StringBuilder();
			for (int i = 0; i < getNames.size(); i++) {
				if (i > 0 && i == getNames.size() - 1)
					logGets.append(" or ");
				else if (i > 0)
					logGets.append(", ");
				logGets.append("\"" + getNames.get(i) + "\"");
			}
			NoSuchMethodException getMethodEx = null;
			for (String getName : getNames) {
				try {
					spec.setGetMethod(clazz.getMethod(getName));
					break;
				} catch (NoSuchMethodException ex) {
					if (getMethodEx == null)
						getMethodEx = ex;
				}
			}
			if (spec.getGetMethod() == null) {
				throw new RuntimeException(String.format(
						"Can't read property \"%s\" in class %s: " +
						"Field is not public and method %s not found",
						spec.getName(), clazz.getName(),
						logGets.toString()) + ": " +
						getMethodEx.getMessage(), getMethodEx);
			}
			// find set method
			List<String> setNames = new ArrayList<String>();
			if ((field.getType() == Boolean.TYPE ||
					field.getType() == Boolean.class) &&
					spec.getName().matches("is[A-Z0-9_].*")) {
				setNames.add("set" + spec.getName().substring(2));
			}
			setNames.add("set" + capName);
			StringBuilder logSets = new StringBuilder();
			for (int i = 0; i < setNames.size(); i++) {
				if (i > 0 && i == setNames.size() - 1)
					logSets.append(" or ");
				else if (i > 0)
					logSets.append(", ");
				logSets.append("\"" + setNames.get(i) + "\"");
			}
			NoSuchMethodException setMethodEx = null;
			for (String setName : setNames) {
				try {
					spec.setSetMethod(clazz.getMethod(setName, field.getType()));
					break;
				} catch (NoSuchMethodException ex) {
					if (setMethodEx == null)
						setMethodEx = ex;
				}
			}
			if (spec.getSetMethod() == null) {
				throw new RuntimeException(String.format(
						"Can't write property \"%s\" in class %s: " +
						"Field is not public and method %s not found",
						spec.getName(), clazz.getName(),
						logSets.toString()) + ": " +
						setMethodEx.getMessage(), setMethodEx);
			}
		}
		return spec;
	}
}
