package eu.ewall.servicebrick.environment.controller;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.ewall.servicebrick.common.controller.SensorEventController;
import eu.ewall.servicebrick.common.dao.SensorEventDao;
import eu.ewall.servicebrick.common.validation.ServiceBrickInputValidator;
import eu.ewall.servicebrick.dailyfunctioning.model.DoorEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.DoorHistory;
import eu.ewall.servicebrick.dailyfunctioning.model.GasCoEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.GasCoHistory;
import eu.ewall.servicebrick.dailyfunctioning.model.GasLpgEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.GasLpgHistory;
import eu.ewall.servicebrick.dailyfunctioning.model.GasNgEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.GasNgHistory;
import eu.ewall.servicebrick.dailyfunctioning.model.HumidityEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.HumidityHistory;
import eu.ewall.servicebrick.dailyfunctioning.model.IlluminanceEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.IlluminanceHistory;
import eu.ewall.servicebrick.dailyfunctioning.model.TemperatureEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.TemperatureHistory;

@RestController
public class EnvironmentController extends SensorEventController {

	@Autowired
	public EnvironmentController(ServiceBrickInputValidator inputValidator,	SensorEventDao sensorEventDao) {
		super(inputValidator, sensorEventDao);
	}

	@RequestMapping(value = "/v1/{username}/door", method = RequestMethod.GET)
	public DoorHistory getDoor(
			@PathVariable String username,
			@RequestParam(value="location", required=false) String location,
			@RequestParam(value="from", required=false) DateTime from,
			@RequestParam(value="to", required=false) DateTime to, 
			@RequestParam(value="latestevents", required=false) Integer latestEvents) {
		return getEvents("getDoor", DoorEvent.class, DoorHistory.class, username, location, from, 
				to, latestEvents);
	}
	
	@RequestMapping(value = "/v1/{username}/humidity", method = RequestMethod.GET)
	public HumidityHistory getHumidity(
			@PathVariable String username,
			@RequestParam(value="location", required=false) String location,
			@RequestParam(value="from", required=false) DateTime from,
			@RequestParam(value="to", required=false) DateTime to, 
			@RequestParam(value="latestevents", required=false) Integer latestEvents) {
		return getEvents("getHumidity", HumidityEvent.class, HumidityHistory.class, username, location, from, 
				to, latestEvents);
	}
	
	@RequestMapping(value = "/v1/{username}/illuminance", method = RequestMethod.GET)
	public IlluminanceHistory getIlluminance(
			@PathVariable String username,
			@RequestParam(value="location", required=false) String location,
			@RequestParam(value="from", required=false) DateTime from,
			@RequestParam(value="to", required=false) DateTime to, 
			@RequestParam(value="latestevents", required=false) Integer latestEvents) {
		return getEvents("getIlluminance", IlluminanceEvent.class, IlluminanceHistory.class, username, location, from, 
				to, latestEvents);
	}
	
	@RequestMapping(value = "/v1/{username}/temperature", method = RequestMethod.GET)
	public TemperatureHistory getTemperature(
			@PathVariable String username,
			@RequestParam(value="location", required=false) String location,
			@RequestParam(value="from", required=false) DateTime from,
			@RequestParam(value="to", required=false) DateTime to, 
			@RequestParam(value="latestevents", required=false) Integer latestEvents) {
		return getEvents("getTemperature", TemperatureEvent.class, TemperatureHistory.class, username, location, from, 
				to, latestEvents);
	}
	
	@RequestMapping(value = "/v1/{username}/gases/co", method = RequestMethod.GET)
	public GasCoHistory getGasCo(
			@PathVariable String username,
			@RequestParam(value="location", required=false) String location,
			@RequestParam(value="from", required=false) DateTime from,
			@RequestParam(value="to", required=false) DateTime to, 
			@RequestParam(value="latestevents", required=false) Integer latestEvents) {
		return getEvents("getGasCo", GasCoEvent.class, GasCoHistory.class, username, location, from, 
				to, latestEvents);
	}
	
	@RequestMapping(value = "/v1/{username}/gases/lpg", method = RequestMethod.GET)
	public GasLpgHistory getGasLpg(
			@PathVariable String username,
			@RequestParam(value="location", required=false) String location,
			@RequestParam(value="from", required=false) DateTime from,
			@RequestParam(value="to", required=false) DateTime to, 
			@RequestParam(value="latestevents", required=false) Integer latestEvents) {
		return getEvents("getGaslpg", GasLpgEvent.class, GasLpgHistory.class, username, location, from, 
				to, latestEvents);
	}
	
	@RequestMapping(value = "/v1/{username}/gases/ng", method = RequestMethod.GET)
	public GasNgHistory getGasNg(
			@PathVariable String username,
			@RequestParam(value="location", required=false) String location,
			@RequestParam(value="from", required=false) DateTime from,
			@RequestParam(value="to", required=false) DateTime to, 
			@RequestParam(value="latestevents", required=false) Integer latestEvents) {
		return getEvents("getGasNg", GasNgEvent.class, GasNgHistory.class, username, location, from, 
				to, latestEvents);
	}
	
}
