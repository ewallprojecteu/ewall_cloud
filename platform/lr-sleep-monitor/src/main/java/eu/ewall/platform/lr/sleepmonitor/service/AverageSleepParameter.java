package eu.ewall.platform.lr.sleepmonitor.service;

import org.joda.time.LocalDate;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.service.model.type.ParameterClassification;

/**
 * This class models the weighted average value of a sleep parameter on a
 * particular week day.
 * 
 * @author Dennis Hofs (RRD)
 */
public class AverageSleepParameter extends AbstractDatabaseObject {
	@DatabaseField(value=DatabaseType.STRING)
	private String user;
	
	@DatabaseField(value=DatabaseType.INT)
	private int weekDay;
	
	@DatabaseField(value=DatabaseType.STRING)
	private String parameter;
	
	@DatabaseField(value=DatabaseType.INT)
	private int weightedAverage;
	
	@DatabaseField(value=DatabaseType.STRING)
	private ParameterClassification category;
	
	@DatabaseField(value=DatabaseType.DATE)
	private LocalDate updated;

	/**
	 * Returns the user name.
	 * 
	 * @return the user name
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the user name.
	 * 
	 * @param user the user name
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Returns the week day (1 is Monday, 7 is Sunday). The average is for the
	 * night that precedes this day.
	 * 
	 * @return the week day (1 is Monday, 7 is Sunday)
	 */
	public int getWeekDay() {
		return weekDay;
	}

	/**
	 * Sets the week day (1 is Monday, 7 is Sunday). The average is for the
	 * night that precedes this day.
	 * 
	 * @param weekDay the week day (1 is Monday, 7 is Sunday)
	 */
	public void setWeekDay(int weekDay) {
		this.weekDay = weekDay;
	}

	/**
	 * Returns the name of the sleep parameter. This should be one of the
	 * PARAM_* constants defined in {@link SleepParameter SleepParameter}.
	 * 
	 * @return the name of the sleep parameter
	 */
	public String getParameter() {
		return parameter;
	}

	/**
	 * Sets the name of the sleep parameter. This should be one of the PARAM_*
	 * constants defined in {@link SleepParameter SleepParameter}.
	 * 
	 * @param parameter the name of the sleep parameter
	 */
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	/**
	 * Returns the weighted average value.
	 * 
	 * @return the weighted average value
	 */
	public int getWeightedAverage() {
		return weightedAverage;
	}

	/**
	 * Sets the weighted average value.
	 * 
	 * @param weightedAverage the weighted average value
	 */
	public void setWeightedAverage(int weightedAverage) {
		this.weightedAverage = weightedAverage;
	}

	/**
	 * Returns the classification category.
	 * 
	 * @return the classification category
	 */
	public ParameterClassification getCategory() {
		return category;
	}

	/**
	 * Sets the classification category.
	 * 
	 * @param category the classification category
	 */
	public void setCategory(ParameterClassification category) {
		this.category = category;
	}

	/**
	 * Returns the date following the night until which sleep data has been
	 * processed to calculate the weighted average.
	 * 
	 * @return the date until which sleep data has been processed
	 */
	public LocalDate getUpdated() {
		return updated;
	}

	/**
	 * Sets the date following the night until which sleep data has been
	 * processed to calculate the weighted average.
	 * 
	 * @param updated the date until which sleep data has been processed
	 */
	public void setUpdated(LocalDate updated) {
		this.updated = updated;
	}
}
