package eu.ewall.platform.idss.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

import java.lang.reflect.Method;

/**
 * This deserializer can read an integer value and convert it to an enum using
 * a static method T forCode().
 * 
 * @author Dennis Hofs (RRD)
 *
 * @param <T> the enum type
 */
public class EnumCodeDeserializer<T extends Enum<?>> extends
JsonDeserializer<T> {
	private Class<T> enumClass;
	
	/**
	 * Constructs a new instance. The enum class must have a static method
	 * forCode(int code).
	 * 
	 * @param enumClass the enum class
	 */
	public EnumCodeDeserializer(Class<T> enumClass) {
		this.enumClass = enumClass;
	}
	
	@Override
	public T deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		int code = p.getIntValue();
		try {
			Method method = enumClass.getMethod("forCode", Integer.TYPE);
			Object result = method.invoke(null, code);
			return enumClass.cast(result);
		} catch (Exception ex) {
			throw new RuntimeException("Can't invoke forCode(): " +
					ex.getMessage(), ex);
		}
	}
}
