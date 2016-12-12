package eu.ewall.platform.idss.utils.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Instant;

/**
 * This serializer can convert a date/time type to a string in format
 * yyyy-MM-dd'T'HH:mm:ss.SSSZZ. It supports the following types:
 * 
 * <p><ul>
 * <li>{@link Long Long} (timestamp in milliseconds)</li>
 * <li>{@link Date Date}</li>
 * <li>{@link Instant Instant}</li>
 * <li>{@link Calendar Calendar}</li>
 * <li>{@link DateTime DateTime}</li>
 * </ul></p>
 * 
 * <p>The types Long, Date and Instant are translated to the default time zone.
 * Local date/times are not supported, because it may represent a time that
 * doesn't exist in the default time zone.</p>
 * 
 * @author Dennis Hofs (RRD)
 */
public class IsoDateTimeSerializer extends JsonSerializer<Object> {
	@Override
	public void serialize(Object value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		DateTime dateTime;
		if (value instanceof Long || value instanceof Date ||
				value instanceof Calendar) {
			dateTime = new DateTime(value);
		} else if (value instanceof Instant) {
			dateTime = ((Instant)value).toDateTime();
		} else if (value instanceof DateTime) {
			dateTime = (DateTime)value;
		} else {
			throw new JsonGenerationException(
					"Can't serialize type to ISO date/time: " +
					value.getClass().getName());
		}
		jgen.writeString(dateTime.toString("yyyy-MM-dd'T'HH:mm:ss.SSSZZ"));
	}
}
