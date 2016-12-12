package eu.ewall.servicebrick.common.config;

import org.joda.time.DateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import eu.ewall.servicebrick.common.format.ReadableInstantFormatter;
import eu.ewall.servicebrick.common.format.TimeZoneJodaDateTimeFormatAnnotationFormatterFactory;
import eu.ewall.servicebrick.common.json.DateTimeSerializer;
import eu.ewall.servicebrick.common.json.EwallModule;

/**
 * Contains Spring configuration for handling special data types in the service bricks.
 */
@Configuration
public class DataTypesConfig {

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		return mapper;
	}
	
	@Bean
	public JodaModule jodaModule() {
		JodaModule module = new JodaModule();
		// Override the default DateTimeSerializer with eWall custom implementation
		module.addSerializer(DateTime.class, new DateTimeSerializer());
		return module;
	}
	
	@Bean
	public EwallModule ewallModule() {
		return new EwallModule();
	}
	
	@Bean
	public WebMvcConfigurer dataTypesWebMvcConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addFormatters(FormatterRegistry registry) {
				registry.addFormatter(new ReadableInstantFormatter());
				registry.addFormatterForFieldAnnotation(new TimeZoneJodaDateTimeFormatAnnotationFormatterFactory());
			}
		};
	}
	
}
