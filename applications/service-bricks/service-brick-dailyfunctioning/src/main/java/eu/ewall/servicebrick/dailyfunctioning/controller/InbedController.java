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
import eu.ewall.servicebrick.dailyfunctioning.dao.InbedDao;
import eu.ewall.servicebrick.dailyfunctioning.model.InbedEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.InbedHistory;

@RestController
public class InbedController {

	private static final Logger log = LoggerFactory.getLogger(InbedController.class);
	
	private ServiceBrickInputValidator inputValidator;
	private InbedDao inbedDao;
	
	@Autowired
	public InbedController(ServiceBrickInputValidator inputValidator, InbedDao inbedDao) {
		this.inputValidator = inputValidator;
		this.inbedDao = inbedDao;
	}

	@RequestMapping(value = "/v1/{username}/inbed", method = RequestMethod.GET)
	public InbedHistory getInbed(
			@PathVariable String username,
			@RequestParam(value="from", required=false) DateTime from,
			@RequestParam(value="to", required=false) DateTime to, 
			@RequestParam(value="latestevents", required=false) Integer latestEvents) {
		long start = System.currentTimeMillis();
		InbedHistory result;
		if(from != null) {
			log.debug("Request for inbed events for " + username + " from " + from + " to " + to);
			inputValidator.validateTimeInterval(username, from, to);
			List<InbedEvent> daoItems = inbedDao.readEvents(username, from, to);
			result = new InbedHistory(username, from, to, daoItems);
		} else {
			log.debug("Request for latest " + latestEvents + " inbed events for " + username);
			inputValidator.validateLatestEvents(username, latestEvents);
			List<InbedEvent> daoItems = inbedDao.readLatestEvents(username, latestEvents);
			result = new InbedHistory(username, latestEvents, daoItems);			
		}
		log.debug("getInbed() call took " + (System.currentTimeMillis() - start) + "ms");
		return result;
	}
		
}
