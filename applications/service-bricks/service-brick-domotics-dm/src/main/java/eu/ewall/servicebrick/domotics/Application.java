package eu.ewall.servicebrick.domotics;

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
import org.springframework.scheduling.annotation.EnableScheduling;

import eu.ewall.servicebrick.common.config.ServiceBrickDataManagerConfig;
import eu.ewall.servicebrick.common.dao.SensorEventDao;
import eu.ewall.servicebrick.common.time.TimeZoneContext;
import eu.ewall.servicebrick.common.time.UserTimeZoneProvider;

@Configuration
@EnableAutoConfiguration
@EnableScheduling
@Import(ServiceBrickDataManagerConfig.class)
@PropertySource("classpath:/service-brick-domotics-dm-${ewall.env:local}.properties")
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