/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.profilingserver.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

import eu.ewall.platform.commons.datamodel.service.Service;
import eu.ewall.platform.middleware.datamanager.dao.service.ServiceDao;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;

/**
 * The Class ServiceServiceImpl.
 * 
 * @author eandgrg, emirmos
 */
@org.springframework.stereotype.Service("serviceService")
public class ServiceServiceImpl implements DisposableBean {

	/** The log. */
	Logger log = LoggerFactory.getLogger(ServiceServiceImpl.class);

	/** The service dao. */
	private ServiceDao serviceDao;

	/**
	 * Instantiates a new service service impl.
	 */
	public ServiceServiceImpl() {
		serviceDao = new ServiceDao();
	}

	/**
	 * Adds the e wall service.
	 *
	 * @param service
	 *            the service
	 * @return true, if successful
	 */
	public boolean addEWallService(Service service) {
		log.debug("addEWallService from");
		return serviceDao.addEWallService(service);

	}

	/**
	 * Gets the all e wall services.
	 *
	 * @return the all e wall services
	 */
	public List<Service> getAllEWallServices() {

		log.debug("getAllEWallServices");
		return serviceDao.getAllEWallServices();
	}

	/**
	 * Gets the e wall service by servicename.
	 *
	 * @param servicename
	 *            the servicename
	 * @return the e wall service by servicename
	 */
	public Service getEWallServiceByServiceName(String servicename) {
		return serviceDao.getEWallServiceByName(servicename);
	}

	/**
	 * Modify e wall service with name.
	 *
	 * @param serviceName
	 *            the service name
	 * @param service
	 *            the service
	 * @return true, if successful
	 */
	public boolean modifyEWallServiceWithName(String serviceName,
			Service service) {
		return serviceDao.modifyEWallServiceWithName(serviceName, service);
	}

	/**
	 * Delete service with name.
	 *
	 * @param servicename
	 *            the servicename
	 * @return true, if successful
	 */
	public boolean deleteServiceWithName(String servicename) {
		return serviceDao.deleteServiceWithName(servicename);
	}

	/**
	 * Gets the service dao.
	 *
	 * @return the service dao
	 */
	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	/**
	 * Sets the service dao.
	 *
	 * @param serviceDao the new service dao
	 */
	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
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
	 * @param services
	 *            the services
	 */
	public void initDeafultData(List<Service> services) {
		if (serviceDao.collectionExits()) {
			log.info("Service collection exists. No init data added.");
		} else {
			log.info("Service collection does not exist. Addding default services data from config file.");

			for (Service service : services) {
				this.addEWallService(service);
			}
		}

	}
}
