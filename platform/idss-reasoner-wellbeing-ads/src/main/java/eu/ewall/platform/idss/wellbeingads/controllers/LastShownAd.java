/*******************************************************************************
 * Copyright 2016 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.idss.wellbeingads.controllers;

import org.joda.time.DateTime;

import eu.ewall.platform.idss.wellbeingads.adtype.WellbeingAdCategory;

/**
 * The Class LastShownAd.
 */
public class LastShownAd {

	/**
	 * Instantiates a new last shown ad.
	 */
	public LastShownAd() {
	}

	/**
	 * Instantiates a new last shown ad.
	 *
	 * @param timeOfTheAdd
	 *            the time of the ad
	 * @param adCategory
	 *            the ad category
	 */
	public LastShownAd(DateTime timeOfTheAdd, WellbeingAdCategory adCategory) {
		this.timeOfTheAd = timeOfTheAdd;
		this.adCategory = adCategory;
	}

	/** The time of the add. */
	private DateTime timeOfTheAd;

	/** The ad category. */
	private WellbeingAdCategory adCategory;

	/**
	 * Gets the time of the ad.
	 *
	 * @return the time of the ad
	 */
	public DateTime getTimeOfTheAd() {
		return timeOfTheAd;
	}

	/**
	 * Sets the time of the ad.
	 *
	 * @param timeOfTheAd
	 *            the new time of the ad
	 */
	public void setTimeOfTheAd(DateTime timeOfTheAd) {
		this.timeOfTheAd = timeOfTheAd;
	}

	/**
	 * Gets the ad category.
	 *
	 * @return the ad category
	 */
	public WellbeingAdCategory getAdCategory() {
		return adCategory;
	}

	/**
	 * Sets the ad category.
	 *
	 * @param adCategory
	 *            the new ad category
	 */
	public void setAdCategory(WellbeingAdCategory adCategory) {
		this.adCategory = adCategory;
	}

}
