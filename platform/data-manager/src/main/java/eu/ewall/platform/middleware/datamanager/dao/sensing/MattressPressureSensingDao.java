/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.datamanager.dao.sensing;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.WriteResult;

import eu.ewall.platform.commons.datamodel.measure.MattressPressureSensing;
import eu.ewall.platform.middleware.datamanager.dao.config.dbtype.EWallDBType;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;

/**
 * The Class MattressPressureSensingDao.
 */
public class MattressPressureSensingDao {

	/** The log. */
	private static final Logger LOG = LoggerFactory
			.getLogger(MattressPressureSensingDao.class);

	/** The Constant MATTRESS_PRESSURE_SENSING_COLLECTION. */
	public static final String MATTRESS_PRESSURE_SENSING_COLLECTION = "mattressPressureSensing";

	/** The mongo ops. */
	private MongoOperations mongoOps;

	/**
	 * Instantiates a new mattress pressure sensing dao.
	 */
	public MattressPressureSensingDao() {
		mongoOps = MongoDBFactory
				.getMongoOperationsForDBType(EWallDBType.DATA);
	}
	
	/**
	 * Creates the.
	 *
	 * @param sensing_environment_id the sensing_environment_id
	 * @param mattressPressureSensing the mattress pressure sensing
	 */
	public void create(String sensing_environment_id,
			MattressPressureSensing mattressPressureSensing) {
		this.mongoOps.insert(mattressPressureSensing, sensing_environment_id + "_"
				+ MATTRESS_PRESSURE_SENSING_COLLECTION);
	}

	/**
	 * Gets the mattress pressure sensing between timestamps and room name.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param fromtimestamp
	 *            the fromtimestamp
	 * @param totimestamp
	 *            the totimestamp
	 * @param room_name
	 *            the room_name
	 * @return the mattress pressure sensing between timestamps
	 */
	public List<MattressPressureSensing> getMattressPressureSensingBetweenTimestamps(
			UUID sensing_environment_id, long fromtimestamp, long totimestamp,
			String room_name) {
		LOG.debug("Retrieving getMattressPressureSensingBetweenTimestamps from database");

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
			return this.mongoOps.find(query, MattressPressureSensing.class,
					sensing_environment_id + "_"
							+ MATTRESS_PRESSURE_SENSING_COLLECTION);
		}
		return null;
	}

	/**
	 * Delete deprecated mattress pressure sensing.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 */
	public void deleteDeprecatedMattressPressureSensing(
			UUID sensing_environment_id, long timestamp) {
		Query query = new Query();
		query.addCriteria(Criteria.where("timestamp").exists(true)
				.andOperator(Criteria.where("timestamp").lt(timestamp)));

		WriteResult result = this.mongoOps.remove(query,
				MattressPressureSensing.class, sensing_environment_id + "_"
						+ MATTRESS_PRESSURE_SENSING_COLLECTION);

		LOG.info(String.format(
				"Deleted %d mattress pressure sensing from database",
				result.getN()));
	}
	
	public long getLastCollectionsTimestamp(String sensing_environment_id, String room_name) {
		Query query = new Query();
		query.addCriteria(Criteria
				.where("indoorPlaceName")
				.is(room_name));
		query.with(new Sort(Sort.Direction.DESC, "timestamp")).limit(1);
		MattressPressureSensing mattressPressureSensing = this.mongoOps.findOne(
				query, MattressPressureSensing.class, sensing_environment_id + "_"
						+ MATTRESS_PRESSURE_SENSING_COLLECTION);

		return mattressPressureSensing == null ? 0 : mattressPressureSensing
				.getTimestamp();
	}
}
