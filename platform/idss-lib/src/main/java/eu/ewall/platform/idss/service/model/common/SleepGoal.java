package eu.ewall.platform.idss.service.model.common;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.service.model.type.ParameterClassification;
import eu.ewall.platform.idss.utils.json.SqlDateDeserializer;
import eu.ewall.platform.idss.utils.json.SqlDateSerializer;

/**
 * A sleep goal for a user at a specified date. There are different goals
 * for different {@link SleepGoalMeasure SleepGoalMeasure}s. The goal defines a
 * goal value and a category (below average, normal, above average).
 * 
 * @author Dennis Hofs (RRD)
 */
public class SleepGoal extends AbstractDatabaseObject {
	@JsonIgnore
	private String id;
	
	@DatabaseField(value= DatabaseType.STRING)
	private String user;

	@DatabaseField(value=DatabaseType.DATE)
	@JsonSerialize(using=SqlDateSerializer.class)
	@JsonDeserialize(using=SqlDateDeserializer.class)
	private LocalDate date;

	@DatabaseField(value=DatabaseType.STRING)
	private ParameterClassification category;

	@DatabaseField(value=DatabaseType.INT)
	private int goal;
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the username.
	 * 
	 * @return the username
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the username.
	 * 
	 * @param user the username
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Returns the date.
	 * 
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 * 
	 * @param date the date
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}

	/**
	 * Returns the category (below average, normal, above average).
	 * 
	 * @return the category
	 */
	public ParameterClassification getCategory() {
		return category;
	}

	/**
	 * Sets the category (below average, normal, above average).
	 * 
	 * @param category the category
	 */
	public void setCategory(ParameterClassification category) {
		this.category = category;
	}

	/**
	 * Returns the goal value.
	 * 
	 * @return the goal value
	 */
	public int getGoal() {
		return goal;
	}

	/**
	 * Sets the goal value.
	 * 
	 * @param goal the goal value
	 */
	public void setGoal(int goal) {
		this.goal = goal;
	}
}
