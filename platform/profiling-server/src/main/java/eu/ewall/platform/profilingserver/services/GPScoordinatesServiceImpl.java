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
import eu.ewall.platform.commons.datamodel.location.GPScoordinates;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;
import eu.ewall.platform.middleware.datamanager.dao.ewallsystem.SensingEnvironmentDao;
import eu.ewall.platform.middleware.datamanager.dao.location.GPSCoordinateDao;

/**
 * The Class GPScoordinatesServiceImpl.
 * 
 * @author eandgrg
 */
@Service("gpsCoordinatesService")
public class GPScoordinatesServiceImpl implements DisposableBean {

	/** The log. */
	Logger log = LoggerFactory.getLogger(GPScoordinatesServiceImpl.class);

	/** The gps coordinat dao. */
	private GPSCoordinateDao gpsCoordinatDao;

	/** The sens environment dao. */
	private SensingEnvironmentDao sensEnvironmentDao;

	/**
	 * Instantiates a new user service impl.
	 */
	public GPScoordinatesServiceImpl() {
		gpsCoordinatDao = new GPSCoordinateDao();
		sensEnvironmentDao = new SensingEnvironmentDao();
	}

	/**
	 * Gets the GPS coordinates by timestamp.
	 *
	 * @param username
	 *            the username
	 * @param timestamp
	 *            the timestamp
	 * @return the GPS coordinates by timestamp
	 */
	public GPScoordinates getGPScoordinatesByTimestamp(String username,
			long timestamp) {

		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return gpsCoordinatDao.getGPScoordinatesByTimestamp(timestamp);

		return null;
	}

	/**
	 * Gets the GPs coordinates by name.
	 *
	 * @param username
	 *            the username
	 * @param name
	 *            the name
	 * @return the GP scoordinates by name
	 */
	public GPScoordinates getGPScoordinatesByName(String username, String name) {
		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return gpsCoordinatDao.getGPScoordinatesByName(name);

		return null;
	}

	/**
	 * Gets the all gps coordinates.
	 *
	 * @param username
	 *            the username
	 * @return the all gps coordinates
	 */
	public List<GPScoordinates> getAllGPScoordinates(String username) {
		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return gpsCoordinatDao.getAllGPScoordinates();

		return null;
	}

	/**
	 * Delete deprecated gps coordinates.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 */
	public void deleteDeprecatedGpsCoordinates(UUID sensing_environment_id,
			long timestamp) {
		log.debug("deleteDeprecatedGpsCoordinates: " + timestamp);

		gpsCoordinatDao.deleteDeprecatedGpsCoordinates(
				UUID.fromString("00000000-0000-0000-0000-000000000000"),
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
