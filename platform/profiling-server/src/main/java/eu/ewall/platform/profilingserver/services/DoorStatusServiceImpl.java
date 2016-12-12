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
import eu.ewall.platform.commons.datamodel.measure.DoorStatus;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;
import eu.ewall.platform.middleware.datamanager.dao.ewallsystem.SensingEnvironmentDao;
import eu.ewall.platform.middleware.datamanager.dao.measurement.DoorStatusDao;

/**
 * The Class DoorStatusServiceImpl.
 * 
 * @author EMIRMOS
 * 
 */
@Service("doorStatusService")
public class DoorStatusServiceImpl implements DisposableBean {

	/** The log. */
	Logger log = LoggerFactory.getLogger(DoorStatusServiceImpl.class);

	/** The door status dao. */
	private DoorStatusDao doorStatusDao;

	/** The sens environment dao. */
	private SensingEnvironmentDao sensEnvironmentDao;

	/**
	 * Instantiates a new user service impl.
	 */
	public DoorStatusServiceImpl() {
		doorStatusDao = new DoorStatusDao();
		sensEnvironmentDao = new SensingEnvironmentDao();
	}

	/**
	 * Gets the CarbonMonoxideMeasurement between timestamps and room_name.
	 *
	 * @param username            the username
	 * @param fromtimestamp            the fromtimestamp
	 * @param totimestamp            the totimestamp
	 * @param room_name the room_name
	 * @return the door status between timestamps
	 */
	public List<DoorStatus> getDoorStatusBetweenTimestamps(String username,
			long fromtimestamp, long totimestamp, String room_name) {
		log.debug("getDoorStatusBetweenTimestamps");

		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return doorStatusDao.getDoorStatusBetweenTimestamps(
					sensingEnvironment.getUuid(), fromtimestamp, totimestamp,
					room_name);

		else
			return null;

	}

	/**
	 * Delete deprecated door statuses.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 */
	public void deleteDeprecatedDoorStatuses(UUID sensing_environment_id,
			long timestamp) {
		log.debug("deleteDeprecatedDoorStatuses: " + timestamp);

		doorStatusDao.deleteDeprecatedDoorStatuses(
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
