package eu.ewall.fusioner.fitbit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.slf4j.Logger;

import eu.ewall.fusioner.fitbit.api.FitbitAccessToken;
import eu.ewall.fusioner.fitbit.api.FitbitAccessTokenException;
import eu.ewall.fusioner.fitbit.api.FitbitActivitySummary;
import eu.ewall.fusioner.fitbit.api.FitbitApi;
import eu.ewall.fusioner.fitbit.api.FitbitDevice;
import eu.ewall.fusioner.fitbit.api.FitbitSleepPeriod;
import eu.ewall.fusioner.fitbit.api.FitbitUserProfile;
import eu.ewall.fusioner.fitbit.exception.HandledException;
import eu.ewall.fusioner.fitbit.model.ActivitySummary;
import eu.ewall.fusioner.fitbit.model.ActivitySummaryTable;
import eu.ewall.fusioner.fitbit.model.FitbitLink;
import eu.ewall.fusioner.fitbit.model.FitbitLinkTable;
import eu.ewall.fusioner.fitbit.model.FitbitUpdate;
import eu.ewall.fusioner.fitbit.model.FitbitUpdateTable;
import eu.ewall.fusioner.fitbit.model.SleepSummary;
import eu.ewall.fusioner.fitbit.model.SleepSummaryTable;
import eu.ewall.fusioner.fitbit.utils.http.HttpClientException;
import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseCriteria;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseSort;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.idss.utils.exception.ParseException;

public class FusionerFitbitSynchronizer {
	private String fitbitClientId;
	private String fitbitSecret;
	private Logger logger;
	
	public FusionerFitbitSynchronizer(String fitbitClientId,
			String fitbitSecret) {
		this.fitbitClientId = fitbitClientId;
		this.fitbitSecret = fitbitSecret;
		logger = AppComponents.getLogger(FusionerFitbitService.LOGTAG);
	}
	
	public void runSync(Database db, List<IDSSUserProfile> users)
			throws Exception {
		DatabaseSort[] sort = new DatabaseSort[] {
				new DatabaseSort("user", true)
		};
		List<FitbitLink> links = db.select(new FitbitLinkTable(), null, 0,
				sort);
		for (FitbitLink link : links) {
			IDSSUserProfile user = FusionerFitbitService.findUser(
					link.getUser(), users);
			if (user == null) {
				db.delete(FitbitLinkTable.NAME, link);
				logger.info("Deleted Fitbit link for obsolete user " +
						link.getUser());
			} else {
				logger.info("Updating Fitbit data for user " + link.getUser());
				updateData(db, user, link);
			}
		}
	}
	
	private void updateData(Database db, IDSSUserProfile user,
			FitbitLink link) {
		FitbitUserProfile profile = null;
		try {
			profile = queryFitbit(new FitbitQueryRunner<FitbitUserProfile>() {
				@Override
				public FitbitUserProfile runQuery(FitbitApi api)
						throws FitbitAccessTokenException, HttpClientException,
						ParseException, IOException {
					return api.getUserProfile();
				}
			}, db, link);
		} catch (HandledException ex) {
			return;
		}
		DateTimeZone tz;
		if (profile != null && profile.getTimezone() != null) {
			tz = DateTimeZone.forID(profile.getTimezone());
			logger.info(String.format("Fitbit time zone for user %s: %s",
					link.getUser(), tz.getID()));
		} else {
			tz = user.getTimeZone();
			logger.info(String.format(
					"Fitbit time zone for user %s not defined, using default: %s",
					link.getUser(), tz.getID()));
		}
		List<FitbitDevice> devices = new ArrayList<FitbitDevice>();
		try {
			devices = queryFitbit(new FitbitQueryRunner<List<FitbitDevice>>() {
				@Override
				public List<FitbitDevice> runQuery(FitbitApi api)
						throws FitbitAccessTokenException, HttpClientException,
						ParseException, IOException {
					return api.getDevices();
				}
			}, db, link);
		} catch (HandledException ex) {
			return;
		}
		FitbitDevice tracker = findTracker(devices);
		if (tracker == null) {
			logger.info(String.format(
					"Fitbit link is removed for user %s, because no tracker was found",
					link.getUser()));
			try {
				db.delete(FitbitLinkTable.NAME, link);
			} catch (DatabaseException dbEx) {
				logger.error("Can't remove Fitbit link: " + dbEx.getMessage(),
						dbEx);
				return;
			}
			return;
		}
		DateTime lastTrackerSyncTime = null;
		if (tracker.getLastSyncTime() != null)
			lastTrackerSyncTime = tracker.getLastSyncTime().toDateTime(tz);
		if (lastTrackerSyncTime == null) {
			logger.info(String.format(
					"Fitbit tracker for user %s has not been synchronized yet",
					link.getUser()));
			return;
		}
		logger.info(String.format(
				"Fitbit tracker for user %s was last synchronized at %s",
				link.getUser(),
				lastTrackerSyncTime.toString("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")));
		DatabaseCriteria criteria = new DatabaseCriteria.Equal("user",
				link.getUser());
		FitbitUpdate lastUpdate;
		try {
			lastUpdate = db.selectOne(new FitbitUpdateTable(), criteria, null);
		} catch (DatabaseException ex) {
			logger.error("Can't query table " + FitbitUpdateTable.NAME + ": " +
					ex.getMessage(), ex);
			return;
		}
		DateTime now = new DateTime(tz);
		DateTime linkTime = new DateTime(link.getLinkTime(), tz);
		LocalDate startDate = linkTime.toLocalDate();
		if (lastUpdate != null) {
			DateTime prevTrackerSyncTime = lastUpdate.getLastTrackerSyncTime();
			if (!lastTrackerSyncTime.isAfter(prevTrackerSyncTime)) {
				logger.info(String.format(
						"Fitbit tracker for user %s not synchronized after last update",
						link.getUser()));
				return;
			}
			startDate = prevTrackerSyncTime.toLocalDate();
		}
		LocalDate endDate = now.toLocalDate();
		LocalDate lastTrackerSyncDate = lastTrackerSyncTime.toLocalDate();
		if (lastTrackerSyncDate.isBefore(endDate))
			endDate = lastTrackerSyncDate;
		if (endDate.isBefore(startDate)) {
			logger.info(String.format(
					"No Fitbit data to update for user %s",
					link.getUser()));
			return;
		}
		logger.info(String.format(
				"Retrieve Fitbit data for user %s between %s and %s",
				link.getUser(), startDate.toString("yyyy-MM-dd"),
				endDate.toString("yyyy-MM-dd")));
		try {
			updateActivitySummaries(db, link, startDate, endDate);
			updateSleepSummaries(db, link, startDate, endDate, tz);
		} catch (HandledException ex) {
			return;
		}
		FitbitUpdate newUpdate;
		if (lastUpdate == null) {
			newUpdate = new FitbitUpdate();
			newUpdate.setUser(link.getUser());
		} else {
			newUpdate = lastUpdate;
		}
		newUpdate.setLastRetrieveTime(now);
		newUpdate.setLastTrackerSyncTime(lastTrackerSyncTime);
		try {
			if (lastUpdate == null)
				db.insert(FitbitUpdateTable.NAME, newUpdate);
			else
				db.update(FitbitUpdateTable.NAME, newUpdate);
		} catch (DatabaseException ex) {
			logger.error("Can't write to table " + FitbitUpdateTable.NAME +
					": " + ex.getMessage(), ex);
			return;
		}
		logger.info("Updated Fitbit data for user " + link.getUser());
	}
	
	private FitbitDevice findTracker(List<FitbitDevice> devices) {
		for (FitbitDevice device : devices) {
			if (device.getType().toLowerCase().equals("tracker"))
				return device;
		}
		return null;
	}
	
	private void updateActivitySummaries(Database db, FitbitLink link,
			LocalDate startDate, LocalDate endDate)
			throws HandledException {
		LocalDate currDate = startDate;
		while (!currDate.isAfter(endDate)) {
			FitbitActivitySummary fitbitSummary =
					retrieveFitbitActivitySummary(currDate, db, link);
			try {
				saveActivitySummary(db, link, currDate,
						currDate.isBefore(endDate), fitbitSummary);
			} catch (DatabaseException ex) {
				logger.error("Can't save activity summary: " + ex.getMessage(),
						ex);
				throw new HandledException();
			}
			currDate = currDate.plusDays(1);
		}
	}
	
	private FitbitActivitySummary retrieveFitbitActivitySummary(
			final LocalDate date, Database db, FitbitLink link)
			throws HandledException {
		return queryFitbit(new FitbitQueryRunner<FitbitActivitySummary>() {
			@Override
			public FitbitActivitySummary runQuery(FitbitApi api)
					throws FitbitAccessTokenException, HttpClientException,
					ParseException, IOException {
				return api.getActivitySummary(date);
			}
		}, db, link);
	}
	
	private void saveActivitySummary(Database db, FitbitLink link,
			LocalDate date, boolean complete,
			FitbitActivitySummary fitbitSummary) throws DatabaseException {
		ActivitySummary summary = new ActivitySummary();
		summary.setUser(link.getUser());
		summary.setDate(date);
		summary.setActivityCalories(fitbitSummary.getActivityCalories());
		summary.setCaloriesBMR(fitbitSummary.getCaloriesBMR());
		summary.setCaloriesOut(fitbitSummary.getCaloriesOut());
		summary.setDistance(fitbitSummary.findDistance("total"));
		summary.setElevation(fitbitSummary.getElevation());
		summary.setFloors(fitbitSummary.getFloors());
		summary.setSteps(fitbitSummary.getSteps());
		summary.setSedentaryMinutes(fitbitSummary.getSedentaryMinutes());
		summary.setLightlyActiveMinutes(
				fitbitSummary.getLightlyActiveMinutes());
		summary.setFairlyActiveMinutes(fitbitSummary.getFairlyActiveMinutes());
		summary.setVeryActiveMinutes(fitbitSummary.getVeryActiveMinutes());
		summary.setComplete(complete);
		DatabaseCriteria criteria = new DatabaseCriteria.And(
				new DatabaseCriteria.Equal("user", link.getUser()),
				new DatabaseCriteria.Equal("date", date.toString("yyyy-MM-dd"))
		);
		db.delete(ActivitySummaryTable.NAME, criteria);
		db.insert(ActivitySummaryTable.NAME, summary);
	}
	
	private void updateSleepSummaries(Database db, FitbitLink link,
			LocalDate startDate, LocalDate endDate, DateTimeZone tz)
			throws HandledException {
		LocalDate currDate = startDate;
		while (!currDate.isAfter(endDate)) {
			List<FitbitSleepPeriod> fitbitSleep =
					retrieveFitbitSleepSummary(currDate, db, link);
			try {
				for (FitbitSleepPeriod period : fitbitSleep) {
					saveSleepSummary(db, link, currDate,
							currDate.isBefore(endDate), period, tz);
				}
			} catch (DatabaseException ex) {
				logger.error("Can't save sleep summary: " + ex.getMessage(),
						ex);
				throw new HandledException();
			}
			currDate = currDate.plusDays(1);
		}
	}

	private void saveSleepSummary(Database db, FitbitLink link,
			LocalDate date, boolean complete,
			FitbitSleepPeriod fitbitSleep, DateTimeZone tz)
			throws DatabaseException {
		SleepSummary summary = new SleepSummary();
		summary.setUser(link.getUser());
		summary.setDate(date);
		DateTime startTime = fitbitSleep.getStartTime().toDateTime(tz);
		summary.setStartTime(startTime.getMillis());
		summary.setIsoStartTime(startTime);
		summary.setDuration(fitbitSleep.getDuration());
		summary.setMainSleep(fitbitSleep.getIsMainSleep());
		summary.setEfficiency(fitbitSleep.getEfficiency());
		summary.setMinutesToFallAsleep(fitbitSleep.getMinutesToFallAsleep());
		summary.setMinutesAsleep(fitbitSleep.getMinutesAsleep());
		summary.setMinutesAwake(fitbitSleep.getMinutesAwake());
		summary.setMinutesAfterWakeup(fitbitSleep.getMinutesAfterWakeup());
		summary.setAwakeCount(fitbitSleep.getAwakeCount());
		summary.setAwakeDuration(fitbitSleep.getAwakeDuration());
		summary.setRestlessCount(fitbitSleep.getRestlessCount());
		summary.setRestlessDuration(fitbitSleep.getRestlessDuration());
		summary.setTimeInBed(fitbitSleep.getTimeInBed());
		summary.setComplete(complete);
		DatabaseCriteria criteria = new DatabaseCriteria.And(
				new DatabaseCriteria.Equal("user", link.getUser()),
				new DatabaseCriteria.Equal("date", date.toString("yyyy-MM-dd")),
				new DatabaseCriteria.Equal("startTime", startTime.getMillis())
		);
		db.delete(SleepSummaryTable.NAME, criteria);
		db.insert(SleepSummaryTable.NAME, summary);
	}
	
	private List<FitbitSleepPeriod> retrieveFitbitSleepSummary(
			final LocalDate date, Database db, FitbitLink link)
			throws HandledException {
		return queryFitbit(new FitbitQueryRunner<List<FitbitSleepPeriod>>() {
			@Override
			public List<FitbitSleepPeriod> runQuery(FitbitApi api)
					throws FitbitAccessTokenException, HttpClientException,
					ParseException, IOException {
				return api.getSleepSummary(date);
			}
		}, db, link);
	}

	/**
	 * Tries to run Fitbit queries. If Fitbit says that the access token needs
	 * to be refreshed, this method will try to refresh the token and store the
	 * new token in the database. If the token can't be refreshed, this method
	 * removes the specified Fitbit links from the database.
	 * 
	 * <p>If any error occurs (token could not be refreshed, Fitbit returned an
	 * error, response could not be parsed, network error, database error),
	 * then this method logs an error message and throws a
	 * HandledException.</p>
	 * 
	 * @param queryRunner the query runner
	 * @param db the database
	 * @param link the Fitbit link
	 * @return the query result
	 * @throws HandledException if any error occurs
	 */
	private <T> T queryFitbit(FitbitQueryRunner<T> queryRunner,
			Database db, FitbitLink link)
			throws HandledException {
		FitbitApi api = new FitbitApi(fitbitClientId, fitbitSecret,
				link.getTokenType(), link.getAccessToken());
		try {
			return queryRunner.runQuery(api);
		} catch (FitbitAccessTokenException ex) {
			logger.info("Refresh Fitbit access token for user " +
					link.getUser());
		} catch (HttpClientException ex) {
			handleFitbitQueryException(ex, db, link);
			throw new HandledException();
		} catch (Exception ex) {
			logger.error(String.format("Fitbit query failed for user %s: %s",
					link.getUser(), ex.getMessage()), ex);
			throw new HandledException();
		}
		FitbitAccessToken newToken;
		try {
			newToken = api.refreshAccessToken(link.getRefreshToken());
		} catch (FitbitAccessTokenException ex) {
			logger.error(String.format(
					"Fitbit link is removed for user %s, because access token is invalid and could not be refreshed",
					link.getUser()));
			try {
				db.delete(FitbitLinkTable.NAME, link);
			} catch (DatabaseException dbEx) {
				logger.error("Can't remove Fitbit link: " + dbEx.getMessage(),
						dbEx);
			}
			throw new HandledException();
		} catch (Exception ex) {
			logger.error(String.format(
					"Refresh access token failed for user %s: %s",
					link.getUser(), ex.getMessage()), ex);
			throw new HandledException();
		}
		try {
			link.setAccessToken(newToken.getAccess_token());
			link.setExpires(System.currentTimeMillis() +
					newToken.getExpires_in());
			link.setRefreshToken(newToken.getRefresh_token());
			link.setTokenType(newToken.getToken_type());
			db.update(FitbitLinkTable.NAME, link);
		} catch (Exception ex) {
			logger.error("Database error: " + ex.getMessage(), ex);
			throw new HandledException();
		}
		try {
			return queryRunner.runQuery(api);
		} catch (FitbitAccessTokenException ex) {
			logger.error(String.format(
					"Fitbit link is removed for user %s, because access token is invalid and could not be refreshed",
					link.getUser()));
			try {
				db.delete(FitbitLinkTable.NAME, link);
			} catch (DatabaseException dbEx) {
				logger.error("Can't remove Fitbit link: " + dbEx.getMessage(),
						dbEx);
			}
			throw new HandledException();
		} catch (HttpClientException ex) {
			handleFitbitQueryException(ex, db, link);
			throw new HandledException();
		} catch (Exception ex) {
			logger.error(String.format("Fitbit query failed for user %s: %s",
					link.getUser(), ex.getMessage()), ex);
			throw new HandledException();
		}
	}
	
	/**
	 * Processes an error returned by Fitbit. It logs an error message. In case
	 * of an insufficient scope error, it will try to remove the Fitbit link.
	 * 
	 * @param ex the exception
	 * @param db the database
	 * @param link the Fitbit link
	 */
	private void handleFitbitQueryException(HttpClientException ex,
			Database db, FitbitLink link) {
		if (FitbitApi.isInsufficientScopeException(ex)) {
			logger.error(String.format(
					"Fitbit link is removed for user %s because of insufficient scope in requested authorization: %s",
					link.getUser(), ex.getMessage()));
			try {
				db.delete(FitbitLinkTable.NAME, link);
			} catch (Exception dbEx) {
				logger.error("Can't remove Fitbit links: " + dbEx.getMessage(),
						dbEx);
			}
			return;
		}
		logger.error(String.format("Fitbit returned an error for user %s: %s",
				link.getUser(), ex.getMessage()));
	}
	
	private interface FitbitQueryRunner<T> {
		T runQuery(FitbitApi api) throws FitbitAccessTokenException,
				HttpClientException, ParseException, IOException;
	}
}
