package eu.ewall.platform.idss.utils.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

import org.joda.time.LocalDateTime;

/**
 * This serializer can convert a {@link LocalDateTime LocalDateTime} to a
 * string in format yyyy-MM-dd HH:mm:ss.
 * 
 * @author Dennis Hofs (RRD)
 */
public class SqlDateTimeSerializer extends JsonSerializer<Object> {
	@Override
	public void serialize(Object value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		if (!(value instanceof LocalDateTime)) {
			throw new JsonGenerationException(
					"Can't serialize type to date/time: " +
					value.getClass().getName());
		}
		LocalDateTime time = (LocalDateTime)value;
		jgen.writeString(time.toString("yyyy-MM-dd HH:mm:ss"));
	}
}
