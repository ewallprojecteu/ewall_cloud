package eu.ewall.platform.idss.automaticgoalsetting;

import java.net.URL;

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
import eu.ewall.platform.idss.automaticgoalsetting.service.IDSSAutomaticGoalSettingService;
import eu.ewall.platform.idss.utils.AppComponents;

/**
 * Main application for the {@link IDSSAutomaticGoalSettingService
 * IDSSAutomaticGoalSettingService} in eWall.
 * 
 * @author Dennis Hofs (RRD)
 */
@Configuration
@EnableAutoConfiguration
@Import({CommonConfig.class, JwtAuthConfig.class, SwaggerConfig.class})
@PropertySource("classpath:/idss-automatic-goal-setting-${ewall.env:local}.properties")
@ComponentScan
public class IDSSAutomaticGoalSettingSpringApplication extends SpringBootServletInitializer {
	
	public IDSSAutomaticGoalSettingSpringApplication() {
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
		return application.sources(
				IDSSAutomaticGoalSettingSpringApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(IDSSAutomaticGoalSettingSpringApplication.class,
				args);
	}
}
