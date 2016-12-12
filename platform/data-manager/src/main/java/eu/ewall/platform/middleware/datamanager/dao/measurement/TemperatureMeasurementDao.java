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

import eu.ewall.platform.commons.datamodel.measure.TemperatureMeasurement;
import eu.ewall.platform.middleware.datamanager.dao.config.dbtype.EWallDBType;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;

/**
 * The Class TemperatureMeasurementDao.
 */
public class TemperatureMeasurementDao {

	/** The log. */
	private static final Logger LOG = LoggerFactory
			.getLogger(TemperatureMeasurementDao.class);

	/** The Constant Temperature_MEASUREMENTS_COLLECTION. */
	public static final String TEMPERATURE_MEASUREMENTS_COLLECTION = "temperatureMeasurement";

	/** The mongo ops. */
	private MongoOperations mongoOps;

	/**
	 * Instantiates a new temperature measurement dao.
	 */
	public TemperatureMeasurementDao() {
		mongoOps = MongoDBFactory.getMongoOperationsForDBType(EWallDBType.DATA);
	}

	/**
	 * Gets the temperature measurements by timestamp.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 * @return the temperature measurements by timestamp
	 */
	public TemperatureMeasurement getTemperatureMeasurementsByTimestamp(
			UUID sensing_environment_id, long timestamp) {
		LOG.debug("Retrieving TemperatureMeasurement with timestamp "
				+ timestamp + " from database.");

		Query query = new Query(Criteria.where("timestamp").is(timestamp));

		if (sensing_environment_id != null) {
			return this.mongoOps.findOne(query, TemperatureMeasurement.class,
					sensing_environment_id + "_"
							+ TEMPERATURE_MEASUREMENTS_COLLECTION);
		}
		return null;

	}

	/**
	 * Gets the temperature measurements between timestamps.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param fromtimestamp
	 *            the fromtimestamp
	 * @param totimestamp
	 *            the totimestamp
	 * @param room_name
	 *            the room_name
	 * @return the temperature measurements between timestamps
	 */
	public List<TemperatureMeasurement> getTemperatureMeasurementsBetweenTimestamps(
			UUID sensing_environment_id, long fromtimestamp, long totimestamp,
			String room_name) {
		LOG.debug("Retrieving getTemperatureMeasurementsBetweenTimestamps from database");

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
			return this.mongoOps.find(query, TemperatureMeasurement.class,
					sensing_environment_id + "_"
							+ TEMPERATURE_MEASUREMENTS_COLLECTION);
		}

		return null;
	}

	/**
	 * Gets the all temperature measurements.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @return the all temperature measurements
	 */
	public List<TemperatureMeasurement> getAllTemperatureMeasurements(
			UUID sensing_environment_id) {
		LOG.debug("Retrieving getAllAccelerometerMeasurements from database");

		if (sensing_environment_id != null) {
			return this.mongoOps.findAll(TemperatureMeasurement.class,
					sensing_environment_id + "_"
							+ TEMPERATURE_MEASUREMENTS_COLLECTION);
		}

		return null;
	}

	/**
	 * Delete deprecated temperature measurements.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 */
	public void deleteDeprecatedTemperatureMeasurements(
			UUID sensing_environment_id, long timestamp) {
		Query query = new Query();
		query.addCriteria(Criteria.where("timestamp").exists(true)
				.andOperator(Criteria.where("timestamp").lt(timestamp)));

		WriteResult result = this.mongoOps.remove(query,
				TemperatureMeasurement.class, sensing_environment_id + "_"
						+ TEMPERATURE_MEASUREMENTS_COLLECTION);

		LOG.info(String.format(
				"Deleted %d temperature measurements from database",
				result.getN()));
	}
}
