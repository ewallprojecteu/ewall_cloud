/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.cloudgateway.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import eu.ewall.platform.commons.datamodel.sensing.VisualSensing;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;
import eu.ewall.platform.middleware.datamanager.dao.sensing.VisualSensingDao;

/**
 * The Class VisualSensingServiceImpl.
 */
@Service("visualSensingService")
public class VisualSensingServiceImpl implements DisposableBean {

	/** The log. */
	Logger log = LoggerFactory.getLogger(VisualSensingServiceImpl.class);

	/** The visual sensing dao. */
	private VisualSensingDao visualSensingDao;

	/**
	 * Instantiates a new visual sensing service impl.
	 */
	public VisualSensingServiceImpl() {
		visualSensingDao = new VisualSensingDao();
	}

	/**
	 * Adds the visual sensing.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param visualSensing
	 *            the visual sensing
	 */
	public void addVisualSensing(String sensing_environment_id,
			VisualSensing visualSensing) {
		log.debug("addVisualSensing");

		visualSensingDao.create(sensing_environment_id, visualSensing);
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

		return visualSensingDao.getLastCollectionsTimestamp(sensing_environment_id);
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
