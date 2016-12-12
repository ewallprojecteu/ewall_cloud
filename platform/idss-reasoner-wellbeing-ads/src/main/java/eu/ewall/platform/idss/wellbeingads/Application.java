/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.idss.wellbeingads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

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
@PropertySource({ "classpath:/idss-reasoner-wellbeing-ads-${ewall.env:local}.properties"})
public class Application extends SpringBootServletInitializer {

    /** The Constant log. */
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.boot.context.web.SpringBootServletInitializer#
     * configure(org.springframework.boot.builder.SpringApplicationBuilder)
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	return application.sources(Application.class);
    }

    /** The env. */
    @Autowired
    private Environment env;
 
}
