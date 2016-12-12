/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.datamanager.dao.measurement;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.WriteResult;

import eu.ewall.platform.commons.datamodel.measure.AccelerometerMeasurement;
import eu.ewall.platform.middleware.datamanager.dao.config.dbtype.EWallDBType;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;

/**
 * The Class AccelerometerMeasurementDao.
 */
public class AccelerometerMeasurementDao {

	/** The log. */
	private static final Logger LOG = LoggerFactory
			.getLogger(AccelerometerMeasurementDao.class);

	/** The Constant ACCELEROMETER_MEASUREMENTS_COLLECTION. */
	public static final String ACCELEROMETER_MEASUREMENTS_COLLECTION = "accelerometerMeasurement";

	/** The mongo ops. */
	private MongoOperations mongoOps;

	/**
	 * Instantiates a new accelerometer measurement dao.
	 */
	public AccelerometerMeasurementDao() {
		mongoOps = MongoDBFactory.getMongoOperationsForDBType(EWallDBType.DATA);
	}
	
	/**
	 * Creates the.
	 *
	 * @param sensing_environment_id the sensing_environment_id
	 * @param accelerometerMeasurement the accelerometer measurement
	 */
	public void create(String sensing_environment_id,
			AccelerometerMeasurement accelerometerMeasurement) {
		this.mongoOps.insert(accelerometerMeasurement, sensing_environment_id + "_"
				+ ACCELEROMETER_MEASUREMENTS_COLLECTION);

	}

	/**
	 * Gets the accelerometer measurements by timestamp.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 * @return the accelerometer measurements by timestamp
	 */
	public AccelerometerMeasurement getAccelerometerMeasurementsByTimestamp(
			UUID sensing_environment_id, long timestamp) {
		LOG.debug("Retrieving AccelerometerMeasurements with timestamp "
				+ timestamp + " from database.");

		Query query = new Query(Criteria.where("timestamp").is(timestamp));

		if (sensing_environment_id != null) {
			return this.mongoOps.findOne(query, AccelerometerMeasurement.class,
					sensing_environment_id + "_"
							+ ACCELEROMETER_MEASUREMENTS_COLLECTION);
		}
		return null;

	}

	/**
	 * Gets the accelerometer measurements between timestamps.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param fromtimestamp
	 *            the fromtimestamp
	 * @param totimestamp
	 *            the totimestamp
	 * @return the accelerometer measurements between timestamps
	 */
	public List<AccelerometerMeasurement> getAccelerometerMeasurementsBetweenTimestamps(
			UUID sensing_environment_id, long fromtimestamp, long totimestamp) {
		LOG.debug("Retrieving getAccelerometerMeasurementsBetweenTimestamps from database");

		Query query = new Query();

		query.addCriteria(Criteria
				.where("timestamp")
				.exists(true)
				.andOperator(Criteria.where("timestamp").gt(fromtimestamp),
						Criteria.where("timestamp").lt(totimestamp)));
		if (sensing_environment_id != null) {
			return this.mongoOps.find(query, AccelerometerMeasurement.class,
					sensing_environment_id + "_"
							+ ACCELEROMETER_MEASUREMENTS_COLLECTION);
		}
		return null;
	}

	/**
	 * Gets the all accelerometer measurements.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @return the all accelerometer measurements
	 */
	public List<AccelerometerMeasurement> getAllAccelerometerMeasurements(
			UUID sensing_environment_id) {
		LOG.debug("Retrieving getAllAccelerometerMeasurements from database");

		if (sensing_environment_id != null) {
			return this.mongoOps.findAll(AccelerometerMeasurement.class,
					sensing_environment_id + "_"
							+ ACCELEROMETER_MEASUREMENTS_COLLECTION);
		}

		return null;
	}

	/**
	 * Delete deprecated accelerometer measurements.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 */
	public void deleteDeprecatedAccelerometerMeasurements(
			UUID sensing_environment_id, long timestamp) {
		Query query = new Query();
		query.addCriteria(Criteria.where("timestamp").exists(true)
				.andOperator(Criteria.where("timestamp").lt(timestamp)));

		WriteResult result = this.mongoOps.remove(query,
				AccelerometerMeasurement.class, sensing_environment_id + "_"
						+ ACCELEROMETER_MEASUREMENTS_COLLECTION);

		LOG.info(String.format(
				"Deleted %d accelerometer measurements from database",
				result.getN()));
	}

	
	public long getLastCollectionsTimestamp(String sensing_environment_id) {
		Query query = new Query();
		query.with(new Sort(Sort.Direction.DESC, "timestamp")).limit(1);
		AccelerometerMeasurement accelerometerMeasurement = this.mongoOps.findOne(
				query, AccelerometerMeasurement.class, sensing_environment_id + "_"
						+ ACCELEROMETER_MEASUREMENTS_COLLECTION);

		return accelerometerMeasurement == null ? 0 : accelerometerMeasurement
				.getTimestamp();
	}
}
