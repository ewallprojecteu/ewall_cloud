
package eu.ewall.platform.notificationmanager;

/****************************************************************
 * Copyright 2014 Ss Cyril and Methodius University in Skopje
*****************************************************************/


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//import org.springframework.beans.factory.annotation.Autowired;

import eu.ewall.common.initialization.SwaggerConfig;
import eu.ewall.common.initialization.CommonConfig;
import eu.ewall.common.initialization.JwtAuthConfig;
import eu.ewall.common.initialization.wadl.WadlController;

//import org.springframework.core.env.Environment;


/**
 * The Class Application.
 */

@Configuration
@EnableAutoConfiguration
@ComponentScan
@Import({CommonConfig.class, JwtAuthConfig.class, SwaggerConfig.class, WadlController.class})
@PropertySource("classpath:/notification-manager-${ewall.env:local}.properties")
//@EnableDiscoveryClient
public class Application extends SpringBootServletInitializer {
    
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
     
}
