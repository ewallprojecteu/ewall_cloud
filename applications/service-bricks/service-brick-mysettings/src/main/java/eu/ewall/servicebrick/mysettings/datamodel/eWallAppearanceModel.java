package eu.ewall.servicebrick.mysettings.datamodel;

/**
 * The Class SharingDataModel.
 */
public class eWallAppearanceModel  {

    private String username;	
    private Integer wallpaper;
    private Integer photoframe;
    private Boolean screensaver;
    private Boolean mySleep;
    private Boolean myHealth;
    private Boolean myDailylife;
    private Boolean myDailyactivity;
    private Boolean myGames;
    
    public eWallAppearanceModel(String username) {
		this.username=username;
	}
	
    public eWallAppearanceModel(String username, Integer wallpaper, Integer photoframe, Boolean screensaver,
    		Boolean mySleep,Boolean myHealth, Boolean myDailylife, Boolean myDailyactivity, Boolean myGames) 
    {
		this.username=username;
		this.wallpaper=wallpaper;
		this.photoframe=photoframe;
		this.screensaver=screensaver;
		this.mySleep=mySleep;
		this.myHealth=myHealth;
		this.myDailylife=myDailylife;
		this.myDailyactivity=myDailyactivity;
		this.myGames=myGames;
	}
    
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
	public Integer getWallpaper() {
		return wallpaper;
	}

	public void setWallpaper(Integer wallpaper) {
		this.wallpaper = wallpaper;
	}
	
	public Integer getPhotoframe() {
		return photoframe;
	}

	public void setPhotoframe(Integer photoframe) {
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
