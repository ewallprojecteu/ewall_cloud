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

import eu.ewall.platform.commons.datamodel.sensing.SpeakerSensing;
import eu.ewall.platform.middleware.datamanager.dao.config.dbtype.EWallDBType;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;

/**
 * The Class SpeakerSensingDao.
 */
public class SpeakerSensingDao {

	/** The log. */
	private static final Logger LOG = LoggerFactory
			.getLogger(SpeakerSensingDao.class);

	/** The Constant SPEAKER_SENSING_COLLECTION. */
	public static final String SPEAKER_SENSING_COLLECTION = "speakerSensing";

	/** The mongo ops. */
	private MongoOperations mongoOps;

	/**
	 * Instantiates a new speaker sensing dao.
	 */
	public SpeakerSensingDao() {
		mongoOps = MongoDBFactory
				.getMongoOperationsForDBType(EWallDBType.DATA);
	}
	
	/**
	 * Creates the.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param speakerSensing
	 *            the speaker sensing
	 */
	public void create(String sensing_environment_id,
			SpeakerSensing speakerSensing) {

		if (speakerSensing != null) {
			this.mongoOps
					.insert(speakerSensing, sensing_environment_id + "_"
							+ SPEAKER_SENSING_COLLECTION);
		}

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
		SpeakerSensing speakerSensing = this.mongoOps.findOne(query,
				SpeakerSensing.class, sensing_environment_id + "_"
						+ SPEAKER_SENSING_COLLECTION);

		return speakerSensing == null ? 0 : speakerSensing.getTimestamp();
	}

	/**
	 * Gets the speaker sensing between timestamps.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param fromtimestamp
	 *            the fromtimestamp
	 * @param totimestamp
	 *            the totimestamp
	 * @return the speaker sensing between timestamps
	 */
	public List<SpeakerSensing> getSpeakerSensingBetweenTimestamps(
			UUID sensing_environment_id, long fromtimestamp, long totimestamp) {
		LOG.debug("Retrieving getSpeakerSensingBetweenTimestamps from database");

		Query query = new Query();

		query.addCriteria(Criteria
				.where("timestamp")
				.exists(true)
				.andOperator(Criteria.where("timestamp").gt(fromtimestamp),
						Criteria.where("timestamp").lt(totimestamp)));

		if (sensing_environment_id != null) {
			return this.mongoOps.find(query, SpeakerSensing.class,
					sensing_environment_id + "_" + SPEAKER_SENSING_COLLECTION);
		}
		return null;
	}

	/**
	 * Delete deprecated speaker sensing.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 */
	public void deleteDeprecatedSpeakerSensing(UUID sensing_environment_id,
			long timestamp) {
		Query query = new Query();
		query.addCriteria(Criteria.where("timestamp").exists(true)
				.andOperator(Criteria.where("timestamp").lt(timestamp)));

		WriteResult result = this.mongoOps.remove(query, SpeakerSensing.class,
				sensing_environment_id + "_" + SPEAKER_SENSING_COLLECTION);

		LOG.info(String.format("Deleted %d speaker sensing from database",
				result.getN()));
	}
}
