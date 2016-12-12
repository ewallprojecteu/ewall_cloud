package eu.ewall.servicebrick.common.config;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestOperations;

import eu.ewall.common.initialization.CommonConfig;
import eu.ewall.servicebrick.common.dao.DataManagerUpdatesDao;
import eu.ewall.servicebrick.common.dao.ProfilingServerDao;
import eu.ewall.servicebrick.common.time.UserTimeZoneProvider;

/**
 * Common configuration to be imported in all service brick data managers.
 */
@Configuration
@Import({CommonConfig.class, ServiceBrickMongoConfig.class})
public class ServiceBrickDataManagerConfig {

	@Autowired 
	private Environment env;
	
	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;
	
	@Bean
	public ProfilingServerDao profilingServerDao() {
		String profilingServerUrl = env.getProperty("profilingServer.url");
		if (profilingServerUrl == null) {
			throw new BeanCreationException("profilingServer.url property not set");
		}
		return new ProfilingServerDao(ewallClient, profilingServerUrl);
	}
	
    @Bean
 	public DataManagerUpdatesDao dataManagerUpdatesDao() {
  		return new DataManagerUpdatesDao();
 	}
    
    @Bean
    public UserTimeZoneProvider userTimeZoneProvider() {
    	UserTimeZoneProvider timeZoneProvider = new UserTimeZoneProvider(profilingServerDao());
    	String cacheTtl = env.getProperty("timeZoneCache.ttl");
    	if (cacheTtl != null) {
    		timeZoneProvider.setCacheTtl(Integer.valueOf(cacheTtl));
    	}
    	return timeZoneProvider;
    }
	
}
