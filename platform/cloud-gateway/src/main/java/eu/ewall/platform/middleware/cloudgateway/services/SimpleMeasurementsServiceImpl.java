/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.cloudgateway.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import eu.ewall.platform.commons.datamodel.sensing.EnvironmentalSensing;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;
import eu.ewall.platform.middleware.datamanager.dao.measurement.SimpleMeasurementDao;

/**
 * The Class EWallSystemManagementServiceImpl.
 *
 * @author emirmos
 */
@Service("MeasurementsService")
public class SimpleMeasurementsServiceImpl implements DisposableBean {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(SimpleMeasurementsServiceImpl.class);

	/** The measurement dao. */
	private SimpleMeasurementDao measurementDao;

	/**
	 * Instantiates a new measurements service impl.
	 */
	public SimpleMeasurementsServiceImpl() {
		measurementDao = new SimpleMeasurementDao();
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
		return measurementDao.addMeasurement(sensing_environment_id, device_id, environmentalSensing);
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

	public long getLastCollectionsTimestamp(String sensing_environment_id, String room_name) {
		return measurementDao.getLastCollectionsTimestamp(sensing_environment_id, room_name);
	}

}
