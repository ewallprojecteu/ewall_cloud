package eu.ewall.platform.idss.automaticgoalsetting.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.slf4j.Logger;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.LRSleepDayPattern;
import eu.ewall.platform.idss.service.model.common.LRSleepWeekPattern;
import eu.ewall.platform.idss.service.model.common.SleepGoal;
import eu.ewall.platform.idss.service.model.common.SleepGoalMeasure;
import eu.ewall.platform.idss.service.model.type.ParameterClassification;

/**
 * This class can generate sleep goals. It is used inside {@link
 * IDSSAutomaticGoalSettingService IDSSAutomaticGoalSettingService}.
 * 
 * @author Dennis Hofs (RRD)
 */
public class SleepGoalGenerator {
	private PullInputProvider pullInputProvider;
	private Logger logger;
	
	/**
	 * Constructs a new sleep goal generator.
	 * 
	 * @param config the configuration
	 * @param pullInputProvider the pull input provider
	 * @param logger the logger
	 */
	public SleepGoalGenerator(PullInputProvider pullInputProvider,
			Logger logger) {
		this.pullInputProvider = pullInputProvider;
		this.logger = logger;
	}
	
	/**
	 * Creates sleep goals for the specified week.
	 * 
	 * @param database the database
	 * @param user the user profile
	 * @param measure the sleep goal measure
	 * @param week the start of the week
	 * @param weekPattern the sleep week pattern based on data known before
	 * the start of the week
	 * @param start the date for which the first goal should be set in this
	 * week
	 * @throws Exception if any error occurs during communication with the
	 * database or the input provider
	 */
	public void createWeekGoals(Database database,
			IDSSUserProfile user, SleepGoalMeasure measure, LocalDate week,
			LRSleepWeekPattern weekPattern, LocalDate start)
			throws Exception {
		int ultimate = pullInputProvider.getUltimateSleepGoal(
				user.getUsername(), measure);
		Map<Integer,SleepGoalCategoryValue> dayAverages =
				new LinkedHashMap<Integer,SleepGoalCategoryValue>();
		for (LRSleepDayPattern day : weekPattern.getDays()) {
			SleepGoalCategoryValue catValue =
					SleepGoalCategoryValue.getFromDayPattern(day, measure);
			if (catValue != null)
				dayAverages.put(day.getWeekDay(), catValue);
		}
		Map<ParameterClassification,Integer> catAvgs = getCategoryAverages(
				dayAverages.values());
		Map<ParameterClassification,Integer> catGoals = calcCategoryGoals(
				catAvgs, ultimate);
		List<SleepGoal> newGoals = new ArrayList<SleepGoal>();
		LocalDate end = week.plusDays(6);
		LocalDate current = start;
		while (!current.isAfter(end)) {
			newGoals.add(createDayGoal(user, current, measure, dayAverages,
					catGoals));
			current = current.plusDays(1);
		}
		SleepGoalTable table = new SleepGoalTable(measure);
		database.insert(table.getName(), newGoals);
		String newline = System.getProperty("line.separator");
		StringBuffer goalsLog = new StringBuffer("Set new sleep goals for " +
				measure.toString().toLowerCase() + " for user " +
				user.getUsername() + ":");
		for (SleepGoal goal : newGoals) {
			goalsLog.append(newline + "    " + goal);
		}
		logger.info(goalsLog.toString());
	}
	
	/**
	 * For each classification that occurs in the specified week pattern, this
	 * method calculates the average value.
	 * 
	 * @param weekPattern the week pattern (may be empty)
	 * @return the average values per category (might not define every possible
	 * category and may be empty)
	 */
	private Map<ParameterClassification,Integer> getCategoryAverages(
			Collection<SleepGoalCategoryValue> catValues) {
		Map<ParameterClassification,Integer> catSums =
				new HashMap<ParameterClassification,Integer>();
		Map<ParameterClassification,Integer> catCounts =
				new HashMap<ParameterClassification,Integer>();
		for (SleepGoalCategoryValue catValue : catValues) {
			ParameterClassification cat = catValue.getCategory();
			int sum = 0;
			int count = 0;
			if (catSums.keySet().contains(cat)) {
				sum = catSums.get(cat);
				count = catCounts.get(cat);
			}
			catSums.put(cat, sum + catValue.getValue());
			catCounts.put(cat, count + 1);
		}
		Map<ParameterClassification,Integer> catAvgs =
				new HashMap<ParameterClassification,Integer>();
		for (ParameterClassification cat : catSums.keySet()) {
			int avg = Math.round((float)catSums.get(cat)/catCounts.get(cat));
			catAvgs.put(cat, avg);
		}
		return catAvgs;
	}
	
	/**
	 * Calculates a new goal for each category that occurs in the specified map
	 * of measured average sleep values.
	 * 
	 * @param catAvgs the average sleep value per category (might not define
	 * every possible category and may be empty)
	 * @param ultimate the ultimate goal
	 * @return the new goals per category (might not define every possible
	 * category and may be empty)
	 */
	private Map<ParameterClassification,Integer> calcCategoryGoals(
			Map<ParameterClassification,Integer> catAvgs, int ultimate) {
		Map<ParameterClassification,Integer> catGoals =
				new HashMap<ParameterClassification,Integer>();
		for (ParameterClassification cat : catAvgs.keySet()) {
			int avg = catAvgs.get(cat);
			// TODO goal calculation for sleep goal measure
//			double goal = avg + config.getIncrementFactor() * avg;
//			if (goal > ultimate)
//				goal = ultimate;
//			int units = (int)Math.round(goal / config.getRoundingFactor());
//			int roundGoal = units * config.getRoundingFactor();
			int goal = 0;
			catGoals.put(cat, goal);
		}
		return catGoals;
	}
	
	/**
	 * Creates a new sleep goal for the specified date. First it determines
	 * the week day. Then it tries to get the category for that day from
	 * "weekPattern" and it gets the goal for that category from "catGoals".
	 * Every category that occurs in "weekPattern" should be defined in
	 * "catGoals". But it is possible that not every week day is defined in
	 * "weekPattern". In that case it will get a default goal from "catGoals".
	 * It prefers the goal for category "normal". If it doesn't exist it will
	 * take "inactive" or "active" in that order.
	 * 
	 * <p>If "weekPattern" and "catGoals" are empty, it will set a default
	 * goal obtained from {@link DefaultSleepGoalGenerator
	 * DefaultSleepGoalGenerator}.</p>
	 * 
	 * @param user the user profile
	 * @param date the date
	 * @param measure the activity measure
	 * @param dayAverages map from week day to the category and average value
	 * for the specified sleep measure
	 * @param catGoals the goals per category
	 * @return the new activity goal
	 */
	private SleepGoal createDayGoal(IDSSUserProfile user, LocalDate date,
			SleepGoalMeasure measure,
			Map<Integer,SleepGoalCategoryValue> dayAverages,
			Map<ParameterClassification,Integer> catGoals) {
		ParameterClassification defCat = null;
		int defGoal = 0;
		ParameterClassification[] defPrefs = new ParameterClassification[] {
				ParameterClassification.NORMAL,
				ParameterClassification.BELOW_NORMAL,
				ParameterClassification.ABOVE_NORMAL
		};
		for (ParameterClassification cat : defPrefs) {
			if (catGoals.containsKey(cat)) {
				defCat = cat;
				defGoal = catGoals.get(cat);
				break;
			}
		}
		if (defCat == null) {
			defCat = ParameterClassification.NORMAL;
			defGoal = new DefaultSleepGoalGenerator().getGoal(measure, user);
		}
		int weekDay = date.getDayOfWeek();
		ParameterClassification cat = null;
		int goalVal = 0;
		SleepGoalCategoryValue catVal = dayAverages.get(weekDay);
		if (catVal != null) {
			cat = catVal.getCategory();
			goalVal = catGoals.get(cat);
		}
		if (cat == null) {
			cat = defCat;
			goalVal = defGoal;
		}
		SleepGoal goal = new SleepGoal();
		goal.setUser(user.getUsername());
		goal.setDate(date);
		goal.setCategory(cat);
		goal.setGoal(goalVal);
		return goal;
	}
}
