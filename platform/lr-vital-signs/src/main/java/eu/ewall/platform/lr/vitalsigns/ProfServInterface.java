

/****************************************************************
 * Copyright 2015 Ss Cyril and Methodius University in Skopje
***************************************************************/

package eu.ewall.platform.lr.vitalsigns;

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
import eu.ewall.platform.commons.datamodel.profile.preferences.*;
import eu.ewall.platform.commons.datamodel.measure.BloodPressureMeasurement;
import eu.ewall.platform.commons.datamodel.measure.HeartRateMeasurement;
import eu.ewall.platform.commons.datamodel.measure.OxygenSaturationMeasurement;
import eu.ewall.platform.commons.datamodel.activity.*;

@Component
public class ProfServInterface {
	
	@Value("${profilingServer.url}")
	private String profilingServerUrl;
	
	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;
	
	
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
			//System.out.println("NM {VSLR} user data fetching");
		}
		catch (RestClientException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}catch(Exception e){
			System.out.println("ProfSerConn {VSLR} error while fetching data: " + e.getMessage() + " " + e.toString());
		}
		
		return usrdat;
	}
	
	/**
	 * Gathers the usernames of the primary users
	 * 
	 * @return The list of usernames 
	 */
		
	public List<String> getPrimaryUsers()
	{
		User[] usrdat = null;
		List<String> usernames = new ArrayList<String>();
		
		try
		{
			usrdat = ewallClient.getForObject(profilingServerUrl+"/users/", new User[0].getClass());
			//System.out.println("getPrimaryUsers {VSLR} user data fetching");
			
			for(User index : usrdat)
			{
				if("PRIMARY_USER".equals(index.getUserRole().toString()))
				{
					usernames.add(index.getUsername());
				}
			}
		}
		catch (RestClientException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}catch(Exception e){
			System.out.println("ProfSerConn {VSLR - getPrimaryUsers} error while fetching data: " + e.getMessage() + " " + e.toString());
		}
		
		return usernames;
	}
	
	
	/**
	 * Gathers the preferred user language
	 * 
	 * @param usrnm
	 * 			the user's username
	 * @return The preferred user language 
	 */
		
	public String getUsrLang(String usrnm)
	{
		String lang = "en";
		try
		{
			User usrdat = getUsrdata(usrnm);
			//System.out.println("VSLR user data fetching");
			
			SystemPreferences sysPrefs = (SystemPreferences)usrdat.getUserProfile().getUserPreferencesSubProfile().getSystemPreferences();
			
			if (sysPrefs !=null)
			{
				lang = sysPrefs.getPreferredLanguage();
			}
			
			 
		}
		catch (RestClientException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}catch(Exception e){
			System.out.println("VSLR error while fetching data: " + e.getMessage() + " " + e.toString());
		}
		
		return lang;
	}
	
	/**
	 * Gathers the blood presure measurements
	 * 
	 * @param usrnm
	 * 			the user's username
	 * @return The average measurement samples 
	 */
		
	public List<Float> getUsrBP(String usrnm, Date To)
	{
		BloodPressureMeasurement[] BPMeasurements = null;
		
		float systBP = -1;
		float diasBP = -1;
		
		Date From = new Date(To.getTime() - 2*60*60000); // intervals of two hours
		
		try
		{
			BPMeasurements = ewallClient.getForObject(profilingServerUrl+"/users/"+usrnm+"/vitals/bloodpressure?from="+From.getTime()+"&to="+To.getTime(), new BloodPressureMeasurement[0].getClass());
			//System.out.println("BPM {VSLR} user data fetching");
			
			// average the BP
						if ((BPMeasurements != null) && (BPMeasurements.length > 0))
						{
							int sumSys = 0;
							int sumDias = 0;
							
							for(BloodPressureMeasurement index : BPMeasurements)
							{
								sumSys+=index.getSystolicBloodPressure();
								sumDias+=index.getDiastolicBloodPressure();
							}
							
							systBP = (float) sumSys/BPMeasurements.length;
							diasBP = (float) sumDias/BPMeasurements.length;
						}
		}
		catch (RestClientException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}catch(Exception e){
			System.out.println("ProfSerConn {VSLR-BPM} error while fetching data: " + e.getMessage() + " " + e.toString());
		}
		
		ArrayList<Float> samples = new ArrayList<Float>();
		samples.add(systBP);
		samples.add(diasBP);
		
		return samples;
	}
	
	/**
	 * Gathers the Hear Rate measurements
	 * 
	 * @param usrnm
	 * 			the user's username
	 * @return The average measurement samples 
	 */
		
	public List<Float> getUsrHR(String usrnm, Date To)
	{
		HeartRateMeasurement[] HRMeasurements = null;
		
		float hrm = -1;
		float hrv = -1;
		
		Date From = new Date(To.getTime() - 2*60*60000); // intervals of two hours
		
		try
		{
			HRMeasurements = ewallClient.getForObject(profilingServerUrl+"/users/"+usrnm+"/vitals/heartrate?from="+From.getTime()+"&to="+To.getTime(), new HeartRateMeasurement[0].getClass());
			//System.out.println("HRM {VSLR} user data fetching from >> " + profilingServerUrl);
			
			// average the BP
						if ((HRMeasurements != null) && (HRMeasurements.length > 0))
						{
							int bpm = 0;
							int bpmvar = 0;
							
							for(HeartRateMeasurement index : HRMeasurements)
							{
								bpm+=index.getHeartRate();
								bpmvar+=index.getHeartRateVariability();
							}
							
							hrm = (float) bpm/HRMeasurements.length;
							hrv = (float) bpmvar/HRMeasurements.length;
						}
		}
		catch (RestClientException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}catch(Exception e){
			System.out.println("ProfSerConn {VSLR-HRM} error while fetching data: " + e.getMessage() + " " + e.toString());
		}
		
		ArrayList<Float> samples = new ArrayList<Float>();
		samples.add(hrm);
		samples.add(hrv);
		
		return samples;
	}
	
	/**
	 * Gathers the Oxygen Saturation measurements
	 * 
	 * @param usrnm
	 * 			the user's username
	 * @return The average measurement samples 
	 */
		
	public float getUsrOS(String usrnm, Date To)
	{
		OxygenSaturationMeasurement[] OSMeasurements = null;
		
		float oxSat = -1;
		
		Date From = new Date(To.getTime() - 2*60*60000); // intervals of two hours
		
		try
		{
			OSMeasurements = ewallClient.getForObject(profilingServerUrl+"/users/"+usrnm+"/vitals/oxygensaturation?from="+From.getTime()+"&to="+To.getTime(), new OxygenSaturationMeasurement[0].getClass());
			//System.out.println("OS {VSLR} user data fetching from >> " + profilingServerUrl);
			
			// average the BP
						if ((OSMeasurements != null) && (OSMeasurements.length > 0))
						{
							int osSum = 0;
							
							for(OxygenSaturationMeasurement index : OSMeasurements)
							{
								osSum+=index.getOxygenSaturation();
								
							}
							
							oxSat = (float) osSum/OSMeasurements.length;
						}
		}
		catch (RestClientException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}catch(Exception e){
			System.out.println("ProfSerConn {VSLR-OS} error while fetching data: " + e.getMessage() + " " + e.toString());
		}
		
				
		return oxSat;
	}
	
	/**
	 * Extracts the caregivers userrnames from the coresponding user profile
	 * 
	 * @param usrnm
	 * 			the user's username
	 * 
	 * @return Returns the list of caregiver usernames 
	 */
	
	public List<String> getCaregivers(String usrnm)
	{
        List<String> caregivers = new ArrayList<String>();
		
		try
		{
			User usrdat = ewallClient.getForObject(profilingServerUrl+"/users/"+usrnm, User.class);
			List<String> crgvr = usrdat.getCaregiversUsernamesList();
			
			for(String Sucg: crgvr)
			{	
				User ucg = ewallClient.getForObject(profilingServerUrl+"/users/"+Sucg, User.class);
				
				if("FORMAL_CAREGIVER".equals(ucg.getUserRole().toString()))
				{
					caregivers.add(ucg.getUsername());
				}
			}
		} 
		catch (RestClientException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}catch(Exception e){
			System.out.println("FDR ProfSerConn{getCaregivers} error while fetching data: " + e.getMessage() + " " + e.toString());
		}
		
		return caregivers;
	}
	
	/**
	 * Return whether the primary user is physically active in the given moment of time
	 * 
	 * @param usrnm
	 * 			the user's username
	 * 
	 * @return Returns true if the user is physically active  
	 */
	
	public boolean isExercising(String usrnm)
	{
		boolean response = false;
		try
		{
			User usrdat = getUsrdata(usrnm);
			
			List<Activity> activ = usrdat.getUserProfile().getActivitiesSubProfile().getActivities();
			
			Date now = new Date();
			long tmpstmp = now.getTime();
			
			for (Activity index : activ)
			{
				if("EXERCISING".equals(index.getActivityType()) || "RUNNING".equals(index.getActivityType()) || "WALKING".equals(index.getActivityType()))
				{
					long from = index.getTimeInterval().getStartTime();
					long until = index.getTimeInterval().getEndTime();
					
					if ((tmpstmp>=from)&&(tmpstmp<=until))
					{
						response = true;
					}
				}
				
			}
			
			 
		}
		catch (RestClientException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}catch(Exception e){
			System.out.println("isExercising error while fetching data: " + e.getMessage() + " " + e.toString());
		}
		
		return response;
	}

}
