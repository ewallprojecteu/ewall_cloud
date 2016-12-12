/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.profile;

import java.util.ArrayList;
import java.util.List;

import eu.ewall.platform.commons.datamodel.message.MessageType;

/**
 * EWallSubProfile containing eWall system specific information related to the
 * user.
 * 
 * @author eandgrg
 */
public class EWallSubProfile extends UserSubProfile {

	/** The (unique) region name. */
	private String regionName;

	/** The account is enabled. */
	private boolean accountIsEnabled;

	/** The real time monitoring allowed flag for checking if the real time monitoring is allowed by the User or not. */
	private boolean realTimeMonitoringAllowed;

	/** List of notification types selected by the user. */
	private List<MessageType> selectedNotificationTypesList;

	/**
	 * List of enumerated items, i.e. the names of all the objects a user can
	 * buy, and therefore own.
	 */
	private List<RewardType> rewardsList = new ArrayList<RewardType>();

	/**
	 * The Constructor.
	 */
	public EWallSubProfile() {
		selectedNotificationTypesList = new ArrayList<MessageType>();
		rewardsList=new ArrayList<RewardType>();
	}

	/**
	 * Instantiates a new e wall sub profile. Note that regions are added
	 * separately to the system and that here only the link between user and
	 * region is established.
	 *
	 * @param regionName
	 *            the (unique) region name
	 */
	public EWallSubProfile(String regionName) {
		this();
		this.regionName = regionName;
	}

	/**
	 * Instantiates a new e wall sub profile. Note that regions are added
	 * separately to the system and that here only the link between user and
	 * region is established.
	 *
	 * @param regionName
	 *            the (unique) region name
	 * @param accountIsEnabled
	 *            the account is enabled
	 */
	public EWallSubProfile(String regionName, boolean accountIsEnabled) {
		this();
		this.regionName = regionName;
		this.accountIsEnabled = accountIsEnabled;
	}

	/**
	 * Instantiates a new e wall sub profile.
	 *
	 * @param regionName
	 *            the region name
	 * @param accountIsEnabled
	 *            the account is enabled
	 * @param selectedNotificationTypesList
	 *            the selected notification types list
	 */
	public EWallSubProfile(String regionName, boolean accountIsEnabled,
			List<MessageType> selectedNotificationTypesList) {
		this();
		this.regionName = regionName;
		this.accountIsEnabled = accountIsEnabled;
		this.selectedNotificationTypesList = selectedNotificationTypesList;
	}

	/**
	 * Instantiates a new e wall sub profile.
	 *
	 * @param regionName
	 *            the region name
	 * @param accountIsEnabled
	 *            the account is enabled
	 * @param realTimeMonitoringAllowed
	 *            the real time monitoring allowed
	 * @param selectedNotificationTypesList
	 *            the selected notification types list
	 */
	public EWallSubProfile(String regionName, boolean accountIsEnabled, boolean realTimeMonitoringAllowed,
			List<MessageType> selectedNotificationTypesList) {
		super();
		this.regionName = regionName;
		this.accountIsEnabled = accountIsEnabled;
		this.realTimeMonitoringAllowed = realTimeMonitoringAllowed;
		this.selectedNotificationTypesList = selectedNotificationTypesList;
	}

	/**
	 * Gets the (unique) region name.
	 *
	 * @return the (unique)region name
	 */
	public String getRegionName() {
		return regionName;
	}

	/**
	 * Sets the region name for the user. Note that regions are added separately
	 * to the system and that here only the link between user and region is
	 * established.
	 *
	 * @param regionName
	 *            the new region name for the user
	 */
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	/**
	 * Checks if is account is enabled.
	 *
	 * @return true, if is account is enabled
	 */
	public boolean isAccountEnabled() {
		return accountIsEnabled;
	}

	/**
	 * Sets the account is enabled.
	 *
	 * @param accountIsEnabled
	 *            the new account is enabled
	 */
	public void setAccountEnabled(boolean accountIsEnabled) {
		this.accountIsEnabled = accountIsEnabled;
	}

	/**
	 * Gets the selected notification types list.
	 *
	 * @return the selected notification types list
	 */
	public List<MessageType> getSelectedNotificationTypesList() {
		return selectedNotificationTypesList;
	}

	/**
	 * Sets the selected notification types list.
	 *
	 * @param selectedNotificationTypesList
	 *            the new selected notification types list
	 */
	public void setSelectedNotificationTypesList(List<MessageType> selectedNotificationTypesList) {
		this.selectedNotificationTypesList = selectedNotificationTypesList;
	}

	/**
	 * Checks if the real time monitoring is allowed by the User or not.
	 *
	 * @return true, if is real time monitoring allowed
	 */
	public boolean isRealTimeMonitoringAllowed() {
		return realTimeMonitoringAllowed;
	}

	/**
	 * Sets the real time monitoring allowed flag.
	 *
	 * @param realTimeMonitoringAllowed
	 *            the new real time monitoring allowed
	 */
	public void setRealTimeMonitoringAllowed(boolean realTimeMonitoringAllowed) {
		this.realTimeMonitoringAllowed = realTimeMonitoringAllowed;
	}

	/**
	 * Gets the rewards list.
	 *
	 * @return the rewards list
	 */
	public List<RewardType> getRewardsList() {
		return rewardsList;
	}

	/**
	 * Sets the rewards list.
	 *
	 * @param rewardsList the new rewards list
	 */
	public void setRewardsList(List<RewardType> rewardsList) {
		this.rewardsList = rewardsList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		StringBuffer buffer = new StringBuffer();
		buffer.append("******EWallSubProfile start******\n");

		buffer.append("regionName: ");
		buffer.append(regionName);
		buffer.append("\n");

		buffer.append("accountIsEnabled: ");
		buffer.append(accountIsEnabled);
		buffer.append("\n");

		buffer.append("realTimeMonitoringAllowed: ");
		buffer.append(realTimeMonitoringAllowed);
		buffer.append("\n");

		buffer.append("selectedNotificationTypesList: ");
		if (selectedNotificationTypesList != null)
			for (MessageType mtype : selectedNotificationTypesList)
				buffer.append(mtype.toString() + ", ");
		else
			buffer.append("empty");
		buffer.append("\n");
		
		buffer.append("rewardsList: ");
		if (rewardsList != null)
			for (RewardType rtype : rewardsList)
				buffer.append(rtype.toString() + ", ");
		else
			buffer.append("empty");
		buffer.append("\n");

		buffer.append("******EWallSubProfile end******\n");
		return buffer.toString();
	}

}