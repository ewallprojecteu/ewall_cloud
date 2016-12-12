package eu.ewall.servicebrick.domotics.processor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;


import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import eu.ewall.platform.commons.datamodel.measure.TemperatureMeasurement;
import eu.ewall.platform.commons.datamodel.measure.HumidityMeasurement;
import eu.ewall.platform.commons.datamodel.measure.IlluminanceMeasurement;
import eu.ewall.platform.commons.datamodel.profile.User;

import eu.ewall.servicebrick.domotics.model.TemperatureEventDomotics;
import eu.ewall.servicebrick.domotics.model.HumidityEventDomotics;
import eu.ewall.servicebrick.domotics.model.IlluminanceEventDomotics;

import eu.ewall.servicebrick.common.model.DomoticsUpdates;


@Component
public class DomoticsAverage {
	private static final Logger log = LoggerFactory.getLogger(DomoticsAverage.class);
	
	/**
	 * Aggregates a set of values by hour and day. 
	 */
	public TemperatureEventDomotics[] aggregateByHourT(TemperatureMeasurement[] tempMeasurements, User primaryUser, String room, DomoticsUpdates domoticsUpdates) {
		ArrayList<TemperatureEventDomotics> temperatures = new ArrayList<TemperatureEventDomotics>();
		
		TemperatureMeasurement[] temperatureMeasurements = new TemperatureMeasurement[tempMeasurements.length];
			
		    
		if(tempMeasurements.length == 0){
			return temperatures.toArray(new TemperatureEventDomotics[temperatures.size()]);
		}
		
		long time =0;
		double dult=0;
		
		if(room == "livingroom")
		{
			time = domoticsUpdates.getLastTemperatureUpdateTimestampLivingroom().getMillis();
			dult = domoticsUpdates.getLastTemperatureLivingroom();
		}
		
		if(room == "kitchen")
		{
			time = domoticsUpdates.getLastTemperatureUpdateTimestampKitchen().getMillis();
			dult = domoticsUpdates.getLastTemperatureKitchen();
			
		}
		
		if(room == "bedroom")
		{
			time = domoticsUpdates.getLastTemperatureUpdateTimestampBedroom().getMillis();
			dult = domoticsUpdates.getLastTemperatureBedroom();
			
		}
		
		if(room == "bathroom")
		{
			time = domoticsUpdates.getLastTemperatureUpdateTimestampBathroom().getMillis();
			dult = domoticsUpdates.getLastTemperatureBathroom();
			
		}
	
		if(tempMeasurements.length > 0){
			
			time=time+3600000;
		
			TimeZone tz = TimeZone.getTimeZone("Europe/London");
			Calendar cRefStart = Calendar.getInstance(tz);
			cRefStart.setTimeInMillis(time);
			cRefStart.set(Calendar.MINUTE, 0);
			cRefStart.set(Calendar.SECOND, 0);
			cRefStart.set(Calendar.MILLISECOND, 0);
		
			time=time-3600000;
			double tmp = (tempMeasurements[0].getTimestamp()-cRefStart.getTimeInMillis())/3600000;
			int cnt = (int) tmp;
		
			if(cnt!=0)
			{
			   //cnt--;
				temperatureMeasurements = new TemperatureMeasurement[tempMeasurements.length + cnt];
				System.arraycopy(tempMeasurements, 0, temperatureMeasurements, cnt, tempMeasurements.length);
				
				TemperatureMeasurement temp_Measurement = new TemperatureMeasurement();
				time=time+3600000;
				int i = 0;
				while(cnt!=0)
				{
					temp_Measurement = new TemperatureMeasurement();
					temp_Measurement.setIndoorPlaceName(room);
					temp_Measurement.setMeasuredValueInCelsius(dult);
					temp_Measurement.setMeasuredValue(String.valueOf(dult));
					temp_Measurement.setTimestamp(time);
		
				temperatureMeasurements[i]= temp_Measurement;
				time=time+3600000;
				i++;
				cnt--;
				
				}
			}
			else
			{
				temperatureMeasurements = new TemperatureMeasurement[tempMeasurements.length];
				System.arraycopy(tempMeasurements, 0, temperatureMeasurements, 0, tempMeasurements.length);
			}
			
		}
		
		
		
		
		//First item, to take the reference hour
		TemperatureMeasurement temperatureMeasurement = temperatureMeasurements[0];
		
		TimeZone tz = TimeZone.getTimeZone("Europe/Rome");
		try {
			String timezoneId = primaryUser.getUserProfile().getvCardSubProfile().getTimezoneid();
			tz = TimeZone.getTimeZone(timezoneId);
		} catch (Exception e) {
			log.error("Error in getting timezoneId from profile, using default ('Europe/Rome'");
		}
		
		Calendar cRefStart = Calendar.getInstance(tz);
		cRefStart.setTimeInMillis(temperatureMeasurement.getTimestamp());
		cRefStart.set(Calendar.MINUTE, 0);
		cRefStart.set(Calendar.SECOND, 0);
		cRefStart.set(Calendar.MILLISECOND, 0);

		Calendar cRefEnd = (Calendar)cRefStart.clone();
		cRefEnd.add(Calendar.HOUR_OF_DAY, 1);
		TemperatureEventDomotics temperature = new TemperatureEventDomotics(
				new DateTime(cRefStart.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
				new DateTime(cRefEnd.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
				room
				);
		temperature.setUsername(primaryUser.getUsername());
		temperature.setAggregation("h");
		temperature.setTemperature(temperatureMeasurements[0].getMeasuredValueInCelsius());
		temperatures.add(temperature);
		
		
	 

		int currentHour = cRefStart.get(Calendar.HOUR_OF_DAY);
		
		for(TemperatureMeasurement measurement : temperatureMeasurements){
			Calendar c1 = Calendar.getInstance(tz);
			//log.info("  "+measurement.getTimestamp());
			c1.setTimeInMillis(measurement.getTimestamp());
			c1.set(Calendar.MINUTE, 0);
			c1.set(Calendar.SECOND, 0);
			c1.set(Calendar.MILLISECOND, 0);

			Calendar c2 = (Calendar)c1.clone();
			c2.add(Calendar.HOUR_OF_DAY, 1);
			if(c1.get(Calendar.HOUR_OF_DAY) != currentHour){
				temperature = new TemperatureEventDomotics(
						new DateTime(c1.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
						new DateTime(c2.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
						room
					);
				temperature.setUsername(primaryUser.getUsername());
				temperature.setAggregation("h");
				temperature.setTemperature(measurement.getMeasuredValueInCelsius());
				currentHour = c1.get(Calendar.HOUR_OF_DAY);
				temperatures.add(temperature);
		
				
			}
		
			double newTemp = measurement.getMeasuredValueInCelsius();
			temperature.addTemperature(newTemp);
			
		
			
		}
		return temperatures.toArray(new TemperatureEventDomotics[temperatures.size()]);
	}
	
	
	
	public TemperatureEventDomotics[] aggregateByDayT(TemperatureEventDomotics[] aggregatedByHour, User primaryUser,String room) {
		ArrayList<TemperatureEventDomotics> temperatures = new ArrayList<TemperatureEventDomotics>();
		if(aggregatedByHour.length == 0){
			return temperatures.toArray(new TemperatureEventDomotics[temperatures.size()]);
		}
		
		//First item, to take the reference day
		TemperatureEventDomotics temperatureMeasurement = aggregatedByHour[0];
		
		TimeZone tz = TimeZone.getTimeZone("Europe/Rome");
		try {
			String timezoneId = primaryUser.getUserProfile().getvCardSubProfile().getTimezoneid();
			tz = TimeZone.getTimeZone(timezoneId);
		} catch (Exception e) {
			log.error("Error in getting timezoneId from profile, using default ('Europe/Rome'");
		}
		Calendar cRefStart = Calendar.getInstance(tz);
		cRefStart.setTimeInMillis(temperatureMeasurement.getFrom().getMillis());
		cRefStart.set(Calendar.HOUR_OF_DAY, 0);
		cRefStart.set(Calendar.MINUTE, 0);
		cRefStart.set(Calendar.SECOND, 0);
		cRefStart.set(Calendar.MILLISECOND, 0);

		Calendar cRefEnd = (Calendar)cRefStart.clone();
		cRefEnd.add(Calendar.DAY_OF_MONTH, 1);
		TemperatureEventDomotics temperature = new TemperatureEventDomotics(
				new DateTime(cRefStart.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
				new DateTime(cRefEnd.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
				room
				);
		temperature.setAggregation("d");
		temperature.setUsername(primaryUser.getUsername());
		temperature.setTemperature(temperatureMeasurement.getTemperature());
		temperatures.add(temperature);
		
		
		int currentDay = cRefStart.get(Calendar.DAY_OF_MONTH);
		
		for(TemperatureEventDomotics hourTemperature : aggregatedByHour){
			
			Calendar c1 = Calendar.getInstance(tz);
			c1.setTimeInMillis(hourTemperature.getFrom().getMillis());
			c1.set(Calendar.HOUR_OF_DAY, 0);
			c1.set(Calendar.MINUTE, 0);
			c1.set(Calendar.SECOND, 0);
			c1.set(Calendar.MILLISECOND, 0);

			Calendar c2 = (Calendar)c1.clone();
			c2.add(Calendar.DAY_OF_MONTH, 1);
			if(c1.get(Calendar.DAY_OF_MONTH) != currentDay){
				temperature = new TemperatureEventDomotics(
						new DateTime(c1.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
						new DateTime(c2.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
						room
					);
				temperature.setUsername(primaryUser.getUsername());
				temperature.setAggregation("d");
				temperature.setTemperature(hourTemperature.getTemperature());
				currentDay = c1.get(Calendar.DAY_OF_MONTH);
				temperatures.add(temperature);				
				
			}
			
			temperature.addTemperature(hourTemperature.getTemperature());
			
		}
		return temperatures.toArray(new TemperatureEventDomotics[temperatures.size()]);
	}

	public HumidityEventDomotics[] aggregateByHourH(HumidityMeasurement[] humMeasurements, User primaryUser, String room, DomoticsUpdates domoticsUpdates) {
		ArrayList<HumidityEventDomotics> humidities = new ArrayList<HumidityEventDomotics>();
		
		HumidityMeasurement[] humidityMeasurements = new HumidityMeasurement[humMeasurements.length];
		
		if(humMeasurements.length == 0){
			return humidities.toArray(new HumidityEventDomotics[humidities.size()]);
		}
		
       long time =0;
       double dulh=0;
		
		if(room == "livingroom")
		{
			time = domoticsUpdates.getLastHumidityUpdateTimestampLivingroom().getMillis();
			dulh = domoticsUpdates.getLastHumidityLivingroom();
			
		}
		
		if(room == "kitchen")
		{
			time = domoticsUpdates.getLastHumidityUpdateTimestampKitchen().getMillis();
			dulh = domoticsUpdates.getLastHumidityKitchen();
		}
		
		if(room == "bedroom")
		{
			time = domoticsUpdates.getLastHumidityUpdateTimestampBedroom().getMillis();
			dulh = domoticsUpdates.getLastHumidityBedroom();
			
		}
		
		if(room == "bathroom")
		{
			time = domoticsUpdates.getLastHumidityUpdateTimestampBathroom().getMillis();
			dulh = domoticsUpdates.getLastHumidityBathroom();
			
		}
		
			if(humMeasurements.length > 0){
				time=time+3600000;
				
				TimeZone tz = TimeZone.getTimeZone("Europe/London");
				Calendar cRefStart = Calendar.getInstance(tz);
				cRefStart.setTimeInMillis(time);
				cRefStart.set(Calendar.MINUTE, 0);
				cRefStart.set(Calendar.SECOND, 0);
				cRefStart.set(Calendar.MILLISECOND, 0);
			
				time=time-3600000;
			double tmp = (humMeasurements[0].getTimestamp()-time)/3600000;
			int cnt = (int) tmp;
			
			
			if(cnt!=0)
			{
				humidityMeasurements = new HumidityMeasurement[humMeasurements.length + cnt];
				System.arraycopy(humMeasurements, 0, humidityMeasurements, cnt, humMeasurements.length);
				
				HumidityMeasurement hum_Measurement = new HumidityMeasurement();
				time=time+3600000;
				int i = 0;
				
				while(cnt!=0)
				{
					hum_Measurement = new HumidityMeasurement();
					hum_Measurement.setIndoorPlaceName(room);
					hum_Measurement.setMeasuredValue(String.valueOf(dulh));
					hum_Measurement.setTimestamp(time);
					
					humidityMeasurements[i]= hum_Measurement;
				time=time+3600000;
				i++;
				cnt--;
				
				}
			}
			else
			{
				humidityMeasurements = new HumidityMeasurement[humMeasurements.length];
				System.arraycopy(humMeasurements, 0, humidityMeasurements, 0, humMeasurements.length);
			}
			
		}
		
		

		//First item, to take the reference hour
		HumidityMeasurement humidityMeasurement = humidityMeasurements[0];
		
		TimeZone tz = TimeZone.getTimeZone("Europe/Rome");
		try {
			String timezoneId = primaryUser.getUserProfile().getvCardSubProfile().getTimezoneid();
			tz = TimeZone.getTimeZone(timezoneId);
		} catch (Exception e) {
			log.error("Error in getting timezoneId from profile, using default ('Europe/Rome'");
		}
		
		Calendar cRefStart = Calendar.getInstance(tz);
		cRefStart.setTimeInMillis(humidityMeasurement.getTimestamp());
		cRefStart.set(Calendar.MINUTE, 0);
		cRefStart.set(Calendar.SECOND, 0);
		cRefStart.set(Calendar.MILLISECOND, 0);

		Calendar cRefEnd = (Calendar)cRefStart.clone();
		cRefEnd.add(Calendar.HOUR_OF_DAY, 1);
		HumidityEventDomotics humidity = new HumidityEventDomotics(
				new DateTime(cRefStart.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
				new DateTime(cRefEnd.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
				room
				);
		humidity.setAggregation("h");
		humidity.setUsername(primaryUser.getUsername());
		humidity.setHumidity(humidityMeasurements[0].getMeasuredValueDouble());
		humidities.add(humidity);
		


		int currentHour = cRefStart.get(Calendar.HOUR_OF_DAY);
		
		for(HumidityMeasurement measurement : humidityMeasurements){
			Calendar c1 = Calendar.getInstance(tz);
			c1.setTimeInMillis(measurement.getTimestamp());
			c1.set(Calendar.MINUTE, 0);
			c1.set(Calendar.SECOND, 0);
			c1.set(Calendar.MILLISECOND, 0);

			Calendar c2 = (Calendar)c1.clone();
			c2.add(Calendar.HOUR_OF_DAY, 1);
			if(c1.get(Calendar.HOUR_OF_DAY) != currentHour){
				humidity = new HumidityEventDomotics(
						new DateTime(c1.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
						new DateTime(c2.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
						room
					);
				humidity.setAggregation("h");
				humidity.setUsername(primaryUser.getUsername());
				humidity.setHumidity(measurement.getMeasuredValueDouble());
				currentHour = c1.get(Calendar.HOUR_OF_DAY);
				humidities.add(humidity);
			
				
			}
	
		
			double newTemp = measurement.getMeasuredValueDouble();
			humidity.addHumidity(newTemp);

			
		}
		return humidities.toArray(new HumidityEventDomotics[humidities.size()]);
	}
	
	
	
	public HumidityEventDomotics[] aggregateByDayH(HumidityEventDomotics[] aggregatedByHour, User primaryUser,String room) {
		ArrayList<HumidityEventDomotics> humidities = new ArrayList<HumidityEventDomotics>();
		if(aggregatedByHour.length == 0){
			return humidities.toArray(new HumidityEventDomotics[humidities.size()]);
		}
		
		//First item, to take the reference day
		HumidityEventDomotics humidityStart = aggregatedByHour[0];
		
		TimeZone tz = TimeZone.getTimeZone("Europe/Rome");
		try {
			String timezoneId = primaryUser.getUserProfile().getvCardSubProfile().getTimezoneid();
			tz = TimeZone.getTimeZone(timezoneId);
		} catch (Exception e) {
			log.error("Error in getting timezoneId from profile, using default ('Europe/Rome'");
		}
		Calendar cRefStart = Calendar.getInstance(tz);
		cRefStart.setTimeInMillis(humidityStart.getFrom().getMillis());
		cRefStart.set(Calendar.HOUR_OF_DAY, 0);
		cRefStart.set(Calendar.MINUTE, 0);
		cRefStart.set(Calendar.SECOND, 0);
		cRefStart.set(Calendar.MILLISECOND, 0);

		Calendar cRefEnd = (Calendar)cRefStart.clone();
		cRefEnd.add(Calendar.DAY_OF_MONTH, 1);
		HumidityEventDomotics humidity = new HumidityEventDomotics(
				new DateTime(cRefStart.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
				new DateTime(cRefEnd.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
				room
				);
		humidity.setAggregation("d");
		humidity.setUsername(primaryUser.getUsername());
		humidity.setHumidity(humidityStart.getHumidity());
		humidities.add(humidity);
		
		
		int currentDay = cRefStart.get(Calendar.DAY_OF_MONTH);
		
		for(HumidityEventDomotics hourHumidity : aggregatedByHour){
			
			Calendar c1 = Calendar.getInstance(tz);
			c1.setTimeInMillis(hourHumidity.getFrom().getMillis());
			c1.set(Calendar.HOUR_OF_DAY, 0);
			c1.set(Calendar.MINUTE, 0);
			c1.set(Calendar.SECOND, 0);
			c1.set(Calendar.MILLISECOND, 0);

			Calendar c2 = (Calendar)c1.clone();
			c2.add(Calendar.DAY_OF_MONTH, 1);
			if(c1.get(Calendar.DAY_OF_MONTH) != currentDay){
				humidity = new HumidityEventDomotics(
						new DateTime(c1.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
						new DateTime(c2.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
						room
					);
				humidity.setUsername(primaryUser.getUsername());
				humidity.setAggregation("d");
				currentDay = c1.get(Calendar.DAY_OF_MONTH);
				humidity.setHumidity(hourHumidity.getHumidity());
				humidities.add(humidity);				
				
			}
			
			humidity.addHumidity(hourHumidity.getHumidity());
			
		}
		return humidities.toArray(new HumidityEventDomotics[humidities.size()]);
	}
	
	public IlluminanceEventDomotics[] aggregateByHourI(IlluminanceMeasurement[] illMeasurements, User primaryUser, String room, DomoticsUpdates domoticsUpdates) {
		ArrayList<IlluminanceEventDomotics> illuminances = new ArrayList<IlluminanceEventDomotics>();
		IlluminanceMeasurement[] illuminanceMeasurements = new IlluminanceMeasurement[illMeasurements.length];
		
		if(illMeasurements.length == 0){
			return illuminances.toArray(new IlluminanceEventDomotics[illuminances.size()]);
		}
		
		
		   long time =0;
	       double duli=0;
			
			if(room == "livingroom")
			{
				time = domoticsUpdates.getLastIlluminanceUpdateTimestampLivingroom().getMillis();
				duli = domoticsUpdates.getLastIlluminanceLivingroom();
				
			}
			
			if(room == "kitchen")
			{
				time = domoticsUpdates.getLastIlluminanceUpdateTimestampKitchen().getMillis();
				duli = domoticsUpdates.getLastIlluminanceKitchen();
			}
			
			if(room == "bedroom")
			{
				time = domoticsUpdates.getLastIlluminanceUpdateTimestampBedroom().getMillis();
				duli = domoticsUpdates.getLastIlluminanceBedroom();
				
			}
			
			if(room == "bathroom")
			{
				time = domoticsUpdates.getLastIlluminanceUpdateTimestampBathroom().getMillis();
				duli = domoticsUpdates.getLastIlluminanceBathroom();
				
			}
			
				if(illMeasurements.length > 0){
					time=time+3600000;
					
					TimeZone tz = TimeZone.getTimeZone("Europe/London");
					Calendar cRefStart = Calendar.getInstance(tz);
					cRefStart.setTimeInMillis(time);
					cRefStart.set(Calendar.MINUTE, 0);
					cRefStart.set(Calendar.SECOND, 0);
					cRefStart.set(Calendar.MILLISECOND, 0);
				
					time=time-3600000;
				
				double tmp = (illMeasurements[0].getTimestamp()-time)/3600000;
				
				int cnt = (int) tmp;
				
				if(cnt!=0)
				{
					
					illuminanceMeasurements = new IlluminanceMeasurement[illMeasurements.length + cnt];
					System.arraycopy(illMeasurements, 0, illuminanceMeasurements, cnt, illMeasurements.length);
					
					IlluminanceMeasurement ill_Measurement = new IlluminanceMeasurement();
					time=time+3600000;
					int i = 0;
					
					
					while(cnt!=0)
					{
						ill_Measurement = new IlluminanceMeasurement();
						ill_Measurement.setIndoorPlaceName(room);
						ill_Measurement.setMeasuredValue(String.valueOf(duli));
						ill_Measurement.setTimestamp(time);
						
						illuminanceMeasurements[i]= ill_Measurement;
					time=time+3600000;
					i++;
					cnt--;
					
					}
				}
				else
				{
					illuminanceMeasurements = new IlluminanceMeasurement[illMeasurements.length];
					System.arraycopy(illMeasurements, 0, illuminanceMeasurements, 0, illMeasurements.length);
				}
				
			}
			
				
				
		//First item, to take the reference hour
		IlluminanceMeasurement illuminanceMeasurement = illuminanceMeasurements[0];
		
		TimeZone tz = TimeZone.getTimeZone("Europe/Rome");
		try {
			String timezoneId = primaryUser.getUserProfile().getvCardSubProfile().getTimezoneid();
			tz = TimeZone.getTimeZone(timezoneId);
		} catch (Exception e) {
			log.error("Error in getting timezoneId from profile, using default ('Europe/Rome'");
		}
		
		Calendar cRefStart = Calendar.getInstance(tz);
		cRefStart.setTimeInMillis(illuminanceMeasurement.getTimestamp());
		cRefStart.set(Calendar.MINUTE, 0);
		cRefStart.set(Calendar.SECOND, 0);
		cRefStart.set(Calendar.MILLISECOND, 0);

		Calendar cRefEnd = (Calendar)cRefStart.clone();
		cRefEnd.add(Calendar.HOUR_OF_DAY, 1);
		IlluminanceEventDomotics illuminance = new IlluminanceEventDomotics(
				new DateTime(cRefStart.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
				new DateTime(cRefEnd.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
				room
				);
		illuminance.setAggregation("h");
		illuminance.setUsername(primaryUser.getUsername());
		illuminance.setIlluminance(illuminanceMeasurements[0].getMeasuredValueDouble());
	   
		illuminances.add(illuminance);
		

		int currentHour = cRefStart.get(Calendar.HOUR_OF_DAY);
		
		for(IlluminanceMeasurement measurement : illuminanceMeasurements){
			Calendar c1 = Calendar.getInstance(tz);
			c1.setTimeInMillis(measurement.getTimestamp());
			c1.set(Calendar.MINUTE, 0);
			c1.set(Calendar.SECOND, 0);
			c1.set(Calendar.MILLISECOND, 0);

			Calendar c2 = (Calendar)c1.clone();
			c2.add(Calendar.HOUR_OF_DAY, 1);
			if(c1.get(Calendar.HOUR_OF_DAY) != currentHour){
				illuminance = new IlluminanceEventDomotics(
						new DateTime(c1.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
						new DateTime(c2.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
						room
					);
				illuminance.setAggregation("h");
				illuminance.setUsername(primaryUser.getUsername());
				illuminance.setIlluminance(measurement.getMeasuredValueDouble());
				currentHour = c1.get(Calendar.HOUR_OF_DAY);
				illuminances.add(illuminance);
			
				
			}
	
		
			double newTemp = measurement.getMeasuredValueDouble();
			illuminance.addIlluminance(newTemp);

			
		}
		return illuminances.toArray(new IlluminanceEventDomotics[illuminances.size()]);
	}
	
	
	
	public IlluminanceEventDomotics[] aggregateByDayI(IlluminanceEventDomotics[] aggregatedByHour, User primaryUser,String room) {
		ArrayList<IlluminanceEventDomotics> illuminancies = new ArrayList<IlluminanceEventDomotics>();
		if(aggregatedByHour.length == 0){
			return illuminancies.toArray(new IlluminanceEventDomotics[illuminancies.size()]);
		}
		
		//First item, to take the reference day
		IlluminanceEventDomotics illuminanceStart = aggregatedByHour[0];
		
		TimeZone tz = TimeZone.getTimeZone("Europe/Rome");
		try {
			String timezoneId = primaryUser.getUserProfile().getvCardSubProfile().getTimezoneid();
			tz = TimeZone.getTimeZone(timezoneId);
		} catch (Exception e) {
			log.error("Error in getting timezoneId from profile, using default ('Europe/Rome'");
		}
		Calendar cRefStart = Calendar.getInstance(tz);
		cRefStart.setTimeInMillis(illuminanceStart.getFrom().getMillis());
		cRefStart.set(Calendar.HOUR_OF_DAY, 0);
		cRefStart.set(Calendar.MINUTE, 0);
		cRefStart.set(Calendar.SECOND, 0);
		cRefStart.set(Calendar.MILLISECOND, 0);

		Calendar cRefEnd = (Calendar)cRefStart.clone();
		cRefEnd.add(Calendar.DAY_OF_MONTH, 1);
		IlluminanceEventDomotics illuminance = new IlluminanceEventDomotics(
				new DateTime(cRefStart.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
				new DateTime(cRefEnd.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
				room
				);
		illuminance.setAggregation("d");
		illuminance.setUsername(primaryUser.getUsername());
		illuminance.setIlluminance(illuminanceStart.getIlluminance());
		illuminancies.add(illuminance);
	
		
		int currentDay = cRefStart.get(Calendar.DAY_OF_MONTH);
		
		for(IlluminanceEventDomotics hourIlluminance : aggregatedByHour){
			
			Calendar c1 = Calendar.getInstance(tz);
			c1.setTimeInMillis(hourIlluminance.getFrom().getMillis());
			c1.set(Calendar.HOUR_OF_DAY, 0);
			c1.set(Calendar.MINUTE, 0);
			c1.set(Calendar.SECOND, 0);
			c1.set(Calendar.MILLISECOND, 0);

			Calendar c2 = (Calendar)c1.clone();
			c2.add(Calendar.DAY_OF_MONTH, 1);
			if(c1.get(Calendar.DAY_OF_MONTH) != currentDay){
				illuminance = new IlluminanceEventDomotics(
						new DateTime(c1.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
						new DateTime(c2.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
						room
					);
				illuminance.setUsername(primaryUser.getUsername());
				illuminance.setAggregation("d");
				currentDay = c1.get(Calendar.DAY_OF_MONTH);
				illuminance.setIlluminance(hourIlluminance.getIlluminance());
				illuminancies.add(illuminance);				
				
			}
			
			illuminance.addIlluminance(hourIlluminance.getIlluminance());
			
		}
		return illuminancies.toArray(new IlluminanceEventDomotics[illuminancies.size()]);
	}

}
