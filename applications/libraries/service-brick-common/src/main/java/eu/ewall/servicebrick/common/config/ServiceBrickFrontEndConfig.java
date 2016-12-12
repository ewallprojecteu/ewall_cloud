package eu.ewall.servicebrick.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Common configuration for service brick front ends that use MongoDB.
 */
@Configuration
@Import({BasicServiceBrickFrontEndConfig.class, ServiceBrickMongoConfig.class})
public class ServiceBrickFrontEndConfig {}
