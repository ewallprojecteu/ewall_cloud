/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.idss.wellbeingads.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import eu.ewall.platform.idss.wellbeingads.model.weather.WeatherCurrentResponse;

/**
 * The Class WeatherDAO.
 */
@Component
public class WeatherDAO {

	/** The log. */
	Logger log = LoggerFactory.getLogger(WeatherDAO.class);

	/** The service brick weather. */
	@Value("${service-brick-weather.url}")
	private String serviceBrickWeather;

	/** The ewall client. */
	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;

	/**
	 * Post notification.
	 *
	 * @param username the username
	 * @return the weather current response
	 */
	public WeatherCurrentResponse getCurrentWeather(String username) {
		String url = serviceBrickWeather + "/weather?userid={username}";
		log.debug("Invoking endpoint: " + url);
		try {
			return ewallClient.getForObject(url, WeatherCurrentResponse.class, username);
		} catch (Exception e) {
			log.error("Error in getting current weather from service brick", e);
			return null;
		}
	
	}
	
}
