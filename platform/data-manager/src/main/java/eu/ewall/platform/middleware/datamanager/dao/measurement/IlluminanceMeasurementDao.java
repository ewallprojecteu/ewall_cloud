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

import eu.ewall.platform.commons.datamodel.measure.IlluminanceMeasurement;
import eu.ewall.platform.middleware.datamanager.dao.config.dbtype.EWallDBType;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;

/**
 * The Class IlluminanceMeasurementDao.
 */
public class IlluminanceMeasurementDao {

	/** The log. */
	private static final Logger LOG = LoggerFactory
			.getLogger(IlluminanceMeasurementDao.class);

	/** The Constant Illuminance_MEASUREMENTS_COLLECTION. */
	public static final String ILLUMINANCE_MEASUREMENTS_COLLECTION = "illuminanceMeasurement";

	/** The mongo ops. */
	private MongoOperations mongoOps;

	/**
	 * Instantiates a new illuminance measurement dao.
	 */
	public IlluminanceMeasurementDao() {
		mongoOps = MongoDBFactory.getMongoOperationsForDBType(EWallDBType.DATA);
	}

	/**
	 * Gets the illuminance measurements by timestamp.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 * @return the illuminance measurements by timestamp
	 */
	public IlluminanceMeasurement getIlluminanceMeasurementsByTimestamp(
			UUID sensing_environment_id, long timestamp) {

		Query query = new Query(Criteria.where("timestamp").is(timestamp));

		if (sensing_environment_id != null) {
			return this.mongoOps.findOne(query, IlluminanceMeasurement.class,
					sensing_environment_id + "_"
							+ ILLUMINANCE_MEASUREMENTS_COLLECTION);
		}
		return null;

	}

	/**
	 * Gets the illuminance measurements between timestamps.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param fromtimestamp
	 *            the fromtimestamp
	 * @param totimestamp
	 *            the totimestamp
	 * @param room_name
	 *            the room_name
	 * @return the illuminance measurements between timestamps
	 */
	public List<IlluminanceMeasurement> getIlluminanceMeasurementsBetweenTimestamps(
			UUID sensing_environment_id, long fromtimestamp, long totimestamp,
			String room_name) {

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
			return this.mongoOps.find(query, IlluminanceMeasurement.class,
					sensing_environment_id + "_"
							+ ILLUMINANCE_MEASUREMENTS_COLLECTION);
		}
		return null;
	}

	/**
	 * Gets the all illuminance measurements.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @return the all illuminance measurements
	 */
	public List<IlluminanceMeasurement> getAllIlluminanceMeasurements(
			UUID sensing_environment_id) {

		if (sensing_environment_id != null) {
			return this.mongoOps.findAll(IlluminanceMeasurement.class,
					sensing_environment_id + "_"
							+ ILLUMINANCE_MEASUREMENTS_COLLECTION);
		}

		return null;
	}

	/**
	 * Delete deprecated illuminance measurements.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 */
	public void deleteDeprecatedIlluminanceMeasurements(
			UUID sensing_environment_id, long timestamp) {
		Query query = new Query();
		query.addCriteria(Criteria.where("timestamp").exists(true)
				.andOperator(Criteria.where("timestamp").lt(timestamp)));

		WriteResult result = this.mongoOps.remove(query,
				IlluminanceMeasurement.class, sensing_environment_id + "_"
						+ ILLUMINANCE_MEASUREMENTS_COLLECTION);

		LOG.info(String.format(
				"Deleted %d illuminance measurements from database",
				result.getN()));
	}
}
