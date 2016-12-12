package eu.ewall.common.initialization;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;

import eu.ewall.common.swagger.SimpleSwaggerPathProvider;

/**
 * Enables generation of API docs via Swagger. Add ewall-swagger-ui as a dependency to expose the API user interface at
 * the base address of the web application.
 */
@Configuration
@EnableSwagger
public class SwaggerConfig {

	/** The env. */
	@Autowired 
	private Environment env;
	
	/** The servlet context. */
	@Autowired
	private ServletContext servletContext;
	
	/** The spring swagger config. */
	@Autowired
	private SpringSwaggerConfig springSwaggerConfig;
	
	/**
	 * Swagger plugin.
	 *
	 * @return the swagger spring mvc plugin
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Bean
	public SwaggerSpringMvcPlugin swaggerPlugin() throws IOException {
		SwaggerSpringMvcPlugin plugin = new SwaggerSpringMvcPlugin(this.springSwaggerConfig);
		plugin.apiInfo(getApiInfo());
		String baseUrl = env.getProperty("service.baseUrl");
		if (baseUrl != null) {
			plugin.pathProvider(new SimpleSwaggerPathProvider(baseUrl));
		}
		return plugin;
	}

	/**
	 * Gets the api info.
	 *
	 * @return the api info
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private ApiInfo getApiInfo() throws IOException {
		try (InputStream inputStream = servletContext.getResourceAsStream("/META-INF/MANIFEST.MF")) {
			Manifest manifest = new Manifest(inputStream);
			Attributes attributes = manifest.getMainAttributes();
			String title = attributes.getValue("Implementation-Title");
			String description = attributes.getValue("Implementation-Description");
			String contact = attributes.getValue("Mailing-List");
			return new ApiInfo(title, description, null, contact, null, null);
		}
	}
	
}
