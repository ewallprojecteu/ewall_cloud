package eu.ewall.platform.profilingserver.controllers;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.ewall.platform.commons.datamodel.ewallsystem.Heartbeat;
import eu.ewall.platform.commons.datamodel.ewallsystem.HeartbeatStatus;
import eu.ewall.platform.profilingserver.services.HeartbeatServiceImpl;

/**
 * The Class HeartbeatController.
 * 
 * @author 
 * 
 */
@RestController
public class HeartbeatController {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(HeartbeatController.class);

	/** The heartbeat service. */
	@Autowired
	private HeartbeatServiceImpl heartbeatService;

	/**
	 * Instantiates a new heartbeat controller.
	 */
	public HeartbeatController() {
	}

	@RequestMapping(value = "users/{username}/heartbeat", method = RequestMethod.POST)
	public ResponseEntity<String>  addHeartbeat(@PathVariable String username, @RequestParam(value="ip", required=false) String ip, @RequestParam("updateFrequencySecs") Integer updateFrequencySecs) {
		
		//This should not happen as it is part of the url
		if(username == null || updateFrequencySecs == null) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

		if(heartbeatService.addHeartbeat(username, ip, updateFrequencySecs)){
			return new ResponseEntity<String>(HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);			
		}
	}

	
	@RequestMapping(value = "users/{username}/heartbeat/isalive", method = RequestMethod.GET)
	public ResponseEntity<HeartbeatStatus> isAlive(@PathVariable String username) {

		LOG.debug("isAlive: " + username);

		Heartbeat heartbeat;
		HeartbeatStatus heartbeatStatus;

		try {
			heartbeat = heartbeatService.getLastHeartbeat(username);
			if(heartbeat == null) {
				heartbeatStatus = new HeartbeatStatus(false, new Heartbeat());
				return new ResponseEntity<HeartbeatStatus>(heartbeatStatus, HttpStatus.OK);
			} else {

				Date heartbeatDate = heartbeat.getDate();
				Date now = new Date();
				if((now.getTime() - heartbeatDate.getTime()) <= (heartbeat.getUpdateFrequencySecs()*1000)){
					heartbeatStatus = new HeartbeatStatus(true, heartbeat);
					return new ResponseEntity<HeartbeatStatus>(heartbeatStatus, HttpStatus.OK);
				} else {
					heartbeatStatus = new HeartbeatStatus(false, heartbeat);
					return new ResponseEntity<HeartbeatStatus>(heartbeatStatus, HttpStatus.OK);
				}
			}

		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

	
	@RequestMapping(value = "users/{username}/heartbeat", method = RequestMethod.GET)
	public ResponseEntity<List<Heartbeat>> getHeartbeats(@PathVariable String username, @RequestParam(value="from", required=false) @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm") Date from, @RequestParam(value="to", required=false) @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm") Date to, @RequestParam(value="lastN", required=false) Integer lastN) {
		try {
			if(lastN!= null && lastN.intValue()>=0) {
				List<Heartbeat> heartbeats = heartbeatService.getLastNHeartbeats(username, lastN);
				return new ResponseEntity<List<Heartbeat>>(heartbeats, HttpStatus.OK);
			} else if(from != null && to != null && from.before(to)) {
					List<Heartbeat> heartbeats = heartbeatService.getHeartbeats(username, from, to);
					return new ResponseEntity<List<Heartbeat>>(heartbeats, HttpStatus.OK);
				} else {
					LOG.warn("Missing input parameters");
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}		

		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}


	
	
}
