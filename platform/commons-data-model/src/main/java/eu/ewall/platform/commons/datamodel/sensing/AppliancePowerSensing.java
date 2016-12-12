/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/


package eu.ewall.platform.commons.datamodel.sensing;



/**
 * The Class VisualSensing.
 * 
 * @author EMIRMOS
 */
public class AppliancePowerSensing extends Sensing {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String applianceName;
	
	protected ApplianceType applianceType = ApplianceType.UNKNOWN;
	
	protected String indoorPlaceName;
	
	protected boolean socketOn;
	
	protected double powerNow;
	
	protected double dailyEnergy;

	/**
	 * @return the applianceName
	 */
	public String getApplianceName() {
		return applianceName;
	}

	/**
	 * @param applianceName the applianceName to set
	 */
	public void setApplianceName(String applianceName) {
		this.applianceName = applianceName;
	}

	/**
	 * @return the applianceType
	 */
	public ApplianceType getApplianceType() {
		return applianceType;
	}

	/**
	 * @param applianceType the applianceType to set
	 */
	public void setApplianceType(ApplianceType applianceType) {
		this.applianceType = applianceType;
	}

	/**
	 * @return the indoorPlaceName
	 */
	public String getIndoorPlaceName() {
		return indoorPlaceName;
	}

	/**
	 * @param indoorPlaceName the indoorPlaceName to set
	 */
	public void setIndoorPlaceName(String indoorPlaceName) {
		this.indoorPlaceName = indoorPlaceName;
	}

	/**
	 * @return the socketOn
	 */
	public boolean isSocketOn() {
		return socketOn;
	}

	/**
	 * @param socketOn the socketOn to set
	 */
	public void setSocketOn(boolean socketOn) {
		this.socketOn = socketOn;
	}

	/**
	 * @return the powerNow
	 */
	public double getPowerNow() {
		return powerNow;
	}

	/**
	 * @param powerNow the powerNow to set
	 */
	public void setPowerNow(double powerNow) {
		this.powerNow = powerNow;
	}

	/**
	 * @return the dailyEnergy
	 */
	public double getDailyEnergy() {
		return dailyEnergy;
	}

	/**
	 * @param dailyEnergy the dailyEnergy to set
	 */
	public void setDailyEnergy(double dailyEnergy) {
		this.dailyEnergy = dailyEnergy;
	}
	
	
}
