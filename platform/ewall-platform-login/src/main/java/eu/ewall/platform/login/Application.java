package eu.ewall.platform.login;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import eu.ewall.common.initialization.CommonConfig;
import eu.ewall.common.initialization.SwaggerConfig;

@Configuration
@EnableAutoConfiguration
@Import({CommonConfig.class, SwaggerConfig.class})
@PropertySource("classpath:/ewall-platform-login-${ewall.env:local}.properties")
@ComponentScan
public class Application extends SpringBootServletInitializer {
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) { 	
        return application.sources(Application.class);
    }
    
}
