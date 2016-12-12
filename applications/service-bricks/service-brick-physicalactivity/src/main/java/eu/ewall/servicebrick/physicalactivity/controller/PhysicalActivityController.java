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
import eu.ewall.servicebrick.physicalactivity.dao.PhysicalActivityDao;
import eu.ewall.servicebrick.physicalactivity.model.PhysicalActivityEvent;
import eu.ewall.servicebrick.physicalactivity.model.PhysicalActivityHistory;

@RestController
public class PhysicalActivityController {

	private static final Logger log = LoggerFactory.getLogger(PhysicalActivityController.class);
	
	private ServiceBrickInputValidator inputValidator;
	private PhysicalActivityDao physicalActivityDao;
	
	@Autowired
	public PhysicalActivityController(ServiceBrickInputValidator inputValidator, 
			PhysicalActivityDao physicalActivityDao) {
		this.inputValidator = inputValidator;
		this.physicalActivityDao = physicalActivityDao;
	}

	@RequestMapping(value = "/v1/{username}/physicalactivity", method = RequestMethod.GET)
	public PhysicalActivityHistory getPhysicalActivity(
			@PathVariable String username,
			@RequestParam(value="from", required=false) DateTime from,
			@RequestParam(value="to", required=false) DateTime to, 
			@RequestParam(value="latestevents", required=false) Integer latestEvents) {
		long start = System.currentTimeMillis();
		PhysicalActivityHistory result;
		if(from != null) {
			log.debug("Request for physicalactivity events for " + username + " from " + from + " to " + to);
			inputValidator.validateTimeInterval(username, from, to);
			List<PhysicalActivityEvent> daoItems = physicalActivityDao.readEvents(username, from, to);
			result = new PhysicalActivityHistory(username, from, to, daoItems);
		} else {
			log.debug("Request for latest " + latestEvents + " physicalactivity events for " + username);
			inputValidator.validateLatestEvents(username, latestEvents);
			List<PhysicalActivityEvent> daoItems = physicalActivityDao.readLatestEvents(username, latestEvents);
			result = new PhysicalActivityHistory(username, latestEvents, daoItems);
		}
		log.debug("getPhysicalActivity() call took " + (System.currentTimeMillis() - start) + "ms");
		return result;
	}
		
}
