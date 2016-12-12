package eu.ewall.platform.idss.dao;

import eu.ewall.platform.idss.utils.beans.PropertyScanner;
import eu.ewall.platform.idss.utils.beans.PropertySpec;

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * This scanner can scan a {@link DatabaseObject DatabaseObject} class for
 * fields that are annotated with {@link DatabaseField DatabaseField} and
 * returns specifications for those fields.
 * 
 * @author Dennis Hofs (RRD)
 */
public class DatabaseFieldScanner {

	/**
	 * Scans the specified class and returns specifications for the database
	 * fields. This excludes the "id" field, which should not be annotated as
	 * a database field.
	 * 
	 * @param clazz the class
	 * @return the database fields
	 */
	public static List<DatabaseFieldSpec> getDatabaseFields(
			Class<? extends DatabaseObject> clazz) {
		List<DatabaseFieldSpec> result = new ArrayList<DatabaseFieldSpec>();
		if (DatabaseObject.class.isAssignableFrom(clazz.getSuperclass())) {
			result.addAll(getDatabaseFields(clazz.getSuperclass().asSubclass(
					DatabaseObject.class)));
		}
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			DatabaseField annot = field.getAnnotation(DatabaseField.class);
			if (annot == null)
				continue;
			PropertySpec propSpec = PropertyScanner.getProperty(clazz,
					field.getName());
			DatabaseFieldSpec spec = new DatabaseFieldSpec(propSpec, annot);
			result.add(spec);
		}
		return result;
	}

	/**
	 * Scans the specified class and returns the names of the database fields.
	 * This excludes the "id" field, which should not be annotated as a
	 * database field.
	 *
	 * @param clazz the class
	 * @return the names of the database fields
	 */
	public static List<String> getDatabaseFieldNames(
			Class<? extends DatabaseObject> clazz) {
		List<DatabaseFieldSpec> fields = getDatabaseFields(clazz);
		List<String> names = new ArrayList<String>();
		for (DatabaseFieldSpec field : fields) {
			names.add(field.getPropSpec().getName());
		}
		return names;
	}
}
