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
import eu.ewall.platform.commons.datamodel.measure.MovementMeasurement;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;
import eu.ewall.platform.middleware.datamanager.dao.ewallsystem.SensingEnvironmentDao;
import eu.ewall.platform.middleware.datamanager.dao.measurement.MovementMeasurementDao;

/**
 * The Class MovementMeasurementServiceImpl.
 * 
 * @author EMIRMOS
 * 
 */
@Service("movementMeasurementService")
public class MovementMeasurementServiceImpl implements DisposableBean {

	/** The log. */
	Logger log = LoggerFactory.getLogger(MovementMeasurementServiceImpl.class);

	/** The movement measurement dao. */
	private MovementMeasurementDao movementMeasurementDao;

	/** The sens environment dao. */
	private SensingEnvironmentDao sensEnvironmentDao;

	/**
	 * Instantiates a new user service impl.
	 */
	public MovementMeasurementServiceImpl() {
		movementMeasurementDao = new MovementMeasurementDao();
		sensEnvironmentDao = new SensingEnvironmentDao();
	}

	/**
	 * Gets the movement measurements by timestamp.
	 *
	 * @param username
	 *            the username
	 * @param timestamp
	 *            the timestamp
	 * @return the movement measurements by timestamp
	 */
	public MovementMeasurement getMovementMeasurementsByTimestamp(
			String username, long timestamp) {
		log.debug("getMovementMeasurementsByTimestamp");

		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return movementMeasurementDao.getMovementMeasurementsByTimestamp(
					sensingEnvironment.getUuid(), timestamp);

		else
			return null;

	}

	/**
	 * Gets the movement measurements between timestamps.
	 *
	 * @param username
	 *            the username
	 * @param fromtimestamp
	 *            the fromtimestamp
	 * @param totimestamp
	 *            the totimestamp
	 * @param room_name
	 *            the room_name
	 * @return the movement measurements between timestamps
	 */
	public List<MovementMeasurement> getMovementMeasurementsBetweenTimestamps(
			String username, long fromtimestamp, long totimestamp,
			String room_name) {
		log.debug("getAccelerometerMeasurementsBetweenTimestamps");

		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return movementMeasurementDao
					.getMovementMeasurementsBetweenTimestamps(
							sensingEnvironment.getUuid(), fromtimestamp,
							totimestamp, room_name);

		else
			return null;

	}

	/**
	 * Gets the all movement measurements for user.
	 *
	 * @param username
	 *            the username
	 * @return the all movement measurements for user
	 */
	public List<MovementMeasurement> getAllMovementMeasurementsForUser(
			String username) {
		log.debug("getAllMovementMeasurementsForUser: " + username);

		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return movementMeasurementDao
					.getAllMovementMeasurements(sensingEnvironment.getUuid());

		else
			return null;
	}

	/**
	 * Delete deprecated movement measurements.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 */
	public void deleteDeprecatedMovementMeasurements(
			UUID sensing_environment_id, long timestamp) {
		log.debug("deleteDeprecatedMovementMeasurements: " + timestamp);

		movementMeasurementDao.deleteDeprecatedMovementMeasurements(
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
