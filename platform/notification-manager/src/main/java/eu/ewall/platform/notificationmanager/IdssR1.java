package eu.ewall.platform.notificationmanager;

/****************************************************************
 * Copyright 2014 Ss Cyril and Methodius University in Skopje
***************************************************************/

/**
 * The Class structure IdssR1. Representing the message format from Reasoner1
 */

public class IdssR1 
{
	/** Name of the user i.e. real name*/
	private final String user;
	
	/** User ID i.e. UserName in the system*/
	private final String userID;
	
	/** Date when the message was generated*/
	private final String date;
	
	/** Time instance when the message was generated*/
	private final String time;
	
	/** ID of the given notification*/
	private final String nID;
	
	/** Priority level of the given notification*/
	private final String priority;
	
	 public IdssR1(String user, String userID, String date, String time, String nID, String priority)
	 {
		 this.user = user;
		 this.userID = userID;
		 this.date = date;
		 this.time = time;
		 this.nID = nID;
		 this.priority = priority;
	 }
	 
	 public String getUser() 
	 {
	        return user;
	    }
	 
	 public String getUserID() 
	 {
	        return userID;
	    }
	 
	 public String getDate() 
	 {
	        return date;
	    }
	 
	 public String getTime() 
	 {
	        return time;
	    }
	 
	 public int getNID() 
	 {
	        return Integer.parseInt(nID);
	    }
	 
	 public double getPriority() 
	 {
	     return Double.parseDouble(priority);   
	     
	 }

}
