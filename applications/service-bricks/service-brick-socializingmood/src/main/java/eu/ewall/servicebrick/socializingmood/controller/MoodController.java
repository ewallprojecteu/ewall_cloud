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
import eu.ewall.servicebrick.socializingmood.dao.MoodDao;
import eu.ewall.servicebrick.socializingmood.model.Mood;
import eu.ewall.servicebrick.socializingmood.model.MoodHistory;

@RestController
public class MoodController {

	private static final Logger log = LoggerFactory.getLogger(MoodController.class);
	
	private ServiceBrickInputValidator inputValidator;
	private MoodDao moodDao;
	
	@Autowired
	public MoodController(ServiceBrickInputValidator inputValidator, MoodDao moodDao) {
		this.inputValidator = inputValidator;
		this.moodDao = moodDao;
	}

	@RequestMapping(value = "/v1/{username}/mood", method = RequestMethod.GET)
	public MoodHistory getMood(
			@PathVariable String username,
			@RequestParam(value="from", required=false) DateTime from,
			@RequestParam(value="to", required=false) DateTime to, 
			@RequestParam(value="latestevents", required=false) Integer latestEvents) {
		long start = System.currentTimeMillis();
		MoodHistory result;
		if(from != null) {
			log.debug("Request for mood events for " + username + " from " + from + " to " + to);
			inputValidator.validateTimeInterval(username, from, to);
			List<Mood> daoItems = moodDao.readEvents(username, from, to);
			result = new MoodHistory(username, from, to, daoItems);
		} else {
			log.debug("Request for latest " + latestEvents + " mood events for " + username);
			inputValidator.validateLatestEvents(username, latestEvents);
			List<Mood> daoItems = moodDao.readLatestEvents(username, latestEvents);
			result = new MoodHistory(username, latestEvents, daoItems);			
		}
		log.debug("getMood() call took " + (System.currentTimeMillis() - start) + "ms");
		return result;
	}
		
}
