package eu.ewall.servicebrick.calendar;

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
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.scheduling.annotation.EnableScheduling;

import eu.ewall.servicebrick.common.config.ServiceBrickDataManagerConfig;
import eu.ewall.servicebrick.common.config.ServiceBrickFrontEndConfig;
import eu.ewall.servicebrick.common.time.TimeZoneContext;
import eu.ewall.servicebrick.common.time.UserTimeZoneProvider;

/**
 * The Class Application.
 */
@Configuration
@EnableAutoConfiguration
@EnableScheduling
@Import({ ServiceBrickFrontEndConfig.class, ServiceBrickDataManagerConfig.class })
@PropertySource({ "classpath:/service-brick-calendar-${ewall.env:local}.properties" })
@ComponentScan
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

    /** The mongo ops. */
    @Autowired
    private MongoOperations mongoOps;
    
    @Autowired
	private TimeZoneContext timeZoneCtx;
    
	@Autowired
	private UserTimeZoneProvider userTimeZoneProvider;

}
