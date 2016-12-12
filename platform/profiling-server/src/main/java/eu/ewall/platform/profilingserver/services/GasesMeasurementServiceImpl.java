/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.profilingserver.services;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import eu.ewall.platform.commons.datamodel.ewallsystem.SensingEnvironment;
import eu.ewall.platform.commons.datamodel.measure.CarbonMonoxideMeasurement;
import eu.ewall.platform.commons.datamodel.measure.LiquefiedPetroleumGasMeasurement;
import eu.ewall.platform.commons.datamodel.measure.NaturalGasMeasurement;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;
import eu.ewall.platform.middleware.datamanager.dao.ewallsystem.SensingEnvironmentDao;
import eu.ewall.platform.middleware.datamanager.dao.measurement.GasMeasurementDao;

/**
 * The Class HumidityMeasurementServiceImpl.
 * 
 * @author EMIRMOS
 * 
 */
@Service("gasesMeasurementService")
public class GasesMeasurementServiceImpl implements DisposableBean {

	/** The log. */
	Logger log = LoggerFactory.getLogger(GasesMeasurementServiceImpl.class);

	/** The gas measurement dao. */
	private GasMeasurementDao gasMeasurementDao;

	/** The sens environment dao. */
	private SensingEnvironmentDao sensEnvironmentDao;

	/**
	 * Instantiates a new user service impl.
	 */
	public GasesMeasurementServiceImpl() {
		gasMeasurementDao = new GasMeasurementDao();
		sensEnvironmentDao = new SensingEnvironmentDao();
	}

	/**
	 * Gets the CarbonMonoxideMeasurement between timestamps and room_name.
	 *
	 * @param username            the username
	 * @param fromtimestamp            the fromtimestamp
	 * @param totimestamp            the totimestamp
	 * @param room_name the room_name
	 * @return the gases measurements between timestamps
	 */
	public List<CarbonMonoxideMeasurement> getCarbonMonoxideMeasurementBetweenTimestamps(
			String username, long fromtimestamp, long totimestamp,
			String room_name) {
		log.debug("getCarbonMonoxideMeasurementBetweenTimestamps");

		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return gasMeasurementDao
					.getCarbonMonoxideMeasurementBetweenTimestamps(
							sensingEnvironment.getUuid(), fromtimestamp,
							totimestamp, room_name);

		else
			return null;

	}

	/**
	 * Gets the LiquefiedPetroleumGasMeasurement between timestamps and
	 * room_name.
	 *
	 * @param username            the username
	 * @param fromtimestamp            the fromtimestamp
	 * @param totimestamp            the totimestamp
	 * @param room_name the room_name
	 * @return the gases measurements between timestamps
	 */
	public List<LiquefiedPetroleumGasMeasurement> getLiquefiedPetroleumGasMeasurementBetweenTimestamps(
			String username, long fromtimestamp, long totimestamp,
			String room_name) {
		log.debug("getLiquefiedPetroleumGasMeasurementBetweenTimestamps");

		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return gasMeasurementDao
					.getLiquefiedPetroleumGasMeasurementBetweenTimestamps(
							sensingEnvironment.getUuid(), fromtimestamp,
							totimestamp, room_name);

		else
			return null;

	}

	/**
	 * Gets the NaturalGasMeasurement between timestamps and room_name.
	 *
	 * @param username            the username
	 * @param fromtimestamp            the fromtimestamp
	 * @param totimestamp            the totimestamp
	 * @param room_name the room_name
	 * @return the gases measurements between timestamps
	 */
	public List<NaturalGasMeasurement> getNaturalGasMeasurementBetweenTimestamps(
			String username, long fromtimestamp, long totimestamp,
			String room_name) {
		log.debug("getNaturalGasMeasurementBetweenTimestamps");

		// get sensing environment to which user belongs to
		SensingEnvironment sensingEnvironment = sensEnvironmentDao
				.getSensingEnvironmentByPrimaryUser(username);

		if (sensingEnvironment != null)
			return gasMeasurementDao
					.getNaturalGasMeasurementBetweenTimestamps(
							sensingEnvironment.getUuid(), fromtimestamp,
							totimestamp, room_name);

		else
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
		log.debug("deleteDeprecatedGasesMeasurements: " + timestamp);

		gasMeasurementDao.deleteDeprecatedGasesMeasurements(
				sensing_environment_id, timestamp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() throws Exception {
		MongoDBFactory.close();
	}

}
