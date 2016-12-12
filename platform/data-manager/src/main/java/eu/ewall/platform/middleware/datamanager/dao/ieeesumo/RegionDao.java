/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.datamanager.dao.ieeesumo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import eu.ewall.platform.commons.datamodel.core.ieeesumo.Region;
import eu.ewall.platform.middleware.datamanager.dao.config.dbtype.EWallDBType;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;

/**
 * The Class RegionDao.
 */
public class RegionDao {

	/** The log. */
	private static final Logger LOG = LoggerFactory.getLogger(RegionDao.class);

	/** The Constant REGIONS_COLLECTION. */
	public static final String REGIONS_COLLECTION = "region";

	/** The mongo ops. */
	private MongoOperations mongoOps;

	/**
	 * Instantiates a new region dao.
	 */
	public RegionDao() {
		mongoOps = MongoDBFactory
				.getMongoOperationsForDBType(EWallDBType.SYSTEM);
	}

	/**
	 * Gets the all e wall regions.
	 *
	 * @return the all e wall regions
	 */
	public List<Region> getAllEWallRegions() {
		LOG.debug("Retrieveing all regions from mongodb.");
		return this.mongoOps.findAll(Region.class, REGIONS_COLLECTION);

	}

	/**
	 * Gets the e wall region by name.
	 *
	 * @param regionName
	 *            the region name
	 * @return the e wall region by name
	 */
	public Region getEWallRegionByName(String regionName) {
		if (regionName.isEmpty() == true)
			return null;

		LOG.debug("Retrieveing region with region name " + regionName
				+ " from mongodb.");

		Query query = new Query(Criteria.where("regionName").is(regionName));
		return this.mongoOps.findOne(query, Region.class, REGIONS_COLLECTION);
	}

	/**
	 * Adds the e wall region if one with the same name does not already exist.
	 *
	 * @param region
	 *            the region
	 * @return true, if successfully added. False it the regionName already
	 *         exists or of region passed is null
	 */
	public boolean addEWallRegion(Region region) {
		if (region == null)
			return false;

		Query query = new Query(Criteria.where("regionName").is(
				region.getRegionName()));
		if (mongoOps.count(query, REGIONS_COLLECTION) > 0) {
			return false;
		} else {

			mongoOps.insert(region, REGIONS_COLLECTION);
			LOG.debug("New region with region name " + region.getRegionName()
					+ " added to mongodb.");
			return true;
		}

	}

	/**
	 * Delete region with name.
	 *
	 * @param regionName
	 *            the region name
	 * @return true, if successful
	 */
	public boolean deleteRegionWithName(String regionName) {

		LOG.debug("Deleting region with region name " + regionName
				+ " from mongodb.");

		Query query = new Query(Criteria.where("regionName").is(regionName));
		this.mongoOps.remove(query, Region.class, REGIONS_COLLECTION);

		return true;

	}

	/**
	 * Modify e wall region with name.
	 *
	 * @param regionName
	 *            the region name
	 * @param newRegion
	 *            the new region
	 * @return true, if successful
	 */
	public boolean modifyEWallRegionWithName(String regionName, Region newRegion) {

		if (regionName == null || newRegion == null)
			return false;

		Query query = new Query(Criteria.where("regionName").is(regionName));
		Region regionFromDB = mongoOps.findOne(query, Region.class,
				REGIONS_COLLECTION);

		if (regionFromDB == null) {
			addEWallRegion(newRegion);
			return true;
		}

		// update all except regionName (which is unique)
		regionFromDB.setCountry(newRegion.getCountry());

		this.mongoOps.save(regionFromDB, REGIONS_COLLECTION);
		LOG.debug("Region with region name " + regionName
				+ " modified in mongodb.");

		return true;

	}
}
