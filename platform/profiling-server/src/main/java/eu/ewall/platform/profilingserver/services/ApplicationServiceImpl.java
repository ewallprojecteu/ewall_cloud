/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.profilingserver.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import eu.ewall.platform.commons.datamodel.service.Application;
import eu.ewall.platform.middleware.datamanager.dao.service.ApplicationDao;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;

/**
 * The Class ApplicationServiceImpl.
 * 
 * @author eandgrg, emirmos
 */
@Service("applicationService")
public class ApplicationServiceImpl implements DisposableBean {

	/** The log. */
	Logger log = LoggerFactory.getLogger(ApplicationServiceImpl.class);

	/** The application dao. */
	private ApplicationDao applicationDao;

	/**
	 * Instantiates a new application service impl.
	 */
	public ApplicationServiceImpl() {
		applicationDao = new ApplicationDao();
	}

	/**
	 * Adds the e wall application.
	 *
	 * @param application
	 *            the application
	 * @return true, if successful
	 */
	public boolean addEWallApplication(Application application) {
		log.debug("addEWallApplication from");
		return applicationDao.addEWallApplication(application);

	}

	/**
	 * Gets the all e wall applications.
	 *
	 * @return the all e wall applications
	 */
	public List<Application> getAllEWallApplications() {

		log.debug("getAllEWallApplications");
		return applicationDao.getAllEWallApplications();
	}

	/**
	 * Gets the e wall application by applicationname.
	 *
	 * @param applicationname
	 *            the applicationname
	 * @return the e wall application by applicationname
	 */
	public Application getEWallApplicationByApplicationName(
			String applicationname) {
		return applicationDao.getEWallApplicationByName(applicationname);
	}

	/**
	 * Modify e wall application with name.
	 *
	 * @param applicationName
	 *            the application name
	 * @param application
	 *            the application
	 * @return true, if successful
	 */
	public boolean modifyEWallApplicationWithName(String applicationName,
			Application application) {
		return applicationDao.modifyEWallApplicationWithName(applicationName,
				application);
	}

	/**
	 * Delete application with name.
	 *
	 * @param applicationname
	 *            the applicationname
	 * @return true, if successful
	 */
	public boolean deleteApplicationWithName(String applicationname) {
		return applicationDao.deleteApplicationWithName(applicationname);
	}

	/**
	 * Gets the application dao.
	 *
	 * @return the application dao
	 */
	public ApplicationDao getApplicationDao() {
		return applicationDao;
	}

	/**
	 * Sets the application dao.
	 *
	 * @param applicationDao
	 *            the new application dao
	 */
	public void setApplicationDao(ApplicationDao applicationDao) {
		this.applicationDao = applicationDao;
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

	/**
	 * Inits the deafult data.
	 *
	 * @param apps
	 *            the apps
	 */
	public void initDeafultData(List<Application> apps) {
		if (applicationDao.collectionExits()) {
			log.info("Application collection exists. No init data added.");
		} else {
			log.info("Application collection does not exist. Addding default applications data from config file.");

			for (Application app : apps) {
				this.addEWallApplication(app);
			}
		}

	}
}
