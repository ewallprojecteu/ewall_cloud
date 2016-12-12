package eu.ewall.servicebrick.weather.dao;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.servicebrick.weather.response.WeatherCurrentResponse;
import eu.ewall.servicebrick.weather.response.WeatherForecastResponse;

@Component
public class WeatherDao {
	
	private static final Logger log = LoggerFactory.getLogger(WeatherDao.class);
	private static final String GET_USER_PROFILE = "/users/";

	@Value("${profilingServer.url}")
	private String profilingServerUrl;
	
	@Value("${weather.url}")
	private String weatherUrl;

	
	@Value("${openweathermap.apikey}")
	private String openWeatherMapApiKey;

	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;
	
	@Autowired
	@Qualifier("weather")
	private RestOperations weatherClient;
	
	@Cacheable(value = "serviceBrickWeatherCache", key = "'getUserProfile-'.concat(#userId).replace(' ','')")	
	public User getUser(String userId) {
		try {
			log.info("Getting profile for user " + userId);
			User user = null;
			try {
				log.info("URL: " + profilingServerUrl+GET_USER_PROFILE+userId);
				user = ewallClient.getForObject(profilingServerUrl+GET_USER_PROFILE+userId, User.class);
			} catch (RestClientException e) {
				e.printStackTrace();
				log.info("RestClientException", e);
				return null;
			}
			
			return user;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info("Exception", e);
		}
		return null;
		
	}
	
	
	//You can use lang parameter to get output in your language. We support the following languages that you can use with the corresponded lang values: English - en, Russian - ru, Italian - it, Spanish - es (or sp), Ukrainian - uk (or ua), German - de, Portuguese - pt, Romanian - ro, Polish - pl, Finnish - fi, Dutch - nl, French - fr, Bulgarian - bg, Swedish - sv (or se), Chinese Traditional - zh_tw, Chinese Simplified - zh (or zh_cn), Turkish - tr, Croatian - hr, Catalan - ca 

	@Cacheable(value="serviceBrickWeatherCache",
			key="'getCurrentWeather-'.concat(#city).concat('-').concat(#latitude==null?'latitude':#latitude).concat('-').concat(#longitude==null?'longitude':#longitude).concat('-').concat(#language==null?'language':#language).concat('-').concat(#units==null?'units':#units).replace(' ','')")
	public WeatherCurrentResponse getCurrentWeather(String city, String latitude, String longitude, String language, String units) {
		
		try {
			String CURRENT_WEATHER = "weather";
			HashMap<String, String> urlVariables = new HashMap<String, String>();
			urlVariables.put("city", city);
			urlVariables.put("language", language);
			urlVariables.put("latitude", latitude);
			urlVariables.put("longitude", longitude);
			
			if(units==null) {
				units = "metric";	
			}
			
			urlVariables.put("units", units);
			WeatherCurrentResponse weatherStatusResponse = weatherClient.getForObject(weatherUrl+CURRENT_WEATHER+"?q={city}&lang={language}&lat={latitude}&lon={longitude}&units={units}&appid="+openWeatherMapApiKey, WeatherCurrentResponse.class, urlVariables);
			return weatherStatusResponse;
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	public WeatherForecastResponse getWeatherForecast(String city, String latitude, String longitude, String language, String units){
		
		try {
			String WEATHER_FORECAST = "forecast";
			HashMap<String, String> urlVariables = new HashMap<String, String>();
			urlVariables.put("city", city);
			urlVariables.put("language", language);
			urlVariables.put("latitude", latitude);
			urlVariables.put("longitude", longitude);
			
			if(units==null) {
				units = "metric";	
			}
			
			urlVariables.put("units", units);
			WeatherForecastResponse weatherForecastResponse = weatherClient.getForObject(weatherUrl+WEATHER_FORECAST+"?q={city}&lang={language}&lat={latitude}&lon={longitude}&units={units}&appid="+openWeatherMapApiKey, WeatherForecastResponse.class, urlVariables);
			return weatherForecastResponse;
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	
}
