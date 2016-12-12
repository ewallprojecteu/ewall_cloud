package eu.ewall.fusioner.fitbit.utils.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import eu.ewall.fusioner.fitbit.utils.beans.PropertyReader;

/**
 * Validates the value of properties in a JavaBeans-like object. It scans for
 * Validate* annotations at fields in the object, and for each annotation it
 * performs the validation on the property value. The property value is
 * obtained with {@link PropertyReader PropertyReader}. The value validation
 * is done with {@link Validation Validation}.
 * 
 * @author Dennis Hofs (RRD)
 */
public class ObjectValidation {
	
	/**
	 * Validates the property values in the specified object. It returns a
	 * map with errors. The keys are the property names with one or more
	 * errors. A value is a list of one or more error messages.
	 * 
	 * @param obj the object to validate
	 * @return the validation result
	 */
	public static Map<String,List<String>> validate(Object obj) {
		Map<String,List<String>> result =
				new LinkedHashMap<String,List<String>>();
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			List<String> errors = new ArrayList<String>();
			Annotation[] annots = field.getAnnotations();
			for (Annotation annot : annots) {
				try {
					validateConstraint(obj, field, annot);
				} catch (ValidationException ex) {
					errors.add(ex.getMessage());
				}
			}
			if (!errors.isEmpty())
				result.put(field.getName(), errors);
		}
		return result;
	}
	
	/**
	 * Generates an error message for the specified validation result. If there
	 * is no error, this method returns null. Otherwise it returns a string
	 * with each error on a separate line.
	 * 
	 * @param validationResult the validation result
	 * @return the error message
	 */
	public static String getErrorMessage(
			Map<String,List<String>> validationResult) {
		if (validationResult.isEmpty())
			return null;
		StringBuilder result = new StringBuilder();
		String newline = System.getProperty("line.separator");
		for (String prop : validationResult.keySet()) {
			List<String> errors = validationResult.get(prop);
			for (String error : errors) {
				if (result.length() > 0)
					result.append(newline);
				result.append(String.format(
						"Invalid value for property \"%s\": %s", prop, error));
			}
		}
		return result.toString();
	}
	
	/**
	 * Validates a property for the specified annotation. This method is called
	 * for all annotations on a field, not only Validate* annotations.
	 * 
	 * @param obj the object
	 * @param field the field
	 * @param annot the annotation
	 * @throws ValidationException if the validation failed
	 */
	private static void validateConstraint(Object obj, Field field,
			Annotation annot) throws ValidationException {
		if (annot.annotationType() == ValidateEmail.class) {
			Object val = PropertyReader.readProperty(obj, field.getName());
			Validation.validateEmail(TypeConversion.getString(val));
		} else if (annot.annotationType() == ValidateNotNull.class) {
			Object val = PropertyReader.readProperty(obj, field.getName());
			Validation.validateNotNull(val);
		}
	}
}
