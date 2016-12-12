package eu.ewall.platform.idss.service.model.state.user;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;

/**
 * Models a daily routine of a user.
 * 
 * @author Harm op den Akker (Roessingh Research and Development)
 */
public class DailyRoutine extends AbstractDatabaseObject {
	
	@DatabaseField(value=DatabaseType.INT)
	private Integer wakeUpTime = null;
	
	/**
	 * Returns the time the user usually wakes up in minutes since midnight.
	 * @return the time the user usually wakes up in minutes since midnight.
	 */
	public Integer getWakeUpTime() {
		return wakeUpTime;
	}
	
	/**
	 * Sets the time the user usually wakes up in minutes since midnight.
	 * @param wakeUpTime the time the user usually wakes up in minutes since midnight.
	 */
	public void setWakeUpTime(Integer wakeUpTime) {
		this.wakeUpTime = wakeUpTime;
	}
	
	@Override
	public String toString() {
		return "wakeUpTime:"+wakeUpTime.intValue();
	}
}
