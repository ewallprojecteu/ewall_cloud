package eu.ewall.platform.idss.utils.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

import org.joda.time.LocalDateTime;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * This deserializer can convert a string in format yyyy-MM-dd HH:mm:ss to a
 * {@link LocalDateTime LocalDateTime}.
 * 
 * @author Dennis Hofs (RRD)
 */
public class SqlDateTimeDeserializer
extends JsonDeserializer<LocalDateTime> {
	@Override
	public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		String val = jp.readValueAs(String.class);
		try {
			DateTimeFormatter parser = DateTimeFormat.forPattern(
					"yyyy-MM-dd HH:mm:ss");
			return parser.parseLocalDateTime(val);
		} catch (Exception ex) {
			throw new JsonParseException("Invalid date/time string: " + val +
					": " + ex.getMessage(), jp.getTokenLocation(), ex);
		}
	}
}
