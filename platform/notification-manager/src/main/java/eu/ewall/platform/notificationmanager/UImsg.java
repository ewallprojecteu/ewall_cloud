package eu.ewall.platform.notificationmanager;

/****************************************************************
 * Copyright 2014 Ss Cyril and Methodius University in Skopje
***************************************************************/

/**
 * The Class  UImsg. Representing the message format of the notifications
 */

public class UImsg {

	/** the user's name	 */
	private String user;
	
	/**the user's ID*/
	private String userID;
	
	/**the date stamp of the given notification*/
	private  String date;
	
	/**the time stamp of the given notification*/
	private  String time;
	
	/**the ID of the given notification*/
	private  String nID;
	
	/**the type of the notification (Recommendation, Reminder or Appointment)*/
	private  String type;
	
	/**the notification title*/
	private  String title;
	
	/**the notification content*/
	private  String content;
	
	/** used for internal processing*/
	private  boolean fullR1 = false;
	
	/** used for internal processing*/
	private  boolean fullR2 = false;
	
	/** priority level of the notification*/ 
	private String priority;
	
	public UImsg(String user, String userID, String date, String time, String type, String title, String content, String nID, String priority)
	 {
		 this.user = user;
		 this.userID = userID;
		 this.date = date;
		 this.time = time; 
		 this.type = type;
		 this.title = title;
		 this.content = content;
		 this.nID = nID;
		 this.fullR1 = true;
		 this.fullR2 = true;
		 this.priority = priority;
	 }
	
	public UImsg()
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
	
	public boolean getFR1() 
	 {
	        return fullR1;
	    }
	
	public boolean getFR2() 
	 {
	        return fullR2;
	    }
	
	public String getDate() 
	 {
	        return date;
	    }
	 
	 public String getTime() 
	 {
	        return time;
	    }
	 
	 public String getNID() 
	 {
	        return nID;
	    }
	 
	 public String getType() 
	 {
	        return type;
	    }
	 
	 public String getTitle() 
	 {
	        return title;
	    }
	 
	 public String getContent() 
	 {
	        return content;
	    }
	 
	 public double getPriority() 
	 {
	     return Double.parseDouble(priority);   
	     
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
	 
	 public boolean setDate(String date) 
	 {
	        this.date = date;
	        
	        return true;
	    }
	 
	 public boolean setTime(String time) 
	 {
	        this.time = time;
	        
	        return true;
	    }
	 
	 public boolean setNID(String nID) 
	 {
	      this.nID =nID;  
		  return true;
	    }
	 
	 public boolean setType(String type) 
	 {
	        this.type = type;
	        
	        return true;
	    }
	 
	 public boolean setTitle(String title) 
	 {
	        this.title = title;
	        return true;
	    }
	 
	 public boolean setContent(String cont) 
	 {
	        this.content= cont;
	        
	        return true;
	    }
	 
	 public boolean setFullR1(boolean bool) 
	 {
	        
		 return this.fullR1 = bool;
	        
	    } 
	 
	 public boolean setFullR2(boolean bool) 
	 {
	        
		 return this.fullR2 = bool;
	        
	    } 
	 
	 public boolean setPriority(double val) 
	 {
	        
		  this.priority = String.valueOf(val);
		  return true;
	        
	    } 
	
}
