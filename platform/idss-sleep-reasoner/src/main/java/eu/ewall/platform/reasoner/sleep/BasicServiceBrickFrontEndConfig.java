package eu.ewall.platform.reasoner.sleep;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestOperations;

import eu.ewall.common.initialization.CommonConfig;
import eu.ewall.common.initialization.JwtAuthConfig;
import eu.ewall.common.initialization.SwaggerConfig;
import eu.ewall.platform.reasoner.sleep.ProfilingServerDao;
import eu.ewall.platform.reasoner.sleep.UserTimeZoneProvider;
import eu.ewall.platform.reasoner.sleep.ServiceBrickInputValidator;

/**
 * Basic common configuration for service brick front ends that do not use MongoDB.
 */
@Configuration
@Import({CommonConfig.class, JwtAuthConfig.class, SwaggerConfig.class})
public class BasicServiceBrickFrontEndConfig {

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
    public UserTimeZoneProvider userTimeZoneProvider() {
    	UserTimeZoneProvider timeZoneProvider = new UserTimeZoneProvider(profilingServerDao());
    	String cacheTtl = env.getProperty("timeZoneCache.ttl");
    	if (cacheTtl != null) {
    		timeZoneProvider.setCacheTtl(Integer.valueOf(cacheTtl));
    	}
    	return timeZoneProvider;
    }
	
    @Bean
    public ServiceBrickInputValidator serviceBrickInputValidator() {
    	return new ServiceBrickInputValidator(userTimeZoneProvider());
    }
    
}
