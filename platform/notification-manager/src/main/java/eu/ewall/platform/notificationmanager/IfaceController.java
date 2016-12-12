package eu.ewall.platform.notificationmanager;

/****************************************************************
 * Copyright 2014 Ss Cyril and Methodius University in Skopje
***************************************************************/


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import eu.ewall.platform.commons.datamodel.message.MessageType;
import eu.ewall.platform.commons.datamodel.message.NotificationMessage;
import eu.ewall.platform.commons.datamodel.message.NotificationContentMsg;
import eu.ewall.platform.commons.datamodel.message.NotificationContentFeedbackMsg;
import eu.ewall.platform.commons.datamodel.message.NotificationStatements;
import eu.ewall.platform.notificationmanager.dao.NotificationMongo;
import eu.ewall.platform.notificationmanager.dao.NotificationDBcontent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;


/**
 * The Class IfaceController
 */

@RestController
public class IfaceController {
	
	
	
	@Value("${priority.dur}")
	private String PriorInterval; 
	
	
	@Value("${priority.thr}")
	private String PriorThr;
	
	@Value("${reason.info}")
	private String reasoningEnabled;
	
	@Value("${email.host}")
	private String emailHost;
	
	@Value("${email.user}")
	private String emailUser;
	
	@Value("${email.pass}")
	private String emailPass;
	
	
	@Autowired
	private ProfServConn profServConn;
	
	@Autowired
	private NotificationMongo NotificationMongo;
	
	/**
	 *  User's email address. Required for testing purposes.
	 */
	public String userAddrsT = "";
	/**
	 *  Relatives' email addresses. Required for testing purposes.
	 */
	public List<String> relativesT = new ArrayList<String>();
	/**
	 *  Caregivers' email addresses. Required for testing purposes.
	 */
	public List<String> caregiversT = new ArrayList<String>();
	
	public String nIDcnt = "";
	
	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(IfaceController.class);
		
	
	/**
	 * Configures the emails used for testing purposes. In the final version this function will be obsolete, as the emails will be gathered from the user profile and/or web portal 
	 * 
	 * @param user
	 * 			the user's email
	 * @param relative
	 * 			email from a given relative
	 * @param caregvr
	 * 			id of the device requesting the notification
	 * @param reset
	 * 			used for deleting all previously gathered address (when reset="Yes")
	 */
	@RequestMapping(value = "/setEmail", method = RequestMethod.POST)
	public void setEmailAdd(@RequestParam(value="userEM", required=true, defaultValue="none") String user,
    		@RequestParam(value="relatEM", required=true, defaultValue="none") String relative,
    		@RequestParam(value="caregivEM", required=true, defaultValue="none") String caregvr,
    		@RequestParam(value="reset", required=true, defaultValue="No") String reset) 
    		 
    {
		
		if (user==null || "".equals(user) || "none".equals(user))
		{
			//System.out.println("No New User address");
		}
		else
		{
			userAddrsT = user;
			//System.out.println("New User address added>>"+user);
		}
		
		if (relative==null || "".equals(relative) || "none".equals(relative))
		{
			//System.out.println("No New relative address");
		}
		else
		{
			relativesT.add(relative);
			//System.out.println("New User relative added>>"+relative);
		}
		
		if (caregvr==null || "".equals(caregvr) || "none".equals(caregvr))
		{
			 //System.out.println("No New caregiver address");
		}
		else
		{
			caregiversT.add(caregvr);
			//System.out.println("New User caregiver added>>"+caregvr);
		}
		
		
		if ("Yes".equals(reset) || "yes".equals(reset))
		{
			userAddrsT ="";
			relativesT.clear();
			caregiversT.clear();
			//System.out.println("All adresses have been deleted");
		}
		
    }
	
	
	/**
	 * Converts the notification message from the predefined structure into an url string
	 * 
	 * @param msg
	 *        the message intended for the User Interface
	 * @return url string of the respective message
	 */
	public String UIsend(UImsg msg)
	{
		String usrLoc = "0";
		String usrLang = "eng";
		for (int i=0;i<uInfo.size();i++)
		{
			if(msg.getUserID().equals(uInfo.get(i).getUserID()))
    		{
    			usrLoc = uInfo.get(i).getLocation();
    			usrLang = uInfo.get(i).getLang();
    		}
		}
		
		String url = "user="+msg.getUser()+"&notID="+msg.getNID()+"&position="+usrLoc+"&lang="+usrLang+"&date="+msg.getDate()+"&time="+msg.getTime()
				+"&type="+msg.getType()+"&title="+msg.getTitle()+"&content="+msg.getContent();
		
		
		return url;
	}
	
	/**
	 * Sends the notification as email 
	 * 
	 * @param msg
	 *        the message intended for the User Interface
	 */
	public void UIsendM(UImsg msg)
	{
	    // use code to send e-mail here
	}
	
	
	/**
	 * Replies with a notification intended for the given user i.e. User Interface device 
	 * 
	 * @param user
	 * 			the user's username
	 * @param userID
	 * 			the user's ID
	 * @param device
	 * 			id of the device requesting the notification
	 * @return the respective notification
	 */
	@RequestMapping(value = "/getNot", method = RequestMethod.GET)
	public String sendNot(@RequestParam(value="user", required=true, defaultValue="BOB") String user,
    		@RequestParam(value="userID", required=true, defaultValue="JAMES007BOB") String userID,
    		@RequestParam(value="device", required=true, defaultValue="0") String device) 
    		 
    {
		String resp = "NaN";
		
		int index = 0;
		long thrTime = 0;
		long defPrior = 0;
		
		String usrLoc = "0";
		for (int i=0;i<uInfo.size();i++)
		{
			if(userID.equals(uInfo.get(i).getUserID()))
    		{
				UInfo(user, userID,profServConn.getUsrLoc(user));
				
    			usrLoc = uInfo.get(i).getLocation(); 
    			thrTime =uInfo.get(i).getTimeStmp().getTime();
    			defPrior =uInfo.get(i).getDefPrior();
    		}
		}
		
		
		for(int i=0; i<resps.size();i++)
    	{
    		//System.out.println("PRED IF i="+ i +"  nID="+ Integer.parseInt(notID)+" getNID="+resps.get(i).getNID()+" UserID="+resps.get(i).getUserID());
			
    		if(userID.equals(resps.get(i).getUserID()))
    		{
    			index = i;
    			
    			long curentTime = new java.util.Date().getTime();
    			long addTime = defPrior*60*Math.round(resps.get(i).getPriority()*1000); // convert from minutes to seconds
    				
    			Date Dthr = new Date();
    			
    			if (resps.get(i).getPriority()>1)
    			{
    			    Dthr.setTime(thrTime + addTime);
    			    
    			}
    			else
    			{
    				 Dthr.setTime(thrTime);
    			}
    			
    			//System.out.println("Dthr="+Dthr);
    			
    			Date Dnow = new Date(curentTime);
    			
    			if((resps.get(index).getFR1())&&(resps.get(index).getFR2())&&(usrLoc.equals(device))&&(Dnow.after(Dthr))) 
    	    	{
    				//System.out.println("NOTIF should be sent");
    				
    				for (int j=0;j<uInfo.size();j++)
    				{
    					if(userID.equals(uInfo.get(j).getUserID()))
    		    		{
    						//System.out.println("Updating uInfo!!!");
    						
    						UserInfo ureg = new UserInfo();
    						ureg.setUser(uInfo.get(j).getUser());
    						ureg.setUserID(uInfo.get(j).getUserID());
    						ureg.setLocation(uInfo.get(j).getLocation());
    						ureg.setLang(uInfo.get(j).getLang());
    						ureg.setDefPrior(uInfo.get(j).getDefPrior());
    						ureg.setTimeStmp(Dnow);
    						thrTime = Dnow.getTime();
    						uInfo.remove(j);
    		    			uInfo.add(j,ureg);
    						break;
    		    		}
    				}
    				
    				resp = UIsend(resps.get(index));
    				resps.remove(index);
    	    	}
    		}
    	}
		
		return resp;
    }
	
	/**
	 *  list of message (notification) structures 
	 */
	List<UImsg> resps = new ArrayList<UImsg>();
	/**
	 * list of user's and their specifications
	 */
	List<UserInfo> uInfo = new ArrayList<UserInfo>();
	
	
	/**
	 * Registers new users, i.e. updates the uInfo list with the new user 
	 * 
	 * @param user
	 * 			the user's username
	 * @param userID
	 * 			the user's ID
	 * @param location
	 * 			the user's location (i.e. at home, or outside)
	 * @param lang
	 * 			the user's language preferences (needed at the User Interface)
	 */
	@RequestMapping(value = "/UsrReg", method = RequestMethod.POST)   
    public void Ureg(@RequestParam(value="user", required=true, defaultValue="Bob") String user,
    		@RequestParam(value="userID", required=true, defaultValue="JAMES007BOB") String userID,
    		@RequestParam(value="position", required=true, defaultValue="1") String location,
    		@RequestParam(value="lang", required=true, defaultValue="en") String lang) 
    		 
    {
		//System.out.println("User register activity");
		
		UserInfo ureg = new UserInfo();
		ureg.setUser(user);
		ureg.setUserID(userID);
		ureg.setLocation(location);
		ureg.setLang(lang);
		
		// in future versions the duration should be extracted from specified user policies or properties file --> (DONE)
		//ureg.setDefPrior(PriorDur);
		ureg.setDefPrior(Long.parseLong(PriorInterval));
		
		
		for (int i=0;i<uInfo.size();i++)
		{
			if(userID.equals(uInfo.get(i).getUserID()))
    		{
    			//System.out.println("User register update");
    			ureg.setTimeStmp(uInfo.get(i).getTimeStmp());
				uInfo.remove(i);
    			uInfo.add(i,ureg);
				return;
    		}
			else if(i == uInfo.size()-1)
    		{
				ureg.setTimeStmp(new Date(System.currentTimeMillis()-24*60*60*1000));
				//System.out.println("New user registered at = "+ureg.getTimeStmp());
				uInfo.add(ureg);
    			break;
    		}
		}
		
		if(uInfo.size()==0) // empty list
		{
			ureg.setTimeStmp(new Date(System.currentTimeMillis()-24*60*60*1000));
			//System.out.println("New user registered at = "+ureg.getTimeStmp());
			uInfo.add(ureg);
		}
		
    }
	
	/**
	 * Updates the uInfo list with the change of the user's location.
	 * 
	 * @param user
	 * 			the user's username
	 * @param userID
	 * 			the user's ID
	 * @param location
	 * 			the user's location (i.e. at home, or outside)
	 */
	@RequestMapping(value = "/Position", method = RequestMethod.POST)   
    public void UInfo(@RequestParam(value="user", required=true, defaultValue="user") String user,
    		@RequestParam(value="userID", required=true, defaultValue="JB") String userID,
    		@RequestParam(value="position", required=true, defaultValue="1") String location) 
    		 
    {
		UserInfo info = new UserInfo();
		info.setUser(user);
		info.setUserID(userID);
		info.setLocation(location);
		
		for (int i=0;i<uInfo.size();i++)
		{
			if(userID.equals(uInfo.get(i).getUserID()))
    		{
    			info.setLang(uInfo.get(i).getLang());
    			info.setTimeStmp(uInfo.get(i).getTimeStmp());
    			info.setDefPrior(uInfo.get(i).getDefPrior());
				uInfo.set(i, info);
				//System.out.println("User location changed: user="+uInfo.get(i).getUser()+" uID="+uInfo.get(i).getUserID()+" Loc="+uInfo.get(i).getLocation()+" Lang="+uInfo.get(i).getLang());
				return;
    		}
		}
    }

	
	String dateR1 = new String();
    String timeR1 = new String();
    Boolean R1rx = false;
    
    /**
     * Receives messages (information) from Reasoner1, and decides whether to send the notification or wait for additional information from Reasoner2
     * 
     * @param user
     * 			the user's username
     * @param userID
     * 			the user's ID
     * @param date
     * 			date stamp when the notification was generated
     * @param time
     * 			time stamp when the notification was generated
     * @param notID
     * 			id of the given notification
     * @param prior
     * 			priority of the given notification
     */
    @RequestMapping(value = "/IDSSR1", method = RequestMethod.POST)   
    public void IR1(@RequestParam(value="user", required=true, defaultValue="user") String user,
    		@RequestParam(value="userID", required=true, defaultValue="JB") String userID,
    		@RequestParam(value="date", required=true, defaultValue="10-10-10") String date, 
    		@RequestParam(value="time", required=true, defaultValue="12:30.214") String time,
    		@RequestParam(value="nID", required=true, defaultValue="1") String notID,
    		@RequestParam(value="prior", required=true, defaultValue="0.5") String prior) 
    {
       
    	
    	
    	UImsg r1 = new UImsg();
    	
    	r1.setUser(user);
    	r1.setUserID(userID);
    	r1.setDate(date);
    	r1.setTime(time);
    	r1.setNID(notID);
    	r1.setPriority(Double.parseDouble(prior));
    	r1.setFullR1(true);
    	
    	int index = 0;
    	 	
    	//System.out.println("SIZER1 = " + resps.size());
    	
    	for(int i=0; i<resps.size();i++)
    	{
    		//System.out.println("PRED IF i="+ i +"  nID="+ Integer.parseInt(notID)+" getNID="+resps.get(i).getNID()+" UserID="+resps.get(i).getUserID());
    		
    		if((notID == resps.get(i).getNID())&&(userID.equals(resps.get(i).getUserID())))
    		{
    			index = i;
    			
    			//System.out.println("VOOO IF i="+ i +"  nID="+ Integer.parseInt(notID)+" getNID="+resps.get(i).getNID()+" UserID="+resps.get(i).getUserID());
    			
    			if (resps.get(index).getFR2())
    	    	{
    				Ureg(user, userID, profServConn.getUsrLoc(user),"en"); // English lang used as default for now
    				
    	    		r1.setType(resps.get(i).getType());
    	    		r1.setTitle(resps.get(i).getTitle());
    	    		r1.setContent(resps.get(i).getContent());
    	    		r1.setFullR2(true);
    	    		
    	    		resps.remove(index);
    				resps.add(index,r1);
    	    		
    	    		UIsendM(r1);
    				//System.out.println(rsp);
    	    		// call fun for http post with arg r1
    				//System.out.println("SIZEbef = " + resps.size());
    	    		//resps.remove(index);
    	    		//System.out.println("SIZEaf = " + resps.size());
    	    		return;
    	    	}
    			else 
    			{
    				resps.remove(index);
    				resps.add(index,r1);
    				return;
    			}
    		}
    		else if(i == resps.size()-1)
    		{
    			resps.add(r1);
    			break;
    		}
    		
    	
    	}
    	
    	if(resps.size()==0) resps.add(r1);
    	
    }
    
    String typeR2 = new String();
    String titleR2 = new String();
    String contentR2 = new String();
 
    /**
     * Receives messages (information) from Reasoner2, and decides whether to send the notification or wait for additional information from Reasoner1
     * 
     * @param user
     * 			the user's username
     * @param userID
     * 			the user's ID
     * @param type
     * 			the graphical representation type of the notification (at the User Interface)
     * @param title
     * 			the title of the notification
     * @param content
     * 			the content of the notification
     * @param notID
     * 			id of the given notification
     */
    @RequestMapping(value = "/IDSSR2", method = RequestMethod.POST)
    public void IR2(@RequestParam(value="user", required=true, defaultValue="user") String user,
    		@RequestParam(value="userID", required=true, defaultValue="JB") String userID,
    		@RequestParam(value="type", required=true, defaultValue="two-buttons") String type, 
    		@RequestParam(value="title", required=true, defaultValue="Important notification") String title,
    		@RequestParam(value="content", required=true, defaultValue="Bla bla" ) String content,
    		@RequestParam(value="nID", required=true, defaultValue="1" ) String notID) 
    {
    	/*typeR2 = type;
    	titleR2 = title;
    	contentR2 = content;
    	return  new UImsg(dateR1,timeR1,typeR2,titleR2,contentR2,"1","0.6");*/
    	
    	
        UImsg r2 = new UImsg();
    	
    	r2.setUser(user);
    	r2.setUserID(userID);
        r2.setType(type);
    	r2.setTitle(title);
    	r2.setContent(content);
    	r2.setNID(notID);
    	r2.setFullR2(true);
    	
        int index = 0;
        
        //System.out.println("SIZER2= " + resps.size());
    	
    	for(int i=0; i<resps.size();i++)
    	{
    		//System.out.println("PRED IF i="+ i +"  nID="+ Integer.parseInt(notID)+" getNID="+resps.get(i).getNID()+" UserID="+resps.get(i).getUserID());
    		
    		if((notID == resps.get(i).getNID())&&(userID.equals(resps.get(i).getUserID())))
    		{
    			index = i;
    			
    			//System.out.println("VOOO IF i="+ i +"  nID="+ Integer.parseInt(notID)+" getNID="+resps.get(i).getNID()+" UserID="+resps.get(i).getUserID());
    			if (resps.get(index).getFR1())
    	    	{
    				Ureg(user, userID, profServConn.getUsrLoc(user),"en"); // English lang used as default for now
    				
    				r2.setDate(resps.get(i).getDate());
    				r2.setTime(resps.get(i).getTime());
    				r2.setPriority(resps.get(i).getPriority());
    				r2.setFullR1(true);
    				
    				resps.remove(index);
    				resps.add(index,r2);
    				
    				// call fun for http post with argument r2
    				UIsendM(r2);
    				
    				//System.out.println(rsp);
    	    		
    				//System.out.println("SIZEbef = " + resps.size());
    	    		//resps.remove(index);
    	    		//System.out.println("SIZEaf = " + resps.size());
    	    		return;
    	    		//break;
    	    	}
    			else 
    			{
    				resps.remove(index);
    				resps.add(index,r2);
    				return;
    				//break;
    			}
    		}
    		else if(i == resps.size()-1)
    		{
    			resps.add(r2);
    			break;
    		}
    		
    	}
    	
    	 if(resps.size()==0) resps.add(r2);
    	
    	
    }   
    
    /**
     * Receives messages (information) from the notif. generators and manages this information
     * 
     * @param notifContent
     *          the notification message as a JSON object
     *         
     * @return Returns the same JSON object with updated filed for the notification ID
     *          
     */
    @RequestMapping(value = "/notification", method = RequestMethod.POST,  headers = "Accept=application/json")
    @ResponseBody
    public NotificationMessage recvNotificaiton(@RequestBody NotificationMessage notifContent) throws IOException, Exception
    {
    	boolean processNotification = false;
    	String decision = reasoningEnabled;
    		
       if("on".equals(decision))
       {
    	   if((profServConn.isNotifSelected(notifContent.getUser(), NotifEnumJSON(notifContent.getType(),notifContent.getContent()))) || (notifContent.getPriority()<=Double.parseDouble(PriorThr)))
    	   {
    		   processNotification = true;
    	   }
       }   
       else if("off".equals(decision))
       {
    	   processNotification = true;
       }
    	
    	
       if(processNotification)
      {
       
        UImsg not = new UImsg();
        
        ObjectMapper mapper = new ObjectMapper();
        String cont = mapper.writeValueAsString(notifContent.getContent());
    	
    	
    	not.setUser(notifContent.getUser());
    	not.setUserID(notifContent.getUser());
    	not.setType(notifContent.getType());
    	not.setTitle(notifContent.getTitle());
    	not.setContent(cont);
    	
    	not.setFullR2(true);
    	not.setFullR1(true);
    	not.setDate(notifContent.getDate());
    	not.setTime(notifContent.getTime());
    	not.setPriority(notifContent.getPriority());
    	
    	NotificationDBcontent NDBC = new NotificationDBcontent();
        
        NDBC.setType(notifContent.getContent().getType());
        NDBC.setSubtype(notifContent.getContent().getSubtype());
        NDBC.setTitle(notifContent.getContent().getTitle());
        NDBC.setMotivational(notifContent.getContent().getMotivational());
        NDBC.setSuggestion(notifContent.getContent().getSuggestion());
        List<NotificationContentFeedbackMsg> notifFeedback = notifContent.getContent().getFeedback();
        List<NotificationStatements> notifStat = notifContent.getContent().getStatements();
        
        for(NotificationContentFeedbackMsg cntFeedback : notifFeedback)
        {
       	 NDBC.addFeedback(cntFeedback);  
        }
        
        for(NotificationStatements cntState : notifStat)
        {
       	 NDBC.addStatements(cntState);
        }
        
        //NDBC.setFeedback(Jobj.getFeedback());
        NDBC.setPrimaryUser(notifContent.getUser());
        NDBC.setResponse("None");
        NDBC.setDateTime(new Date());
        NDBC.setSource(notifContent.getSource());
        
        nIDcnt = NotificationMongo.addNotification(NDBC); // this function should fill the nID
        
        //System.out.println("MongoDB ID <<ifaceController>> "+ nIDcnt);
        
        not.setNID(nIDcnt);
    	
    	resps.add(not);
    	
    	Ureg(notifContent.getUser(), notifContent.getUser(), profServConn.getUsrLoc(notifContent.getUser()),"en");
    	
        UIsendM(not);
        
        notifContent.setNotificationID(nIDcnt);
        
      }
    	
    	return notifContent;
   
    }
    
    /**
     * Receives messages (information) from the notif. generators and manages this information
     * 
     * @param user
     * 			the user's username
     * @param date
     * 			the trigger date of the notification
     * @param time
     * 			the trigger time of the notification
     * @param type
     * 			the notification type
     * @param title
     * 			the title of the notification
     * @param content
     * 			the content of the notification
     * @param prior
     * 			the notification priority coefficient
     * @param source
     *          the url address of the notification generator
     */
    
    @RequestMapping(value = "/Notif", method = RequestMethod.POST)
    public String GetNotif(@RequestParam(value="user", required=true, defaultValue="user") String user,
    		@RequestParam(value="date", required=true, defaultValue="10-10-10") String date, 
    		@RequestParam(value="time", required=true, defaultValue="12:30.214") String time,
    		@RequestParam(value="type", required=true, defaultValue="Default") String type, 
    		@RequestParam(value="title", required=true, defaultValue="Default") String title,
    		@RequestParam(value="content", required=true, defaultValue="Default" ) String content,
    		@RequestParam(value="prior", required=true, defaultValue="0.5") String prior,
    		@RequestParam(value="source", required=true, defaultValue="Default") String source)
    {
        
    	UImsg not = new UImsg();
    	
    	String newCont = content;
    	
   	 try
        {
   		 newCont = new String(content.getBytes( "ISO-8859-1" ), "UTF-8" );
        }
        catch ( UnsupportedEncodingException e )
        {
        	LOG.warn(e.getMessage()); 
            
        }
    	
    	not.setUser(user);
    	not.setUserID(user);
    	not.setType(type);
    	not.setTitle(title);
    	not.setContent(newCont);
    	
    	not.setFullR2(true);
    	not.setFullR1(true);
    	not.setDate(date);
    	not.setTime(time);
    	not.setPriority(Double.parseDouble(prior));
    	
    	
    	//nIDcnt++; // --> for now the nID is generated with a counter in order to provide backward compatibility. Future releases will incorporate different nID generators.
    	
    	
    	try // all notification are being stored in DB regardless of the user preferences 
		{
		if(IsJson(newCont))
	       {
			
			     NotificationContentMsg Jobj = new ObjectMapper().readValue(newCont, NotificationContentMsg.class);
	             
	             NotificationDBcontent NDBC = new NotificationDBcontent();
	             
	             NDBC.setType(Jobj.getType());
	             NDBC.setSubtype(Jobj.getSubtype());
	             NDBC.setTitle(Jobj.getTitle());
	             NDBC.setMotivational(Jobj.getMotivational());
	             NDBC.setSuggestion(Jobj.getSuggestion());
	             List<NotificationContentFeedbackMsg> notifFeedback = Jobj.getFeedback();
	             List<NotificationStatements> notifStat = Jobj.getStatements();
	             
	             for(NotificationContentFeedbackMsg cntFeedback : notifFeedback)
	             {
	            	 NDBC.addFeedback(cntFeedback);  
	             }
	             
	             for(NotificationStatements cntState : notifStat)
	             {
	            	 NDBC.addStatements(cntState);
	             }
	             
	             //NDBC.setFeedback(Jobj.getFeedback());
	             NDBC.setPrimaryUser(user);
	             NDBC.setResponse("None");
	             NDBC.setDateTime( new Date());
	             NDBC.setSource(source);
	             
	             nIDcnt = NotificationMongo.addNotification(NDBC); // this function should fill the nID
	             
	             //System.out.println("MongoDB ID <<ifaceController>> "+ nIDcnt);
	                        
	             
	       }
		}
		catch (JsonParseException e) {
			LOG.warn(e.getMessage());
		} catch (JsonMappingException e) {
			LOG.warn(e.getMessage());
		}catch(IOException e){
			LOG.warn("Notif error while storing data: " + e.getMessage() + " " + e.toString());
		}
    	
    	not.setNID(nIDcnt);
    	//System.out.println("Notif IN >>> nIDcnt" + nIDcnt);
    	
    	resps.add(not);
    	
    	
    	Ureg(user, user, profServConn.getUsrLoc(user),"en"); // English lang used as default for now
    	
    	// add the notification to pending if the user is subscribed to it or if it is an alarm
    	/*if ((profServConn.isNotifSelected(not.getUser(), NotifEnum(not.getType(),not.getContent()))) || (not.getPriority()<=Double.parseDouble(PriorThr)))
    	{
    		System.out.println("Storing the notification with ID" + nIDcnt);
    		try
    		{
    		if(IsJson(content))
		       {
 	
 	             // the storing process will go here, when we decide policies for storing nots. Now all nots are being stored.
		       }
    		}
			catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			}catch(IOException e){
				System.out.println("Notif error while storing data: " + e.getMessage() + " " + e.toString());
			}
    		
    		resps.add(not);
    	}*/
	    	
    	
    	UIsendM(not);
    	
    	return nIDcnt;
    
    }
    
    /**
     * Checks if the content of the notification is a JSON object 
     * 
     * @param json
     * 			the notification content
     * 
     * @return Returns true if the content is a JSON object
     */
    
    
    public boolean IsJson(String json)
    {
    	 boolean valid = false;
    	 
    	 try {
    	 ObjectMapper mapper = new ObjectMapper();
    	 JsonNode jsonNode = mapper.readTree(json); 
    	 valid = true;
    	 }
    	 catch(JsonProcessingException e)
    	 {
    		 LOG.warn(e.getMessage());
    	 }
    	 catch(IOException e)
    	 {
    		 LOG.warn("IsJson error while fetching data: " + e.getMessage() + " " + e.toString());
    	 }
    	 
	    
    	 return valid;
    }
    
   
    /**
     * Returns the correct enumeration for the notification type 
     * 
     * @param type
     * 			the notification type
     * 
     * @param content
     * 			the notification content
     * 
     * @return Returns the enumeration
     */ 
    public MessageType NotifEnum(String type, String content)
    {
    	String activity = "default";
    	MessageType NotType = MessageType.NORMAL; // This is the default Notification Type
    	
    	if(IsJson(content))
	       {
	
    		try 
    		{
	         NotificationContent Jobj = new ObjectMapper().readValue(content, NotificationContent.class);
	         
	         if (Jobj.getSubtype()!= null)
	         {
	        	 activity = Jobj.getSubtype(); 
	         }
	         
	         
	         switch(type.toLowerCase())
	     	{
	     	
	     	case "reminder" :
	             
	     		switch(activity.toLowerCase())
	         	{
	     		 case "eating": NotType = MessageType.REMINDER_EAT; 
	     			 break;
	     			 
	     		 case "drinking": NotType = MessageType.REMINDER_DRINK; 
	 			     break;
	     		
	     		 case "sleeping": NotType = MessageType.REMINDER_SLEEP; 
	 			     break;
	 			     
	     		 case "showering": NotType = MessageType.REMINDER_SHOWER; 
	 			     break;
	 			     
	     		 case "physicalexercise": NotType = MessageType.REMINDER_PHYSICALEXERCISE; 
	 			     break;
	 			     
	     		 case "cognitiveexercise": NotType = MessageType.REMINDER_COGNITIVEEXERCISE; 
	 		         break;
	 		         
	     		 case "inactivity": NotType = MessageType.REMINDER_INACTIVITY; 
	 		         break;	
	 		         
	     		 case "medicineintake": NotType = MessageType.REMINDER_MEDICINEINTAKE; 
	 		         break;
	 		         
	     		 case "vitalsigns": NotType = MessageType.REMINDER_VITALSIGNS; 
	 		         break;
	 		         
	     		 case "homeenvironment": NotType = MessageType.REMINDER_HOME; 
	 		         break;
	         	}
	     		
	             break;
	     	
	         case "recommendation":
	            
	         	switch(activity.toLowerCase())
	         	{
	         	case "eating": NotType = MessageType.RECOMMENDATION_EAT; 
	 			 break;
	 			 
	 		 case "drinking": NotType = MessageType.RECOMMENDATION_DRINK; 
	 		     break;
	 		
	 		 case "sleeping": NotType = MessageType.RECOMMENDATION_SLEEP; 
	 		     break;
	 		     
	 		 case "showering": NotType = MessageType.RECOMMENDATION_SHOWER; 
	 		     break;
	 		     
	 		 case "physicalexercise": NotType = MessageType.RECOMMENDATION_PHYSICALEXERCISE; 
	 		     break;
	 		     
	 		 case "cognitiveexercise": NotType = MessageType.RECOMMENDATION_COGNITIVEEXERCISE; 
	 	         break;
	 	         
	 		 case "socializing": NotType = MessageType.RECOMMENDATION_SOCIAL;  
	 	         break;	
	 	         
	 		 case "progressreport": NotType = MessageType.RECOMMENDATION_PROGRESSREPORT; 
	 	         break;
	 	         
	 		 case "vitalsigns": NotType = MessageType.RECOMMENDATION_VITALSIGNS; 
	 	         break;
	 	         
	 		 case "homeenvironment": NotType = MessageType.RECOMMENDATION_HOME; 
	 	         break;
	         	}
	         	
	            break; 
	             
	         case "appointment" :
	         	switch(activity.toLowerCase())
	         	{
	         	 case "medical": NotType = MessageType.APPOINTMENT_MEDICAL;
	         	 	break;
	         	 	
	         	 case "social": NotType = MessageType.APPOINTMENT_SOCIAL;
	      	 		break;
	         	}
	         	
	             break; 
	            
	         }
	         
    		}
    		catch (JsonParseException e) {
    			LOG.warn(e.getMessage());
    		} catch (JsonMappingException e) {
    			LOG.warn(e.getMessage());
    		}catch(IOException e){
    			LOG.warn("ProfSerConn error while fetching data: " + e.getMessage() + " " + e.toString());
    		}
	       
	       }

    	//System.out.println("The NOT TYPE IS: "+ NotType);
    	return NotType;
    }

    /**
     * Returns the correct enumeration for the JSON formated notification type  
     * 
     * @param type
     * 			the notification type
     * 
     * @param content
     * 			the notification content in JSON format
     * 
     * @return Returns the enumeration
     */ 
    public MessageType NotifEnumJSON(String type, NotificationContentMsg content)
    {
    	String activity = "default";
    	MessageType NotType = MessageType.NORMAL; // This is the default Notification Type
    	
    	   
    	  if (content.getSubtype()!= null)
	         {
	        	 activity = content.getSubtype(); 
	         }
	         
	         
	         switch(type.toLowerCase())
	     	{
	     	
	     	case "reminder" :
	             
	     		switch(activity.toLowerCase())
	         	{
	     		 case "eating": NotType = MessageType.REMINDER_EAT; 
	     			 break;
	     			 
	     		 case "drinking": NotType = MessageType.REMINDER_DRINK; 
	 			     break;
	     		
	     		 case "sleeping": NotType = MessageType.REMINDER_SLEEP; 
	 			     break;
	 			     
	     		 case "showering": NotType = MessageType.REMINDER_SHOWER; 
	 			     break;
	 			     
	     		 case "physicalexercise": NotType = MessageType.REMINDER_PHYSICALEXERCISE; 
	 			     break;
	 			     
	     		 case "cognitiveexercise": NotType = MessageType.REMINDER_COGNITIVEEXERCISE; 
	 		         break;
	 		         
	     		 case "inactivity": NotType = MessageType.REMINDER_INACTIVITY; 
	 		         break;	
	 		         
	     		 case "medicineintake": NotType = MessageType.REMINDER_MEDICINEINTAKE; 
	 		         break;
	 		         
	     		 case "vitalsigns": NotType = MessageType.REMINDER_VITALSIGNS; 
	 		         break;
	 		         
	     		 case "homeenvironment": NotType = MessageType.REMINDER_HOME; 
	 		         break;
	         	}
	     		
	             break;
	     	
	         case "recommendation":
	            
	         	switch(activity.toLowerCase())
	         	{
	         	case "eating": NotType = MessageType.RECOMMENDATION_EAT; 
	 			 break;
	 			 
	 		 case "drinking": NotType = MessageType.RECOMMENDATION_DRINK; 
	 		     break;
	 		
	 		 case "sleeping": NotType = MessageType.RECOMMENDATION_SLEEP; 
	 		     break;
	 		     
	 		 case "showering": NotType = MessageType.RECOMMENDATION_SHOWER; 
	 		     break;
	 		     
	 		 case "physicalexercise": NotType = MessageType.RECOMMENDATION_PHYSICALEXERCISE; 
	 		     break;
	 		     
	 		 case "cognitiveexercise": NotType = MessageType.RECOMMENDATION_COGNITIVEEXERCISE; 
	 	         break;
	 	         
	 		 case "socializing": NotType = MessageType.RECOMMENDATION_SOCIAL;  
	 	         break;	
	 	         
	 		 case "progressreport": NotType = MessageType.RECOMMENDATION_PROGRESSREPORT; 
	 	         break;
	 	         
	 		 case "vitalsigns": NotType = MessageType.RECOMMENDATION_VITALSIGNS; 
	 	         break;
	 	         
	 		 case "homeenvironment": NotType = MessageType.RECOMMENDATION_HOME; 
	 	         break;
	         	}
	         	
	            break; 
	             
	         case "appointment" :
	         	switch(activity.toLowerCase())
	         	{
	         	 case "medical": NotType = MessageType.APPOINTMENT_MEDICAL;
	         	 	break;
	         	 	
	         	 case "social": NotType = MessageType.APPOINTMENT_SOCIAL;
	      	 		break;
	         	}
	         	
	             break; 
	            
	         }
	         
    		

    	//System.out.println("The NOT TYPE IS: "+ NotType);
    	return NotType;
    }
    
    /**
     * Formats the email body of the notification. Supports both string and JSON content objects  
     * 
     * @param usrnm
     * 			the notification content
     * 
     * @param content
     * 			the notification content
     * 
     * @return Returns the email body of the notification.
     */
    public String JsonParse(String usrnm, String content)  throws JsonParseException, 
             						  JsonMappingException, 
IOException {
    	
    	       String body = "";
    	       
    	       String nmsrnm = profServConn.getUsrTrueName(usrnm);
    	       
		       if(IsJson(content))
		       {
    	
    	        NotificationContent Jobj = new ObjectMapper().readValue(content, NotificationContent.class);
    	        
    	        String motiv = Jobj.getMotivational();
    	        String sugest = Jobj.getSuggestion();
    	        
    	        if (motiv == null)
    	        {
    	        	List<Statements> stat = Jobj.getStatements();
    	        	motiv = stat.get(0).getText();
    	        	sugest = stat.get(0).getType();
    	        }
    	        
    	        body = "\n" +
		    	        Jobj.getTitle() + " for \n" +
		    	        nmsrnm + "\n" +
		    	        "Notification content: " + motiv +"\n" +
		    	        "Triggered suggestion: " +sugest;
    	        
    	        
    	        //System.out.println(body);
    	        
		       }
		       else 
		       {
		    	   body = "\n" +
		    			  "NOTIFICATION for \n" +
		    	          nmsrnm + "\n" +
		       	          "Notification content: " + content;
		    	   
		    	   //System.out.println(body);
		       }
		       
		       return body;
    
            }
    
    
}