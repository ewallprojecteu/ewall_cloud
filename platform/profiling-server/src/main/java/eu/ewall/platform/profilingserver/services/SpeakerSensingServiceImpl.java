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
import eu.ewall.platform.commons.datamodel.sensing.SpeakerSensing;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;
import eu.ewall.platform.middleware.datamanager.dao.ewallsystem.SensingEnvironmentDao;
import eu.ewall.platform.middleware.datamanager.dao.sensing.SpeakerSensingDao;

/**
 * The Class SpeakerSensingServiceImpl.
 * 
 * @author emirmos
 */
@Service("speakerSensingService")
public class SpeakerSensingServiceImpl implements DisposableBean {

	/** The log. */
	Logger log = LoggerFactory.getLogger(SpeakerSensingServiceImpl.class);

	/** The speaker sensing dao. */
	private SpeakerSensingDao speakerSensingDao;

	/** The sens environment dao. */
	private SensingEnvironmentDao sensEnvironmentDao;

	/**
	 * Instantiates a new user service impl.
	 */
	public SpeakerSensingServiceImpl() {
		speakerSensingDao = new SpeakerSensingDao();
		sensEnvironmentDao = new SensingEnvironmentDao();
	}

	/**
	 * Gets the speaker sensing between timestamps.
	 *
	 * @param username
	 *            the username
	 * @param fromtimestamp
	 *            the fromtimestamp
	 * @param totimestamp
	 *            the totimestamp
	 * @return the speaker sensing between timestamps
	 */
	public List<SpeakerSensing> getSpeakerSensingBetweenTimestamps(
			String username, long fromtimestamp, long totimestamp) {
		log.debug("getSpeakerSensingBetweenTimestamps");

		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return speakerSensingDao.getSpeakerSensingBetweenTimestamps(
					sensingEnvironment.getUuid(), fromtimestamp, totimestamp);

		else
			return null;

	}

	/**
	 * Delete deprecated speaker sensing.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 */
	public void deleteDeprecatedSpeakerSensing(UUID sensing_environment_id,
			long timestamp) {
		log.debug("deleteDeprecatedSpeakerSensing: " + timestamp);

		speakerSensingDao.deleteDeprecatedSpeakerSensing(
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
