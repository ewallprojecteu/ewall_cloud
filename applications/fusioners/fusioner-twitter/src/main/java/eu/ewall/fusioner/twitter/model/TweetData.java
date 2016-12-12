/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.fusioner.twitter.model;

import java.io.Serializable;
import java.util.Date;

/**
 * The Class TweetData. Contains useful metadata of posted "tweet".
 * 
 */
public class TweetData implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6055816243536699463L;

	/** The created at. */
	private Date createdAt;

	/** The text. */
	private String text;

	/** The is retweeted. */
	private boolean isRetweeted;

	/** The lang. */
	private String lang;

	/**
	 * Creator of tweet, separated into individual class
	 * 
	 * The tweetCreator.
	 */
	private TweetCreator tweetCreator;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Text: %s [created at: %s by %s]", text,
				createdAt, tweetCreator.getUserName());
	}

	/**
	 * Gets the created at.
	 *
	 * @return the created at
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * Sets the created at.
	 *
	 * @param createdAt
	 *            the new created at
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text.
	 *
	 * @param text
	 *            the new text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Checks if is retweeted.
	 *
	 * @return true, if is retweeted
	 */
	public boolean isRetweeted() {
		return isRetweeted;
	}

	/**
	 * Sets the retweeted.
	 *
	 * @param isRetweeted
	 *            the new retweeted
	 */
	public void setRetweeted(boolean isRetweeted) {
		this.isRetweeted = isRetweeted;
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
	 * Gets the tweet creator.
	 *
	 * @return the tweet creator
	 */
	public TweetCreator getTweetCreator() {
		return tweetCreator;
	}

	/**
	 * Sets the tweet creator.
	 *
	 * @param tweetCreator the new tweet creator
	 */
	public void setTweetCreator(TweetCreator tweetCreator) {
		this.tweetCreator = tweetCreator;
	}
}
