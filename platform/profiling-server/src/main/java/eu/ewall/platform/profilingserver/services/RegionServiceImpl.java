/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.profilingserver.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import eu.ewall.platform.commons.datamodel.core.ieeesumo.Region;
import eu.ewall.platform.middleware.datamanager.dao.ieeesumo.RegionDao;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;

/**
 * The Class RegionServiceImpl.
 * 
 * @author eandgrg
 */
@Service("regionService")
public class RegionServiceImpl implements DisposableBean {

	/** The log. */
	Logger log = LoggerFactory.getLogger(RegionServiceImpl.class);

	/** The region dao. */
	private RegionDao regionDao;

	/**
	 * Instantiates a new region service impl.
	 */
	public RegionServiceImpl() {
		regionDao = new RegionDao();
	}

	/**
	 * Adds the e wall region.
	 *
	 * @param region
	 *            the region
	 * @return true, if successful
	 */
	public boolean addEWallRegion(Region region) {
		log.debug("addEWallRegion from");
		return regionDao.addEWallRegion(region);

	}

	/**
	 * Gets the all e wall regions.
	 *
	 * @return the all e wall regions
	 */
	public List<Region> getAllEWallRegions() {

		log.debug("getAllEWallRegions");
		return regionDao.getAllEWallRegions();
	}

	/**
	 * Gets the e wall region by regionname.
	 *
	 * @param regionname
	 *            the regionname
	 * @return the e wall region by regionname
	 */
	public Region getEWallRegionByRegionName(String regionname) {
		return regionDao.getEWallRegionByName(regionname);
	}

	/**
	 * Modify e wall region with name.
	 *
	 * @param regionName
	 *            the region name
	 * @param region
	 *            the region
	 * @return true, if successful
	 */
	public boolean modifyEWallRegionWithName(String regionName, Region region) {
		return regionDao.modifyEWallRegionWithName(regionName, region);
	}

	/**
	 * Delete region with name.
	 *
	 * @param regionname
	 *            the regionname
	 * @return true, if successful
	 */
	public boolean deleteRegionWithName(String regionname) {
		return regionDao.deleteRegionWithName(regionname);
	}

	/**
	 * Gets the region dao.
	 *
	 * @return the region dao
	 */
	public RegionDao getRegionDao() {
		return regionDao;
	}

	/**
	 * Sets the region dao.
	 *
	 * @param regionDao
	 *            the new region dao
	 */
	public void setRegionDao(RegionDao regionDao) {
		this.regionDao = regionDao;
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
