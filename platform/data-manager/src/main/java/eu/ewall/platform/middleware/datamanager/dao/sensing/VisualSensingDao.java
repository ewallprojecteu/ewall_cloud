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

import eu.ewall.platform.commons.datamodel.sensing.VisualSensing;
import eu.ewall.platform.middleware.datamanager.dao.config.dbtype.EWallDBType;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;

/**
 * The Class VisualSensingDao.
 */
public class VisualSensingDao {

	/** The log. */
	private static final Logger LOG = LoggerFactory
			.getLogger(VisualSensingDao.class);

	/** The Constant VISUAL_SENSING_COLLECTION. */
	public static final String VISUAL_SENSING_COLLECTION = "visualSensing";

	/** The mongo ops. */
	private MongoOperations mongoOps;

	/**
	 * Instantiates a new visual sensing dao.
	 */
	public VisualSensingDao() {
		mongoOps = MongoDBFactory
				.getMongoOperationsForDBType(EWallDBType.DATA);
	}

	/**
	 * Creates the.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param visualSensing
	 *            the visual sensing
	 */
	public void create(String sensing_environment_id,
			VisualSensing visualSensing) {
		this.mongoOps.insert(visualSensing, sensing_environment_id + "_" + VISUAL_SENSING_COLLECTION);

	}

	/**
	 * Gets the last collections timestamp.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @return the last collections timestamp
	 */
	public long getLastCollectionsTimestamp(String sensing_environment_id) {
		Query query = new Query();
		query.with(new Sort(Sort.Direction.DESC, "timestamp")).limit(1);
		VisualSensing visualSensing = this.mongoOps.findOne(query,
				VisualSensing.class, sensing_environment_id + "_"
						+ VISUAL_SENSING_COLLECTION);

		return visualSensing == null ? 0 : visualSensing.getTimestamp();
	}
	
	/**
	 * Gets the visual sensing between timestamps.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param fromtimestamp
	 *            the fromtimestamp
	 * @param totimestamp
	 *            the totimestamp
	 * @return the visual sensing between timestamps
	 */
	public List<VisualSensing> getVisualSensingBetweenTimestamps(
			UUID sensing_environment_id, long fromtimestamp, long totimestamp) {
		LOG.debug("Retrieving getVisualSensingBetweenTimestamps from database");

		Query query = new Query();

		query.addCriteria(Criteria
				.where("timestamp")
				.exists(true)
				.andOperator(Criteria.where("timestamp").gt(fromtimestamp),
						Criteria.where("timestamp").lt(totimestamp)));

		if (sensing_environment_id != null) {
			return this.mongoOps.find(query, VisualSensing.class,
					sensing_environment_id + "_" + VISUAL_SENSING_COLLECTION);
		}
		return null;
	}

	/**
	 * Delete deprecated visual sensing.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 */
	public void deleteDeprecatedVisualSensing(UUID sensing_environment_id,
			long timestamp) {
		Query query = new Query();
		query.addCriteria(Criteria.where("timestamp").exists(true)
				.andOperator(Criteria.where("timestamp").lt(timestamp)));

		WriteResult result = this.mongoOps.remove(query, VisualSensing.class,
				sensing_environment_id + "_" + VISUAL_SENSING_COLLECTION);

		LOG.info(String.format("Deleted %d visual sensing from database",
				result.getN()));
	}
}
