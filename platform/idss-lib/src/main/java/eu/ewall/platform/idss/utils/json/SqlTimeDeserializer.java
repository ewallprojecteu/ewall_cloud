package eu.ewall.platform.idss.utils.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

import org.joda.time.LocalTime;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * This deserializer can convert a string in format HH:mm:ss to a {@link
 * LocalTime LocalTime}.
 * 
 * @author Dennis Hofs (RRD)
 */
public class SqlTimeDeserializer
extends JsonDeserializer<LocalTime> {
	@Override
	public LocalTime deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		String val = jp.readValueAs(String.class);
		try {
			DateTimeFormatter parser = DateTimeFormat.forPattern("HH:mm:ss");
			return parser.parseLocalTime(val);
		} catch (Exception ex) {
			throw new JsonParseException("Invalid time string: " + val + ": " +
					ex.getMessage(), jp.getTokenLocation(), ex);
		}
	}
}
