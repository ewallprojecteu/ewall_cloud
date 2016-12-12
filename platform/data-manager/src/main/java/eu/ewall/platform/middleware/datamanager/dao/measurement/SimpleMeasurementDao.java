/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.datamanager.dao.measurement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import eu.ewall.platform.commons.datamodel.measure.AccelerometerMeasurement;
import eu.ewall.platform.commons.datamodel.measure.CarbonMonoxideMeasurement;
import eu.ewall.platform.commons.datamodel.measure.ConstantQuantityMeasureType;
import eu.ewall.platform.commons.datamodel.measure.DoorStatus;
import eu.ewall.platform.commons.datamodel.measure.HumidityMeasurement;
import eu.ewall.platform.commons.datamodel.measure.IlluminanceMeasurement;
import eu.ewall.platform.commons.datamodel.measure.IndoorMeasurement;
import eu.ewall.platform.commons.datamodel.measure.LiquefiedPetroleumGasMeasurement;
import eu.ewall.platform.commons.datamodel.measure.MovementMeasurement;
import eu.ewall.platform.commons.datamodel.measure.NaturalGasMeasurement;
import eu.ewall.platform.commons.datamodel.measure.TemperatureMeasurement;
import eu.ewall.platform.commons.datamodel.sensing.EnvironmentalSensing;
import eu.ewall.platform.middleware.datamanager.dao.config.dbtype.EWallDBType;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;

/**
 * The Class SimpleMeasurementDao.
 */
public class SimpleMeasurementDao {

	/** The log. */
	private static final Logger LOG = LoggerFactory
			.getLogger(SimpleMeasurementDao.class);

	/** The Constants. */
	public static final String INDOOR_MEASUREMENTS_COLLECTION = "indoorMeasurement";

	/** The Constant TEMPERATURE_MEASUREMENTS_COLLECTION. */
	public static final String TEMPERATURE_MEASUREMENTS_COLLECTION = "temperatureMeasurement";

	/** The Constant HUMIDITY_MEASUREMENTS_COLLECTION. */
	public static final String HUMIDITY_MEASUREMENTS_COLLECTION = "humidityMeasurement";

	/** The Constant ILLUMINANCE_MEASUREMENTS_COLLECTION. */
	public static final String ILLUMINANCE_MEASUREMENTS_COLLECTION = "illuminanceMeasurement";

	/** The Constant MOVEMENT_MEASUREMENTS_COLLECTION. */
	public static final String MOVEMENT_MEASUREMENTS_COLLECTION = "movementMeasurement";

	/** The Constant DOOR_STATUS_COLLECTION. */
	private static final String DOOR_STATUS_COLLECTION = "doorStatus";
	
	/** The Constant CARBON_MONOXIDE_COLLECTION. */
	private static final String CARBON_MONOXIDE_COLLECTION = "carbonMonoxideMeasurement";
	
	/** The Constant LIQUIFIED_PETROLEUM_GAS_COLLECTION. */
	private static final String LIQUIFIED_PETROLEUM_GAS_COLLECTION = "liquefiedPetroleumGasMeasurement";
	
	/** The Constant NATURAL_GAS_MEASUREMENT. */
	private static final String NATURAL_GAS_MEASUREMENT = "naturalGasMeasurement";

	/** The mongo ops. */
	private MongoOperations mongoOps;

	/**
	 * Instantiates a new temperature measurement dao.
	 */
	public SimpleMeasurementDao() {
		mongoOps = MongoDBFactory.getMongoOperationsForDBType(EWallDBType.DATA);
	}

	/**
	 * Adds the measurement.
	 *
	 * @param sensing_environment_id            the sensing_environment_id
	 * @param device_id the device_id
	 * @param measurement            the measurement
	 * @return true, if successful
	 */
	public boolean addMeasurement(String sensing_environment_id, String device_id, EnvironmentalSensing environmentalSensing) {

		if (environmentalSensing.getTemperature() != null) {
			mongoOps.insert(new TemperatureMeasurement(environmentalSensing.getTemperature(), environmentalSensing.getTimestamp(), environmentalSensing.getIndoorPlaceName()),
					sensing_environment_id + "_"
							+ TEMPERATURE_MEASUREMENTS_COLLECTION);
		} 
		if (environmentalSensing.getHumidity() != null) {
			mongoOps.insert(new HumidityMeasurement(environmentalSensing.getHumidity(), environmentalSensing.getTimestamp(), environmentalSensing.getIndoorPlaceName()),
					sensing_environment_id + "_"
							+ HUMIDITY_MEASUREMENTS_COLLECTION);
		} 
		if (environmentalSensing.getIlluminance() != null) {
			mongoOps.insert(new IlluminanceMeasurement(environmentalSensing.getIlluminance(), environmentalSensing.getTimestamp(), environmentalSensing.getIndoorPlaceName()),
					sensing_environment_id + "_"
							+ ILLUMINANCE_MEASUREMENTS_COLLECTION);
		} 
		if (environmentalSensing.getMovement()!= null) {
			mongoOps.insert(new MovementMeasurement(environmentalSensing.getMovement(), environmentalSensing.getTimestamp(), environmentalSensing.getIndoorPlaceName()),
					sensing_environment_id + "_"
							+ MOVEMENT_MEASUREMENTS_COLLECTION);
		} 
		if (environmentalSensing.getDoorOpen() != null) {
			mongoOps.insert(new DoorStatus(environmentalSensing.getDoorOpen(), environmentalSensing.getTimestamp(), environmentalSensing.getIndoorPlaceName()),
					sensing_environment_id + "_"
							+ DOOR_STATUS_COLLECTION);
		} 
		if (environmentalSensing.getCarbonMonoxide() != null) {
			mongoOps.insert(new CarbonMonoxideMeasurement(environmentalSensing.getCarbonMonoxide(), environmentalSensing.getTimestamp(), environmentalSensing.getIndoorPlaceName()),
					sensing_environment_id + "_"
							+ CARBON_MONOXIDE_COLLECTION);
		} 
		if (environmentalSensing.getLiquefiedPetroleumGas() != null) {
			mongoOps.insert(new LiquefiedPetroleumGasMeasurement(environmentalSensing.getLiquefiedPetroleumGas(), environmentalSensing.getTimestamp(), environmentalSensing.getIndoorPlaceName()),
					sensing_environment_id + "_"
							+ LIQUIFIED_PETROLEUM_GAS_COLLECTION);
		} 
		if (environmentalSensing.getNaturalGas() != null) {
			mongoOps.insert(new NaturalGasMeasurement(environmentalSensing.getNaturalGas(), environmentalSensing.getTimestamp(), environmentalSensing.getIndoorPlaceName()),
					sensing_environment_id + "_"
							+ NATURAL_GAS_MEASUREMENT);
		} 

		return true;

	}

	/**
	 * Gets the all measurement.
	 *
	 * @param ewall_system_id
	 *            the ewall_system_id
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @return the all measurement
	 */
/*	public List<IndoorMeasurement> getAllMeasurement(String ewall_system_id,
			String sensing_environment_id) {
		List<IndoorMeasurement> indoorMeasurementsList = new ArrayList<IndoorMeasurement>();

		List<TemperatureMeasurement> temperatureMeasurmentsList = mongoOps
				.findAll(TemperatureMeasurement.class, ewall_system_id + ":"
						+ sensing_environment_id + ":"
						+ TEMPERATURE_MEASUREMENTS_COLLECTION);
		indoorMeasurementsList
				.addAll(convertTempToSimpleMeasurementsList(temperatureMeasurmentsList));

		List<HumidityMeasurement> humidityMeasurements = mongoOps.findAll(
				HumidityMeasurement.class, ewall_system_id + ":"
						+ sensing_environment_id + ":"
						+ HUMIDITY_MEASUREMENTS_COLLECTION);
		indoorMeasurementsList
				.addAll(convertHumToSimpleMeasurementsList(humidityMeasurements));

		List<IlluminanceMeasurement> illuminanceMeasurements = mongoOps
				.findAll(IlluminanceMeasurement.class, ewall_system_id + ":"
						+ sensing_environment_id + ":"
						+ ILLUMINANCE_MEASUREMENTS_COLLECTION);
		indoorMeasurementsList
				.addAll(convertIllumToSimpleMeasurementsList(illuminanceMeasurements));

		List<MovementMeasurement> movementMeasurements = mongoOps.findAll(
				MovementMeasurement.class, ewall_system_id + ":"
						+ sensing_environment_id + ":"
						+ MOVEMENT_MEASUREMENTS_COLLECTION);
		indoorMeasurementsList
				.addAll(convertMovToSimpleMeasurementsList(movementMeasurements));

		return indoorMeasurementsList;

	}
*/
	/**
	 * Convert temp to simple measurements list.
	 *
	 * @param measurementsList
	 *            the measurements list
	 * @return the list
	 */
/*	private List<IndoorMeasurement> convertTempToSimpleMeasurementsList(
			List<TemperatureMeasurement> measurementsList) {
		List<IndoorMeasurement> simpleMeasurementsList = new ArrayList<IndoorMeasurement>();

		for (TemperatureMeasurement measurement : measurementsList) {
			IndoorMeasurement simpleMeasurement = new IndoorMeasurement();
			double measuredValue = measurement.getMeasuredValueInCelsius();

			simpleMeasurement.setMeasuredValue((new Double(measuredValue))
					.toString());
			simpleMeasurement
					.setConstantQuantityMeasureType(ConstantQuantityMeasureType.TEMPERATURE_MEASURE_CELSIUS);
			simpleMeasurement.setTimestamp(measurement.getTimestamp());
			simpleMeasurement.setIndoorPlaceName(measurement.getIndoorPlaceName());
			simpleMeasurementsList.add(simpleMeasurement);
		}

		return simpleMeasurementsList;

	}
*/
	/**
	 * Convert hum to simple measurements list.
	 *
	 * @param measurementsList
	 *            the measurements list
	 * @return the list
	 */
/*	private List<IndoorMeasurement> convertHumToSimpleMeasurementsList(
			List<HumidityMeasurement> measurementsList) {
		List<IndoorMeasurement> simpleMeasurementsList = new ArrayList<IndoorMeasurement>();

		for (HumidityMeasurement measurement : measurementsList) {
			IndoorMeasurement simpleMeasurement = new IndoorMeasurement();
			simpleMeasurement.setMeasuredValue(measurement.getMeasuredValue());
			simpleMeasurement
					.setConstantQuantityMeasureType(ConstantQuantityMeasureType.HUMIDITY_MEASURE);
			simpleMeasurement.setTimestamp(measurement.getTimestamp());
			simpleMeasurement.setIndoorPlaceName(measurement.getIndoorPlaceName());
			simpleMeasurementsList.add(simpleMeasurement);
		}

		return simpleMeasurementsList;

	}
*/
	/**
	 * Convert illum to simple measurements list.
	 *
	 * @param measurementsList
	 *            the measurements list
	 * @return the list
	 */
/*	private List<IndoorMeasurement> convertIllumToSimpleMeasurementsList(
			List<IlluminanceMeasurement> measurementsList) {
		List<IndoorMeasurement> simpleMeasurementsList = new ArrayList<IndoorMeasurement>();

		for (IlluminanceMeasurement measurement : measurementsList) {
			IndoorMeasurement simpleMeasurement = new IndoorMeasurement();
			simpleMeasurement.setMeasuredValue(measurement.getMeasuredValue());
			simpleMeasurement
					.setConstantQuantityMeasureType(ConstantQuantityMeasureType.LUMINOUS_FLUX_MEASURE);
			simpleMeasurement.setTimestamp(measurement.getTimestamp());
			simpleMeasurement.setIndoorPlaceName(measurement.getIndoorPlaceName());
			simpleMeasurementsList.add(simpleMeasurement);
		}

		return simpleMeasurementsList;

	}
*/
	/**
	 * Convert mov to simple measurements list.
	 *
	 * @param measurementsList
	 *            the measurements list
	 * @return the list
	 */
/*	private List<IndoorMeasurement> convertMovToSimpleMeasurementsList(
			List<MovementMeasurement> measurementsList) {
		List<IndoorMeasurement> simpleMeasurementsList = new ArrayList<IndoorMeasurement>();

		for (MovementMeasurement measurement : measurementsList) {
			IndoorMeasurement simpleMeasurement = new IndoorMeasurement();

			simpleMeasurement.setMeasuredValue(measurement.getMeasuredValue());
			simpleMeasurement
					.setConstantQuantityMeasureType(ConstantQuantityMeasureType.MOVEMENT_MEASURE);
			simpleMeasurement.setTimestamp(measurement.getTimestamp());
			simpleMeasurement.setIndoorPlaceName(measurement.getIndoorPlaceName());
			simpleMeasurementsList.add(simpleMeasurement);
		}

		return simpleMeasurementsList;

	}
*/
	
	public long getLastCollectionsTimestamp(String sensing_environment_id, String room_name) {
		long lastTimestamp = Long.MIN_VALUE;
		Query query = new Query();
		query.addCriteria(Criteria
				.where("indoorPlaceName")
				.is(room_name));
		query.with(new Sort(Sort.Direction.DESC, "timestamp")).limit(1);
		
		TemperatureMeasurement temperatureMeasurement = this.mongoOps.findOne(
				query, TemperatureMeasurement.class, sensing_environment_id + "_"
						+ TEMPERATURE_MEASUREMENTS_COLLECTION);
		lastTimestamp = temperatureMeasurement == null ? lastTimestamp : Math.max(lastTimestamp, temperatureMeasurement
				.getTimestamp());
		
		
		HumidityMeasurement humidityMeasurement = this.mongoOps.findOne(
				query, HumidityMeasurement.class, sensing_environment_id + "_"
						+ HUMIDITY_MEASUREMENTS_COLLECTION);
		lastTimestamp = humidityMeasurement == null ? lastTimestamp : Math.max(lastTimestamp, humidityMeasurement
				.getTimestamp());
		
		
		IlluminanceMeasurement illuminanceMeasurement = this.mongoOps.findOne(
				query, IlluminanceMeasurement.class, sensing_environment_id + "_"
						+ ILLUMINANCE_MEASUREMENTS_COLLECTION);
		lastTimestamp = illuminanceMeasurement == null ? lastTimestamp : Math.max(lastTimestamp, illuminanceMeasurement
				.getTimestamp());
		
		
		MovementMeasurement movementMeasurement = this.mongoOps.findOne(
				query, MovementMeasurement.class, sensing_environment_id + "_"
						+ MOVEMENT_MEASUREMENTS_COLLECTION);
		lastTimestamp = movementMeasurement == null ? lastTimestamp : Math.max(lastTimestamp, movementMeasurement
				.getTimestamp());
		
		DoorStatus doorStatus = this.mongoOps.findOne(
				query, DoorStatus.class, sensing_environment_id + "_"
						+ DOOR_STATUS_COLLECTION);
		lastTimestamp = doorStatus == null ? lastTimestamp : Math.max(lastTimestamp, doorStatus
				.getTimestamp());
		
		
		CarbonMonoxideMeasurement carbonMonoxideMeasurement = this.mongoOps.findOne(
				query, CarbonMonoxideMeasurement.class, sensing_environment_id + "_"
						+ CARBON_MONOXIDE_COLLECTION);
		lastTimestamp = carbonMonoxideMeasurement == null ? lastTimestamp : Math.max(lastTimestamp, carbonMonoxideMeasurement
				.getTimestamp());
		
		LiquefiedPetroleumGasMeasurement liquefiedPetroleumGasMeasurement = this.mongoOps.findOne(
				query, LiquefiedPetroleumGasMeasurement.class, sensing_environment_id + "_"
						+ LIQUIFIED_PETROLEUM_GAS_COLLECTION);
		lastTimestamp = liquefiedPetroleumGasMeasurement == null ? lastTimestamp : Math.max(lastTimestamp, liquefiedPetroleumGasMeasurement
				.getTimestamp());
		
		NaturalGasMeasurement naturalGasMeasurement = this.mongoOps.findOne(
				query, NaturalGasMeasurement.class, sensing_environment_id + "_"
						+ NATURAL_GAS_MEASUREMENT);
		lastTimestamp = naturalGasMeasurement == null ? lastTimestamp : Math.max(lastTimestamp, naturalGasMeasurement
				.getTimestamp());
		
		return lastTimestamp;
	}

}
