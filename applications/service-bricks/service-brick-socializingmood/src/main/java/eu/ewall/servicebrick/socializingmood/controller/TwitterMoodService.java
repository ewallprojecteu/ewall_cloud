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
import eu.ewall.servicebrick.socializingmood.dao.MoodTwitterDao;
import eu.ewall.servicebrick.socializingmood.model.MoodTwitter;
import eu.ewall.servicebrick.socializingmood.model.MoodTwitterHistory;

@RestController
public class TwitterMoodService {
	
	private static final Logger log = LoggerFactory.getLogger(SocializingController.class);
	
	private ServiceBrickInputValidator inputValidator;
	private MoodTwitterDao moodTwitterDao;
	
	@Autowired
	public TwitterMoodService(ServiceBrickInputValidator inputValidator, MoodTwitterDao moodTwitterDao) {
		this.inputValidator = inputValidator;
		this.moodTwitterDao = moodTwitterDao;
	}

	@RequestMapping(value = "/v1/{username}/twitterMood", method = RequestMethod.GET)
	public MoodTwitterHistory getTwitterMood(
			@PathVariable String username,
			@RequestParam(value="from", required=true) DateTime from,
			@RequestParam(value="to", required=true) DateTime to) {
		MoodTwitterHistory result;
		
		log.debug("Request for mood for " + username + " from " + from + " to " + to);
		inputValidator.validateTimeInterval(username, from, to);
		
		List<MoodTwitter> daoItems = moodTwitterDao.readEvents(username, from, to);
		result = new MoodTwitterHistory(username, from, to, daoItems);
		
		return result;
	}
}
