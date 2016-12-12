package eu.ewall.platform.ewallet;

import eu.ewall.platform.commons.datamodel.profile.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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

import eu.ewall.platform.idss.HttpBadRequestException;
import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseConnection;
import eu.ewall.platform.idss.dao.DatabaseCriteria;
import eu.ewall.platform.idss.dao.DatabaseSort;
import eu.ewall.platform.idss.service.model.common.ServiceResponse;
import eu.ewall.platform.idss.utils.AppComponents;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * This class defines endpoints and scheduled tasks for the EWalletService. It
 * is possible to implement multiple services within the same web application.
 * 
 * @authors Dennis Hofs (RRD)
 * 			Aristodemos Pnevmatikakis (AIT)
 */
@Configuration
@EnableScheduling
@RestController
@Service("EWalletService")
public class EWalletService implements ApplicationListener<ContextClosedEvent> {
	private static final String LOGTAG = "EWalletService";
	private Logger logger = AppComponents.getLogger(LOGTAG);

	// ewallClient is used to communicate with other eWALL services
	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;

	// The following values are defined in:
	// src/main/resources/ewallet-*.properties
	// They are set by set methods with annotation @Value
	private String mongoHost;
	private int mongoPort;
	private String mongoDbName;
	private String profilingServerURL;
	private String applicationServerURL;

	@Value("${mongo.host}")
	public void setMongoHost(String host) {
		this.mongoHost = host;
	}
	
	@Value("${mongo.port}")
	public void setMongoPort(int port) {
		this.mongoPort = port;
	}
	
	@Value("${mongo.dbname}")
	public void setMongoDbName(String name) {
		this.mongoDbName = name;
	}
	
	@Value("${profilingServer.url}")
	public void setProfilingServerURL(String url) {
		this.profilingServerURL = url;
	}

	@Value("${applicationServer.url}")
	public void setApplicationServerURL(String url) {
		this.applicationServerURL = url;
	}

	// Endpoints are defined by endpoint @RequestMapping
	
	// This method defines endpoint GET /getEWallet?username={username}
	// It wants to return an EWalletData object or null. When you return null,
	// Spring translates it to an empty response, which is not valid JSON.
	// Therefore we wrap the EWalletData object or null inside a
	// ServiceResponse object.
	// If you throw an exception with annotation @ResponseStatus, you can
	// control the HTTP error status. See for example
	// eu.ewall.platform.idss.HttpBadRequestException in idss-lib. Any other
	// exception results in error 500 Internal Server Error.

	// This method defines endpoint
	// GET /getEWallet?username={username}
	@RequestMapping(value="/getEWallet", method=RequestMethod.GET)
	public ServiceResponse<EWalletData> getEWalletData(
			@RequestParam(value="username", required=true)
			String username) throws Exception {
		// findUser checks whether the user exists and throws 400 Bad Request
		// if the user doesn't exist
		findUser(username);
		DatabaseLoader dbLoader = getDatabaseLoader();
		DatabaseConnection dbConn = dbLoader.connect();
		try {
			Database db = dbLoader.initDatabase(dbConn);
			DatabaseCriteria criteria = new DatabaseCriteria.Equal("username", username);
			DatabaseSort[] sort = new DatabaseSort[] {
				new DatabaseSort("timestamp", false)
			};
			EWalletData result = db.selectOne(new EWalletDataTable(), criteria, sort);
			return new ServiceResponse<EWalletData>(result);
		} finally {
			dbConn.close();
		}
	}
	
	@RequestMapping(value="/getCoinTotal", method=RequestMethod.GET)
	public Double getCoinTotal(
			@RequestParam(value="username", required=true)
			String username) throws Exception {
		findUser(username);
		DatabaseLoader dbLoader = getDatabaseLoader();
		DatabaseConnection dbConn = dbLoader.connect();
		try {
			Database db = dbLoader.initDatabase(dbConn);
			DatabaseCriteria criteria = new DatabaseCriteria.Equal("username", username);
			DatabaseSort[] sort = new DatabaseSort[] {
				new DatabaseSort("timestamp", false)
			};
			EWalletData result = db.selectOne(new EWalletDataTable(), criteria, sort);
			if(result != null) {
				return result.getCurrentCoins();
			} else {
				return 0.0d;
			}
		} finally {
			dbConn.close();
		}
	}

	// This method defines endpoint
	// POST /buyUnlistedItem?username={username}&item={item}&cost={cost}
	@RequestMapping(value="/buyUnlistedItem", method=RequestMethod.POST)
	public boolean postBuyUnlistedItem(
			@RequestParam(value="username", required=true)
					String username,
			@RequestParam(value="item", required=true)
					String item,
			@RequestParam(value="cost", required=true)
					double cost
	) throws Exception {
		User user = findUser(username);
		DateTimeZone tz = getTimeZone(user);
		if (tz == null)
			tz = DateTimeZone.getDefault();
		DateTime now = new DateTime(tz);
		try {
			postEWalletData(username, -cost, "NONE");
		} catch(HttpBadRequestException ex) {
			return false;
			//throw new HttpBadRequestException("User " + username + " does not have enough coins for the transaction!");
		}
		TransactionData data = new TransactionData();
		data.setUsername(username);
		data.setItem(item);
		data.setCost(cost);
		data.setTimestamp(now.getMillis());
		DatabaseLoader dbLoader = getDatabaseLoader();
		DatabaseConnection dbConn = dbLoader.connect();
		try {
			Database db = dbLoader.initDatabase(dbConn);
			db.insert(TransactionDataTable.NAME, data);
		} finally {
			dbConn.close();
		}
		return true;
	}


	// This method defines endpoint
	// POST /buyItem?username={username}&item={item}
	@RequestMapping(value="/buyItem", method=RequestMethod.POST)
	public int postBuyItem(
			@RequestParam(value="username", required=true)
					String username,
			@RequestParam(value="item", required=true)
					String itemString
	) throws Exception {
		RewardType item = null;
		try {
			item = RewardType.valueOf(itemString);
		} catch(Exception ex) {
			logger.info("Sought item " + itemString + " is not for sale!");
			return -1;
		}
		User user = findUser(username);
		// Get rewards list of the user
		UserProfile profile = user.getUserProfile();
		EWallSubProfile subProfile = profile.geteWallSubProfile();
		List<RewardType> rewards = subProfile.getRewardsList();
		// Stop if the item is already there
		for (RewardType reward : rewards) {
			if (reward == item) {
				logger.info("Item " + item + " already bought!");
				return -2;
			}
		}
		DateTimeZone tz = getTimeZone(user);
		if (tz == null)
			tz = DateTimeZone.getDefault();
		DateTime now = new DateTime(tz);
		double cost = (double)RewardCosts.getRewardCosts(item);
		if (cost == -1) {
			logger.info("Item " + item + " not found!");
			return -1;
		}
		try {
			postEWalletData(username, -cost, "NONE");
		} catch(HttpBadRequestException ex) {
			logger.info("User " + username + " does not have enough coins for the transaction!");
			return -3;
			//throw new HttpBadRequestException("User " + username + " does not have enough coins for the transaction!");
		}
		TransactionData data = new TransactionData();
		data.setUsername(username);
		data.setItem(item.toString());
		data.setCost(cost);
		data.setTimestamp(now.getMillis());
		DatabaseLoader dbLoader = getDatabaseLoader();
		DatabaseConnection dbConn = dbLoader.connect();
		try {
			Database db = dbLoader.initDatabase(dbConn);
			db.insert(TransactionDataTable.NAME, data);
		} finally {
			dbConn.close();
		}
		// Update rewards and store profile
		rewards.add(item);
		subProfile.setRewardsList(rewards);
		profile.seteWallSubProfile(subProfile);
		user.setUserProfile(profile);
		ewallClient.postForEntity(profilingServerURL + "/profiling-server/users/" + username, user, User.class);
		return 1;
	}


	// This method defines endpoint
	// POST /earnCoins?username={username}&coins={coins}&from={one of the five coin generators}
	@RequestMapping(value="/earnCoins", method=RequestMethod.POST)
	public void postEWalletData(
			@RequestParam(value="username", required=true)
			String username,
			@RequestParam(value="coins", required=true)
			double coins,
			@RequestParam(value="from", required=true)
			String from
	) throws Exception {
		User user = findUser(username);
		DateTimeZone tz = getTimeZone(user);
		if (tz == null)
			tz = DateTimeZone.getDefault();
		DateTime now = new DateTime(tz);
		DatabaseLoader dbLoader = getDatabaseLoader();
		EWalletData data = new EWalletData();
		data.setUsername(username);
		DatabaseConnection dbConn = dbLoader.connect();
		try {
			Database db = dbLoader.initDatabase(dbConn);
			DatabaseCriteria criteria = new DatabaseCriteria.Equal("username", username);
			DatabaseSort[] sort = new DatabaseSort[] {
					new DatabaseSort("timestamp", false)
			};
			EWalletData temp = db.selectOne(new EWalletDataTable(), criteria, sort);
			if (temp != null) {
				data.setCoinsFromActivity(temp.getCoinsFromActivity());
				data.setCoinsFromExercise(temp.getCoinsFromExercise());
				data.setCoinsFromSleep(temp.getCoinsFromSleep());
				data.setCoinsFromGames(temp.getCoinsFromGames());
				data.setCoinsFromUsage(temp.getCoinsFromUsage());
				data.setCurrentCoins(temp.getCurrentCoins());
			}
		} finally {
			dbConn.close();
		}
		data.addToCurrentCoins(coins);
		if (data.getCurrentCoins() < 0) {
			throw new HttpBadRequestException("Cannot reduce coins below zero!");
		}
		if (from.equalsIgnoreCase("ACTIVITY"))
			data.addToCoinsFromActivity(coins);
		else if (from.equalsIgnoreCase("SLEEP"))
			data.addToCoinsFromSleep(coins);
		else if (from.equalsIgnoreCase("EXERCISE"))
			data.addToCoinsFromExercise(coins);
		else if (from.equalsIgnoreCase("GAMES"))
			data.addToCoinsFromGames(coins);
		else if (from.equalsIgnoreCase("USAGE"))
			data.addToCoinsFromUsage(coins);
		data.setIsoTime(now);
		data.setTimestamp(now.getMillis());
		dbConn = dbLoader.connect();
		try {
			Database db = dbLoader.initDatabase(dbConn);
			db.insert(EWalletDataTable.NAME, data);
		} finally {
			dbConn.close();
		}
	}
	
	// Scheduled tasks are defined with annotation @Scheduled. Take into
	// account that users live in different time zones.
	@Scheduled(fixedRate=300000)
	public void runScheduledActivity() throws Exception {
		User[] users = {};
		try {
			users = ewallClient.getForObject(profilingServerURL + "/profiling-server/users/?associatedWithSensEnvFilter=ONLY_ASSOCIATED_USERS", User[].class);
		} catch(Exception ex) {
			logger.info("Failed to access users list");
			return;
		}
		for (int idx = 0; idx < users.length; idx++) {
			String username = users[idx].getUsername();
			double coins = 0;
			ActivityData fitbit = null;
			DateTimeZone tz = getTimeZone(users[idx]);
			if (tz == null)
				tz = DateTimeZone.getDefault();
			DateTime now = new DateTime(tz);
			try {
				fitbit = ewallClient.getForObject(applicationServerURL + "/fusioner-fitbit2/movementData?username=" + username, ActivityData.class);
			} catch(Exception ex) {
				logger.info(username + ": Failed to access Fitbit fusioner. Skipping user " + username);
				continue;
			}
			if (fitbit == null) {
				logger.info(username + ": Fitbit fusioner returned null. Skipping user " + username);
				continue;
			}
			DatabaseLoader dbLoader = getDatabaseLoader();
			DatabaseConnection dbConn = dbLoader.connect();
			try {
				Database db = dbLoader.initDatabase(dbConn);
				DatabaseCriteria criteria = new DatabaseCriteria.Equal("username", username);
				DatabaseSort[] sort = new DatabaseSort[]{
						new DatabaseSort("timestamp", false)
				};
				ActivityData temp = db.selectOne(new ActivityDataTable(), criteria, sort);
				if (temp != null) {
					//Have already a stored record for this user
					if (!temp.getLastTrackerSyncTime().equals(fitbit.getLastTrackerSyncTime())) {
						//Different tracker sync times, update coins
						if (fitbit.getSteps() >= temp.getSteps()) {
							//Calculate coins based on difference
							int dSteps = fitbit.getSteps() - temp.getSteps();
							int dVeryActiveMinutes = fitbit.getVeryActiveMinutes() - temp.getVeryActiveMinutes();
							int dFairlyActiveMinutes = fitbit.getFairlyActiveMinutes() - temp.getFairlyActiveMinutes();
							int dLightlyActiveMinutes = fitbit.getLightlyActiveMinutes() - temp.getLightlyActiveMinutes();
							coins = dSteps / 100.0 + dLightlyActiveMinutes / 2.0 + dFairlyActiveMinutes + dVeryActiveMinutes * 2;
						} else {
							//New day, all activity into coins
							coins = fitbit.getSteps() / 100.0 + fitbit.getLightlyActiveMinutes() / 2.0 +
									fitbit.getFairlyActiveMinutes() + fitbit.getVeryActiveMinutes() * 2;
						}
					}
				} else {
					//No record stored for this user. All activity into coins
					coins = fitbit.getSteps() / 100.0 + fitbit.getLightlyActiveMinutes() / 2.0 +
							fitbit.getFairlyActiveMinutes() + fitbit.getVeryActiveMinutes() * 2;
				}
				if (coins > 0) {
					//Coins changed due to activity, store the fitbit ActivityData record in database
					fitbit.setTimestamp(now.getMillis());
					db.insert(ActivityDataTable.NAME, fitbit);
				}
			} finally {
				dbConn.close();
			}
			if (coins > 0) {
				//Since coins have changed due to activity, store a EWalletData record in database
				postEWalletData(username, coins, "ACTIVITY");
			}
			logger.info(username + ": " + coins + " new activity coins");
		}
	}


	// Scheduled tasks are defined with annotation @Scheduled. Take into
	// account that users live in different time zones.
	@Scheduled(fixedRate=3600000)
	public void runScheduledSleep() throws Exception {
		User[] users = {};
		try {
			users = ewallClient.getForObject(profilingServerURL + "/profiling-server/users/?associatedWithSensEnvFilter=ONLY_ASSOCIATED_USERS", User[].class);
		} catch(Exception ex) {
			logger.info("Failed to access users list");
			return;
		}
		String t1, t2;
		for (int idx = 0; idx < users.length; idx++) {
			String username = users[idx].getUsername();
			double coins = 0;
			SleepData[] sleep;
			DateTimeZone tz = getTimeZone(users[idx]);
			if (tz == null)
				tz = DateTimeZone.getDefault();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			sdf.setTimeZone(tz.toTimeZone());
			DatabaseLoader dbLoader = getDatabaseLoader();
			DatabaseConnection dbConn = dbLoader.connect();
			Database db;
			try {
				db = dbLoader.initDatabase(dbConn);
				DatabaseCriteria criteria = new DatabaseCriteria.Equal("username", username);
				DatabaseSort[] sort = new DatabaseSort[]{
						new DatabaseSort("timestamp", false)
				};
				SleepTimeData temp = db.selectOne(new SleepTimeDataTable(), criteria, sort);
				if (temp != null) {
					//There are previous sleep records in MongoDB for that user.
					//Ask idss-sleep-reasoner for data since the latest of them.
					t1 = sdf.format(new Date(temp.getTimestamp() + 1));
				} else {
					//No previous sleep record in MongoDB for that user.
					//Ask idss-sleep-reasoner for data since the start of time.
					t1 = sdf.format(new Date(0));
				}
			} finally {
				dbConn.close();
			}
			t2 = sdf.format(new Date());
			logger.info(t2 + " " + t1);
			try {
				sleep = ewallClient.getForObject(profilingServerURL + "/idss-sleep-reasoner/sleepdata?username=" + username + "&from=" + t1 + "&to=" + t2, SleepData[].class);
				logger.info(username + ": " + sleep.length + " records");
			} catch(Exception ex) {
				logger.info(username + ": Failed to access Fitbit sleep data. Skipping user " + username);
				continue;
			}
			if (sleep == null) {
				logger.info(username + ": Sleep fusioner returned null. Skipping user " + username);
				continue;
			}
			dbConn = dbLoader.connect();
			try {
				db = dbLoader.initDatabase(dbConn);
				for (int i = 0; i < sleep.length; i++) {
					SleepTimeData temp = new SleepTimeData();
					temp.setTimestamp(sleep[i].getBedOffTime());
					temp.setUsername(sleep[i].getUsername());
					temp.setDuration(sleep[i].getTotalSleepTime());
					db.insert(SleepTimeDataTable.NAME, temp);
					double c = sleep[i].getTotalSleepTime() * sleep[i].getSleepEfficiency() / 60.0 / 8;
					if (c > 100)
						c = 100;
					coins += c;
				}
			} finally {
				dbConn.close();
			}
			if (coins > 0) {
				//Since coins have changed due to activity, store a EWalletData record in database
				postEWalletData(username, coins, "SLEEP");
			}
			logger.info(username + ": " + coins + " new sleep coins");
		}
	}

	/**
	 * Requests the User object from the profiling server. If the user doesn't
	 * exist, this method throws a HttpBadRequestException, which results in
	 * HTTP status 400 Bad Request.
	 * 
	 * @param username the username
	 * @return the User object
	 * @throws Exception if the user is not found or an error occurs while
	 * communicating with the profiling server
	 */
	private User findUser(String username) throws Exception {
		try {
			return ewallClient.getForObject(profilingServerURL + "/profiling-server/users/{username}", User.class, username);
		} catch (HttpClientErrorException ex) {
			if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
				throw new HttpBadRequestException("User not found: " + username);
			} else {
				throw ex;
			}
		}
	}
	
	/**
	 * Returns the time zone for the specified user. If the time zone is not
	 * defined, this method returns null.
	 * 
	 * @param user the user
	 * @return the time zone or null
	 */
	private DateTimeZone getTimeZone(User user) {
		UserProfile profile = user.getUserProfile();
		if (profile == null)
			return null;
		VCardSubProfile vcard = profile.getvCardSubProfile();
		if (vcard == null)
			return null;
		String timezoneid = vcard.getTimezoneid();
		if (timezoneid == null)
			return null;
		return DateTimeZone.forID(timezoneid);
	}

	private DatabaseLoader getDatabaseLoader() {
		return new DatabaseLoader(mongoHost, mongoPort, mongoDbName);
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		// Here you can clean up things and close resources before the web
		// application is closed (for example at Tomcat restart).
	}
}