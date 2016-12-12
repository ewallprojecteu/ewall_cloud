package eu.ewall.platform.idss.utils.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

import org.joda.time.LocalTime;

/**
 * This serializer can convert a {@link LocalTime LocalTime} to a string in
 * format HH:mm:ss.
 * 
 * @author Dennis Hofs (RRD)
 */
public class SqlTimeSerializer extends JsonSerializer<Object> {
	@Override
	public void serialize(Object value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		if (!(value instanceof LocalTime)) {
			throw new JsonGenerationException(
					"Can't serialize type to time: " +
					value.getClass().getName());
		}
		LocalTime time = (LocalTime)value;
		jgen.writeString(time.toString("HH:mm:ss"));
	}
}
