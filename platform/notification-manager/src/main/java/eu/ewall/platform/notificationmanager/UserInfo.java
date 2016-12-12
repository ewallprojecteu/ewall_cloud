package eu.ewall.platform.notificationmanager;

/****************************************************************
 * Copyright 2014 Ss Cyril and Methodius University in Skopje
***************************************************************/

/**
 * The Class UserInfo. Representing the list format of the users' preferences and information.
 */

import java.util.Date;

public class UserInfo {
	
	/**the user's name*/
	private String user;
	
	/**the user's ID*/
	private String userID;
	
	/** the user's location (e.g. at home, or outside)*/
	private String location;
	
	/**user's language preference*/
	private String lang;
	
	/**user's time stamp of last sent notification*/
	private Date timeStmp;
	
	/**user's default priority wait time*/
	private long DefPrior;
	
	public UserInfo(String user, String userID, String location, String lang, Date date, long DefPrior)
	 {
		 this.user = user;
		 this.userID = userID;
		 this.location = location;
		 this.lang = lang;
		 this.timeStmp = date;
		 this.DefPrior = DefPrior;
	 }
	
	public UserInfo()
	 {
		
	 }
	
	
	// get class vals
	public String getUser() 
	 {
	        return user;
	    }
	 
	 public String getUserID() 
	 {
	        return userID;
	    }
	 public String getLocation() 
	 {
	        return location;
	    }
	 
	 public String getLang() 
	 {
	        return lang;
	    }
	 
	 public Date getTimeStmp() 
	 {
	        return timeStmp;
	    }
	 
	 public long getDefPrior() 
	 {
	        return DefPrior;
	    }
	 
	// set class vals
	 
		 public boolean setUser(String user) 
		 {
		        this.user = user;
		        
		        return true;
		    }
		 
		 public boolean setUserID(String userID) 
		 {
		        this.userID = userID;
		        
		        return true;
		    }
		
		 public boolean setLocation(String location) 
		 {
		        this.location = location;
		        
		        return true;
		    }
		 
		 public boolean setLang(String lang) 
		 {
		        this.lang = lang;
		        
		        return true;
		    }
		 
		 public boolean setTimeStmp(Date date) 
		 {
		        this.timeStmp = date;
		        
		        return true;
		    }
		 
		 public boolean setDefPrior(long prior) 
		 {
		        this.DefPrior = prior;
		        
		        return true;
		    }
	 
}
