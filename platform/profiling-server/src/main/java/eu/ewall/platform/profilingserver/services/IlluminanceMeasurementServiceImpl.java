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
import eu.ewall.platform.commons.datamodel.measure.IlluminanceMeasurement;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;
import eu.ewall.platform.middleware.datamanager.dao.ewallsystem.SensingEnvironmentDao;
import eu.ewall.platform.middleware.datamanager.dao.measurement.IlluminanceMeasurementDao;

/**
 * The Class IlluminanceMeasurementServiceImpl.
 * 
 * @author EMIRMOS
 * 
 */
@Service("illuminanceMeasurementService")
public class IlluminanceMeasurementServiceImpl implements DisposableBean {

	/** The log. */
	Logger log = LoggerFactory
			.getLogger(IlluminanceMeasurementServiceImpl.class);

	/** The illuminance measurement dao. */
	private IlluminanceMeasurementDao illuminanceMeasurementDao;

	/** The sens environment dao. */
	private SensingEnvironmentDao sensEnvironmentDao;

	/**
	 * Instantiates a new user service impl.
	 */
	public IlluminanceMeasurementServiceImpl() {
		illuminanceMeasurementDao = new IlluminanceMeasurementDao();
		sensEnvironmentDao = new SensingEnvironmentDao();
	}

	/**
	 * Gets the illuminance measurements by timestamp.
	 *
	 * @param username
	 *            the username
	 * @param timestamp
	 *            the timestamp
	 * @return the illuminance measurements by timestamp
	 */
	public IlluminanceMeasurement getIlluminanceMeasurementsByTimestamp(
			String username, long timestamp) {
		log.debug("getIlluminanceMeasurementsByTimestamp");

		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return illuminanceMeasurementDao
					.getIlluminanceMeasurementsByTimestamp(
							sensingEnvironment.getUuid(), timestamp);

		else
			return null;

	}

	/**
	 * Gets the illuminance measurements between timestamps.
	 *
	 * @param username
	 *            the username
	 * @param fromtimestamp
	 *            the fromtimestamp
	 * @param totimestamp
	 *            the totimestamp
	 * @param room_name
	 *            the room_name
	 * @return the illuminance measurements between timestamps
	 */
	public List<IlluminanceMeasurement> getIlluminanceMeasurementsBetweenTimestamps(
			String username, long fromtimestamp, long totimestamp,
			String room_name) {
		log.debug("getAccelerometerMeasurementsBetweenTimestamps");

		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return illuminanceMeasurementDao
					.getIlluminanceMeasurementsBetweenTimestamps(
							sensingEnvironment.getUuid(), fromtimestamp,
							totimestamp, room_name);

		else
			return null;

	}

	/**
	 * Gets the all illuminance measurements for user.
	 *
	 * @param username
	 *            the username
	 * @return the all illuminance measurements for user
	 */
	public List<IlluminanceMeasurement> getAllIlluminanceMeasurementsForUser(
			String username) {
		log.debug("getAllIlluminanceMeasurementsForUser: " + username);

		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return illuminanceMeasurementDao
					.getAllIlluminanceMeasurements(sensingEnvironment.getUuid());

		else
			return null;
	}

	/**
	 * Delete deprecated illuminance measurements.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 */
	public void deleteDeprecatedIlluminanceMeasurements(
			UUID sensing_environment_id, long timestamp) {
		log.debug("deleteDeprecatedIlluminanceMeasurements: " + timestamp);

		illuminanceMeasurementDao.deleteDeprecatedIlluminanceMeasurements(
				sensing_environment_id, timestamp);
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
