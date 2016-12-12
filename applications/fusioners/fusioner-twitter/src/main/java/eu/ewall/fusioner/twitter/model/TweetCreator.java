/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.fusioner.twitter.model;

import java.io.Serializable;

/**
 * The Class TweetCreator. 
 * 
 */
public class TweetCreator implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5496279281337893271L;

	/** The id. */
	private long id;

	/** The user name. */
	private String userName;

	/** The user screen name. */
	private String userScreenName;

	/** The profile image url. */
	private String profileImageUrl;

	/** The is default profile image. */
	private boolean isDefaultProfileImage;

	/** The time zone. */
	private String timeZone;

	/** The lang. */
	private String lang;

	/** The is geo enabled. */
	private boolean isGeoEnabled;

	/** The is follow request sent. */
	private boolean isFollowRequestSent;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the user name.
	 *
	 * @param userName
	 *            the new user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Gets the user screen name.
	 *
	 * @return the user screen name
	 */
	public String getUserScreenName() {
		return userScreenName;
	}

	/**
	 * Sets the user screen name.
	 *
	 * @param userScreenName
	 *            the new user screen name
	 */
	public void setUserScreenName(String userScreenName) {
		this.userScreenName = userScreenName;
	}

	/**
	 * Gets the profile image url.
	 *
	 * @return the profile image url
	 */
	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	/**
	 * Sets the profile image url.
	 *
	 * @param profileImageUrl
	 *            the new profile image url
	 */
	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	/**
	 * Checks if is default profile image.
	 *
	 * @return true, if is default profile image
	 */
	public boolean isDefaultProfileImage() {
		return isDefaultProfileImage;
	}

	/**
	 * Sets the default profile image.
	 *
	 * @param isDefaultProfileImage
	 *            the new default profile image
	 */
	public void setDefaultProfileImage(boolean isDefaultProfileImage) {
		this.isDefaultProfileImage = isDefaultProfileImage;
	}

	/**
	 * Gets the time zone.
	 *
	 * @return the time zone
	 */
	public String getTimeZone() {
		return timeZone;
	}

	/**
	 * Sets the time zone.
	 *
	 * @param timeZone
	 *            the new time zone
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	/**
	 * Gets the lang.
	 *
	 * @return the lang
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * Sets the lang.
	 *
	 * @param lang
	 *            the new lang
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}

	/**
	 * Checks if is geo enabled.
	 *
	 * @return true, if is geo enabled
	 */
	public boolean isGeoEnabled() {
		return isGeoEnabled;
	}

	/**
	 * Sets the geo enabled.
	 *
	 * @param isGeoEnabled
	 *            the new geo enabled
	 */
	public void setGeoEnabled(boolean isGeoEnabled) {
		this.isGeoEnabled = isGeoEnabled;
	}

	/**
	 * Checks if is follow request sent.
	 *
	 * @return true, if is follow request sent
	 */
	public boolean isFollowRequestSent() {
		return isFollowRequestSent;
	}

	/**
	 * Sets the follow request sent.
	 *
	 * @param isFollowRequestSent
	 *            the new follow request sent
	 */
	public void setFollowRequestSent(boolean isFollowRequestSent) {
		this.isFollowRequestSent = isFollowRequestSent;
	}
}
