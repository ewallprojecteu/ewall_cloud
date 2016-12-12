package eu.ewall.servicebrick.common.mongo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import eu.ewall.servicebrick.common.AggregationPeriod;

/**
 * MongoDB converter to write an AggregationPeriod to a string instead of an object with attributes.
 */
@WritingConverter
public class AggregationPeriodWriteConverter implements Converter<AggregationPeriod, String> {

	@Override
	public String convert(AggregationPeriod source) {
		return source.toString();
	}

}
