package eu.ewall.platform.ekitchen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import eu.ewall.common.initialization.CommonConfig;
import eu.ewall.common.initialization.JwtAuthConfig;
import eu.ewall.common.initialization.SwaggerConfig;

/**
 * Start class of the web application. The class name is specified in the Maven
 * POM.
 * 
 * @author Aristodemos Pnevmatikakis (AIT)
 */
@Configuration
@EnableAutoConfiguration
@Import({CommonConfig.class, JwtAuthConfig.class, SwaggerConfig.class})
@PropertySource("classpath:/ekitchen-${ewall.env:local}.properties")
@ComponentScan
public class EKitchenSpringApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(EKitchenSpringApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(EKitchenSpringApplication.class, args);
	}
}
