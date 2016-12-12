package eu.ewall.servicebrick.dailyfunctioning.controller;

import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.ewall.servicebrick.common.validation.ServiceBrickInputValidator;
import eu.ewall.servicebrick.dailyfunctioning.dao.LocationDao;
import eu.ewall.servicebrick.dailyfunctioning.model.LocationEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.LocationHistory;

@RestController
public class LocationController {

	private static final Logger log = LoggerFactory.getLogger(LocationController.class);
	
	private ServiceBrickInputValidator inputValidator;
	private LocationDao locationDao;
	
	@Autowired
	public LocationController(ServiceBrickInputValidator inputValidator, LocationDao locationDao) {
		this.inputValidator = inputValidator;
		this.locationDao = locationDao;
	}

	@RequestMapping(value = "/v1/{username}/location", method = RequestMethod.GET)
	public LocationHistory getLocation(
			@PathVariable String username,
			@RequestParam(value="from", required=false) DateTime from,
			@RequestParam(value="to", required=false) DateTime to, 
			@RequestParam(value="latestevents", required=false) Integer latestEvents) {
		long start = System.currentTimeMillis();
		LocationHistory result;
		if(from != null) {
			log.debug("Request for location events for " + username + " from " + from + " to " + to);
			inputValidator.validateTimeInterval(username, from, to);
			List<LocationEvent> daoItems = locationDao.readEvents(username, from, to);
			result = new LocationHistory(username, from, to, daoItems);
		} else {
			log.debug("Request for latest " + latestEvents + " location events for " + username);
			inputValidator.validateLatestEvents(username, latestEvents);
			List<LocationEvent> daoItems = locationDao.readLatestEvents(username, latestEvents);
			result = new LocationHistory(username, latestEvents, daoItems);			
		}
		log.debug("getLocation() call took " + (System.currentTimeMillis() - start) + "ms");
		return result;
	}
	
}
