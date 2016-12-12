/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.cloudgateway.services;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import eu.ewall.platform.commons.datamodel.ewallsystem.PointOfContact;
import eu.ewall.platform.commons.datamodel.ewallsystem.ProxyStatus;
import eu.ewall.platform.commons.datamodel.ewallsystem.RegistrationStatus;
import eu.ewall.platform.commons.datamodel.ewallsystem.SensingEnvironment;
import eu.ewall.platform.middleware.datamanager.dao.ewallsystem.SensingEnvironmentDao;

/**
 * The Class SensingEnvironmentsServiceImpl.
 *
 * @author emirmos
 */
@Service("SensingEnvironmentsService")
public class SensingEnvironmentsServiceImpl {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(SensingEnvironmentsServiceImpl.class);

	/** Interval for periodically checking of offline sensing environments. */
	private final long CHECK_OFFLINE_SENS_ENV_INTERVAL = 60 * 60 * 1000; // 1 hour

	/** Expiration time of remote proxy (must be greater than remote proxy eu.ewall.platform.remoteproxy.services.PeriodicAnnouncingService.java.ANNOUNCING_INTERVAL). */
	private final long EXPIRATION_TIME = 60 * 60 * 1000; // 1 hour

	/** The sensing environment dao. */
	private SensingEnvironmentDao sensingEnvironmentDao;
	
	/**
	 * Instantiates a new sensing environments service impl.
	 */
	public SensingEnvironmentsServiceImpl() {
		sensingEnvironmentDao = new SensingEnvironmentDao();
	}

	/**
	 * Gets the sensing environment.
	 *
	 * @param sensingEnvironmentId
	 *            the sensing environment id
	 * @return the sensing environment
	 */
	public SensingEnvironment getSensingEnvironment(UUID sensingEnvironmentId) {
		return sensingEnvironmentDao.getSensingEnvironmentByUUID(sensingEnvironmentId.toString());
	}

	/**
	 * Gets the sensing environments.
	 *
	 * @return the sensing environments
	 */
	public List<SensingEnvironment> getSensingEnvironments() {
		return sensingEnvironmentDao.getAllSensingEnvironments();
	}

	/**
	 * Register sensing environment.
	 *
	 * @param sensingEnvironment
	 *            the sensing environment
	 * @param newPoc
	 *            the new poc
	 * @return true, if successful
	 */
	public boolean registerSensingEnvironment(
			SensingEnvironment sensingEnvironment, PointOfContact newPoc) {
		if (newPoc != null) {
			PointOfContact poc = sensingEnvironment.getPointOfContact();
			poc.setContactURI(newPoc.getContactURI());
			poc.setProxyStatus(newPoc.getProxyStatus());
			// update expiration time to NOW + EXPIRATION_TIME
			long currentTimeInMiliseconds = System.currentTimeMillis();
			poc.setExpirationTime(currentTimeInMiliseconds + EXPIRATION_TIME);
			poc.setLastModifiedTime(currentTimeInMiliseconds);
			poc.setLocalPlatformVersion(newPoc.getLocalPlatformVersion());
			sensingEnvironment.setPointOfContact(poc);
		}

		if (sensingEnvironment.getRegistrationStatus() == RegistrationStatus.REGISTERED) {
			LOG.debug("registerSensingEnvironment: Sensing environment "
					+ sensingEnvironment.getUuid().toString()
					+ " already registered at eWALL system.");
		} else {

			sensingEnvironment
					.setRegistrationStatus(RegistrationStatus.REGISTERED);

			LOG.info("registerSensingEnvironment: Sensing environment "
					+ sensingEnvironment.getUuid().toString()
					+ " registered at eWALL system.");
		}

		return sensingEnvironmentDao
				.updateSensingEnvironment(sensingEnvironment);
	}

	/**
	 * De register sensing environment.
	 *
	 * @param sensingEnvironmentId
	 *            the sensing environment id
	 * @param poc
	 *            the poc
	 * @return true, if successful
	 */
	public boolean deRegisterSensingEnvironment(UUID sensingEnvironmentId, PointOfContact poc) {

		SensingEnvironment sensingEnvironment = sensingEnvironmentDao.getSensingEnvironmentByUUID(sensingEnvironmentId.toString());

		if (sensingEnvironment == null) {
			LOG.warn("deRegisterSensingEnvironment: Sensing environment "
					+ sensingEnvironmentId + " does not exist at eWALL system.");
			return false;
		}

		if (poc != null)
			sensingEnvironment.setPointOfContact(poc);

		if (sensingEnvironment.getRegistrationStatus() == RegistrationStatus.NOT_REGISTERED) {
			LOG.warn("registerSensingEnvironment: Sensing environment "
					+ sensingEnvironmentId
					+ " already deregistered at eWALL system.");
		} else {

			sensingEnvironment
					.setRegistrationStatus(RegistrationStatus.NOT_REGISTERED);

			LOG.info("registerSensingEnvironment: Sensing environment "
					+ sensingEnvironmentId + " deregistered at eWALL System.");
		}

		return sensingEnvironmentDao.updateSensingEnvironment(sensingEnvironment);
	}

	/**
	 * Adds or updates the sensing environment.
	 *
	 * @param sensingEnvironment
	 *            the sensing environment
	 * @return true, if successful
	 */
	public boolean addSensingEnvironment(SensingEnvironment sensingEnvironment) {
		return sensingEnvironmentDao.addSensingEnvironment(sensingEnvironment);
	}

	/**
	 * Update sensing environment.
	 *
	 * @param sensingEnvironment
	 *            the sensing environment
	 * @return true, if successful
	 */
	public boolean updateSensingEnvironment(
			SensingEnvironment sensingEnvironment) {
		return sensingEnvironmentDao.updateSensingEnvironment(sensingEnvironment);
	}

	/**
	 * Delete sensing environment.
	 *
	 * @param sensing_environment_id the sensing_environment_id
	 * @return true, if successful
	 */
	public boolean deleteSensingEnvironment(String sensing_environment_id) {
		return sensingEnvironmentDao.deleteSensingEnvironment(sensing_environment_id);
	}


	/**
	 * Save point of contact.
	 *
	 * @param sensingEnvironmentId
	 *            the sensing environment id
	 * @param poc
	 *            the point of contact
	 * @return true, if successful
	 */
	public boolean savePoC(UUID sensingEnvironmentId,
			PointOfContact poc) {
		SensingEnvironment sensingEnvironment = sensingEnvironmentDao.getSensingEnvironmentByUUID(sensingEnvironmentId.toString());

		if (sensingEnvironment == null) {
			LOG.warn("deRegisterSensingEnvironment: Sensing environment "
					+ sensingEnvironmentId + " does not exist at eWALL system.");
			return false;
		}

		if (poc != null)
			sensingEnvironment.setPointOfContact(poc);

		return sensingEnvironmentDao.updateSensingEnvironment(sensingEnvironment);
	}

	/**
	 * Delete point of contact.
	 *
	 * @param sensingEnvironmentId
	 *            the sensing environment id
	 * @return true, if successful
	 */
	public boolean deletePoC(UUID sensingEnvironmentId) {
		
		SensingEnvironment sensingEnvironment = sensingEnvironmentDao.getSensingEnvironmentByUUID(sensingEnvironmentId.toString());

		if (sensingEnvironment == null) {
			LOG.warn("deRegisterSensingEnvironment: Sensing environment "
					+ sensingEnvironmentId + " does not exist.");
			return false;
		}

		sensingEnvironment.setPointOfContact(null);

		//update sensing environment data
		return sensingEnvironmentDao.updateSensingEnvironment(sensingEnvironment);
	}

	/**
	 * Check for offline sensing environment.
	 */
	@Scheduled(fixedDelay = CHECK_OFFLINE_SENS_ENV_INTERVAL)
	public void checkSenEnvExpirations() {
		LOG.debug("checkSenEnvExpirations");

		List<SensingEnvironment> sensEnvironments = getSensingEnvironments();

		for (SensingEnvironment sensingEnvironment : sensEnvironments) {
			PointOfContact poc = sensingEnvironment.getPointOfContact();
			if (poc != null) {
				
				long currentTimeInMiliseconds = System.currentTimeMillis();
				// if expiration time expired, set ProxyStatus to offline
				if (poc.getExpirationTime() < currentTimeInMiliseconds) {
					poc.setProxyStatus(ProxyStatus.OFFLINE);
				}

				updateSensingEnvironment(sensingEnvironment);
			}
		}
	}

}
