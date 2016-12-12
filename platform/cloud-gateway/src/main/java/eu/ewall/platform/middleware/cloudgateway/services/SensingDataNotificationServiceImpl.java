/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.cloudgateway.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import eu.ewall.platform.commons.datamodel.ewallsystem.SensingEnvironment;
import eu.ewall.platform.commons.datamodel.measure.AccelerometerMeasurement;
import eu.ewall.platform.middleware.cloudgateway.dao.SensingDataNotificationDAO;
import eu.ewall.platform.middleware.datamanager.dao.ewallsystem.SensingEnvironmentDao;

/**
 * The Class SensingDataNotificationServiceImpl.
 * 
 * @author emirmos
 */
@Service("notificationService")
@EnableAsync
public class SensingDataNotificationServiceImpl {

	/** The log. */
	Logger log = LoggerFactory
			.getLogger(SensingDataNotificationServiceImpl.class);

	/** The sensing data notification dao. */
	@Autowired
	SensingDataNotificationDAO sensingDataNotificationDAO;

	/** The sensing environment dao. */
	private SensingEnvironmentDao sensingEnvironmentDao;

	/**
	 * Instantiates a new sensing data notification service impl.
	 */
	public SensingDataNotificationServiceImpl() {
		sensingEnvironmentDao = new SensingEnvironmentDao();
	}

	/**
	 * Process sensing data.
	 *
	 * @param sensingEnvironmentId
	 *            the sensing environment id
	 * @param sensingData
	 *            the sensing data
	 * @return true, if successful
	 */
	public boolean processSensingData(String sensingEnvironmentId,
			AccelerometerMeasurement sensingData) {

		String source = AccelerometerMeasurement.class.getName();

		if (sensingData.isFallDetected()) {
			SensingEnvironment env = sensingEnvironmentDao
					.getSensingEnvironmentByUUID(sensingEnvironmentId);
			if (env != null) {
				sensingDataNotificationDAO.sendNotification(
						env.getPrimaryUser(), source);
				log.info("Notification sent.");
				return true;
			}

		}

		return false;
	}
}
