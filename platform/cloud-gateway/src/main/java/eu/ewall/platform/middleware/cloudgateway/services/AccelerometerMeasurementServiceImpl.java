/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.cloudgateway.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import eu.ewall.platform.commons.datamodel.measure.AccelerometerMeasurement;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;
import eu.ewall.platform.middleware.datamanager.dao.measurement.AccelerometerMeasurementDao;

/**
 * The Class AccelerometerMeasurementServiceImpl.
 *
 * @author eandgrg
 */
@Service("accelerometerMeasurementService")
public class AccelerometerMeasurementServiceImpl implements DisposableBean {

	/** The log. */
	Logger log = LoggerFactory
			.getLogger(AccelerometerMeasurementServiceImpl.class);

	/** The accelerometer measurement dao. */
	private AccelerometerMeasurementDao accelerometerMeasurementDao;

	/**
	 * Instantiates a new user service impl.
	 */
	public AccelerometerMeasurementServiceImpl() {
		accelerometerMeasurementDao = new AccelerometerMeasurementDao();
	}

	/**
	 * Adds the accelerometer measurement.
	 *
	 * @param sensing_environment_id the sensing_environment_id
	 * @param accelerometerMeasurement            the accelerometer measurement
	 */
	public void addAccelerometerMeasurement(String sensing_environment_id,
			AccelerometerMeasurement accelerometerMeasurement) {
		log.debug("addAccelerometerMeasurement");

		accelerometerMeasurementDao.create(sensing_environment_id, accelerometerMeasurement);
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

	public long getLastCollectionsTimestamp(String sensing_environment_id) {
		return accelerometerMeasurementDao.getLastCollectionsTimestamp(sensing_environment_id);
	}

}
