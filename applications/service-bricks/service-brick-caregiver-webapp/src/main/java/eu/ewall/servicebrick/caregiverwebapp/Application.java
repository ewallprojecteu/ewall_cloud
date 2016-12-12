package eu.ewall.servicebrick.caregiverwebapp;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.scheduling.annotation.EnableScheduling;

import eu.ewall.servicebrick.caregiverwebapp.dao.CaregiverWebappDao;
import eu.ewall.servicebrick.caregiverwebapp.model.CaregiverUserThresholds;
import eu.ewall.servicebrick.caregiverwebapp.model.Threshold;
import eu.ewall.servicebrick.caregiverwebapp.model.Threshold.Priority;
import eu.ewall.servicebrick.common.config.ServiceBrickDataManagerConfig;
import eu.ewall.servicebrick.common.config.ServiceBrickFrontEndConfig;
import eu.ewall.servicebrick.common.dao.SensorEventDao;
import eu.ewall.servicebrick.common.time.TimeZoneContext;
import eu.ewall.servicebrick.common.time.UserTimeZoneProvider;
import eu.ewall.servicebrick.physicalactivity.dao.MovementDao;

@Configuration
@EnableAutoConfiguration
@EnableScheduling
@Import({ServiceBrickFrontEndConfig.class, ServiceBrickDataManagerConfig.class})
@PropertySource({"classpath:/service-brick-caregiver-webapp-${ewall.env:local}.properties","classpath:/service-brick-caregiver-webapp-defaults.properties"})
@ComponentScan
public class Application extends SpringBootServletInitializer {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) { 	
        return application.sources(Application.class);
    }
    

	@Autowired
	private Environment env;
	@Autowired
	private CaregiverWebappDao caregiverWebappDao;
	@Autowired
	private MongoOperations mongoOps;
	@Autowired
	private TimeZoneContext timeZoneCtx;
	@Autowired
	private UserTimeZoneProvider userTimeZoneProvider;
	
    @Bean
 	public SensorEventDao sensorEventDao() {
  		return new SensorEventDao(mongoOps, timeZoneCtx, userTimeZoneProvider);
 	}

    @Bean
 	public MovementDao movementDao() {
  		return new MovementDao(mongoOps, timeZoneCtx, userTimeZoneProvider);
 	}

    @PostConstruct
	public void initDbDefaults() throws Exception {
		String[] names = env.getProperty("threshold.names").split(",");
		ArrayList<Threshold> defaults = new ArrayList<Threshold>();
		
		for(String name : names){
			Threshold caregiverWebappDefault = new Threshold();
			caregiverWebappDefault.setName(name);
			caregiverWebappDefault.setLinkedEventName(env.getProperty(name+".linkedEventName"));
			caregiverWebappDefault.setRangeMin(Double.valueOf(env.getProperty(name+".rangeMin")));
			caregiverWebappDefault.setRangeMax(Double.valueOf(env.getProperty(name+".rangeMax")));
			caregiverWebappDefault.setTargetMin(Double.valueOf(env.getProperty(name+".targetMin")));
			caregiverWebappDefault.setTargetMax(Double.valueOf(env.getProperty(name+".targetMax")));
			caregiverWebappDefault.setDefaultTargetMin(Double.valueOf(env.getProperty(name+".defaultTargetMin")));
			caregiverWebappDefault.setDefaultTargetMax(Double.valueOf(env.getProperty(name+".defaultTargetMax")));
			caregiverWebappDefault.setPriority(Priority.valueOf(env.getProperty(name+".priority")));
			caregiverWebappDefault.setDefaultPriority(Priority.valueOf(env.getProperty(name+".defaultPriority")));
			caregiverWebappDefault.setVisible(Boolean.parseBoolean(env.getProperty(name+".visible")));
			caregiverWebappDefault.setDefaultVisible(Boolean.parseBoolean(env.getProperty(name+".defaultVisible")));
			defaults.add(caregiverWebappDefault);
		}
		CaregiverUserThresholds defaultThresholds = new CaregiverUserThresholds("default", "default");
		defaultThresholds.setThresholds(defaults);
		caregiverWebappDao.initializeDefaults(defaultThresholds);
	}
	
	
}
