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
import eu.ewall.platform.commons.datamodel.measure.HumidityMeasurement;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;
import eu.ewall.platform.middleware.datamanager.dao.ewallsystem.SensingEnvironmentDao;
import eu.ewall.platform.middleware.datamanager.dao.measurement.HumidityMeasurementDao;

/**
 * The Class HumidityMeasurementServiceImpl.
 * 
 * @author EMIRMOS
 * 
 */
@Service("humidityMeasurementService")
public class HumidityMeasurementServiceImpl implements DisposableBean {

	/** The log. */
	Logger log = LoggerFactory.getLogger(HumidityMeasurementServiceImpl.class);

	/** The humidity measurement dao. */
	private HumidityMeasurementDao humidityMeasurementDao;

	/** The sens environment dao. */
	private SensingEnvironmentDao sensEnvironmentDao;

	/**
	 * Instantiates a new user service impl.
	 */
	public HumidityMeasurementServiceImpl() {
		humidityMeasurementDao = new HumidityMeasurementDao();
		sensEnvironmentDao = new SensingEnvironmentDao();
	}

	/**
	 * Gets the humidity measurements by timestamp.
	 *
	 * @param username
	 *            the username
	 * @param timestamp
	 *            the timestamp
	 * @return the humidity measurements by timestamp
	 */
	public HumidityMeasurement getHumidityMeasurementsByTimestamp(
			String username, long timestamp) {
		log.debug("getHumidityMeasurementsByTimestamp");

		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return humidityMeasurementDao.getHumidityMeasurementsByTimestamp(
					sensingEnvironment.getUuid(), timestamp);

		else
			return null;

	}

	/**
	 * Gets the humidity measurements between timestamps.
	 *
	 * @param username            the username
	 * @param fromtimestamp            the fromtimestamp
	 * @param totimestamp            the totimestamp
	 * @param room_name the room_name
	 * @return the humidity measurements between timestamps
	 */
	public List<HumidityMeasurement> getHumidityMeasurementsBetweenTimestamps(
			String username, long fromtimestamp, long totimestamp,
			String room_name) {
		log.debug("getAccelerometerMeasurementsBetweenTimestamps");

		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return humidityMeasurementDao
					.getHumidityMeasurementsBetweenTimestamps(
							sensingEnvironment.getUuid(), fromtimestamp,
							totimestamp, room_name);

		else
			return null;

	}

	/**
	 * Gets the all humidity measurements for user.
	 *
	 * @param username
	 *            the username
	 * @return the all humidity measurements for user
	 */
	public List<HumidityMeasurement> getAllHumidityMeasurementsForUser(
			String username) {
		log.debug("getAllHumidityMeasurementsForUser: " + username);

		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return humidityMeasurementDao.getAllHumidityMeasurements(
					sensingEnvironment.getUuid());

		else
			return null;
	}

	/**
	 * Delete deprecated humidity measurements.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 */
	public void deleteDeprecatedHumidityMeasurements(
			UUID sensing_environment_id, long timestamp) {
		log.debug("deleteDeprecatedHumidityMeasurements: " + timestamp);

		humidityMeasurementDao.deleteDeprecatedHumidityMeasurements(
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
