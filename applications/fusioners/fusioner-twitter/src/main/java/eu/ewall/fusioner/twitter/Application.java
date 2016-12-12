/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.fusioner.twitter;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import eu.ewall.common.initialization.CommonConfig;
import eu.ewall.common.initialization.JwtAuthConfig;
import eu.ewall.common.initialization.SwaggerConfig;
import eu.ewall.fusioner.twitter.config.EnvironmentUtils;

/**
 * The Class Application.
 * 
 */
@Configuration
@EnableAutoConfiguration
@Import({ CommonConfig.class, JwtAuthConfig.class, SwaggerConfig.class })
@PropertySource("classpath:/fusioner-twitter-${ewall.env:local}.properties")
@ComponentScan
public class Application extends SpringBootServletInitializer {

	/** The env. */
	@Autowired
	private Environment env;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.boot.context.web.SpringBootServletInitializer#configure
	 * (org.springframework.boot.builder.SpringApplicationBuilder)
	 */
	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	/**
	 * Initialize the environment for Twitter access keys, redirect Uri and
	 * additional information.
	 */
	@PostConstruct
	public void init() {
		EnvironmentUtils.setEnvironment(env);
	}
}