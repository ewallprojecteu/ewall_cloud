/****************************************************************
 * Copyright 2014 Ss Cyril and Methodius University in Skopje
***************************************************************/

package eu.ewall.platform.notificationmanager.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;



import java.util.Date;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import eu.ewall.platform.notificationmanager.dao.factory.MongoDBFactory;

/**
 * The Class NotificationMongo.
 * 
 */
@Component
@RestController
public class NotificationMongo {
	
	@Value("${mongo.dbname}")
	private String mongoDBName;
	
	@Value("${srv.enc}")
	private String serverEncoding;

	/** The log. */
	private static Logger LOG = LoggerFactory.getLogger(NotificationMongo.class);
	/** The mongo ops. */
	public MongoOperations mongoOps;

	/** The Constant NOTIFICATIONS_COLLECTION. */
	public static final String NOTIFICATIONS_COLLECTION = "notificationsCollection";
	
	@Autowired
	private MongoDBFactory MongoDBFactory;
	

	/**
	 * Gets the all notifications for a given caregiver. Optionally can filter by timestamps.
	 *
	 * @return the all notifications for the caregiver 
	 */
	@RequestMapping(value = "/{caregiverUsername}/notifications", method = RequestMethod.GET)
	public List<CaregiverNotificationDBcontentResponse> getAllNotsFromCG(@PathVariable("caregiverUsername") String caregiverUsername,
			@RequestParam(value="From", required=true, defaultValue="") String From, 
			@RequestParam(value="To", required=true, defaultValue="") String To) {
		//LOG.debug("Retrieveing all users from mongodb.");
		//MongoDBFactory MDBF = new MongoDBFactory();
		
		//System.out.println("MongoDB From  "+ From);
		//System.out.println("MongoDB To  "+ To);
		
		List <CaregiverNotificationDBcontent> theNot = new ArrayList<CaregiverNotificationDBcontent>();
		List <CaregiverNotificationDBcontentResponse> notifResponse = new ArrayList<CaregiverNotificationDBcontentResponse>();
		
		if("".equals(From) || "".equals(To)) //get all data for the given caregiver 
		{
			//System.out.println("MongoDB Query by all");
			mongoOps = MongoDBFactory.getMongoOperations();
			Query query = new Query(Criteria.where("caregiver").is(caregiverUsername));
			theNot = mongoOps.find(query, CaregiverNotificationDBcontent.class, NOTIFICATIONS_COLLECTION);
		}
		else // get the data based on time information
		{
			Date frmD = new Date();
			Date toD = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			
			try
			{
			 
			  if (From.indexOf(" ")>=0) // plus sign not decoded correctly
				{
					String[] parts = From.split(" ");
					frmD =  df.parse(parts[0]+"+"+parts[1]);	
				}
				else
				{
					frmD =  df.parse(From);	
				}
			  
			}
			catch(ParseException e)
			{
				LOG.warn("NotificationMongo{getAllNotsFromCG} error while sending data: " + e.getMessage() + " " + e.toString());
			   
				if(From.length()==23) // dateTime is sent without offset
				{
					try
					{
						frmD =  df.parse(From+"+00:00");
					}
					catch(ParseException ee)
					{
						LOG.warn("NotificationMongo{getAllNotsFromCG2} error while sending data: " + ee.getMessage() + " " + ee.toString());
					}
				}
				
			}
			
			
			
			try
			{
			  if (To.indexOf(" ")>=0) // plus sign not decoded correctly
				{
				    String[] parts = To.split(" ");
					toD =  df.parse(parts[0]+"+"+parts[1]);	
				}
				else
				{
					toD =  df.parse(To);	
				}
			  
			}
			catch(ParseException e)
			{
				LOG.warn("NotificationMongo{getAllNotsFromCG} error while sending data: " + e.getMessage() + " " + e.toString());
			   
				if(From.length()==23) // dateTime is sent without offset
				{
					try
					{
						toD =  df.parse(To+"+00:00");
					}
					catch(ParseException ee)
					{
						LOG.warn("NotificationMongo{getAllNotsFromCG2} error while sending data: " + ee.getMessage() + " " + ee.toString());
					}
				}
			}
			
			//System.out.println("MongoDB Query by date");
			 mongoOps = MongoDBFactory.getMongoOperations();
			 Query query = new Query(Criteria.where("caregiver").is(caregiverUsername).andOperator(Criteria.where("dateTime").gt(frmD),Criteria.where("dateTime").lt(toD)));
			 theNot = mongoOps.find(query, CaregiverNotificationDBcontent.class, NOTIFICATIONS_COLLECTION);		
		}
		
		for (CaregiverNotificationDBcontent cnt : theNot)
		{
			    CaregiverNotificationDBcontentResponse hlp = new CaregiverNotificationDBcontentResponse();
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
				//tempD = df.parse(cnt.getDateTime()+"-00:00");
				//cnt.setDateTime(tempD);
				hlp.set_Id(cnt.get_Id());
				hlp.setCaregiver(cnt.getCaregiver());
				hlp.setContent(cnt.getContent());
				hlp.setDateTime(df.format(cnt.getDateTime()));
				hlp.setPrimaryUser(cnt.getPrimaryUser());
				hlp.setPriority(cnt.getPriority());
				hlp.setSource(cnt.getSource());
				hlp.setTitle(cnt.getTitle());
				hlp.setTriggerType(cnt.getType());
				
			   //System.out.println("Date in query FORMAT " + df.format(cnt.getDateTime()));
					
			 //  System.out.println("Date in query " + cnt.getDateTime().toString());
			   
			   notifResponse.add(hlp);
		}
		
		return notifResponse;
	}
	
	/**
	 * Inserts a caregiver related notification in the DB. 
	 *
	 * @return the notification ID 
	 */
	@RequestMapping(value = "/{caregiverUsername}/notifications", method = RequestMethod.POST)
	public String insertCaregiverNotificationDB(@PathVariable("caregiverUsername") String caregiverUsername,
			@RequestParam(value="primaryUser", required=true, defaultValue="") String user,
			@RequestParam(value="dateTime", required=true, defaultValue="") String date,
			@RequestParam(value="type", required=true, defaultValue="Default") String type, 
    		@RequestParam(value="title", required=true, defaultValue="Default") String title,
    		@RequestParam(value="content", required=true, defaultValue="Default" ) String content,
    		@RequestParam(value="priority", required=true, defaultValue="0.5") String prior,
    		@RequestParam(value="source", required=true, defaultValue="Default") String source)
		{
		   
		    CaregiverNotificationDBcontent Notif = new CaregiverNotificationDBcontent();
		    
		    Notif.setCaregiver(caregiverUsername);
		    Notif.setPrimaryUser(user);
		    
		    Date dtm = new Date();
		    
			//Date toD = new Date();
		    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			try
			{
				//System.out.println("NotificationMongo rcv DATESTRING : " + date);
				
				
				if (date.indexOf(" ")>=0) // plus sign not decoded correctly
				{
					String[] parts = date.split(" ");
					dtm =  df.parse(parts[0]+"+"+parts[1]);	
				}
				else
				{
					dtm =  df.parse(date);	
				}
			    
			
			}
			catch(ParseException e)
			{
				LOG.warn("NotificationMongo{insertCaregiverNotificationDB} error while sending data: " + e.getMessage() + " " + e.toString());
				
				if(date.length()==23) // dateTime is sent without offset
				{
					try
					{
					 dtm =  df.parse(date+"+00:00");
					}
					catch(ParseException ee)
					{
						LOG.warn("NotificationMongo{insertCaregiverNotificationDB2} error while sending data: " + ee.getMessage() + " " + ee.toString());
					}
				}
			}
			
			String newCont = content;
	    	
			if("no".equals(serverEncoding))
		   	  try
		        {
		   		 newCont = new String(content.getBytes( "ISO-8859-1" ), "UTF-8" );
		        }
		        catch ( UnsupportedEncodingException e )
		        {
		           LOG.warn(e.getMessage());
		            
		        }
		    
		    Notif.setDateTime(dtm);
		    Notif.setType(type);
		    Notif.setTitle(title);
		    Notif.setContent(newCont);
		    Notif.setPriority(prior);
		    Notif.setSource(source);
		    
		    //NotificationContent Jobj = new ObjectMapper().readValue(content, NotificationContent.class);
		    
		    
		    mongoOps = MongoDBFactory.getMongoOperations();
			mongoOps.insert(Notif, NOTIFICATIONS_COLLECTION);
			//LOG.debug("New user with username " + user.getUsername()
			//		+ " added to mongodb.");
			
			Query query = new Query(Criteria.where("primaryUser").is(Notif.getPrimaryUser()));
			query.with(new Sort(Sort.Direction.DESC, "_id"));
			String dbID = mongoOps.findOne(query, CaregiverNotificationDBcontent.class, NOTIFICATIONS_COLLECTION).get_Id();
			
			//System.out.println("MongoDB ID <<addNotification>> "+ dbID);
			
			return dbID;
		}
	

	/**
	 * Adds a new notification in the DB
	 *
	 * @param Notif
	 *            the Notification
	 * @return true, if successful
	 */
	public String addNotification(NotificationDBcontent Notif) 
	{

		//System.out.println("MongoDB name <<addNotification>> "+ mongoDBName);
		
		    //MongoDBFactory MDBF = new MongoDBFactory();
		    mongoOps = MongoDBFactory.getMongoOperations();
			mongoOps.insert(Notif, NOTIFICATIONS_COLLECTION);
			//LOG.debug("New user with username " + user.getUsername()
			//		+ " added to mongodb.");
			
			Query query = new Query(Criteria.where("primaryUser").is(Notif.getPrimaryUser()));
			query.with(new Sort(Sort.Direction.DESC, "_id"));
			String dbID = mongoOps.findOne(query, NotificationDBcontent.class, NOTIFICATIONS_COLLECTION).get_Id();
			
			//System.out.println("MongoDB ID <<addNotification>> "+ dbID);
			
			return dbID;
	}
	
	/**
	 * Updates a notification with the response from the mainscreen/user
	 *
	 * @param id
	 *            the Notification id in the DB
	 * @param response
	 *            the Notification response
	 * @return True for succesfull update
	 */
	public boolean updateNotification(String id, String response) 
	{
		mongoOps = MongoDBFactory.getMongoOperations();
		Query query = new Query(Criteria.where("_id").is(id));
		NotificationDBcontent Notif = mongoOps.findOne(query, NotificationDBcontent.class, NOTIFICATIONS_COLLECTION);
		Notif.setResponse(response);
		mongoOps.save(Notif, NOTIFICATIONS_COLLECTION);
		
		return true;
	}
	
	/**
	 * Gets the url address of the respective notification generator
	 *
	 * @param id
	 *            the Notification id in the DB
	 * 
	 * @return The url address of the notification generator
	 */
	public String getSource(String id)
	{
		mongoOps = MongoDBFactory.getMongoOperations();
		Query query = new Query(Criteria.where("_id").is(id));
		NotificationDBcontent Notif = mongoOps.findOne(query, NotificationDBcontent.class, NOTIFICATIONS_COLLECTION);
		return Notif.getSource();
	}
	


}
