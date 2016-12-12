package eu.ewall.servicebrick.common.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import eu.ewall.servicebrick.common.AggregationPeriod;

/**
 * Serializer for AggregationPeriod.
 */
public class AggregationPeriodSerializer extends StdSerializer<AggregationPeriod> {
	
	public AggregationPeriodSerializer() {
		super(AggregationPeriod.class);
	}
	
	@Override
	public void serialize(AggregationPeriod value, JsonGenerator jgen, SerializerProvider provider) 
			throws IOException, JsonGenerationException {
		jgen.writeString(value.toString());
	}

}
