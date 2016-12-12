/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.profilingserver.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.ewall.platform.commons.datamodel.core.ieeesumo.Region;
import eu.ewall.platform.profilingserver.services.RegionServiceImpl;

/**
 * The Class RegionController.
 *
 * @author eandgrg
 */
@RestController
@RequestMapping(value = "/regions")
public class RegionController {

	/** The log. */
	private static Logger LOG = LoggerFactory.getLogger(RegionController.class);

	/** The region region. */
	@Autowired
	private RegionServiceImpl regionService;

	/**
	 * Instantiates a new region controller.
	 */
	public RegionController() {
	}

	/**
	 * Adds the new region.
	 *
	 * @param regionAsContent
	 *            the region as content
	 * @return the response entity
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<String> addNewRegion(
			@RequestBody Region regionAsContent) {

		LOG.debug("addingNewRegion");

		try {
			if (regionAsContent != null) {

				// if the region with this name already exist report conflict
				// 409
				if (regionService.addEWallRegion(regionAsContent) == false) {
					return new ResponseEntity<String>(HttpStatus.CONFLICT);
				}

			} else
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<String>(HttpStatus.CREATED);

	}

	/**
	 * Gets the e wall regions.
	 *
	 * @return the e wall regions
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<List<Region>> getAllRegions() {

		LOG.debug("Getting all ewall regions ");

		return new ResponseEntity<List<Region>>(
				regionService.getAllEWallRegions(), HttpStatus.OK);

	}

	/**
	 * Gets the e wall region by region name.
	 *
	 * @param regionName
	 *            the region name
	 * @return the e wall region by regionName
	 */
	@RequestMapping(value = "/{regionName}", method = RequestMethod.GET)
	public ResponseEntity<Region> getRegionByRegionname(
			@PathVariable("regionName") String regionName) {

		LOG.debug("Getting region with regionname: " + regionName);

		try {
			Region region = regionService
					.getEWallRegionByRegionName(regionName);

			if (region != null) {
				return new ResponseEntity<Region>(region, HttpStatus.OK);
			} else {
				return new ResponseEntity<Region>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<Region>(HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * Modify region by region name.
	 *
	 * @param regionAsContent
	 *            the region as content
	 * @param regionName
	 *            the region name
	 * @return the response entity
	 */
	@RequestMapping(value = "/{regionName}", method = RequestMethod.POST)
	public ResponseEntity<String> modifyRegionByRegionName(
			@RequestBody Region regionAsContent,
			@PathVariable("regionName") String regionName) {

		LOG.debug("Modify region with region name: " + regionName);

		try {

			if (regionAsContent != null && regionName != null) {
				regionService.modifyEWallRegionWithName(regionName,
						regionAsContent);
			} else
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<String>(HttpStatus.OK);

	}

	/**
	 * Delete region by region name.
	 *
	 * @param regionName
	 *            the region name
	 * @return the response entity
	 */
	@RequestMapping(value = "/{regionName}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteRegionByRegionName(
			@PathVariable("regionName") String regionName) {

		LOG.debug("Delete region with regionname: " + regionName);

		try {
			if (regionService.deleteRegionWithName(regionName))

				return new ResponseEntity<String>(HttpStatus.OK);
			else {
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

	}

}
