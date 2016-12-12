package eu.ewall.common.initialization;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import eu.ewall.common.authentication.jwt.TokenHeaderInterceptor;
import eu.ewall.common.filter.CorsFilter;

/**
 * The Class CommonConfig.
 */
@Configuration
public class CommonConfig {
    
	/** The env. */
	@Autowired 
	private Environment env;
	
	/**
	 * Cors filter.
	 *
	 * @return the filter
	 */
	@Bean
	public Filter corsFilter() {
		return new CorsFilter();
	}
	
    /**
     * Ewall rest client.
     *
     * @return the rest operations
     */
    // Rest client for invoking ewall services. Includes the authentication token in the request headers.
	@Bean
	@Qualifier("ewall")
	public RestOperations ewallRestClient() {
		RestTemplate restTemplate = new RestTemplate();
		String serviceName = env.getProperty("service.name");
		String secret = env.getProperty("secret");
		if (serviceName == null) {
			throw new IllegalArgumentException("service.name property is required");
		}
		if (secret == null) {
			throw new IllegalArgumentException("secret property is required");
		}
		List<ClientHttpRequestInterceptor> list = new ArrayList<ClientHttpRequestInterceptor>();
		list.add(new TokenHeaderInterceptor(serviceName, secret));
		restTemplate.setInterceptors(list);
		return restTemplate;
	}

}
