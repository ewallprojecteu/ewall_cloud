package eu.ewall.servicebrick.common.json;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Serializer for Joda DateTime that uses the time zone of the serialized object instead of overriding it with a
 * default one.
 */
public class DateTimeSerializer extends StdSerializer<DateTime> {

	private static final DateTimeFormatter ISO_FORMATTER = ISODateTimeFormat.dateTime();
	
	public DateTimeSerializer() {
		super(DateTime.class);
	}
	
	@Override
	public void serialize(DateTime value, JsonGenerator jgen, SerializerProvider provider) 
			throws IOException, JsonGenerationException {
		jgen.writeString(ISO_FORMATTER.print(value));
	}

}
