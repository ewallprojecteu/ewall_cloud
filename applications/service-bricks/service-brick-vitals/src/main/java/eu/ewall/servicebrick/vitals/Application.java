package eu.ewall.servicebrick.vitals;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import eu.ewall.servicebrick.common.config.BasicServiceBrickFrontEndConfig;

@Configuration
@EnableAutoConfiguration
@Import(BasicServiceBrickFrontEndConfig.class)
@PropertySource("classpath:/service-brick-vitals-${ewall.env:local}.properties")
@ComponentScan
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) { 	
        return application.sources(Application.class);
    }
	
}
