package eu.ewall.servicebrick.common.mongo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import eu.ewall.servicebrick.common.AggregationPeriod;

/**
 * MongoDB converter to read an AggregationPeriod from a string value.
 */
@ReadingConverter
public class AggregationPeriodReadConverter implements Converter<String, AggregationPeriod> {

	@Override
	public AggregationPeriod convert(String source) {
		return new AggregationPeriod(source);
	}

}
