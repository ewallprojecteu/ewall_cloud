package eu.ewall.servicebrick.dailyfunctioning.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.ewall.servicebrick.common.dao.DataManagerUpdatesDao;
import eu.ewall.servicebrick.common.model.AccelerometerUpdate;
import eu.ewall.servicebrick.common.validation.ServiceBrickInputValidator;
import eu.ewall.servicebrick.dailyfunctioning.model.SleepHistory;

@RestController
public class SleepController {
	
	private static final Logger log = LoggerFactory.getLogger(DomoticsController.class);
  
	private ServiceBrickInputValidator inputValidator;
	
	private DataManagerUpdatesDao dataManagerUpdatesDao;
	
	

	public SleepController(){
	}
	
	@RequestMapping(value = "/v1/{username}/sleep", method = RequestMethod.GET)
	public String getResponse(
			@PathVariable String username) {
		
		// sleepDao.getSleep();
		
		return "Test " + username;
	}
	
	//@RequestMapping(value="/testsleep", method=RequestMethod.GET)
	//public SleepHistory testSleep(){
	//	return new SleepHistory("IvanIvanov", 1234567890, 42);
	//}
}
