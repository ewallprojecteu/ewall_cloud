/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.cloudgateway.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import eu.ewall.platform.commons.datamodel.sensing.VitalsSensing;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;
import eu.ewall.platform.middleware.datamanager.dao.sensing.VitalSensingDao;

/**
 * The Class HealthSensingServiceImpl.
 * 
 * @author emirmos
 */
@Service("vitalsSensingService")
public class VitalsSensingServiceImpl implements DisposableBean {

	/** The log. */
	Logger log = LoggerFactory.getLogger(VitalsSensingServiceImpl.class);

	/** The vital sensing dao. */
	private VitalSensingDao vitalSensingDao;

	/**
	 * Instantiates a new health sensing service impl.
	 */
	public VitalsSensingServiceImpl() {
		vitalSensingDao = new VitalSensingDao();
	}

	/**
	 * Adds the health sensing.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param device_id 
	 * @param vitalsSensing
	 *            the vitals sensing
	 */
	public void addHealthSensing(String sensing_environment_id,
			String device_id, VitalsSensing vitalsSensing) {
		log.debug("addVisualSensing");

		vitalSensingDao.create(sensing_environment_id, device_id, vitalsSensing);
	}

	/**
	 * Gets the last collections timestamp.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param device_id 
	 * @return the last collections timestamp
	 */
	public long getLastCollectionsTimestamp(String sensing_environment_id, String device_id) {
		log.debug("getLastCollectionsTimestamp");

		return vitalSensingDao.getLastCollectionsTimestamp(sensing_environment_id, device_id);
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
