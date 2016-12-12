package eu.ewall.servicebrick.caregiverwebapp.dao;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import eu.ewall.servicebrick.caregiverwebapp.model.CaregiverUserSettings;
import eu.ewall.servicebrick.caregiverwebapp.model.CaregiverUserThresholds;
import eu.ewall.servicebrick.caregiverwebapp.model.Notification;
import eu.ewall.servicebrick.caregiverwebapp.model.Notification.LocationType;
import eu.ewall.servicebrick.caregiverwebapp.model.Threshold;
import eu.ewall.servicebrick.common.dao.TimeZoneDaoSupport;
import eu.ewall.servicebrick.common.time.TimeZoneContext;
import eu.ewall.servicebrick.common.time.UserTimeZoneProvider;

/**
 * DAO that reads and writes inactivity data in Mongo DB.
 */
@Component
public class CaregiverWebappDao extends TimeZoneDaoSupport {
	
	private static final Logger log = LoggerFactory.getLogger(CaregiverWebappDao.class);
	
	@Value("${maxNotifications}")
	private int maxNotifications;

	@Autowired
	public CaregiverWebappDao(MongoOperations mongoOps, TimeZoneContext timeZoneCtx, 
			UserTimeZoneProvider userTimeZoneProvider) {
		super(mongoOps, timeZoneCtx, userTimeZoneProvider);
	}
	
	public void initializeDefaults(CaregiverUserThresholds defaultThresholds){
		log.info("Initializing default values in the db...");
		CaregiverUserThresholds savedDefaultThresholds = readThresholds("default", "default");
		if(savedDefaultThresholds == null || savedDefaultThresholds.getThresholds().size()!=defaultThresholds.getThresholds().size()){
			insertThresholds(defaultThresholds);
		} else {
			boolean changed = false;
			for(Threshold defaultItem : defaultThresholds.getThresholds()){
				if(!savedDefaultThresholds.getThresholds().contains(defaultItem)){
					savedDefaultThresholds.getThresholds().add(defaultItem);
					changed = true;
				}
			}
			if(changed){
				insertThresholds(savedDefaultThresholds);
			}
		}
	}
	
	public CaregiverUserSettings readSettings(String caregiverUsername, String primaryUserUsername) {
		Query query = query(where("caregiverUsername").is(caregiverUsername).andOperator(Criteria.where("primaryUserUsername").is(primaryUserUsername)));
		CaregiverUserSettings settings = (CaregiverUserSettings) mongoOps.findOne(query, CaregiverUserSettings.class);
		return settings;
	}
	
	
	public void insertSettings(CaregiverUserSettings settings) {
		Update settingsUpdate = new Update();
		settingsUpdate.set("caregiverUsername", settings.getCaregiverUsername());
		settingsUpdate.set("primaryUserUsername", settings.getPrimaryUserUsername());
		settingsUpdate.set("favourite", settings.isFavourite());
		settingsUpdate.set("lastViewed", settings.getLastViewed());
		mongoOps.upsert(
					query(where("caregiverUsername").is(settings.getCaregiverUsername()).andOperator(Criteria.where("primaryUserUsername").is(settings.getPrimaryUserUsername()))), 
					settingsUpdate,
					CaregiverUserSettings.class);
	}

	public CaregiverUserThresholds readThresholds(String caregiverUsername, String primaryUserUsername) {
		Query query = query(where("caregiverUsername").is(caregiverUsername).andOperator(Criteria.where("primaryUserUsername").is(primaryUserUsername)));
		CaregiverUserThresholds thresholds = (CaregiverUserThresholds) mongoOps.findOne(query, CaregiverUserThresholds.class);
		return thresholds;
	}

	public List<CaregiverUserThresholds> readThresholds(String primaryUserUsername) {
		Query query = query(where("primaryUserUsername").is(primaryUserUsername));
		List<CaregiverUserThresholds> thresholds = (List<CaregiverUserThresholds>) mongoOps.find(query, CaregiverUserThresholds.class);
		return thresholds;
	}

	public void insertThresholds(CaregiverUserThresholds thresholds) {
		Update thresholdsUpdate = new Update();
		thresholdsUpdate.set("caregiverUsername", thresholds.getCaregiverUsername());
		thresholdsUpdate.set("primaryUserUsername", thresholds.getPrimaryUserUsername());
		thresholdsUpdate.set("thresholds", thresholds.getThresholds());
		mongoOps.upsert(
					query(where("caregiverUsername").is(thresholds.getCaregiverUsername()).andOperator(Criteria.where("primaryUserUsername").is(thresholds.getPrimaryUserUsername()))), 
					thresholdsUpdate,
					CaregiverUserThresholds.class);
	}
	
	public void updateThreshold(String caregiverUsername, String primaryUserUsername, Threshold threshold){
		CaregiverUserThresholds thresholds = readThresholds(caregiverUsername, primaryUserUsername);
		Threshold storedThreshold = thresholds.getThresholdByName(threshold.getName());
		storedThreshold.setPriority(threshold.getPriority());
		storedThreshold.setTargetMin(threshold.getTargetMin());
		storedThreshold.setTargetMax(threshold.getTargetMax());
		storedThreshold.setVisible(threshold.isVisible());
		storedThreshold.setOverrideDefault(threshold.isOverrideDefault());
		insertThresholds(thresholds);
	}
	public List<Notification> readNotificationsByCaregiverAndPrimaryUser(String caregiverUsername, String primaryUserUsername) {
		Query query = query(where("caregiverUsername").is(caregiverUsername).andOperator(Criteria.where("username").is(primaryUserUsername))).with(new Sort(Sort.Direction.DESC, "detectionDate")).limit(maxNotifications);
		List<Notification> notifications = (List<Notification>) mongoOps.find(query, Notification.class);
		return notifications;
	}

	public List<Notification> readNotificationsByPrimaryUser(String primaryUserUsername) {
		Query query = query(where("username").is(primaryUserUsername)).with(new Sort(Sort.Direction.DESC, "detectionDate")).limit(maxNotifications);
		List<Notification> notifications = (List<Notification>) mongoOps.find(query, Notification.class);
		return notifications;
	}

	public List<Notification> readNotificationsByCaregiver(String caregiverUsername) {
		Query query = query(where("caregiverUsername").is(caregiverUsername)).with(new Sort(Sort.Direction.DESC, "detectionDate")).limit(maxNotifications);
		List<Notification> notifications = (List<Notification>) mongoOps.find(query, Notification.class);
		return notifications;
	}

	public void insertNotifications(List<Notification> notifications) {
		mongoOps.insert(notifications, Notification.class);
	}

	public Notification readNotificationByUUID(String uuid) {
		Query query = query(where("uuid").is(uuid));
		Notification notification = (Notification) mongoOps.findOne(query, Notification.class);
		return notification;
	}

	public void updateNotificationReadingDate(String uuid, DateTime lastReadingDate){
		Update notificationUpdate = new Update();
		Notification notification = readNotificationByUUID(uuid);
		notificationUpdate.set("lastReadingDate", lastReadingDate);
		notificationUpdate.set("unread", false);
		if(notification.getFirstReadingDate()==null){
			notificationUpdate.set("firstReadingDate", lastReadingDate);
		}
		mongoOps.updateFirst(
					query(where("uuid").is(uuid)), 
					notificationUpdate,
					Notification.class);
		
	}
	
	public void updateNotificationStatus(Notification notification){
		Update notificationUpdate = new Update();
		notificationUpdate.set("unread", notification.isUnread());
		notificationUpdate.set("lastReadingDate", new DateTime());
		if(notification.getFirstReadingDate()==null){
			notificationUpdate.set("firstReadingDate", new DateTime());
		}
		mongoOps.updateFirst(
					query(where("uuid").is(notification.getUuid())), 
					notificationUpdate,
					Notification.class);
		
	}

	public Notification findLastNotificationByType(String caregiverUsername, String primaryUserUsername, String thresholdName, LocationType locationType) {
		Query query = new Query();
		query.addCriteria(Criteria.where("caregiverUsername").is(caregiverUsername)
				.andOperator(Criteria.where("username").is(primaryUserUsername), Criteria.where("thresholdName").is(thresholdName), Criteria.where("locationType").is(locationType)))
				.limit(1).with(new Sort(Sort.Direction.DESC, "detectionDate"));
		Notification notification = (Notification) mongoOps.findOne(query, Notification.class);
		if(notification!=null) {
			return notification;
		}
		return null;		
	}

}
