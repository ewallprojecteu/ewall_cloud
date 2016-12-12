package eu.ewall.servicebrick.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.servicebrick.weather.dao.WeatherDao;
import eu.ewall.servicebrick.weather.response.WeatherCurrentResponse;
import eu.ewall.servicebrick.weather.response.WeatherForecastResponse;


@RestController
public class WeatherController {
	
	private static final Logger log = LoggerFactory.getLogger(WeatherController.class);
	
	@Autowired
	private WeatherDao dao;
	
	/**
	 * Provides weather data related to a location (city, country code)
	 * @param userid
	 * @param from
	 * @param to
	 * @return A TemplateResponse1 object represented in JSON
	 */
	@RequestMapping(value="/weather", method=RequestMethod.GET)
    public ResponseEntity<WeatherCurrentResponse> getWeatherStatus(
    		@RequestParam(value="userid", required=true) String userid, 
    		@RequestParam(value="lat", required=false) String latitude,  
    		@RequestParam(value="lon", required=false) String longitude,  
    		@RequestParam(value="city", required=false) String city, 
    		@RequestParam(value="lang", required=false) String language,  
    		@RequestParam(value="units", required=false) String units) {
		log.info("Got request 'weather' (GET) for user " + userid);
		User userProfile = dao.getUser(userid);
		if (userProfile == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		//If city is null and no coords are provided, we take the city form the user profile
		if (city==null && (latitude==null || longitude==null)) {
			log.info("Getting profile for user " + userid);
			log.info("Getting city from profile");
			city = userProfile.getUserProfile().getvCardSubProfile().getCity();
			log.info("Getting weather for city " + city);
		}
		log.info("Getting Country from profile");
		String country = userProfile.getUserProfile().getvCardSubProfile().getCountry();
		if(country!=null && !"".equals(country)){
			city=city.concat(",").concat(country);
			log.info("Getting weather for city " + city);
		}
		WeatherCurrentResponse currentWeather = dao.getCurrentWeather(city, latitude, longitude, language, units);
		if (currentWeather == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(currentWeather, HttpStatus.OK);
		}
    }

	@Cacheable(value="serviceBrickWeatherCache", key="'getWeatherForecast-'.concat(#userid).concat('-').concat(#latitude==null?'latitude':#latitude).concat('-').concat(#longitude==null?'longitude':#longitude).concat('-').concat(#city==null?'city':#city).concat('-').concat(#language==null?'language':#language).concat('-').concat(#units==null?'units':#units).replace(' ','')")
	@RequestMapping(value="/forecast", method=RequestMethod.GET)
    public  WeatherForecastResponse getWeatherForecast(@RequestParam(value="userid", required=true) String userid, 
    												@RequestParam(value="lat", required=false) String latitude,  
    												@RequestParam(value="lon", required=false) String longitude,  
    												@RequestParam(value="city", required=false) String city, 
    												@RequestParam(value="lang", required=false) String language,  
    												@RequestParam(value="units", required=false) String units) {
    	try {
			log.info("Got request 'weather forecast' (GET) for user " + userid );
			
			log.info("Getting profile for user " + userid);
			User user = dao.getUser(userid);
			log.info("Got profile " + user);
			
			//If city is null and no coords are provided, we take the city form the user profile
			if(city==null && (latitude==null || longitude==null)) {
				log.info("Getting city from profile");
				city = user.getUserProfile().getvCardSubProfile().getCity();
				log.info("Getting forecast for city " + city);
			}
			
			String country = user.getUserProfile().getvCardSubProfile().getCountry();
			if(country!=null && !"".equals(country)){
				city=city.concat(",").concat(country);
				log.info("Getting weather for city " + city);
			}
			WeatherForecastResponse weatherForecast = dao.getWeatherForecast(city, latitude, longitude, language, units);			
			return weatherForecast;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception", e);
		}
    	return null;
    }

}
