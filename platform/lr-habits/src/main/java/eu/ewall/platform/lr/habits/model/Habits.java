package eu.ewall.platform.lr.habits.model;


import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;


public class Habits extends AbstractDatabaseObject {
	
	@DatabaseField(value=DatabaseType.STRING)
	private String username;
	
	@DatabaseField(value=DatabaseType.STRING)
	private String habitType;
	
	@DatabaseField(value=DatabaseType.STRING)
	private String habitDayOfWeek;
	
	@DatabaseField(value=DatabaseType.STRING)
	private String habitTimeOfDay;
	

	
	public Habits() {
		
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getHabitType() {
		return habitType;
	}
	public void setHabitType(String habitType) {
		this.habitType = habitType;
	}
	
	public String getHabitDayOfWeek() {
		return habitDayOfWeek;
	}
	public void setHabitDayOfWeek(String habitDayOfWeek) {
		this.habitDayOfWeek = habitDayOfWeek;
	}
	
	public String getHabitTimeOfDay() {
		return habitTimeOfDay;
	}
	public void setHabitTimeOfDay(String habitTimeOfDay) {
		this.habitTimeOfDay = habitTimeOfDay;
	}
	
	
	
	
	
}
