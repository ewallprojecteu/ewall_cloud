/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.datamanager.dao.location;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.WriteResult;

import eu.ewall.platform.commons.datamodel.location.GPScoordinates;
import eu.ewall.platform.middleware.datamanager.dao.config.dbtype.EWallDBType;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;

/**
 * The Class GPSCoordinateDao.
 */
public class GPSCoordinateDao {

	/** The log. */
	private static final Logger LOG = LoggerFactory
			.getLogger(GPSCoordinateDao.class);

	/** The Constant GPSCOORDINATES_COLLECTION. */
	public static final String GPSCOORDINATES_COLLECTION = "gpsCoordinates";

	/** The mongo ops. */
	private MongoOperations mongoOps;

	/**
	 * Instantiates a new GPS coordinate dao.
	 */
	public GPSCoordinateDao() {
		mongoOps = MongoDBFactory
				.getMongoOperationsForDBType(EWallDBType.DATA);
	}

	/**
	 * Gets the all gps coordinates.
	 *
	 * @return the all gps coordinates
	 */
	public List<GPScoordinates> getAllGPScoordinates() {
		LOG.debug("Retrieveing all gps coordinates from mongodb.");
		return this.mongoOps.findAll(GPScoordinates.class,
				GPSCOORDINATES_COLLECTION);

	}

	/**
	 * Gets the GPS coordinates by timestamp.
	 *
	 * @param timestamp
	 *            the timestamp
	 * @return the GPS coordinates by timestamp
	 */
	public GPScoordinates getGPScoordinatesByTimestamp(long timestamp) {
		LOG.debug("Retrieving GPScoordinates with timestamp " + timestamp
				+ " from database.");

		Query query = new Query(Criteria.where("timestamp").is(timestamp));
		return this.mongoOps.findOne(query, GPScoordinates.class,
				GPSCOORDINATES_COLLECTION);

	}

	/**
	 * Gets the GP scoordinates by name.
	 *
	 * @param name
	 *            the name
	 * @return the GP scoordinates by name
	 */
	public GPScoordinates getGPScoordinatesByName(String name) {
		LOG.debug("Retrieving GPScoordinates with name " + name
				+ " from database.");

		Query query = new Query(Criteria.where("name").is(name));
		return this.mongoOps.findOne(query, GPScoordinates.class,
				GPSCOORDINATES_COLLECTION);

	}

	/**
	 * Adds the gps coordinates.
	 *
	 * @param gpsCoordinates
	 *            the gps coordinates
	 * @return true, if successful
	 */
	public boolean addGPScoordinates(GPScoordinates gpsCoordinates) {

		mongoOps.insert(gpsCoordinates, GPSCOORDINATES_COLLECTION);

		LOG.debug("New gps data: " + gpsCoordinates.getName()
				+ " added to mongodb.");

		return true;

	}

	/**
	 * Delete deprecated gps coordinates.
	 *
	 * @param ewall_system_id
	 *            the ewall_system_id
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 */
	public void deleteDeprecatedGpsCoordinates(UUID ewall_system_id,
			UUID sensing_environment_id, long timestamp) {
		Query query = new Query();
		query.addCriteria(Criteria.where("timestamp").exists(true)
				.andOperator(Criteria.where("timestamp").lt(timestamp)));

		WriteResult result = this.mongoOps.remove(query, GPScoordinates.class,
				GPSCOORDINATES_COLLECTION);

		LOG.info(String.format("Deleted %d gps coordinates from database",
				result.getN()));
	}
}
