package eu.ewall.platform.lr.vitalsigns;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import eu.ewall.platform.commons.datamodel.message.MessageType;
import eu.ewall.platform.lr.vitalsigns.dao.VitalSignsMongo;
import eu.ewall.platform.lr.vitalsigns.dao.VitalSignsDBcontent;
import eu.ewall.platform.lr.vitalsigns.dao.VitalSignsDBcontentFloating;

@Component
public class VitalSignsDecisionMaker {
	
	@Value("${hr.thr}")
	private String hrThr;
	
	@Value("${hrv.thr}")
	private String hrvThr;
	
	@Value("${sbp.thr}")
	private String sbpThr;
	
	@Value("${dbp.thr}")
	private String dbpThr;
	
	@Value("${os.thr}")
	private String osThr;
	
	@Autowired
	private ProfServInterface ProfServInterface;
	
	@Autowired
	private VitalSignsMongo VitalSignsMongo;
	
	@Autowired
	private NotificationService NotificationService;
	
	/**
	 * Triggers the collection of vitals for the primary users
	 * 
	 */
	public void gatherSamples()
	{
		List<String> primUsrs = ProfServInterface.getPrimaryUsers();
		
		if (primUsrs != null)
		{
			for (String index : primUsrs)
			{
				samplesHour(index);
				
				//System.out.println("VLSR: The PU name is: " + index);
			}
		}
		
		
	}
	
	
	/**
	 * Gathers the primary user vitals and processes them accordingly
	 * 
	 * @param username
	 * 			the user's username
	 */
	public void samplesHour(String username) 
	{
		
		//System.out.println(" The PU name is: " + username);
		
		Date now = new Date();
		VitalSignsDBcontent samples = new VitalSignsDBcontent();
		VitalSignsDBcontentFloating samplesF = new VitalSignsDBcontentFloating();
		
		samples.setDateTime(now);
		samples.setUser(username);
		samplesF.setDateTime(now);
		samplesF.setUser(username);
        
		List <Float> bpmeas = ProfServInterface.getUsrBP(username, now);
		
		samples.setSbp(bpmeas.get(0));
		samples.setDbp(bpmeas.get(1));
		samplesF.setSbp(bpmeas.get(0));
		samplesF.setDbp(bpmeas.get(1));
        
       // System.out.println("sBPM is: "+bpmeas.get(0));
       // System.out.println("dBPM is: "+bpmeas.get(1));
        
        
        
        List <Float> hrmeas = ProfServInterface.getUsrHR(username, now);
        
        samples.setHr(hrmeas.get(0));
        samples.setHrv(hrmeas.get(1));
        samplesF.setHr(hrmeas.get(0));
        samplesF.setHrv(hrmeas.get(1));
      
        //System.out.println("HR is: "+hrmeas.get(0));
        //System.out.println("HRV is: "+hrmeas.get(1));
      
        
        
        float oxSatur = ProfServInterface.getUsrOS(username, now);
        
        samples.setOs(oxSatur);
        samplesF.setOs(oxSatur);
	      
        //System.out.println("Oxygen Saturation is: "+ oxSatur);
		
		
        
        // storing hourly and floating info 
        samples.setAverInt("hour");
        int cntH = VitalSignsMongo.addMeasHour(samples);
        
        if(!ProfServInterface.isExercising(username))
        {
	        samplesF.setAverInt("floating");
	        VitalSignsMongo.addMeasFloating(samplesF);
        }
        
        if(cntH==12)
        {
        	// calc the daily value 
        	samplesDay(username);
        }
        
        
        switch(checkInstantDeviation(username,samples).toLowerCase())
        {
	        case "hr": NotificationService.caregiverAlarm(username, "hr", "instant", samples);
	        		   NotificationService.userAlalrm(username, "hr", "instant");
	        		   
	 	 	break;
	 	 	
	        case "hrv": NotificationService.caregiverAlarm(username, "hr", "instant", samples); // the same notification template is sent for the hr and hrv
 		                NotificationService.userAlalrm(username, "hr", "instant");
 		                
	        break;
	        
	        case "sbp": NotificationService.caregiverAlarm(username, "bp", "instant", samples); 
             	        NotificationService.userAlalrm(username, "bp", "instant");
             	        
            break;
            
	        case "dbp": NotificationService.caregiverAlarm(username, "bp", "instant", samples); // the same notification template is sent for the sbp and dbp
	        			NotificationService.userAlalrm(username, "bp", "instant");
	        			
 	        break;
 	        
	        case "os": NotificationService.caregiverAlarm(username, "os", "instant", samples);  
					   NotificationService.userAlalrm(username, "os", "instant");
					   
			break;
        }
        
       
        
    
	}
	
	/**
	 * Gathers the hourly primary user vitals and processes them accordingly
	 * 
	 * @param username
	 * 			the user's username
	 */
	public void samplesDay(String username)
	{
		VitalSignsDBcontent sumsamples = calcAvgVal(username, "hour");
		sumsamples.setAverInt("day");
		int cntD = VitalSignsMongo.addMeasDay(sumsamples);
		
		if(cntD==7)
        {
        	// calc the weekly value --> samplesWeek
			samplesWeek(username);
        }
		
		// code for slight deviation detection
		
	   switch(checkSlightDeviation(username,"day",cntD).toLowerCase())
        {
	        case "hr": NotificationService.userNotification(username, "hr", "slight");
	 	 	break;
	 	 	
	        case "hrv": NotificationService.userNotification(username, "hr", "slight");
	        break;
	        
	        case "sbp": NotificationService.userNotification(username, "bp", "slight");
            break;
            
	        case "dbp": NotificationService.userNotification(username, "bp", "slight");
 	        break;
 	        
	        case "os": NotificationService.userNotification(username, "os", "slight");
			break;
        }
	}
	
	/**
	 * Gathers the daily primary user vitals and processes them accordingly
	 * 
	 * @param username
	 * 			the user's username
	 */
	public void samplesWeek(String username)
	{
		VitalSignsDBcontent sumsamples = calcAvgVal(username, "day");
		sumsamples.setAverInt("week");
		int cntD = VitalSignsMongo.addMeasWeek(sumsamples);
		
		if(cntD==4)
        {
        	// calc the weekly value --> samplesWeek
			samplesMonth(username);
        }
		
		// code for slight deviation detection
		
		
		switch(checkSlightDeviation(username,"week",cntD).toLowerCase())
        {
	        case "hr": NotificationService.caregiverNotification(username, "hr", "slight", sumsamples);
	        		   NotificationService.userNotification(username, "hr", "slight");
	 	 	break;
	 	 	
	        case "hrv": NotificationService.caregiverNotification(username, "hr", "slight", sumsamples); // the same notification template is sent for the hr and hrv
 		               NotificationService.userNotification(username, "hr", "slight");
	        break;
	        
	        case "sbp": NotificationService.caregiverNotification(username, "bp", "slight", sumsamples); 
             	        NotificationService.userNotification(username, "bp", "slight");
            break;
            
	        case "dbp": NotificationService.caregiverNotification(username, "bp", "slight", sumsamples); // the same notification template is sent for the sbp and dbp
	        			NotificationService.userNotification(username, "bp", "slight");
 	        break;
 	        
	        case "os": NotificationService.caregiverNotification(username, "os", "slight", sumsamples);  
					   NotificationService.userNotification(username, "os", "slight");
			break;
        }
	}
	
	/**
	 * Gathers the weekly primary user vitals and processes them accordingly
	 * 
	 * @param username
	 * 			the user's username
	 */
	public void samplesMonth(String username)
	{
		VitalSignsDBcontent sumsamples = calcAvgVal(username, "week");
		sumsamples.setAverInt("month");
		int cntD = VitalSignsMongo.addMeasMonth(sumsamples);
		
		if(cntD==12)
        {
        	// calc the weekly value
			samplesYear(username);
        }
		
		// code for slight deviation detection
		

        boolean isChronic = false;
		switch(checkChronicDeviation(username,"month",cntD))
        {
	        case "hr": NotificationService.caregiverNotification(username, "hr", "chronic", sumsamples);
	        		   NotificationService.userNotification(username, "hr", "chronic");
	        		   isChronic = true;
	        		   
	 	 	break;
	 	 	
	        case "hrv": NotificationService.caregiverNotification(username, "hr", "chronic", sumsamples); // the same notification template is sent for the hr and hrv
 		                NotificationService.userNotification(username, "hr", "chronic");
 		                isChronic = true;
	        break;
	        
	        case "sbp": NotificationService.caregiverNotification(username, "bp", "chronic", sumsamples); 
             	        NotificationService.userNotification(username, "bp", "chronic");
             	       isChronic = true;
            break;
            
	        case "dbp": NotificationService.caregiverNotification(username, "bp", "chronic", sumsamples); // the same notification template is sent for the sbp and dbp
	        			NotificationService.userNotification(username, "bp", "chronic"); 
	        			isChronic = true;
 	        break;
 	        
	        case "os": NotificationService.caregiverNotification(username, "os", "chronic", sumsamples);  
					   NotificationService.userNotification(username, "os", "chronic");
					   isChronic = true;
			break;
        }
		
		
		if(!isChronic)
				//chronic deviation not triggered
        {
        	// trigger a notification to PU and CG
			switch(checkSlightDeviation(username,"month",cntD).toLowerCase())
	        {
		        case "hr": NotificationService.caregiverNotification(username, "hr", "slight", sumsamples);
		        		   NotificationService.userNotification(username, "hr", "slight");
		        		   
		 	 	break;
		 	 	
		        case "hrv": NotificationService.caregiverNotification(username, "hr", "slight", sumsamples); // the same notification template is sent for the hr and hrv
	 		               NotificationService.userNotification(username, "hr", "slight");
		        break;
		        
		        case "sbp": NotificationService.caregiverNotification(username, "bp", "slight", sumsamples); 
	             	        NotificationService.userNotification(username, "bp", "slight");
	            break;
	            
		        case "dbp": NotificationService.caregiverNotification(username, "bp", "slight", sumsamples); // the same notification template is sent for the sbp and dbp
		        			NotificationService.userNotification(username, "bp", "slight");
	 	        break;
	 	        
		        case "os": NotificationService.caregiverNotification(username, "os", "slight", sumsamples);  
						   NotificationService.userNotification(username, "os", "slight");
				break;
	        }
        }
		
		
	}
	
	/**
	 * Gathers the monthly primary user vitals and processes them accordingly
	 * 
	 * @param username
	 * 			the user's username
	 */
	public void samplesYear(String username)
	{
		VitalSignsDBcontent sumsamples = calcAvgVal(username, "month");
		sumsamples.setAverInt("year");
		int cntD = VitalSignsMongo.addMeasYear(sumsamples);
		
		switch(checkChronicDeviation(username,"year",cntD))
        {
	        case "hr": NotificationService.caregiverNotification(username, "hr", "chronic", sumsamples);
	        		   NotificationService.userNotification(username, "hr", "chronic");
	        		   
	 	 	break;
	 	 	
	        case "hrv": NotificationService.caregiverNotification(username, "hr", "chronic", sumsamples); // the same notification template is sent for the hr and hrv
 		                NotificationService.userNotification(username, "hr", "chronic");
 		                
	        break;
	        
	        case "sbp": NotificationService.caregiverNotification(username, "bp", "chronic", sumsamples); 
             	        NotificationService.userNotification(username, "bp", "chronic");
             	       
            break;
            
	        case "dbp": NotificationService.caregiverNotification(username, "bp", "chronic", sumsamples); // the same notification template is sent for the sbp and dbp
	        			NotificationService.userNotification(username, "bp", "chronic"); 
	        			
 	        break;
 	        
	        case "os": NotificationService.caregiverNotification(username, "os", "chronic", sumsamples);  
					   NotificationService.userNotification(username, "os", "chronic");
					   
			break;
        }
		
	}
	
	
	/**
	 * Decides whether a deviation is present in the user's vitals
	 * 
	 * @param username
	 * 			the user's username
	 * @param AvInt
	 * 			the time frame of interest
	 * @param cnt
	 * 			the number of samples taken into consideration
	 * @return
	 * 		   the source of possible deviation 
	 */
	public String checkSlightDeviation(String username, String AvInt, int cnt)
	{
		String deviation = "none";
		
		List<VitalSignsDBcontent> samples = VitalSignsMongo.getMeas(username, AvInt);
		
		double thrsh = (cnt-1)/2;
		
		double firstHR = 0;
		double lastHR = 0;
		double aboveHR = 0;
		double firstHRV = 0;
		double lastHRV = 0;
		double belowHRV = 0;
		double firstSBP = 0;
		double lastSBP = 0;
		double aboveSBP = 0;
		double firstDBP = 0;
		double lastDBP = 0;
		double aboveDBP = 0;
		double firstOS = 0;
		double lastOS = 0;
		double belowOS = 0;
		
		
		
		for (VitalSignsDBcontent index : samples)
		{
			// init first values
			if((firstHR == 0 ) && (index.getHr()!=-1) )
			{
				firstHR = index.getHr();
			}
			
			if((firstHRV == 0)&& (index.getHrv()!=-1))
			{
				firstHRV = index.getHrv();
			}
			
			if((firstSBP == 0) && (index.getSbp()!=-1) )
			{
				firstSBP = index.getSbp();
			}
			
			if((firstDBP == 0) && (index.getDbp()!=-1) )
			{
				firstDBP = index.getDbp();
			}
			
			if((firstOS == 0) && (index.getOs()!=-1))
			{
				firstOS = index.getOs();
			}
			
			
			 
			 // init last values
			 if((index.getHr()!=-1) ) 
				{
				  lastHR = index.getHr();
				}
				
				if((index.getHrv()!=-1))
				{
					lastHRV = index.getHrv();
				}
				
				if((index.getSbp()!=-1) )
				{
					lastSBP = index.getSbp();
				}
				
				if((index.getDbp()!=-1) )
				{
					lastDBP = index.getDbp();
				}
				
				if((index.getOs()!=-1))
				{
					lastOS = index.getOs();
				}
			
			if((firstHR < index.getHr()) && (index.getHr()!=-1) && (firstHR!=0))
			{
				aboveHR++;
			}
			
			if((firstHRV > index.getHrv()) && (index.getHrv()!=-1) && (firstHRV!=0))
			{
				belowHRV++;
			}
			
			if((firstSBP < index.getSbp()) && (index.getSbp()!=-1) && (firstSBP!=0))
			{
				aboveSBP++;
			}
			
			if((firstDBP < index.getDbp()) && (index.getDbp()!=-1) && (firstDBP!=0))
			{
				aboveDBP++;
			}
			
			if((firstOS > index.getOs()) && (index.getOs()!=-1) && (firstOS!=0))
			{
				belowOS++;
			}
		}
		
		if((aboveHR>=thrsh) && (lastHR > firstHR))
		{
			deviation = "hr";
		}
		
		if((belowHRV>=thrsh) && (lastHRV < firstHRV))
		{
			deviation = "hrv";
		}
		
		if((aboveSBP>=thrsh) && (lastSBP > firstSBP))
		{
			deviation = "sbp";
		}
		
		if((aboveDBP>=thrsh) && (lastDBP > firstDBP))
		{
			deviation = "dbp";
		}
		
		if((belowOS>=thrsh) && (lastOS < firstOS))
		{
			deviation = "os";
		}
		
		return deviation;
	}
	
	/**
	 * Decides whether a deviation is present in the user's vitals
	 * 
	 * @param username
	 * 			the user's username
	 * @param AvInt
	 * 			the time frame of interest
	 * @param cnt
	 * 			the number of samples taken into consideration
	 * @return
	 * 		   the source of possible deviation 
	 */
	public String checkChronicDeviation(String username, String AvInt, int cnt)
	{
		String deviation = "none";
		
		List<VitalSignsDBcontent> samples = VitalSignsMongo.getMeas(username, AvInt);
		
		double thrsh = 0;
		
		if ((cnt < 4) && ("year".equals(AvInt)))
		{thrsh = cnt*0.5;}
		else {thrsh = cnt*0.75;}
		 // more than 75 % of the samples should be above the threshold to assume on a chronic deviation
		
		double firstHR = 0;
		double lastHR = 0;
		double aboveHR = 0;
		double firstHRV = 0;
		double lastHRV = 0;
		double belowHRV = 0;
		double firstSBP = 0;
		double lastSBP = 0;
		double aboveSBP = 0;
		double firstDBP = 0;
		double lastDBP = 0;
		double aboveDBP = 0;
		double firstOS = 0;
		double lastOS = 0;
		double belowOS = 0;
		
		
		
		for (VitalSignsDBcontent index : samples)
		{
			// init first values
			if((firstHR == 0 ) && (index.getHr()!=-1) )
			{
				firstHR = index.getHr();
			}
			
			if((firstHRV == 0)&& (index.getHrv()!=-1))
			{
				firstHRV = index.getHrv();
			}
			
			if((firstSBP == 0) && (index.getSbp()!=-1) )
			{
				firstSBP = index.getSbp();
			}
			
			if((firstDBP == 0) && (index.getDbp()!=-1) )
			{
				firstDBP = index.getDbp();
			}
			
			if((firstOS == 0) && (index.getOs()!=-1))
			{
				firstOS = index.getOs();
			}
			
			
			 
			 // init last values
			 if((index.getHr()!=-1) ) 
				{
				  lastHR = index.getHr();
				}
				
				if((index.getHrv()!=-1))
				{
					lastHRV = index.getHrv();
				}
				
				if((index.getSbp()!=-1) )
				{
					lastSBP = index.getSbp();
				}
				
				if((index.getDbp()!=-1) )
				{
					lastDBP = index.getDbp();
				}
				
				if((index.getOs()!=-1))
				{
					lastOS = index.getOs();
				}
			
			if((firstHR < index.getHr()) && (index.getHr()!=-1) && (firstHR!=0))
			{
				aboveHR++;
			}
			
			if((firstHRV > index.getHrv()) && (index.getHrv()!=-1) && (firstHRV!=0))
			{
				belowHRV++;
			}
			
			if((firstSBP < index.getSbp()) && (index.getSbp()!=-1) && (firstSBP!=0))
			{
				aboveSBP++;
			}
			
			if((firstDBP < index.getDbp()) && (index.getDbp()!=-1) && (firstDBP!=0))
			{
				aboveDBP++;
			}
			
			if((firstOS > index.getOs()) && (index.getOs()!=-1) && (firstOS!=0))
			{
				belowOS++;
			}
		}
		
		if((aboveHR>=thrsh) && (lastHR > firstHR))
		{
			deviation = "hr";
		}
		
		if((belowHRV>=thrsh) && (lastHRV < firstHRV))
		{
			deviation = "hrv";
		}
		
		if((aboveSBP>=thrsh) && (lastSBP > firstSBP))
		{
			deviation = "sbp";
		}
		
		if((aboveDBP>=thrsh) && (lastDBP > firstDBP))
		{
			deviation = "dbp";
		}
		
		if((belowOS>=thrsh) && (lastOS < firstOS))
		{
			deviation = "os";
		}
		
		return deviation;
	}
	
	/**
	 * Check if the current sample is above/bellow the average value by some threshold margin
	 * 
	 * @param username
	 * 			the user's username
	 * 
	 * @param samples
	 * 			the user's vital samples
	 * 
	 * @return	
	 * 			True if there has been a deviation detected, false if there is no deviation detected
	 */
	public String checkInstantDeviation(String username, VitalSignsDBcontent samples)
	{
		String deviation = "none";
		
		VitalSignsDBcontentFloating samplesF = VitalSignsMongo.getMeasFloating(username);
		
		float hrT = Float.parseFloat(hrThr);
		float hrvT = Float.parseFloat(hrvThr);
		float sbpT = Float.parseFloat(sbpThr);
		float dbpT = Float.parseFloat(dbpThr);
		float osT = Float.parseFloat(osThr);
		
	   if(!ProfServInterface.isExercising(username)){
			if((samples.getHr()>=hrT+samplesF.getHr())&&(samplesF.getHr()!=0)&&(samples.getHr()!=-1))
			{deviation = "hr";}
			else if((samples.getHrv()<=samplesF.getHrv()-hrvT)&&(samplesF.getHrv()!=0)&&(samples.getHrv()!=-1))
			{deviation = "hrv";}
			else if((samples.getSbp()>=sbpT+samplesF.getSbp())&&(samplesF.getSbp()!=0)&&(samples.getSbp()!=-1))
			{deviation = "sbp";}
			else if((samples.getDbp()>=dbpT+samplesF.getDbp())&&(samplesF.getDbp()!=0)&&(samples.getDbp()!=-1))
			{deviation = "dbp";}
			else if((samples.getOs()<=samplesF.getOs()-osT)&&(samplesF.getOs()!=0)&&(samples.getOs()!=-1))
			{deviation = "os";}
	   }
			
		
		return deviation;
	}
	
	/**
	 * Computes an average of the vitals samples
	 * 
	 * @param usrnm
	 * 			the user's username
	 * 
	 * @param AvInt
	 * 			the time interval
	 * 
	 * @return	
	 * 			The average values
	 */
	public VitalSignsDBcontent calcAvgVal(String usrnm, String AvInt)
	{
       List<VitalSignsDBcontent> samplesAll = VitalSignsMongo.getMeas(usrnm, AvInt);
		
		Date now = new Date ();
    	
    	VitalSignsDBcontent sumsamples = new VitalSignsDBcontent();
    	sumsamples.setUser(usrnm);
    	sumsamples.setAverInt("");
    	sumsamples.setDateTime(now);
    	
    	int sbp =0;
		int dbp =0;
		int hr=0;
		int hrv=0;
		int os = 0;
		
		int sbpCnt=0;
		int dbpCnt=0;
		int hrCnt=0;
		int hrvCnt=0;
		int osCnt=0;
    	
    	for(VitalSignsDBcontent index : samplesAll)
		{
    		if(index.getSbp()>-1)
    		{
    		  sbp+=index.getSbp();
    		  sbpCnt++;
    		}
    		
    		if(index.getDbp()>-1)
    		{
    		 dbp+=index.getDbp();
    		 dbpCnt++;
    		}
    		
    		if(index.getHr()>-1)
    		{
    		  hr+=index.getHr();
    		  hrCnt++;
    		}
    		
    		if(index.getHrv()>-1)
    		{
    		  hrv+=index.getHrv();
    		  hrvCnt++;
    		}
    		
    		if(index.getOs()>-1)
    		{
    		  os+=index.getOs();
    		  osCnt++;
    		}
			
		}
		
    	if(sbpCnt>0)
    	{sumsamples.setSbp(sbp/sbpCnt);}
    	else
    	{sumsamples.setSbp(-1);}
    	
    	if(dbpCnt>0)
    	{sumsamples.setDbp(dbp/dbpCnt);}
    	else
    	{sumsamples.setDbp(-1);}
    	
    	if(hrCnt>0)
    	{sumsamples.setHr(hr/hrCnt);}
    	else
    	{sumsamples.setHr(-1);}
    	
    	if(hrvCnt>0)
    	{sumsamples.setHrv(hrv/hrvCnt);}
    	else
    	{sumsamples.setHrv(-1);}
    	
    	if(osCnt>0)
    	{sumsamples.setOs(os/osCnt);}
    	else
    	{sumsamples.setOs(-1);}
    	
    	return sumsamples;
	}
	
}
