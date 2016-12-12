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

import eu.ewall.platform.commons.datamodel.measure.BloodPressureMeasurement;
import eu.ewall.platform.commons.datamodel.measure.HeartRateMeasurement;
import eu.ewall.platform.commons.datamodel.measure.OxygenSaturationMeasurement;
import eu.ewall.platform.commons.datamodel.sensing.VitalsSensing;
import eu.ewall.platform.middleware.datamanager.dao.config.dbtype.EWallDBType;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;

/**
 * The Class UserDao.
 */
public class VitalSensingDao {

	/** The log. */
	private static final Logger LOG = LoggerFactory
			.getLogger(VitalSensingDao.class);

	/** The Constant HEART_RATE_MEASUREMENT_COLLECTION. */
	public static final String HEART_RATE_MEASUREMENT_COLLECTION = "heartRateMeasurement";

	/** The Constant OXYGEN_SATURATION_MEASUREMENT_COLLECTION. */
	public static final String OXYGEN_SATURATION_MEASUREMENT_COLLECTION = "oxygenSaturationMeasurement";

	/** The Constant BLOOD_PRESSURE_MEASUREMENT_COLLECTION. */
	public static final String BLOOD_PRESSURE_MEASUREMENT_COLLECTION = "bloodPressureMeasurement";

	/** The mongo ops. */
	private MongoOperations mongoOps;

	/**
	 * Instantiates a new user dao.
	 */
	public VitalSensingDao() {
		mongoOps = MongoDBFactory.getMongoOperationsForDBType(EWallDBType.DATA);
	}

	/**
	 * Creates the.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param device_id 
	 * @param vitalsSensing
	 *            the vitals sensing
	 */
	public void create(String sensing_environment_id, String device_id,
			VitalsSensing vitalsSensing) {
		if (vitalsSensing != null) {
			LOG.debug("vitals data not null.");
			if (vitalsSensing.getHearthRate() != -1
					|| vitalsSensing.getHearthRateVariability() != -1) {
				HeartRateMeasurement hrm = new HeartRateMeasurement(vitalsSensing.getHearthRate(), vitalsSensing.getHearthRateVariability() ,
						vitalsSensing.getTimestamp());
				hrm.setDeviceId(device_id);
				this.mongoOps.insert(hrm, sensing_environment_id + "_"
						+ HEART_RATE_MEASUREMENT_COLLECTION);

			}
			if (vitalsSensing.getOxygenSaturation() != -1) {
				OxygenSaturationMeasurement os = new OxygenSaturationMeasurement(
						vitalsSensing.getOxygenSaturation(),
						vitalsSensing.getTimestamp());
				os.setDeviceId(device_id);
				this.mongoOps.insert(os, sensing_environment_id + "_"
						+ OXYGEN_SATURATION_MEASUREMENT_COLLECTION);

			}
			if (vitalsSensing.getSystolicBloodPressure() != -1
					|| vitalsSensing.getDiastolicBloodPressure() != -1) {
				BloodPressureMeasurement bpm = new BloodPressureMeasurement(
						vitalsSensing.getSystolicBloodPressure() , vitalsSensing.getDiastolicBloodPressure(), vitalsSensing.getTimestamp());
				bpm.setDeviceId(device_id);
				this.mongoOps.insert(bpm, sensing_environment_id + "_"
						+ BLOOD_PRESSURE_MEASUREMENT_COLLECTION);
			}
	
		}
	}

	/**
	 * Gets the last collections timestamp.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param device_id 
	 * @return the last collections timestamp
	 */
	public long getLastCollectionsTimestamp(String sensing_environment_id, String device_id) {
		long lastTimestamp = Long.MIN_VALUE;
		Query query = new Query();
		query.addCriteria(Criteria
				.where("deviceId")
				.is(device_id));
		query.with(new Sort(Sort.Direction.DESC, "timestamp")).limit(1);
		HeartRateMeasurement heartRateMeasurement = this.mongoOps.findOne(
				query, HeartRateMeasurement.class, sensing_environment_id + "_"
						+ HEART_RATE_MEASUREMENT_COLLECTION);
		lastTimestamp = heartRateMeasurement == null ? lastTimestamp : Math.max(lastTimestamp, heartRateMeasurement
				.getTimestamp());
		
		OxygenSaturationMeasurement oxygenSaturationMeasurement = this.mongoOps.findOne(
				query, OxygenSaturationMeasurement.class, sensing_environment_id + "_"
						+ OXYGEN_SATURATION_MEASUREMENT_COLLECTION);
		lastTimestamp = oxygenSaturationMeasurement == null ? lastTimestamp : Math.max(lastTimestamp, oxygenSaturationMeasurement
				.getTimestamp());
		
		BloodPressureMeasurement bloodPressureMeasurement = this.mongoOps.findOne(
				query, BloodPressureMeasurement.class, sensing_environment_id + "_"
						+ BLOOD_PRESSURE_MEASUREMENT_COLLECTION);
		lastTimestamp = bloodPressureMeasurement == null ? lastTimestamp : Math.max(lastTimestamp, bloodPressureMeasurement
				.getTimestamp());

		return lastTimestamp;
	}

	/**
	 * Gets the heart rate between timestamps.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param fromtimestamp
	 *            the fromtimestamp
	 * @param totimestamp
	 *            the totimestamp
	 * @param latestEvents
	 *            the latest events
	 * @return the heart rate between timestamps
	 */
	public List<HeartRateMeasurement> getHeartRateBetweenTimestamps(
			UUID sensing_environment_id, Long fromtimestamp, Long totimestamp,
			Integer latestEvents) {
		LOG.debug("Retrieving getHeartRateBetweenTimestamps from database");

		if (fromtimestamp == null)
			fromtimestamp = Long.MIN_VALUE;
		
		if (totimestamp == null) 
			totimestamp = Long.MAX_VALUE;

		Query query = new Query();
		query.addCriteria(Criteria
					.where("timestamp")
					.exists(true)
					.andOperator(Criteria.where("timestamp").gt(fromtimestamp),
							Criteria.where("timestamp").lt(totimestamp)));
		
		if (latestEvents != null) {
			query.with(new Sort(Sort.Direction.DESC, "timestamp")).limit(
					latestEvents);
		}

		if (sensing_environment_id != null) {
			return this.mongoOps.find(query, HeartRateMeasurement.class,
					sensing_environment_id + "_"
							+ HEART_RATE_MEASUREMENT_COLLECTION);
		}
		return null;
	}

	/**
	 * Gets the oxygen saturation between timestamps.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param fromtimestamp
	 *            the fromtimestamp
	 * @param totimestamp
	 *            the totimestamp
	 * @param latestEvents
	 *            the latest events
	 * @return the oxygen saturation between timestamps
	 */
	public List<OxygenSaturationMeasurement> getOxygenSaturationBetweenTimestamps(
			UUID sensing_environment_id, Long fromtimestamp, Long totimestamp,
			Integer latestEvents) {
		LOG.debug("Retrieving getOxygenSaturationBetweenTimestamps from database");

		if (fromtimestamp == null)
			fromtimestamp = Long.MIN_VALUE;
		
		if (totimestamp == null) 
			totimestamp = Long.MAX_VALUE;

		Query query = new Query();
		query.addCriteria(Criteria
					.where("timestamp")
					.exists(true)
					.andOperator(Criteria.where("timestamp").gt(fromtimestamp),
							Criteria.where("timestamp").lt(totimestamp)));
		
		if (latestEvents != null) {
			query.with(new Sort(Sort.Direction.DESC, "timestamp")).limit(
					latestEvents);
		}

		if (sensing_environment_id != null) {
			return this.mongoOps.find(query, OxygenSaturationMeasurement.class,
					sensing_environment_id + "_"
							+ OXYGEN_SATURATION_MEASUREMENT_COLLECTION);
		}
		return null;
	}

	/**
	 * Gets the blood pressure between timestamps.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param fromtimestamp
	 *            the fromtimestamp
	 * @param totimestamp
	 *            the totimestamp
	 * @param latestEvents
	 *            the latest events
	 * @return the blood pressure between timestamps
	 */
	public List<BloodPressureMeasurement> getBloodPressureBetweenTimestamps(
			UUID sensing_environment_id, Long fromtimestamp, Long totimestamp,
			Integer latestEvents) {
		LOG.debug("Retrieving getBloodPressureBetweenTimestamps from database");

		if (fromtimestamp == null)
			fromtimestamp = Long.MIN_VALUE;
		
		if (totimestamp == null) 
			totimestamp = Long.MAX_VALUE;

		Query query = new Query();
		query.addCriteria(Criteria
					.where("timestamp")
					.exists(true)
					.andOperator(Criteria.where("timestamp").gt(fromtimestamp),
							Criteria.where("timestamp").lt(totimestamp)));

		if (latestEvents != null) {
			query.with(new Sort(Sort.Direction.DESC, "timestamp")).limit(
					latestEvents);
		}

		if (sensing_environment_id != null) {
			return this.mongoOps.find(query, BloodPressureMeasurement.class,
					sensing_environment_id + "_"
							+ BLOOD_PRESSURE_MEASUREMENT_COLLECTION);
		}
		return null;
	}

	/**
	 * Delete deprecated vitals sensing.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param timestamp
	 *            the timestamp
	 */
	public void deleteDeprecatedVitalsSensing(UUID sensing_environment_id,
			long timestamp) {
		Query query = new Query();
		query.addCriteria(Criteria.where("timestamp").exists(true)
				.andOperator(Criteria.where("timestamp").lt(timestamp)));

		WriteResult result = this.mongoOps.remove(query,
				BloodPressureMeasurement.class, sensing_environment_id + "_"
						+ BLOOD_PRESSURE_MEASUREMENT_COLLECTION);

		LOG.info(String.format("Deleted %d vitals sensing from database",
				result.getN()));
	}
}
