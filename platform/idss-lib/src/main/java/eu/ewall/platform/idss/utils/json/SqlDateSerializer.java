package eu.ewall.platform.idss.utils.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

import org.joda.time.LocalDate;

/**
 * This serializer can convert a {@link LocalDate LocalDate} to a string in
 * format yyyy-MM-dd.
 * 
 * @author Dennis Hofs (RRD)
 */
public class SqlDateSerializer extends JsonSerializer<Object> {
	@Override
	public void serialize(Object value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		if (!(value instanceof LocalDate)) {
			throw new JsonGenerationException(
					"Can't serialize type to date: " +
					value.getClass().getName());
		}
		LocalDate date = (LocalDate)value;
		jgen.writeString(date.toString("yyyy-MM-dd"));
	}
}
