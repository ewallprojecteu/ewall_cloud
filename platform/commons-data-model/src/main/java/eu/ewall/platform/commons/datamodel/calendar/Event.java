package eu.ewall.platform.commons.datamodel.calendar;

/**
 * The Class Event.
 */
public class Event {

	/** The id. */
	public String id;
	
	public String groupID;
	
	public String title;

	public long startDate;
	
	public long endDate;

	/** The username. */
	public String username;

	/** The repeat times. */
	public Integer repeatTimes;

	/** The repeat interval. */
	public RepeatInterval repeatInterval;

	/** The reminder time. */
	public Integer reminderTime;

	
	public String getGroupID() {
		return groupID;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	

	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	/**
	 * Gets the repeat times.
	 *
	 * @return the repeat times
	 */
	public Integer getRepeatTimes() {
		return repeatTimes;
	}

	/**
	 * Sets the repeat times.
	 *
	 * @param repeatTimes
	 *            the new repeat times
	 */
	public void setRepeatTimes(Integer repeatTimes) {
		this.repeatTimes = repeatTimes;
	}
	
	

	public RepeatInterval getRepeatInterval() {
		return repeatInterval;
	}

	public void setRepeatInterval(RepeatInterval repeatInterval) {
		this.repeatInterval = repeatInterval;
	}

	/**
	 * Gets the reminder time.
	 *
	 * @return the reminder time
	 */
	public Integer getReminderTime() {
		return reminderTime;
	}

	/**
	 * Sets the reminder time.
	 *
	 * @param reminderTime
	 *            the new reminder time
	 */
	public void setReminderTime(Integer reminderTime) {
		this.reminderTime = reminderTime;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 *
	 * @param username
	 *            the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

}
