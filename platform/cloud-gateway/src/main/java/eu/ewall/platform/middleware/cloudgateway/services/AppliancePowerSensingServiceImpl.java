/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.cloudgateway.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import eu.ewall.platform.commons.datamodel.sensing.AppliancePowerSensing;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;
import eu.ewall.platform.middleware.datamanager.dao.sensing.AppliancePowerSensingDao;

/**
 * The Class AppliancePowerSensingServiceImpl.
 */
@Service("appliancePowerSensingService")
public class AppliancePowerSensingServiceImpl implements DisposableBean {

	/** The log. */
	Logger log = LoggerFactory
			.getLogger(AppliancePowerSensingServiceImpl.class);

	/** The appliance power sensing dao. */
	private AppliancePowerSensingDao appliancePowerSensingDao;

	/**
	 * Instantiates a new appliance power sensing service impl.
	 */
	public AppliancePowerSensingServiceImpl() {
		appliancePowerSensingDao = new AppliancePowerSensingDao();
	}

	/**
	 * Adds the appliance power sensing.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param appliancePowerSensing
	 *            the appliance power sensing
	 */
	public void addAppliancePowerSensing(String sensing_environment_id,
			AppliancePowerSensing appliancePowerSensing) {
		log.debug("addAppliancePowerSensing");

		appliancePowerSensingDao.create(sensing_environment_id,
				appliancePowerSensing);
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

		return appliancePowerSensingDao
				.getLastCollectionsTimestamp(sensing_environment_id);
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
