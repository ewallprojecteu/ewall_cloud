package eu.ewall.servicebrick.physicalactivity;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import eu.ewall.common.initialization.wadl.WadlController;
import eu.ewall.servicebrick.common.config.ServiceBrickDataManagerConfig;


@EnableDiscoveryClient
@Configuration
@EnableAutoConfiguration
@EnableScheduling
@Import({ServiceBrickDataManagerConfig.class, WadlController.class })
@PropertySource("classpath:/service-brick-physicalactivity-dm-${ewall.env:local}.properties")
@ComponentScan
public class Application extends SpringBootServletInitializer {
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) { 	
        return application.sources(Application.class);
    }
    
}
