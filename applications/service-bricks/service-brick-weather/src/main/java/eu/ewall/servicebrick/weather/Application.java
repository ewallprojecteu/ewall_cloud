package eu.ewall.servicebrick.weather;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import eu.ewall.common.initialization.CacheConfig;
import eu.ewall.common.initialization.CommonConfig;
import eu.ewall.common.initialization.JwtAuthConfig;
import eu.ewall.common.initialization.SwaggerConfig;

@Configuration
@EnableAutoConfiguration
@Import({CommonConfig.class, JwtAuthConfig.class, CacheConfig.class, SwaggerConfig.class})
@PropertySource("classpath:/service-brick-weather-${ewall.env:local}.properties")
@ComponentScan
public class Application extends SpringBootServletInitializer {
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) { 	
        return application.sources(Application.class);
    }
    
    // The client provided by CommonConfig adds the ewall authentication header but the weather service doesn't need it
 	@Bean
 	@Qualifier("weather")
 	public RestOperations weatherRestClient() {
 		return new RestTemplate();
 	}
    
}
