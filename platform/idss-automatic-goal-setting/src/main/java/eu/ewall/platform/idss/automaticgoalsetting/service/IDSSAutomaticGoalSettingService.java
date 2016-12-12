package eu.ewall.platform.idss.automaticgoalsetting.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.ILoggerFactory;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseConnection;
import eu.ewall.platform.idss.dao.DatabaseCriteria;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseFactory;
import eu.ewall.platform.idss.dao.DatabaseObjectMapper;
import eu.ewall.platform.idss.dao.DatabaseSort;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.service.ScheduledService;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.ActivityGoal;
import eu.ewall.platform.idss.service.model.common.ActivityMeasure;
import eu.ewall.platform.idss.service.model.common.AverageActivity;
import eu.ewall.platform.idss.service.model.common.DataUpdated;
import eu.ewall.platform.idss.service.model.common.LRSleepWeekPattern;
import eu.ewall.platform.idss.service.model.common.SleepGoalMeasure;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.idss.utils.datetime.VirtualClock;

/**
 * Scheduled service for intelligent decision support - automatic goal setting.
 * After construction you must call {@link
 * #setPullInputProvider(PullInputProvider) setInputProvider()} and {@link
 * #setConfig(Configuration) setConfig()}.
 * 
 * <p>You must configure a {@link DatabaseFactory DatabaseFactory} in the
 * {@link AppComponents AppComponents}. You may also configure an {@link
 * ILoggerFactory ILoggerFactory}.</p>
 * 
 * <p>The service gets the time from the {@link VirtualClock VirtualClock} so
 * it supports quick simulations.</p>
 * 
 * @author Dennis Hofs (RRD)
 */
public class IDSSAutomaticGoalSettingService extends ScheduledService {
	// TODO make configurable
	private static final String PROCESSING_START_DATE = "2016-01-01";

	private Configuration config;
	private PullInputProvider pullInputProvider;
	private final Object dbLock = new Object();
	private String dbName;
	
	/**
	 * Constructs a new instance.
	 * 
	 * @param dbName the database name
	 * @throws Exception if an error occurs while initializing the database
	 * connection
	 */
	public IDSSAutomaticGoalSettingService(String dbName) throws Exception {
		this.dbName = dbName;
	}
	
	/**
	 * Sets the configuration. This must be called before starting the service.
	 * 
	 * @param config the configuration
	 */
	public void setConfig(Configuration config) {
		this.config = config;
	}
	
	/**
	 * Sets the pull input provider. This must be called before starting the
	 * service.
	 * 
	 * @param pullInputProvider the pull input provider
	 */
	public void setPullInputProvider(PullInputProvider pullInputProvider) {
		this.pullInputProvider = pullInputProvider;
	}
	
	/**
	 * Returns the activity goal for the specified user at the specified date.
	 * If no goal is defined, this method returns null.
	 * 
	 * @param user the username
	 * @param measure the activity measure
	 * @param date the date
	 * @return the activity goal or null
	 * @throws DatabaseException if an error occurs while reading from the
	 * database
	 * @throws IOException if the database connection can't be established
	 */
	public ActivityGoal getActivityGoal(String user, ActivityMeasure measure,
			LocalDate date) throws DatabaseException, IOException {
		DatabaseConnection dbConn = openDatabaseConnection();
		try {
			Database database = getDatabase(dbConn);
			DatabaseObjectMapper mapper = new DatabaseObjectMapper();
			String dateVal = (String)mapper.toPrimitiveDatabaseValue(date,
					DatabaseType.DATE);
			DatabaseCriteria criteria = new DatabaseCriteria.And(
					new DatabaseCriteria.Equal("user", user),
					new DatabaseCriteria.Equal("date", dateVal)
			);
			return database.selectOne(new ActivityGoalTable(measure), criteria,
					null);
		} finally {
			closeDatabaseConnection(dbConn);
		}
	}
	
	/**
	 * Returns the activity goals for the specified user between two specified
	 * dates. It only returns dates for which a goal is defined. The returned
	 * list is ordered by date.
	 * 
	 * @param user the username
	 * @param measure the activity measure
	 * @param from the start date
	 * @param to the end date
	 * @return the activity goals
	 * @throws DatabaseException if an error occurs while reading from the
	 * database
	 * @throws IOException if the database connection can't be established
	 */
	public List<ActivityGoal> getActivityGoals(String user,
			ActivityMeasure measure, LocalDate from, LocalDate to)
			throws DatabaseException, IOException {
		DatabaseConnection dbConn = openDatabaseConnection();
		try {
			Database database = getDatabase(dbConn);
			DatabaseObjectMapper mapper = new DatabaseObjectMapper();
			String fromVal = (String)mapper.toPrimitiveDatabaseValue(from,
					DatabaseType.DATE);
			String toVal = (String)mapper.toPrimitiveDatabaseValue(to,
					DatabaseType.DATE);
			DatabaseCriteria criteria = new DatabaseCriteria.And(
					new DatabaseCriteria.Equal("user", user),
					new DatabaseCriteria.GreaterEqual("date", fromVal),
					new DatabaseCriteria.LessEqual("date", toVal)
			);
			DatabaseSort[] sort = new DatabaseSort[] {
					new DatabaseSort("date", true)
			};
			return database.select(new ActivityGoalTable(measure), criteria, 0,
					sort);
		} finally {
			closeDatabaseConnection(dbConn);
		}
	}

	@Override
	public void runTask() throws Exception {
		logger.info("Start task: IDSS Automatic Goal Setting.");
		DatabaseConnection dbConn = openDatabaseConnection();
		try {
			Database database = getDatabase(dbConn);
			List<IDSSUserProfile> users = pullInputProvider.getUsers();
			for (IDSSUserProfile user : users) {
				runUserTask(database, user);
			}
		} finally {
			closeDatabaseConnection(dbConn);
		}
		logger.info("End task: iDSS Automatic Goal Setting");
	}
	
	/**
	 * Runs the service task for the specified user.
	 * 
	 * @param database the database
	 * @param user the user
	 * @throws Exception if any error occurs during communication with the
	 * database or the input provider
	 */
	private void runUserTask(Database database, IDSSUserProfile user)
			throws Exception {
		updateGoals(GoalMainType.ACTIVITY, database, user);
		updateGoals(GoalMainType.SLEEP, database, user);
	}
	
	/**
	 * Updates activity goals or sleep goals for the specified user.
	 * 
	 * @param goalType the goal type (activity or sleep)
	 * @param database the database
	 * @param user the user
	 * @throws Exception if any error occurs during communication with the
	 * database or the input provider
	 */
	private void updateGoals(GoalMainType goalType, Database database,
			IDSSUserProfile user) throws Exception {
		VirtualClock clock = VirtualClock.getInstance();
		DateTime sysNow = clock.getTime();
		DateTime now = sysNow.withZone(user.getTimeZone());
		LocalDate today = now.toLocalDate();
		DatabaseCriteria criteria = new DatabaseCriteria.Equal("user",
				user.getUsername());
		GoalsUpdatedTable goalsUpdatedTable = new GoalsUpdatedTable(goalType);
		DataUpdated updated = database.selectOne(goalsUpdatedTable, criteria,
				null);
		LocalDate lastGoalDate = null;
		if (updated != null) {
			// clear newer goals from goals tables
			criteria = new DatabaseCriteria.And(
					new DatabaseCriteria.Equal("user", user.getUsername()),
					new DatabaseCriteria.GreaterThan("date",
							updated.getDate().toString("yyyy-MM-dd"))
			);
			if (goalType == GoalMainType.ACTIVITY) {
				for (ActivityMeasure measure : ActivityMeasure.values()) {
					ActivityGoalTable table = new ActivityGoalTable(measure);
					database.delete(table.getName(), criteria);
				}
			} else {
				for (SleepGoalMeasure measure : SleepGoalMeasure.values()) {
					SleepGoalTable table = new SleepGoalTable(measure);
					database.delete(table.getName(), criteria);
				}
			}
			lastGoalDate = updated.getDate();
		} else {
			// clear all goals tables
			criteria = new DatabaseCriteria.Equal("user", user.getUsername());
			if (goalType == GoalMainType.ACTIVITY) {
				for (ActivityMeasure measure : ActivityMeasure.values()) {
					ActivityGoalTable table = new ActivityGoalTable(measure);
					database.delete(table.getName(), criteria);
				}
			} else {
				for (SleepGoalMeasure measure : SleepGoalMeasure.values()) {
					SleepGoalTable table = new SleepGoalTable(measure);
					database.delete(table.getName(), criteria);
				}
			}
		}
		if (lastGoalDate != null && lastGoalDate.isAfter(today))
			return;
		// no goal defined for tomorrow
		// weekStartDay: 1 = Monday, 7 = Sunday
		int weekStartDay = pullInputProvider.getWeekStart(user.getUsername());
		LocalDate startWeek;
		LocalDate startDate;
		if (lastGoalDate == null) {
			// set startWeek to first day of the week at or after
			// PROCESSING_START_DATE
			DateTimeFormatter parser = DateTimeFormat.forPattern("yyyy-MM-dd");
			lastGoalDate = parser.parseLocalDate(PROCESSING_START_DATE);
			startWeek = lastGoalDate;
			int weekDay = startWeek.getDayOfWeek();
			startWeek = startWeek.plusDays((weekStartDay + 7 - weekDay) % 7);
			startDate = startWeek;
		} else {
			// set startWeek to first day of the week containing the day after
			// lastGoalDate
			startDate = lastGoalDate.plusDays(1);
			startWeek = startDate;
			int weekDay = startWeek.getDayOfWeek();
			startWeek = startWeek.plusDays((weekStartDay - 7 - weekDay) % 7);
		}
		// set endWeek to first day of the week containing tomorrow
		LocalDate endWeek = today.plusDays(1);
		int weekDay = endWeek.getDayOfWeek();
		endWeek = endWeek.plusDays((weekStartDay - 7 - weekDay) % 7);
		LocalDate week = startWeek;
		LocalDate patternUpdate;
		if (goalType == GoalMainType.ACTIVITY) {
			patternUpdate = pullInputProvider.getLastActivityWeekPatternUpdate(
					user.getUsername());
		} else {
			patternUpdate = pullInputProvider.getLastSleepWeekPatternUpdate(
					user.getUsername());
		}
		if (patternUpdate == null)
			return;
		while (!week.isAfter(endWeek)) {
			// 'patternUpdate' must be two days before 'week' or later
			// Example: If a week starts at a Monday, we want to set new goals
			// on Sunday with the most recent week pattern. So the week pattern
			// must have been updated with activity data up to and including
			// Saturday.
			LocalDate requiredUpdate = getWeekPatternUpdateForWeek(week);
			LocalDate weekStart;
			if (startDate.isAfter(week))
				weekStart = startDate;
			else
				weekStart = week;
			if (patternUpdate.isBefore(requiredUpdate))
				break;
			if (goalType == GoalMainType.ACTIVITY) {
				ActivityGoalGenerator goalGenerator =
						new ActivityGoalGenerator(config, pullInputProvider,
						logger);
				LocalDate update = getWeekPatternUpdateForWeek(week);
				for (ActivityMeasure measure : ActivityMeasure.values()) {
					List<AverageActivity> weekPattern =
							pullInputProvider.getActivityWeekPattern(
							user.getUsername(), measure, update);
					goalGenerator.createWeekGoals(database, user, measure,
							week, weekPattern, weekStart);
				}
			} else {
				SleepGoalGenerator goalGenerator =
						new SleepGoalGenerator(pullInputProvider, logger);
				LocalDate update = getWeekPatternUpdateForWeek(week);
				LRSleepWeekPattern weekPattern =
						pullInputProvider.getSleepWeekPattern(
						user.getUsername(), update);
				for (SleepGoalMeasure measure : SleepGoalMeasure.values()) {
					goalGenerator.createWeekGoals(database, user, measure,
							week, weekPattern, weekStart);
				}
			}
			LocalDate weekEnd = week.plusDays(6);
			if (updated != null) {
				updated.setDate(weekEnd);
				database.update(goalsUpdatedTable.getName(), updated);
			} else {
				updated = new DataUpdated();
				updated.setUser(user.getUsername());
				updated.setDate(weekEnd);
				database.insert(goalsUpdatedTable.getName(), updated);
			}
			week = week.plusWeeks(1);
		}
	}
	
	/**
	 * Returns the date when the week pattern must have been updated before
	 * generating goals for the specified week. This is two days before the
	 * start of the week. Example: If a week starts at a Monday, we want to set
	 * new goals on Sunday with the most recent week pattern. So the week
	 * pattern must have been updated with activity data up to and including
	 * Saturday.
	 * 
	 * @param week the start date of the week
	 * @return the date when the week pattern must have been updated
	 */
	private LocalDate getWeekPatternUpdateForWeek(LocalDate week) {
		return week.minusDays(2);
	}
	
	/**
	 * Returns the database for this service. It will create or upgrade the
	 * database if needed.
	 * 
	 * @param dbConn the database connection
	 * @return the database
	 * @throws DatabaseException if a database error occurs
	 */
	private Database getDatabase(DatabaseConnection dbConn)
			throws DatabaseException {
		synchronized (dbLock) {
			List<DatabaseTableDef<?>> tableDefs =
					new ArrayList<DatabaseTableDef<?>>();
			for (ActivityMeasure measure : ActivityMeasure.values()) {
				tableDefs.add(new ActivityGoalTable(measure));
			}
			for (SleepGoalMeasure measure : SleepGoalMeasure.values()) {
				tableDefs.add(new SleepGoalTable(measure));
			}
			tableDefs.add(new GoalsUpdatedTable(GoalMainType.ACTIVITY));
			tableDefs.add(new GoalsUpdatedTable(GoalMainType.SLEEP));
			Database db = dbConn.initDatabase(dbName, tableDefs, false);
			if (db.selectTables().contains("goals_updated"))
				db.dropTable("goals_updated");
			return db;
		}
	}
}
