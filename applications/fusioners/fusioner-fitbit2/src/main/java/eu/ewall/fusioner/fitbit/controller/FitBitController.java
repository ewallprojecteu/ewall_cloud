/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.fusioner.fitbit.controller;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.MongoClient;

import eu.ewall.fusioner.fitbit.DatabaseLoader;
import eu.ewall.fusioner.fitbit.model.ActivitySummary;
import eu.ewall.fusioner.fitbit.model.ActivitySummaryTable;
import eu.ewall.fusioner.fitbit.model.FitBitEvent;
import eu.ewall.fusioner.fitbit.model.FitbitUpdate;
import eu.ewall.fusioner.fitbit.model.FitbitUpdateTable;
import eu.ewall.fusioner.fitbit.model.MovementUpdate;
import eu.ewall.fusioner.fitbit.model.SleepHistorySummary;
import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseConnection;
import eu.ewall.platform.idss.dao.DatabaseCriteria;

/**
 * The Class FitBitController.
 */
@RestController
@PropertySource("classpath:/fusioner-fitbit-${ewall.env:local}.properties")

public class FitBitController {

	/** The log. */
	private static final Logger logger = LoggerFactory.getLogger(FitBitController.class);
	protected MongoOperations mongoOps;
		
	@Value("${mongo.host}")
	private String host;
	
	@Value("${mongo.port}")
	private int port;
	
	@Value("${mongo.dbname}")
	private String dbname;
	
	@RequestMapping(value = "/sleepData", method = RequestMethod.GET)
	public SleepHistorySummary getSleepData(
			@RequestParam(value="username", required=true)
			String username,
			@RequestParam(value="from", required=true)
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
			DateTime from,
			@RequestParam(value="to", required=true)
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
			DateTime to) {
		
	    SleepHistorySummary sleepData = new SleepHistorySummary(username,from.toString(),to.toString(),null);
		List<FitBitEvent> sData = new ArrayList<FitBitEvent>();
		
		try {
			MongoClient mongo = new MongoClient(host, port);
	        mongoOps = new MongoTemplate(mongo, dbname);
		
	        String begin = from.toString();
	        String end = to.toString();
			Query query = query(where("user").is(username).andOperator(Criteria.where("date").gte(begin), Criteria.where("date").lt(end))).with(new Sort(Sort.Direction.ASC, "date"));
		
			sData = mongoOps.find(query, FitBitEvent.class,"sleep_summaries");
			 
			mongo.close();
		} catch (UnknownHostException e) {
			logger.error("Error retrieving sleep data from database",e);
		}
		
		sleepData.setSleepEvents(sData);
		return sleepData;
	}

	
	@RequestMapping(value = "/movementData", method = RequestMethod.GET)
	public ResponseEntity<MovementUpdate> getMovementData(
			@RequestParam(value="username", required=true)
			String username) throws Exception {

		FitbitUpdate fitbitUpdate;
		ActivitySummary activitySummary;
		
		DatabaseConnection dbConn = DatabaseLoader.connect();
		try {			
			Database db = DatabaseLoader.initDatabase(dbConn, dbname);
			DatabaseCriteria dbCriteria = new DatabaseCriteria.Equal("user",username);
			fitbitUpdate = db.selectOne(new FitbitUpdateTable(),dbCriteria,null);
			
			if(fitbitUpdate == null) {
				logger.warn("No fitbit updates found for user '"+username+"', returning empty response.");
				dbConn.close();
				return null;
			}
				
			DateTime syncDate = fitbitUpdate.getLastTrackerSyncTime();
			String syncDateString = syncDate.toString("yyyy-MM-dd");
			dbCriteria = new DatabaseCriteria.And(new DatabaseCriteria.Equal("user",username), new DatabaseCriteria.Equal("date",syncDateString));
			activitySummary = db.selectOne(new ActivitySummaryTable(), dbCriteria, null);
			
			if(activitySummary == null) {
				logger.warn("No activity summary found for user '"+username+"' and date '"+syncDateString+"', returning empty response.");
				dbConn.close();
				return null;
			}
				
		} finally {
			dbConn.close();
		}
						
		DateTime lastRetrieveTime = new DateTime(fitbitUpdate.getLastRetrieveTime());
		DateTime lastTrackerSyncTime = new DateTime(fitbitUpdate.getLastTrackerSyncTime());
		MovementUpdate movementUpdate = new MovementUpdate();
		movementUpdate.setUsername(activitySummary.getUser());
		movementUpdate.setActivityCalories(activitySummary.getActivityCalories());
		movementUpdate.setCaloriesBMR(activitySummary.getCaloriesBMR());
		movementUpdate.setCaloriesOut(activitySummary.getCaloriesOut());
		
		int complete = 0;
		if(activitySummary.isComplete()) complete = 1;
		movementUpdate.setComplete(complete);
		movementUpdate.setDate(activitySummary.getDate().toString());
		movementUpdate.setDistance(activitySummary.getDistance());
		movementUpdate.setElevation(activitySummary.getElevation());
		movementUpdate.setFairlyActiveMinutes(activitySummary.getFairlyActiveMinutes());
		movementUpdate.setFloors(activitySummary.getFloors());
		movementUpdate.setLastRetrieveTime(lastRetrieveTime.toString());
		movementUpdate.setLastTrackerSyncTime(lastTrackerSyncTime.toString());
		movementUpdate.setLightlyActiveMinutes(activitySummary.getLightlyActiveMinutes());
		movementUpdate.setSedentaryMinutes(activitySummary.getSedentaryMinutes());
		movementUpdate.setSteps(activitySummary.getSteps());
		movementUpdate.setVeryActiveMinutes(activitySummary.getVeryActiveMinutes());
		
		return new ResponseEntity<MovementUpdate>(movementUpdate, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/movementDataForDate", method = RequestMethod.GET)
	public ResponseEntity<MovementUpdate> getMovementDataForDate(
			@RequestParam(value="username", required=true)
			String username,
			@RequestParam(value="day")
			int day,
			@RequestParam(value="month")
			int month,
			@RequestParam(value="year")
			int year) throws Exception {
		
		FitbitUpdate fitbitUpdate;
		ActivitySummary activitySummary;
		
		DatabaseConnection dbConn = DatabaseLoader.connect();
		try {			
			Database db = DatabaseLoader.initDatabase(dbConn, dbname);
			DatabaseCriteria dbCriteria = new DatabaseCriteria.Equal("user",username);
			fitbitUpdate = db.selectOne(new FitbitUpdateTable(),dbCriteria,null);
			
			if(fitbitUpdate == null) {
				logger.warn("No fitbit updates found for user '"+username+"', returning empty response.");
				dbConn.close();
				return null;
			}
				
			DateTime retrieveDate = new DateTime();
			retrieveDate = retrieveDate.withYear(year);
			retrieveDate = retrieveDate.withMonthOfYear(month);
			retrieveDate = retrieveDate.withDayOfMonth(day);
			String retrieveDateString = retrieveDate.toString("yyyy-MM-dd");
			dbCriteria = new DatabaseCriteria.And(new DatabaseCriteria.Equal("user",username), new DatabaseCriteria.Equal("date",retrieveDateString));
			activitySummary = db.selectOne(new ActivitySummaryTable(), dbCriteria, null);
			
			if(activitySummary == null) {
				logger.warn("No activity summary found for user '"+username+"' and date '"+retrieveDateString+"', returning empty response.");
				dbConn.close();
				return null;
			}
				
		} finally {
			dbConn.close();
		}
						
		DateTime lastRetrieveTime = new DateTime(fitbitUpdate.getLastRetrieveTime());
		DateTime lastTrackerSyncTime = new DateTime(fitbitUpdate.getLastTrackerSyncTime());
		MovementUpdate movementUpdate = new MovementUpdate();
		movementUpdate.setUsername(activitySummary.getUser());
		movementUpdate.setActivityCalories(activitySummary.getActivityCalories());
		movementUpdate.setCaloriesBMR(activitySummary.getCaloriesBMR());
		movementUpdate.setCaloriesOut(activitySummary.getCaloriesOut());
		
		int complete = 0;
		if(activitySummary.isComplete()) complete = 1;
		movementUpdate.setComplete(complete);
		movementUpdate.setDate(activitySummary.getDate().toString());
		movementUpdate.setDistance(activitySummary.getDistance());
		movementUpdate.setElevation(activitySummary.getElevation());
		movementUpdate.setFairlyActiveMinutes(activitySummary.getFairlyActiveMinutes());
		movementUpdate.setFloors(activitySummary.getFloors());
		movementUpdate.setLastRetrieveTime(lastRetrieveTime.toString());
		movementUpdate.setLastTrackerSyncTime(lastTrackerSyncTime.toString());
		movementUpdate.setLightlyActiveMinutes(activitySummary.getLightlyActiveMinutes());
		movementUpdate.setSedentaryMinutes(activitySummary.getSedentaryMinutes());
		movementUpdate.setSteps(activitySummary.getSteps());
		movementUpdate.setVeryActiveMinutes(activitySummary.getVeryActiveMinutes());
		
		return new ResponseEntity<MovementUpdate>(movementUpdate, HttpStatus.OK);
	}
	
	
}
