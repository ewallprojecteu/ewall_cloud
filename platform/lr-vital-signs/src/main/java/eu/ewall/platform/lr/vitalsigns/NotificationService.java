package eu.ewall.platform.lr.vitalsigns;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import eu.ewall.platform.commons.datamodel.message.NotificationContentFeedbackMsg;
import eu.ewall.platform.commons.datamodel.message.NotificationContentMsg;
import eu.ewall.platform.lr.vitalsigns.dao.VitalSignsDBcontent;

@Component
public class NotificationService {
	
	@Autowired
	private ProfServInterface ProfServInterface;
	
	@Autowired
	private InterfaceController InterfaceController;
	
	
	/**
	 * Constructs and sends a user notification
	 * 
	 * @param usrnm
	 * 			the user's username
	 * @param source
	 * 			the source of the deviation e.g. hr, hrv, os
	 * @param deviation
	 * 			the type of deviation e.g. slight, chronic
	 * 
	 */
	public void userNotification(String usrnm, String source, String deviation)
	{
		String CountryChar = ProfServInterface.getUsrLang(usrnm);
		
		//String CountryChar ="de";
		
		NotificationContentMsg content = new NotificationContentMsg();
		content.setType("recommendation");
		content.setSubtype("vitalsigns");
		
		switch(CountryChar.toLowerCase())
     	{
     	 case "de": content.setTitle("MITTEILUNG");
     	   break;
 	 	   
     	case "it":  content.setTitle("NOTIFICA");
 	 	   break;
 	 	   
     	case "nl" : content.setTitle("KENNISGEVING");
     	   break;
     	  
     	case "da" : content.setTitle("NOTIFIKATION");
   	  	   break;
   	  	   
     	default   : content.setTitle("NOTIFICATION");
          break;
     	}
		
		content.addFeedback(new NotificationContentFeedbackMsg("button",
				"OK", "","",""));
		
		if("slight".equals(deviation)){	
			if("hr".equals(source)){
				switch(CountryChar.toLowerCase())
		     	{
		     	 case "de": content.setMotivational("Dein Puls ist leicht angestiegen");
		 		            content.setSuggestion("eWALL wird deinen Puls weiterhin überwachen. Wenn notwendig, wird deine Betreuungsperson informiert.");
		     	   break;
		 	 	   
		     	case "it": content.setMotivational("C'è stato un leggero incremento della tua frequenza cardiaca");
		         		   content.setSuggestion("eWALL continuerà a monitorare la tua frequenza cardiaca. Il tuo badante verra informato se richiesto.");
		 	 	   break;
		 	 	   
		     	case "nl" : content.setMotivational("Er is een lichte verhoging van uw hartslag geconstateerd");
				   		    content.setSuggestion("eWALL zal uw hartslag parameters blijven monitoren. Wanneer nodig zullen uw zorgverleners op de hoogte worden gesteld");
		     	   break;
		     	  
		     	case "da" : content.setMotivational("Din pulser steget en anelse"); // should be da or dk ?????????????????????????????????
				            content.setSuggestion("eWALL vil fortsat monitorere din puls. Hvis det er nødvendigt vil dine omsorgspersoner blive kontaktet.");
		   	  	   break;
		   	  	   
		     	default: content.setMotivational("There has been a slight increase of your heart rate");
				         content.setSuggestion("eWALL will continue to monitor your heart rate parameters. If required your caregivers will be informed");
		          break;
		     	}}
			else if("bp".equals(source)){
				switch(CountryChar.toLowerCase())
		     	{
		     	 case "de": content.setMotivational("Dein Blutdruck ist leicht angestiegen");
		 		            content.setSuggestion("eWALL wird deinen Blutdruck weiterhin überwachen. Falls notwendig, wird deine Betreuungsperson informiert.");
		     	   break;
		 	 	   
		     	case "it": content.setMotivational("C'è stato un leggero incremento della tua pressione sanguigna");
		         		   content.setSuggestion("eWALL continuerà a monitorare la tua pressione sanguigna. Il tuo badante verra informato se richiesto.");
		 	 	   break;
		 	 	   
		     	case "nl" : content.setMotivational("Er is een lichte verhoging van uw bloeddruk geconstateerd");
				   		    content.setSuggestion("eWALL zal uw bloeddruk parameters blijven monitoren. Wanneer nodig zullen uw zorgverleners op de hoogte worden gesteld.");
		     	   break;
		     	  
		     	case "da" : content.setMotivational("Dit blodtryk er steget en anelse");
				            content.setSuggestion("eWALL vil fortsat monitorere dit blodtryk. Hvis det er nødvendigt vil dine omsorgspersoner blive kontaktet");
		   	  	   break;
		   	  	   
		     	default: content.setMotivational("There has been a slight increase of your blood preasure");
				         content.setSuggestion("eWALL will continue to monitor your blood pressure parameters. If required your caregivers will be informed");
		          break;
		     	}}
			else if("os".equals(source)){
				switch(CountryChar.toLowerCase())
		     	{
		     	 case "de": content.setMotivational("Deine Sauerstoffsättigung im Blut ist leicht gefallen");
		 		            content.setSuggestion("eWALL wird deine Sauerstoffsättigung weiterhin überwachen. Falls notwendig, wird deine Betreuungsperson informiert.");
		     	   break;
		 	 	   
		     	case "it": content.setMotivational("C'è stato un leggero abbassamento della tua saturazione di ossigeno");
		         		   content.setSuggestion("eWALL continuerà a monitorare la tua saturazione di ossigeno. Il tuo badante verra informato se richiesto.");
		 	 	   break;
		 	 	   
		     	case "nl" : content.setMotivational("Er is een lichte daling van uw bloed zuurstof gehalte geconstateerd");
				   		    content.setSuggestion("eWALL zal uw bloed zuurstof gehalte blijven monitoren. Wanneer nodig zullen uw zorgverleners op de hoogte worden gesteld.");
		     	   break;
		     	  
		     	case "da" : content.setMotivational("Du saturerer lavere end normalt");
				            content.setSuggestion("eWALL vil fortsat monitorere din saturation. Hvis det er nødvendigt vil dine omsorgspersoner blive kontaktet.");
		   	  	   break;
		   	  	   
		     	default: content.setMotivational("There has been a slight decrease of your oxygen saturation");
				         content.setSuggestion("eWALL will continue to monitor your oxygen saturation. If required your caregivers will be informed");
		          break;
		     	}}
		}
		else if("chronic".equals(deviation)){	
			if("hr".equals(source)){
				switch(CountryChar.toLowerCase())
		     	{
		     	 case "de": content.setMotivational("Dein Puls ist chronisch erhöht");
		 		            content.setSuggestion("Deine Betreuungsperson wird informiert.");
		     	   break;
		 	 	   
		     	case "it": content.setMotivational("È stato rilevato un incremento cronico della tua frequenza cardiaca");
		         		   content.setSuggestion("Il tuo caregiver è stato informato");
		 	 	   break;
		 	 	   
		     	case "nl" : content.setMotivational("Er is een chronische verhoging van uw hartslag waarden geconstateerd");
				   		    content.setSuggestion("Uw zorgverleners worden op de hoogte gesteld.");
		     	   break;
		     	  
		     	case "da" : content.setMotivational("En stigning i din puls er opdaget"); // should be da or dk ?????????????????????????????????
				            content.setSuggestion("Din omsorgsperson er informeret");
		   	  	   break;
		   	  	   
		     	default: content.setMotivational("A chronic increase of your heart rate has been detected");
				         content.setSuggestion("Your caregivers are being informed");
		          break;
		     	}}
			else if("bp".equals(source)){
				switch(CountryChar.toLowerCase())
		     	{
		     	 case "de": content.setMotivational("Dein Blutdruck ist chronisch erhöht.");
		 		            content.setSuggestion("Deine Betreuungsperson wird informiert");
		     	   break;
		 	 	   
		     	case "it": content.setMotivational("È stato rilevato un incremento cronico della tua pressione sanguigna");
		         		   content.setSuggestion("Il tuo caregiver è stato informato");
		 	 	   break;
		 	 	   
		     	case "nl" : content.setMotivational("Er is een chronische verhoging van uw bloeddruk geconstateerd");
				   		    content.setSuggestion("Uw zorgverleners worden op de hoogte gesteld");
		     	   break;
		     	  
		     	case "da" : content.setMotivational("En stigning i dit blodtryk er opdaget");
				            content.setSuggestion("Din omsorgsperson er informeret");
		   	  	   break;
		   	  	   
		     	default: content.setMotivational("A chronic increase of your blood pressure has been detected");
				         content.setSuggestion("Your caregivers are being informed");
		          break;
		     	}}
			else if("os".equals(source)){
				switch(CountryChar.toLowerCase())
		     	{
		     	 case "de": content.setMotivational("Deine Sauerstoffsättigung ist chronisch erhöht.");
		 		            content.setSuggestion("Deine Betreuungsperson wird informiert");
		     	   break;
		 	 	   
		     	case "it": content.setMotivational("È stato rilevato un abbassamento cronico della tua saturazione di ossigeno");
		         		   content.setSuggestion("Il tuo caregiver è stato informato");
		 	 	   break;
		 	 	   
		     	case "nl" : content.setMotivational("Er is een chronische daling van uw bloed zuurstof waarden geconstateerd.");
				   		    content.setSuggestion("Uw zorgverleners worden op de hoogte gesteld");
		     	   break;
		     	  
		     	case "da" : content.setMotivational("En stigning i din saturation er opdaget");
				            content.setSuggestion("Din omsorgsperson er informeret");
		   	  	   break;
		   	  	   
		     	default: content.setMotivational("A chronic decrease of your oxygen saturation has been detected");
				         content.setSuggestion("Your caregivers are being informed");
		          break;
		     	}}
		}
		
		
		// send the notification
		try
		{
		  InterfaceController.sendNotificationUser(usrnm, content);
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
	 * Constructs and sends a caregiver notification
	 * 
	 * @param usrnm
	 * 			the user's username
	 * @param source
	 * 			the source of the deviation e.g. hr, hrv, os
	 * @param deviation
	 * 			the type of deviation e.g. slight, chronic
	 * @param samples
	 * 			the vital signs samples
	 * 
	 */
	public void caregiverNotification(String usrnm, String source, String deviation, VitalSignsDBcontent samples)
	{
		
		
		String CountryChar = "";
		
		//String CountryChar ="en";
		
		NotificationContent content = new NotificationContent();
		content.setType("recommendation");
		content.setSubtype("vitalsigns");
		
		content.addFeedback(new NotificationContentFeedback("button",
				"OK", ""));
		
        List<String> crgv = ProfServInterface.getCaregivers(usrnm);
		
		
		for(String caregiverUsername : crgv)
		{
		
			CountryChar = ProfServInterface.getUsrLang(caregiverUsername);
			
			switch(CountryChar.toLowerCase())
	     	{
	     	 case "de": content.setTitle("MITTEILUNG");
	     	   break;
	 	 	   
	     	case "it":  content.setTitle("NOTIFICA");
	 	 	   break;
	 	 	   
	     	case "nl" : content.setTitle("KENNISGEVING");
	     	   break;
	     	  
	     	case "da" : content.setTitle("NOTIFIKATION");
	   	  	   break;
	   	  	   
	     	default   : content.setTitle("NOTIFICATION");
	          break;
	     	}
		
		if("slight".equals(deviation)){	
			if("hr".equals(source)){
				switch(CountryChar.toLowerCase())
		     	{
		     	 case "de": content.setMotivational("eWALL hat erkannt, dass der Puls von "+usrnm+" leicht gestiegen ist");
		 		            content.setSuggestion("");
		     	   break;
		 	 	   
		     	case "it": content.setMotivational("eWALL ha rilevato un lieve incremento della frequenza cardiaca nell'utente "+usrnm);
		         		   content.setSuggestion("");
		 	 	   break;
		 	 	   
		     	case "nl" : content.setMotivational("eWALL heeft een lichte stijging van de hartslag van gebruiker "+usrnm+" geconstateerd");
				   		    content.setSuggestion("");
		     	   break;
		     	  
		     	case "da" : content.setMotivational("eWALL har opdaget en lille stigning i pulsen hos bruger "+usrnm); // should be da or dk ?????????????????????????????????
				            content.setSuggestion("");
		   	  	   break;
		   	  	   
		     	default: content.setMotivational("eWALL has detected a slight increase of the heart rate of user " +usrnm);
				         content.setSuggestion("");
		          break;
		     	}}
			else if("bp".equals(source)){
				switch(CountryChar.toLowerCase())
		     	{
		     	 case "de": content.setMotivational("eWALL hat erkannt, dass der Blutdruck von "+usrnm+" leicht gestiegen ist");
		 		            content.setSuggestion("");
		     	   break;
		 	 	   
		     	case "it": content.setMotivational("eWALL ha rilevato un lieve incremento della pressione sanguigna nell'utente "+ usrnm);
		         		   content.setSuggestion("");
		 	 	   break;
		 	 	   
		     	case "nl" : content.setMotivational("eWALL heeft een lichte stijging van de bloeddruk van gebruiker "+usrnm+" geconstateerd");
				   		    content.setSuggestion("");
		     	   break;
		     	  
		     	case "da" : content.setMotivational("eWALL har opdaget en lille stigning i blodtrykket hos bruger "+usrnm);
				            content.setSuggestion("");
		   	  	   break;
		   	  	   
		     	default: content.setMotivational("eWALL has detected a slight increase of the blood pressure of user "+usrnm);
				         content.setSuggestion("");
		          break;
		     	}}
			else if("os".equals(source)){
				switch(CountryChar.toLowerCase())
		     	{
		     	 case "de": content.setMotivational("eWALL hat erkannt, dass die Sauerstoffsättigung von "+usrnm+" leicht gefallen ist");
		 		            content.setSuggestion("");
		     	   break;
		 	 	   
		     	case "it": content.setMotivational("eWALL ha rilevato un lieve incremento della saturazione di ossigeno nell'utente "+usrnm);
		         		   content.setSuggestion("");
		 	 	   break;
		 	 	   
		     	case "nl" : content.setMotivational("eWALL heeft een lichte daling van de bloed zuurstof waarden van gebruiker "+usrnm+" geconstateerd");
				   		    content.setSuggestion("");
		     	   break;
		     	  
		     	case "da" : content.setMotivational("eWALL har opdaget en lille stigning i saturationen hos bruger "+usrnm);
				            content.setSuggestion("");
		   	  	   break;
		   	  	   
		     	default: content.setMotivational("eWALL has detected a slight decrease of the oxygen saturation of user "+usrnm);
				         content.setSuggestion("");
		          break;
		     	}}
		}
		else if("chronic".equals(deviation)){	
			if("hr".equals(source)){
				switch(CountryChar.toLowerCase())
		     	{
		     	 case "de": content.setMotivational("eWALL hat erkannt, dass der Puls von "+usrnm+" chronisch erhöht ist");
		 		            content.setSuggestion("");
		     	   break;
		 	 	   
		     	case "it": content.setMotivational("eWALL ha rilevato un incremento cronico della frequenza cardiaca nell'utente "+usrnm);
		         		   content.setSuggestion("");
		 	 	   break;
		 	 	   
		     	case "nl" : content.setMotivational("eWALL heeft een chronische stijging van de hartslag van gebruiker "+usrnm+" geconstateerd");
				   		    content.setSuggestion("");
		     	   break;
		     	  
		     	case "da" : content.setMotivational("eWALL har opdaget en lille kronisk stigning i pulsen hos bruger "+usrnm); // should be da or dk ?????????????????????????????????
				            content.setSuggestion("");
		   	  	   break;
		   	  	   
		     	default: content.setMotivational("eWALL has detected a chronic increase of the heart rate of user "+usrnm);
				         content.setSuggestion("");
		          break;
		     	}}
			else if("bp".equals(source)){
				switch(CountryChar.toLowerCase())
		     	{
		     	 case "de": content.setMotivational("eWALL hat erkannt, dass der Blutdruck von "+usrnm+" chronisch erhöht ist");
		 		            content.setSuggestion("");
		     	   break;
		 	 	   
		     	case "it": content.setMotivational("eWALL ha rilevato un incremento cronico della pressione sanguigna nell'utente " +usrnm);
		         		   content.setSuggestion("");
		 	 	   break;
		 	 	   
		     	case "nl" : content.setMotivational("eWALL heeft een chronische stijging van de bloeddruk van gebruiker "+usrnm+" geconstateerd");
				   		    content.setSuggestion("");
		     	   break;
		     	  
		     	case "da" : content.setMotivational("eWALL har opdaget en kronisk stigning af blodtrykket hos bruger "+usrnm);
				            content.setSuggestion("");
		   	  	   break;
		   	  	   
		     	default: content.setMotivational("eWALL has detected a chronic increase of the blood pressure of user "+usrnm);
				         content.setSuggestion("");
		          break;
		     	}}
			else if("os".equals(source)){
				switch(CountryChar.toLowerCase())
		     	{
		     	 case "de": content.setMotivational("eWALL hat erkannt, dass die Sauerstoffsättigung von "+usrnm+" chronisch gesenkt ist");
		 		            content.setSuggestion("");
		     	   break;
		 	 	   
		     	case "it": content.setMotivational("eWALL ha rilevato un abbassamento cronico della saurazione di ossigeno nell'utente "+usrnm);
		         		   content.setSuggestion("");
		 	 	   break;
		 	 	   
		     	case "nl" : content.setMotivational("eWALL heeft een chronische daling van de zuurstof saturatie waarden van gebruiker "+usrnm+" geconstateerd");
				   		    content.setSuggestion("");
		     	   break;
		     	  
		     	case "da" : content.setMotivational("eWALL har opdaget et kronisk fald i saturationen hos bruger "+usrnm);
				            content.setSuggestion("");
		   	  	   break;
		   	  	   
		     	default: content.setMotivational("eWALL has detected a chronic decrease of the oxygen saturation of user "+usrnm);
				         content.setSuggestion("");
		          break;
		     	}}
		    }
	    }
		
		
		// send the notification
		try
		{
		  InterfaceController.sendNotificationCaregiver(usrnm, content);
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
	 * Constructs and sends a user alarm
	 * 
	 * @param usrnm
	 * 			the user's username
	 * @param source
	 * 			the source of the deviation e.g. hr, hrv, os
	 * @param deviation
	 * 			the type of deviation e.g. slight, chronic
	 * 
	 */
	public void userAlalrm(String usrnm, String source, String deviation)
	{
		String CountryChar = ProfServInterface.getUsrLang(usrnm);
		
		//String CountryChar ="da";
		
		//System.out.println("VLSR: USER Alarm");
		
		NotificationContentMsg content = new NotificationContentMsg();
		content.setType("generic");
		content.setSubtype("vitalsigns");
		if("it".equals(CountryChar))
		{
			content.setTitle("ALLARME");
		}
		else
		{
		 content.setTitle("ALARM");
		}
		
		content.addFeedback(new NotificationContentFeedbackMsg("button",
				"OK", "","",""));
		
		if("hr".equals(source)){
			switch(CountryChar.toLowerCase())
	     	{
	     	 case "de": content.setMotivational("Dein Puls ist stark erhöht");
	 		            content.setSuggestion("Bitte stoppe jede körperliche Anstrengung."
	 		            					+ "Deine Betreuungsperson wird informiert.");
	     	   break;
	 	 	   
	     	case "it": content.setMotivational("C'è stato un incremento significativo della tua frequenza cardiaca");
	         		   content.setSuggestion("Per favore astieniti da ogni attività fisica. Il tuo caregiver è stato informato");
	 	 	   break;
	 	 	   
	     	case "nl" : content.setMotivational("Er is een significante stijging in uw hartslag waarden geconstateerd");
			   		    content.setSuggestion("Stopt u alstublieft met uw fysieke activiteit. Uw zorgverleners worden op de hoogte gesteld.");
	     	   break;
	     	  
	     	case "da" : content.setMotivational("Der er en signifikant stigning i din puls"); // should be da or dk ?????????????????????????????????
			            content.setSuggestion("Venligst undlad at være fysisk aktiv. Din omsorgsperson er informeret");
	   	  	   break;
	   	  	   
	     	default: content.setMotivational("There is a significant increase of your heart rate");
			         content.setSuggestion("Please refrain yourself from any physical activity. "
			         						+ "Your caregivers are being informed");
	          break;
	     	}}
		else if("bp".equals(source)){
			switch(CountryChar.toLowerCase())
	     	{
	     	 case "de": content.setMotivational("Dein Blutdruck ist stark erhöht");
	 		            content.setSuggestion("Bitte stoppe jede körperliche Anstrengung. "
	 		            					+ "Deine Betreuungsperson wird informiert.");
	     	   break;
	 	 	   
	     	case "it": content.setMotivational("C'è stato un incremento significativo della tua pressione sanguigna");
	         		   content.setSuggestion("Per favore astieniti da ogni attività fisica. Il tuo caregiver è stato informato");
	 	 	   break;
	 	 	   
	     	case "nl" : content.setMotivational("Er is een significante stijging in bloeddruk waarden geconstateerd");
			   		    content.setSuggestion("Stopt u alstublieft met uw fysieke activiteit. Uw zorgverleners worden op de hoogte gesteld.");
	     	   break;
	     	  
	     	case "da" : content.setMotivational("Der er en signifikant stigning i dit blodtryk");
			            content.setSuggestion("Venligst undlad at være fysisk aktiv. Din omsorgsperson er informeret");
	   	  	   break;
	   	  	   
	     	default: content.setMotivational("There is a significant increase of your blood pressure");
			         content.setSuggestion("Please refrain yourself from any physical activity. "
			         						+ "Your caregivers are being informed");
	          break;
	     	}}
		else if("os".equals(source)){
			switch(CountryChar.toLowerCase())
	     	{
	     	 case "de": content.setMotivational("Deine Sauerstoffsättigung ist stark gefallen");
	 		            content.setSuggestion("Bitte stoppe jede körperliche Anstrengung. "
	 		            					+ "Deine Betreuungsperson wird informiert.");
	     	   break;
	 	 	   
	     	case "it": content.setMotivational("C'è stato un abbassamento significativo della tua saturazione di ossigeno");
	         		   content.setSuggestion("Per favore astieniti da ogni attività fisica. Il tuo caregiver è stato informato");
	 	 	   break;
	 	 	   
	     	case "nl" : content.setMotivational("Er is een significante daling van uw bloed zuurstof gehalte geconstateerd");
			   		    content.setSuggestion("Stopt u alstublieft met uw fysieke activiteit. Uw zorgverleners worden op de hoogte gesteld.");
	     	   break;
	     	  
	     	case "da" : content.setMotivational("Der er en signifikant stigning i din saturation");
			            content.setSuggestion("Venligst undlad at være fysisk aktiv. Din omsorgsperson er informeret");
	   	  	   break;
	   	  	   
	     	default: content.setMotivational("There is a significant decrease of your oxygen saturation");
			         content.setSuggestion("Please refrain yourself from any physical activity. "
			         						+ "Your caregivers are being informed");
	          break;
	     	}}
		
		
		// send the alarm
		try
		{
		  InterfaceController.sendNotificationAlarmUser(usrnm, content);
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
	 * Constructs and sends a caregiver alarm
	 * 
	 * @param usrnm
	 * 			the user's username
	 * @param source
	 * 			the source of the deviation e.g. hr, hrv, os
	 * @param deviation
	 * 			the type of deviation e.g. slight, chronic
	 * @param samples
	 * 			the vital signs samples
	 * 
	 */
	public void caregiverAlarm(String usrnm, String source, String deviation, VitalSignsDBcontent samples)
	{
       String CountryChar = "";
		
		NotificationContent content = new NotificationContent();
		content.setType("generic");
		content.setSubtype("vitalsigns");
		content.setTitle("ALARM");
		
		content.addFeedback(new NotificationContentFeedback("button",
				"OK", ""));
		
		List<String> crgv = ProfServInterface.getCaregivers(usrnm);
		
		
		for(String caregiverUsername : crgv)
		{
		
			CountryChar = ProfServInterface.getUsrLang(caregiverUsername);
			
			if("it".equals(CountryChar))
				content.setTitle("ALLARME");
			
		 if("hr".equals(source)){
			switch(CountryChar.toLowerCase())
	     	{
	     	 case "de": content.setMotivational("eWALL hat erkannt, dass der Puls von " +usrnm+ " stark gestiegen ist");
	 		            content.setSuggestion("");
	     	   break;
	 	 	   
	     	case "it": content.setMotivational("eWALL ha rilevato un incremento significativo della frequenza cardiaca nell'utente "+usrnm);
	         		   content.setSuggestion("");
	 	 	   break;
	 	 	   
	     	case "nl" : content.setMotivational("eWALL heeft een significante stijging van de hartslag van gebruiker "+usrnm+" geconstateerd");
			   		    content.setSuggestion("");
	     	   break;
	     	  
	     	case "da" : content.setMotivational("eWALL har opdaget en signifikant stigning ved pulsen hos bruger "+usrnm);
			            content.setSuggestion("");
	   	  	   break;
	   	  	   
	     	default: content.setMotivational("eWALL has detected a significant increase of the heart rate of user " + usrnm);
			         content.setSuggestion("");
	          break;
	     	}}
		else if("bp".equals(source)){
			switch(CountryChar.toLowerCase())
	     	{
	     	 case "de": content.setMotivational("eWALL hat erkannt, dass der Blutdruck von " +usrnm+ " stark gestiegen ist");
	 		            content.setSuggestion("");
	     	   break;
	 	 	   
	     	case "it": content.setMotivational("eWALL ha rilevato un incremento significativo della pressione sanguigna nell'utente "+usrnm);
	         		   content.setSuggestion("");
	 	 	   break;
	 	 	   
	     	case "nl" : content.setMotivational("eWALL heeft een significante stijging van de bloeddruk van gebruiker " +usrnm+ " geconstateerd");
			   		    content.setSuggestion("");
	     	   break;
	     	  
	     	case "da" : content.setMotivational("eWALL har opdaget en signifikant stigning ved blodtrykket hos bruger "+usrnm);
			            content.setSuggestion("");
	   	  	   break;
	   	  	   
	     	default: content.setMotivational("eWALL has detected a significant increase of the blood pressure of user "+usrnm);
			         content.setSuggestion("");
	          break;
	     	}}
		else if("os".equals(source)){
			switch(CountryChar.toLowerCase())
	     	{
	     	 case "de": content.setMotivational("eWALL hat erkannt, dass die Sauerstoffsättigung von "+usrnm+" stark gefallen ist");
	 		            content.setSuggestion("");
	     	   break;
	 	 	   
	     	case "it": content.setMotivational("eWALL ha rilevato un abbassameto significativo della saturazione di ossigeno nell'utente "+usrnm);
	         		   content.setSuggestion("");
	 	 	   break;
	 	 	   
	     	case "nl" : content.setMotivational("eWALL heeft een significante daling van de zuurstof saturatie van gebruiker "+usrnm+" geconstateerd");
			   		    content.setSuggestion("");
	     	   break;
	     	  
	     	case "da" : content.setMotivational("eWALL har opdaget et signifikant fald i saturationen hos bruger "+usrnm);
			            content.setSuggestion("");
	   	  	   break;
	   	  	   
	     	default: content.setMotivational("eWALL has detected a significant decrease of the oxygen saturation of user "+usrnm);
			         content.setSuggestion("");
	          break;
	     	}}
		
		try
		{
		  InterfaceController.sendNotificationAlarmCaregiver(usrnm, content);
		}
		catch (RestClientException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}catch(Exception e){
			System.out.println("Caregiver Alarm error while sending data: " + e.getMessage() + " " + e.toString());
		}
	  }
	}

}
