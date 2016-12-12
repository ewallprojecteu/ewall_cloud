package eu.ewall.platform.idss.service.lr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseCriteria;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseObjectMapper;
import eu.ewall.platform.idss.dao.DatabaseSort;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.AverageDataParameter;
import eu.ewall.platform.idss.service.model.common.DataUpdated;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.idss.utils.datetime.VirtualClock;

/**
 * Base class for a lifestyle reasoner that calculates weighted averages for a
 * set of source data parameters for each day of the week. A scheduled service
 * should call {@link #updateWeekPatterns(IDSSUserProfile, Database)
 * updateWeekPatterns()} regularly, for example every hour.
 * 
 * @author Dennis Hofs (RRD)
 *
 * @param <T> the source data class
 */
public abstract class WeekDayLifestyleReasoner<T extends LRSourceDayData> {

	/**
	 * Checks if enough source data is available to calculate new week
	 * patterns. If so it will read the new source data and calculate the week
	 * pattern(s). The source data and week patterns are stored in the
	 * database.
	 * 
	 * @param user the user name
	 * @param database the database
	 * @throws DatabaseException if a database error occurs
	 * @throws IOException if an error occurs while reading the source data
	 */
	public void updateWeekPatterns(IDSSUserProfile user, Database database)
			throws DatabaseException, IOException {
		VirtualClock clock = AppComponents.get(VirtualClock.class);
		DateTime now = clock.getTime().withZone(user.getTimeZone());
		DatabaseTableDef<DataUpdated> patternUpdatedTable =
				getWeekPatternUpdatedTable();
		DatabaseTableDef<AverageDataParameter> averagesTable =
				getAveragesTable();
		DatabaseCriteria criteria = new DatabaseCriteria.Equal("user",
				user.getUsername());
		DataUpdated patternUpdate = database.selectOne(patternUpdatedTable,
				criteria, null);
		LocalDate lastUpdate = null;
		if (patternUpdate != null) {
			lastUpdate = patternUpdate.getDate();
			// Delete records after lastUpdate from averagesTable, so the table
			// agrees with patternUpdatedTable. The tables might not match
			// because of manual database edits or a previous task could have
			// been interrupted.
			DatabaseObjectMapper mapper = new DatabaseObjectMapper();
			criteria = new DatabaseCriteria.And(
					new DatabaseCriteria.Equal("user", user.getUsername()),
					new DatabaseCriteria.GreaterThan("updated",
							(String)mapper.toPrimitiveDatabaseValue(lastUpdate,
							DatabaseType.DATE))
			);
			database.delete(averagesTable.getName(), criteria);
		} else {
			lastUpdate = getStartDate(user, database, now).minusDays(1);
			// Delete all records from averagesTable, so the table agrees with
			// patternUpdatedTable. The tables might not match because of
			// manual database edits or a previous task could have been
			// interrupted.
			criteria = new DatabaseCriteria.Equal("user", user.getUsername());
			database.delete(averagesTable.getName(), criteria);
		}
		LocalDate lastCompleteDate = getLastCompleteSourceDate(user, database,
				now);
		if (lastCompleteDate == null || !lastCompleteDate.isAfter(lastUpdate))
			return;
		List<T> history = updateSourceData(database, user, now,
				lastCompleteDate);
		int nextHistoryIndex = 0;
		List<T> dateHistory = new ArrayList<T>();
		LocalDate date = lastUpdate.plusDays(1);
		while (!date.isAfter(lastCompleteDate)) {
			// add data until date (inclusive)
			for (int i = nextHistoryIndex; i < history.size(); i++) {
				T dayData = history.get(i);
				if (dayData.getDate().isAfter(date))
					break;
				dateHistory.add(dayData);
				nextHistoryIndex++;
			}
			calculateWeekPattern(database, user.getUsername(), date,
					dateHistory);
			date = date.plusDays(1);
		}
	}

	/**
	 * Retrieves new source data until and including the specified date. That
	 * is the date until which the source data should be complete, as obtained
	 * from {@link #getLastCompleteSourceDate(DateTime)
	 * getLastCompleteSourceDate()}. It reads data from the source and adds the
	 * (possibly derived) data to the local database. It returns the complete
	 * history of the user.
	 * 
	 * <p>If there is no source data in the database yet, it will process all
	 * data starting at {@link #getStartDate() getStartDate()}.</p>
	 * 
	 * @param database the database
	 * @param user the user name
	 * @param now the current time in the time zone of the user
	 * @param lastCompleteDate the date until and including which the source
	 * data is complete
	 * @return the data history
	 * @throws DatabaseException if a database error occurs
	 * @throws IOException if an error occurs while reading source data
	 */
	private List<T> updateSourceData(Database database, IDSSUserProfile user,
			DateTime now, LocalDate lastCompleteDate) throws DatabaseException,
			IOException {
		DatabaseCriteria criteria = new DatabaseCriteria.Equal("user",
				user.getUsername());
		DatabaseTableDef<T> dataTable = getSourceDataTable();
		DatabaseTableDef<DataUpdated> dataUpdatedTable =
				getSourceDataUpdatedTable();
		DataUpdated dataUpdated = database.selectOne(dataUpdatedTable,
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
			database.delete(dataTable.getName(), criteria);
		} else {
			lastUpdate = getStartDate(user, database, now).minusDays(1);
			// Delete all records from table "night_sleep_periods", so the
			// table agrees with "sleep_updated". The tables might not match
			// because of manual database edits or a previous task could have
			// been interrupted.
			criteria = new DatabaseCriteria.Equal("user", user.getUsername());
			database.delete(dataTable.getName(), criteria);
		}
		criteria = new DatabaseCriteria.Equal("user", user.getUsername());
		DatabaseSort[] sort = new DatabaseSort[] {
				new DatabaseSort("date", true)
		};
		List<T> history = database.select(dataTable, criteria, 0, sort);
		if (!lastCompleteDate.isAfter(lastUpdate))
			return history;
		LocalDate startUpdate = lastUpdate.plusDays(1);
		Logger logger = AppComponents.getLogger(getLogTag());
		logger.info("Retrieve source data for user {} from {} to {}",
				user.getUsername(),
				startUpdate.toString("yyyy-MM-dd"),
				lastCompleteDate.toString("yyyy-MM-dd"));
		LocalDate currentDate = startUpdate;
		while (!currentDate.isAfter(lastCompleteDate)) {
			T data = readSourceData(user, database, currentDate);
			if (data == null) {
				logger.info("No source data for " +
						currentDate.toString("yyyy-MM-dd"));
			} else {
				history.add(data);
				database.insert(dataTable.getName(), data);
				logger.info("New source data: " + data);
			}
			if (dataUpdated == null) {
				dataUpdated = new DataUpdated();
				dataUpdated.setUser(user.getUsername());
				dataUpdated.setDate(currentDate);
				database.insert(dataUpdatedTable.getName(), dataUpdated);
			} else {
				dataUpdated.setDate(currentDate);
				database.update(dataUpdatedTable.getName(), dataUpdated);
			}
			currentDate = currentDate.plusDays(1);
		}
		return history;
	}

	/**
	 * Calculates the week pattern as known after the specified date. It adds
	 * the pattern to the database.
	 * 
	 * @param database the database
	 * @param user the user name
	 * @param date the date that should be processed
	 * @param history the data history until the specified date (inclusive)
	 * @throws DatabaseException if a database error occurs
	 */
	private void calculateWeekPattern(Database database, String user,
			LocalDate date, List<T> history) throws DatabaseException {
		// update weighted averages
		List<AverageDataParameter> weekPattern =
				new ArrayList<AverageDataParameter>();
		List<WeekPatternParameter<T>> params = getWeekPatternParameters();
		DatabaseTableDef<AverageDataParameter> averagesTable =
				getAveragesTable();
		LRDataClassifier<T> classifier = getClassifier();
		for (int weekDay = 1; weekDay <= 7; weekDay++) {
			for (WeekPatternParameter<T> param : params) {
				Float weightedAvg = calcWeightedAverage(param, history,
						weekDay);
				if (weightedAvg == null)
					continue;
				AverageDataParameter avg = new AverageDataParameter();
				avg.setUser(user);
				avg.setWeekDay(weekDay);
				avg.setParameter(param.getName());
				avg.setWeightedAverage(weightedAvg);
				avg.setCategory(classifier.classify(param, history,
						weightedAvg));
				avg.setUpdated(date);
				database.insert(averagesTable.getName(), avg);
				weekPattern.add(avg);
			}
		}
		DatabaseCriteria criteria = new DatabaseCriteria.Equal("user", user);
		DatabaseTableDef<DataUpdated> patternUpdatedTable =
				getWeekPatternUpdatedTable();
		DataUpdated patternUpdate = database.selectOne(patternUpdatedTable,
				criteria, null);
		if (patternUpdate != null) {
			patternUpdate.setDate(date);
			database.update(patternUpdatedTable.getName(), patternUpdate);
		} else {
			patternUpdate = new DataUpdated();
			patternUpdate.setUser(user);
			patternUpdate.setDate(date);
			database.insert(patternUpdatedTable.getName(), patternUpdate);
		}
		if (weekPattern.isEmpty())
			return;
		String newline = System.getProperty("line.separator");
		StringBuffer logMsg = new StringBuffer(String.format(
				"Calculated week pattern for user %s at %s:",
				user, date.toString("yyyy-MM-dd")));
		for (AverageDataParameter avgParam : weekPattern) {
			logMsg.append(newline + "    " + avgParam);
		}
		Logger logger = AppComponents.getLogger(getLogTag());
		logger.info(logMsg.toString());
	}
	
	/**
	 * Calculates the weighted average activity of a data parameter for the
	 * specified week day. If the history does not contain any data for that
	 * week day, then this method returns null.
	 * 
	 * @param history the data history
	 * @param weekDay the week day (1 = Monday, 7 = Sunday)
	 * @return the weighted average or null
	 */
	private Float calcWeightedAverage(WeekPatternParameter<T> param,
			List<T> history, int weekDay) {
		List<T> weekDayHistory = filterHistoryForWeekDay(history, weekDay);
		if (weekDayHistory.isEmpty())
			return null;
		LocalDate start = weekDayHistory.get(0).getDate();
		LocalDate end = weekDayHistory.get(weekDayHistory.size() - 1)
				.getDate();
		LocalDate current = start;
		Iterator<T> it = weekDayHistory.iterator();
		float sum = 0;
		int totalWeight = 0;
		T nextItem = it.next();
		int currWeight = 1;
		while (nextItem != null && !current.isAfter(end)) {
			if (!nextItem.getDate().isAfter(current)) {
				float val = param.calculate(nextItem);
				sum += currWeight * val;
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
	private List<T> filterHistoryForWeekDay(List<T> history, int weekDay) {
		List<T> result = new ArrayList<T>();
		for (T day : history) {
			int cmpWeekDay = day.getDate().getDayOfWeek();
			if (cmpWeekDay == weekDay)
				result.add(day);
		}
		return result;
	}
	
	/**
	 * Returns the log tag. This is used to log when new source data is
	 * retrieved and when new week patterns are created.
	 * 
	 * @return the log tag
	 */
	protected abstract String getLogTag();
	
	/**
	 * Returns the classifier that classifies weighted averages as below
	 * normal, normal or above normal.
	 * 
	 * @return the classifier
	 */
	protected abstract LRDataClassifier<T> getClassifier();

	/**
	 * Returns the start date from which source data should be processed.
	 * 
	 * @param user the user
	 * @param database the database
	 * @param now the current time in the time zone of the user
	 * @return the start date
	 * @throws DatabaseException if a database error occurs
	 */
	protected abstract LocalDate getStartDate(IDSSUserProfile user,
			Database database, DateTime now) throws DatabaseException;
	
	/**
	 * Returns the last date for which the source data is complete. If there is
	 * no complete date yet, this method returns null.
	 * 
	 * @param user the user
	 * @param database the database
	 * @param now the current time in the time zone of the user
	 * @return the last date for which the source data is complete, or null
	 * @throws DatabaseException if a database error occurs
	 */
	protected abstract LocalDate getLastCompleteSourceDate(
			IDSSUserProfile user, Database database, DateTime now)
			throws DatabaseException;

	/**
	 * Reads the source data for the specified date. This is only called for
	 * complete dates (see {@link #getLastCompleteSourceDate()
	 * getLastCompleteSourceDate()}). If there is no data at the specified
	 * date, this method returns null.
	 * 
	 * @param user the user
	 * @param database the database
	 * @param date the date
	 * @return the source data or null
	 * @throws DatabaseException if a database error occurs
	 * @throws IOException if a reading error occurs
	 */
	protected abstract T readSourceData(IDSSUserProfile user,
			Database database, LocalDate date) throws DatabaseException,
			IOException;

	/**
	 * Returns the database table that stores until and including what date
	 * source data has been retrieved and stored in the local database. Only
	 * complete dates are read (see {@link #getLastCompleteSourceDate()
	 * getLastCompleteSourceDate()}). The source data is stored in {@link
	 * #getSourceDataTable() getSourceDataTable()}. Since a day may have no
	 * data, the last record in the update table may be later than the last
	 * record in the data table.
	 * 
	 * @return the database table for source data updates
	 */
	protected abstract DatabaseTableDef<DataUpdated>
			getSourceDataUpdatedTable();
	
	/**
	 * Returns the database table with source data. It has zero or one record
	 * for each date. The date for which source data was last retrieved can be
	 * obtained from {@link #getSourceDataUpdatedTable()
	 * getSourceDataUpdatedTable()}. Since a day may have no data, the last
	 * record in the update table may be later than the last record in the data
	 * table.
	 * 
	 * @return the database table with source data
	 */
	protected abstract DatabaseTableDef<T> getSourceDataTable();
	
	/**
	 * Returns the database table that stores until and including what date
	 * source data has been processed to generate a week pattern. A week
	 * pattern is generated every time the source data of a complete day
	 * becomes available.
	 * 
	 * @return the database table for week pattern updates
	 */
	protected abstract DatabaseTableDef<DataUpdated>
			getWeekPatternUpdatedTable();
	
	/**
	 * Returns the database table with week patterns. A week pattern consists
	 * of a set of {@link AverageDataParameter AverageDataParameter} for the
	 * parameters defined by {@link #getWeekPatternParameters()
	 * getWeekPatternParameters()} and for every week day. The table contains
	 * multiple week patterns. A week pattern is calculated every time the
	 * source data of a complete day becomes available. The "updated" property
	 * of an {@link AverageDataParameter AverageDataParameter} indicates what
	 * source data was used to calculate the week pattern.
	 * 
	 * @return the database table with week patterns
	 */
	protected abstract DatabaseTableDef<AverageDataParameter>
			getAveragesTable();

	/**
	 * Returns the parameters for which the lifestyle reasoner should calculate
	 * a weighted average to include in a week pattern.
	 * 
	 * @return the parameters
	 */
	protected abstract List<WeekPatternParameter<T>>
			getWeekPatternParameters();
}
