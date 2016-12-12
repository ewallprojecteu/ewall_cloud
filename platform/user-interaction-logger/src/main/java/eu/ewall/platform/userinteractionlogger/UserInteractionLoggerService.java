package eu.ewall.platform.userinteractionlogger;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;

import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.idss.CommonsUserReader;
import eu.ewall.platform.idss.HttpBadRequestException;
import eu.ewall.platform.idss.HttpNotFoundException;
import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseConnection;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.mongodb.MongoDatabaseFactory;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.DateTimeServiceResponse;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.userinteractionlogger.model.UserInteractionLog;
import eu.ewall.platform.userinteractionlogger.model.UserInteractionLogTable;

@Configuration
@EnableScheduling
@RestController
@Service("UserInteractionLogger")
public class UserInteractionLoggerService implements
ApplicationListener<ContextClosedEvent> {
	
	private InteractionReasonerService reasonerService = null;
	
	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;
	
	private UILConfiguration config = new UILConfiguration();
	
	private final Object lock = new Object();
	
	@Value("${profilingServer.url}")
	public void setProfilingServerURL(String profilingServerUrl) {
		config.setProfilingServerUrl(profilingServerUrl);
	}
	
	@Value("${mongo.host}")
	public void setMongoHost(String mongoDBHost) {
		config.setMongoDBHost(mongoDBHost);
	}
	
	@Value("${mongo.port}")
	public void setMongoPort(int mongoDBPort) {
		config.setMongoDBPort(mongoDBPort);
	}
	
	@Value("${mongo.dbname}")
	public void setMongoDbName(String mongoDBName) {
		config.setMongoDBName(mongoDBName);
	} 
	
	@RequestMapping(value="/logInteraction", method=RequestMethod.POST)
	public void logInteraction(
			@RequestParam(value="userid", required=true)
			String userid,
			@RequestParam(value="applicationName", required=true)
			String applicationName,
			@RequestParam(value="buttonId", required=true)
			String buttonId,
			@RequestParam(value="comment", required=false, defaultValue="")
			String comment,
			@RequestParam(value="timestamp", required=true)
			String timestamp) throws Exception {
		// check if user exists
		getUserProfile(userid);
		DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser()
				.withOffsetParsed();
		DateTime tzTime;
		String dateString;
		try {
			tzTime = parser.parseDateTime(timestamp);
			dateString = tzTime.getDayOfMonth()+"-"+tzTime.getMonthOfYear()+"-"+tzTime.getYear();
		} catch (IllegalArgumentException ex) {
			throw new HttpBadRequestException(
					"Parameter timestamp not formatted as ISO date/time: " +
					timestamp, ex);
		}
		DatabaseAccessPoint dap = AppComponents.get(DatabaseAccessPoint.class);
		DatabaseConnection dbConn = dap.openDatabase();
		try {
			Database db = dap.getDatabase(dbConn);
			UserInteractionLogTable table = dap.initLogTable(db, userid);
			UserInteractionLog log = new UserInteractionLog();
			log.setUserid(userid);
			log.setApplicationName(applicationName);
			log.setButtonId(buttonId);
			log.setComment(comment);
			log.setUtcTime(tzTime.getMillis());
			log.setTzTime(tzTime);
			log.setDateString(dateString);
			db.insert(table.getName(), log);
		} finally {
			dap.closeDatabase(dbConn);
		}
	}
	
	@RequestMapping(value="/interactionCount", method=RequestMethod.GET)
	public int getInteractionCount(
			@RequestParam(value="username", required=true)
			String username,
			@RequestParam(value="date", required=false, defaultValue="")
			String date) throws Exception {
		return getInteractionCountWithApplication(username, date, null);
	}

	@RequestMapping(value="/interactionCountMainScreen",
			method=RequestMethod.GET)
	public int getInteractionCountMainScreen(
			@RequestParam(value="username", required=true)
			String username,
			@RequestParam(value="date", required=false, defaultValue="")
			String date) throws Exception {
		return getInteractionCountWithApplication(username, date,
				"mainScreen");
	}
	
	@RequestMapping(value="/interactionCountCalendarApplication",
			method=RequestMethod.GET)
	public int getInteractionCountCalendarApplication(
			@RequestParam(value="username", required=true)
			String username,
			@RequestParam(value="date", required=false, defaultValue="")
			String date) throws Exception {
		return getInteractionCountWithApplication(username, date,
				"calendarApplication");
	}
	
	@RequestMapping(value="/interactionCountCognitiveExerciseApplication",
			method=RequestMethod.GET)
	public int getInteractionCountCognitiveExerciseApplication(
			@RequestParam(value="username", required=true)
			String username,
			@RequestParam(value="date", required=false, defaultValue="")
			String date) throws Exception {
		return getInteractionCountWithApplication(username, date,
				"cognitiveExerciseApplication");
	}
	
	@RequestMapping(value="/interactionCountDomoticsApplication",
			method=RequestMethod.GET)
	public int getInteractionCountDomoticsApplication(
			@RequestParam(value="username", required=true)
			String username,
			@RequestParam(value="date", required=false, defaultValue="")
			String date) throws Exception {
		return getInteractionCountWithApplication(username, date,
				"domoticsApplication");
	}
	
	@RequestMapping(value="/interactionCountFallPreventionApplication",
			method=RequestMethod.GET)
	public int getInteractionCountFallPreventionApplication(
			@RequestParam(value="username", required=true)
			String username,
			@RequestParam(value="date", required=false, defaultValue="")
			String date) throws Exception {
		return getInteractionCountWithApplication(username, date,
				"fallPreventionApplication");
	}
	
	@RequestMapping(value="/interactionCountHelpApplication",
			method=RequestMethod.GET)
	public int getInteractionCountHelpApplication(
			@RequestParam(value="username", required=true)
			String username,
			@RequestParam(value="date", required=false, defaultValue="")
			String date) throws Exception {
		return getInteractionCountWithApplication(username, date,
				"helpApplication");
	}
	
	@RequestMapping(value="/interactionCountMyActivityApplication",
			method=RequestMethod.GET)
	public int getInteractionCountMyActivityApplication(
			@RequestParam(value="username", required=true)
			String username,
			@RequestParam(value="date", required=false, defaultValue="")
			String date) throws Exception {
		return getInteractionCountWithApplication(username, date,
				"myActivityApplication");
	}
	
	@RequestMapping(value="/interactionCountMyDailyLifeApplication",
			method=RequestMethod.GET)
	public int getInteractionCountMyDailyLifeApplication(
			@RequestParam(value="username", required=true)
			String username,
			@RequestParam(value="date", required=false, defaultValue="")
			String date) throws Exception {
		return getInteractionCountWithApplication(username, date,
				"myDailyLifeApplication");
	}
	
	@RequestMapping(value="/interactionCountMyHealthApplication",
			method=RequestMethod.GET)
	public int getInteractionCountMyHealthApplication(
			@RequestParam(value="username", required=true)
			String username,
			@RequestParam(value="date", required=false, defaultValue="")
			String date) throws Exception {
		return getInteractionCountWithApplication(username, date,
				"myHealthApplication");
	}
	
	@RequestMapping(value="/interactionCountRewardApplication",
			method=RequestMethod.GET)
	public int getInteractionCountRewardApplication(
			@RequestParam(value="username", required=true)
			String username,
			@RequestParam(value="date", required=false, defaultValue="")
			String date) throws Exception {
		return getInteractionCountWithApplication(username, date,
				"rewardApplication");
	}
	
	@RequestMapping(value="/interactionCountSleepApplication",
			method=RequestMethod.GET)
	public int getInteractionCountSleepApplication(
			@RequestParam(value="username", required=true)
			String username,
			@RequestParam(value="date", required=false, defaultValue="")
			String date) throws Exception {
		return getInteractionCountWithApplication(username, date,
				"sleepApplication");
	}
	
	@RequestMapping(value="/interactionCountVideoExerciseApplication",
			method=RequestMethod.GET)
	public int getInteractionCountVideoExerciseApplication(
			@RequestParam(value="username", required=true)
			String username,
			@RequestParam(value="date", required=false, defaultValue="")
			String date) throws Exception {
		return getInteractionCountWithApplication(username, date,
				"videoExerciseApplication");
	}
	
	@RequestMapping(value="/lastInteraction",
			method=RequestMethod.GET)
	public DateTimeServiceResponse getLastInteraction(
			@RequestParam(value="username", required=true)
			String username) throws Exception {
		return getLastInteractionWithApplication(username, null);
	}

	@RequestMapping(value="/lastInteractionMainScreen",
			method=RequestMethod.GET)
	public DateTimeServiceResponse getLastInteractionMainScreen(
			@RequestParam(value="username", required=true)
			String username) throws Exception {
		return getLastInteractionWithApplication(username,
				"mainScreen");
	}
	
	@RequestMapping(value="/lastInteractionCalendarApplication",
			method=RequestMethod.GET)
	public DateTimeServiceResponse getLastInteractionCalendarApplication(
			@RequestParam(value="username", required=true)
			String username) throws Exception {
		return getLastInteractionWithApplication(username,
				"calendarApplication");
	}
	
	@RequestMapping(value="/lastInteractionCognitiveExerciseApplication",
			method=RequestMethod.GET)
	public DateTimeServiceResponse getLastInteractionCognitiveExerciseApplication(
			@RequestParam(value="username", required=true)
			String username) throws Exception {
		return getLastInteractionWithApplication(username,
				"cognitiveExerciseApplication");
	}
	
	@RequestMapping(value="/lastInteractionDomoticsApplication",
			method=RequestMethod.GET)
	public DateTimeServiceResponse getLastInteractionDomoticsApplication(
			@RequestParam(value="username", required=true)
			String username) throws Exception {
		return getLastInteractionWithApplication(username,
				"domoticsApplication");
	}
	
	@RequestMapping(value="/lastInteractionFallPreventionApplication",
			method=RequestMethod.GET)
	public DateTimeServiceResponse getLastInteractionFallPreventionApplication(
			@RequestParam(value="username", required=true)
			String username) throws Exception {
		return getLastInteractionWithApplication(username,
				"fallPreventionApplication");
	}
	
	@RequestMapping(value="/lastInteractionHelpApplication",
			method=RequestMethod.GET)
	public DateTimeServiceResponse getLastInteractionHelpApplication(
			@RequestParam(value="username", required=true)
			String username) throws Exception {
		return getLastInteractionWithApplication(username,
				"helpApplication");
	}
	
	@RequestMapping(value="/lastInteractionMyActivityApplication",
			method=RequestMethod.GET)
	public DateTimeServiceResponse getLastInteractionMyActivityApplication(
			@RequestParam(value="username", required=true)
			String username) throws Exception {
		return getLastInteractionWithApplication(username,
				"myActivityApplication");
	}
	
	@RequestMapping(value="/lastInteractionMyDailyLifeApplication",
			method=RequestMethod.GET)
	public DateTimeServiceResponse getLastInteractionMyDailyLifeApplication(
			@RequestParam(value="username", required=true)
			String username) throws Exception {
		return getLastInteractionWithApplication(username,
				"myDailyLifeApplication");
	}
	
	@RequestMapping(value="/lastInteractionMyHealthApplication",
			method=RequestMethod.GET)
	public DateTimeServiceResponse getLastInteractionMyHealthApplication(
			@RequestParam(value="username", required=true)
			String username) throws Exception {
		return getLastInteractionWithApplication(username,
				"myHealthApplication");
	}
	
	@RequestMapping(value="/lastInteractionRewardApplication",
			method=RequestMethod.GET)
	public DateTimeServiceResponse getLastInteractionRewardApplication(
			@RequestParam(value="username", required=true)
			String username) throws Exception {
		return getLastInteractionWithApplication(username,
				"rewardApplication");
	}
	
	@RequestMapping(value="/lastInteractionSleepApplication",
			method=RequestMethod.GET)
	public DateTimeServiceResponse getLastInteractionSleepApplication(
			@RequestParam(value="username", required=true)
			String username) throws Exception {
		return getLastInteractionWithApplication(username,
				"sleepApplication");
	}
	
	@RequestMapping(value="/lastInteractionVideoExerciseApplication",
			method=RequestMethod.GET)
	public DateTimeServiceResponse getLastInteractionVideoExerciseApplication(
			@RequestParam(value="username", required=true)
			String username) throws Exception {
		return getLastInteractionWithApplication(username,
				"videoExerciseApplication");
	}
	
	private IDSSUserProfile getUserProfile(String username)
			throws HttpNotFoundException, HttpClientErrorException {
		String url = config.getProfilingServerUrl() + "/users/{username}";
		// check if user exists
		User eWallCommonUser;
		try {
			eWallCommonUser = ewallClient.getForObject(url, User.class,
					username);
		} catch (HttpClientErrorException ex) {
			if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
				throw new HttpNotFoundException(String.format(
						"User \"%s\" not found", username), ex);
			} else {
				throw ex;
			}
		}
		return CommonsUserReader.readUser(eWallCommonUser);
	}
	
	private DateTimeServiceResponse getLastInteractionWithApplication(
			String username, String app) throws Exception {
		synchronized (lock) {
			if (reasonerService == null)
				initReasoner();
		}
		getUserProfile(username);
		return new DateTimeServiceResponse(
				reasonerService.getLastInteractionWithApp(username, app));
	}
	
	private int getInteractionCountWithApplication(String username,
			String dateStr, String app) throws Exception {
		synchronized (lock) {
			if (reasonerService == null)
				initReasoner();
		}
		IDSSUserProfile user = getUserProfile(username);
		LocalDate date;
		if (dateStr == null || dateStr.length() == 0) {
			date = new LocalDate(user.getTimeZone());
		} else {
			DateTimeFormatter parser = DateTimeFormat.forPattern("yyyy-MM-dd");
			try {
				date = parser.parseLocalDate(dateStr);
			} catch (IllegalArgumentException ex) {
				throw new HttpBadRequestException(
						"Parameter date not formatted as yyyy-MM-dd: " +
						dateStr, ex);
			}
		}
		return reasonerService.getInteractionCountWithApp(user, date, app);
	}
	
	@Override
	public void onApplicationEvent(ContextClosedEvent ev) {
		AppComponents comps = AppComponents.getInstance();
		if (comps.hasComponent(DatabaseAccessPoint.class))
			comps.getComponent(DatabaseAccessPoint.class).close();
	}
	
	// ---------- Lifestyle Reasoning services
	
	@Scheduled(fixedRateString="${runTaskScheduleRateString:300000}")
	public void runTask() throws Exception {
		ILoggerFactory logFactory = AppComponents.getInstance().getComponent(
				ILoggerFactory.class);
		Logger logger = logFactory.getLogger(InteractionReasonerService.LOGTAG);
		try {
			synchronized (lock) {
				if (reasonerService == null)
					initReasoner();
			}
			reasonerService.runTask();
		} catch (DatabaseException ex) {
			logger.warn("Database error: {}: Trying again at next run",ex.getMessage());
		}
	}
	
	/**
	 * Initializes the {@link InteractionReasonerService} upon first start, or after
	 * a server reset.
	 */
	private void initReasoner() {
		AppComponents comps = AppComponents.getInstance();
		MongoDatabaseFactory dbFactory = comps.getComponent(
				MongoDatabaseFactory.class);
		dbFactory.setHost(config.getMongoDBHost());
		dbFactory.setPort(config.getMongoDBPort());
		if (!comps.hasComponent(DatabaseAccessPoint.class))
			comps.addComponent(new DatabaseAccessPoint(config));
		reasonerService = new InteractionReasonerService(config,ewallClient);
	}
}
