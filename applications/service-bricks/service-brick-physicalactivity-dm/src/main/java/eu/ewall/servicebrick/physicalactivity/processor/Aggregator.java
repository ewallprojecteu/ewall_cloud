package eu.ewall.servicebrick.physicalactivity.processor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import eu.ewall.platform.commons.datamodel.activity.ActivityType;
import eu.ewall.platform.commons.datamodel.measure.AccelerometerMeasurement;
import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.servicebrick.common.AggregationPeriod;
import eu.ewall.servicebrick.common.AggregationUnit;
import eu.ewall.servicebrick.physicalactivity.model.InactivityEvent;
import eu.ewall.servicebrick.physicalactivity.model.Movement;
import eu.ewall.servicebrick.physicalactivity.model.MovementUpdate;
import eu.ewall.servicebrick.physicalactivity.model.FitBitMovement;
import eu.ewall.servicebrick.physicalactivity.model.PhysicalActivityEvent;


@Component
public class Aggregator {
	private static final Logger log = LoggerFactory.getLogger(Aggregator.class);
	
	/**
	 * Aggregates a set of accelerometer values by hour. 
	 * Evaluates the "physicalActvityType" against the last known value, and updates the db if a change is detected.
	 * @param accelerometerMeasurements
	 * @param primaryUser
	 * @param activityEvents 
	 * @param lastActivityType 
	 * @param lastInactivityState 
	 * @param inactivityEvents 
	 * @return Array of Movement objects, each one representing the values aggregated by hour for steps, calories, kms 
	 */
	public Movement[] aggregateByHour(AccelerometerMeasurement[] accelerometerMeasurements, User primaryUser, ArrayList<PhysicalActivityEvent> activityEvents, String lastActivityType, ArrayList<InactivityEvent> inactivityEvents, boolean lastInactivityState, long lastStepsValue) {
		ArrayList<Movement> movements = new ArrayList<Movement>();
		if(accelerometerMeasurements.length == 0){
			return movements.toArray(new Movement[movements.size()]);
		}
		
		//First item, to take the reference hour
		AccelerometerMeasurement accelerometerMeasurement = accelerometerMeasurements[0];
		PhysicalActivityEvent lastActivityEvent = new PhysicalActivityEvent(primaryUser.getUsername(), new DateTime(accelerometerMeasurement.getTimestamp()), accelerometerMeasurement.getActivityType());
		if(!lastActivityEvent.getActivityType().equals(lastActivityType)){
			activityEvents.add(lastActivityEvent);
		}
		
		
		InactivityEvent lastInactivityEvent = new InactivityEvent(primaryUser.getUsername(), new DateTime(accelerometerMeasurement.getTimestamp()), isInactive(accelerometerMeasurement));
		if(!(lastInactivityEvent.isInactive() == (lastInactivityState))){
			inactivityEvents.add(lastInactivityEvent);
		}
		
		
		double userWeight = 0d;
		try {
			userWeight = primaryUser.getUserProfile().getHealthSubProfile().getHealthMeasurements().getWeight();
		} catch (Exception e) {
			log.debug("Wrong or missing value for weight in user profile");
		}
		double userHeight = 0d;
		try {
			userHeight = primaryUser.getUserProfile().getHealthSubProfile().getHealthMeasurements().getHeight();
		} catch (Exception e) {
			log.debug("Wrong or missing value for height in user profile");
		}
		TimeZone tz = TimeZone.getTimeZone("Europe/Rome");
		try {
			String timezoneId = primaryUser.getUserProfile().getvCardSubProfile().getTimezoneid();
			tz = TimeZone.getTimeZone(timezoneId);
		} catch (Exception e) {
			log.error("Error in getting timezoneId from profile, using default ('Europe/Rome'");
		}
		
		Calendar cRefStart = Calendar.getInstance(tz);
		cRefStart.setTimeInMillis(accelerometerMeasurement.getTimestamp());
		cRefStart.set(Calendar.MINUTE, 0);
		cRefStart.set(Calendar.SECOND, 0);
		cRefStart.set(Calendar.MILLISECOND, 0);

		Calendar cRefEnd = (Calendar)cRefStart.clone();
		cRefEnd.add(Calendar.HOUR_OF_DAY, 1);
		Movement movement = new Movement(
				new DateTime(cRefStart.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
				new DateTime(cRefEnd.getTimeInMillis(), DateTimeZone.forID(tz.getID()))
				);
		movements.add(movement);
		movement.setAggregation(new AggregationPeriod(1, AggregationUnit.HOUR));
		movement.setUsername(primaryUser.getUsername());
		movement.setSteps(0);
		long startingValue = lastStepsValue;

		int currentHour = cRefStart.get(Calendar.HOUR_OF_DAY);
		
		for(AccelerometerMeasurement measurement : accelerometerMeasurements){
			Calendar c1 = Calendar.getInstance(tz);
			c1.setTimeInMillis(measurement.getTimestamp());
			c1.set(Calendar.MINUTE, 0);
			c1.set(Calendar.SECOND, 0);
			c1.set(Calendar.MILLISECOND, 0);

			Calendar c2 = (Calendar)c1.clone();
			c2.add(Calendar.HOUR_OF_DAY, 1);
			if(c1.get(Calendar.HOUR_OF_DAY) != currentHour){
				movement = new Movement(
						new DateTime(c1.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
						new DateTime(c2.getTimeInMillis(), DateTimeZone.forID(tz.getID()))
					);
				movements.add(movement);				
				movement.setUsername(primaryUser.getUsername());
				movement.setAggregation(new AggregationPeriod(1, AggregationUnit.HOUR));
				currentHour = c1.get(Calendar.HOUR_OF_DAY);
				movement.setSteps(0);
				startingValue = measurement.getSteps();
			}
			//step calculation 
			//Handle the case (if possible) when the accelerometer is reset, so steps are set to 0
			if(measurement.getSteps()<startingValue){
				startingValue=0;
			}
			long newSteps = measurement.getSteps()-startingValue-movement.getSteps();
			movement.addSteps(newSteps);
			
			if(userHeight > 0)
			{
				movement.addKilometers((double)newSteps*userHeight*(double)0.414/(double)100000);
			}
			else
			{
				movement.addKilometers((double)newSteps/(double)1320);
			}

			double walkedDistance = (double)newSteps*userHeight*(double)0.414/(double)100000;
			if((userHeight == 0d) || (userWeight == 0d))
			{
				movement.addBurnedCalories(newSteps*0.039);
			}
			else
			{
				movement.addBurnedCalories((double)2.2*userWeight*(double)0.53*walkedDistance*(double)0.621);
			}
			
			if(!measurement.getActivityType().equals(lastActivityEvent.getActivityType())){				
				lastActivityEvent = new PhysicalActivityEvent(primaryUser.getUsername(), new DateTime(measurement.getTimestamp()), measurement.getActivityType());
				activityEvents.add(lastActivityEvent);	
				log.debug("***Adding event");
			}	
			
			boolean inactivityState = isInactive(measurement);
			if(!(inactivityState == lastInactivityEvent.isInactive())){				
				lastInactivityEvent = new InactivityEvent(primaryUser.getUsername(), new DateTime(measurement.getTimestamp()), inactivityState);
				inactivityEvents.add(lastInactivityEvent);	
				log.debug("***Adding inactivityevent");
			}	
			
		}
		return movements.toArray(new Movement[movements.size()]);
	}
	
	
	
	public Movement[] aggregateByDay(Movement[] aggregatedByHour, User primaryUser) {
		ArrayList<Movement> movements = new ArrayList<Movement>();
		if(aggregatedByHour.length == 0){
			return movements.toArray(new Movement[movements.size()]);
		}
		
		//First item, to take the reference day
		Movement movementStart = aggregatedByHour[0];
		double userWeight = 0d;
		try {
			userWeight = primaryUser.getUserProfile().getHealthSubProfile().getHealthMeasurements().getWeight();
		} catch (Exception e) {
			log.debug("Wrong or missing value for weight in user profile");
		}
		double userHeight = 0d;
		try {
			userHeight = primaryUser.getUserProfile().getHealthSubProfile().getHealthMeasurements().getHeight();
		} catch (Exception e) {
			log.debug("Wrong or missing value for height in user profile");
		}
		TimeZone tz = TimeZone.getTimeZone("Europe/Rome");
		try {
			String timezoneId = primaryUser.getUserProfile().getvCardSubProfile().getTimezoneid();
			tz = TimeZone.getTimeZone(timezoneId);
		} catch (Exception e) {
			log.error("Error in getting timezoneId from profile, using default ('Europe/Rome'");
		}
		Calendar cRefStart = Calendar.getInstance(tz);
		cRefStart.setTimeInMillis(movementStart.getFrom().getMillis());
		cRefStart.set(Calendar.HOUR_OF_DAY, 0);
		cRefStart.set(Calendar.MINUTE, 0);
		cRefStart.set(Calendar.SECOND, 0);
		cRefStart.set(Calendar.MILLISECOND, 0);

		Calendar cRefEnd = (Calendar)cRefStart.clone();
		cRefEnd.add(Calendar.DAY_OF_MONTH, 1);
		Movement movement = new Movement(
				new DateTime(cRefStart.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
				new DateTime(cRefEnd.getTimeInMillis(), DateTimeZone.forID(tz.getID()))
				);
		movements.add(movement);
		movement.setAggregation(new AggregationPeriod(1, AggregationUnit.DAY));
		movement.setUsername(primaryUser.getUsername());
		movement.setSteps(0);
		
		int currentDay = cRefStart.get(Calendar.DAY_OF_MONTH);
		
		for(Movement hourMovement : aggregatedByHour){
			
			Calendar c1 = Calendar.getInstance(tz);
			c1.setTimeInMillis(hourMovement.getFrom().getMillis());
			c1.set(Calendar.HOUR_OF_DAY, 0);
			c1.set(Calendar.MINUTE, 0);
			c1.set(Calendar.SECOND, 0);
			c1.set(Calendar.MILLISECOND, 0);

			Calendar c2 = (Calendar)c1.clone();
			c2.add(Calendar.DAY_OF_MONTH, 1);
			if(c1.get(Calendar.DAY_OF_MONTH) != currentDay){
				movement = new Movement(
						new DateTime(c1.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
						new DateTime(c2.getTimeInMillis(), DateTimeZone.forID(tz.getID()))
					);
				movements.add(movement);				
				movement.setUsername(primaryUser.getUsername());
				movement.setAggregation(new AggregationPeriod(1, AggregationUnit.DAY));
				currentDay = c1.get(Calendar.DAY_OF_MONTH);
				movement.setSteps(0);
			}
			//step calculation 
			movement.addSteps(hourMovement.getSteps());
			
			if(userHeight!= 0)
			{
				movement.addKilometers((double)hourMovement.getSteps()*userHeight*(double)0.414/(double)100000);
			}
			else
			{
				movement.addKilometers((double)hourMovement.getSteps()/(double)1320);
			}

			double  walkedDistance = (double)hourMovement.getSteps()*userHeight*(double)0.414/(double)100000;
			if((userHeight == 0) || (userWeight == 0))
			{
				movement.addBurnedCalories(hourMovement.getSteps()*0.039);
			}
			else
			{
				movement.addBurnedCalories((double)2.2*userWeight*(double)0.53*walkedDistance*(double)0.621);
			}
		}
		return movements.toArray(new Movement[movements.size()]);
	}

	
	
	public Movement[] aggregateByWeek(Movement[] aggregatedByDay, User primaryUser) {
		ArrayList<Movement> movements = new ArrayList<Movement>();
		if(aggregatedByDay.length == 0){
			return movements.toArray(new Movement[movements.size()]);
		}
		
		//First item, to take the reference day
		Movement movementStart = aggregatedByDay[0];
		double userWeight = 0d;
		try {
			userWeight = primaryUser.getUserProfile().getHealthSubProfile().getHealthMeasurements().getWeight();
		} catch (Exception e) {
			log.debug("Wrong or missing value for weight in user profile");
		}
		double userHeight = 0d;
		try {
			userHeight = primaryUser.getUserProfile().getHealthSubProfile().getHealthMeasurements().getHeight();
		} catch (Exception e) {
			log.debug("Wrong or missing value for height in user profile");
		}
		TimeZone tz = TimeZone.getTimeZone("Europe/Rome");
		try {
			String timezoneId = primaryUser.getUserProfile().getvCardSubProfile().getTimezoneid();
			tz = TimeZone.getTimeZone(timezoneId);
		} catch (Exception e) {
			log.error("Error in getting timezoneId from profile, using default ('Europe/Rome'");
		}
		Calendar cRefStart = Calendar.getInstance(tz);
		cRefStart.setTimeInMillis(movementStart.getFrom().getMillis());
		cRefStart.setFirstDayOfWeek(Calendar.MONDAY);
		cRefStart.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cRefStart.set(Calendar.HOUR_OF_DAY, 0);
		cRefStart.set(Calendar.MINUTE, 0);
		cRefStart.set(Calendar.SECOND, 0);
		cRefStart.set(Calendar.MILLISECOND, 0);
		
		Calendar cRefEnd = (Calendar)cRefStart.clone();
		cRefEnd.setFirstDayOfWeek(Calendar.MONDAY);
		cRefEnd.add(Calendar.WEEK_OF_MONTH, 1);
		Movement movement = new Movement(
				new DateTime(cRefStart.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
				new DateTime(cRefEnd.getTimeInMillis(), DateTimeZone.forID(tz.getID()))
				);
		movements.add(movement);
		movement.setAggregation(new AggregationPeriod(1, AggregationUnit.WEEK));
		movement.setUsername(primaryUser.getUsername());
		movement.setSteps(0);
		
		int currentWeek = cRefStart.get(Calendar.WEEK_OF_YEAR);
		
		for(Movement dayMovement : aggregatedByDay){
			
			Calendar c1 = Calendar.getInstance(tz);
			c1.setTimeInMillis(dayMovement.getFrom().getMillis());
			c1.setFirstDayOfWeek(Calendar.MONDAY);
			c1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			c1.set(Calendar.HOUR_OF_DAY, 0);
			c1.set(Calendar.MINUTE, 0);
			c1.set(Calendar.SECOND, 0);
			c1.set(Calendar.MILLISECOND, 0);

			Calendar c2 = (Calendar)c1.clone();
			c2.setFirstDayOfWeek(Calendar.MONDAY);
			c2.add(Calendar.WEEK_OF_MONTH, 1);
			if(c1.get(Calendar.WEEK_OF_YEAR) != currentWeek){
				movement = new Movement(
						new DateTime(c1.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
						new DateTime(c2.getTimeInMillis(), DateTimeZone.forID(tz.getID()))
					);
				movements.add(movement);				
				movement.setUsername(primaryUser.getUsername());
				movement.setAggregation(new AggregationPeriod(1, AggregationUnit.WEEK));
				currentWeek = c1.get(Calendar.WEEK_OF_YEAR);
				movement.setSteps(0);
			}
			//step calculation taking into account that they do not start form 0 every day
			movement.addSteps(dayMovement.getSteps());
			
			if(userHeight!= 0)
			{
				movement.addKilometers((double)dayMovement.getSteps()*userHeight*(double)0.414/(double)100000);
			}
			else
			{
				movement.addKilometers((double)dayMovement.getSteps()/(double)1320);
			}
			
			double walkedDistance = (double)dayMovement.getSteps()*userHeight*(double)0.414/(double)100000;
			if((userHeight == 0) || (userWeight == 0))
			{
				movement.addBurnedCalories(dayMovement.getSteps()*0.039);
			}
			else
			{
				movement.addBurnedCalories((double)2.2*userWeight*(double)0.53*walkedDistance*(double)0.621);
			}
		}
		return movements.toArray(new Movement[movements.size()]);
	}

	public Movement[] aggregateByMonth(Movement[] aggregatedByDay, User primaryUser) {
		ArrayList<Movement> movements = new ArrayList<Movement>();
		if(aggregatedByDay.length == 0){
			return movements.toArray(new Movement[movements.size()]);
		}
		
		//First item, to take the reference day
		Movement movementStart = aggregatedByDay[0];
		double userWeight = 0d;
		try {
			userWeight = primaryUser.getUserProfile().getHealthSubProfile().getHealthMeasurements().getWeight();
		} catch (Exception e) {
			log.debug("Wrong or missing value for weight in user profile");
		}
		double userHeight = 0d;
		try {
			userHeight = primaryUser.getUserProfile().getHealthSubProfile().getHealthMeasurements().getHeight();
		} catch (Exception e) {
			log.debug("Wrong or missing value for height in user profile");
		}
		TimeZone tz = TimeZone.getTimeZone("Europe/Rome");
		try {
			String timezoneId = primaryUser.getUserProfile().getvCardSubProfile().getTimezoneid();
			tz = TimeZone.getTimeZone(timezoneId);
		} catch (Exception e) {
			log.error("Error in getting timezoneId from profile, using default ('Europe/Rome'");
		}
		Calendar cRefStart = Calendar.getInstance(tz);
		cRefStart.setTimeInMillis(movementStart.getFrom().getMillis());
		cRefStart.set(Calendar.DAY_OF_MONTH, 1);
		cRefStart.set(Calendar.HOUR_OF_DAY, 0);
		cRefStart.set(Calendar.MINUTE, 0);
		cRefStart.set(Calendar.SECOND, 0);
		cRefStart.set(Calendar.MILLISECOND, 0);

		Calendar cRefEnd = (Calendar)cRefStart.clone();
		cRefEnd.add(Calendar.MONTH, 1);
		Movement movement = new Movement(
				new DateTime(cRefStart.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
				new DateTime(cRefEnd.getTimeInMillis(), DateTimeZone.forID(tz.getID()))
				);
		movements.add(movement);
		movement.setAggregation(new AggregationPeriod(1, AggregationUnit.MONTH));
		movement.setUsername(primaryUser.getUsername());
		movement.setSteps(0);
		
		int currentMonth = cRefStart.get(Calendar.MONTH);
		
		for(Movement dayMovement : aggregatedByDay){
			Calendar c1 = Calendar.getInstance(tz);
			c1.setTimeInMillis(dayMovement.getFrom().getMillis());
			c1.set(Calendar.DAY_OF_MONTH, 1);
			c1.set(Calendar.HOUR_OF_DAY, 0);
			c1.set(Calendar.MINUTE, 0);
			c1.set(Calendar.SECOND, 0);
			c1.set(Calendar.MILLISECOND, 0);

			Calendar c2 = (Calendar)c1.clone();
			c2.add(Calendar.MONTH, 1);
			if(c1.get(Calendar.MONTH) != currentMonth){
				movement = new Movement(
					new DateTime(c1.getTimeInMillis(), DateTimeZone.forID(tz.getID())),
					new DateTime(c2.getTimeInMillis(), DateTimeZone.forID(tz.getID()))
					);
				movements.add(movement);				
				movement.setUsername(primaryUser.getUsername());
				movement.setAggregation(new AggregationPeriod(1, AggregationUnit.MONTH));
				currentMonth = c1.get(Calendar.MONTH);
				movement.setSteps(0);
			}
			//step calculation, taking into account that they do not start form 0 every day
			movement.addSteps(dayMovement.getSteps());
			
			if(userHeight!= 0)
			{
				movement.addKilometers((double)dayMovement.getSteps()*userHeight*(double)0.414/(double)100000);
			}
			else
			{
				movement.addKilometers((double)dayMovement.getSteps()/(double)1320);
			}
			
			double walkedDistance = (double)dayMovement.getSteps()*userHeight*(double)0.414/(double)100000;
			if((userHeight == 0) || (userWeight == 0))
			{
				movement.addBurnedCalories(dayMovement.getSteps()*0.039);
			}
			else
			{
				movement.addBurnedCalories((double)2.2*userWeight*(double)0.53*walkedDistance*(double)0.621);
			}
			 

		}
		return movements.toArray(new Movement[movements.size()]);
	}

	

	public FitBitMovement[] aggregateByHourFitBit(MovementUpdate movement_FitBit, String primaryUser, DateTimeZone timeZone, int lastSteps, int lastCalories, double lastDistance, DateTime lastTrackerSyncTime, Boolean newDay) {
		ArrayList<FitBitMovement> fitbitmovements = new ArrayList<FitBitMovement>();
		
		
		
		
		FitBitMovement fitbitmovement;
		
			
	

		DateTime lrt = new DateTime(movement_FitBit.getLastTrackerSyncTime(),timeZone);
		

		//log.info("lrt "+lrt);
		//log.info("lastTrackerSyncTime "+ lastTrackerSyncTime);
			
		     if(lrt.isAfter(lastTrackerSyncTime)){
				DateTime cRefStart = new DateTime(lrt,timeZone);
				cRefStart=cRefStart.withMinuteOfHour(0);
				cRefStart=cRefStart.withSecondOfMinute(0);
				cRefStart=cRefStart.withMillisOfSecond(0);

				DateTime cRefEnd = new DateTime(cRefStart,timeZone);
				cRefEnd=cRefEnd.plusHours(1);
				
				fitbitmovement = new FitBitMovement(
						new DateTime(cRefStart,timeZone),
						new DateTime(cRefEnd,timeZone)
					);
				//log.info("fitbitmovement.getFrom "+fitbitmovement.getFrom());
				//log.info("fitbitmovement.getTo "+fitbitmovement.getTo());
				
				fitbitmovements.add(fitbitmovement);				
				fitbitmovement.setUsername(primaryUser);
				fitbitmovement.setAggregation(new AggregationPeriod(1, AggregationUnit.HOUR));
				fitbitmovement.setSteps(0);
				fitbitmovement.setBurnedCalories(0);
				fitbitmovement.setKilometers(0);
				
				  int newSteps;
			      int newCalories;
			      double newDistance;
			   
			   
			      if(lrt.getDayOfMonth()==lastTrackerSyncTime.getDayOfMonth())
			      {	  
			
			      if((movement_FitBit.getSteps()-lastSteps)==0)
			      {
			      
			      }
			      else
			      {  
			        if((movement_FitBit.getSteps()-lastSteps)>0) 
			        {
			        newSteps = movement_FitBit.getSteps()-lastSteps;
					newCalories = movement_FitBit.getActivityCalories() - lastCalories;
					newDistance = movement_FitBit.getDistance() - lastDistance;

					fitbitmovement.addSteps(newSteps);
					fitbitmovement.addBurnedCalories(newCalories);
					fitbitmovement.addKilometers(newDistance);
			        }
			        else
			        {
			       	newSteps = movement_FitBit.getSteps();
					newCalories = movement_FitBit.getActivityCalories();
					newDistance = movement_FitBit.getDistance();

					fitbitmovement.addSteps(newSteps);
					fitbitmovement.addBurnedCalories(newCalories);
					fitbitmovement.addKilometers(newDistance);
			        }
			        
			      }
			      
			      }
			      else
			      {
			
				       	newSteps = movement_FitBit.getSteps();
						newCalories = movement_FitBit.getActivityCalories();
						newDistance = movement_FitBit.getDistance();

						fitbitmovement.addSteps(newSteps);
						fitbitmovement.addBurnedCalories(newCalories);
						fitbitmovement.addKilometers(newDistance);
				  }  
				
			}
			
			
		return fitbitmovements.toArray(new FitBitMovement[fitbitmovements.size()]);
	}
	
	public FitBitMovement[] aggregateByDayFitBit(FitBitMovement[] aggregatedByHourFitBit, String primaryUser, DateTimeZone timeZone) {
		ArrayList<FitBitMovement> fitbitmovements = new ArrayList<FitBitMovement>();
		
		if(aggregatedByHourFitBit.length == 0){
			return fitbitmovements.toArray(new FitBitMovement[fitbitmovements.size()]);
		}
	
		//First item, to take the reference day
		FitBitMovement fitbitmovementStart = aggregatedByHourFitBit[0];
		
		
		DateTime cRefStart = new DateTime(fitbitmovementStart.getFrom(),timeZone);
		cRefStart=cRefStart.withHourOfDay(0);
		cRefStart=cRefStart.withMinuteOfHour(0);
		cRefStart=cRefStart.withSecondOfMinute(0);
		cRefStart=cRefStart.withMillisOfSecond(0);
		
		DateTime cRefEnd = new DateTime(cRefStart,timeZone);
		cRefEnd=cRefEnd.plusDays(1);
	
		
		FitBitMovement fitbitmovement = new FitBitMovement(
				new DateTime(cRefStart,timeZone),
				new DateTime(cRefEnd,timeZone)
				);

		
		fitbitmovements.add(fitbitmovement);				
		fitbitmovement.setUsername(primaryUser);
		fitbitmovement.setAggregation(new AggregationPeriod(1, AggregationUnit.DAY));
		fitbitmovement.setSteps(0);
		fitbitmovement.setBurnedCalories(0);
		fitbitmovement.setKilometers(0);
		
		DateTime currentDay = new DateTime(cRefStart,timeZone);
		
		for(FitBitMovement fitbithourMovement : aggregatedByHourFitBit){
			
			
			DateTime c1RefStart = new DateTime(fitbithourMovement.getFrom(),timeZone);
			c1RefStart=cRefStart.withHourOfDay(0);
			c1RefStart=cRefStart.withMinuteOfHour(0);
			c1RefStart=cRefStart.withSecondOfMinute(0);
			c1RefStart=cRefStart.withMillisOfSecond(0);
			
			DateTime c1RefEnd = new DateTime(c1RefStart,timeZone);
			c1RefEnd=c1RefEnd.plusDays(1);
			
		
			if(c1RefStart.getDayOfMonth()!=(currentDay.getDayOfMonth())){
				
				fitbitmovement = new FitBitMovement(
						new DateTime(c1RefStart,timeZone),
						new DateTime(c1RefEnd,timeZone)
					);
				fitbitmovements.add(fitbitmovement);				
				fitbitmovement.setUsername(primaryUser);
				fitbitmovement.setAggregation(new AggregationPeriod(1, AggregationUnit.DAY));
				currentDay = new DateTime(c1RefStart,timeZone);
				fitbitmovement.setSteps(0);
				fitbitmovement.setBurnedCalories(0);
				fitbitmovement.setKilometers(0);
				
				
			}
			//step calculation 
			fitbitmovement.addSteps(fitbithourMovement.getSteps());
			fitbitmovement.addBurnedCalories(fitbithourMovement.getBurnedCalories());
			fitbitmovement.addKilometers(fitbithourMovement.getKilometers());
			
					}
		return fitbitmovements.toArray(new FitBitMovement[fitbitmovements.size()]);
	}

	
	public FitBitMovement[] aggregateByWeekFitBit(FitBitMovement[] aggregatedByDayFitBit, String primaryUser, DateTimeZone timeZone) {
		ArrayList<FitBitMovement> fitbitmovements = new ArrayList<FitBitMovement>();
		
		if(aggregatedByDayFitBit.length == 0){
			return fitbitmovements.toArray(new FitBitMovement[fitbitmovements.size()]);
		}
		
		//First item, to take the reference day
		FitBitMovement fitbitmovementStart = aggregatedByDayFitBit[0];
		
		
		DateTime cRefStart = new DateTime(fitbitmovementStart.getFrom(),timeZone);
		cRefStart=cRefStart.withDayOfWeek(1);
		cRefStart=cRefStart.withHourOfDay(0);
		cRefStart=cRefStart.withMinuteOfHour(0);
		cRefStart=cRefStart.withSecondOfMinute(0);
		cRefStart=cRefStart.withMillisOfSecond(0);
		
		DateTime cRefEnd = new DateTime(cRefStart,timeZone);
		cRefEnd=cRefEnd.plusWeeks(1);
	
		
		FitBitMovement fitbitmovement = new FitBitMovement(
				new DateTime(cRefStart,timeZone),
				new DateTime(cRefEnd,timeZone)
				);

		
		fitbitmovements.add(fitbitmovement);				
		fitbitmovement.setUsername(primaryUser);
		fitbitmovement.setAggregation(new AggregationPeriod(1, AggregationUnit.WEEK));
		fitbitmovement.setSteps(0);
		fitbitmovement.setBurnedCalories(0);
		fitbitmovement.setKilometers(0);
		
		DateTime currentDay = new DateTime(cRefStart,timeZone);
		
		for(FitBitMovement fitbithourMovement : aggregatedByDayFitBit){
			
			
			DateTime c1RefStart = new DateTime(fitbithourMovement.getFrom(),timeZone);
			c1RefStart=cRefStart.withDayOfWeek(1);
			c1RefStart=cRefStart.withHourOfDay(0);
			c1RefStart=cRefStart.withMinuteOfHour(0);
			c1RefStart=cRefStart.withSecondOfMinute(0);
			c1RefStart=cRefStart.withMillisOfSecond(0);
			
			DateTime c1RefEnd = new DateTime(c1RefStart,timeZone);
			c1RefEnd=c1RefEnd.plusWeeks(1);
			
		
			if(c1RefStart.getDayOfWeek()!=(currentDay).getDayOfWeek()){
				
				fitbitmovement = new FitBitMovement(
						new DateTime(c1RefStart,timeZone),
						new DateTime(c1RefEnd,timeZone)
					);
				fitbitmovements.add(fitbitmovement);				
				fitbitmovement.setUsername(primaryUser);
				fitbitmovement.setAggregation(new AggregationPeriod(1, AggregationUnit.WEEK));
				currentDay = new DateTime(c1RefStart,timeZone);
				fitbitmovement.setSteps(0);
				fitbitmovement.setBurnedCalories(0);
				fitbitmovement.setKilometers(0);
				
			}
			//step calculation 
			fitbitmovement.addSteps(fitbithourMovement.getSteps());
			fitbitmovement.addBurnedCalories(fitbithourMovement.getBurnedCalories());
			fitbitmovement.addKilometers(fitbithourMovement.getKilometers());
			
					}
		return fitbitmovements.toArray(new FitBitMovement[fitbitmovements.size()]);
	}

	public FitBitMovement[] aggregateByMonthFitBit(FitBitMovement[] aggregatedByDayFitBit, String primaryUser, DateTimeZone timeZone) {
		ArrayList<FitBitMovement> fitbitmovements = new ArrayList<FitBitMovement>();
		
		if(aggregatedByDayFitBit.length == 0){
			return fitbitmovements.toArray(new FitBitMovement[fitbitmovements.size()]);
		}
		
		//First item, to take the reference day
		FitBitMovement fitbitmovementStart = aggregatedByDayFitBit[0];
		
		
		DateTime cRefStart = new DateTime(fitbitmovementStart.getFrom(),timeZone);
		cRefStart=cRefStart.withDayOfMonth(1);
		cRefStart=cRefStart.withHourOfDay(0);
		cRefStart=cRefStart.withMinuteOfHour(0);
		cRefStart=cRefStart.withSecondOfMinute(0);
		cRefStart=cRefStart.withMillisOfSecond(0);
		
		DateTime cRefEnd = new DateTime(cRefStart,timeZone);
		cRefEnd=cRefEnd.plusMonths(1);
	
		
		FitBitMovement fitbitmovement = new FitBitMovement(
				new DateTime(cRefStart,timeZone),
				new DateTime(cRefEnd,timeZone)
				);

		
		fitbitmovements.add(fitbitmovement);				
		fitbitmovement.setUsername(primaryUser);
		fitbitmovement.setAggregation(new AggregationPeriod(1, AggregationUnit.MONTH));
		fitbitmovement.setSteps(0);
		fitbitmovement.setBurnedCalories(0);
		fitbitmovement.setKilometers(0);
		
		DateTime currentDay = new DateTime(cRefStart,timeZone);
		
		for(FitBitMovement fitbithourMovement : aggregatedByDayFitBit){
			
			
			DateTime c1RefStart = new DateTime(fitbithourMovement.getFrom(),timeZone);
			c1RefStart=cRefStart.withDayOfMonth(1);
			c1RefStart=cRefStart.withHourOfDay(0);
			c1RefStart=cRefStart.withMinuteOfHour(0);
			c1RefStart=cRefStart.withSecondOfMinute(0);
			c1RefStart=cRefStart.withMillisOfSecond(0);
			
			DateTime c1RefEnd = new DateTime(c1RefStart,timeZone);
			c1RefEnd=c1RefEnd.plusMonths(1);
			
		
			if(c1RefStart.getDayOfWeek()!=(currentDay).getDayOfWeek()){
				
				fitbitmovement = new FitBitMovement(
						new DateTime(c1RefStart,timeZone),
						new DateTime(c1RefEnd,timeZone)
					);
				fitbitmovements.add(fitbitmovement);				
				fitbitmovement.setUsername(primaryUser);
				fitbitmovement.setAggregation(new AggregationPeriod(1, AggregationUnit.MONTH));
				currentDay = new DateTime(c1RefStart,timeZone);
				fitbitmovement.setSteps(0);
				fitbitmovement.setBurnedCalories(0);
				fitbitmovement.setKilometers(0);
			
			}
			//step calculation 
			
			fitbitmovement.addSteps(fitbithourMovement.getSteps());
			fitbitmovement.addBurnedCalories(fitbithourMovement.getBurnedCalories());
			fitbitmovement.addKilometers(fitbithourMovement.getKilometers());
					}
		return fitbitmovements.toArray(new FitBitMovement[fitbitmovements.size()]);
	}

	
	private boolean isInactive(AccelerometerMeasurement measurement){
		if(measurement.getActivityType()==null)
			measurement.setActivityType(ActivityType.UNKNOWN);
		if((measurement.getActivityType().equals(ActivityType.RESTING))||(measurement.getActivityType().equals(ActivityType.UNKNOWN)||(measurement.getActivityType().equals(ActivityType.SLEEPING)))){
			return true;
		}
		return false;
	}
	
	
	/*public static void main(String[] args){
		
		Calendar cRefEnd = Calendar.getInstance();
		cRefEnd.set(Calendar.HOUR, 0);
		cRefEnd.set(Calendar.MINUTE, 0);
		cRefEnd.set(Calendar.SECOND, 0);
		cRefEnd.set(Calendar.MILLISECOND, 0);
		cRefEnd.setFirstDayOfWeek(Calendar.MONDAY);
		cRefEnd.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		for(int i=1; i<100; i++){
			cRefEnd.add(Calendar.WEEK_OF_MONTH, 1);
			System.out.println(cRefEnd.getTime());
		}
	}*/
}
