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
import eu.ewall.platform.commons.datamodel.sensing.VisualSensing;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;
import eu.ewall.platform.middleware.datamanager.dao.ewallsystem.SensingEnvironmentDao;
import eu.ewall.platform.middleware.datamanager.dao.sensing.VisualSensingDao;

/**
 * The Class VisualSensingServiceImpl.
 * 
 * @author EMIRMOS
 */
@Service("visualSensingService")
public class VisualSensingServiceImpl implements DisposableBean {

	/** The log. */
	Logger log = LoggerFactory.getLogger(VisualSensingServiceImpl.class);

	/** The visual sensing dao. */
	private VisualSensingDao visualSensingDao;

	/** The sens environment dao. */
	private SensingEnvironmentDao sensEnvironmentDao;

	/**
	 * Instantiates a new user service impl.
	 */
	public VisualSensingServiceImpl() {
		visualSensingDao = new VisualSensingDao();
		sensEnvironmentDao = new SensingEnvironmentDao();
	}

	/**
	 * Gets the visual sensing between timestamps.
	 *
	 * @param username
	 *            the username
	 * @param fromtimestamp
	 *            the fromtimestamp
	 * @param totimestamp
	 *            the totimestamp
	 * @return the visual sensing between timestamps
	 */
	public List<VisualSensing> getVisualSensingBetweenTimestamps(
			String username, long fromtimestamp, long totimestamp) {
		log.debug("getVisualSensingBetweenTimestamps");

		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return visualSensingDao.getVisualSensingBetweenTimestamps(
					sensingEnvironment.getUuid(), fromtimestamp, totimestamp);

		else
			return null;

	}

	/**
	 * Delete deprecated visual sensing.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 */
	public void deleteDeprecatedVisualSensing(UUID sensing_environment_id,
			long timestamp) {
		log.debug("deleteDeprecatedVisualSensing: " + timestamp);

		visualSensingDao.deleteDeprecatedVisualSensing(sensing_environment_id,
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
