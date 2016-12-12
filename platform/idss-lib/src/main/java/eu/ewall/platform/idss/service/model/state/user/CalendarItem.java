package eu.ewall.platform.idss.service.model.state.user;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;

import org.joda.time.DateTime;

/**
 * This class models an item in the calendar of a user.
 * 
 * @author Dennis Hofs (RRD)
 */
public class CalendarItem extends AbstractDatabaseObject {

	@DatabaseField(value= DatabaseType.ISOTIME)
	private DateTime startTime;

	@DatabaseField(value=DatabaseType.ISOTIME)
	private DateTime endTime;

	@DatabaseField(value=DatabaseType.INT)
	private boolean free;

	/**
	 * Returns the start time in the time zone of the user.
	 * 
	 * @return the start time
	 */
	public DateTime getStartTime() {
		return startTime;
	}

	/**
	 * Sets the start time in the time zone of the user.
	 * 
	 * @param startTime the start time
	 */
	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}

	/**
	 * Returns the end time in the time zone of the user.
	 * 
	 * @return the end time
	 */
	public DateTime getEndTime() {
		return endTime;
	}

	/**
	 * Sets the end time in the time zone of the user.
	 * 
	 * @param endTime the end time
	 */
	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
	}

	/**
	 * Returns whether the user is free to perform other activities.
	 * 
	 * @return true if the user is free to perform other activities
	 */
	public boolean isFree() {
		return free;
	}

	/**
	 * Sets whether the user is free to perform other activities.
	 * 
	 * @param free true if the user is free to perform other activities
	 */
	public void setFree(boolean free) {
		this.free = free;
	}
}
