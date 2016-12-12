package eu.ewall.platform.idss.service.model.common;

import java.util.List;

/**
 * A sleep pattern for a week day as calculated by the lifestyle reasoner for
 * sleep monitoring. This is part of a {@link LRSleepWeekPattern
 * LRSleepWeekPattern}.
 * 
 * @author Dennis Hofs (RRD)
 */
public class LRSleepDayPattern {
	private int weekDay;
	private List<LRSleepDayParameter> parameters;

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
	 * Returns the values for various sleep parameters for this week day.
	 * 
	 * @return the sleep parameters
	 */
	public List<LRSleepDayParameter> getParameters() {
		return parameters;
	}

	/**
	 * Sets the values for various sleep parameters for this week day.
	 * 
	 * @param parameters the sleep parameters
	 */
	public void setParameters(List<LRSleepDayParameter> parameters) {
		this.parameters = parameters;
	}

	/**
	 * Finds the parameter with the specified name. The name should be one of
	 * the PARAM_* constants defined in {@link LRSleepDayParameter
	 * LRSleepDayParameter}. If the parameter is not found, this method returns
	 * null.
	 * 
	 * @param name the parameter name
	 * @return the parameter or null
	 */
	public LRSleepDayParameter findParameter(String name) {
		for (LRSleepDayParameter parameter : parameters) {
			if (parameter.getParameter().equals(name))
				return parameter;
		}
		return null;
	}
}
