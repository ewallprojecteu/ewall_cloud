package eu.ewall.servicebrick.dailyfunctioning.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.ewall.servicebrick.common.dao.DataManagerUpdatesDao;
import eu.ewall.servicebrick.common.model.SensorsUpdates;
import eu.ewall.servicebrick.common.validation.ServiceBrickInputValidator;
import eu.ewall.servicebrick.dailyfunctioning.dao.DomoticsDao;
//import eu.ewall.servicebrick.dailyfunctioning.model.DomoticsEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.DomoticsHistory;
import eu.ewall.servicebrick.dailyfunctioning.model.MattressSensorEvent;

@RestController
public class DomoticsController {
	
	private static final Logger log = LoggerFactory.getLogger(DomoticsController.class);
  
	private ServiceBrickInputValidator inputValidator;
	private DomoticsDao domoticsDao;
	
	private DataManagerUpdatesDao dataManagerUpdatesDao;
	

	public DomoticsController(/*ServiceBrickInputValidator inputValidator/*, 
			DataManagerUpdatesDao  dataManagerUpdatesDao*/){
		//this.inputValidator = inputValidator;
//		this.dataManagerUpdatesDao =  dataManagerUpdatesDao;
	}
	
	@RequestMapping(value = "/v1/{username}/domotics", method = RequestMethod.GET)
	public DomoticsHistory getDomotics(
			@PathVariable String username) {
		long start = System.currentTimeMillis();
	//	DomoticsHistory result;
		log.debug("Request for domotics events for " + username );
		SensorsUpdates sensorsUpdates = null;
		sensorsUpdates = dataManagerUpdatesDao.getSensorsReadingByUsername(username);
		DomoticsHistory result = new DomoticsHistory(username, sensorsUpdates);
		log.debug("getDomotics() call took " + (System.currentTimeMillis() - start) + "ms");
		return result;
	}
}
