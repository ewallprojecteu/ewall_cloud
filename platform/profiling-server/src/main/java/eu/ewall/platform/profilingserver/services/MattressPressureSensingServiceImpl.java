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
import eu.ewall.platform.commons.datamodel.measure.MattressPressureSensing;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;
import eu.ewall.platform.middleware.datamanager.dao.ewallsystem.SensingEnvironmentDao;
import eu.ewall.platform.middleware.datamanager.dao.sensing.MattressPressureSensingDao;

/**
 * The Class MattressPressureSensingServiceImpl.
 * 
 * @author EMIRMOS
 * 
 */
@Service("mattressPressureSensingService")
public class MattressPressureSensingServiceImpl implements DisposableBean {

	/** The log. */
	Logger log = LoggerFactory
			.getLogger(MattressPressureSensingServiceImpl.class);

	/** The mattress pressure sensing dao. */
	private MattressPressureSensingDao mattressPressureSensingDao;

	/** The sens environment dao. */
	private SensingEnvironmentDao sensEnvironmentDao;

	/**
	 * Instantiates a new user service impl.
	 */
	public MattressPressureSensingServiceImpl() {
		mattressPressureSensingDao = new MattressPressureSensingDao();
		sensEnvironmentDao = new SensingEnvironmentDao();
	}

	/**
	 * Gets the MattressPressureSensing between timestamps and room_name.
	 *
	 * @param username
	 *            the username
	 * @param fromtimestamp
	 *            the fromtimestamp
	 * @param totimestamp
	 *            the totimestamp
	 * @param room_name
	 *            the room_name
	 * @return the MattressPressureSensing between timestamps
	 */
	public List<MattressPressureSensing> getMattressPressureSensingBetweenTimestamps(
			String username, long fromtimestamp, long totimestamp,
			String room_name) {
		log.debug("getMattressPressureSensingBetweenTimestamps");

		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return mattressPressureSensingDao
					.getMattressPressureSensingBetweenTimestamps(
							sensingEnvironment.getUuid(), fromtimestamp,
							totimestamp, room_name);

		else
			return null;

	}

	/**
	 * Delete deprecated mattress pressure sensing.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 */
	public void deleteDeprecatedMattressPressureSensing(
			UUID sensing_environment_id, long timestamp) {
		log.debug("deleteDeprecatedMattressPressureSensing: " + timestamp);

		mattressPressureSensingDao.deleteDeprecatedMattressPressureSensing(
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
