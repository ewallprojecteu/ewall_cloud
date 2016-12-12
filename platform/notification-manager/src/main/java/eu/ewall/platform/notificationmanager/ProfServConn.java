

/****************************************************************
 * Copyright 2014 Ss Cyril and Methodius University in Skopje
***************************************************************/

package eu.ewall.platform.notificationmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.RestOperations;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClientException;

import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.measure.MovementMeasurement;
import eu.ewall.platform.commons.datamodel.message.MessageType;

@Component
public class ProfServConn {
	
	@Value("${profilingServer.url}")
	private String profilingServerUrl;
	
	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;
	
	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(ProfServConn.class);
	
	
	/**
	 * Gathers the user profile for a specific username from the Profiling Serv.
	 * 
	 * @param usrnm
	 * 			the user's username
	 * @return The full user profile 
	 */
		
	public User getUsrdata(String usrnm)
	{
		User usrdat = null;
		try
		{
			usrdat = ewallClient.getForObject(profilingServerUrl+"/users/"+usrnm, User.class);
		}
		catch (RestClientException e) {
			LOG.warn(e.getMessage());
		} catch (NumberFormatException e) {
			LOG.warn(e.getMessage());
		}catch(Exception e){
			LOG.warn("ProfSerConn error while fetching data: " + e.getMessage() + " " + e.toString());
		}
		
		return usrdat;
	}
	
	/**
	 * Gathers the (primary) user's email address from the Profiling Serv.
	 * 
	 * @param usrnm
	 * 			the user's username
	 * @return The user's email 
	 */
	public String getUsrmail(String usrnm)
	{
		String email = "";
		
		try
		{
			User usrdat = getUsrdata(usrnm);
			email = usrdat.getUserProfile().getvCardSubProfile().getEmail();
		} 
		catch (RestClientException e) {
			LOG.warn(e.getMessage());
		} catch (NumberFormatException e) {
			LOG.warn(e.getMessage());
		}catch(Exception e){
			LOG.warn("ProfSerConn{getUsrmail} error while fetching data: " + e.getMessage() + " " + e.toString());
		}
		
		return email;
	}
	
	/**
	 * Gathers the (primary) user's name and surname from the Profiling Serv.
	 * 
	 * @param usrnm
	 * 			the user's username
	 * 
	 * @return The user's name and surname 
	 */
	public String getUsrTrueName(String usrnm)
	{
		String name = "";
		
		try
		{
			User usrdat = getUsrdata(usrnm);
			name = "Name: "+ usrdat.getFirstName() + "\n"+
					"Surname: " +usrdat.getLastName();
		} 
		catch (RestClientException e) {
			LOG.warn(e.getMessage());
		} catch (NumberFormatException e) {
			LOG.warn(e.getMessage());
		}catch(Exception e){
			LOG.warn("ProfSerConn{getUsrTrueName} error while fetching data: " + e.getMessage() + " " + e.toString());
		}
		
		return name;
	}
	
	/**
	 * Gathers the usernames of all formal cairgivers linked with a specific user
	 * 
	 * @param usrnm
	 * 			the user's username
	 * @return List of formal cairgivers' usernames 
	 */
	public List<String> getFrmlCGname(String usrnm)
	{
		List<String> caregivers = new ArrayList<String>();
		
		try
		{
			User usrdat = getUsrdata(usrnm);
			List<String> crgvr = usrdat.getCaregiversUsernamesList();
			
			for(String Sucg: crgvr)
			{	
				User ucg = getUsrdata(Sucg);
				
				if("FORMAL_CAREGIVER".equals(ucg.getUserRole().toString()))
				{
					caregivers.add(ucg.getUsername());
				}
			}
		} 
		catch (RestClientException e) {
			LOG.warn(e.getMessage());
		} catch (NumberFormatException e) {
			LOG.warn(e.getMessage());
		}catch(Exception e){
			LOG.warn("ProfSerConn{getFrmlCGname} error while fetching data: " + e.getMessage() + " " + e.toString());
		}
		
		return caregivers;
	}
	
	/**
	 * Gathers the email address of all formal cairgivers linked with a specific user
	 * 
	 * @param usrnm
	 * 			the user's username
	 * @return List of formal cairgivers' emails 
	 */
	public List<String> getFrmlCGmail(String usrnm)
	{
		List<String> caregivers = new ArrayList<String>();
		
		try
		{
			User usrdat = getUsrdata(usrnm);
			List<String> crgvr = usrdat.getCaregiversUsernamesList();
			
			for(String Sucg: crgvr)
			{	
				User ucg = getUsrdata(Sucg);
				
				if("FORMAL_CAREGIVER".equals(ucg.getUserRole().toString()))
				{
					caregivers.add(ucg.getUserProfile().getvCardSubProfile().getEmail());
				}
			}
		} 
		catch (RestClientException e) {
			LOG.warn(e.getMessage());
		} catch (NumberFormatException e) {
			LOG.warn(e.getMessage());
		}catch(Exception e){
			LOG.warn("ProfSerConn{getFrmlCGmail} error while fetching data: " + e.getMessage() + " " + e.toString());
		}
		
		return caregivers;
	}
	
	/**
	 * Gathers the email address of all informal cairgivers linked with a specific user
	 * 
	 * @param usrnm
	 * 			the user's username
	 * @return List of informal cairgivers' emails 
	 */
	public List<String> getInfrmlCGmail(String usrnm)
	{
		List<String> caregivers = new ArrayList<String>();
		
		try
		{
			User usrdat = getUsrdata(usrnm);
			List<String> crgvr = usrdat.getCaregiversUsernamesList();
			
			for(String Sucg: crgvr)
			{
				User ucg = getUsrdata(Sucg);
				
				if("INFORMAL_CAREGIVER".equals(ucg.getUserRole().toString()))
				{
					caregivers.add(ucg.getUserProfile().getvCardSubProfile().getEmail());
				}
			}
		} 
		catch (RestClientException e) {
			LOG.warn(e.getMessage());
		} catch (NumberFormatException e) {
			LOG.warn(e.getMessage());
		}catch(Exception e){
			LOG.warn("ProfSerConn{getFrmlCGmail} error while fetching data: " + e.getMessage() + " " + e.toString());
		}
		
		return caregivers;
	}
	
	
	/**
	 * Gathers the usernames of all informal cairgivers linked with a specific user
	 * 
	 * @param usrnm
	 * 			the user's username
	 * @return List of informal cairgivers' usernames 
	 */
	public List<String> getInfrmlCGname(String usrnm)
	{
		List<String> caregivers = new ArrayList<String>();
		
		try
		{
			User usrdat = getUsrdata(usrnm);
			List<String> crgvr = usrdat.getCaregiversUsernamesList();
			
			for(String Sucg: crgvr)
			{
				User ucg = getUsrdata(Sucg);
				
				if("INFORMAL_CAREGIVER".equals(ucg.getUserRole().toString()))
				{
					caregivers.add(ucg.getUsername());
				}
			}
		} 
		catch (RestClientException e) {
			LOG.warn(e.getMessage());
		} catch (NumberFormatException e) {
			LOG.warn(e.getMessage());
		}catch(Exception e){
			LOG.warn("ProfSerConn{getFrmlCGmail} error while fetching data: " + e.getMessage() + " " + e.toString());
		}
		
		return caregivers;
	}
	
	
	// used only for local testing purposes
	/*public void updateNots() 
	{
		String usrnm = "fcg1";
		
		User usrdat = getUsrdata(usrnm);
		
		usrdat.getUserProfile().getEWallSubProfile().getSelectedNotificationTypesList().add(MessageType.REMINDER_SLEEP);
		
		try
		{
			ewallClient.postForObject(profilingServerUrl+"/users/"+usrnm, usrdat, User.class);
			//usrdat = ewallClient.getForObject(profilingServerUrl+"/users/"+usrnm, User.class);
		}
		catch (RestClientException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}catch(Exception e){
			System.out.println("ProfSerConn error while fetching data: " + e.getMessage() + " " + e.toString());
		}
	}*/
	
	
	
	/**
	 * Checks if the user has selected the given notification type
	 * 
	 * @param usrnm
	 * 			the user's username
	 * @param msgType
	 * 			the given notification type
	 * @return Binary decision whether the user has selected the given notification type
	 */
	public boolean isNotifSelected(String usrnm, MessageType msgType)
	{
		boolean decision = false;
		
		try
		{
			User usrdat = getUsrdata(usrnm);
			List<MessageType> NotifList = usrdat.getUserProfile().geteWallSubProfile().getSelectedNotificationTypesList();
			
			if ((NotifList.size() == 0) || (NotifList== null) || (msgType.equals(MessageType.NORMAL)))
			{
				
				decision = true;
			}
			else 
			 {
				for(MessageType msgC: NotifList)
				{
			
					if (msgType.equals(msgC))
					{
						
						decision = true;
					}
				}
			 }
			
		} 
		catch (RestClientException e) {
			LOG.warn(e.getMessage());
		} catch (NumberFormatException e) {
			LOG.warn(e.getMessage());
		}catch(Exception e){
			LOG.warn("ProfSerConn{isNotifSelected} error while fetching data: " + e.getMessage() + " " + e.toString());
		}
		
		return decision;
	}
	
	/**
	 * Checks if the user is in the living room (utilizing the PIR sensor data in the living room)
	 * 
	 * @param usrnm
	 * 			the user's username
	 * @return Binary decision whether the user is in the living room (i.e. "1" if the user is the room, "0" if not)  
	 */
	public String getUsrLoc(String usrnm)
	{
		Date to = new Date();
		Date from = new Date(to.getTime()-10000); // hardcoded at 10sec.
		
		MovementMeasurement[] mvmnt = null;
		try
		{
			mvmnt = ewallClient.getForObject(profilingServerUrl+"/users/"+usrnm+"/movement/timestamp?from="+from.getTime()+"&to="+to.getTime(), new MovementMeasurement[0].getClass());
			//mvmnt = ewallClient.getForObject("http://ewall.radio.pub.ro/platform-dev/profiling-server/users/leonard/movement/timestamp?from=1415029103338&to=1415029114232", new MovementMeasurement[0].getClass());
		}
		catch (RestClientException e) {
			LOG.warn(e.getMessage());
		} catch (NumberFormatException e) {
			LOG.warn(e.getMessage());
		}catch(Exception e){
			LOG.warn("ProfSerConn{getUsrLoc} error while fetching data: " + e.getMessage() + " " + e.toString());
		}
		
		
		if ((mvmnt == null) || (mvmnt.length==0))
		{
			return "0"; // if no location info available --> assume mainscreen as default
		}
		else 
		{
			//System.out.println("mvmnt.length: " + String.valueOf(mvmnt.length));
			if (mvmnt[mvmnt.length-1].getMeasuredValueBoolean())
			{
				return "0";
			}
			else
			{
				return "1";
			}
		}

	}

}
