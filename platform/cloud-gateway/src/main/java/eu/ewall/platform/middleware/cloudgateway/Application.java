/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.cloudgateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestOperations;

import eu.ewall.common.initialization.CommonConfig;
import eu.ewall.common.initialization.JwtAuthConfig;
import eu.ewall.common.initialization.SwaggerConfig;
import eu.ewall.common.initialization.wadl.WadlController;

/**
 * The Class Application.
 */
@Configuration
@EnableScheduling
@ComponentScan
@EnableAutoConfiguration
@Import({ CommonConfig.class, JwtAuthConfig.class, SwaggerConfig.class,
		WadlController.class })
@PropertySource({ "classpath:/cloud-gateway-${ewall.env:local}.properties",
		"classpath:/cloud-gateway-defaults.properties" })
public class Application extends SpringBootServletInitializer {

	@Autowired
	private Environment env;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.boot.context.web.SpringBootServletInitializer#
	 * configure (org.springframework.boot.builder.SpringApplicationBuilder)
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

	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;

}