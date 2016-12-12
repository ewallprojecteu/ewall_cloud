package eu.ewall.platform.idss.service.model.state.user;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;

/**
 * This class defines a user's weekly routine as a set of daily routines, precisely
 * defined by the day of the week name (to avoid any confusion).
 * 
 * @author Harm op den Akker (Roessingh Research and Development)
 */
public class WeeklyRoutine extends AbstractDatabaseObject {
	
	@DatabaseField(value=DatabaseType.OBJECT)
	private DailyRoutine mondayRoutine;
	
	@DatabaseField(value=DatabaseType.OBJECT)
	private DailyRoutine tuesdayRoutine;
	
	@DatabaseField(value=DatabaseType.OBJECT)
	private DailyRoutine wednesdayRoutine;
	
	@DatabaseField(value=DatabaseType.OBJECT)
	private DailyRoutine thursdayRoutine;
	
	@DatabaseField(value=DatabaseType.OBJECT)
	private DailyRoutine fridayRoutine;
	
	@DatabaseField(value=DatabaseType.OBJECT)
	private DailyRoutine saturdayRoutine;
	
	@DatabaseField(value=DatabaseType.OBJECT)
	private DailyRoutine sundayRoutine;
	
	/**
	 * Returns the user's {@link DailyRoutine} for Mondays.
	 * @return the user's {@link DailyRoutine} for Mondays.
	 */
	public DailyRoutine getMondayRoutine() {
		return mondayRoutine;
	}
	
	/**
	 * Sets the user's {@link DailyRoutine} for Mondays.
	 * @param mondayRoutine the user's {@link DailyRoutine} for Mondays.
	 */
	public void setMondayRoutine(DailyRoutine mondayRoutine) {
		this.mondayRoutine = mondayRoutine;
	}
	
	/**
	 * Returns the user's {@link DailyRoutine} for Tuesdays.
	 * @return the user's {@link DailyRoutine} for Tuesdays.
	 */
	public DailyRoutine getTuesdayRoutine() {
		return tuesdayRoutine;
	}
	
	/**
	 * Sets the user's {@link DailyRoutine} for Tuesdays.
	 * @param tuesdayRoutine the user's {@link DailyRoutine} for Tuesdays.
	 */
	public void setTuesdayRoutine(DailyRoutine tuesdayRoutine) {
		this.tuesdayRoutine = tuesdayRoutine;
	}
	
	/**
	 * Returns the user's {@link DailyRoutine} for Wednesdays.
	 * @return the user's {@link DailyRoutine} for Wednesdays.
	 */
	public DailyRoutine getWednesdayRoutine() {
		return wednesdayRoutine;
	}
	
	/**
	 * Sets the user's {@link DailyRoutine} for Wednesdays.
	 * @param wednesdayRoutine the user's {@link DailyRoutine} for Wednesdays.
	 */
	public void setWednesdayRoutine(DailyRoutine wednesdayRoutine) {
		this.wednesdayRoutine = wednesdayRoutine;
	}
	
	/**
	 * Returns the user's {@link DailyRoutine} for Thursdays.
	 * @return the user's {@link DailyRoutine} for Thursdays.
	 */
	public DailyRoutine getThursdayRoutine() {
		return thursdayRoutine;
	}
	
	/**
	 * Sets the user's {@link DailyRoutine} for Thursdays.
	 * @param thursdayRoutine the user's {@link DailyRoutine} for Thursdays.
	 */
	public void setThursdayRoutine(DailyRoutine thursdayRoutine) {
		this.thursdayRoutine = thursdayRoutine;
	}
	
	/**
	 * Returns the user's {@link DailyRoutine} for Fridays.
	 * @return the user's {@link DailyRoutine} for Fridays.
	 */
	public DailyRoutine getFridayRoutine() {
		return fridayRoutine;
	}
	
	/**
	 * Sets the user's {@link DailyRoutine} for Fridays.
	 * @param fridayRoutine the user's {@link DailyRoutine} for Fridays.
	 */
	public void setFridayRoutine(DailyRoutine fridayRoutine) {
		this.fridayRoutine = fridayRoutine;
	}
	
	/**
	 * Returns the user's {@link DailyRoutine} for Saturdays.
	 * @return the user's {@link DailyRoutine} for Saturdays.
	 */
	public DailyRoutine getSaturdayRoutine() {
		return saturdayRoutine;
	}
	
	/**
	 * Sets the user's {@link DailyRoutine} for Saturdays.
	 * @param saturdayRoutine the user's {@link DailyRoutine} for Saturdays.
	 */
	public void setSaturdayRoutine(DailyRoutine saturdayRoutine) {
		this.saturdayRoutine = saturdayRoutine;
	}
	
	/**
	 * Returns the user's {@link DailyRoutine} for Sundays.
	 * @return the user's {@link DailyRoutine} for Sundays.
	 */
	public DailyRoutine getSundayRoutine() {
		return sundayRoutine;
	}
	
	/**
	 * Sets the user's {@link DailyRoutine} for Sundays.
	 * @param sundayRoutine the user's {@link DailyRoutine} for Sundays.
	 */
	public void setSundayRoutine(DailyRoutine sundayRoutine) {
		this.sundayRoutine = sundayRoutine;
	}
	
	@Override
	public String toString() {
		String result = "WeeklyRoutine: ";
		result+="[Monday: "+mondayRoutine.toString()+"]";
		result+=", ";
		result+="[Tuesday: "+tuesdayRoutine.toString()+"]";
		result+=", ";
		result+="[Wednesday: "+wednesdayRoutine.toString()+"]";
		result+=", ";
		result+="[Thursday: "+thursdayRoutine.toString()+"]";
		result+=", ";
		result+="[Friday: "+fridayRoutine.toString()+"]";
		result+=", ";
		result+="[Saturday: "+saturdayRoutine.toString()+"]";
		result+=", ";
		result+="[Sunday: "+sundayRoutine.toString()+"]";
		return result;
	}
	
}
