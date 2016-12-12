package eu.ewall.platform.idss.utils.json;

import java.io.IOException;

import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class DateTimeFromEpochTimeDeserializer extends JsonDeserializer<DateTime>{

	@Override
	public DateTime deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		Long value = parser.readValueAs(Long.class);
		value = value * 1000l;
		// TODO: Check if there are no time-zone issues here.
		DateTime dateTime = new DateTime(value);
		return dateTime;
	}
	
	

}
