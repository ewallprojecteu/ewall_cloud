package eu.ewall.servicebrick.mysettings.controller;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;

import eu.ewall.servicebrick.mysettings.datamodel.SharingData;
import eu.ewall.servicebrick.mysettings.datamodel.MySettingsModel;
import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.profile.UserRole;
import eu.ewall.servicebrick.common.dao.ProfilingServerDao;
import eu.ewall.servicebrick.mysettings.datamodel.MyNotificationsModel;
import eu.ewall.servicebrick.mysettings.datamodel.eWallAppearanceModel;


/**
 * The Class MySettingsController.
 */
@RestController
public class MySettingsController {

	
	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;
	@Autowired
	private ProfilingServerDao profilingServerDao;
			
	@Autowired
	private MongoOperations mongoOps;
	
	private String profilingServerUrl;

	@Value("${profilingServer.url}")
	public void setProfilingServerURL(String url) {
		this.profilingServerUrl = url;
	}
	
	public MySettingsModel getMySettingsReadingByUsername(String username) {
		return mongoOps.findOne(query(where("username").is(username)), MySettingsModel.class);
	}
	
	public void updateMySettings(MySettingsModel mySettingsModel) {
		Update mySettingsUpdate = new Update();
		mySettingsUpdate.set("username", mySettingsModel.getUsername());
		mySettingsUpdate.set("sharingFamily", mySettingsModel.getSharingFamily());
		mySettingsUpdate.set("sharingGP", mySettingsModel.getSharingGP());
		mySettingsUpdate.set("myDailyactivity", mySettingsModel.getMyDailyactivity());
		mySettingsUpdate.set("myDailylife", mySettingsModel.getMyDailylife());
		mySettingsUpdate.set("myGames", mySettingsModel.getMyGames());
		mySettingsUpdate.set("myHealth", mySettingsModel.getMyHealth());
		mySettingsUpdate.set("mySleep", mySettingsModel.getMySleep());
		mySettingsUpdate.set("photoframe", mySettingsModel.getPhotoframe());
		mySettingsUpdate.set("screensaver", mySettingsModel.getScreensaver());
		mySettingsUpdate.set("wallpaper", mySettingsModel.getWallpaper());
		mySettingsUpdate.set("medicalNotifications", mySettingsModel.getMedicalNotifications());
		mySettingsUpdate.set("healthNotifications", mySettingsModel.getHealthNotifications());
		mySettingsUpdate.set("houseNotifications", mySettingsModel.getHouseNotifications());
		
				
		mongoOps.upsert(
				query(where("username").is(mySettingsModel.getUsername())), 
				mySettingsUpdate,
				MySettingsModel.class);
	}
	
//	private static final Logger log = LoggerFactory.getLogger(MySettingsController.class);

	/**
	 * Gets the sharing data.
	 *
	 * @param username            the username
	 * @return 	username            the username
	 * 			sharingFamiliy      Sharing the personal details to the family (“true” or “false” )
	 * 			sharringGP          Sharing the personal details to the GP (“true” or “false” )
	 * 			sharingCaregivers   List of the names of the caregivers to whom the sharing is enabled. 
	 * 			caregiverNames      List containing all available caregivers
	 */
	@RequestMapping(value = "/sharingdata/{username}", method = RequestMethod.GET)
	public SharingData getSharingData(
			@PathVariable String username)
			 {
		//DateTimeZone userZone = getUserTimeZone(username);
		User primaryUser = profilingServerDao.getUser(username);
		
		if(primaryUser==null)
		{
		return null;
		}
		
		MySettingsModel mySettings = getMySettingsReadingByUsername(username);
		
		if(mySettings == null)
		{
			mySettings = new MySettingsModel(username);
			updateMySettings(mySettings);
			
		}
		
		
		List<String> sharingCaregivers = new ArrayList<String>();
	    List<String> caregiverNames = new ArrayList<String>();
	    
	    sharingCaregivers = primaryUser.getCaregiversUsernamesList();
	    
	    String url = profilingServerUrl+"/users/";
		User[] users = ewallClient.getForObject(url, new User[0].getClass());
		
		for(User user : users){
			if(user.getUserRole().equals(UserRole.FORMAL_CAREGIVER)){
				caregiverNames.add(user.getUsername());
			}
			if(user.getUserRole().equals(UserRole.INFORMAL_CAREGIVER)){
				caregiverNames.add(user.getUsername());
			}
		}
			
        SharingData sd = new SharingData(username, mySettings.getSharingFamily(), mySettings.getSharingGP());
        sd.setCaregiverNames(caregiverNames);
        sd.setSharingCaregivers(sharingCaregivers);
		return sd;
	}
	
	
	@RequestMapping(value = "/sharingdata", method = RequestMethod.POST)
	public ResponseEntity<String> setSharingData(
			@RequestParam(value="username", required=true) String username,
			@RequestParam(value="sharingFamily", required=true) Boolean sharingFamily,
			@RequestParam(value="sharingGP", required=true) Boolean sharingGP,
			@RequestParam(value="sharingCaregivers", required=false) List<String> sharingCaregivers) {
		
		
		MySettingsModel mySettings = getMySettingsReadingByUsername(username);
		
		
		String url = profilingServerUrl+"/users/"+username;
		User user = ewallClient.getForObject(url, User.class,
				username);
	
		    if(sharingCaregivers!=null)
		    {
	        user.setCaregiversUsernamesList(sharingCaregivers);
		
	        ewallClient.postForEntity(url, user, User.class);
		    }
		    else
		    {
		    	
		    	int cntr = user.getCaregiversUsernamesList().size();
		    	String caregiverName;
		    	while (cntr>0)
		    	{
		    		cntr--;
		    		caregiverName = user.getCaregiversUsernamesList().get(cntr).toString();
		    		user.removeCaregiverNameFromEncompassingCaregiverNamesList(caregiverName);
		    		
		    	}
		    	
		        ewallClient.postForEntity(url, user, User.class);
		        
		    }
		    	     
		mySettings.setSharingFamily(sharingFamily);
		mySettings.setSharingGP(sharingGP);
		updateMySettings(mySettings);

		
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	
	@RequestMapping(value = "/mynotifications/{username}", method = RequestMethod.GET)
	public MyNotificationsModel getNotificationsData(
			@PathVariable String username)
			 {
		//DateTimeZone userZone = getUserTimeZone(username);
		if(profilingServerDao.getUser(username)==null)
		{
		return null;
		}
		
		MySettingsModel mySettings = getMySettingsReadingByUsername(username);
		
		
		
		if(mySettings == null)
		{
			mySettings = new MySettingsModel(username);
			updateMySettings(mySettings);
			
		}
		
		
		MyNotificationsModel nd = new MyNotificationsModel(username,mySettings.getHealthNotifications(), mySettings.getHouseNotifications(), mySettings.getMedicalNotifications());  
		
               
		return nd;
	}
	
	
	@RequestMapping(value = "/mynotifications", method = RequestMethod.POST)
	public ResponseEntity<String> setNotificationsData(
			@RequestParam(value="username", required=true) String username, 
			@RequestParam(value="healthNotifications", required=true) int healthNotifications, 
			@RequestParam(value="houseNotifications", required=true) int houseNotifications, 
			@RequestParam(value="medicalNotifications", required=true) int medicalNotifications) {
		MySettingsModel mySettings = getMySettingsReadingByUsername(username);
		
		mySettings.setHealthNotifications(healthNotifications);
		mySettings.setHouseNotifications(houseNotifications);
		mySettings.setMedicalNotifications(medicalNotifications);
		updateMySettings(mySettings);

		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/myewallappearance/{username}", method = RequestMethod.GET)
	public eWallAppearanceModel getAppearanceData(
			@PathVariable String username)
			 {
		//DateTimeZone userZone = getUserTimeZone(username);
		if(profilingServerDao.getUser(username)==null)
		{
		return null;
		}
		
		
		
		
		MySettingsModel mySettings = getMySettingsReadingByUsername(username);
		if(mySettings == null)
		{
			mySettings = new MySettingsModel(username);
			updateMySettings(mySettings);
			
		}
		eWallAppearanceModel ad = new eWallAppearanceModel(username,mySettings.getWallpaper(), mySettings.getPhotoframe(),
				mySettings.getScreensaver(), mySettings.getMySleep(), mySettings.getMyHealth(), mySettings.getMyDailylife(),
				mySettings.getMyDailyactivity(),mySettings.getMyGames());  
		
		String url = profilingServerUrl+"/users/"+username;
		User user = ewallClient.getForObject(url, User.class,
				username);
		
		int cntr = user.getApplicationNamesList().size();
		
		while (cntr>0)
		{
			cntr--;
			if(user.getApplicationNamesList().get(cntr).equals("Daily Physical Activity Monitoring"))
		{
				ad.setMyDailyactivity(true);
		}
			
			if(user.getApplicationNamesList().get(cntr).equals("Daily Functioning Monitoring"))
			{
					ad.setMyDailylife(true);
			}
			
			if(user.getApplicationNamesList().get(cntr).equals("Sleep"))
			{
					ad.setMySleep(true);
			}	
			
			if(user.getApplicationNamesList().get(cntr).equals("Healthcare Monitor"))
			{
					ad.setMyHealth(true);
			}	
			if(user.getApplicationNamesList().get(cntr).equals("Sudoku"))
			{
					ad.setMyGames(true);
			}			
			
		}
        
		return ad;
	}
	
	
	@RequestMapping(value = "/myewallappearance", method = RequestMethod.POST)
	public ResponseEntity<String> setAppearanceData(
			@RequestParam(value="username", required=true) String username,
			@RequestParam(value="wallpaper", required=true) int wallpaper,
			@RequestParam(value="photoframe", required=true) int photoframe, 
			@RequestParam(value="screensaver", required=true) Boolean screensaver, 
			@RequestParam(value="mySleep", required=true) Boolean mySleep, 
			@RequestParam(value="myHealth", required=true) Boolean myHealth, 
			@RequestParam(value="myDailylife", required=true) Boolean myDailylife, 
			@RequestParam(value="myDailyactivity", required=true) Boolean myDailyactivity, 
			@RequestParam(value="myGames", required=true) Boolean myGames ) {
		
		MySettingsModel mySettings = getMySettingsReadingByUsername(username);
		
		
		
		String url = profilingServerUrl+"/users/"+username;
		User user = ewallClient.getForObject(url, User.class,
				username);
		
         int cntr = user.getApplicationNamesList().size();
         
         mySettings.setMyDailyactivity(false);
         mySettings.setMyDailylife(false);
         mySettings.setMySleep(false);
         mySettings.setMyHealth(false);
         mySettings.setMyGames(false);
         
		while (cntr>0)
		{
			cntr--;
			if(user.getApplicationNamesList().get(cntr).equals("Daily Physical Activity Monitoring"))
		{
				mySettings.setMyDailyactivity(true);
		}
			
			if(user.getApplicationNamesList().get(cntr).equals("Daily Functioning Monitoring"))
			{
				mySettings.setMyDailylife(true);
			}
			
			if(user.getApplicationNamesList().get(cntr).equals("Sleep"))
			{
				mySettings.setMySleep(true);
			}	
			
			if(user.getApplicationNamesList().get(cntr).equals("Healthcare Monitor"))
			{
				mySettings.setMyHealth(true);
			}	
			if(user.getApplicationNamesList().get(cntr).equals("Sudoku"))
			{
				mySettings.setMyGames(true);
			}			
			
		}
		
		if(myDailyactivity==true)
		{
			if(mySettings.getMyDailyactivity().booleanValue()==false)
			user.addApplicationNameToApplicationNamesList("Daily Physical Activity Monitoring");
		}
		else
			user.removeApplicationNameFromEncompassingApplicationNamesList("Daily Physical Activity Monitoring");
		
		if(myDailylife==true)
		{
			if(mySettings.getMyDailylife().booleanValue()==false)
			user.addApplicationNameToApplicationNamesList("Daily Functioning Monitoring");
		}
		else
			user.removeApplicationNameFromEncompassingApplicationNamesList("Daily Functioning Monitoring");
		
		if(myGames==true)
		{
			if(mySettings.getMyGames().booleanValue()==false)
			user.addApplicationNameToApplicationNamesList("Sudoku");
		}
		else
			user.removeApplicationNameFromEncompassingApplicationNamesList("Sudoku");
		
		if(mySleep==true)
		{
			if(mySettings.getMySleep().booleanValue()==false)
			user.addApplicationNameToApplicationNamesList("Sleep");
		}
		else
			user.removeApplicationNameFromEncompassingApplicationNamesList("Sleep");
		
		if(myHealth==true)
		{
			if(mySettings.getMyHealth().booleanValue()==false)
			user.addApplicationNameToApplicationNamesList("Healthcare Monitor");
		}
		else
			user.removeApplicationNameFromEncompassingApplicationNamesList("Healthcare Monitor");
		
		ewallClient.postForEntity(url, user, User.class);
		
		mySettings.setMyDailyactivity(myDailyactivity);
		mySettings.setMyDailylife(myDailylife);
		mySettings.setMyGames(myGames);
		mySettings.setMyHealth(myHealth);
		mySettings.setMySleep(mySleep);
		mySettings.setPhotoframe(photoframe);
		mySettings.setScreensaver(screensaver);
		mySettings.setWallpaper(wallpaper);
		
		updateMySettings(mySettings);

		return new ResponseEntity<String>(HttpStatus.OK);
	}

	

}