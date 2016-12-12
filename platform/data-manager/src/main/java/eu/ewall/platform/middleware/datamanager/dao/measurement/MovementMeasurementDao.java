/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.datamanager.dao.measurement;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.WriteResult;

import eu.ewall.platform.commons.datamodel.measure.MovementMeasurement;
import eu.ewall.platform.middleware.datamanager.dao.config.dbtype.EWallDBType;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;

/**
 * The Class MovementMeasurementDao.
 */
public class MovementMeasurementDao {

	/** The log. */
	private static final Logger LOG = LoggerFactory
			.getLogger(MovementMeasurementDao.class);

	/** The Constant Movement_MEASUREMENTS_COLLECTION. */
	public static final String MOVEMENT_MEASUREMENTS_COLLECTION = "movementMeasurement";

	/** The mongo ops. */
	private MongoOperations mongoOps;

	/**
	 * Instantiates a new movement measurement dao.
	 */
	public MovementMeasurementDao() {
		mongoOps = MongoDBFactory.getMongoOperationsForDBType(EWallDBType.DATA);
	}

	/**
	 * Gets the movement measurements by timestamp.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 * @return the movement measurements by timestamp
	 */
	public MovementMeasurement getMovementMeasurementsByTimestamp(
			UUID sensing_environment_id, long timestamp) {
		LOG.debug("Retrieving MovementMeasurement with timestamp " + timestamp
				+ " from database.");

		Query query = new Query(Criteria.where("timestamp").is(timestamp));

		if (sensing_environment_id != null) {
			return this.mongoOps.findOne(query, MovementMeasurement.class,
					sensing_environment_id + "_"
							+ MOVEMENT_MEASUREMENTS_COLLECTION);
		}
		return null;

	}

	/**
	 * Gets the movement measurements between timestamps.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param fromtimestamp
	 *            the fromtimestamp
	 * @param totimestamp
	 *            the totimestamp
	 * @param room_name
	 *            the room_name
	 * @return the movement measurements between timestamps
	 */
	public List<MovementMeasurement> getMovementMeasurementsBetweenTimestamps(
			UUID sensing_environment_id, long fromtimestamp, long totimestamp,
			String room_name) {
		LOG.debug("Retrieving getMovementMeasurementsBetweenTimestamps from database");

		Query query = new Query();

		if (room_name == null)
			query.addCriteria(Criteria
					.where("timestamp")
					.exists(true)
					.andOperator(Criteria.where("timestamp").gt(fromtimestamp),
							Criteria.where("timestamp").lt(totimestamp)));
		else
			query.addCriteria(Criteria
					.where("indoorPlaceName")
					.is(room_name)
					.andOperator(Criteria.where("timestamp").gt(fromtimestamp),
							Criteria.where("timestamp").lt(totimestamp)));

		if (sensing_environment_id != null) {
			return this.mongoOps.find(query, MovementMeasurement.class,
					sensing_environment_id + "_"
							+ MOVEMENT_MEASUREMENTS_COLLECTION);
		}
		return null;
	}

	/**
	 * Gets the all movement measurements.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @return the all movement measurements
	 */
	public List<MovementMeasurement> getAllMovementMeasurements(
			UUID sensing_environment_id) {
		LOG.debug("Retrieving getAllAccelerometerMeasurements from database");

		if (sensing_environment_id != null) {
			return this.mongoOps.findAll(MovementMeasurement.class,
					sensing_environment_id + "_"
							+ MOVEMENT_MEASUREMENTS_COLLECTION);
		}

		return null;
	}

	/**
	 * Delete deprecated movement measurements.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 */
	public void deleteDeprecatedMovementMeasurements(
			UUID sensing_environment_id, long timestamp) {
		Query query = new Query();
		query.addCriteria(Criteria.where("timestamp").exists(true)
				.andOperator(Criteria.where("timestamp").lt(timestamp)));

		WriteResult result = this.mongoOps.remove(query,
				MovementMeasurement.class, sensing_environment_id + "_"
						+ MOVEMENT_MEASUREMENTS_COLLECTION);

		LOG.info(String.format(
				"Deleted %d movement measurements from database", result.getN()));
	}
}
