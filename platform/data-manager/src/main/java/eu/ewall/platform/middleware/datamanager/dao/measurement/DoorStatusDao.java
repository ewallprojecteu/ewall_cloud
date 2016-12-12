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

import eu.ewall.platform.commons.datamodel.measure.DoorStatus;
import eu.ewall.platform.middleware.datamanager.dao.config.dbtype.EWallDBType;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;

/**
 * The Class DoorStatusDao.
 */
public class DoorStatusDao {

	/** The log. */
	private static final Logger LOG = LoggerFactory
			.getLogger(DoorStatusDao.class);

	/** The Constant DOOR_STATUS_COLLECTION. */
	private static final String DOOR_STATUS_COLLECTION = "doorStatus";

	/** The mongo ops. */
	private MongoOperations mongoOps;

	/**
	 * Instantiates a new door status dao.
	 */
	public DoorStatusDao() {
		mongoOps = MongoDBFactory.getMongoOperationsForDBType(EWallDBType.DATA);
	}

	/**
	 * Gets the door status between timestamps and room name.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param fromtimestamp
	 *            the fromtimestamp
	 * @param totimestamp
	 *            the totimestamp
	 * @param room_name
	 *            the room_name
	 * @return the door status between timestamps
	 */
	public List<DoorStatus> getDoorStatusBetweenTimestamps(
			UUID sensing_environment_id, long fromtimestamp, long totimestamp,
			String room_name) {
		LOG.debug("Retrieving getDoorStatusBetweenTimestamps from database");

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
			return this.mongoOps.find(query, DoorStatus.class,
					sensing_environment_id + "_" + DOOR_STATUS_COLLECTION);
		}
		return null;
	}

	/**
	 * Delete deprecated door statuses.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 */
	public void deleteDeprecatedDoorStatuses(UUID sensing_environment_id,
			long timestamp) {
		Query query = new Query();
		query.addCriteria(Criteria.where("timestamp").exists(true)
				.andOperator(Criteria.where("timestamp").lt(timestamp)));

		WriteResult result = this.mongoOps.remove(query, DoorStatus.class,
				sensing_environment_id + "_" + DOOR_STATUS_COLLECTION);

		LOG.info(String.format("Deleted %d door statuses from database",
				result.getN()));
	}
}
