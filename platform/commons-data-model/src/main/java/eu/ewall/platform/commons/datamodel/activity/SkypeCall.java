/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.activity;

import java.util.ArrayList;
import java.util.List;

import eu.ewall.platform.commons.datamodel.measure.TimeInterval;

/**
 * The Class SkypeCall.
 *
 * @author eandgrg
 */
public class SkypeCall extends CallingActivity {

	/** The other parties list. */
	private List<String> otherPartiesList;

	/**
	 * The Constructor.
	 */
	public SkypeCall() {
		this.otherPartiesList = new ArrayList<String>();
	}

	/**
	 * The Constructor.
	 *
	 * @param timeInterval
	 *            the time interval
	 */
	public SkypeCall(TimeInterval timeInterval) {
		this();
		this.timeInterval = timeInterval;
	}

	/**
	 * Gets the other parties list.
	 *
	 * @return the otherPartiesList
	 */
	public List<String> getOtherPartiesList() {
		return otherPartiesList;
	}


}
