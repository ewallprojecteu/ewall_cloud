/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.cloudgateway.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import eu.ewall.platform.commons.datamodel.measure.MattressPressureSensing;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;
import eu.ewall.platform.middleware.datamanager.dao.sensing.MattressPressureSensingDao;

/**
 * The Class MattressPressureSensingServiceImpl.
 * 
 * @author emirmos
 */
@Service("mattressPressureSensingService")
public class MattressPressureSensingServiceImpl implements DisposableBean {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(MattressPressureSensingServiceImpl.class);

	/** The mattress pressure sensing dao. */
	private MattressPressureSensingDao mattressPressureSensingDao;

	/**
	 * Instantiates a new user service impl.
	 */
	public MattressPressureSensingServiceImpl() {
		mattressPressureSensingDao = new MattressPressureSensingDao();
	}

	/**
	 * Adds the mattress pressure sensing.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param mattressPressureSensing
	 *            the mattress pressure sensing
	 */
	public void addMattressPressureSensing(String sensing_environment_id,
			MattressPressureSensing mattressPressureSensing) {

		mattressPressureSensingDao.create(sensing_environment_id,
				mattressPressureSensing);
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

	public long getLastCollectionsTimestamp(String sensing_environment_id,
			String indoorPlaceName) {
		return mattressPressureSensingDao.getLastCollectionsTimestamp(sensing_environment_id, indoorPlaceName);
	}

}
