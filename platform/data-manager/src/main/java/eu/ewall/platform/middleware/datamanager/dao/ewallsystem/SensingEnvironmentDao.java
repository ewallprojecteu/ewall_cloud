/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.datamanager.dao.ewallsystem;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import eu.ewall.platform.commons.datamodel.ewallsystem.SensingEnvironment;
import eu.ewall.platform.middleware.datamanager.dao.config.dbtype.EWallDBType;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;

/**
 * The Class SensingEnvironmentDao.
 */
public class SensingEnvironmentDao {

	/** The log. */
	private static final Logger LOG = LoggerFactory
			.getLogger(SensingEnvironmentDao.class);

	/** The Constant SENSING_ENVIRONMENS_COLLECTION. */
	public static final String SENSING_ENVIRONMENS_COLLECTION = "sensingEnvironment";

	/** The mongo ops. */
	private MongoOperations mongoOps;

	/**
	 * Instantiates a new sensing environment dao.
	 */
	public SensingEnvironmentDao() {
		mongoOps = MongoDBFactory
				.getMongoOperationsForDBType(EWallDBType.SYSTEM);
	}

	/**
	 * Gets the all sensing environments.
	 *
	 * @return the all sensing environments
	 */
	public List<SensingEnvironment> getAllSensingEnvironments() {
		LOG.debug("Retrieveing all sensing environments from mongodb.");
		return this.mongoOps.findAll(SensingEnvironment.class,
				SENSING_ENVIRONMENS_COLLECTION);
	}

	/**
	 * Gets the sensing environment by uuid.
	 *
	 * @param uuid
	 *            the uuid
	 * @return the sensing environment by uuid
	 */
	public SensingEnvironment getSensingEnvironmentByUUID(String uuid) {
		LOG.debug("Retrieveing sensing environment with uuid " + uuid
				+ " from mongodb.");
		Query query = new Query(Criteria.where("_id").is(uuid));

		return this.mongoOps.findOne(query, SensingEnvironment.class,
				SENSING_ENVIRONMENS_COLLECTION);
	}

	/**
	 * Gets the sensing environment by primary user.
	 *
	 * @param username
	 *            the username
	 * @return the sensing environment by primary user
	 */
	public SensingEnvironment getSensingEnvironmentByPrimaryUser(String username) {
		LOG.debug("Retrieveing sensing environment with primary user "
				+ username + " from mongodb.");
		Query query = new Query(Criteria.where("primaryUser").is(username));

		return this.mongoOps.findOne(query, SensingEnvironment.class,
				SENSING_ENVIRONMENS_COLLECTION);
	}

	/**
	 * Adds the sensing environment.
	 *
	 * @param sensingEnvironment
	 *            the sensing environment
	 * @return true, if successful
	 */
	public boolean addSensingEnvironment(SensingEnvironment sensingEnvironment) {
		mongoOps.insert(sensingEnvironment, SENSING_ENVIRONMENS_COLLECTION);

		LOG.debug("New sensing environment with name "
				+ sensingEnvironment.getName() + " added to mongodb.");
		return true;
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
		if (sensingEnvironment == null) {
			LOG.info("Updating with null ewall sensing environment object.");
			return false;
		}

		Query query = new Query(Criteria.where("_id").is(
				sensingEnvironment.getUuid().toString()));

		SensingEnvironment existingSensEnv = mongoOps.findOne(query,
				SensingEnvironment.class, SENSING_ENVIRONMENS_COLLECTION);

		if (existingSensEnv == null) {
			LOG.info("Sensing environment with uuid "
					+ sensingEnvironment.getUuid()
					+ " not found in DB. Update terminated.");
			return false;
		}

		existingSensEnv.setIndoorPlaces(sensingEnvironment.getIndoorPlaces());
		existingSensEnv.setName(sensingEnvironment.getName());
		existingSensEnv.setPointOfContact(sensingEnvironment
				.getPointOfContact());
		existingSensEnv.setPrimaryUser(sensingEnvironment.getPrimaryUser());
		existingSensEnv.setRegionName(sensingEnvironment.getRegionName());
		existingSensEnv.setRegistrationStatus(sensingEnvironment
				.getRegistrationStatus());
		existingSensEnv.setEnabled(sensingEnvironment.isEnabled());

		mongoOps.save(existingSensEnv, SENSING_ENVIRONMENS_COLLECTION);

		LOG.debug("EWall sensing environment with uuid "
				+ existingSensEnv.getUuid() + " updated");

		return true;
	}

	/**
	 * Delete sensing environment.
	 *
	 * @param uuid
	 *            the uuid
	 * @return true, if successful
	 */
	public boolean deleteSensingEnvironment(String uuid) {
		LOG.debug("Deleting sensing environment with uuid  " + uuid
				+ " from mongodb.");

		Query query = new Query(Criteria.where("_id").is(uuid));
		this.mongoOps.remove(query, SensingEnvironment.class,
				SENSING_ENVIRONMENS_COLLECTION);

		return true;
	}
}
