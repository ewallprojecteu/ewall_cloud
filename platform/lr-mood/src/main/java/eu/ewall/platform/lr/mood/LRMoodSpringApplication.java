package eu.ewall.platform.lr.mood;

import java.net.URL;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import eu.ewall.common.initialization.CommonConfig;
import eu.ewall.common.initialization.JwtAuthConfig;
import eu.ewall.common.initialization.SwaggerConfig;
import eu.ewall.common.initialization.wadl.WadlController;
import eu.ewall.platform.idss.utils.AppComponents;

//@EnableDiscoveryClient
@Configuration
@EnableAutoConfiguration
@Import({CommonConfig.class, JwtAuthConfig.class, SwaggerConfig.class, WadlController.class})
@PropertySource("classpath:/lr-mood-${ewall.env:local}.properties")
@ComponentScan
public class LRMoodSpringApplication extends SpringBootServletInitializer {

	public LRMoodSpringApplication() {
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
		return application.sources(LRMoodSpringApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(LRMoodSpringApplication.class, args);
	}
}

