package eu.ewall.servicebrick.physicalactivity.controller;

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
import eu.ewall.servicebrick.physicalactivity.dao.InactivityDao;
import eu.ewall.servicebrick.physicalactivity.model.InactivityEvent;
import eu.ewall.servicebrick.physicalactivity.model.InactivityHistory;

@RestController
public class InactivityController {

	private static final Logger log = LoggerFactory.getLogger(InactivityController.class);
	
	private ServiceBrickInputValidator inputValidator;
	private InactivityDao inactivityDao;
	
	@Autowired
	public InactivityController(ServiceBrickInputValidator inputValidator, InactivityDao inactivityDao) {
		this.inputValidator = inputValidator;
		this.inactivityDao = inactivityDao;
	}

	@RequestMapping(value = "/v1/{username}/inactivity", method = RequestMethod.GET)
	public InactivityHistory getInactivity(
			@PathVariable String username,
			@RequestParam(value="from", required=false) DateTime from,
			@RequestParam(value="to", required=false) DateTime to, 
			@RequestParam(value="latestevents", required=false) Integer latestEvents) {
		long start = System.currentTimeMillis();
		InactivityHistory result;
		if(from != null) {
			log.debug("Request for inactivity events for " + username + " from " + from + " to " + to);
			inputValidator.validateTimeInterval(username, from, to);
			List<InactivityEvent> daoItems = inactivityDao.readEvents(username, from, to);
			result = new InactivityHistory(username, from, to, daoItems);
		} else {
			log.debug("Request for latest " + latestEvents + " inactivity events for " + username);
			inputValidator.validateLatestEvents(username, latestEvents);
			List<InactivityEvent> daoItems = inactivityDao.readLatestEvents(username, latestEvents);
			result = new InactivityHistory(username, latestEvents, daoItems);			
		}
		log.debug("getInactivity() call took " + (System.currentTimeMillis() - start) + "ms");
		return result;
	}
		
}
