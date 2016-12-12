package eu.ewall.platform.userinteractionlogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.profile.UserRole;
import eu.ewall.platform.idss.CommonsUserReader;
import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseConnection;
import eu.ewall.platform.idss.dao.DatabaseCriteria;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseSort;
import eu.ewall.platform.idss.service.RemoteMethodException;
import eu.ewall.platform.idss.service.ScheduledService;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.idss.utils.datetime.VirtualClock;
import eu.ewall.platform.idss.utils.exception.FatalException;
import eu.ewall.platform.userinteractionlogger.model.DailyInteractionLog;
import eu.ewall.platform.userinteractionlogger.model.DailyInteractionLogTable;
import eu.ewall.platform.userinteractionlogger.model.UserInteractionLog;
import eu.ewall.platform.userinteractionlogger.model.UserInteractionLogTable;

public class InteractionReasonerService extends ScheduledService {
	
	public static final String LOGTAG = "InteractionReasonerService";
	
	private Map<String,IDSSUserProfile> idssUserProfiles;
	
	private UILConfiguration config;
	private RestOperations ewallClient;
	
	public InteractionReasonerService(UILConfiguration config, RestOperations ewallClient) {
		this.config = config;
		this.ewallClient = ewallClient;
		idssUserProfiles = new HashMap<String,IDSSUserProfile>();
	}
	
	@Override
	public void runTask() throws FatalException, Exception {
		updateUserInfo();
		updateDailyInteractions();
	}
	
	/**
	 * SERVICE FUNCTION: Called on each scheduled run.
	 * 
	 * @throws IOException
	 * @throws RemoteMethodException
	 * @throws Exception
	 */
	private void updateDailyInteractions() throws IOException, RemoteMethodException, Exception {
		// For each user in the system
		Iterator<String> it = idssUserProfiles.keySet().iterator();
		while(it.hasNext()) {
			String username = it.next();
			IDSSUserProfile idssUserProfile = idssUserProfiles.get(username);
			
			// Get the current date (in their timezone) and the day before (yesterday)
			VirtualClock clock = VirtualClock.getInstance();
			DateTime dateToday = clock.getTime().withZone(idssUserProfile.getTimeZone());
			DateTime dateYesterday = dateToday.minusDays(1);
			
			// Get all log rules for that user for yesterday and add them all up
			int day = dateYesterday.getDayOfMonth();
			int month = dateYesterday.getMonthOfYear();
			int year = dateYesterday.getYear();
			
			getAndStoreDailyInteractionLog(username,day,month,year);
		}
	};
	
	public DailyInteractionLog getAndStoreDailyInteractionLog(String username,
			int day, int month, int year) throws DatabaseException,
			IOException {
		DailyInteractionLog dailyInteractionLog = null;
		boolean updateDailyLog = true;
		
		DatabaseAccessPoint dap = AppComponents.get(DatabaseAccessPoint.class);
		DatabaseConnection databaseConnection = dap.openDatabase();
		try {
			Database database = dap.getDatabase(databaseConnection);
			DatabaseCriteria criteria;
			
			// Check if a complete DailyInteractionLog is already available
			DailyInteractionLogTable dailyInteractionLogTable = new DailyInteractionLogTable();
			criteria = new DatabaseCriteria.And(
					new DatabaseCriteria.Equal("username",username),
					new DatabaseCriteria.Equal("day",day),
					new DatabaseCriteria.Equal("month",month),
					new DatabaseCriteria.Equal("year",year)
					);
			dailyInteractionLog = database.selectOne(dailyInteractionLogTable,criteria,null);
			if(dailyInteractionLog != null) {
				if(dailyInteractionLog.getComplete()) {
					return dailyInteractionLog;
				}
			} else {
				dailyInteractionLog = new DailyInteractionLog(username,day,month,year);
				updateDailyLog = false;
			}
			
			// If a complete interaction log is not available for the given username
			// and date parameters, generate one.
						
			criteria = new DatabaseCriteria.Equal("user", username);
			UserKeyTable userKeyTable = new UserKeyTable();

			UserKey userKey = database.selectOne(userKeyTable, criteria, null);
			if (userKey == null) {
				// No logging information known for this user at all, return empty log and do not store
				return dailyInteractionLog;
			} else {
				// Analyze the user interaction log table
				UserInteractionLogTable uilTable = dap.initLogTable(database, username);
				String dateString = day+"-"+month+"-"+year;
				criteria = new DatabaseCriteria.Equal("dateString",dateString);
				
				// Set total number of interactions for this day
				int totalInteractions = database.count(uilTable.getName(), criteria);
				dailyInteractionLog.setInteractions(totalInteractions);
				
				// Check if this daily interaction log should be considered "final"
				if(isDayFinished(username,day,month,year)) {
					dailyInteractionLog.setComplete(true);
				}				
				
				// Store daily Interaction Log object
				if(updateDailyLog) {
					database.update(DailyInteractionLogTable.TABLE_NAME, dailyInteractionLog);
				} else {
					database.insert(DailyInteractionLogTable.TABLE_NAME, dailyInteractionLog);
				}
				return dailyInteractionLog;
			}
		} finally {
			dap.closeDatabase(databaseConnection);
		}
	}
	
	public DateTime getLastInteractionWithApp(String username, String app)
			throws DatabaseException, IOException {
		DatabaseAccessPoint dap = AppComponents.get(DatabaseAccessPoint.class);
		DatabaseConnection dbConn = dap.openDatabase();
		try {
			Database db = dap.getDatabase(dbConn);
			UserInteractionLogTable table = dap.initLogTable(db, username);
			DatabaseCriteria criteria = null;
			if (app != null) {
				criteria = new DatabaseCriteria.Or(
					new DatabaseCriteria.And(
						new DatabaseCriteria.Equal(
								"applicationName", "mainScreen"),
						new DatabaseCriteria.Equal("buttonId", app)
					),
					new DatabaseCriteria.Equal("applicationName", app)
				);
			}
			DatabaseSort[] sort = new DatabaseSort[] {
					new DatabaseSort("utcTime", false)
			};
			UserInteractionLog log = db.selectOne(table, criteria, sort);
			if (log == null)
				return null;
			return log.getTzTime();
		} finally {
			dap.closeDatabase(dbConn);
		}
	}
	
	public int getInteractionCountWithApp(IDSSUserProfile user, LocalDate date,
			String app) throws DatabaseException, IOException {
		DatabaseAccessPoint dap = AppComponents.get(DatabaseAccessPoint.class);
		DatabaseConnection dbConn = dap.openDatabase();
		try {
			Database db = dap.getDatabase(dbConn);
			UserInteractionLogTable table = dap.initLogTable(db,
					user.getUsername());
			List<DatabaseCriteria> andCriteria = new ArrayList<DatabaseCriteria>();
			DateTime startTime = date.toDateTimeAtStartOfDay(
					user.getTimeZone());
			DateTime endTime = date.plusDays(1).toDateTimeAtStartOfDay(
					user.getTimeZone());
			andCriteria.add(new DatabaseCriteria.GreaterEqual(
					"utcTime", startTime.getMillis()));
			andCriteria.add(new DatabaseCriteria.LessThan(
					"utcTime", endTime.getMillis()));
			if (app != null) {
				DatabaseCriteria selAppCriteria = new DatabaseCriteria.Or(
					new DatabaseCriteria.And(
						new DatabaseCriteria.Equal(
								"applicationName", "mainScreen"),
						new DatabaseCriteria.Equal("buttonId", app)),
					new DatabaseCriteria.Equal("applicationName", app)
				);
				andCriteria.add(selAppCriteria);
			}
			DatabaseCriteria criteria = new DatabaseCriteria.And(
					andCriteria.toArray(
					new DatabaseCriteria[andCriteria.size()]));
			return db.count(table.getName(), criteria);
		} finally {
			dap.closeDatabase(dbConn);
		}
	}
	
	/**
	 * SERVICE FUNCTION: Called on each scheduled run.
	 * 
	 * @throws IOException
	 * @throws RemoteMethodException
	 * @throws Exception
	 */
	private void updateUserInfo() throws IOException, RemoteMethodException, Exception {
		idssUserProfiles = new HashMap<String,IDSSUserProfile>();
		String url = config.getProfilingServerUrl() + "/users/";
		List<?> objList = ewallClient.getForObject(url, List.class);
		ObjectMapper mapper = new ObjectMapper();
		for (Object obj : objList) {
			User user = mapper.convertValue(obj, User.class);
			
			// InteractionReasoner service only runs for primary users
			if(user.getUserRole().toString().equals(UserRole.PRIMARY_USER.toString())) {
				idssUserProfiles.put(user.getUsername(),CommonsUserReader.readUser(user));
			}
			
		}	
		
	}
	
	private boolean isDayFinished(String username, int day, int month, int year) {
		IDSSUserProfile idssUserProfile = idssUserProfiles.get(username);
		if(idssUserProfile != null) {
			VirtualClock clock = VirtualClock.getInstance();
			DateTime dateToday = clock.getTime().withZone(idssUserProfile.getTimeZone());
			
			int yearToday = dateToday.getYear();
			if(year < yearToday) return true;
			int monthToday = dateToday.getMonthOfYear();
			if(month < monthToday) return true;
			int dayToday = dateToday.getDayOfMonth();
			if(day < dayToday) return true;
		}		
		return false;
	}
	
}
