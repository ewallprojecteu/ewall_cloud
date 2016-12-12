package eu.ewall.servicebrick.vitals.controller;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.ewall.platform.commons.datamodel.measure.BloodPressureMeasurement;
import eu.ewall.platform.commons.datamodel.measure.HeartRateMeasurement;
import eu.ewall.platform.commons.datamodel.measure.OxygenSaturationMeasurement;
import eu.ewall.servicebrick.common.dao.ProfilingServerDao;
import eu.ewall.servicebrick.common.time.UserTimeZoneProvider;
import eu.ewall.servicebrick.common.validation.ServiceBrickInputValidator;
import eu.ewall.servicebrick.vitals.model.BloodPressureHistory;
import eu.ewall.servicebrick.vitals.model.BloodPressureSbMeasurement;
import eu.ewall.servicebrick.vitals.model.HeartRateHistory;
import eu.ewall.servicebrick.vitals.model.HeartRateSbMeasurement;
import eu.ewall.servicebrick.vitals.model.OxygenSaturationHistory;
import eu.ewall.servicebrick.vitals.model.OxygenSaturationSbMeasurement;

@RestController
public class VitalsController {

	private static final Logger log = LoggerFactory.getLogger(VitalsController.class);
	
	private ServiceBrickInputValidator inputValidator;
	private ProfilingServerDao profilingServerDao;
	private UserTimeZoneProvider userTimeZoneProvider;
	
	@Autowired
	public VitalsController(ServiceBrickInputValidator inputValidator, ProfilingServerDao profilingServerDao, 
			UserTimeZoneProvider userTimeZoneProvider) {
		this.inputValidator = inputValidator;
		this.profilingServerDao = profilingServerDao;
		this.userTimeZoneProvider = userTimeZoneProvider;
	}

	private DateTimeZone getUserTimeZone(String username) {
		DateTimeZone zone = userTimeZoneProvider.getUserTimeZone(username);
		if (zone == null) {
			throw new RuntimeException("Undefined time zone for user " + username);
		}
		return zone;
	}
	
	@RequestMapping(value = "/v1/{username}/bloodpressure", method = RequestMethod.GET)
	public BloodPressureHistory getBloodPressure(
			@PathVariable String username,
			@RequestParam(value="from", required=false) DateTime from,
			@RequestParam(value="to", required=false) DateTime to,
			@RequestParam(value="latestevents", required=false) Integer latestEvents) {
		long start = System.currentTimeMillis();
		log.debug("Blood pressure requested for " + username + " from " + from + " to " + to + " latestEvents " + latestEvents);
		if(from !=null || to != null) {  			
			inputValidator.validateTimeInterval(username, from, to);
		}
		if(latestEvents != null) {
			inputValidator.validateLatestEvents(username, latestEvents);
		}
		DateTimeZone userZone = getUserTimeZone(username);
		BloodPressureMeasurement[] pfMeasurements = profilingServerDao.getBloodPressure(username, from, to, latestEvents);
		List<BloodPressureSbMeasurement> sbMeasurements = new ArrayList<>(pfMeasurements.length);
		for (BloodPressureMeasurement pfMeasurement : pfMeasurements) {
			BloodPressureSbMeasurement sbMeasurement = new BloodPressureSbMeasurement(
					new DateTime(pfMeasurement.getTimestamp(), userZone),
					pfMeasurement.getSystolicBloodPressure(), 
					pfMeasurement.getDiastolicBloodPressure());
			sbMeasurements.add(sbMeasurement);
		}
		BloodPressureHistory result = new BloodPressureHistory(username, from, to, sbMeasurements);
		log.debug("Blood pressure request took " + (System.currentTimeMillis() - start) + " ms");
		return result;
	}
	
	@RequestMapping(value = "/v1/{username}/heartrate", method = RequestMethod.GET)
	public HeartRateHistory getHeartRate(
			@PathVariable String username,
			@RequestParam(value="from", required=false) DateTime from,
			@RequestParam(value="to", required=false) DateTime to,
			@RequestParam(value="latestevents", required=false) Integer latestEvents) {
		long start = System.currentTimeMillis();
		log.debug("Heart rate requested for " + username + " from " + from + " to " + to + " latestEvents " + latestEvents);
		if(from !=null || to != null) {  			
			inputValidator.validateTimeInterval(username, from, to);
		}
		if(latestEvents != null) {
			inputValidator.validateLatestEvents(username, latestEvents);
		}
		DateTimeZone userZone = getUserTimeZone(username);
		HeartRateMeasurement[] pfMeasurements = profilingServerDao.getHeartRate(username, from, to, latestEvents);
		List<HeartRateSbMeasurement> sbMeasurements = new ArrayList<>(pfMeasurements.length);
		for (HeartRateMeasurement pfMeasurement : pfMeasurements) {
			HeartRateSbMeasurement sbMeasurement = new HeartRateSbMeasurement(
					new DateTime(pfMeasurement.getTimestamp(), userZone),
					pfMeasurement.getHeartRate(), 
					pfMeasurement.getHeartRateVariability());
			sbMeasurements.add(sbMeasurement);
		}
		HeartRateHistory result = new HeartRateHistory(username, from, to, sbMeasurements);
		log.debug("Heart rate request took " + (System.currentTimeMillis() - start) + " ms");
		return result;
	}
	
	@RequestMapping(value = "/v1/{username}/oxygensaturation", method = RequestMethod.GET)
	public OxygenSaturationHistory getOxygenSaturation(
			@PathVariable String username,
			@RequestParam(value="from", required=false) DateTime from,
			@RequestParam(value="to", required=false) DateTime to,
			@RequestParam(value="latestevents", required=false) Integer latestEvents) {
		long start = System.currentTimeMillis();
		log.debug("Oxygen saturation requested for " + username + " from " + from + " to " + to + " latestEvents " + latestEvents);
		if(from !=null || to != null) {  			
			inputValidator.validateTimeInterval(username, from, to);
		}
		if(latestEvents != null) {
			inputValidator.validateLatestEvents(username, latestEvents);
		}
		DateTimeZone userZone = getUserTimeZone(username);
		OxygenSaturationMeasurement[] pfMeasurements = profilingServerDao.getOxygenSaturation(username, from, to, latestEvents);
		List<OxygenSaturationSbMeasurement> sbMeasurements = new ArrayList<>(pfMeasurements.length);
		for (OxygenSaturationMeasurement pfMeasurement : pfMeasurements) {
			OxygenSaturationSbMeasurement sbMeasurement = new OxygenSaturationSbMeasurement(
					new DateTime(pfMeasurement.getTimestamp(), userZone),
					pfMeasurement.getOxygenSaturation());
			sbMeasurements.add(sbMeasurement);
		}
		OxygenSaturationHistory result = new OxygenSaturationHistory(username, from, to, sbMeasurements);
		log.debug("Oxygen saturation request took " + (System.currentTimeMillis() - start) + " ms");
		return result;
	}
	
}
