package eu.ewall.servicebrick.mysettings.datamodel;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * The Class SharingDataModel.
 */
@Document(collection="mySettings")
public class MySettingsModel  {

	@Id
	private ObjectId id;
	private String username;
	private Boolean sharingFamily;
	private Boolean sharingGP;
	private int healthNotifications;
	private int houseNotifications;
	private int medicalNotifications;
	private int wallpaper;
	private int photoframe;
	private Boolean screensaver;
	private Boolean mySleep;
	private Boolean myHealth;
	private Boolean myDailylife;
	private Boolean myDailyactivity;
	private Boolean myGames;
	
	
	public MySettingsModel(String username) {
		this.username=username;
		this.sharingFamily = false;
		this.sharingGP = false;
		this.myDailyactivity = false;
		this.myDailylife = false;
		this.myGames = false;
		this.myHealth = false;
		this.mySleep = false;
		this.screensaver = false;
		this.photoframe = 0;
		this.wallpaper =0;
		this.healthNotifications = 0;
		this.houseNotifications = 0;
		this.medicalNotifications = 0;
	
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public Boolean getSharingFamily() {
		return sharingFamily;
	}

	public void setSharingFamily(Boolean sharingFamily) {
		this.sharingFamily = sharingFamily;
	}

	public Boolean getSharingGP() {
		return sharingGP;
	}

	public void setSharingGP(Boolean sharingGP) {
		this.sharingGP = sharingGP;
	}
	
	public int getHealthNotifications() {
		return healthNotifications;
	}

	public void setHealthNotifications(int healthNotifications) {
		this.healthNotifications = healthNotifications;
	}
	
	public int getHouseNotifications() {
		return houseNotifications;
	}

	public void setHouseNotifications(int houseNotifications) {
		this.houseNotifications = houseNotifications;
	}
	
	public int getMedicalNotifications() {
		return medicalNotifications;
	}

	public void setMedicalNotifications(int medicalNotifications) {
		this.medicalNotifications = medicalNotifications;
	}
	
	public int getWallpaper() {
		return wallpaper;
	}

	public void setWallpaper(int wallpaper) {
		this.wallpaper = wallpaper;
	}
	
	public int getPhotoframe() {
		return photoframe;
	}

	public void setPhotoframe(int photoframe) {
		this.photoframe = photoframe;
	}
	
	public Boolean getScreensaver() {
		return screensaver;
	}

	public void setScreensaver(Boolean screensaver) {
		this.screensaver = screensaver;
	}
	
	public Boolean getMySleep() {
		return mySleep;
	}

	public void setMySleep(Boolean mySleep) {
		this.mySleep = mySleep;
	}
	
	public Boolean getMyHealth() {
		return myHealth;
	}

	public void setMyHealth(Boolean myHealth) {
		this.myHealth = myHealth;
	}
	
	public Boolean getMyDailylife() {
		return myDailylife;
	}

	public void setMyDailylife(Boolean myDailylife) {
		this.myDailylife = myDailylife;
	}
	
	public Boolean getMyDailyactivity() {
		return myDailyactivity;
	}

	public void setMyDailyactivity(Boolean myDailyactivity) {
		this.myDailyactivity = myDailyactivity;
	}
	
	public Boolean getMyGames() {
		return myGames;
	}

	public void setMyGames(Boolean myGames) {
		this.myGames = myGames;
	}
	

}
