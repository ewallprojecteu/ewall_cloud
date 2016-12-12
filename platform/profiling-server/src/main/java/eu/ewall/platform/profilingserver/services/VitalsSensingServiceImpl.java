/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.profilingserver.services;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import eu.ewall.platform.commons.datamodel.ewallsystem.SensingEnvironment;
import eu.ewall.platform.commons.datamodel.measure.BloodPressureMeasurement;
import eu.ewall.platform.commons.datamodel.measure.HeartRateMeasurement;
import eu.ewall.platform.commons.datamodel.measure.OxygenSaturationMeasurement;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;
import eu.ewall.platform.middleware.datamanager.dao.ewallsystem.SensingEnvironmentDao;
import eu.ewall.platform.middleware.datamanager.dao.sensing.VitalSensingDao;

/**
 * The Class HealthSensingServiceImpl.
 * 
 * @author emirmos
 */
@Service("healthSensingService")
public class VitalsSensingServiceImpl implements DisposableBean {

	/** The log. */
	Logger log = LoggerFactory.getLogger(VitalsSensingServiceImpl.class);

	/** The vital sensing dao. */
	private VitalSensingDao vitalSensingDao;

	/** The sens environment dao. */
	private SensingEnvironmentDao sensEnvironmentDao;

	/**
	 * Instantiates a new health sensing service impl.
	 */
	public VitalsSensingServiceImpl() {
		vitalSensingDao = new VitalSensingDao();
		sensEnvironmentDao = new SensingEnvironmentDao();
	}

	/**
	 * Gets the heart rate between timestamps.
	 *
	 * @param username
	 *            the username
	 * @param fromtimestamp
	 *            the fromtimestamp
	 * @param totimestamp
	 *            the totimestamp
	 * @param latestEvents
	 *            the latest events
	 * @return the heart rate between timestamps
	 */
	public List<HeartRateMeasurement> getHeartRateBetweenTimestamps(
			String username, Long fromtimestamp, Long totimestamp,
			Integer latestEvents) {
		log.debug("getHeartRateBetweenTimestamps");

		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return vitalSensingDao.getHeartRateBetweenTimestamps(
					sensingEnvironment.getUuid(), fromtimestamp, totimestamp,
					latestEvents);

		else
			return null;

	}

	/**
	 * Gets the oxygen saturation between timestamps.
	 *
	 * @param username
	 *            the username
	 * @param fromtimestamp
	 *            the fromtimestamp
	 * @param totimestamp
	 *            the totimestamp
	 * @param latestEvents
	 *            the latest events
	 * @return the oxygen saturation between timestamps
	 */
	public List<OxygenSaturationMeasurement> getOxygenSaturationBetweenTimestamps(
			String username, Long fromtimestamp, Long totimestamp,
			Integer latestEvents) {
		log.debug("getOxygenSaturationBetweenTimestamps");

		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return vitalSensingDao.getOxygenSaturationBetweenTimestamps(
					sensingEnvironment.getUuid(), fromtimestamp, totimestamp,
					latestEvents);

		else
			return null;

	}

	/**
	 * Gets the blood pressure between timestamps.
	 *
	 * @param username
	 *            the username
	 * @param fromtimestamp
	 *            the fromtimestamp
	 * @param totimestamp
	 *            the totimestamp
	 * @param latestEvents
	 *            the latest events
	 * @return the blood pressure between timestamps
	 */
	public List<BloodPressureMeasurement> getBloodPressureBetweenTimestamps(
			String username, Long fromtimestamp, Long totimestamp,
			Integer latestEvents) {
		log.debug("getBloodPressureBetweenTimestamps");

		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null) {

			return vitalSensingDao.getBloodPressureBetweenTimestamps(
					sensingEnvironment.getUuid(), fromtimestamp, totimestamp,
					latestEvents);

		}

		else
			return null;

	}

	/**
	 * Delete deprecated vitals sensing.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 */
	public void deleteDeprecatedVitalsSensing(UUID sensing_environment_id,
			long timestamp) {
		log.debug("deleteDeprecatedVitalsSensing: " + timestamp);

		vitalSensingDao.deleteDeprecatedVitalsSensing(sensing_environment_id,
				timestamp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() throws Exception {
		MongoDBFactory.close();
	}

}
