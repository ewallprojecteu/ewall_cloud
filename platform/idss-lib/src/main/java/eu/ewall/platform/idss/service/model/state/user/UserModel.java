package eu.ewall.platform.idss.service.model.state.user;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.service.model.state.StateModel;
import eu.ewall.platform.idss.service.model.type.ActivityUnit;
import eu.ewall.platform.idss.service.model.type.GoalIntention;
import eu.ewall.platform.idss.service.model.type.UnitSystem;

/**
 * This model defines the state of a user. When you set an attribute, you
 * should also set the source reliability and update time. This can be done
 * at once using {@link #setAttribute(String, Object, Double, DateTime)
 * setAttribute()} from {@link StateModel StateModel}.
 * 
 * @author Dennis Hofs (RRD)
 * @author Harm op den Akker (RRD)
 */
public class UserModel extends StateModel {

	@DatabaseField(value=DatabaseType.FLOAT)
	private Double langFormalityPref = null;

	@DatabaseField(value=DatabaseType.FLOAT)
	private Double langLengthPref = null;

	@DatabaseField(value=DatabaseType.FLOAT)
	private Double langSuggestivePref = null;

	@DatabaseField(value=DatabaseType.INT)
	private Boolean langHonorifics = null;

	@DatabaseField(value=DatabaseType.FLOAT)
	private Double healthLiteracy = null;
	
	@DatabaseField(value=DatabaseType.OBJECT_LIST)
	private List<CalendarItem> todayCalendar = new ArrayList<CalendarItem>();

	@DatabaseField(value=DatabaseType.MAP, elemType=DatabaseType.FLOAT)
	private Map<String,Double> activityPrefs =
			new LinkedHashMap<String,Double>();

	@DatabaseField(value=DatabaseType.INT)
	private Boolean hasBicycle = null;
	
	@DatabaseField(value=DatabaseType.STRING)
	private GoalIntention goalIntention = null;
	
	@DatabaseField(value=DatabaseType.STRING)
	private String firstName = null;
	
	@DatabaseField(value=DatabaseType.STRING)
	private String lastName = null;
	
	@DatabaseField(value=DatabaseType.STRING)
	private ActivityUnit preferredActivityUnit = null;
	
	@DatabaseField(value=DatabaseType.STRING)
	private UnitSystem preferredUnitSystem = null;
	
	@DatabaseField(value=DatabaseType.OBJECT)
	private WeeklyRoutine weeklyRoutine = null;
	
	@DatabaseField(value=DatabaseType.INT)
	private Integer coinTotal;
	
	@DatabaseField(value=DatabaseType.LIST, elemType=DatabaseType.STRING)
	private List<String> unlockedRewards;
	
	/**
	 * Returns the language formality preference (0 = prefers informal,
	 * 1 = prefers formal).
	 * 
	 * @return the language formality preference
	 */
	public Double getLangFormalityPref() {
		return langFormalityPref;
	}

	/**
	 * Sets the language formality preference (0 = prefers informal,
	 * 1 = prefers formal).
	 * 
	 * @param langFormalityPref the language formality preference
	 */
	public void setLangFormalityPref(Double langFormalityPref) {
		this.langFormalityPref = langFormalityPref;
	}

	/**
	 * Returns the language length preference (0 = prefers short text,
	 * 1 = prefers long text).
	 * 
	 * @return the language length preference
	 */
	public Double getLangLengthPref() {
		return langLengthPref;
	}

	/**
	 * Sets the language length preference (0 = prefers short text,
	 * 1 = prefers long text).
	 * 
	 * @param langLengthPref the language length preference
	 */
	public void setLangLengthPref(Double langLengthPref) {
		this.langLengthPref = langLengthPref;
	}

	/**
	 * Returns the preference for imperative/suggestive language (0 = prefers
	 * imperative, 1 = prefers suggestive).
	 * 
	 * @return the preference for imperative/suggestive language
	 */
	public Double getLangSuggestivePref() {
		return langSuggestivePref;
	}

	/**
	 * Sets the preference for imperative/suggestive language (0 = prefers
	 * imperative, 1 = prefers suggestive).
	 * 
	 * @param langSuggestivePref the preference for imperative/suggestive
	 * language
	 */
	public void setLangSuggestivePref(Double langSuggestivePref) {
		this.langSuggestivePref = langSuggestivePref;
	}

	/**
	 * Returns whether honorifics should be used when addressing the user.
	 * 
	 * @return true if honorifics (u, vous, Sie) should be used, false if not
	 * (jij, tu, du)
	 */
	public Boolean isLangHonorifics() {
		return langHonorifics;
	}

	/**
	 * Sets whether honorifics should be used when addressing the user.
	 * 
	 * @param langHonorifics true if honorifics (u, vous, Sie) should be used,
	 * false if not (jij, tu, du)
	 */
	public void setLangHonorifics(Boolean langHonorifics) {
		this.langHonorifics = langHonorifics;
	}

	/**
	 * Returns the health literacy (0..1).
	 * 
	 * @return the health literacy
	 */
	public Double getHealthLiteracy() {
		return healthLiteracy;
	}

	/**
	 * Sets the health literacy (0..1).
	 * 
	 * @param healthLiteracy the health literacy
	 */
	public void setHealthLiteracy(Double healthLiteracy) {
		this.healthLiteracy = healthLiteracy;
	}

	/**
	 * Returns the calendar items for today.
	 * 
	 * @return the calendar items for today
	 */
	public List<CalendarItem> getTodayCalendar() {
		return todayCalendar;
	}

	/**
	 * Sets the calendar item for today.
	 * 
	 * @param todayCalendar the calendar items for today
	 */
	public void setTodayCalendar(List<CalendarItem> todayCalendar) {
		this.todayCalendar = todayCalendar;
	}

	/**
	 * Returns the preferences for specific types of activities. The keys in
	 * the map are the activity IDs from an activity ontology. The value is a
	 * number between 0 (user does not want to perform this type of activity)
	 * and 1 (user loves performing this type of activity).
	 * 
	 * @return the activity preferences
	 */
	public Map<String, Double> getActivityPrefs() {
		return activityPrefs;
	}

	/**
	 * Sets the preferences for specific types of activities. The keys in the
	 * map are the activity IDs from an activity ontology. The value is a
	 * number between 0 (user does not want to perform this type of activity)
	 * and 1 (user loves performing this type of activity).
	 * 
	 * @param activityPrefs the activity preferences
	 */
	public void setActivityPrefs(Map<String, Double> activityPrefs) {
		this.activityPrefs = activityPrefs;
	}

	/**
	 * Returns whether the user has a bicycle.
	 * 
	 * @return true if the user has a bicycle, false otherwise
	 */
	public Boolean isHasBicycle() {
		return hasBicycle;
	}

	/**
	 * Sets whether the user has a bicycle.
	 * 
	 * @param hasBicycle true if the user has a bicycle, false otherwise
	 */
	public void setHasBicycle(Boolean hasBicycle) {
		this.hasBicycle = hasBicycle;
	}

	/**
	 * Returns the goal intention.
	 * 
	 * @return the goal intention
	 */
	public GoalIntention getGoalIntention() {
		return goalIntention;
	}

	/**
	 * Sets the goal intention.
	 * 
	 * @param goalIntention the goal intention
	 */
	public void setGoalIntention(GoalIntention goalIntention) {
		this.goalIntention = goalIntention;
	}
	
	/**
	 * Returns the first name of this user.
	 * @return the first name of this user.
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Sets the first name for this user.
	 * @param firstName the first name for this user.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * Returns the last name of this user.
	 * @return the last name of this user.
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Sets the last name for this user.
	 * @param lastName the last name for this user.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * Returns the preferred {@link ActivityUnit} for this user as either {@link ActivityUnit#STEPS},
	 * {@link ActivityUnit#CALORIES}, or {@link ActivityUnit#DISTANCE}.
	 * @return the preferred unit for activity for this user.
	 */
	public ActivityUnit getPreferredActivityUnit() {
		return preferredActivityUnit;
	}
	
	/**
	 * Sets the preferred activity unit for this user as either {@link ActivityUnit#STEPS},
	 * {@link ActivityUnit#CALORIES}, or {@link ActivityUnit#DISTANCE}.
	 * @param preferredActivityUnit the preferred unit for activity for this user.
	 */
	public void setPreferredActivityUnit(ActivityUnit preferredActivityUnit) {
		this.preferredActivityUnit = preferredActivityUnit;
	}
	
	/**
	 * Returns the preferred unit system for this user as either {@link UnitSystem#METRIC},
	 * {@link UnitSystem#IMPERIAL}, or {@link UnitSystem#STANDARD}.
	 * @return the preferred unit system for this user.
	 */
	public UnitSystem getPreferredUnitSystem() {
		return preferredUnitSystem;
	}
	
	/**
	 * Sets the preferred unit system for this user as either {@link UnitSystem#METRIC},
	 * {@link UnitSystem#IMPERIAL}, or {@link UnitSystem#STANDARD}.
	 * @param preferredUnitSystem the preferred unit system for this user.
	 */
	public void setPreferredUnitSystem(UnitSystem preferredUnitSystem) {
		this.preferredUnitSystem = preferredUnitSystem;
	}
	
	/**
	 * Returns the user's {@link WeeklyRoutine}.
	 * @return the user's {@link WeeklyRoutine}.
	 */
	public WeeklyRoutine getWeeklyRoutine() {
		return weeklyRoutine;
	}
	
	/**
	 * Sets the user's {@link WeeklyRoutine}.
	 * @param weeklyRoutine the user's {@link WeeklyRoutine}.
	 */
	public void setWeeklyRoutine(WeeklyRoutine weeklyRoutine) {
		this.weeklyRoutine = weeklyRoutine;
	}
	
	public Integer getCoinTotal() {
		return coinTotal;
	}
	
	public void setCoinTotal(Integer coinTotal) {
		this.coinTotal = coinTotal;
	}
	
	public List<String> getUnlockedRewards() {
		return unlockedRewards;
	}
	
	public void setUnlockedRewards(List<String> unlockedRewards) {
		this.unlockedRewards = unlockedRewards;
	}
	
}
