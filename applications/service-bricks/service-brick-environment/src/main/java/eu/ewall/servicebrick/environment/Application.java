package eu.ewall.servicebrick.environment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.core.MongoOperations;

import eu.ewall.servicebrick.common.config.ServiceBrickFrontEndConfig;
import eu.ewall.servicebrick.common.dao.SensorEventDao;
import eu.ewall.servicebrick.common.time.TimeZoneContext;
import eu.ewall.servicebrick.common.time.UserTimeZoneProvider;

@Configuration
@EnableAutoConfiguration
@Import(ServiceBrickFrontEndConfig.class)
@PropertySource("classpath:/service-brick-environment-${ewall.env:local}.properties")
@ComponentScan
public class Application extends SpringBootServletInitializer {

	@Autowired
	private MongoOperations mongoOps;
	@Autowired
	private TimeZoneContext timeZoneCtx;
	@Autowired
	private UserTimeZoneProvider userTimeZoneProvider;
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) { 	
        return application.sources(Application.class);
    }
	
    @Bean
 	public SensorEventDao sensorEventDao() {
  		return new SensorEventDao(mongoOps, timeZoneCtx, userTimeZoneProvider);
 	}
    
}
