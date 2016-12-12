package eu.ewall.servicebrick.common.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.CustomConversions;

import eu.ewall.common.initialization.MongoConfig;
import eu.ewall.servicebrick.common.mongo.AggregationPeriodReadConverter;
import eu.ewall.servicebrick.common.mongo.AggregationPeriodWriteConverter;
import eu.ewall.servicebrick.common.mongo.DateTimeReadConverter;
import eu.ewall.servicebrick.common.time.TimeZoneContext;

@Configuration
public class ServiceBrickMongoConfig extends MongoConfig {

	@Override
	public CustomConversions customConversions() {
		List<Converter<?, ?>> converters = new ArrayList<Converter<?, ?>>(3);
	    converters.add(new AggregationPeriodReadConverter());
	    converters.add(new AggregationPeriodWriteConverter());
	    converters.add(new DateTimeReadConverter(timeZoneContext()));
	    return new CustomConversions(converters);
	}
	
	@Bean
	public TimeZoneContext timeZoneContext() {
		return new TimeZoneContext();
	}
	
}
