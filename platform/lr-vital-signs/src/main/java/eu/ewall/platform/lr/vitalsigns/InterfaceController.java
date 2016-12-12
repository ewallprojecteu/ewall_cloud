/****************************************************************
 * Copyright 2015 Ss Cyril and Methodius University in Skopje
***************************************************************/

package eu.ewall.platform.lr.vitalsigns;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.io.IOException;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.*;
import org.springframework.scheduling.concurrent.*;

import eu.ewall.platform.commons.datamodel.message.NotificationMessage;
import eu.ewall.platform.commons.datamodel.message.NotificationContentMsg;
import eu.ewall.platform.commons.datamodel.message.NotificationContentFeedbackMsg;
import eu.ewall.platform.commons.datamodel.message.NotificationStatements;

import javax.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.ewall.platform.lr.vitalsigns.dao.VitalSignsDBcontent;
import eu.ewall.platform.lr.vitalsigns.dao.VitalSignsDBcontentFloating;
import eu.ewall.platform.lr.vitalsigns.dao.VitalSignsMongo;


/**
 * The Class InterfaceController
 */

@RestController
public class InterfaceController {
	
	@Value("${notificationManager.url}")
	private String notificationManagerUrl;
	
	@Value("${service.baseUrl}")
	private String baseUrl; 
	
	@Autowired
	private ProfServInterface ProfServInterface;
	
	@Autowired
	private VitalSignsMongo VitalSignsMongo;
	
	@Autowired
	private VitalSignsDecisionMaker VitalSignsDecisionMaker;
	
	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;
	
	
	long period = 2*60*60*1000;
	//long period = 10*1000;
	
	private TaskScheduler scheduler = new ConcurrentTaskScheduler();
	
	Date moment = new Date();
	Date start = new Date(moment.getTime() + 50000);

    @PostConstruct
    private void executeJob() {
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
            	//System.out.println("VLSR: Code has been runed");
            	VitalSignsDecisionMaker.gatherSamples();
            }
        }, start, period);
    }
	
	
	@RequestMapping(value = "/test/{username}", method = RequestMethod.GET)
	public void getPrefTest(@PathVariable("username") String username)
			{
		        
				NotificationContentMsg content = new NotificationContentMsg();
				content.setType("recommendation");
				content.setSubtype("vitalsigns");			
				
				content.setTitle("NOTIFIKATION");
				
				content.addFeedback(new NotificationContentFeedbackMsg("button",
						"OK", "","",""));
				
				content.setMotivational("Dein Puls ist leicht angestiegen");
		        content.setSuggestion("eWALL wird deinen Puls weiterhin überwachen. Wenn notwendig, wird deine Betreuungsperson informiert.");
			
			
		        try
				{
				  sendNotificationUser(username, content);
				}
				catch (RestClientException e) {
					e.printStackTrace();
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}catch(Exception e){
					System.out.println("User Alarm error while sending data: " + e.getMessage() + " " + e.toString());
				}
			}
	
	
	/**
	 * Returns the last sample for a given user
	 * 
	 * @param username
	 * 			the user's username
	 * @return 
	 *     The last vital signs sample 
	 */
	@RequestMapping(value = "/samples/{username}", method = RequestMethod.GET)
	public int getSamples(@PathVariable("username") String username)
			{
			   Date thr = new Date();
		       return VitalSignsMongo.getLastSample(username, "day",thr);
			}
	
	/**
	 * Returns the preferred language for a given user
	 * 
	 * @param username
	 * 			the user's username
	 * @return 
	 *     The preferred language 
	 */
	@RequestMapping(value = "/language/{username}", method = RequestMethod.GET)
	public String getLang(@PathVariable("username") String username)
			{
			   return  ProfServInterface.getUsrLang(username);
			}
	
	/**
	 * Returns the list of caregivers for a given user
	 * 
	 * @param username
	 * 			the user's username
	 * @return 
	 *     The list of caregivers usernames 
	 */
	@RequestMapping(value = "/caregiver/{username}", method = RequestMethod.GET)
	public List<String> getCrgc(@PathVariable("username") String username)
			{
			   return  ProfServInterface.getCaregivers(username);
			}
	
	/**
	 * Returns the all samples for a specific interval  for a given user
	 * 
	 * @param username
	 * 			the user's username
	 * @param intrv
	 * 			the interval
	 * @return 
	 *     The list of samples 
	 */
	@RequestMapping(value = "/VSsamples/{username}", method = RequestMethod.GET)
	public List<VitalSignsDBcontent> getVSSamples(@PathVariable("username") String username,
										@RequestParam(value="interval", required=true, defaultValue="hour") String intrv)
			{
			   return VitalSignsMongo.getMeas(username, intrv);
			}
	
	/**
	 * Sends a primary user related notification towards the Notification Manager
	 *
	 * @param usrnm
	 *            the primary user username
	 *      
	 * @param content
	 *            the notification content
	 *      
	 */
	public void sendNotificationUser(String usrnm, NotificationContentMsg content) throws IOException, Exception
	{
		String url = notificationManagerUrl + "/notification";
		Date now = new Date();
		
		NotificationMessage msg = new NotificationMessage();
		
		msg.setContent(content);
		msg.setDate(new SimpleDateFormat("dd-MM-yyyy").format(now));
		msg.setTime(new SimpleDateFormat("hh:mm:ss").format(now));
		msg.setPriority(1.1);
		msg.setSource(baseUrl);
		msg.setTitle(content.getTitle());
		msg.setType("recomendation");
		msg.setUser(usrnm);
		
		try
		{
			msg = ewallClient.postForObject(url,msg,NotificationMessage.class);
			// msg.getNotificationID() --> to extract the notification ID, if required.
				
		}
		catch (RestClientException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}catch(Exception e){
			System.out.println("NotManIface{sendFallInfo} error while sending data: " + e.getMessage() + " " + e.toString());
		}	
	}
	
	/**
	 * Sends a primary user related Alarm towards the Notification Manager
	 *
	 * @param usrnm
	 *            the primary user username
	 *      
	 * @param content
	 *            the Alarm content
	 *      
	 */
	public void sendNotificationAlarmUser(String usrnm, NotificationContentMsg content) throws IOException, Exception
	{
		String url = notificationManagerUrl + "/notification";
		Date now = new Date();
		
		NotificationMessage msg = new NotificationMessage();
		
		msg.setContent(content);
		msg.setDate(new SimpleDateFormat("dd-MM-yyyy").format(now));
		msg.setTime(new SimpleDateFormat("hh:mm:ss").format(now));
		msg.setPriority(0.9);
		msg.setSource(baseUrl);
		msg.setTitle(content.getTitle());
		msg.setType("recomendation");
		msg.setUser(usrnm);
		
		try
		{
			msg = ewallClient.postForObject(url,msg,NotificationMessage.class);
			// msg.getNotificationID() --> to extract the notification ID, if required.
				
		}
		catch (RestClientException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}catch(Exception e){
			System.out.println("NotManIface{sendFallInfo} error while sending data: " + e.getMessage() + " " + e.toString());
		}
	
	
	}
	
	/**
	 * Sends a caregiver related notification towards the Notification Manager
	 *
	 * @param usrnm
	 *            the primary user username
	 *      
	 * @param content
	 *            the notification content
	 *      
	 */
	public void sendNotificationCaregiver(String usrnm, NotificationContent content) throws IOException, Exception
	{
		Date now = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		String dateTime = df.format(now);
		
		List<String> crgv = getCrgc(usrnm);
		
		
		String url = notificationManagerUrl + 
				"/{caregiverUsername}/notifications?primaryUser={primaryUser}&dateTime={dateTime}&type=Fall&title={title}&content={content}&prior=1.5&source={source}";
	 
		
		for(String caregiverUsername : crgv)
		{
			//System.out.println("caregiver usrnm: " + caregiverUsername);
			
			try
			{
				String nID = ewallClient.postForObject(url,null,String.class,caregiverUsername,usrnm,dateTime,content.getTitle(),content.getMotivational(),baseUrl);
				//System.out.println("Alarm has been sent ID >> " + nID);
			}
			catch (RestClientException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}catch(Exception e){
				System.out.println("NotManIface{sendAlm} error while sending data: " + e.getMessage() + " " + e.toString());
			}
		}
	
	}
	
	/**
	 * Sends a caregiver related Alarm towards the Notification Manager
	 *
	 * @param usrnm
	 *            the primary user username
	 *      
	 * @param content
	 *            the Alarm content
	 *      
	 */
	public void sendNotificationAlarmCaregiver(String usrnm, NotificationContent content) throws IOException, Exception
	{
		Date now = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		String dateTime = df.format(now);
		
		List<String> crgv = getCrgc(usrnm);
		
		
		String url = notificationManagerUrl + 
				"/{caregiverUsername}/notifications?primaryUser={primaryUser}&dateTime={dateTime}&type=Fall&title={title}&content={content}&prior=1.5&source={source}";
	 
		
		for(String caregiverUsername : crgv)
		{
			//System.out.println("caregiver usrnm: " + caregiverUsername);
			
			try
			{
				String nID = ewallClient.postForObject(url,null,String.class,caregiverUsername,usrnm,dateTime,content.getTitle(),content.getMotivational(),baseUrl);
				//System.out.println("Alarm has been sent ID >> " + nID);
			}
			catch (RestClientException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}catch(Exception e){
				System.out.println("NotManIface{sendAlm} error while sending data: " + e.getMessage() + " " + e.toString());
			}
		}
	
	}

}
