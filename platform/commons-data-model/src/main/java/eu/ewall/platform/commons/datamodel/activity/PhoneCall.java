/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.activity;

import eu.ewall.platform.commons.datamodel.measure.TimeInterval;

/**
 * The Class PhoneCall.
 *
 * @author eandgrg
 */
public class PhoneCall extends CallingActivity {

	/** The mobile. */
	protected int mobile;

	/** The telephone. */
	protected int telephone;

	/**
	 * The Constructor.
	 */
	public PhoneCall() {

	}

	/**
	 * The Constructor.
	 *
	 * @param mobile
	 *            the mobile
	 * @param telephone
	 *            the telephone
	 * @param timeInterval
	 *            the time interval
	 * @param isIncoming
	 *            the is incoming
	 */
	public PhoneCall(int mobile, int telephone, TimeInterval timeInterval,
			boolean isIncoming) {
		this.mobile = mobile;
		this.telephone = telephone;
		this.timeInterval = timeInterval;
		this.isIncoming = isIncoming;
	}

	/**
	 * Gets the mobile.
	 *
	 * @return the mobile
	 */
	public int getMobile() {
		return mobile;
	}

	/**
	 * Sets the mobile.
	 *
	 * @param mobile
	 *            the mobile to set
	 */
	public void setMobile(int mobile) {
		this.mobile = mobile;
	}

	/**
	 * Gets the telephone.
	 *
	 * @return the telephone
	 */
	public int getTelephone() {
		return telephone;
	}

	/**
	 * Sets the telephone.
	 *
	 * @param telephone
	 *            the telephone to set
	 */
	public void setTelephone(int telephone) {
		this.telephone = telephone;
	}


}
