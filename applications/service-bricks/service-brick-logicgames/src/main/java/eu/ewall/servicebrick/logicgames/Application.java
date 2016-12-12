package eu.ewall.servicebrick.logicgames;

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

import eu.ewall.servicebrick.common.config.ServiceBrickDataManagerConfig;
import eu.ewall.servicebrick.common.config.ServiceBrickFrontEndConfig;


@Configuration
@EnableAutoConfiguration
@Import({ServiceBrickFrontEndConfig.class, ServiceBrickDataManagerConfig.class})
@PropertySource({"classpath:/service-brick-logicgames-${ewall.env:local}.properties"})
@ComponentScan
public class Application extends SpringBootServletInitializer {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) { 	
        return application.sources(Application.class);
    }
    
    	
}
