package eu.ewall.platform.lr.sleepmonitor.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseConnection;
import eu.ewall.platform.idss.dao.DatabaseCriteria;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseFactory;
import eu.ewall.platform.idss.dao.DatabaseObjectMapper;
import eu.ewall.platform.idss.dao.DatabaseSort;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.response.ewall.sleep.ResponseWakeUpRoutine;
import eu.ewall.platform.idss.service.ScheduledService;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.DataUpdated;
import eu.ewall.platform.idss.service.model.common.LRSleepDayParameter;
import eu.ewall.platform.idss.service.model.common.LRSleepDayPattern;
import eu.ewall.platform.idss.service.model.common.LRSleepWeekPattern;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.idss.utils.datetime.VirtualClock;
import eu.ewall.platform.idss.utils.exception.FatalException;

/**
 * Scheduled service for the lifestyle reasoner for sleep monitoring. After
 * construction you must call {@link #setInputProvider(PullInputProvider)
 * setInputProvider()} and {@link #setConfig(Configuration) setConfig()}.
 * 
 * <p>The service stores data in a database. It requests a {@link
 * DatabaseFactory DatabaseFactory} from the {@link AppComponents
 * AppComponents}.</p>
 * 
 * <p>The service gets the time from the {@link VirtualClock VirtualClock} so
 * it supports quick simulations.</p>
 * 
 * @author Dennis Hofs (RRD)
 */
public class LRSleepMonitorService extends ScheduledService {
	public static final String LOGTAG = "LRSleepMonitorService";
	
	// TODO make configurable
	private static final String PROCESSING_START_DATE = "2016-01-01";

	private Configuration config;
	private PullInputProvider pullInputProvider;
	private SleepParameterClassifier classifier;
	private final Object dbLock = new Object();
	private String dbName;

	/**
	 * Constructs a new instance.
	 * 
	 * @param dbName the database name
	 * @throws Exception if an error occurs while initialising the database
	 * connection
	 */
	public LRSleepMonitorService(String dbName) throws Exception {
		this.dbName = dbName;
		classifier = new SleepParameterClassifier();
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
	 * Returns the date following the night until which sleep data has been
	 * processed to generate a week pattern. If no sleep data has been
	 * processed, this method returns null.
	 * 
	 * @param user the user name
	 * @return the date until which sleep data has been processed or null
	 * @throws DatabaseException if a database error occurs
	 * @throws IOException if the database connection can't be established
	 */
	public DataUpdated getLastUpdate(String user)
			throws DatabaseException, IOException {
		DatabaseConnection dbConn = openDatabaseConnection();
		try {
			Database database = getDatabase(dbConn);
			DatabaseCriteria criteria = new DatabaseCriteria.Equal("user", user);
			return database.selectOne(new SleepProcessedTable(), criteria, null);
		} finally {
			closeDatabaseConnection(dbConn);
		}
	}
	
	/**
	 * Returns the sleep week pattern for the specified user as known after the
	 * night that precedes the specified date. This means that no sleep data
	 * after that night has been processed to calculate the week pattern. If
	 * the date is set to null, it returns the most recent week pattern. You
	 * may call {@link #getLastUpdate(String) getLastUpdate()} to find out how
	 * much sleep data has been processed so far.
	 * 
	 * @param user the user name
	 * @param date the date as described above
	 * @return the sleep week pattern
	 * @throws DatabaseException if a database error occurs
	 * @throws IOException if the database connection can't be established
	 */
	public LRSleepWeekPattern getSleepWeekPattern(String user, LocalDate date)
			throws DatabaseException, IOException {
		DatabaseConnection dbConn = openDatabaseConnection();
		try {
			Database database = getDatabase(dbConn);
			LRSleepWeekPattern result = new LRSleepWeekPattern();
			List<LRSleepDayPattern> days = new ArrayList<LRSleepDayPattern>();
			result.setUser(user);
			result.setDays(days);
			result.setUpdated(null);
			DatabaseCriteria criteria = new DatabaseCriteria.Equal("user", user);
			DataUpdated dataUpdated = database.selectOne(new SleepProcessedTable(),
					criteria, null);
			if (dataUpdated == null)
				return result;
			LocalDate lastUpdate = dataUpdated.getDate();
			if (date == null || date.isAfter(lastUpdate))
				date = lastUpdate;
			criteria = new DatabaseCriteria.And(
					new DatabaseCriteria.Equal("user", user),
					new DatabaseCriteria.LessEqual("updated",
							date.toString("yyyy-MM-dd"))
			);
			DatabaseSort[] sort = new DatabaseSort[] {
					new DatabaseSort("updated", false)
			};
			AverageSleepParameter avg = database.selectOne(
					new AverageSleepParameterTable(), criteria, sort);
			if (avg == null)
				return result;
			LocalDate updated = avg.getUpdated();
			criteria = new DatabaseCriteria.And(
					new DatabaseCriteria.Equal("user", user),
					new DatabaseCriteria.Equal("updated",
							updated.toString("yyyy-MM-dd"))
			);
			sort = new DatabaseSort[] {
					new DatabaseSort("weekDay", true),
					new DatabaseSort("parameter", true)
			};
			List<AverageSleepParameter> avgParams = database.select(
					new AverageSleepParameterTable(), criteria, 0, sort);
			result.setUpdated(updated);
			result.setDays(getSleepDayPatterns(avgParams));
			return result;
		} finally {
			closeDatabaseConnection(dbConn);
		}
	}
	
	/**
	 * Converts the specified list of average sleep parameters to a list of
	 * sleep day patterns.
	 * 
	 * @param avgParams the average sleep parameters for one week pattern,
	 * ordered by weekDay, then parameter name
	 * @return the sleep day patterns
	 */
	private List<LRSleepDayPattern> getSleepDayPatterns(
			List<AverageSleepParameter> avgParams) {
		List<LRSleepDayPattern> result = new ArrayList<LRSleepDayPattern>();
		LRSleepDayPattern dayPattern = null;
		List<LRSleepDayParameter> currentDayParams = null;
		for (AverageSleepParameter avg : avgParams) {
			if (dayPattern == null || dayPattern.getWeekDay() !=
					avg.getWeekDay()) {
				dayPattern = new LRSleepDayPattern();
				dayPattern.setWeekDay(avg.getWeekDay());
				currentDayParams = new ArrayList<LRSleepDayParameter>();
				dayPattern.setParameters(currentDayParams);
				result.add(dayPattern);
			}
			LRSleepDayParameter dayParam = new LRSleepDayParameter();
			dayParam.setParameter(avg.getParameter());
			dayParam.setWeightedAverage(avg.getWeightedAverage());
			dayParam.setCategory(avg.getCategory());
			currentDayParams.add(dayParam);
		}
		return result;
	}

	@Override
	public void runTask() throws FatalException, Exception {
		logger.info("Start task: lifestyle reasoner (sleep monitor)");
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
		logger.info("End task: lifestyle reasoner (sleep monitor)");
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
		LocalDate lastCompleteNight = today;
		LocalDateTime localNow = now.toLocalDateTime();
		LocalDateTime endOfNight = today.toLocalDateTime(
				config.getEndOfNightTime());
		if (localNow.isBefore(endOfNight))
			lastCompleteNight = lastCompleteNight.minusDays(1);
		DatabaseCriteria criteria = new DatabaseCriteria.Equal("user",
				user.getUsername());
		DataUpdated dataProc = database.selectOne(new SleepProcessedTable(),
				criteria, null);
		LocalDate lastUpdate = null;
		if (dataProc != null) {
			lastUpdate = dataProc.getDate();
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
			database.delete(AverageSleepParameterTable.NAME, criteria);
		} else {
			DateTimeFormatter formatter = DateTimeFormat.forPattern(
					"yyyy-MM-dd");
			lastUpdate = formatter.parseLocalDate(PROCESSING_START_DATE);
			lastUpdate = lastUpdate.minusDays(1);
			// Delete all records from table "averages", so the table agrees
			// with "processed". The tables might not match because of manual
			// database edits or a previous task could have been interrupted.
			criteria = new DatabaseCriteria.Equal("user", user.getUsername());
			database.delete(AverageSleepParameterTable.NAME, criteria);
		}
		if (!lastCompleteNight.isAfter(lastUpdate))
			return;
		List<NightSleepData> history = updateUserSleepData(database, user,
				lastCompleteNight);
		int nextHistoryIndex = 0;
		List<NightSleepData> dateHistory = new ArrayList<NightSleepData>();
		LocalDate date = lastUpdate.plusDays(1);
		while (!date.isAfter(lastCompleteNight)) {
			// add data until date (inclusive)
			for (int i = nextHistoryIndex; i < history.size(); i++) {
				NightSleepData sleepData = history.get(i);
				if (sleepData.getDate().isAfter(date))
					break;
				dateHistory.add(sleepData);
				nextHistoryIndex++;
			}
			calculateWeekPattern(database, user.getUsername(), date,
					dateHistory);
			date = date.plusDays(1);
		}
	}

	/**
	 * Retrieves new sleep data until the specified date. That is the date
	 * following the last complete night. It gets night sleep data from the
	 * input provider and adds it to the database. It returns the complete
	 * history of the user.
	 * 
	 * <p>If there is no sleep data in the database yet, it will process all
	 * data starting at PROCESSING_START_DATE.</p>
	 * 
	 * @param database the database
	 * @param user the user name
	 * @param lastCompleteNight the date following the last complete night
	 * (time 0:00 in the time zone of the user)
	 * @return the sleep data history
	 * @throws Exception if any error occurs during communication with the
	 * database or the input provider
	 */
	private List<NightSleepData> updateUserSleepData(Database database,
			IDSSUserProfile user, LocalDate lastCompleteNight) throws Exception {
		DatabaseCriteria criteria = new DatabaseCriteria.Equal("user",
				user.getUsername());
		DataUpdated dataUpdated = database.selectOne(new SleepUpdatedTable(),
				criteria, null);
		LocalDate lastUpdate = null;
		if (dataUpdated != null) {
			lastUpdate = dataUpdated.getDate();
			// Delete records after lastUpdate from table
			// "night_sleep_periods", so the table agrees with "sleep_updated".
			// The tables might not match because of manual database edits or a
			// previous task could have been interrupted.
			DatabaseObjectMapper mapper = new DatabaseObjectMapper();
			criteria = new DatabaseCriteria.And(
					new DatabaseCriteria.Equal("user", user.getUsername()),
					new DatabaseCriteria.GreaterThan("date",
							(String)mapper.toPrimitiveDatabaseValue(lastUpdate,
							DatabaseType.DATE))
			);
			database.delete(NightSleepDataTable.NAME, criteria);
		} else {
			DateTimeFormatter parser = DateTimeFormat.forPattern("yyyy-MM-dd");
			lastUpdate = parser.parseLocalDate(PROCESSING_START_DATE);
			lastUpdate = lastUpdate.minusDays(1);
			// Delete all records from table "night_sleep_periods", so the
			// table agrees with "sleep_updated". The tables might not match
			// because of manual database edits or a previous task could have
			// been interrupted.
			criteria = new DatabaseCriteria.Equal("user", user.getUsername());
			database.delete(NightSleepDataTable.NAME, criteria);
		}
		criteria = new DatabaseCriteria.Equal("user", user.getUsername());
		DatabaseSort[] sort = new DatabaseSort[] {
				new DatabaseSort("date", true)
		};
		List<NightSleepData> history = database.select(
				new NightSleepDataTable(), criteria, 0, sort);
		if (!lastCompleteNight.isAfter(lastUpdate))
			return history;
		LocalDate startUpdate = lastUpdate.plusDays(1);
		logger.info("Retrieve sleep data for user {} from {} to {}",
				user.getUsername(),
				startUpdate.toString("yyyy-MM-dd"),
				lastCompleteNight.toString("yyyy-MM-dd"));
		LocalDate currentDate = startUpdate;
		while (!currentDate.isAfter(lastCompleteNight)) {
			NightSleepData sleepData = pullInputProvider.getNightSleepData(
					user, currentDate);
			if (sleepData == null) {
				logger.info("No sleep data for " +
						currentDate.toString("yyyy-MM-dd"));
			} else {
				history.add(sleepData);
				database.insert(NightSleepDataTable.NAME, sleepData);
				logger.info("New sleep data: " + sleepData);
			}
			if (dataUpdated == null) {
				dataUpdated = new DataUpdated();
				dataUpdated.setUser(user.getUsername());
				dataUpdated.setDate(currentDate);
				database.insert(SleepUpdatedTable.NAME, dataUpdated);
			} else {
				dataUpdated.setDate(currentDate);
				database.update(SleepUpdatedTable.NAME, dataUpdated);
			}
			currentDate = currentDate.plusDays(1);
		}
		return history;
	}

	/**
	 * Calculates the week pattern as known after the night that precedes the
	 * specified date. It adds the pattern to the database.
	 * 
	 * @param database the database
	 * @param user the user name
	 * @param date the date after the last night that should be processed
	 * @param history the activity history until the specified date (inclusive)
	 * @throws Exception if a database error occurs
	 */
	private void calculateWeekPattern(Database database, String user,
			LocalDate date, List<NightSleepData> history) throws Exception {
		// update weighted averages
		List<AverageSleepParameter> weekPattern =
				new ArrayList<AverageSleepParameter>();
		SleepParameter[] sleepParams = SleepParameter.getParameters();
		for (int weekDay = 1; weekDay <= 7; weekDay++) {
			for (SleepParameter param : sleepParams) {
				Integer weightedAvg = calcWeightedAverage(param, history,
						weekDay);
				if (weightedAvg == null)
					continue;
				AverageSleepParameter avg = new AverageSleepParameter();
				avg.setUser(user);
				avg.setWeekDay(weekDay);
				avg.setParameter(param.getName());
				avg.setWeightedAverage(weightedAvg);
				avg.setCategory(classifier.getAverageCategory(param, history,
						weightedAvg));
				avg.setUpdated(date);
				database.insert(AverageSleepParameterTable.NAME, avg);
				weekPattern.add(avg);
			}
		}
		DatabaseCriteria criteria = new DatabaseCriteria.Equal("user", user);
		DataUpdated sleepProc = database.selectOne(new SleepProcessedTable(),
				criteria, null);
		if (sleepProc != null) {
			sleepProc.setDate(date);
			database.update(SleepProcessedTable.NAME, sleepProc);
		} else {
			sleepProc = new DataUpdated();
			sleepProc.setUser(user);
			sleepProc.setDate(date);
			database.insert(SleepProcessedTable.NAME, sleepProc);
		}
		if (weekPattern.isEmpty())
			return;
		String newline = System.getProperty("line.separator");
		StringBuffer logMsg = new StringBuffer(String.format(
				"Calculated week pattern for user %s at %s:",
				user, date.toString("yyyy-MM-dd")));
		for (AverageSleepParameter avgParam : weekPattern) {
			logMsg.append(newline + "    " + avgParam);
		}
		logger.info(logMsg.toString());
	}
	
	/**
	 * Calculates the weighted average activity of a sleep parameter for the
	 * specified week day. If the history does not contain any data for that
	 * week day, then this method returns null.
	 * 
	 * @param history the sleep data history
	 * @param weekDay the week day (1 = Monday, 7 = Sunday)
	 * @return the weighted average or null
	 */
	private Integer calcWeightedAverage(SleepParameter param,
			List<NightSleepData> history, int weekDay) {
		List<NightSleepData> weekDayHistory = filterHistoryForWeekDay(history,
				weekDay);
		if (weekDayHistory.isEmpty())
			return null;
		LocalDate start = weekDayHistory.get(0).getDate();
		LocalDate end = weekDayHistory.get(weekDayHistory.size() - 1).getDate();
		LocalDate current = start;
		Iterator<NightSleepData> it = weekDayHistory.iterator();
		float sum = 0;
		int totalWeight = 0;
		NightSleepData nextItem = it.next();
		int currWeight = 1;
		while (nextItem != null && !current.isAfter(end)) {
			if (!nextItem.getDate().isAfter(current)) {
				int val = param.calculate(nextItem);
				sum += currWeight * val;
				totalWeight += currWeight;
				nextItem = null;
				if (it.hasNext())
					nextItem = it.next();
			}
			current = current.plusWeeks(1);
			currWeight++;
		}
		return Math.round(sum / totalWeight);
	}
	
	/**
	 * Filters the specified history so the result only contains dates with the
	 * specified week day.
	 * 
	 * @param history the history
	 * @param weekDay the week day (1 = Monday, 7 = Sunday)
	 * @return the filtered history
	 */
	private List<NightSleepData> filterHistoryForWeekDay(
			List<NightSleepData> history, int weekDay) {
		List<NightSleepData> result = new ArrayList<NightSleepData>();
		for (NightSleepData day : history) {
			int cmpWeekDay = day.getDate().getDayOfWeek();
			if (cmpWeekDay == weekDay)
				result.add(day);
		}
		return result;
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
			tableDefs.add(new NightSleepDataTable());
			tableDefs.add(new SleepUpdatedTable());
			tableDefs.add(new AverageSleepParameterTable());
			tableDefs.add(new SleepProcessedTable());
			return dbConn.initDatabase(dbName, tableDefs, false);
		}
	}
	
	public ResponseWakeUpRoutine getWakeUpRoutine(String userid) throws DatabaseException, IOException {
		DatabaseConnection dbConn = openDatabaseConnection();
		
		try {
			ResponseWakeUpRoutine result = new ResponseWakeUpRoutine();
			
			// Check if there is sleep data in the database for the given user
			Database database = getDatabase(dbConn);
			DatabaseCriteria criteria = new DatabaseCriteria.Equal("user", userid);
			DataUpdated dataUpdated = database.selectOne(new SleepProcessedTable(),criteria,null);
			if (dataUpdated == null) {
				return result;
			}		
			
			criteria = new DatabaseCriteria.And(
					new DatabaseCriteria.Equal("user", userid),
					new DatabaseCriteria.Equal("parameter", "bedOffTime"));
			DatabaseSort[] sort = new DatabaseSort[] {
					new DatabaseSort("updated", false)
			};
			AverageSleepParameter avg = database.selectOne(
					new AverageSleepParameterTable(), criteria, sort);
			if (avg == null)
				return result;
			
			LocalDate updated = avg.getUpdated();
			criteria = new DatabaseCriteria.And(
					new DatabaseCriteria.Equal("user", userid),
					new DatabaseCriteria.Equal("parameter", "bedOffTime"),
					new DatabaseCriteria.Equal("updated",
							updated.toString("yyyy-MM-dd"))
			);
			sort = new DatabaseSort[] { new DatabaseSort("weekDay", true) };
			List<AverageSleepParameter> avgParams = database.select(
					new AverageSleepParameterTable(), criteria, 0, sort);
			
			for(AverageSleepParameter averageSleepParameter : avgParams) {
				
				if(averageSleepParameter.getWeekDay() == 1) {
					result.setWakeUpTimeMonday(averageSleepParameter.getWeightedAverage());
				}
				
				if(averageSleepParameter.getWeekDay() == 2) {
					result.setWakeUpTimeTuesday(averageSleepParameter.getWeightedAverage());
				}
				
				if(averageSleepParameter.getWeekDay() == 3) {
					result.setWakeUpTimeWednesday(averageSleepParameter.getWeightedAverage());
				}
				
				if(averageSleepParameter.getWeekDay() == 4) {
					result.setWakeUpTimeThursday(averageSleepParameter.getWeightedAverage());
				}
				
				if(averageSleepParameter.getWeekDay() == 5) {
					result.setWakeUpTimeFriday(averageSleepParameter.getWeightedAverage());
				}
				
				if(averageSleepParameter.getWeekDay() == 6) {
					result.setWakeUpTimeSaturday(averageSleepParameter.getWeightedAverage());
				}
				
				if(averageSleepParameter.getWeekDay() == 7) {
					result.setWakeUpTimeSunday(averageSleepParameter.getWeightedAverage());
				}			
				
			}
			return result;			
			
		} finally {
			closeDatabaseConnection(dbConn);
		}
	}
}
