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

import eu.ewall.platform.commons.datamodel.sensing.AppliancePowerSensing;
import eu.ewall.platform.commons.datamodel.sensing.ApplianceType;
import eu.ewall.platform.middleware.datamanager.dao.config.dbtype.EWallDBType;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;

/**
 * The Class AppliancePowerSensingDao.
 */
public class AppliancePowerSensingDao {

	/** The log. */
	private static final Logger LOG = LoggerFactory
			.getLogger(AppliancePowerSensingDao.class);

	/** The Constant APPLIANCE_POWER_SENSING_COLLECTION. */
	public static final String APPLIANCE_POWER_SENSING_COLLECTION = "appliancePowerSensing";

	/** The mongo ops. */
	private MongoOperations mongoOps;

	/**
	 * Instantiates a new appliance power sensing dao.
	 */
	public AppliancePowerSensingDao() {
		mongoOps = MongoDBFactory
				.getMongoOperationsForDBType(EWallDBType.DATA);
	}
	
	/**
	 * Creates the.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param powerSensing
	 *            the power sensing
	 */
	public void create(String sensing_environment_id,
			AppliancePowerSensing powerSensing) {

		if (powerSensing != null) {

			this.mongoOps.insert(powerSensing, sensing_environment_id + "_"
					+ APPLIANCE_POWER_SENSING_COLLECTION);
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
		AppliancePowerSensing appliancePowerSensing = this.mongoOps.findOne(
				query, AppliancePowerSensing.class, sensing_environment_id + "_"
						+ APPLIANCE_POWER_SENSING_COLLECTION);

		return appliancePowerSensing == null ? 0 : appliancePowerSensing
				.getTimestamp();
	}

	/**
	 * Gets the appliance power sensing between timestamps.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param fromtimestamp
	 *            the fromtimestamp
	 * @param totimestamp
	 *            the totimestamp
	 * @param applianceName
	 *            the appliance name
	 * @param applianceType
	 *            the appliance type
	 * @param roomName
	 *            the room name
	 * @return the appliance power sensing between timestamps
	 */
	public List<AppliancePowerSensing> getAppliancePowerSensingBetweenTimestamps(
			UUID sensing_environment_id, long fromtimestamp, long totimestamp,
			String applianceName, ApplianceType applianceType, String roomName) {
		LOG.debug("Retrieving getAppliancePowerSensingBetweenTimestamps from database");

		Query query = new Query();

		query.addCriteria(Criteria
				.where("timestamp")
				.exists(true)
				.andOperator(Criteria.where("timestamp").gt(fromtimestamp),
						Criteria.where("timestamp").lt(totimestamp)));

		if (roomName != null)
			query.addCriteria(Criteria.where("indoorPlaceName").is(roomName));

		if (applianceName != null)
			query.addCriteria(Criteria.where("applianceName").is(applianceName));

		if (applianceType != null)
			query.addCriteria(Criteria.where("applianceType").is(applianceType));

		if (sensing_environment_id != null) {
			return this.mongoOps.find(query, AppliancePowerSensing.class,
					sensing_environment_id + "_"
							+ APPLIANCE_POWER_SENSING_COLLECTION);
		}
		return null;
	}

	/**
	 * Delete deprecated appliance power sensing.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 */
	public void deleteDeprecatedAppliancePowerSensing(
			UUID sensing_environment_id, long timestamp) {
		Query query = new Query();
		query.addCriteria(Criteria.where("timestamp").exists(true)
				.andOperator(Criteria.where("timestamp").lt(timestamp)));

		WriteResult result = this.mongoOps.remove(query,
				AppliancePowerSensing.class, sensing_environment_id + "_"
						+ APPLIANCE_POWER_SENSING_COLLECTION);

		LOG.info(String.format(
				"Deleted %d appliance power sensing from database",
				result.getN()));
	}
}
