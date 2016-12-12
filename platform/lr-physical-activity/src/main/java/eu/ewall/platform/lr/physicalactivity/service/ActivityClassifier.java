package eu.ewall.platform.lr.physicalactivity.service;

import java.util.List;

import eu.ewall.platform.idss.service.model.type.ActivityCategory;

/**
 * This class can classify activity levels.
 * 
 * @author Dennis Hofs (RRD)
 */
public interface ActivityClassifier {
	
	/**
	 * Returns whether the specified activity value is an outlier.
	 * 
	 * @param history the activity history, excluding the specified activity
	 * value
	 * @param value the activity value
	 * @return true if it is an outlier, false otherwise
	 */
	public boolean isOutlier(List<DayActivity> history, int value);
	
	/**
	 * Returns the category for the specified activity value.
	 * 
	 * @param history the activity history without outliers, excluding the
	 * specified activity value
	 * @param value the activity value
	 * @return the category
	 */
	public ActivityCategory getSingleDayCategory(List<DayActivity> history,
			int value);
	
	/**
	 * Returns the category for the specified weighted average.
	 * 
	 * @param history the activity history without outliers
	 * @param average the weighted average
	 * @return the category
	 */
	public ActivityCategory getAverageCategory(List<DayActivity> history,
			int average);
}
