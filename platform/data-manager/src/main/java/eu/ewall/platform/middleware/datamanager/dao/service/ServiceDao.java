/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.datamanager.dao.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import eu.ewall.platform.commons.datamodel.service.Application;
import eu.ewall.platform.commons.datamodel.service.Service;
import eu.ewall.platform.middleware.datamanager.dao.config.dbtype.EWallDBType;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;

/**
 * The Class ServiceDao.
 */
public class ServiceDao {

	/** The log. */
	private static final Logger LOG = LoggerFactory.getLogger(ServiceDao.class);

	/** The Constant SERVICES_COLLECTION. */
	public static final String SERVICES_COLLECTION = "service";

	/** The mongo ops. */
	private MongoOperations mongoOps;

	/**
	 * Instantiates a new service dao.
	 */
	public ServiceDao() {
		mongoOps = MongoDBFactory
				.getMongoOperationsForDBType(EWallDBType.SYSTEM);
	}

	/**
	 * Gets the all e wall services.
	 *
	 * @return the all e wall services
	 */
	public List<Service> getAllEWallServices() {
		LOG.debug("Retrieveing all services from mongodb.");
		return this.mongoOps.findAll(Service.class, SERVICES_COLLECTION);

	}

	/**
	 * Gets the e wall service by name.
	 *
	 * @param serviceName
	 *            the servicename
	 * @return the e wall service by name
	 */
	public Service getEWallServiceByName(String serviceName) {

		LOG.debug("Retrieveing service with service name " + serviceName
				+ " from mongodb.");

		Query query = new Query(Criteria.where("name").is(serviceName));
		return this.mongoOps.findOne(query, Service.class, SERVICES_COLLECTION);
	}

	/**
	 * Adds the e wall service.
	 *
	 * @param service
	 *            the service
	 * @return true, if successful
	 */
	public boolean addEWallService(Service service) {

		// if service already exists report it cannot be added
		Query queryUsername = new Query(Criteria.where("name").is(
				service.getName()));
		if ((mongoOps.count(queryUsername, SERVICES_COLLECTION) > 0)) {
			return false;
		}

		mongoOps.insert(service, SERVICES_COLLECTION);

		LOG.debug("New service with service name " + service.getName()
				+ " added to mongodb.");

		return true;

	}

	/**
	 * Check if some application contained deleted service and if yes remove
	 * connection.
	 *
	 * @param serviceName
	 *            the service name
	 * @return true, if successful
	 */
	public boolean checkIfSomeApplicationContainedDeletedServiceAndIfYesRemoveConnection(
			String serviceName) {
		boolean returnStatus = false;

		// get the list of all applications
		ApplicationDao applicationMongo = new ApplicationDao();
		List<Application> allApps = applicationMongo.getAllEWallApplications();
		// go through all applications
		for (Application each : allApps) {

			// if application contains the connection to the service delete the
			// connection
			if (each.getEncompassingServiceNames().contains(serviceName)) {

				// save change in application
				returnStatus = each
						.removeServiceNameFromEncompassingServiceNamesList(serviceName);
				this.mongoOps
						.save(each, ApplicationDao.APPLICATIONS_COLLECTION);

				LOG.debug("Deleting service name also from the application: "
						+ each.getName() + " from mongodb.");
			}
		}
		return returnStatus;

	}

	/**
	 * Delete service with name.
	 *
	 * @param serviceName
	 *            the service name
	 * @return true, if successful
	 */
	public boolean deleteServiceWithName(String serviceName) {

		LOG.debug("Deleting service with service name " + serviceName
				+ " from mongodb.");

		Query query = new Query(Criteria.where("name").is(serviceName));
		this.mongoOps.remove(query, Service.class, SERVICES_COLLECTION);

		checkIfSomeApplicationContainedDeletedServiceAndIfYesRemoveConnection(serviceName);

		return true;

	}

	/**
	 * Modify e wall service.
	 *
	 * @param name
	 *            the service name
	 * @param service
	 *            the service
	 * @return the write result
	 */
	public boolean modifyEWallServiceWithName(String name, Service service) {

		// delete existing
		Query query = new Query(Criteria.where("name").is(name));
		this.mongoOps.remove(query, Service.class, SERVICES_COLLECTION);

		// insert new one
		this.mongoOps.insert(service, SERVICES_COLLECTION);
		LOG.debug("Service with service name " + name + " modified in mongodb.");

		return true;

	}

	/**
	 * Collection exits.
	 *
	 * @return true, if successful
	 */
	public boolean collectionExits() {
		return mongoOps.collectionExists(SERVICES_COLLECTION);
	}
}
