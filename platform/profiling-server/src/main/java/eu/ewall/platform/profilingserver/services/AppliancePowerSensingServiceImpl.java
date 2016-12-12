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
import eu.ewall.platform.commons.datamodel.sensing.AppliancePowerSensing;
import eu.ewall.platform.commons.datamodel.sensing.ApplianceType;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;
import eu.ewall.platform.middleware.datamanager.dao.ewallsystem.SensingEnvironmentDao;
import eu.ewall.platform.middleware.datamanager.dao.sensing.AppliancePowerSensingDao;

/**
 * The Class AppliancePowerSensingServiceImpl.
 *
 * @author emirmos
 */
@Service("appliancePowerSensingService")
public class AppliancePowerSensingServiceImpl implements DisposableBean {

	/** The log. */
	Logger log = LoggerFactory
			.getLogger(AppliancePowerSensingServiceImpl.class);

	/** The appliance power sensing dao. */
	private AppliancePowerSensingDao appliancePowerSensingDao;

	/** The sens environment dao. */
	private SensingEnvironmentDao sensEnvironmentDao;

	/**
	 * Instantiates a new appliance power sensing service impl.
	 */
	public AppliancePowerSensingServiceImpl() {
		appliancePowerSensingDao = new AppliancePowerSensingDao();
		sensEnvironmentDao = new SensingEnvironmentDao();
	}

	/**
	 * Gets the appliance power sensing between timestamps.
	 *
	 * @param username            the username
	 * @param fromtimestamp            the fromtimestamp
	 * @param totimestamp            the totimestamp
	 * @param applianceName            the appliance name
	 * @param applianceType the appliance type
	 * @param roomName            the room name
	 * @return the appliance power sensing between timestamps
	 */
	public List<AppliancePowerSensing> getAppliancePowerSensingBetweenTimestamps(
			String username, long fromtimestamp, long totimestamp,
			String applianceName, ApplianceType applianceType, String roomName) {
		log.debug("getAppliancePowerSensingBetweenTimestamps");

		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return appliancePowerSensingDao
					.getAppliancePowerSensingBetweenTimestamps(
							sensingEnvironment.getUuid(), fromtimestamp,
							totimestamp, applianceName, applianceType, roomName);

		else
			return null;

	}

	/**
	 * Delete deprecated appliance power sensing.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 */
	public void deleteDeprecatedAppliancePowerSensing(
			UUID sensing_environment_id, long timestamp) {
		log.debug("deleteDeprecatedAppliancePowerSensing: " + timestamp);

		appliancePowerSensingDao.deleteDeprecatedAppliancePowerSensing(
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
