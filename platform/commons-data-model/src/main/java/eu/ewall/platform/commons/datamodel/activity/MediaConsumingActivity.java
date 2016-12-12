/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.activity;

import eu.ewall.platform.commons.datamodel.measure.TimeInterval;

/**
 * The Class MediaConsumingActivity.
 *
 * @author eandgrg
 */
public class MediaConsumingActivity extends Activity {

	/** The media deviceId. */
	protected int mediaID;

	/** The media description. */
	protected String mediaDescription;

	/**
	 * The Constructor.
	 */
	public MediaConsumingActivity() {

	}

	/**
	 * The Constructor.
	 *
	 * @param timeInterval
	 *            the time interval
	 * @param mediaID
	 *            the media deviceId
	 * @param mediaDescription
	 *            the media description
	 */
	public MediaConsumingActivity(TimeInterval timeInterval, 
			int mediaID, String mediaDescription) {
		this.timeInterval = timeInterval;
		this.mediaID = mediaID;
		this.mediaDescription = mediaDescription;
	}

	/**
	 * Gets the media deviceId.
	 *
	 * @return the mediaID
	 */
	public int getMediaID() {
		return mediaID;
	}

	/**
	 * Sets the media deviceId.
	 *
	 * @param mediaID
	 *            the mediaID to set
	 */
	public void setMediaID(int mediaID) {
		this.mediaID = mediaID;
	}

	/**
	 * Gets the media description.
	 *
	 * @return the mediaDescription
	 */
	public String getMediaDescription() {
		return mediaDescription;
	}

	/**
	 * Sets the media description.
	 *
	 * @param mediaDescription
	 *            the mediaDescription to set
	 */
	public void setMediaDescription(String mediaDescription) {
		this.mediaDescription = mediaDescription;
	}

}
