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
import eu.ewall.servicebrick.dailyfunctioning.dao.FunctioningActivityDao;
import eu.ewall.servicebrick.dailyfunctioning.model.FunctioningActivityEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.FunctioningActivityHistory;

@RestController
public class FunctioningActivityController {

	private static final Logger log = LoggerFactory.getLogger(FunctioningActivityController.class);
	
	private ServiceBrickInputValidator inputValidator;
	private FunctioningActivityDao functioningActivityDao;
	
	@Autowired
	public FunctioningActivityController(ServiceBrickInputValidator inputValidator,
			FunctioningActivityDao functioningActivityDao) {
		this.inputValidator = inputValidator;
		this.functioningActivityDao = functioningActivityDao;
	}

	@RequestMapping(value = "/v1/{username}/functioningactivity", method = RequestMethod.GET)
	public FunctioningActivityHistory getFunctioningActivity(
			@PathVariable String username,
			@RequestParam(value="from", required=false) DateTime from,
			@RequestParam(value="to", required=false) DateTime to, 
			@RequestParam(value="latestevents", required=false) Integer latestEvents) {
		long start = System.currentTimeMillis();
		FunctioningActivityHistory result;
		if(from != null) {
			log.debug("Request for functioningactivity events for " + username + " from " + from + " to " + to);
			inputValidator.validateTimeInterval(username, from, to);
			List<FunctioningActivityEvent> daoItems = functioningActivityDao.readEvents(username, from, to);
			result = new FunctioningActivityHistory(username, from, to, daoItems);
		} else {
			log.debug("Request for latest " + latestEvents + " functioningactivity events for " + username);
			inputValidator.validateLatestEvents(username, latestEvents);
			List<FunctioningActivityEvent> daoItems = functioningActivityDao.readLatestEvents(username, latestEvents);
			result = new FunctioningActivityHistory(username, latestEvents, daoItems);			
		}
		log.debug("getFunctioningActivity() call took " + (System.currentTimeMillis() - start) + "ms");
		return result;
	}
		
}
