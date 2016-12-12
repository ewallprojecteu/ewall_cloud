package eu.ewall.platform.idss.service.model.common;

import eu.ewall.platform.idss.service.model.type.ParameterClassification;

/**
 * The weighted average value and classification of a sleep parameter as
 * calculated by the lifestyle reasoner for sleep monitoring. An {@link
 * LRSleepDayPattern LRSleepDayPattern} contains instances of this class for
 * various sleep parameters.
 * 
 * @author Dennis Hofs (RRD)
 */
public class LRSleepDayParameter {
	public static final String PARAM_BED_ON_TIME = "bedOnTime";
	public static final String PARAM_BED_OFF_TIME = "bedOffTime";
	public static final String PARAM_TOTAL_SLEEP_TIME = "totalSleepTime";
	public static final String PARAM_FREQUENCY_WAKING_UP = "frequencyWakingUp";
	public static final String PARAM_SLEEP_EFFICIENCY = "sleepEfficiency";

	private String parameter;
	private int weightedAverage;
	private ParameterClassification category;

	/**
	 * Returns the parameter name. This should be one of the PARAM_* constants
	 * defined in this class.
	 * 
	 * @return the parameter name
	 */
	public String getParameter() {
		return parameter;
	}

	/**
	 * Sets the parameter name. This should be one of the PARAM_* constants
	 * defined in this class.
	 * 
	 * @param parameter the parameter name
	 */
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	/**
	 * Returns the weighted average value for this parameter.
	 * 
	 * @return the weighted average value for this parameter
	 */
	public int getWeightedAverage() {
		return weightedAverage;
	}

	/**
	 * Sets the weighted average value for this parameter.
	 * 
	 * @param weightedAverage the weighted average value for this parameter
	 */
	public void setWeightedAverage(int weightedAverage) {
		this.weightedAverage = weightedAverage;
	}

	/**
	 * Returns the classification of the weighted average value for this
	 * parameter.
	 * 
	 * @return the classification category
	 */
	public ParameterClassification getCategory() {
		return category;
	}

	/**
	 * Sets the classification of the weighted average value for this
	 * parameter.
	 * 
	 * @param category the classification category
	 */
	public void setCategory(ParameterClassification category) {
		this.category = category;
	}
}
