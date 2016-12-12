package eu.ewall.servicebrick.domotics.controller;

import java.util.ArrayList;
import java.util.List;

import java.lang.Double;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.ewall.servicebrick.common.validation.ServiceBrickInputValidator;
import eu.ewall.servicebrick.common.controller.SensorEventController;
import eu.ewall.servicebrick.common.dao.SensorEventDao;
import eu.ewall.servicebrick.domotics.model.HumidityEventDomotics;
import eu.ewall.servicebrick.domotics.model.HumidityHistoryDomotics;
import eu.ewall.servicebrick.domotics.model.IlluminanceEventDomotics;
import eu.ewall.servicebrick.domotics.model.IlluminanceHistoryDomotics;
import eu.ewall.servicebrick.domotics.model.TemperatureEventDomotics;
import eu.ewall.servicebrick.domotics.model.TemperatureHistoryDomotics;
import eu.ewall.servicebrick.domotics.model.DomoticsEvent;

@RestController
public class DomoticsController extends SensorEventController {
	

	@Autowired
	public DomoticsController(ServiceBrickInputValidator inputValidator, SensorEventDao sensorEventDao) {
		super(inputValidator, sensorEventDao);
			
	}

	@RequestMapping(value = "/v1/{username}/domotics", method = RequestMethod.GET)
	public ArrayList<DomoticsEvent> getDomotics(@PathVariable String username) {
		DateTime timeTemperature = new DateTime();
		DateTime timeHumidity= new DateTime();
		DateTime timeIlluminance= new DateTime();
		
               
		ArrayList<DomoticsEvent> domoticsRooms = new ArrayList<DomoticsEvent>();

		LocalDate currentDate = new LocalDate();
					
		Integer today_flag = 0;
		String location = "livingroom";
		
		DomoticsEvent domoticsLivingroom = new DomoticsEvent();
		
		domoticsLivingroom.setUsername(username);
		domoticsLivingroom.setLocation(location);
		
		HumidityHistoryDomotics hHD = getEventsDomotics("getHumidityDomotics", HumidityEventDomotics.class,
				HumidityHistoryDomotics.class, username, location, null,null ,"h", 1);
		
	    if(hHD.getHumidityEvents().size()>0)
	    {
		timeHumidity = new DateTime(hHD.getHumidityEvents().get(0).getFrom());
		//log.info(" t: " + timeHumidity.toString());
		if (currentDate.equals(timeHumidity.toLocalDate())) {
			today_flag++;
			
			domoticsLivingroom.setHumidity(hHD.getHumidityEvents().get(0).getHumidity());
		} else
			domoticsLivingroom.setHumidity(0);
	    }
	    else
			domoticsLivingroom.setHumidity(0);
				
	    TemperatureHistoryDomotics tHD = getEventsDomotics("getTemperatureDomotics", TemperatureEventDomotics.class,
				TemperatureHistoryDomotics.class, username, location, null, null,"h", 1);
	    
				if(tHD.getTemperatureEvents().size()>0)
			{
			timeTemperature = new DateTime(tHD.getTemperatureEvents().get(0).getFrom());
			
			if (currentDate.equals(timeTemperature.toLocalDate())) {
				today_flag++;
				domoticsLivingroom.setTemperature(tHD.getTemperatureEvents().get(0).getTemperature());
			} else
				domoticsLivingroom.setTemperature(0);
			}
			else
			domoticsLivingroom.setTemperature(0);
		
			IlluminanceHistoryDomotics iHD = getEventsDomotics("getIlluminanceDomotics", IlluminanceEventDomotics.class,
					IlluminanceHistoryDomotics.class, username, location, null, null,"h", 1);
		
			 if(iHD.getIlluminanceEvents().size()>0)
			 {
				timeIlluminance = new DateTime(iHD.getIlluminanceEvents().get(0).getFrom());
				
				if (currentDate.equals(timeIlluminance.toLocalDate())) {
					today_flag++;
					domoticsLivingroom.setIlluminance(iHD.getIlluminanceEvents().get(0).getIlluminance());
				} else
					domoticsLivingroom.setIlluminance(0);
				
				
			 }
			 else
				domoticsLivingroom.setIlluminance(0);	 
		
		if(today_flag!=0)
		{
		if(timeHumidity.isAfter(timeIlluminance)&&timeHumidity.isAfter(timeTemperature))
			domoticsLivingroom.setTimestamp(timeHumidity);
		else
		{
			if(timeIlluminance.isAfter(timeHumidity)&&timeIlluminance.isAfter(timeTemperature))
				domoticsLivingroom.setTimestamp(timeIlluminance);
			else
				if(timeTemperature.isAfter(timeHumidity)&&timeTemperature.isAfter(timeIlluminance))
					domoticsLivingroom.setTimestamp(timeTemperature);	
				else
					domoticsLivingroom.setTimestamp(timeIlluminance);
		}
		domoticsRooms.add(domoticsLivingroom);

		}
		
		DomoticsEvent domoticsKitchen = new DomoticsEvent();
		location = "kitchen";
		domoticsKitchen.setUsername(username);
		domoticsKitchen.setLocation(location);
		today_flag=0;
		timeTemperature = new DateTime();
		timeHumidity= new DateTime();
		timeIlluminance= new DateTime();
		
		hHD = getEventsDomotics("getHumidityDomotics", HumidityEventDomotics.class,
				HumidityHistoryDomotics.class, username, location, null,null ,"h", 1);
			
			
			
		   if(hHD.getHumidityEvents().size()>0)
		    {
			   timeHumidity = new DateTime(hHD.getHumidityEvents().get(0).getFrom());

			
			if (currentDate.equals(timeHumidity.toLocalDate())) {
				today_flag++;
				domoticsKitchen.setHumidity(hHD.getHumidityEvents().get(0).getHumidity());
			} else
				domoticsKitchen.setHumidity(0);
		    }		
		   else
			   
		   	domoticsKitchen.setHumidity(0);
		   	
		   tHD = getEventsDomotics("getTemperatureDomotics", TemperatureEventDomotics.class,
					TemperatureHistoryDomotics.class, username, location, null, null,"h", 1);
				
			
			    if(tHD.getTemperatureEvents().size()>0)
				{
			    	timeTemperature = new DateTime(tHD.getTemperatureEvents().get(0).getFrom());
				
				if (currentDate.equals(timeTemperature.toLocalDate())) {
					today_flag++;
					domoticsKitchen.setTemperature(tHD.getTemperatureEvents().get(0).getTemperature());
				} else
					domoticsKitchen.setTemperature(0);
				}
			    else
			    domoticsKitchen.setTemperature(0);
			    iHD = getEventsDomotics("getIlluminanceDomotics", IlluminanceEventDomotics.class,
						IlluminanceHistoryDomotics.class, username, location, null, null,"h", 1);
					
				
				 if(iHD.getIlluminanceEvents().size()>0)
				 {
					 timeIlluminance = new DateTime(iHD.getIlluminanceEvents().get(0).getFrom());
					
					if (currentDate.equals(timeIlluminance.toLocalDate())) {
						today_flag++;
						domoticsKitchen.setIlluminance(iHD.getIlluminanceEvents().get(0).getIlluminance());
					} else
						domoticsKitchen.setIlluminance(0);
					
				 }
				 else
				 domoticsKitchen.setIlluminance(0);
				 
		
				 if(today_flag!=0)
					{
					if(timeHumidity.isAfter(timeIlluminance)&&timeHumidity.isAfter(timeTemperature))
						domoticsKitchen.setTimestamp(timeHumidity);
					else
					{
						if(timeIlluminance.isAfter(timeHumidity)&&timeIlluminance.isAfter(timeTemperature))
							domoticsKitchen.setTimestamp(timeIlluminance);
						else
							if(timeTemperature.isAfter(timeHumidity)&&timeTemperature.isAfter(timeIlluminance))
								domoticsKitchen.setTimestamp(timeTemperature);	
							else
								domoticsKitchen.setTimestamp(timeIlluminance);
					}
		domoticsRooms.add(domoticsKitchen);
					}
		DomoticsEvent domoticsBedroom = new DomoticsEvent();
		location = "bedroom";
		domoticsBedroom.setUsername(username);
		domoticsBedroom.setLocation(location);
		today_flag=0;
		timeTemperature = new DateTime();
		timeHumidity= new DateTime();
		timeIlluminance= new DateTime();
		
		hHD = getEventsDomotics("getHumidityDomotics", HumidityEventDomotics.class,
				HumidityHistoryDomotics.class, username, location, null,null ,"h", 1);
			
			
		 if(hHD.getHumidityEvents().size()>0)
		    {
			 timeHumidity = new DateTime(hHD.getHumidityEvents().get(0).getFrom());

			
			if (currentDate.equals(timeHumidity.toLocalDate())) {
				today_flag++;
				domoticsBedroom.setHumidity(hHD.getHumidityEvents().get(0).getHumidity());
			} else
				domoticsBedroom.setHumidity(0);
		    }
		 else
		 domoticsBedroom.setHumidity(0);		
		 tHD = getEventsDomotics("getTemperatureDomotics", TemperatureEventDomotics.class,
					TemperatureHistoryDomotics.class, username, location, null, null,"h", 1);
				
				
			    if(tHD.getTemperatureEvents().size()>0)
				{
			    	timeTemperature = new DateTime(tHD.getTemperatureEvents().get(0).getFrom());
				
				if (currentDate.equals(timeTemperature.toLocalDate())) {
					today_flag++;
					domoticsBedroom.setTemperature(tHD.getTemperatureEvents().get(0).getTemperature());
				} else
					domoticsBedroom.setTemperature(0);
				}
			    else
			    domoticsBedroom.setTemperature(0);
			    
			    iHD = getEventsDomotics("getIlluminanceDomotics", IlluminanceEventDomotics.class,
						IlluminanceHistoryDomotics.class, username, location, null, null,"h", 1);
					
				
				 if(iHD.getIlluminanceEvents().size()>0)
				 {
					 timeIlluminance = new DateTime(iHD.getIlluminanceEvents().get(0).getFrom());
					
					if (currentDate.equals(timeIlluminance.toLocalDate())) {
						today_flag++;
						domoticsBedroom.setIlluminance(iHD.getIlluminanceEvents().get(0).getIlluminance());
					} else
						domoticsBedroom.setIlluminance(0);
					
				 }
				 else
				 domoticsBedroom.setIlluminance(0);
		
				 if(today_flag!=0)
					{
					if(timeHumidity.isAfter(timeIlluminance)&&timeHumidity.isAfter(timeTemperature))
						domoticsBedroom.setTimestamp(timeHumidity);
					else
					{
						if(timeIlluminance.isAfter(timeHumidity)&&timeIlluminance.isAfter(timeTemperature))
							domoticsBedroom.setTimestamp(timeIlluminance);
						else
							if(timeTemperature.isAfter(timeHumidity)&&timeTemperature.isAfter(timeIlluminance))
								domoticsBedroom.setTimestamp(timeTemperature);
							else
								domoticsBedroom.setTimestamp(timeIlluminance);
					}
		domoticsRooms.add(domoticsBedroom);
					}
				 
		DomoticsEvent domoticsBathroom = new DomoticsEvent();
		location = "bathroom";
		domoticsBathroom.setUsername(username);
		domoticsBathroom.setLocation(location);
		today_flag=0;
		timeTemperature = new DateTime();
		timeHumidity= new DateTime();
		timeIlluminance= new DateTime();
		
		
			 hHD = getEventsDomotics("getHumidityDomotics", HumidityEventDomotics.class,
						HumidityHistoryDomotics.class, username, location, null,null ,"h", 1);
		
		
			 if(hHD.getHumidityEvents().size()>0)
			 {
		timeHumidity = new DateTime(hHD.getHumidityEvents().get(0).getFrom());

		
		if (currentDate.equals(timeHumidity.toLocalDate())) {
			today_flag++;
				domoticsBathroom.setHumidity(hHD.getHumidityEvents().get(0).getHumidity());
				} else
					domoticsBathroom.setHumidity(0);
		    }
		 else
		 domoticsBathroom.setHumidity(0);
					
		 tHD = getEventsDomotics("getTemperatureDomotics", TemperatureEventDomotics.class,
					TemperatureHistoryDomotics.class, username, location, null, null,"h", 1);
			
		    if(tHD.getTemperatureEvents().size()>0)
			{
		    	timeTemperature = new DateTime(tHD.getTemperatureEvents().get(0).getFrom());
			
			if (currentDate.equals(timeTemperature.toLocalDate())) {
				today_flag++;
				domoticsBathroom.setTemperature(tHD.getTemperatureEvents().get(0).getTemperature());
			} else
				domoticsBathroom.setTemperature(0);
			}
		    else
		    domoticsBathroom.setTemperature(0);
		    iHD = getEventsDomotics("getIlluminanceDomotics", IlluminanceEventDomotics.class,
					IlluminanceHistoryDomotics.class, username, location, null, null,"h", 1);
				
			
			 if(iHD.getIlluminanceEvents().size()>0)
			 {
				 timeIlluminance = new DateTime(iHD.getIlluminanceEvents().get(0).getFrom());
				
				if (currentDate.equals(timeIlluminance.toLocalDate())) {
					today_flag++;
					domoticsBathroom.setIlluminance(iHD.getIlluminanceEvents().get(0).getIlluminance());
				} else
					domoticsBathroom.setIlluminance(0);
				
			 }
			 else
			 domoticsBathroom.setIlluminance(0);
		
			 if(today_flag!=0)
				{
				if(timeHumidity.isAfter(timeIlluminance)&&timeHumidity.isAfter(timeTemperature))
					domoticsBathroom.setTimestamp(timeHumidity);
				else
				{
					if(timeIlluminance.isAfter(timeHumidity)&&timeIlluminance.isAfter(timeTemperature))
						domoticsBathroom.setTimestamp(timeIlluminance);
					else
						if(timeTemperature.isAfter(timeHumidity)&&timeTemperature.isAfter(timeIlluminance))
							domoticsBathroom.setTimestamp(timeTemperature);	
						else
							domoticsBathroom.setTimestamp(timeIlluminance);
				}
		domoticsRooms.add(domoticsBathroom);
				}
		return domoticsRooms;
	}

	@RequestMapping(value = "/v1/{username}/roomhumidity", method = RequestMethod.GET)
	public HumidityHistoryDomotics getHumidity(@PathVariable String username,
			@RequestParam(value = "location", required = false) String location,
			@RequestParam(value = "from", required = false) DateTime from,
			@RequestParam(value = "to", required = false) DateTime to,
			@RequestParam(value = "latestevents", required = false) Integer latestEvents) {
		
		
		List<HumidityEventDomotics> humidityEvents = new ArrayList<HumidityEventDomotics>();
		HumidityHistoryDomotics humidityHistoryDomotics = new HumidityHistoryDomotics(username, location, from, to,
				humidityEvents);

		HumidityHistoryDomotics humHistoryDomotics = new HumidityHistoryDomotics(username, location, from, to,
				humidityEvents);

		
		for(int room=0;room<4;room++)
		{
			if(room==0)
				location="livingroom";
			if(room==1)
				location="kitchen";
			if(room==2)
				location="bedroom";
			if(room==3)
				location="bathroom";
	
			
		Days days = Days.daysBetween(from, to);
		String aggregation;

		if (days.isGreaterThan(Days.TWO)) {
			
			aggregation="d";
		} else
		{
			aggregation="h";
		}
			
		

				humidityHistoryDomotics = getEventsDomotics("getHumidityDomotics", HumidityEventDomotics.class,
						HumidityHistoryDomotics.class, username, location, from, to,aggregation, latestEvents);
				
				
				Integer SizeofList = humidityHistoryDomotics.getHumidityEvents().size();

				double tmp_humidity;

				for (int i = 0; i < SizeofList; i++) {
					HumidityEventDomotics humidityEvent = new HumidityEventDomotics();
					tmp_humidity =humidityHistoryDomotics.getHumidityEvents().get(i).getHumidity();
					if(Double.isNaN(tmp_humidity))
					{
						tmp_humidity=0;
						humidityHistoryDomotics.getHumidityEvents().get(i).setHumidity(tmp_humidity);
					}
				
					humidityEvent.setLocation(location);
					humidityEvent.setFrom(humidityHistoryDomotics.getHumidityEvents().get(i).getFrom());
					humidityEvent.setTo(humidityHistoryDomotics.getHumidityEvents().get(i).getTo());

					humidityEvent.setHumidity(tmp_humidity);
					humidityEvents.add(humidityEvent);

				}
				
		}
		humHistoryDomotics.setHumidityEvents(humidityEvents);

		return humHistoryDomotics;
	}

	@RequestMapping(value = "/v1/{username}/roomtemperature", method = RequestMethod.GET)
	public TemperatureHistoryDomotics getTemperature(@PathVariable String username,
			@RequestParam(value = "location", required = false) String location,
			@RequestParam(value = "from", required = false) DateTime from,
			@RequestParam(value = "to", required = false) DateTime to,
			@RequestParam(value = "latestevents", required = false) Integer latestEvents) {

		
		
		List<TemperatureEventDomotics> temperatureEvents = new ArrayList<TemperatureEventDomotics>();
		TemperatureHistoryDomotics temperatureHistoryDomotics = new TemperatureHistoryDomotics(username, location, from,
				to, temperatureEvents);
		TemperatureHistoryDomotics tempHistoryDomotics = new TemperatureHistoryDomotics(username, location, from,
				to, temperatureEvents); 
		
		for(int room=0;room<4;room++)
		{
			if(room==0)
				location="livingroom";
			if(room==1)
				location="kitchen";
			if(room==2)
				location="bedroom";
			if(room==3)
				location="bathroom";
	

		Days days = Days.daysBetween(from, to);
		
		String aggregation;

		if (days.isGreaterThan(Days.TWO)) {
			
			aggregation="d";
		} else
		{
			aggregation="h";
		}
			
		

				temperatureHistoryDomotics = getEventsDomotics("getTemperatureDomotics", TemperatureEventDomotics.class,
						TemperatureHistoryDomotics.class, username, location, from, to,aggregation, latestEvents);
				
				Integer SizeofList = temperatureHistoryDomotics.getTemperatureEvents().size();

				double tmp_temperature;

				for (int i = 0; i < SizeofList; i++) {
					TemperatureEventDomotics temperatureEvent = new TemperatureEventDomotics();
					tmp_temperature =temperatureHistoryDomotics.getTemperatureEvents().get(i).getTemperature();
					if(Double.isNaN(tmp_temperature))
					{
						tmp_temperature=0;
						temperatureHistoryDomotics.getTemperatureEvents().get(i).setTemperature(tmp_temperature);
					}
				
					temperatureEvent.setLocation(location);
					temperatureEvent.setFrom(temperatureHistoryDomotics.getTemperatureEvents().get(i).getFrom());
					temperatureEvent.setTo(temperatureHistoryDomotics.getTemperatureEvents().get(i).getTo());

					temperatureEvent.setTemperature(tmp_temperature);
					temperatureEvents.add(temperatureEvent);

				}
				
		}
		
		tempHistoryDomotics.setTemperatureEvents(temperatureEvents);
		return tempHistoryDomotics;
	}

	@RequestMapping(value = "/v1/{username}/roomilluminance", method = RequestMethod.GET)
	public IlluminanceHistoryDomotics getIlluminance(@PathVariable String username,
			@RequestParam(value = "location", required = false) String location,
			@RequestParam(value = "from", required = false) DateTime from,
			@RequestParam(value = "to", required = false) DateTime to,
			@RequestParam(value = "latestevents", required = false) Integer latestEvents) {

		
		List<IlluminanceEventDomotics> illuminanceEvents = new ArrayList<IlluminanceEventDomotics>();
		IlluminanceHistoryDomotics illuminanceHistoryDomotics = new IlluminanceHistoryDomotics(username, location, from,
				to, illuminanceEvents);
		IlluminanceHistoryDomotics illHistoryDomotics = new IlluminanceHistoryDomotics(username, location, from,
				to, illuminanceEvents);
		
	
		for(int room=0;room<4;room++)
		{
			if(room==0)
				location="livingroom";
			if(room==1)
				location="kitchen";
			if(room==2)
				location="bedroom";
			if(room==3)
				location="bathroom";
		

		Days days = Days.daysBetween(from, to);
		String aggregation;

		if (days.isGreaterThan(Days.TWO)) {
			
			aggregation="d";
		} else
		{
			aggregation="h";
		}
			
		

				illuminanceHistoryDomotics = getEventsDomotics("getIlluminanceDomotics", IlluminanceEventDomotics.class,
						IlluminanceHistoryDomotics.class, username, location, from, to,aggregation, latestEvents);
				
				Integer SizeofList = illuminanceHistoryDomotics.getIlluminanceEvents().size();

				double tmp_illuminance;

				for (int i = 0; i < SizeofList; i++) {
					IlluminanceEventDomotics illuminanceEvent = new IlluminanceEventDomotics();
					tmp_illuminance =illuminanceHistoryDomotics.getIlluminanceEvents().get(i).getIlluminance();
					if(Double.isNaN(tmp_illuminance))
					{
						tmp_illuminance=0;
						illuminanceHistoryDomotics.getIlluminanceEvents().get(i).setIlluminance(tmp_illuminance);
					}
				
					illuminanceEvent.setLocation(location);
					illuminanceEvent.setFrom(illuminanceHistoryDomotics.getIlluminanceEvents().get(i).getFrom());
					illuminanceEvent.setTo(illuminanceHistoryDomotics.getIlluminanceEvents().get(i).getTo());

					illuminanceEvent.setIlluminance(tmp_illuminance);
					illuminanceEvents.add(illuminanceEvent);

				}
				
		}
		illHistoryDomotics.setIlluminanceEvents(illuminanceEvents);
		return illHistoryDomotics;
	}
}
