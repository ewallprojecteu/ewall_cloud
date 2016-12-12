/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.activity;

/**
 * The Class CallingActivity.
 *
 * @author eandgrg
 */
public abstract class CallingActivity extends Activity {

	/** The is incoming. */
	protected boolean isIncoming;

	/**
	 * Checks if is incoming.
	 *
	 * @return the isIncoming
	 */
	public boolean isIncoming() {
		return isIncoming;
	}

	/**
	 * Sets the incoming.
	 *
	 * @param isIncoming
	 *            the isIncoming to set
	 */
	public void setIncoming(boolean isIncoming) {
		this.isIncoming = isIncoming;
	}

}
