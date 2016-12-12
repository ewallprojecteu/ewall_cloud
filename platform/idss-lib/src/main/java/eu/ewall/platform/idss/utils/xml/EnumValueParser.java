package eu.ewall.platform.idss.utils.xml;

import eu.ewall.platform.idss.utils.exception.ParseException;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Value parser for enum values. It performs a case-insensitive search of a
 * value in a specified enum class.
 * 
 * @author Dennis Hofs (RRD)
 * @param <T> the enum type
 */
public class EnumValueParser<T extends Enum<T>> implements XMLValueParser<T> {
	private Class<T> enumClass;

	/**
	 * Constructs a new parser.
	 * 
	 * @param enumClass the enum class
	 */
	public EnumValueParser(Class<T> enumClass) {
		this.enumClass = enumClass;
	}

	@Override
	public T parse(String xml) throws ParseException {
		String lowerXml = xml.toLowerCase();
		Object array;
		String invokeError = "Can't invoke method values() on enum class";
		try {
			Method method = enumClass.getMethod("values");
			array = method.invoke(null);
		} catch (InvocationTargetException ex) {
			Throwable targetEx = ex.getTargetException();
			if (targetEx == null)
				targetEx = ex;
			throw new RuntimeException(invokeError + ": " +
					targetEx.getMessage(), targetEx);
		} catch (Exception ex) {
			throw new RuntimeException(invokeError + ": " + ex.getMessage(),
					ex);
		}
		int len = Array.getLength(array);
		for (int i = 0; i < len; i++) {
			Object objItem = Array.get(array, i);
			if (objItem.toString().toLowerCase().equals(lowerXml))
				return enumClass.cast(objItem);
		}
		throw new ParseException("Value of enum class " + enumClass.getName() +
				" not found: " + xml);
	}
}
