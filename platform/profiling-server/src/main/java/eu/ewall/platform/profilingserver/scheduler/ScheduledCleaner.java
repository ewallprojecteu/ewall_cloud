/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.profilingserver.scheduler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import eu.ewall.platform.commons.datamodel.ewallsystem.SensingEnvironment;
import eu.ewall.platform.middleware.datamanager.dao.ewallsystem.SensingEnvironmentDao;
import eu.ewall.platform.profilingserver.services.AccelerometerMeasurementServiceImpl;
import eu.ewall.platform.profilingserver.services.AppliancePowerSensingServiceImpl;
import eu.ewall.platform.profilingserver.services.DoorStatusServiceImpl;
import eu.ewall.platform.profilingserver.services.GPScoordinatesServiceImpl;
import eu.ewall.platform.profilingserver.services.GasesMeasurementServiceImpl;
import eu.ewall.platform.profilingserver.services.HumidityMeasurementServiceImpl;
import eu.ewall.platform.profilingserver.services.IlluminanceMeasurementServiceImpl;
import eu.ewall.platform.profilingserver.services.MattressPressureSensingServiceImpl;
import eu.ewall.platform.profilingserver.services.MovementMeasurementServiceImpl;
import eu.ewall.platform.profilingserver.services.SpeakerSensingServiceImpl;
import eu.ewall.platform.profilingserver.services.TemperatureMeasurementServiceImpl;
import eu.ewall.platform.profilingserver.services.VisualSensingServiceImpl;
import eu.ewall.platform.profilingserver.services.VitalsSensingServiceImpl;

/**
 * The Class ScheduleCleaner.
 */
@Component
public class ScheduledCleaner {

	/** The log. */
	private static Logger LOG = LoggerFactory.getLogger(ScheduledCleaner.class);

	/** The Constant CLEAR_OLD_DATA_INTERVAL. */
	private static final long CLEAR_OLD_DATA_INTERVAL = 1 * 24 * 60 * 60 * 1000; // 1day

	/** The documents deprecated timestamp period. */
	@Value("${documents.deprecatedTimestampPeriod}")
	private String documentsDeprecatedTimestampPeriod;

	/** The sens environment dao. */
	private SensingEnvironmentDao sensEnvironmentDao;

	/** The accelerometer measurement service. */
	@Autowired
	private AccelerometerMeasurementServiceImpl accelerometerMeasurementService;

	/** The appliance power sensing service. */
	@Autowired
	private AppliancePowerSensingServiceImpl appliancePowerSensingService;

	/** The humidity measurement service. */
	@Autowired
	private DoorStatusServiceImpl doorStatusService;

	/** The humidity measurement service. */
	@Autowired
	private GasesMeasurementServiceImpl gasesMeasurementService;

	/** The gps coordinates service. */
	@Autowired
	private GPScoordinatesServiceImpl gpsCoordinatesService;

	/** The humidity measurement service. */
	@Autowired
	private HumidityMeasurementServiceImpl humidityMeasurementService;

	/** The illuminance measurement service. */
	@Autowired
	private IlluminanceMeasurementServiceImpl illuminanceMeasurementService;

	/** The humidity measurement service. */
	@Autowired
	private MattressPressureSensingServiceImpl mattressPressureSensingService;

	/** The movement measurement service. */
	@Autowired
	private MovementMeasurementServiceImpl movementMeasurementService;

	/** The speaker sensing service. */
	@Autowired
	private SpeakerSensingServiceImpl speakerSensingService;

	/** The temperature measurement service. */
	@Autowired
	private TemperatureMeasurementServiceImpl temperatureMeasurementService;

	/** The visual sensing service. */
	@Autowired
	private VisualSensingServiceImpl visualSensingService;

	/** The vitals sensing service. */
	@Autowired
	private VitalsSensingServiceImpl vitalsSensingService;
	
	/**
	 * Instantiates a new scheduled cleaner.
	 */
	public ScheduledCleaner() {
		sensEnvironmentDao = new SensingEnvironmentDao();
	}

	/**
	 * Clear old database documents.<br>
	 * Caution! Uncomment "@Scheduled" annotation if you know what are you doing
	 */
	// @Scheduled(fixedDelay = CLEAR_OLD_DATA_INTERVAL)
	public void clearOldDatabaseDocuments() {
		LOG.debug("clearOldDatabaseDocuments");

		/* Minimal period */
		long documentsDeprecatedTimestampPeriodMinLong = 7 * 24 * 60 * 60
				* 1000; // 7 days

		try {
			long documentsDeprecatedTimestampPeriodLong = Long
					.parseLong(documentsDeprecatedTimestampPeriod);
			if (documentsDeprecatedTimestampPeriodLong < documentsDeprecatedTimestampPeriodMinLong) {
				/* Default period */
				documentsDeprecatedTimestampPeriodLong = 30 * 24 * 60 * 60
						* 1000; // 30 days
			}

			long timestampPoint = System.currentTimeMillis()
					- Long.parseLong(documentsDeprecatedTimestampPeriod);

			List<SensingEnvironment> sensingEnvironments = sensEnvironmentDao
					.getAllSensingEnvironments();
			for (SensingEnvironment sensEnv : sensingEnvironments) {
				deleteDeprecatedMeasurements(timestampPoint, sensEnv);
			}

		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}

	/**
	 * Delete deprecated measurements.
	 *
	 * @param timestampPoint
	 *            the timestamp point
	 * @param sensEnv
	 *            the sens env
	 */
	private void deleteDeprecatedMeasurements(long timestampPoint,
			SensingEnvironment sensEnv) {
		accelerometerMeasurementService
				.deleteDeprecatedAccelerometerMeasurements(sensEnv.getUuid(),
						timestampPoint);
		appliancePowerSensingService.deleteDeprecatedAppliancePowerSensing(
				sensEnv.getUuid(), timestampPoint);
		doorStatusService.deleteDeprecatedDoorStatuses(sensEnv.getUuid(),
				timestampPoint);
		gasesMeasurementService.deleteDeprecatedGasesMeasurements(
				sensEnv.getUuid(), timestampPoint);
		gpsCoordinatesService.deleteDeprecatedGpsCoordinates(sensEnv.getUuid(),
				timestampPoint);
		humidityMeasurementService.deleteDeprecatedHumidityMeasurements(
				sensEnv.getUuid(), timestampPoint);
		illuminanceMeasurementService.deleteDeprecatedIlluminanceMeasurements(
				sensEnv.getUuid(), timestampPoint);
		mattressPressureSensingService.deleteDeprecatedMattressPressureSensing(
				sensEnv.getUuid(), timestampPoint);
		movementMeasurementService.deleteDeprecatedMovementMeasurements(
				sensEnv.getUuid(), timestampPoint);
		speakerSensingService.deleteDeprecatedSpeakerSensing(sensEnv.getUuid(),
				timestampPoint);
		temperatureMeasurementService.deleteDeprecatedTemperatureMeasurements(
				sensEnv.getUuid(), timestampPoint);
		visualSensingService.deleteDeprecatedVisualSensing(sensEnv.getUuid(),
				timestampPoint);
		vitalsSensingService.deleteDeprecatedVitalsSensing(sensEnv.getUuid(),
				timestampPoint);
	}
}
