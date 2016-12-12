package eu.ewall.platform.idss.automaticgoalsetting.service;

import eu.ewall.platform.idss.service.model.common.LRSleepDayParameter;
import eu.ewall.platform.idss.service.model.common.LRSleepDayPattern;
import eu.ewall.platform.idss.service.model.common.SleepGoalMeasure;
import eu.ewall.platform.idss.service.model.type.ParameterClassification;
import eu.ewall.platform.idss.utils.json.JsonObject;

public class SleepGoalCategoryValue extends JsonObject {
	private SleepGoalMeasure measure;
	private ParameterClassification category;
	private int value;
	
	public SleepGoalCategoryValue(SleepGoalMeasure measure,
			ParameterClassification category, int value) {
		this.measure = measure;
		this.category = category;
		this.value = value;
	}

	public SleepGoalMeasure getMeasure() {
		return measure;
	}

	public ParameterClassification getCategory() {
		return category;
	}

	public int getValue() {
		return value;
	}
	
	/**
	 * Returns the category (below average, normal, above average) and weighted
	 * average value for the specified sleep goal measure. If it's not defined
	 * in the specified pattern, this method returns null.
	 * 
	 * @param pattern the sleep day pattern
	 * @param measure the sleep goal measure
	 * @return the category and weighted average value, or null
	 */
	public static SleepGoalCategoryValue getFromDayPattern(
			LRSleepDayPattern pattern, SleepGoalMeasure measure) {
		switch (measure) {
		case MINIMUM_MINUTES_SLEPT:
			return getMinimumMinutesSlept(pattern);
		case MAXIMUM_MINUTES_SLEPT:
			return getMaximumMinutesSlept(pattern);
		case SLEEP_EFFICIENCY:
			return getSleepEfficiency(pattern);
		case GOING_TO_SLEEP_TIME:
			return getGoingToSleepTime(pattern);
		case WAKING_UP_TIME:
			return getWakingUpTime(pattern);
		default:
			throw new RuntimeException("Sleep goal measure " + measure +
					" not supported");
		}
	}
	
	private static SleepGoalCategoryValue getMinimumMinutesSlept(
			LRSleepDayPattern pattern) {
		LRSleepDayParameter param = pattern.findParameter(
				LRSleepDayParameter.PARAM_TOTAL_SLEEP_TIME);
		if (param == null)
			return null;
		return new SleepGoalCategoryValue(
				SleepGoalMeasure.MINIMUM_MINUTES_SLEPT, param.getCategory(),
				param.getWeightedAverage());
	}
	
	private static SleepGoalCategoryValue getMaximumMinutesSlept(
			LRSleepDayPattern pattern) {
		LRSleepDayParameter param = pattern.findParameter(
				LRSleepDayParameter.PARAM_TOTAL_SLEEP_TIME);
		if (param == null)
			return null;
		return new SleepGoalCategoryValue(
				SleepGoalMeasure.MAXIMUM_MINUTES_SLEPT, param.getCategory(),
				param.getWeightedAverage());
	}
	
	private static SleepGoalCategoryValue getSleepEfficiency(
			LRSleepDayPattern pattern) {
		LRSleepDayParameter param = pattern.findParameter(
				LRSleepDayParameter.PARAM_SLEEP_EFFICIENCY);
		if (param == null)
			return null;
		return new SleepGoalCategoryValue(SleepGoalMeasure.SLEEP_EFFICIENCY,
				param.getCategory(), param.getWeightedAverage());
	}
	
	private static SleepGoalCategoryValue getGoingToSleepTime(
			LRSleepDayPattern pattern) {
		LRSleepDayParameter param = pattern.findParameter(
				LRSleepDayParameter.PARAM_BED_ON_TIME);
		if (param == null)
			return null;
		return new SleepGoalCategoryValue(SleepGoalMeasure.GOING_TO_SLEEP_TIME,
				param.getCategory(), param.getWeightedAverage());
	}
	
	private static SleepGoalCategoryValue getWakingUpTime(
			LRSleepDayPattern pattern) {
		LRSleepDayParameter param = pattern.findParameter(
				LRSleepDayParameter.PARAM_BED_OFF_TIME);
		if (param == null)
			return null;
		return new SleepGoalCategoryValue(SleepGoalMeasure.WAKING_UP_TIME,
				param.getCategory(), param.getWeightedAverage());
	}
}
