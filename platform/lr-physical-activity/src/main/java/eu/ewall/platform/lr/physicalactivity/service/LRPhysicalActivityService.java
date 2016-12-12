package eu.ewall.platform.lr.physicalactivity.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import eu.ewall.platform.idss.service.model.common.ActivityMeasure;
import eu.ewall.platform.idss.service.model.common.AverageActivity;
import eu.ewall.platform.idss.service.model.common.DataUpdated;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.idss.utils.datetime.VirtualClock;

/**
 * Scheduled service for the lifestyle reasoner for physical activity. After
 * construction you must call {@link #setPullInputProvider(PullInputProvider)
 * setPullInputProvider()}.
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
public class LRPhysicalActivityService extends ScheduledService {
	public static final String LOGTAG = "LRPhysicalActivityService";
	
	// TODO make configurable
	private static final String PROCESSING_START_DATE = "2016-01-01";

	private PullInputProvider pullInputProvider;
	private ActivityClassifier classifier;
	private final Object dbLock = new Object();
	private String dbName;
	
	/**
	 * Constructs a new instance.
	 * 
	 * @param dbName the database name
	 * @throws Exception if an error occurs while initialising the database
	 * connection
	 */
	public LRPhysicalActivityService(String dbName) throws Exception {
		this.dbName = dbName;
		classifier = new DefaultActivityClassifier();
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
	 * Returns the date until which activity data has been processed to
	 * generate a week pattern. This date is inclusive. If no activity data
	 * has been processed, this method returns null.
	 * 
	 * @param user the user name
	 * @return the date until which activity data has been processed or null
	 * @throws DatabaseException if a database error occurs
	 * @throws IOException if the database connection can't be established
	 */
	public DataUpdated getLastUpdate(String user)
			throws DatabaseException, IOException {
		DatabaseConnection dbConn = openDatabaseConnection();
		try {
			Database database = getDatabase(dbConn);
			DatabaseCriteria criteria = new DatabaseCriteria.Equal("user",
					user);
			return database.selectOne(new ActivityProcessedTable(), criteria,
					null);
		} finally {
			closeDatabaseConnection(dbConn);
		}
	}
	
	/**
	 * Returns the week pattern of physical activity for the specified user as
	 * known after the specified date. This means that no activity data after
	 * that date has been processed to calculate the week pattern. If the date
	 * is set to null, it returns the most recent week pattern. It returns at
	 * most one instance for each week day. Days with no activity history are
	 * not included. You may call {@link #getLastUpdate(String)
	 * getLastUpdate()} to find out how much activity data has been processed
	 * so far.
	 * 
	 * @param user the user name
	 * @param date the date as described above
	 * @return the week pattern of physical activity
	 * @throws DatabaseException if a database error occurs
	 * @throws IOException if the database connection can't be established
	 */
	public List<AverageActivity> getActivityWeekPattern(
			ActivityMeasure measure, String user, LocalDate date)
			throws DatabaseException, IOException {
		DatabaseConnection dbConn = openDatabaseConnection();
		try {
			Database database = getDatabase(dbConn);
			DatabaseCriteria criteria;
			DatabaseObjectMapper mapper = new DatabaseObjectMapper();
			if (date != null) {
				criteria = new DatabaseCriteria.And(
						new DatabaseCriteria.Equal("user", user),
						new DatabaseCriteria.LessEqual("updated",
								(String)mapper.toPrimitiveDatabaseValue(date,
								DatabaseType.DATE))
				);
			} else {
				criteria = new DatabaseCriteria.Equal("user", user);
			}
			DatabaseSort[] sort = new DatabaseSort[] {
					new DatabaseSort("updated", false)
			};
			AverageActivity avg = database.selectOne(
					new AverageActivityTable(measure), criteria, sort);
			if (avg == null)
				return new ArrayList<AverageActivity>();
			criteria = new DatabaseCriteria.And(
					new DatabaseCriteria.Equal("user", user),
					new DatabaseCriteria.Equal("updated",
							(String)mapper.toPrimitiveDatabaseValue(
							avg.getUpdated(), DatabaseType.DATE))
			);
			sort = new DatabaseSort[] {
					new DatabaseSort("weekDay", true)
			};
			return database.select(new AverageActivityTable(measure), criteria,
					0, sort);
		} finally {
			closeDatabaseConnection(dbConn);
		}
	}
	
	@Override
	public void runTask() throws Exception {
		logger.info("Start task: lifestyle reasoner (physical activity)");
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
		logger.info("End task: lifestyle reasoner (physical activity)");
	}
	
	/**
	 * Runs the service task for the specified user.
	 * 
	 * @param database the database
	 * @param user the user
	 * @throws Exception if any error occurs during communication with the
	 * database or the input provider
	 */
	private void runUserTask(Database database, IDSSUserProfile user) throws Exception {
		VirtualClock clock = VirtualClock.getInstance();
		DateTime sysNow = clock.getTime();
		DateTime now = sysNow.withZone(user.getTimeZone());
		LocalDate today = now.toLocalDate();
		LocalDate yesterday = today.minusDays(1);
		DatabaseCriteria criteria = new DatabaseCriteria.Equal("user",
				user.getUsername());
		DataUpdated actProc = database.selectOne(
				new ActivityProcessedTable(), criteria, null);
		LocalDate lastUpdate = null;
		if (actProc != null) {
			lastUpdate = actProc.getDate();
			// Delete records after lastUpdate from table "averages", so the
			// table agrees with "processed". The tables might not match
			// because of manual database edits or a previous task could have
			// been interrupted.
			DatabaseObjectMapper mapper = new DatabaseObjectMapper();
			criteria = new DatabaseCriteria.And(
					new DatabaseCriteria.Equal("user", user.getUsername()),
					new DatabaseCriteria.GreaterThan("updated",
							(String)mapper.toPrimitiveDatabaseValue(lastUpdate,
							DatabaseType.DATE))
			);
			for (ActivityMeasure measure : ActivityMeasure.values()) {
				AverageActivityTable avgTable = new AverageActivityTable(
						measure);
				database.delete(avgTable.getName(), criteria);
			}
		} else {
			DateTimeFormatter parser = DateTimeFormat.forPattern("yyyy-MM-dd");
			lastUpdate = parser.parseLocalDate(PROCESSING_START_DATE);
			lastUpdate = lastUpdate.minusDays(1);
			// Delete all records from table "averages", so the table agrees
			// with "processed". The tables might not match because of manual
			// database edits or a previous task could have been interrupted.
			criteria = new DatabaseCriteria.Equal("user", user.getUsername());
			for (ActivityMeasure measure : ActivityMeasure.values()) {
				AverageActivityTable avgTable = new AverageActivityTable(
						measure);
				database.delete(avgTable.getName(), criteria);
			}
		}
		if (!yesterday.isAfter(lastUpdate))
			return;
		Map<ActivityMeasure,List<DayActivity>> history =
				new LinkedHashMap<ActivityMeasure,List<DayActivity>>();
		Map<ActivityMeasure,List<DayActivity>> dateHistory =
				new LinkedHashMap<ActivityMeasure,List<DayActivity>>();
		for (ActivityMeasure measure : ActivityMeasure.values()) {
			List<DayActivity> measureHistory = updateUserActivity(database,
					user, measure, yesterday);
			history.put(measure, measureHistory);
			dateHistory.put(measure, new ArrayList<DayActivity>());
		}
		int nextHistoryIndex = 0;
		LocalDate date = lastUpdate.plusDays(1);
		while (!date.isAfter(yesterday)) {
			for (ActivityMeasure measure : ActivityMeasure.values()) {
				// add activity until date (inclusive)
				List<DayActivity> measureHistory = history.get(measure);
				List<DayActivity> measureDateHistory = dateHistory.get(
						measure);
				for (int i = nextHistoryIndex; i < measureHistory.size();
						i++) {
					DayActivity act = measureHistory.get(i);
					if (act.getDate().isAfter(date))
						break;
					measureDateHistory.add(act);
					nextHistoryIndex++;
				}
			}
			calculateWeekPattern(database, user.getUsername(), date,
					dateHistory);
			date = date.plusDays(1);
		}
	}
	
	/**
	 * Processes new activity data until yesterday (inclusive). It gets the
	 * activity value for the specified measure from the input provider,
	 * classifies it and adds it to the database. It returns the complete
	 * classified activity history of the user without outliers.
	 * 
	 * <p>If there is no activity data in the database yet, it will process
	 * all data starting at PROCESSING_START_DATE.</p>
	 * 
	 * @param database the database
	 * @param user the user name
	 * @param measure the activity measure
	 * @param yesterday the date of yesterday (time 0:00, time zone of the
	 * user)
	 * @return the activity history without outliers
	 * @throws Exception if any error occurs during communication with the
	 * database or the input provider
	 */
	private List<DayActivity> updateUserActivity(Database database,
			IDSSUserProfile user, ActivityMeasure measure,
			LocalDate yesterday) throws Exception {
		LocalDate lastUpdate = null;
		DatabaseCriteria criteria = new DatabaseCriteria.Equal("user",
				user.getUsername());
		DatabaseSort[] sort = new DatabaseSort[] {
				new DatabaseSort("date", false)
		};
		DayActivityTable table = new DayActivityTable(measure);
		DayActivity lastActivity = database.selectOne(table, criteria, sort);
		if (lastActivity != null)
			lastUpdate = lastActivity.getDate();
		if (lastUpdate == null) {
			DateTimeFormatter parser = DateTimeFormat.forPattern("yyyy-MM-dd");
			lastUpdate = parser.parseLocalDate(PROCESSING_START_DATE);
			lastUpdate = lastUpdate.minusDays(1);
		}
		criteria = new DatabaseCriteria.Equal("user", user.getUsername());
		sort = new DatabaseSort[] {
				new DatabaseSort("date", true)
		};
		List<DayActivity> history = database.select(table, criteria, 0, sort);
		List<DayActivity> noOutliers = filterNoOutliers(history);
		if (!yesterday.isAfter(lastUpdate))
			return noOutliers;
		LocalDate startUpdate = lastUpdate.plusDays(1);
		logger.info("Process activity in {} for user {} from {} to {}",
				measure.toString().toLowerCase(),
				user.getUsername(),
				startUpdate.toString("yyyy-MM-dd"),
				yesterday.toString("yyyy-MM-dd"));
		LocalDate currentDate = startUpdate;
		while (!currentDate.isAfter(yesterday)) {
			int value = 0;
			switch (measure) {
			case STEPS:
				value = pullInputProvider.getStepCount(user, currentDate);
				break;
			case CALORIES:
				value = pullInputProvider.getCalories(user, currentDate);
				break;
			case DISTANCE:
				value = pullInputProvider.getDistance(user, currentDate);
				break;
			default:
				throw new RuntimeException("Activity measure " + measure +
						" not supported");
			}
			DayActivity dayActivity = classifyDayActivity(user.getUsername(),
					currentDate, value, history, noOutliers);
			history.add(dayActivity);
			if (!dayActivity.isOutlier())
				noOutliers.add(dayActivity);
			database.insert(table.getName(), dayActivity);
			logger.info("Classified day activity in {}: {}",
					measure.toString().toLowerCase(), dayActivity);
			currentDate = currentDate.plusDays(1);
		}
		return noOutliers;
	}
	
	/**
	 * Classifies the activity of the specified date and returns a new
	 * DayActivity object. The service task calls this method once for each
	 * completed date.
	 * 
	 * @param user the username
	 * @param date the date
	 * @param value the activity value at the specified date
	 * @param history the activity history of the specified user until
	 * the specified date (exclusive), with outliers
	 * @param noOutliers the same as "history" but without outliers
	 * @return the DayActivity object
	 * @throws Exception if a database error occurs
	 */
	private DayActivity classifyDayActivity(String user, LocalDate date,
			int value, List<DayActivity> history, List<DayActivity> noOutliers)
			throws Exception {
		DayActivity dayActivity = new DayActivity();
		dayActivity.setUser(user);
		dayActivity.setDate(date);
		dayActivity.setValue(value);
		boolean outlier = classifier.isOutlier(history, value);
		dayActivity.setOutlier(outlier);
		if (!outlier) {
			dayActivity.setCategory(classifier.getSingleDayCategory(
					noOutliers, value));
		} else {
			dayActivity.setCategory(null);
		}
		return dayActivity;
	}
	
	/**
	 * Calculates the week pattern as known after the specified date. It adds
	 * the pattern to the database.
	 * 
	 * @param database the database
	 * @param user the user name
	 * @param date the date until which activity data should be processed
	 * (inclusive)
	 * @param history the activity history until the specified date (inclusive)
	 * and without outliers. It should contain an entry for each possible
	 * activity measure.
	 * @throws Exception if a database error occurs
	 */
	private void calculateWeekPattern(Database database, String user,
			LocalDate date, Map<ActivityMeasure,List<DayActivity>> history)
			throws Exception {
		// update weighted averages
		for (ActivityMeasure measure : ActivityMeasure.values()) {
			List<AverageActivity> weekPattern = new ArrayList<AverageActivity>();
			List<DayActivity> measureHistory = history.get(measure);
			for (int weekDay = 1; weekDay <= 7; weekDay++) {
				Integer weightedAvg = calcWeightedAverage(measureHistory,
						weekDay);
				if (weightedAvg == null)
					continue;
				AverageActivity avg = new AverageActivity();
				avg.setUser(user);
				avg.setWeekDay(weekDay);
				avg.setWeightedAverage(weightedAvg);
				avg.setCategory(classifier.getAverageCategory(measureHistory,
						weightedAvg));
				avg.setUpdated(date);
				AverageActivityTable avgTable = new AverageActivityTable(
						measure);
				database.insert(avgTable.getName(), avg);
				weekPattern.add(avg);
			}
			if (weekPattern.isEmpty())
				continue;
			String newline = System.getProperty("line.separator");
			StringBuffer logMsg = new StringBuffer(String.format(
					"Calculated week pattern in %s for user %s at %s:",
					measure.toString().toLowerCase(), user,
					date.toString("yyyy-MM-dd")));
			for (AverageActivity avgAct : weekPattern) {
				logMsg.append(newline + "    " + avgAct);
			}
			logger.info(logMsg.toString());
		}
		DatabaseCriteria criteria = new DatabaseCriteria.Equal("user", user);
		DataUpdated actProc = database.selectOne(
				new ActivityProcessedTable(), criteria, null);
		if (actProc != null) {
			actProc.setDate(date);
			database.update(ActivityProcessedTable.NAME, actProc);
		} else {
			actProc = new DataUpdated();
			actProc.setUser(user);
			actProc.setDate(date);
			database.insert(ActivityProcessedTable.NAME, actProc);
		}
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
				tableDefs.add(new DayActivityTable(measure));
				tableDefs.add(new AverageActivityTable(measure));
			}
			tableDefs.add(new ActivityProcessedTable());
			return dbConn.initDatabase(dbName, tableDefs, false);
		}
	}
	
	/**
	 * Calculates the weighted average activity for the specified week day. If
	 * the history does not contain any activity for that week day, then this
	 * method returns null.
	 * 
	 * @param history the activity history without outliers
	 * @param weekDay the week day (1 = Monday, 7 = Sunday)
	 * @return the weighted average or null
	 */
	private Integer calcWeightedAverage(List<DayActivity> history,
			int weekDay) {
		List<DayActivity> weekDayHistory = filterHistoryForWeekDay(history,
				weekDay);
		if (weekDayHistory.isEmpty())
			return null;
		LocalDate start = weekDayHistory.get(0).getDate();
		LocalDate end = weekDayHistory.get(weekDayHistory.size() - 1)
				.getDate();
		LocalDate current = start;
		Iterator<DayActivity> it = weekDayHistory.iterator();
		int sum = 0;
		int totalWeight = 0;
		DayActivity nextItem = it.next();
		int currWeight = 1;
		while (nextItem != null && !current.isAfter(end)) {
			if (!nextItem.getDate().isAfter(current)) {
				sum += currWeight * nextItem.getValue();
				totalWeight += currWeight;
				nextItem = null;
				if (it.hasNext())
					nextItem = it.next();
			}
			current = current.plusWeeks(1);
			currWeight++;
		}
		return sum / totalWeight;
	}
	
	/**
	 * Filters the specified history so the result only contains dates with the
	 * specified week day.
	 * 
	 * @param history the history
	 * @param weekDay the week day (1 = Monday, 7 = Sunday)
	 * @return the filtered history
	 */
	private List<DayActivity> filterHistoryForWeekDay(
			List<DayActivity> history, int weekDay) {
		List<DayActivity> result = new ArrayList<DayActivity>();
		for (DayActivity day : history) {
			int cmpWeekDay = day.getDate().getDayOfWeek();
			if (cmpWeekDay == weekDay)
				result.add(day);
		}
		return result;
	}
	
	/**
	 * Filters the specified history so the result won't contain outliers.
	 * 
	 * @param history the activity history
	 * @return the filtered history
	 */
	private List<DayActivity> filterNoOutliers(List<DayActivity> history) {
		List<DayActivity> result = new ArrayList<DayActivity>();
		for (DayActivity act : history) {
			if (!act.isOutlier())
				result.add(act);
		}
		return result;
	}
}
