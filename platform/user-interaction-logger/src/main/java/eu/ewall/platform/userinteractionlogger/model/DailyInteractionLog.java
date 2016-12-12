package eu.ewall.platform.userinteractionlogger.model;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;

/**
 * A {@link DailyInteractionLog} is a database object that contains summarized
 * logging information for a particular user and a particular date.
 * @author Harm op den Akker (RRD)
 */
public class DailyInteractionLog extends AbstractDatabaseObject  {
	
	@DatabaseField(value=DatabaseType.STRING)
	private String username;
	
	@DatabaseField(value=DatabaseType.INT)
	private int day;
	
	@DatabaseField(value=DatabaseType.INT)
	private int month;
	
	@DatabaseField(value=DatabaseType.INT)
	private int year;
	
	@DatabaseField(value=DatabaseType.INT)
	private int interactions = 0;
	
	@DatabaseField(value=DatabaseType.INT)
	private boolean complete = false;
	
	// ---------- Constructors ---------- //
	
	/**
	 * Creates an instance of an empty {@link DailyInteractionLog}.
	 */
	public DailyInteractionLog() {

	}
	
	public DailyInteractionLog(String username, int day, int month, int year) {
		this.username = username;
		this.day = day;
		this.month = month;
		this.year = year;
	}
	
	/**
	 * Creates an instance of a {@link DailyInteractionLog} with the given parameters.
	 * @param username the username of the user associated with this {@link DailyInteractionLog}.
	 * @param day the day-part of the date for this {@link DailyInteractionLog}.
	 * @param month the month-part of the date for this {@link DailyInteractionLog}.
	 * @param year the year-part of the date for this {@link DailyInteractionLog}.
	 * @param interactions the total number of interactions for the user on this date.
	 */
	public DailyInteractionLog(String username, int day, int month, int year, int interactions, boolean complete) {
		this.username = username;
		this.day = day;
		this.month = month;
		this.year = year;
		this.interactions = interactions;
		this.complete = complete;
	}
	
	// ---------- Getters ---------- //
	
	/**
	 * Returns the username of the user associated with this {@link DailyInteractionLog}.
	 * @return the username of the user associated with this {@link DailyInteractionLog}.
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Returns the day-part of the date for this {@link DailyInteractionLog}.
	 * @return the day-part of the date for this {@link DailyInteractionLog}.
	 */
	public int getDay() {
		return day;
	}
	
	/**
	 * Returns the month-part of the date for this {@link DailyInteractionLog}.
	 * @return the month-part of the date for this {@link DailyInteractionLog}.
	 */
	public int getMonth() {
		return month;
	}
	
	/**
	 * Returns the year-part of the date for this {@link DailyInteractionLog}.
	 * @return the year-part of the date for this {@link DailyInteractionLog}.
	 */
	public int getYear() {
		return year;
	}
	
	/**
	 * Returns the total number of interactions for the user on this date.
	 * @return the total number of interactions for the user on this date.
	 */
	public int getInteractions() {
		return interactions;
	}
	
	public boolean getComplete() {
		return complete;
	}
	
	// ---------- Setters ---------- //
	
	/**
	 * Sets the username of the user associated with this {@link DailyInteractionLog}.
	 * @param username the username of the user associated with this {@link DailyInteractionLog}.
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Sets the day-part of the date for this {@link DailyInteractionLog}.
	 * @param day the day-part of the date for this {@link DailyInteractionLog}.
	 */
	public void setDay(int day) {
		this.day = day;
	}
	
	/**
	 * Sets the month-part of the date for this {@link DailyInteractionLog}.
	 * @param month the month-part of the date for this {@link DailyInteractionLog}.
	 */
	public void setMonth(int month) {
		this.month = month;
	}
	
	/**
	 * Sets the year-part of the date for this {@link DailyInteractionLog}.
	 * @param year the year-part of the date for this {@link DailyInteractionLog}.
	 */
	public void setYear(int year) {
		this.year = year;
	}
	
	/**
	 * Sets the total number of interactions for the user on this date.
	 * @param interactions the total number of interactions for the user on this date.
	 */
	public void setInteractions(int interactions) {
		this.interactions = interactions;
	}
	
	public void setComplete(boolean complete) {
		this.complete = complete;
	}
}
