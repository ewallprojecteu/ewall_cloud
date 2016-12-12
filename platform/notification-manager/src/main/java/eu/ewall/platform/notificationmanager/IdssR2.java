package eu.ewall.platform.notificationmanager;

/****************************************************************
 * Copyright 2014 Ss Cyril and Methodius University in Skopje
***************************************************************/

/**
 * The Class structure IdssR2. Representing the message format from Reasoner2
 */

public class IdssR2 
{
	/** Name of the user i.e. real name*/
	private final String user;
	
	/** User ID i.e. UserName in the system*/
	private final String userID;
	
	/** Type of graphical representation needed for the notification*/
	private final String type;
	
	/** Title of the given notification*/
	private final String title;
	
	/** Content of the given notification*/
	private final String content;
	
	/** ID of the given notification*/
	private final String nID;
	
	public IdssR2(String user, String userID, String type, String title, String content, String nID)
	 {
		 this.user = user;
		 this.userID = userID;
		 this.type = type;
		 this.title = title;
		 this.content = content;
		 this.nID = nID;
	 }
	
	 
	public String getUser() 
	 {
	        return user;
	    }
	 
	 public String getUserID() 
	 {
	        return userID;
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
	 
	 public int getNID() 
	 {
	        return Integer.parseInt(nID);
	    }

}
