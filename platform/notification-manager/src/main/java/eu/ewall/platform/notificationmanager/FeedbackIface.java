package eu.ewall.platform.notificationmanager;

/****************************************************************
 * Copyright 2015 Ss Cyril and Methodius University in Skopje
***************************************************************/

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.io.IOException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
//import javax.servlet.http.HttpServletRequest;


import eu.ewall.platform.commons.datamodel.message.MessageType;
import eu.ewall.platform.notificationmanager.dao.NotificationMongo;
import eu.ewall.platform.notificationmanager.dao.NotificationDBcontent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;


/**
 * The Class FeedbackIface
 */

@RestController
public class FeedbackIface {
	
	@Value("${local.addr}")
	private String theAddr;
	
	@Autowired
	private NotificationMongo NotificationMongo;
	
	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;
	
	/**
	 * Gets the feedback from the mainscreen/user 
	 *
	 * @param id
	 *            the Notification id in the DB
	 *      
	 * @param feedback
	 *            the usesr's feedback            
	 * 
	 */
	@RequestMapping(value = "/userFeedback", method = RequestMethod.POST)
	public void getFeedback(@RequestParam(value="notID", required=true, defaultValue="") String id, 
			@RequestParam(value="feedback", required=true, defaultValue="") String feedback)
	{
		String url = NotificationMongo.getSource(id);
		
		//System.out.println("The url address: " + getSourceAdd(url));
		
		ewallClient.postForObject(getSourceAdd(url)+"/Feedback?notID="+id+"&feedback="+feedback,"",String.class);
		
		
	}
	
	/**
	 * Convert the url base address of the source in useful format that can be utilized for communication between components in the cloud
	 *
	 * @param url
	 *            the base address of the component
	 *      
	 * @return the valid communication address of the component 
	 */
	public String getSourceAdd(String url)
	{
		int indx = url.indexOf('/');
		int pos = 0;
		while (indx != -1)
		{
			   System.out.println("Index at " + indx);
			   indx = url.indexOf('/', indx + 1);
			   
			   if (indx != -1)
			   {
				   pos = indx;
			   }
		}
		String base = url.substring(pos);
		System.out.println("the base is " + base);
		
		return theAddr+base;
	}
	
	// only for local testing purposes 
	/*@RequestMapping(value = "/Feedback", method = RequestMethod.POST)
	public void testFeedback(@RequestParam(value="notID", required=true, defaultValue="") String id, 
			@RequestParam(value="feedback", required=true, defaultValue="") String feedback)
	{
		
		System.out.println("The ID address: " + id);
		System.out.println("The feedback: " + feedback);
	}*/

}
