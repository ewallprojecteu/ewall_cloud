package eu.ewall.platform.idss.utils.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

import org.joda.time.Instant;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * This deserializer can convert a string in ISO date/time format to an {@link
 * Instant Instant}.
 * 
 * @author Dennis Hofs (RRD)
 */
public class InstantFromIsoDateTimeDeserializer
extends JsonDeserializer<Instant> {
	@Override
	public Instant deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		String val = jp.readValueAs(String.class);
		try {
			DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser();
			return parser.parseDateTime(val).toInstant();
		} catch (Exception ex) {
			throw new JsonParseException("Invalid ISO date/time string: " +
					val + ": " + ex.getMessage(), jp.getTokenLocation(), ex);
		}
	}
}
