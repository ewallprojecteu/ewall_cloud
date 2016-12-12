/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.profilingserver;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.ewall.common.initialization.CommonConfig;
import eu.ewall.common.initialization.JwtAuthConfig;
import eu.ewall.common.initialization.SwaggerConfig;
import eu.ewall.common.initialization.wadl.WadlController;

/**
 * The Class Application.
 */
@Configuration
@EnableAutoConfiguration
@EnableScheduling
@Import({ CommonConfig.class, JwtAuthConfig.class, SwaggerConfig.class,
		WadlController.class })
@PropertySource({ "classpath:/profiling-server-${ewall.env:local}.properties",
		"classpath:/profiling-server-defaults.properties" })
@ComponentScan
//@EnableDiscoveryClient
public class Application extends SpringBootServletInitializer {

	/** The env. */
	@Autowired
	private Environment env;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.boot.context.web.SpringBootServletInitializer#
	 * configure(org.springframework.boot.builder.SpringApplicationBuilder)
	 */
	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	/**
	 * Gets the converter.
	 *
	 * @return the converter
	 */
	@Bean
	public MappingJackson2HttpMessageConverter getConverter() {
		return new MappingJackson2HttpMessageConverter();
	}

	/**
	 * Rest template.
	 *
	 * @return the rest template
	 */
	@Bean
	@Qualifier("profServRestTemplate")
	public RestTemplate restTemplate() {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new Jackson2HalModule());
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(Arrays.asList(
				MediaType.APPLICATION_JSON,
				MediaType.parseMediaType("application/hal+json"),
				MediaType.APPLICATION_XML));
		converter.setObjectMapper(mapper);
		return new RestTemplate(
				Collections.<HttpMessageConverter<?>> singletonList(converter));
	}

	/**
	 * Password encoder.
	 *
	 * @return the password encoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
