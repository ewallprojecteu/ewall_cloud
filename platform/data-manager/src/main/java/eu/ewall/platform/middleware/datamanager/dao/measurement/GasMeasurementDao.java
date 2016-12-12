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

import eu.ewall.platform.commons.datamodel.measure.CarbonMonoxideMeasurement;
import eu.ewall.platform.commons.datamodel.measure.LiquefiedPetroleumGasMeasurement;
import eu.ewall.platform.commons.datamodel.measure.NaturalGasMeasurement;
import eu.ewall.platform.middleware.datamanager.dao.config.dbtype.EWallDBType;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;

/**
 * The Class GasMeasurementDao.
 */
public class GasMeasurementDao {

	/** The log. */
	private static final Logger LOG = LoggerFactory
			.getLogger(GasMeasurementDao.class);

	/** The Constant CARBON_MONOXIDE_COLLECTION. */
	private static final String CARBON_MONOXIDE_COLLECTION = "carbonMonoxideMeasurement";

	/** The Constant LIQUIFIED_PETROLEUM_GAS_COLLECTION. */
	private static final String LIQUIFIED_PETROLEUM_GAS_COLLECTION = "liquefiedPetroleumGasMeasurement";

	/** The Constant NATURAL_GAS_MEASUREMENT. */
	private static final String NATURAL_GAS_MEASUREMENT = "naturalGasMeasurement";

	/** The mongo ops. */
	private MongoOperations mongoOps;

	/**
	 * Instantiates a new gas measurement dao.
	 */
	public GasMeasurementDao() {
		mongoOps = MongoDBFactory.getMongoOperationsForDBType(EWallDBType.DATA);
	}

	/**
	 * Gets the carbon monoxide measurements between timestamps and room name.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param fromtimestamp
	 *            the fromtimestamp
	 * @param totimestamp
	 *            the totimestamp
	 * @param room_name
	 *            the room_name
	 * @return the carbon monoxide measurements between timestamps
	 */
	public List<CarbonMonoxideMeasurement> getCarbonMonoxideMeasurementBetweenTimestamps(
			UUID sensing_environment_id, long fromtimestamp, long totimestamp,
			String room_name) {
		LOG.debug("Retrieving getCarbonMonoxideMeasurementBetweenTimestamps from database");

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
			return this.mongoOps.find(query, CarbonMonoxideMeasurement.class,
					sensing_environment_id + "_" + CARBON_MONOXIDE_COLLECTION);
		}
		return null;
	}

	/**
	 * Gets the LPG measurements between timestamps and room name.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param fromtimestamp
	 *            the fromtimestamp
	 * @param totimestamp
	 *            the totimestamp
	 * @param room_name
	 *            the room_name
	 * @return the carbon monoxide measurements between timestamps
	 */
	public List<LiquefiedPetroleumGasMeasurement> getLiquefiedPetroleumGasMeasurementBetweenTimestamps(
			UUID sensing_environment_id, long fromtimestamp, long totimestamp,
			String room_name) {
		LOG.debug("Retrieving getLiquefiedPetroleumGasMeasurementBetweenTimestamps from database");

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
			return this.mongoOps.find(query,
					LiquefiedPetroleumGasMeasurement.class,
					sensing_environment_id + "_"
							+ LIQUIFIED_PETROLEUM_GAS_COLLECTION);
		}
		return null;
	}

	/**
	 * Gets the natural gas measurements between timestamps and room name.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param fromtimestamp
	 *            the fromtimestamp
	 * @param totimestamp
	 *            the totimestamp
	 * @param room_name
	 *            the room_name
	 * @return the carbon monoxide measurements between timestamps
	 */
	public List<NaturalGasMeasurement> getNaturalGasMeasurementBetweenTimestamps(
			UUID sensing_environment_id, long fromtimestamp, long totimestamp,
			String room_name) {
		LOG.debug("Retrieving getNaturalGasMeasurementBetweenTimestamps from database");

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
			return this.mongoOps.find(query, NaturalGasMeasurement.class,
					sensing_environment_id + "_" + NATURAL_GAS_MEASUREMENT);
		}
		return null;
	}

	/**
	 * Delete deprecated gases measurements.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 */
	public void deleteDeprecatedGasesMeasurements(UUID sensing_environment_id,
			long timestamp) {
		Query query = new Query();
		query.addCriteria(Criteria.where("timestamp").exists(true)
				.andOperator(Criteria.where("timestamp").lt(timestamp)));

		WriteResult result = this.mongoOps.remove(query,
				NaturalGasMeasurement.class, sensing_environment_id + "_"
						+ NATURAL_GAS_MEASUREMENT);

		LOG.info(String.format("Deleted %d gases measurements from database",
				result.getN()));
	}
}
