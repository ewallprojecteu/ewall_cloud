package eu.ewall.servicebrick.common.dao;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;

import eu.ewall.servicebrick.common.model.AccelerometerUpdate;
import eu.ewall.servicebrick.common.model.EmotionUpdate;
import eu.ewall.servicebrick.common.model.NotificationsUpdates;
import eu.ewall.servicebrick.common.model.SensorsUpdates;
import eu.ewall.servicebrick.common.model.DomoticsUpdates;
import eu.ewall.servicebrick.common.model.FitBitActivityUpdate;
import eu.ewall.servicebrick.common.model.FitBit_Updates;


/**
 * DAO that reads and writes timestamp of updates in Mongo DB.
 */
public class DataManagerUpdatesDao {
	private static Logger log = LoggerFactory.getLogger(DataManagerUpdatesDao.class);
	
	
	@Autowired
	private MongoOperations mongoOps;
		
	public void updateAccelerometerReading(AccelerometerUpdate physicalActivityUpdate) {
		log.debug("Updating accelerometer reading for user: " + physicalActivityUpdate.getUsername());
		Update accelerometerUpdate = new Update();
		accelerometerUpdate.set("lastAccelerometerUpdate", physicalActivityUpdate.getLastAccelerometerUpdate().toDate());
		accelerometerUpdate.set("lastActivityType", physicalActivityUpdate.getLastActivityType());
		accelerometerUpdate.set("lastInactivityState", physicalActivityUpdate.getLastInactivityState());
		accelerometerUpdate.set("lastStepsValue", physicalActivityUpdate.getLastStepsValue());
		mongoOps.upsert(
				query(where("username").is(physicalActivityUpdate.getUsername())), 
				accelerometerUpdate,
				AccelerometerUpdate.class);
	}
	
	public void updateFitBitReading(FitBitActivityUpdate fitbitActivityUpdate) {
		log.debug("Updating FitBit reading for user: " + fitbitActivityUpdate.getUsername());
		Update fitbitUpdate = new Update();
		fitbitUpdate.set("lastSteps", fitbitActivityUpdate.getLastSteps());
		fitbitUpdate.set("lastCalories", fitbitActivityUpdate.getLastCalories());
		fitbitUpdate.set("lastDistance", fitbitActivityUpdate.getLastDistance());
		fitbitUpdate.set("lastRetrieveTime", fitbitActivityUpdate.getLastRetrieveTime());
		fitbitUpdate.set("lastTrackerSyncTime", fitbitActivityUpdate.getLastTrackerSyncTime());
		mongoOps.upsert(
				query(where("username").is(fitbitActivityUpdate.getUsername())), 
				fitbitUpdate,
				FitBitActivityUpdate.class);
	}
	
	public AccelerometerUpdate getAccelerometerReadingByUsername(String username) {
		log.debug("Getting last accelerometer reading for user: " + username);
		return mongoOps.findOne(query(where("username").is(username)), AccelerometerUpdate.class);
	}
	
	public FitBitActivityUpdate getFitBitActivityReadingByUsername(String username) {
		log.debug("Getting last FitBit reading for user: " + username);
		return mongoOps.findOne(query(where("username").is(username)), FitBitActivityUpdate.class);
	}
	
	public void updateMoodReading(EmotionUpdate visualsensingUpdate) {
		log.debug("Updating visual sensing reading for user: " + visualsensingUpdate.getUsername());
		Update moodUpdate = new Update();
		moodUpdate.set("lastMoodUpdate", visualsensingUpdate.getLastMoodUpdate().toDate());
		moodUpdate.set("lastTrack_id", visualsensingUpdate.getLastTrack_id());
		moodUpdate.set("lastEmotion", visualsensingUpdate.getLastEmotion());
		moodUpdate.set("lastEmotionConf", visualsensingUpdate.getLastEmotionConf());
		moodUpdate.set("lastValence", visualsensingUpdate.getLastValence());
		moodUpdate.set("lastArousal", visualsensingUpdate.getLastArousal());
		mongoOps.upsert(
				query(where("username").is(visualsensingUpdate.getUsername())), 
				moodUpdate,
				EmotionUpdate.class);
	}
	
	public EmotionUpdate getMoodReadingByUsername(String username) {
		log.debug("Getting last mood reading for user: " + username);
		return mongoOps.findOne(query(where("username").is(username)), EmotionUpdate.class);
	}
	
	public void updateSensorsReading(SensorsUpdates sensorsUpdate) {
		log.info("Updating sensors reading for user: " + sensorsUpdate.getUsername());
		mongoOps.save(sensorsUpdate);
	}
	
	public SensorsUpdates getSensorsReadingByUsername(String username) {
		log.info("Getting last sensors reading for user: " + username);
		return mongoOps.findOne(query(where("username").is(username)), SensorsUpdates.class);
	}
	
	public void updateDomoticsReading(DomoticsUpdates domoticsUpdate) {
		log.info("Updating domotics reading for user: " + domoticsUpdate.getUsername());
		mongoOps.save(domoticsUpdate);
	}
	
	public DomoticsUpdates getDomoticsReadingByUsername(String username) {
		log.info("Getting last domotics reading for user: " + username);
		return mongoOps.findOne(query(where("username").is(username)), DomoticsUpdates.class);
	}
	
	public NotificationsUpdates getNotificationsReadingByUsername(String username) {
		log.info("Getting last notifications reading for user: " + username);
		return mongoOps.findOne(query(where("username").is(username)), NotificationsUpdates.class);
	}

	public void updateNotificationsReading(NotificationsUpdates notificationsUpdates) {
		log.info("Updating notifications reading for user: " + notificationsUpdates.getUsername());
		mongoOps.save(notificationsUpdates);
	}
	
	public FitBit_Updates getFitBit_UpdatesReadingByUsername(String username) {
		log.info("Getting last notifications reading for user: " + username);
		return mongoOps.findOne(query(where("username").is(username)), FitBit_Updates.class);
	}
	
	
	
	
}
