package eu.ewall.platform.commons.datamodel.resource;

/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

import java.util.List;

/**
 * The Class YouTubeVideo.
 */
public class YouTubeVideo extends VideoMediaResource {

	/** The videoId. */
	private String videoId;

	/** The url. */
	private String url;

	/** The title. */
	private String title;

	/** The privacy status. */
	private PrivacyStatus privacyStatus;

	/** The tag id's list. */
	private List<String> tagIDsList;

	/**
	 * Instantiates a new you tube video.
	 */
	public YouTubeVideo() {
	}

	/**
	 * Instantiates a new you tube video.
	 *
	 * @param videoId
	 *            the video id
	 * @param url
	 *            the url
	 * @param title
	 *            the title
	 * @param privacyStatus
	 *            the privacy status
	 * @param tagIDsList
	 *            the tag i ds list
	 */
	public YouTubeVideo(String videoId, String url, String title,
			PrivacyStatus privacyStatus, List<String> tagIDsList) {
		super();
		this.videoId = videoId;
		this.url = url;
		this.title = title;
		this.privacyStatus = privacyStatus;
		this.tagIDsList = tagIDsList;
	}

	/**
	 * Gets the video id.
	 *
	 * @return the video id
	 */
	public String getVideoId() {
		return videoId;
	}

	/**
	 * Sets the video id.
	 *
	 * @param videoId
	 *            the new video id
	 */
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the url.
	 *
	 * @param url
	 *            the new url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title
	 *            the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the privacy status.
	 *
	 * @return the privacy status
	 */
	public PrivacyStatus getPrivacyStatus() {
		return privacyStatus;
	}

	/**
	 * Sets the privacy status.
	 *
	 * @param privacyStatus
	 *            the new privacy status
	 */
	public void setPrivacyStatus(PrivacyStatus privacyStatus) {
		this.privacyStatus = privacyStatus;
	}

	/**
	 * Gets the tag i ds list.
	 *
	 * @return the tag i ds list
	 */
	public List<String> getTagIDsList() {
		return tagIDsList;
	}

	/**
	 * Sets the tag i ds list.
	 *
	 * @param tagIDsList
	 *            the new tag i ds list
	 */
	public void setTagIDsList(List<String> tagIDsList) {
		this.tagIDsList = tagIDsList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("name: ");
		buffer.append(name);
		buffer.append("\n");
		buffer.append("description: ");
		buffer.append(description);
		buffer.append("\n");
		buffer.append("videoId: ");
		buffer.append(videoId);
		buffer.append("\n");
		buffer.append("url: ");
		buffer.append(url);
		buffer.append("\n");
		buffer.append("title: ");
		buffer.append(title);
		buffer.append("\n");
		buffer.append("privacyStatus: ");
		buffer.append(privacyStatus);
		buffer.append("\n");
		buffer.append("tagIDsList: ");
		buffer.append(tagIDsList.toString());
		buffer.append("\n");

		return buffer.toString();
	}

}