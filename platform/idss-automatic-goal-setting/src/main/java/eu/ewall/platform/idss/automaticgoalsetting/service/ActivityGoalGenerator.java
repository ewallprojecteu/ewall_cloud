package eu.ewall.platform.idss.automaticgoalsetting.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.slf4j.Logger;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.ActivityGoal;
import eu.ewall.platform.idss.service.model.common.ActivityMeasure;
import eu.ewall.platform.idss.service.model.common.AverageActivity;
import eu.ewall.platform.idss.service.model.type.ActivityCategory;

/**
 * This class can generate activity goals. It is used inside {@link
 * IDSSAutomaticGoalSettingService IDSSAutomaticGoalSettingService}.
 * 
 * @author Dennis Hofs (RRD)
 */
public class ActivityGoalGenerator {
	private Configuration config;
	private PullInputProvider pullInputProvider;
	private Logger logger;
	
	/**
	 * Constructs a new activity goal generator.
	 * 
	 * @param config the configuration
	 * @param pullInputProvider the pull input provider
	 * @param logger the logger
	 */
	public ActivityGoalGenerator(Configuration config,
			PullInputProvider pullInputProvider, Logger logger) {
		this.config = config;
		this.pullInputProvider = pullInputProvider;
		this.logger = logger;
	}
	
	/**
	 * Creates activity goals for the specified week.
	 * 
	 * @param database the database
	 * @param user the user profile
	 * @param measure the activity measure
	 * @param week the start of the week
	 * @param weekPattern the activity week pattern based on data known before
	 * the start of the week
	 * @param start the date for which the first goal should be set in this
	 * week
	 * @throws Exception if any error occurs during communication with the
	 * database or the input provider
	 */
	public void createWeekGoals(Database database,
			IDSSUserProfile user, ActivityMeasure measure, LocalDate week,
			List<AverageActivity> weekPattern, LocalDate start)
			throws Exception {
		int ultimate = pullInputProvider.getUltimateActivityGoal(
				user.getUsername(), measure);
		Map<ActivityCategory,Integer> catAvgs = getCategoryAverages(
				weekPattern);
		Map<ActivityCategory,Integer> catGoals = calcCategoryGoals(
				catAvgs, ultimate);
		List<ActivityGoal> newGoals = new ArrayList<ActivityGoal>();
		LocalDate end = week.plusDays(6);
		LocalDate current = start;
		while (!current.isAfter(end)) {
			newGoals.add(createDayGoal(user, current, measure, weekPattern,
					catGoals));
			current = current.plusDays(1);
		}
		ActivityGoalTable table = new ActivityGoalTable(measure);
		database.insert(table.getName(), newGoals);
		String newline = System.getProperty("line.separator");
		StringBuffer goalsLog = new StringBuffer("Set new activity goals in " +
				measure.toString().toLowerCase() + " for user " +
				user.getUsername() + ":");
		for (ActivityGoal goal : newGoals) {
			goalsLog.append(newline + "    " + goal);
		}
		logger.info(goalsLog.toString());
	}
	
	/**
	 * For each activity category that occurs in the specified week pattern,
	 * this method calculates the average activity value.
	 * 
	 * @param weekPattern the week pattern (may be empty)
	 * @return the average activity values per category (might not define every
	 * possible category and may be empty)
	 */
	private Map<ActivityCategory,Integer> getCategoryAverages(
			List<AverageActivity> weekPattern) {
		Map<ActivityCategory,Integer> catSums =
				new HashMap<ActivityCategory,Integer>();
		Map<ActivityCategory,Integer> catCounts =
				new HashMap<ActivityCategory,Integer>();
		for (AverageActivity day : weekPattern) {
			ActivityCategory cat = day.getCategory();
			int sum = 0;
			int count = 0;
			if (catSums.keySet().contains(cat)) {
				sum = catSums.get(cat);
				count = catCounts.get(cat);
			}
			catSums.put(cat, sum + day.getWeightedAverage());
			catCounts.put(cat, count + 1);
		}
		Map<ActivityCategory,Integer> catAvgs =
				new HashMap<ActivityCategory,Integer>();
		for (ActivityCategory cat : catSums.keySet()) {
			int avg = Math.round((float)catSums.get(cat)/catCounts.get(cat));
			catAvgs.put(cat, avg);
		}
		return catAvgs;
	}
	
	/**
	 * Calculates a new goal for each activity category that occurs in the
	 * specified map of measured average activity values.
	 * 
	 * @param catAvgs the average activity value per activity category (might
	 * not define every possible category and may be empty)
	 * @param ultimate the ultimate goal
	 * @return the new goals per category (might not define every possible
	 * category and may be empty)
	 */
	private Map<ActivityCategory,Integer> calcCategoryGoals(
			Map<ActivityCategory,Integer> catAvgs, int ultimate) {
		Map<ActivityCategory,Integer> catGoals =
				new HashMap<ActivityCategory,Integer>();
		for (ActivityCategory cat : catAvgs.keySet()) {
			int avg = catAvgs.get(cat);
			double goal = avg + config.getIncrementFactor() * avg;
			if (goal > ultimate)
				goal = ultimate;
			int units = (int)Math.round(goal / config.getRoundingFactor());
			int roundGoal = units * config.getRoundingFactor();
			catGoals.put(cat, roundGoal);
		}
		return catGoals;
	}
	
	/**
	 * Creates a new activity goal for the specified date. First it determines
	 * the week day. Then it tries to get the activity category for that day
	 * from "weekPattern" and it gets the goal for that category from
	 * "catGoals". Every activity category that occurs in "weekPattern" should
	 * be defined in "catGoals". But it is possible that not every week day is
	 * defined in "weekPattern". In that case it will get a default goal from
	 * "catGoals". It prefers the goal for category "normal". If it doesn't
	 * exist it will take "inactive" or "active" in that order.
	 * 
	 * <p>If "weekPattern" and "catGoals" are empty, it will set a default
	 * goal obtained from {@link DefaultActivityGoalGenerator
	 * DefaultActivityGoalGenerator}.</p>
	 * 
	 * @param user the user profile
	 * @param date the date
	 * @param measure the activity measure
	 * @param weekPattern the activity week pattern
	 * @param catGoals the goals per category
	 * @return the new activity goal
	 */
	private ActivityGoal createDayGoal(IDSSUserProfile user, LocalDate date,
			ActivityMeasure measure, List<AverageActivity> weekPattern,
			Map<ActivityCategory,Integer> catGoals) {
		ActivityCategory defCat = null;
		int defGoal = 0;
		ActivityCategory[] defPrefs = new ActivityCategory[] {
				ActivityCategory.NORMAL,
				ActivityCategory.INACTIVE,
				ActivityCategory.ACTIVE
		};
		for (ActivityCategory cat : defPrefs) {
			if (catGoals.containsKey(cat)) {
				defCat = cat;
				defGoal = catGoals.get(cat);
				break;
			}
		}
		if (defCat == null) {
			defCat = ActivityCategory.NORMAL;
			defGoal = new DefaultActivityGoalGenerator().getGoal(measure, user);
		}
		int weekDay = date.getDayOfWeek();
		ActivityCategory cat = null;
		int goalVal = 0;
		for (AverageActivity day : weekPattern) {
			if (day.getWeekDay() == weekDay) {
				cat = day.getCategory();
				goalVal = catGoals.get(cat);
				break;
			}
		}
		if (cat == null) {
			cat = defCat;
			goalVal = defGoal;
		}
		ActivityGoal goal = new ActivityGoal();
		goal.setUser(user.getUsername());
		goal.setDate(date);
		goal.setCategory(cat);
		goal.setGoal(goalVal);
		return goal;
	}
}
