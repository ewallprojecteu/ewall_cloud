package eu.ewall.common.initialization;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;

import eu.ewall.common.authentication.jwt.JwtExclusions;
import eu.ewall.common.authentication.jwt.JwtFilter;
import eu.ewall.common.initialization.JwtAuthConfig.JwtEnabledCondition;

/**
 * The Class JwtAuthConfig.
 */
@Configuration
@Conditional(JwtEnabledCondition.class)
public class JwtAuthConfig {
    
	/** The env. */
	@Autowired 
	private Environment env;
	
	/** The error controller. */
	@Autowired(required = false)
	private ErrorController errorController;
	
	/** The spring swagger config. */
	@Autowired(required = false)
	private SpringSwaggerConfig springSwaggerConfig;
	
	/** The spring swagger config. */
	@Autowired(required = false)
	private JwtExclusions jwtExclusions;
	
	/**
	 * Jwt filter.
	 *
	 * @return the filter
	 */
	@Bean
	public Filter jwtFilter() {
		String secret = env.getProperty("secret");
	    String serviceName = env.getProperty("service.name");
    	JwtFilter jwtFilter = new JwtFilter();
	    jwtFilter.setSecret(secret);
	    jwtFilter.setServiceName(serviceName);
	    List<String> exclusions = new LinkedList<>();
	    if (errorController != null) {
	    	exclusions.add(errorController.getErrorPath());
	    }
	    if (springSwaggerConfig != null) {
	    	exclusions.addAll(getSwaggerUiPatterns());
	    }
	    if (jwtExclusions != null) {
	    	exclusions.addAll(jwtExclusions.getExclusions());
	    }
	    if (exclusions.size() > 0) {
	    	jwtFilter.setExclusions(exclusions);
	    }
	    return jwtFilter;
	}

	/**
	 * Gets the swagger ui patterns.
	 *
	 * @return the swagger ui patterns
	 */
	private List<String> getSwaggerUiPatterns() {
		List<String> patterns = new ArrayList<>(8);
		patterns.add("/");
		patterns.add("/index.html");
		patterns.add("/o2c.html");
		patterns.add("/swagger-ui.js");
		patterns.add("/swagger-ui.min.js");
		patterns.add("/css/**");
		patterns.add("/fonts/**");
		patterns.add("/images/**");
		patterns.add("/lib/**");
		patterns.add("/api-docs/**");
		return patterns;
	}
	
	/**
	 * The Class JwtEnabledCondition.
	 */
	public static class JwtEnabledCondition implements ConfigurationCondition {
		
		/* (non-Javadoc)
		 * @see org.springframework.context.annotation.ConfigurationCondition#getConfigurationPhase()
		 */
		@Override
		public ConfigurationPhase getConfigurationPhase() {
			// The jwt.enabled property is not available at PARSE_CONFIGURATION phase, so evaluate the condition only
			// at REGISTER_BEAN.
			return ConfigurationPhase.REGISTER_BEAN;
		}
		
		/* (non-Javadoc)
		 * @see org.springframework.context.annotation.Condition#matches(org.springframework.context.annotation.ConditionContext, org.springframework.core.type.AnnotatedTypeMetadata)
		 */
		@Override
		public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
			String jwtEnabled = context.getEnvironment().getProperty("jwt.enabled");
			if (jwtEnabled != null && jwtEnabled.equals("false")) {
				Logger log = LoggerFactory.getLogger(JwtAuthConfig.class);
				log.warn("*** NO AUTHENTICATION FILTER APPLIED! This should NOT happen in real exec environment");
				return false;
			}
			return true;
		}
		
	}
	
}
