package eu.ewall.fusioner.fitbit;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import eu.ewall.common.authentication.jwt.JwtExclusions;
import eu.ewall.common.initialization.CommonConfig;
import eu.ewall.common.initialization.JwtAuthConfig;
import eu.ewall.common.initialization.SwaggerConfig;
import eu.ewall.platform.idss.utils.AppComponents;

/**
 * Main application for the Fitbit fusioner in eWall.
 * 
 * @author Dennis Hofs (RRD)
 */
@Configuration
@EnableAutoConfiguration
@Import({CommonConfig.class, JwtAuthConfig.class, SwaggerConfig.class})
@PropertySource("classpath:/fusioner-fitbit-${ewall.env:local}.properties")
@ComponentScan
public class FusionerFitbitSpringApplication extends SpringBootServletInitializer {
	

   @Bean
   public JwtExclusions jwtExclusions() {
	   	List<String> exclusions = new ArrayList<String>();
	   	exclusions.add("/authorize_callback");
		return new JwtExclusions(exclusions);
    }

	
	public FusionerFitbitSpringApplication() {
		URL configUrl = getClass().getResource("config.xml");
		if (configUrl == null)
			throw new RuntimeException("Resource \"config.xml\" not found");
		AppComponents components = AppComponents.getInstance();
		try {
			components.buildComponents(configUrl);
		} catch (Exception ex) {
			throw new RuntimeException(
					"Can't build application components from resource \"config.xml\": " +
					ex.getMessage(), ex);
		}
	}

	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder application) {
		return application.sources(FusionerFitbitSpringApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(FusionerFitbitSpringApplication.class, args);
	}
}
