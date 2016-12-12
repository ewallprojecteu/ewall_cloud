package eu.ewall.platform.ekitchen;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;

import eu.ewall.platform.idss.HttpBadRequestException;
import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseConnection;
import eu.ewall.platform.idss.dao.DatabaseCriteria;
import eu.ewall.platform.idss.dao.DatabaseSort;
import eu.ewall.platform.idss.service.model.common.ServiceResponse;
import eu.ewall.platform.idss.utils.AppComponents;

import java.util.List;

/**
 * This class defines endpoints and scheduled tasks for the EKitchenService. It
 * is possible to implement multiple services within the same web application.
 * 
 * @author Aristodemos Pnevmatikakis (AIT)
 */
@Configuration
@EnableScheduling
@RestController
@Service("EKitchenService")
public class EKitchenService implements ApplicationListener<ContextClosedEvent> {
	private static final String LOGTAG = "EKitchenService";
	private Logger logger = AppComponents.getLogger(LOGTAG);

	// ewallClient is used to communicate with other eWALL services
	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;

	// The following values are defined in:
	// src/main/resources/ekitchen-*.properties
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
	// POST ekitchen/fridge?username={username}
	// for adding a list of IDs in the fridge
	@RequestMapping(value="/fridge", method=RequestMethod.POST)
	public ResponseEntity<Boolean> updateFridge(
			@RequestParam(value="username", required=true) String username,
			@RequestParam(value="ingredients", required=true) String toFridge) throws Exception {
		User user = findUser(username);
		DateTimeZone tz = getTimeZone(user);
		if (tz == null)
			tz = DateTimeZone.getDefault();
		DateTime now = new DateTime(tz);
		FridgeData data = new FridgeData();
		data.setUsername(username);
		data.setInFridge(toFridge);
		data.setTimestamp(now.getMillis());
		DatabaseLoader dbLoader = getDatabaseLoader();
		DatabaseConnection dbConn = dbLoader.connect();
		try {
			Database db = dbLoader.initDatabase(dbConn);
			db.insert(FridgeDataTable.NAME, data);
		} finally {
			dbConn.close();
		}
		return ResponseEntity.status(HttpStatus.OK).body(true);
	}

	// This method defines endpoint
	// GET ekitchen/fridge?username={username}
	// for getting the fridge contents as a list of IDs
	@RequestMapping(value="/fridge", method=RequestMethod.GET)
	public ResponseEntity<FridgeData> getFridge(
			@RequestParam(value="username", required=true) String username) throws Exception {
		findUser(username);
		DatabaseLoader dbLoader = getDatabaseLoader();
		DatabaseConnection dbConn = dbLoader.connect();
		try {
			Database db = dbLoader.initDatabase(dbConn);
			DatabaseCriteria criteria = new DatabaseCriteria.Equal("username", username);
			DatabaseSort[] sort = new DatabaseSort[] {
					new DatabaseSort("timestamp", false)
			};
			FridgeData result = db.selectOne(new FridgeDataTable(), criteria, sort);
			if (result == null) {
				result = new FridgeData();
				result.setInFridge("");
			}
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch(Exception ex) {
			FridgeData result = new FridgeData();
			result.setInFridge("");
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} finally {
			dbConn.close();
		}
	}

	// This method defines endpoint
	// POST ekitchen/pantry?username={username}
	// for adding a list of IDs in the pantry
	@RequestMapping(value="/pantry", method=RequestMethod.POST)
	public ResponseEntity<Boolean> updatePantry(
			@RequestParam(value="username", required=true) String username,
			@RequestParam(value="ingredients", required=true) String toPantry) throws Exception {
		User user = findUser(username);
		DateTimeZone tz = getTimeZone(user);
		if (tz == null)
			tz = DateTimeZone.getDefault();
		DateTime now = new DateTime(tz);
		PantryData data = new PantryData();
		data.setUsername(username);
		data.setInPantry(toPantry);
		data.setTimestamp(now.getMillis());
		DatabaseLoader dbLoader = getDatabaseLoader();
		DatabaseConnection dbConn = dbLoader.connect();
		try {
			Database db = dbLoader.initDatabase(dbConn);
			db.insert(PantryDataTable.NAME, data);
		} finally {
			dbConn.close();
		}
		return ResponseEntity.status(HttpStatus.OK).body(true);
	}

	// This method defines endpoint
	// GET ekitchen/pantry?username={username}
	// for getting the pantry contents as a list of IDs
	@RequestMapping(value="/pantry", method=RequestMethod.GET)
	public ResponseEntity<PantryData> getPantry(
			@RequestParam(value="username", required=true) String username) throws Exception {
		findUser(username);
		DatabaseLoader dbLoader = getDatabaseLoader();
		DatabaseConnection dbConn = dbLoader.connect();
		try {
			Database db = dbLoader.initDatabase(dbConn);
			DatabaseCriteria criteria = new DatabaseCriteria.Equal("username", username);
			DatabaseSort[] sort = new DatabaseSort[] {
					new DatabaseSort("timestamp", false)
			};
			PantryData result = db.selectOne(new PantryDataTable(), criteria, sort);
			if (result == null) {
				result = new PantryData();
				result.setInPantry("");
			}
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch(Exception ex) {
			PantryData result = new PantryData();
			result.setInPantry("");
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} finally {
			dbConn.close();
		}
	}

	// This method defines endpoint
	// POST ekitchen/shopping?username={username}
	// for adding a list of IDs in the shopping list
	@RequestMapping(value="/shopping", method=RequestMethod.POST)
	public ResponseEntity<Boolean> updateShopping(
			@RequestParam(value="username", required=true) String username,
			@RequestParam(value="ingredients", required=true) String toShopping) throws Exception {
		User user = findUser(username);
		DateTimeZone tz = getTimeZone(user);
		if (tz == null)
			tz = DateTimeZone.getDefault();
		DateTime now = new DateTime(tz);
		ShoppingData data = new ShoppingData();
		data.setUsername(username);
		data.setInShopping(toShopping);
		data.setTimestamp(now.getMillis());
		DatabaseLoader dbLoader = getDatabaseLoader();
		DatabaseConnection dbConn = dbLoader.connect();
		try {
			Database db = dbLoader.initDatabase(dbConn);
			db.insert(ShoppingDataTable.NAME, data);
		} finally {
			dbConn.close();
		}
		return ResponseEntity.status(HttpStatus.OK).body(true);
	}

	// This method defines endpoint
	// GET ekitchen/shopping?username={username}
	// for getting the shopping list contents as a list of IDs
	@RequestMapping(value="/shopping", method=RequestMethod.GET)
	public ResponseEntity<ShoppingData> getShopping(
			@RequestParam(value="username", required=true) String username) throws Exception {
		findUser(username);
		DatabaseLoader dbLoader = getDatabaseLoader();
		DatabaseConnection dbConn = dbLoader.connect();
		try {
			Database db = dbLoader.initDatabase(dbConn);
			DatabaseCriteria criteria = new DatabaseCriteria.Equal("username", username);
			DatabaseSort[] sort = new DatabaseSort[] {
					new DatabaseSort("timestamp", false)
			};
			ShoppingData result = db.selectOne(new ShoppingDataTable(), criteria, sort);
			if (result == null) {
				result = new ShoppingData();
				result.setInShopping("");
			}
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch(Exception ex) {
			ShoppingData result = new ShoppingData();
			result.setInShopping("");
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} finally {
			dbConn.close();
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
