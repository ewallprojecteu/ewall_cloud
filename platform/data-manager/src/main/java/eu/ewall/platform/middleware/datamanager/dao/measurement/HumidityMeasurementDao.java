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

import eu.ewall.platform.commons.datamodel.measure.HumidityMeasurement;
import eu.ewall.platform.middleware.datamanager.dao.config.dbtype.EWallDBType;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;

/**
 * The Class HumidityMeasurementDao.
 */
public class HumidityMeasurementDao {

	/** The log. */
	private static final Logger LOG = LoggerFactory
			.getLogger(HumidityMeasurementDao.class);

	/** The Constant HUMIDITY_MEASUREMENTS_COLLECTION. */
	public static final String HUMIDITY_MEASUREMENTS_COLLECTION = "humidityMeasurement";

	/** The mongo ops. */
	private MongoOperations mongoOps;

	/**
	 * Instantiates a new humidity measurement dao.
	 */
	public HumidityMeasurementDao() {
		mongoOps = MongoDBFactory.getMongoOperationsForDBType(EWallDBType.DATA);
	}

	/**
	 * Gets the humidity measurements by timestamp.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 * @return the humidity measurements by timestamp
	 */
	public HumidityMeasurement getHumidityMeasurementsByTimestamp(
			UUID sensing_environment_id, long timestamp) {

		Query query = new Query(Criteria.where("timestamp").is(timestamp));

		if (sensing_environment_id != null) {
			return this.mongoOps.findOne(query, HumidityMeasurement.class,
					sensing_environment_id + "_"
							+ HUMIDITY_MEASUREMENTS_COLLECTION);
		}
		return null;

	}

	/**
	 * Gets the humidity measurements between timestamps.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param fromtimestamp
	 *            the fromtimestamp
	 * @param totimestamp
	 *            the totimestamp
	 * @param room_name
	 *            the room_name
	 * @return the humidity measurements between timestamps
	 */
	public List<HumidityMeasurement> getHumidityMeasurementsBetweenTimestamps(
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
			return this.mongoOps.find(query, HumidityMeasurement.class,
					sensing_environment_id + "_"
							+ HUMIDITY_MEASUREMENTS_COLLECTION);
		}
		return null;
	}

	/**
	 * Gets the all humidity measurements.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @return the all humidity measurements
	 */
	public List<HumidityMeasurement> getAllHumidityMeasurements(
			UUID sensing_environment_id) {

		if (sensing_environment_id != null) {
			return this.mongoOps.findAll(HumidityMeasurement.class,
					sensing_environment_id + "_"
							+ HUMIDITY_MEASUREMENTS_COLLECTION);
		}

		return null;
	}

	/**
	 * Delete deprecated humidity measurements.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 */
	public void deleteDeprecatedHumidityMeasurements(
			UUID sensing_environment_id, long timestamp) {
		Query query = new Query();
		query.addCriteria(Criteria.where("timestamp").exists(true)
				.andOperator(Criteria.where("timestamp").lt(timestamp)));

		WriteResult result = this.mongoOps.remove(query,
				HumidityMeasurement.class, sensing_environment_id + "_"
						+ HUMIDITY_MEASUREMENTS_COLLECTION);

		LOG.info(String.format(
				"Deleted %d humidity measurements from database", result.getN()));
	}
}
