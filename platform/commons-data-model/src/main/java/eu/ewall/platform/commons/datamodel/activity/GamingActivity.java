/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.activity;

import eu.ewall.platform.commons.datamodel.measure.TimeInterval;

/**
 * The Class GamingActivity.
 *
 * @author eandgrg
 */
public class GamingActivity extends Activity {

	/** The game deviceId. */
	protected double gameID;

	/** The game name. */
	protected String gameName;

	/** The game score. */
	protected double gameScore;

	/**
	 * The Constructor.
	 */
	public GamingActivity() {

	}

	/**
	 * The Constructor.
	 *
	 * @param timeInterval
	 *            the time interval
	 * @param gameID
	 *            the game deviceId
	 * @param gameName
	 *            the game name
	 * @param gameScore
	 *            the game score
	 */
	public GamingActivity(TimeInterval timeInterval, 
			double gameID, String gameName, double gameScore) {
		this.timeInterval = timeInterval;
		this.gameID = gameID;
		this.gameName = gameName;
		this.gameScore = gameScore;
	}

	/**
	 * Gets the game deviceId.
	 *
	 * @return the gameID
	 */
	public double getGameID() {
		return gameID;
	}

	/**
	 * Sets the game deviceId.
	 *
	 * @param gameID
	 *            the gameID to set
	 */
	public void setGameID(double gameID) {
		this.gameID = gameID;
	}

	/**
	 * Gets the game name.
	 *
	 * @return the gameName
	 */
	public String getGameName() {
		return gameName;
	}

	/**
	 * Sets the game name.
	 *
	 * @param gameName
	 *            the gameName to set
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	/**
	 * Gets the game score.
	 *
	 * @return the gameScore
	 */
	public double getGameScore() {
		return gameScore;
	}

	/**
	 * Sets the game score.
	 *
	 * @param gameScore
	 *            the gameScore to set
	 */
	public void setGameScore(double gameScore) {
		this.gameScore = gameScore;
	}

}
