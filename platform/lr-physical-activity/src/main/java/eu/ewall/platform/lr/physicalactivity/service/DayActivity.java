package eu.ewall.platform.lr.physicalactivity.service;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.service.model.type.ActivityCategory;

import org.joda.time.LocalDate;

/**
 * The activity of a user on one date. It always represents a completed day.
 * 
 * @author Dennis Hofs (RRD)
 */
public class DayActivity extends AbstractDatabaseObject {
	@DatabaseField(value=DatabaseType.STRING)
	private String user;
	
	@DatabaseField(value=DatabaseType.DATE)
	private LocalDate date;
	
	@DatabaseField(value=DatabaseType.INT)
	private int value;
	
	@DatabaseField(value=DatabaseType.STRING)
	private ActivityCategory category;
	
	@DatabaseField(value=DatabaseType.INT)
	private boolean outlier;

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
	 * Returns the activity value on this date.
	 * 
	 * @return the activity value on this date
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Sets the activity value on this date.
	 * 
	 * @param value the activity value on this date
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * Returns the category as obtained with {@link
	 * ActivityClassifier#getSingleDayCategory(java.util.List, int)
	 * ActivityClassifier.getSingleDayCategory()}.
	 * 
	 * @return the category
	 */
	public ActivityCategory getCategory() {
		return category;
	}

	/**
	 * Sets the category as obtained with {@link
	 * ActivityClassifier#getSingleDayCategory(java.util.List, int)
	 * ActivityClassifier.getSingleDayCategory()}.
	 * 
	 * @param category the category
	 */
	public void setCategory(ActivityCategory category) {
		this.category = category;
	}

	/**
	 * Returns whether this day was an outlier, as determined by {@link
	 * ActivityClassifier#isOutlier(java.util.List, int)
	 * ActivityClassifier.isOutlier()}.
	 * 
	 * @return true if this day was an outlier, false otherwise
	 */
	public boolean isOutlier() {
		return outlier;
	}

	/**
	 * Sets whether this day was an outlier, as determined by {@link
	 * ActivityClassifier#isOutlier(java.util.List, int)
	 * ActivityClassifier.isOutlier()}.
	 * 
	 * @param outlier true if this day was an outlier, false otherwise
	 */
	public void setOutlier(boolean outlier) {
		this.outlier = outlier;
	}
}
