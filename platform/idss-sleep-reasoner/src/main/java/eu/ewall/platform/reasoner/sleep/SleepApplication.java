package eu.ewall.platform.reasoner.sleep;

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
import eu.ewall.platform.reasoner.sleep.ServiceBrickFrontEndConfig;

//@EnableDiscoveryClient
@Configuration
@EnableAutoConfiguration
@Import({CommonConfig.class, JwtAuthConfig.class, SwaggerConfig.class, WadlController.class,ServiceBrickFrontEndConfig.class})
@PropertySource("classpath:/idss-sleep-${ewall.env:local}.properties")
@ComponentScan
public class SleepApplication extends SpringBootServletInitializer {

	public SleepApplication() {
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
		return application.sources(SleepApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(SleepApplication.class, args);
	}
}

