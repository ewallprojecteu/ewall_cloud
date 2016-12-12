package eu.ewall.platform.reasoner.sleep;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.CustomConversions;

import eu.ewall.common.initialization.MongoConfig;
import eu.ewall.platform.reasoner.sleep.DateTimeReadConverter;
import eu.ewall.platform.reasoner.sleep.TimeZoneContext;

@Configuration
public class ServiceBrickMongoConfig extends MongoConfig {

	@Override
	public CustomConversions customConversions() {
		List<Converter<?, ?>> converters = new ArrayList<Converter<?, ?>>(3);
	    converters.add(new DateTimeReadConverter(timeZoneContext()));
	    return new CustomConversions(converters);
	}
	
	@Bean
	public TimeZoneContext timeZoneContext() {
		return new TimeZoneContext();
	}
	
}
