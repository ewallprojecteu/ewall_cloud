package eu.ewall.platform.idss.service.model.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.service.model.type.ActivityCategory;
import eu.ewall.platform.idss.utils.json.SqlDateDeserializer;
import eu.ewall.platform.idss.utils.json.SqlDateSerializer;

import org.joda.time.LocalDate;

/**
 * This class models the weighted average activity on a particular week day.
 * 
 * @author Dennis Hofs (RRD)
 */
public class AverageActivity extends AbstractDatabaseObject {
	@JsonIgnore
	private String id;
	
	@DatabaseField(value=DatabaseType.STRING)
	private String user;

	@DatabaseField(value=DatabaseType.INT)
	private int weekDay;

	@DatabaseField(value=DatabaseType.INT)
	private int weightedAverage;

	@DatabaseField(value=DatabaseType.STRING)
	private ActivityCategory category;
	
	@DatabaseField(value=DatabaseType.DATE)
	@JsonSerialize(using=SqlDateSerializer.class)
	@JsonDeserialize(using=SqlDateDeserializer.class)
	private LocalDate updated;
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

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
	 * Returns the weighted average activity, calculated over all daily step
	 * counts for this week day, excluding outliers.
	 * 
	 * @return the weighted average
	 */
	public int getWeightedAverage() {
		return weightedAverage;
	}

	/**
	 * Sets the weighted average activity, calculated over all daily step
	 * counts for this week day, excluding outliers.
	 * 
	 * @param weightedAverage the weighted average
	 */
	public void setWeightedAverage(int weightedAverage) {
		this.weightedAverage = weightedAverage;
	}

	/**
	 * Returns the category as obtained with
	 * ActivityClassifier.getAverageCategory().
	 * 
	 * @return the category
	 */
	public ActivityCategory getCategory() {
		return category;
	}

	/**
	 * Sets the category as obtained with
	 * ActivityClassifier.getAverageCategory().
	 * 
	 * @param category the category
	 */
	public void setCategory(ActivityCategory category) {
		this.category = category;
	}

	/**
	 * Returns the date until which activity data has been processed to
	 * calculate this weighted average. It includes the activity of this date.
	 * 
	 * @return the date until which activity data has been processed
	 */
	public LocalDate getUpdated() {
		return updated;
	}

	/**
	 * Sets the date until which activity data has been processed to calculate
	 * this weighted average. It includes the activity of this date.
	 * 
	 * @param updated the date until which activity data has been processed
	 */
	public void setUpdated(LocalDate updated) {
		this.updated = updated;
	}
}
