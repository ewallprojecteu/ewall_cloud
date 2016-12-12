package eu.ewall.servicebrick.socializingmood.controller;

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
import eu.ewall.servicebrick.socializingmood.dao.SocializingDao;
import eu.ewall.servicebrick.socializingmood.model.Socializing;
import eu.ewall.servicebrick.socializingmood.model.SocializingHistory;

@RestController
public class SocializingController {

	private static final Logger log = LoggerFactory.getLogger(SocializingController.class);
	
	private ServiceBrickInputValidator inputValidator;
	private SocializingDao socializingDao;
	
	@Autowired
	public SocializingController(ServiceBrickInputValidator inputValidator, SocializingDao socializingDao) {
		this.inputValidator = inputValidator;
		this.socializingDao = socializingDao;
	}

	@RequestMapping(value = "/v1/{username}/socializing", method = RequestMethod.GET)
	public SocializingHistory getSocializing(
			@PathVariable String username,
			@RequestParam(value="from", required=false) DateTime from,
			@RequestParam(value="to", required=false) DateTime to, 
			@RequestParam(value="latestevents", required=false) Integer latestEvents) {
		long start = System.currentTimeMillis();
		SocializingHistory result;
		if(from != null) {
			log.debug("Request for mood events for " + username + " from " + from + " to " + to);
			inputValidator.validateTimeInterval(username, from, to);
			List<Socializing> daoItems = socializingDao.readEvents(username, from, to);
			result = new SocializingHistory(username, from, to, daoItems);
		} else {
			log.debug("Request for latest " + latestEvents + " mood events for " + username);
			inputValidator.validateLatestEvents(username, latestEvents);
			List<Socializing> daoItems = socializingDao.readLatestEvents(username, latestEvents);
			result = new SocializingHistory(username, latestEvents, daoItems);			
		}
		log.debug("getSocializing() call took " + (System.currentTimeMillis() - start) + "ms");
		return result;
	}
		
}
