package eu.ewall.platform.lr.vitalsigns;

/****************************************************************
 * Copyright 2015 Ss Cyril and Methodius University in Skopje
*****************************************************************/


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

//import org.springframework.beans.factory.annotation.Autowired;

import eu.ewall.common.initialization.SwaggerConfig;
import eu.ewall.common.initialization.CommonConfig;
import eu.ewall.common.initialization.JwtAuthConfig;

//import org.springframework.core.env.Environment;


/**
 * The Class Application.
 */

@Configuration
@EnableAutoConfiguration
@ComponentScan
@Import({CommonConfig.class, JwtAuthConfig.class, SwaggerConfig.class})
@PropertySource("classpath:/lr-vital-signs-${ewall.env:local}.properties")
public class Application extends SpringBootServletInitializer {
    
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
     
}
