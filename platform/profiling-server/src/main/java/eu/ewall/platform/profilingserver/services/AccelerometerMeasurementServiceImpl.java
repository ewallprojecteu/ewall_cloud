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
import eu.ewall.platform.commons.datamodel.measure.AccelerometerMeasurement;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;
import eu.ewall.platform.middleware.datamanager.dao.ewallsystem.SensingEnvironmentDao;
import eu.ewall.platform.middleware.datamanager.dao.measurement.AccelerometerMeasurementDao;

/**
 * The Class UserServiceImpl.
 * 
 * @author eandgrg
 */
@Service("accelerometerMeasurementService")
public class AccelerometerMeasurementServiceImpl implements DisposableBean {

	/** The log. */
	Logger log = LoggerFactory
			.getLogger(AccelerometerMeasurementServiceImpl.class);

	/** The accelerometer measurement dao. */
	private AccelerometerMeasurementDao accelerometerMeasurementDao;

	/** The sens environment dao. */
	private SensingEnvironmentDao sensEnvironmentDao;

	/**
	 * Instantiates a new user service impl.
	 */
	public AccelerometerMeasurementServiceImpl() {
		accelerometerMeasurementDao = new AccelerometerMeasurementDao();
		sensEnvironmentDao = new SensingEnvironmentDao();
	}

	/**
	 * Gets the accelerometer measurements by timestamp.
	 *
	 * @param username
	 *            the username
	 * @param timestamp
	 *            the timestamp
	 * @return the accelerometer measurements by timestamp
	 */
	public AccelerometerMeasurement getAccelerometerMeasurementsByTimestamp(
			String username, long timestamp) {
		log.debug("getAccelerometerMeasurementsByTimestamp");

		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return accelerometerMeasurementDao
					.getAccelerometerMeasurementsByTimestamp(
							sensingEnvironment.getUuid(), timestamp);

		else
			return null;

	}

	/**
	 * Gets the accelerometer measurements between timestamps.
	 *
	 * @param username
	 *            the username
	 * @param fromtimestamp
	 *            the fromtimestamp
	 * @param totimestamp
	 *            the totimestamp
	 * @return the accelerometer measurements between timestamps
	 */
	public List<AccelerometerMeasurement> getAccelerometerMeasurementsBetweenTimestamps(
			String username, long fromtimestamp, long totimestamp) {
		log.debug("getAccelerometerMeasurementsBetweenTimestamps");

		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return accelerometerMeasurementDao
					.getAccelerometerMeasurementsBetweenTimestamps(
							sensingEnvironment.getUuid(), fromtimestamp,
							totimestamp);

		else
			return null;

	}

	/**
	 * Gets the all accelerometer measurements for user.
	 *
	 * @param username
	 *            the username
	 * @return the all accelerometer measurements for user
	 */
	public List<AccelerometerMeasurement> getAllAccelerometerMeasurementsForUser(
			String username) {
		log.debug("getAllAccelerometerMeasurementsForUser: " + username);

		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return accelerometerMeasurementDao
					.getAllAccelerometerMeasurements(sensingEnvironment
							.getUuid());

		else
			return null;
	}

	/**
	 * Delete deprecated accelerometer measurements.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 */
	public void deleteDeprecatedAccelerometerMeasurements(
			UUID sensing_environment_id, long timestamp) {
		log.debug("deleteDeprecatedAccelerometerMeasurements: " + timestamp);

		accelerometerMeasurementDao.deleteDeprecatedAccelerometerMeasurements(
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
