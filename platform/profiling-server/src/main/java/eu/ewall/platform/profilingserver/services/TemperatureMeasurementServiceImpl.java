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
import eu.ewall.platform.commons.datamodel.measure.TemperatureMeasurement;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;
import eu.ewall.platform.middleware.datamanager.dao.ewallsystem.SensingEnvironmentDao;
import eu.ewall.platform.middleware.datamanager.dao.measurement.TemperatureMeasurementDao;

/**
 * The Class TemperatureMeasurementServiceImpl.
 * 
 * @author EMIRMOS
 *
 */
@Service("temperatureMeasurementService")
public class TemperatureMeasurementServiceImpl implements DisposableBean {

	/** The log. */
	Logger log = LoggerFactory
			.getLogger(TemperatureMeasurementServiceImpl.class);

	/** The temperature measurement dao. */
	private TemperatureMeasurementDao temperatureMeasurementDao;

	/** The sens environment dao. */
	private SensingEnvironmentDao sensEnvironmentDao;

	/**
	 * Instantiates a new user service impl.
	 */
	public TemperatureMeasurementServiceImpl() {
		temperatureMeasurementDao = new TemperatureMeasurementDao();
		sensEnvironmentDao = new SensingEnvironmentDao();
	}

	/**
	 * Gets the temperature measurements by timestamp.
	 *
	 * @param username
	 *            the username
	 * @param timestamp
	 *            the timestamp
	 * @return the temperature measurements by timestamp
	 */
	public TemperatureMeasurement getTemperatureMeasurementsByTimestamp(
			String username, long timestamp) {
		log.debug("getTemperatureMeasurementsByTimestamp");

		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return temperatureMeasurementDao
					.getTemperatureMeasurementsByTimestamp(
							sensingEnvironment.getUuid(), timestamp);

		else
			return null;

	}

	/**
	 * Gets the temperature measurements between timestamps.
	 *
	 * @param username
	 *            the username
	 * @param fromtimestamp
	 *            the fromtimestamp
	 * @param totimestamp
	 *            the totimestamp
	 * @param room_name
	 *            the room_name
	 * @return the temperature measurements between timestamps
	 */
	public List<TemperatureMeasurement> getTemperatureMeasurementsBetweenTimestamps(
			String username, long fromtimestamp, long totimestamp,
			String room_name) {
		log.debug("getAccelerometerMeasurementsBetweenTimestamps");

		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return temperatureMeasurementDao
					.getTemperatureMeasurementsBetweenTimestamps(
							sensingEnvironment.getUuid(), fromtimestamp,
							totimestamp, room_name);

		else
			return null;

	}

	/**
	 * Gets the all temperature measurements for user.
	 *
	 * @param username
	 *            the username
	 * @return the all temperature measurements for user
	 */
	public List<TemperatureMeasurement> getAllTemperatureMeasurementsForUser(
			String username) {
		log.debug("getAllTemperatureMeasurementsForUser: " + username);

		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return temperatureMeasurementDao
					.getAllTemperatureMeasurements(sensingEnvironment.getUuid());

		else
			return null;
	}

	/**
	 * Delete deprecated temperature measurements.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 */
	public void deleteDeprecatedTemperatureMeasurements(
			UUID sensing_environment_id, long timestamp) {
		log.debug("deleteDeprecatedTemperatureMeasurements: " + timestamp);

		temperatureMeasurementDao.deleteDeprecatedTemperatureMeasurements(
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
