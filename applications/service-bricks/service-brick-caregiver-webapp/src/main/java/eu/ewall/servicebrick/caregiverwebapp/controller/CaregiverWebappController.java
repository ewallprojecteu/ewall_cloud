package eu.ewall.servicebrick.caregiverwebapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.servicebrick.caregiverwebapp.dao.CaregiverWebappDao;
import eu.ewall.servicebrick.caregiverwebapp.model.CaregiverUserSettings;
import eu.ewall.servicebrick.caregiverwebapp.model.CaregiverUserThresholds;
import eu.ewall.servicebrick.caregiverwebapp.model.Notification;
import eu.ewall.servicebrick.caregiverwebapp.model.Threshold;
import eu.ewall.servicebrick.common.dao.ProfilingServerDao;
import eu.ewall.servicebrick.common.model.CWAUser;

@RestController
public class CaregiverWebappController {

	private static final Logger log = LoggerFactory.getLogger(CaregiverWebappController.class);
	
	private ProfilingServerDao profilingServerDao;
	private CaregiverWebappDao caregiverWebappDao;


	
	@Autowired
	public CaregiverWebappController(ProfilingServerDao profilingServerDao, CaregiverWebappDao caregiverWebappDao) {
		this.profilingServerDao = profilingServerDao;
		this.caregiverWebappDao = caregiverWebappDao;
	}

	/**
	 * Endpoint which returns a list of primary user profiles, enriched with custom properties related to
	 * the relationship between a specific caregiver and a specific primary user, namely:
	 * <li>favourite: defines if the user has been marked as favourite by the caregiver from the caregiver webapp</li> 
	 * <li>lastViewed: report that last time the caregiver accessed the user info from the caregiver webapp</li> 
	 * 
	 * @param caregiverUsername
	 * @return profiles of the primary users assigned to the specified caregiver (as they are in the eWALL data management layer) 
	 * enriched with properties specific to the caregiver webapp 
	 */
	@RequestMapping(value = "v1/{caregiverUsername}/primaryusers", method = RequestMethod.GET)
	public List<CWAUser> getPrimaryUsersExtendedProfile(@PathVariable String caregiverUsername) {
		long start = System.currentTimeMillis();
		List<CWAUser> users = profilingServerDao.getPrimaryUsersExtendedProfile(caregiverUsername);
		List<CWAUser> returnList = new ArrayList<CWAUser>();
		for(User user : users){
			CWAUser cwaUser = (CWAUser) user;
			
			CaregiverUserSettings settings = caregiverWebappDao.readSettings(caregiverUsername, cwaUser.getUsername());
			if(settings != null) {
				cwaUser.setFavourite(settings.isFavourite());
				cwaUser.setLastViewed(settings.getLastViewed());
			} else {
				cwaUser.setFavourite(false);
			}
			returnList.add(cwaUser);
		}
		
		log.debug("getPrimaryUsersExtendedProfile() call took " + (System.currentTimeMillis() - start) + "ms");
		return returnList;
	}
	
	@RequestMapping(value = "v1/{caregiverUsername}/primaryuser/{primaryUserUsername}", method = RequestMethod.GET)
	public CWAUser getPrimaryUserExtendedProfile(@PathVariable String caregiverUsername, @PathVariable String primaryUserUsername) {
		long start = System.currentTimeMillis();
		CWAUser user = profilingServerDao.getCWAUser(primaryUserUsername);
		CaregiverUserSettings settings = caregiverWebappDao.readSettings(caregiverUsername, user.getUsername());
		if(settings != null) {
			settings.setLastViewed(new DateTime());
			user.setFavourite(settings.isFavourite());
			user.setLastViewed(new DateTime());			
		} else {
			settings = new CaregiverUserSettings(caregiverUsername, primaryUserUsername);
			settings.setLastViewed(new DateTime());
			user.setLastViewed(new DateTime());			
			user.setFavourite(false);
		}
		caregiverWebappDao.insertSettings(settings);
		log.debug("getPrimaryUserExtendedProfile() call took " + (System.currentTimeMillis() - start) + "ms");
		return user;
	}
	
	/**
	 * Updates the fields "favourite" and "lastViewed" for the couple caregiver-primaryuser
	 * @param caregiverUsername
	 * @param primaryUserUsername
	 * @param favourite
	 * @param lastViewed
	 * @return http response
	 */
	@RequestMapping(value = "v1/{caregiverUsername}/primaryusers/{primaryUserUsername}", method = RequestMethod.POST)
	public ResponseEntity<String>  updateSettings(@PathVariable String caregiverUsername, @PathVariable String primaryUserUsername, @RequestParam("favourite") boolean favourite) {
		long start = System.currentTimeMillis();
		//We do not check for the existence of the two actors, since they are passed to the application in the previous calls
		
		//This should not happen as they are part of the url
		if(caregiverUsername == null || primaryUserUsername == null) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

		CaregiverUserSettings settings = caregiverWebappDao.readSettings(caregiverUsername, primaryUserUsername);
		if(settings != null) {
			settings.setLastViewed(new DateTime());
			settings.setFavourite(favourite);
		} else {
			settings = new CaregiverUserSettings(caregiverUsername, primaryUserUsername);
			settings.setLastViewed(new DateTime());
			settings.setFavourite(favourite);
		}
		
		caregiverWebappDao.insertSettings(settings);
		
		
		log.debug("updateSettings() call took " + (System.currentTimeMillis() - start) + "ms");
		return new ResponseEntity<String>(HttpStatus.OK);
	}
		
	@RequestMapping(value = "v1/{caregiverUsername}/primaryusers/{primaryUserUsername}/thresholds", method = RequestMethod.GET)
	public CaregiverUserThresholds getThresholds(@PathVariable String caregiverUsername, @PathVariable String primaryUserUsername) {
		long start = System.currentTimeMillis();

		CaregiverUserThresholds thresholds = caregiverWebappDao.readThresholds(caregiverUsername, primaryUserUsername);
		
		//If there are no threshold for this user yet, we create them based on the defaults 
		if(thresholds == null){
			thresholds = caregiverWebappDao.readThresholds("default", "default");
			thresholds.setCaregiverUsername(caregiverUsername);
			thresholds.setPrimaryUserUsername(primaryUserUsername);
			caregiverWebappDao.insertThresholds(thresholds);
		}
		
		log.debug("getThresholds() call took " + (System.currentTimeMillis() - start) + "ms");
		return thresholds;
	}

	@RequestMapping(value = "v1/{caregiverUsername}/primaryusers/{primaryUserUsername}/thresholds", method = RequestMethod.PUT)
	public ResponseEntity<String>  updateThresholds(@PathVariable String caregiverUsername, @PathVariable String primaryUserUsername, @RequestBody CaregiverUserThresholds thresholds) {
		long start = System.currentTimeMillis();
		//We do not check for the existence of the two actors, since they are passed to the application in the previous calls
		
		//This should not happen as they are part of the url
		if(caregiverUsername == null || primaryUserUsername == null) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		thresholds.setCaregiverUsername(caregiverUsername);
		thresholds.setPrimaryUserUsername(primaryUserUsername);
		caregiverWebappDao.insertThresholds(thresholds);
		
		log.debug("updateThresholds() call took " + (System.currentTimeMillis() - start) + "ms");
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "v1/{caregiverUsername}/primaryusers/{primaryUserUsername}/thresholds/threshold", method = RequestMethod.POST)
	public ResponseEntity<String> updateThreshold(@PathVariable String caregiverUsername, @PathVariable String primaryUserUsername, @RequestBody Threshold threshold) {
		long start = System.currentTimeMillis();
		log.info("Updating settings for threshold " + threshold.getName());
		caregiverWebappDao.updateThreshold(caregiverUsername, primaryUserUsername, threshold);
		log.debug("updateThreshold() call took " + (System.currentTimeMillis() - start) + "ms");
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "v1/{caregiverUsername}/notifications", method = RequestMethod.GET)
	public ArrayList<Notification> getNotificationsByCaregiver(@PathVariable String caregiverUsername) {
		long start = System.currentTimeMillis();
		log.info("Retrieving notifications for " + caregiverUsername);
		ArrayList<Notification> notifications = (ArrayList<Notification>) caregiverWebappDao.readNotificationsByCaregiver(caregiverUsername);
		log.debug("getNotificationsByCaregiver() call took " + (System.currentTimeMillis() - start) + "ms");
		return notifications;
	}
	
	@RequestMapping(value = "v1/{caregiverUsername}/notifications/{primaryUserUsername}", method = RequestMethod.GET)
	public ArrayList<Notification> getNotificationsByCaregiverAndPrimaryUser(@PathVariable String caregiverUsername, @PathVariable String primaryUserUsername) {
		long start = System.currentTimeMillis();
		log.info("Retrieving notifications for caregiver " + caregiverUsername + " related to primary user " + primaryUserUsername);
		ArrayList<Notification> notifications = (ArrayList<Notification>) caregiverWebappDao.readNotificationsByCaregiverAndPrimaryUser(caregiverUsername, primaryUserUsername);
		log.debug("getNotificationsByCaregiverAndPrimaryUser() call took " + (System.currentTimeMillis() - start) + "ms");
		return notifications;
	}
	
	@RequestMapping(value = "v1/notifications/{primaryUserUsername}", method = RequestMethod.GET)
	public ArrayList<Notification> getNotificationsByPrimaryUser(@PathVariable String primaryUserUsername) {
		long start = System.currentTimeMillis();
		log.info("Retrieving notifications related to " + primaryUserUsername);
		ArrayList<Notification> notifications = (ArrayList<Notification>) caregiverWebappDao.readNotificationsByPrimaryUser(primaryUserUsername);
		log.debug("getNotificationsByPrimaryUser() call took " + (System.currentTimeMillis() - start) + "ms");
		return notifications;
	}
	
	@RequestMapping(value = "v1/notifications/notification", method = RequestMethod.POST)
	public ResponseEntity<String> updateNotificationStatus(@RequestBody Notification notification) {
		long start = System.currentTimeMillis();
		log.info("Updating reading status for notification " + notification.getUuid());
		caregiverWebappDao.updateNotificationStatus(notification);
		log.debug("updateNotificationStatus() call took " + (System.currentTimeMillis() - start) + "ms");
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	

}
