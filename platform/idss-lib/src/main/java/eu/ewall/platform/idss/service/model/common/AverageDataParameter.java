package eu.ewall.platform.idss.service.model.common;

import org.joda.time.LocalDate;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.service.model.type.ParameterClassification;

/**
 * This class models the weighted average value of a data parameter on a
 * particular week day. A set of parameters for each day of the week constitute
 * a week pattern. The property "updated" indicates until when source data has
 * been processed to calculate the week pattern. A new week pattern is
 * calculated every time the source data of a complete day becomes available.
 * Older week patterns are preserved in the database. Using the property
 * "updated" it's possible to see how week patterns evolved over time.
 * 
 * @author Dennis Hofs (RRD)
 */
public class AverageDataParameter extends AbstractDatabaseObject {
	@DatabaseField(value=DatabaseType.STRING)
	private String user;
	
	@DatabaseField(value=DatabaseType.INT)
	private int weekDay;
	
	@DatabaseField(value=DatabaseType.STRING)
	private String parameter;
	
	@DatabaseField(value=DatabaseType.FLOAT)
	private float weightedAverage;
	
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
	 * Returns the week day (1 is Monday, 7 is Sunday).
	 * 
	 * @return the week day (1 is Monday, 7 is Sunday)
	 */
	public int getWeekDay() {
		return weekDay;
	}

	/**
	 * Sets the week day (1 is Monday, 7 is Sunday).
	 * 
	 * @param weekDay the week day (1 is Monday, 7 is Sunday)
	 */
	public void setWeekDay(int weekDay) {
		this.weekDay = weekDay;
	}

	/**
	 * Returns the name of the data parameter.
	 * 
	 * @return the name of the data parameter
	 */
	public String getParameter() {
		return parameter;
	}

	/**
	 * Sets the name of the data parameter.
	 * 
	 * @param parameter the name of the data parameter
	 */
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	/**
	 * Returns the weighted average value.
	 * 
	 * @return the weighted average value
	 */
	public float getWeightedAverage() {
		return weightedAverage;
	}

	/**
	 * Sets the weighted average value.
	 * 
	 * @param weightedAverage the weighted average value
	 */
	public void setWeightedAverage(float weightedAverage) {
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
	 * Returns the date until which source data has been processed to calculate
	 * the weighted average. The date is inclusive. The average was calculated
	 * only when the complete source data for the returned date was available.
	 * 
	 * @return the date until which data has been processed
	 */
	public LocalDate getUpdated() {
		return updated;
	}

	/**
	 * Sets the date until which source data has been processed to calculate
	 * the weighted average. The date is inclusive. The average should be
	 * calculated only when the complete source data for the specified date is
	 * available.
	 * 
	 * @param updated the date until which data has been processed
	 */
	public void setUpdated(LocalDate updated) {
		this.updated = updated;
	}
}
