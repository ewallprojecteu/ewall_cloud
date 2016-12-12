package eu.ewall.platform.idss.utils.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

import java.lang.reflect.Method;

/**
 * This serializer calls method code() on an enum and writes it as an integer
 * value. The enum class must have a method int code().
 * 
 * @author Dennis Hofs (RRD)
 */
public class EnumCodeSerializer extends JsonSerializer<Enum<?>> {

	@Override
	public void serialize(Enum<?> value, JsonGenerator gen,
			SerializerProvider serializers) throws IOException,
			JsonProcessingException {
		int code;
		try {
			Method method = value.getClass().getMethod("code");
			code = (Integer)method.invoke(value);
		} catch (Exception ex) {
			throw new RuntimeException("Can't invoke code(): " +
					ex.getMessage(), ex);
		}
		gen.writeNumber(code);
	}
}
