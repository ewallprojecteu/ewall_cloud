/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.cloudgateway.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import eu.ewall.platform.commons.datamodel.sensing.SpeakerSensing;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;
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

	/**
	 * Instantiates a new speaker sensing service impl.
	 */
	public SpeakerSensingServiceImpl() {
		speakerSensingDao = new SpeakerSensingDao();
	}

	/**
	 * Adds the speaker sensing.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param speakerSensing
	 *            the speaker sensing
	 */
	public void addSpeakerSensing(String sensing_environment_id,
			SpeakerSensing speakerSensing) {
		log.debug("addVisualSensing");

		speakerSensingDao.create(sensing_environment_id, speakerSensing);
	}

	/**
	 * Gets the last collections timestamp.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @return the last collections timestamp
	 */
	public long getLastCollectionsTimestamp(String sensing_environment_id) {
		log.debug("getLastCollectionsTimestamp");

		return speakerSensingDao.getLastCollectionsTimestamp(sensing_environment_id);
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
