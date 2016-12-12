package eu.ewall.platform.idss.service.lr;

import eu.ewall.platform.idss.service.model.common.AverageDataParameter;

/**
 * Parameter for which a {@link WeekDayLifestyleReasoner
 * WeekDayLifestyleReasoner} can calculate a weighted average for each week
 * day as part of a week pattern. Using this class the lifestyle reasoner
 * creates instances of {@link AverageDataParameter AverageDataParameter} that
 * are stored in the database.
 * 
 * @author Dennis Hofs (RRD)
 */
public interface WeekPatternParameter<T extends LRSourceDayData> {
	
	/**
	 * Returns the parameter name. This will be stored in {@link
	 * AverageDataParameter AverageDataParameter}.
	 * 
	 * @return the parameter name
	 */
	String getName();
	
	/**
	 * Returns the value for this parameter from the specified day data.
	 * 
	 * @param data the day data
	 * @return the value for this parameter
	 */
	float calculate(T data);
}
