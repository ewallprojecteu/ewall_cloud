package eu.ewall.servicebrick.caregiverwebapp.processor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import eu.ewall.platform.commons.datamodel.measure.BloodPressureMeasurement;
import eu.ewall.platform.commons.datamodel.measure.HeartRateMeasurement;
import eu.ewall.platform.commons.datamodel.measure.OxygenSaturationMeasurement;
import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.servicebrick.caregiverwebapp.dao.CaregiverWebappDao;
import eu.ewall.servicebrick.caregiverwebapp.model.CaregiverUserThresholds;
import eu.ewall.servicebrick.caregiverwebapp.model.Notification;
import eu.ewall.servicebrick.caregiverwebapp.model.Notification.LocationType;
import eu.ewall.servicebrick.caregiverwebapp.model.Notification.NotificationType;
import eu.ewall.servicebrick.caregiverwebapp.model.Threshold;
import eu.ewall.servicebrick.caregiverwebapp.model.Threshold.Priority;
import eu.ewall.servicebrick.common.AggregationPeriod;
import eu.ewall.servicebrick.common.AggregationUnit;
import eu.ewall.servicebrick.common.dao.DataManagerUpdatesDao;
import eu.ewall.servicebrick.common.dao.ProfilingServerDao;
import eu.ewall.servicebrick.common.dao.SensorEventDao;
import eu.ewall.servicebrick.common.model.NotificationsUpdates;
import eu.ewall.servicebrick.common.model.SensorEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.GasCoEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.GasEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.GasLpgEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.GasNgEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.HumidityEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.IlluminanceEvent;
import eu.ewall.servicebrick.dailyfunctioning.model.TemperatureEvent;
import eu.ewall.servicebrick.physicalactivity.dao.MovementDao;
import eu.ewall.servicebrick.physicalactivity.model.Movement;


@Component
public class ThresholdsProcessor {

	private static final Logger log = LoggerFactory.getLogger(ThresholdsProcessor.class);
	
	private static final int DEFAULT_MAX_DAYS_PER_REQUEST = 20;
	
	@Autowired
	private SensorEventDao sensorEventDao;
	@Autowired
	private CaregiverWebappDao caregiverWebappDao;
	@Autowired
	private ProfilingServerDao profilingServerDao;
	@Autowired
	private MovementDao movementDao;
	@Autowired
	private DataManagerUpdatesDao dataManagerUpdatesDao;
	
	@Value("${maxDaysPerRequest}")
	private String maxDaysPerRequestStr;

	@Value("${processing.startDateYYYYMMdd}")
	private String startDateYYYYMMdd;
	
    SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMdd");

    @Scheduled(initialDelayString="${processing.delay}", fixedRateString="${processing.interval}")
	public <T extends SensorEvent> void process() {
		log.debug("Starting thresholds processing for notifications");
		long start = System.currentTimeMillis();
		
		

		User[] primaryUsers = profilingServerDao.getPrimaryUsers();

		int nrOfUsers = primaryUsers.length;
		long nrOfMeasurements = 0;
		int maxDaysPerRequest;
		
		try {
			if(maxDaysPerRequestStr==null || Integer.parseInt(maxDaysPerRequestStr)<=0) {
				maxDaysPerRequest = DEFAULT_MAX_DAYS_PER_REQUEST;
			} else {
				maxDaysPerRequest = Integer.parseInt(maxDaysPerRequestStr);
			}
		} catch (NumberFormatException e1) {
			log.error("Wrong configuration value for maxDaysPerRequest: " + e1.getMessage());
			maxDaysPerRequest = DEFAULT_MAX_DAYS_PER_REQUEST;
		}
		
		DateTime dt = new DateTime();
		try {
			dt = new DateTime(sdf.parse(startDateYYYYMMdd));
		} catch (ParseException e1) {
			log.error("Error in configured starting date 'processing.startDateYYYYMMdd'. Setting to default (now)");
		}
		log.info("Thresholds processing: issuing requests for periods of " + maxDaysPerRequest + " days");
		
		for(User primaryUser : primaryUsers){
			
			NotificationsUpdates notificationsUpdates = null;
			ArrayList<Notification> notifications = new ArrayList<Notification>();
			log.info(primaryUser.getUsername());
			try {
				notificationsUpdates = dataManagerUpdatesDao.getNotificationsReadingByUsername(primaryUser.getUsername());
				if(notificationsUpdates!=null){
					log.debug("Got notifications reading for user " + notificationsUpdates.getUsername());
				}
			} catch (Exception e) {
				log.debug("Exception in getting notifications reading for user " + primaryUser.getUsername(), e);
			} 
			if(notificationsUpdates==null){
				log.info("Starting date for indexing: " + dt);
				notificationsUpdates = new NotificationsUpdates(primaryUser.getUsername(), dt);
			}

			List<CaregiverUserThresholds> userThresholds = caregiverWebappDao.readThresholds(primaryUser.getUsername());
			DateTime from = notificationsUpdates.getLastUpdateTimestamp();
			DateTime to = new DateTime();
			boolean changed = false;
			
			//Sensor events: environment (per room)
			ArrayList<T> allEvents = new ArrayList<T>();
			List<T> events = (List<T>) sensorEventDao.readEvents(GasCoEvent.class, primaryUser.getUsername(), from, to);
			allEvents.addAll(events);
			events = (List<T>) sensorEventDao.readEvents(GasLpgEvent.class, primaryUser.getUsername(), from, to);
			allEvents.addAll(events);
			events = (List<T>) sensorEventDao.readEvents(GasNgEvent.class, primaryUser.getUsername(), from, to);
			allEvents.addAll(events);
			events = (List<T>) sensorEventDao.readEvents(TemperatureEvent.class, primaryUser.getUsername(), from, to);
			allEvents.addAll(events);
			events = (List<T>) sensorEventDao.readEvents(IlluminanceEvent.class, primaryUser.getUsername(), from, to);
			allEvents.addAll(events);
			events = (List<T>) sensorEventDao.readEvents(HumidityEvent.class, primaryUser.getUsername(), from, to);
			allEvents.addAll(events);
			//log.info(""+allEvents.size());
			if(allEvents.size()>0){
				changed=true;
			}
			for(T event : allEvents ){
				log.info(event.getClass().getSimpleName() + ", " + event.getUsername());
				for(CaregiverUserThresholds thresholds : userThresholds){
					log.info("managing threshold " + event.getClass().getSimpleName() + " for caregiver: " + thresholds.getCaregiverUsername() + ", user: " + thresholds.getPrimaryUserUsername());
					Threshold threshold = thresholds.getThresholdByLinkedEventName(event.getLocation().concat(".").concat(event.getClass().getSimpleName()));
					log.info("threshold: " + threshold.getName());
					manageEvent(event, threshold, thresholds.getCaregiverUsername(), primaryUser, notifications);
				}
			}
			
			//Movement data (aggregated by 24 h)
			try {
				List<Movement> movements = movementDao.readMovements(primaryUser.getUsername(), from, to, new AggregationPeriod(1, AggregationUnit.DAY), Movement.class);
				if(movements.size()>0){
					changed=true;
				}
				for(Movement movement : movements){
					log.info("Managing movement for " + movement.getUsername());
					for(CaregiverUserThresholds thresholds : userThresholds){
						log.info("managing steps, calories and kilometers thresholds  for caregiver: " + thresholds.getCaregiverUsername() + ", user: " + thresholds.getPrimaryUserUsername());
						Threshold threshold = thresholds.getThresholdByName("physicalActivities_burnedCalories");
						log.info("threshold: " + threshold.getName());
						manageMeasurement(movement.getBurnedCalories(), movement.getTo(), threshold, thresholds.getCaregiverUsername(), primaryUser, NotificationType.CALORIES_GOAL_NOT_MATCHED, LocationType.UNDEFINED, notifications);
						threshold = thresholds.getThresholdByName("physicalActivities_steps");
						log.info("threshold: " + threshold.getName());
						manageMeasurement(movement.getSteps(), movement.getTo(), threshold, thresholds.getCaregiverUsername(), primaryUser, NotificationType.STEPS_GOAL_NOT_MATCHED, LocationType.UNDEFINED, notifications);
						threshold = thresholds.getThresholdByName("physicalActivities_kilometers");
						log.info("threshold: " + threshold.getName());
						manageMeasurement(movement.getKilometers(), movement.getTo(), threshold, thresholds.getCaregiverUsername(), primaryUser, NotificationType.KILOMETERS_GOAL_NOT_MATCHED, LocationType.UNDEFINED, notifications);
					}				
				}
			} catch (Exception e) {
				log.error("No movement measured, skipping");
			}
			
			//Vital signs
			try {
				BloodPressureMeasurement[] bloodPressureMeasurements = profilingServerDao.getBloodPressure(primaryUser.getUsername(), from, to, null);
				if(bloodPressureMeasurements.length>0){
					changed=true;
				}
				for(BloodPressureMeasurement measurement : bloodPressureMeasurements) {
					log.info("Managing " + measurement.getClass().getSimpleName() + " for " + primaryUser.getUsername());
					for(CaregiverUserThresholds thresholds : userThresholds){
						log.info("managing " + measurement.getClass().getSimpleName() + " for caregiver: " + thresholds.getCaregiverUsername() + ", user: " + thresholds.getPrimaryUserUsername());
						Threshold threshold = thresholds.getThresholdByName("vitals_bloodPressureDiastolic");
						log.info("threshold: " + threshold.getName());
						manageMeasurement(measurement.getDiastolicBloodPressure(), new DateTime(measurement.getTimestamp()), threshold, thresholds.getCaregiverUsername(), primaryUser, NotificationType.BLOOD_PRESSURE_DIASTOLIC_VALUE_OUT_OF_RANGE, LocationType.UNDEFINED, notifications);
						threshold = thresholds.getThresholdByName("vitals_bloodPressureSystolic");
						log.info("threshold: " + threshold.getName());
						manageMeasurement(measurement.getSystolicBloodPressure(), new DateTime(measurement.getTimestamp()), threshold, thresholds.getCaregiverUsername(), primaryUser, NotificationType.BLOOD_PRESSURE_SYSTOLIC_VALUE_OUT_OF_RANGE, LocationType.UNDEFINED, notifications);
					}
				}
			} catch (Exception e) {
				log.error("No blood pressure measured, skipping");
			}
			try {
				HeartRateMeasurement[] heartRateMeasurements = profilingServerDao.getHeartRate(primaryUser.getUsername(), from, to, null);
				if(heartRateMeasurements.length>0){
					changed=true;
				}
				for(HeartRateMeasurement measurement : heartRateMeasurements) {
					log.info("Managing " + measurement.getClass().getSimpleName() + " for " + primaryUser.getUsername());
					for(CaregiverUserThresholds thresholds : userThresholds){
						log.info("managing " + measurement.getClass().getSimpleName() + " for caregiver: " + thresholds.getCaregiverUsername() + ", user: " + thresholds.getPrimaryUserUsername());
						Threshold threshold = thresholds.getThresholdByName("vitals_heartRate");
						log.info("threshold: " + threshold.getName());
						manageMeasurement(measurement.getHeartRate(), new DateTime(measurement.getTimestamp()), threshold, thresholds.getCaregiverUsername(), primaryUser,  NotificationType.HEART_RATE_VALUE_OUT_OF_RANGE, LocationType.UNDEFINED, notifications);
					}
				}
			} catch (Exception e) {
				log.error("No heart rate measured, skipping");
			}
			try {
				OxygenSaturationMeasurement[] oxygenSaturationMeasurements = profilingServerDao.getOxygenSaturation(primaryUser.getUsername(), from, to, null);
				if(oxygenSaturationMeasurements.length>0){
					changed=true;
				}
				for(OxygenSaturationMeasurement measurement : oxygenSaturationMeasurements) {
					log.info("Managing " + measurement.getClass().getSimpleName() + " for " + primaryUser.getUsername());
					for(CaregiverUserThresholds thresholds : userThresholds){
						log.info("managing " + measurement.getClass().getSimpleName() + " for caregiver: " + thresholds.getCaregiverUsername() + ", user: " + thresholds.getPrimaryUserUsername());
						Threshold threshold = thresholds.getThresholdByName("vitals_oxygenSaturation");
						log.info("threshold: " + threshold.getName());
						manageMeasurement(measurement.getOxygenSaturation(), new DateTime(measurement.getTimestamp()), threshold, thresholds.getCaregiverUsername(), primaryUser,  NotificationType.OXYGEN_SATURATION_VALUE_OUT_OF_RANGE, LocationType.UNDEFINED, notifications);
					}
				}
			} catch (Exception e) {
				log.error("No oxygen saturation measured, skipping");
			}	
			if(notifications.size()>0){
				caregiverWebappDao.insertNotifications(notifications);
			}
			if(changed){
				notificationsUpdates.setLastUpdateTimestamp(new DateTime());
				dataManagerUpdatesDao.updateNotificationsReading(notificationsUpdates);
			}

		}		
	} 
	
	private void manageMeasurement(double measuredValue, DateTime detectionDate, Threshold threshold, String caregiverUsername, User primaryUser, NotificationType notificationType, LocationType locationType, ArrayList<Notification> notifications) {
		try {
			log.info("managing measurement for " + threshold.getName() + ", value: " + measuredValue);
			if(isOutOfThreshold(measuredValue, threshold)) {
				createNotification(measuredValue, detectionDate, threshold, caregiverUsername, primaryUser, notificationType, locationType, notifications);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	private void createNotification(double value, DateTime detectionDate, Threshold threshold, String caregiverUsername, User primaryUser, NotificationType notificationType, LocationType locationType, ArrayList<Notification> notifications) {
		Notification lastNotification = caregiverWebappDao.findLastNotificationByType(caregiverUsername, primaryUser.getUsername(), threshold.getName(), locationType);
		Notification lastNotification2 = searchInNotStored(caregiverUsername, primaryUser.getUsername(), threshold.getName(), notifications, locationType);
		if(lastNotification2!=null && (lastNotification==null || lastNotification2.getDetectionDate().isAfter(lastNotification.getDetectionDate()))){
			lastNotification=lastNotification2;
		}
		//getPriority
		//get detectedDate
		//based on priority, evaluate if is is needed to create a new notification
		boolean expired = false;
		if(lastNotification != null){
			try {
				DateTime lastNotificationDetectionDate = lastNotification.getDetectionDate();
				DateTime currentDetectionDate =  new DateTime(detectionDate);
				if(currentDetectionDate.isAfter(lastNotificationDetectionDate)){
					Priority priority = lastNotification.getPriority();
					Duration duration = (new Interval(lastNotificationDetectionDate, currentDetectionDate)).toDuration();
					Duration highPriorityDuration = new Duration(1000*60*60);
					Duration midPriorityDuration = new Duration(1000*60*60*12);
					Duration lowPriorityDuration = new Duration(1000*60*60*24);
					switch(priority){
						case HIGH:
							if(duration.isLongerThan(highPriorityDuration)){
								expired = true;
								
							};
							break;
						case MEDIUM:
							if(duration.isLongerThan(midPriorityDuration)){
								expired = true;
							};
							break;
						case LOW:
							if(duration.isLongerThan(lowPriorityDuration)){
								expired = true;
							};
							break;
						case NONE:
							break;
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if((lastNotification == null || expired) && (threshold.getPriority() != Priority.NONE)) {
			log.info("Creating notification for " + threshold.getName() + ": value " + value + " is out of range; priority is " + threshold.getPriority());
			Notification notification = new Notification(new DateTime(), detectionDate, caregiverUsername, primaryUser.getUsername(), primaryUser.getFirstName(), primaryUser.getLastName(), notificationType, locationType, value, threshold.getTargetMin(), threshold.getTargetMax(), true, null, null, threshold.getPriority(), threshold.getName());
			notifications.add(notification);
		}
		
	}

	private Notification searchInNotStored(String caregiverUsername, String username, String thresholdName, ArrayList<Notification> notifications, LocationType locationType) {
		Notification mostRecent = null; 
		for(Notification notification : notifications){
			if(notification.getCaregiverUsername().equals(caregiverUsername) && notification.getUsername().equals(username) && notification.getThresholdName().equals(thresholdName) && notification.getLocationType().equals(locationType)){
				if(mostRecent==null || notification.getDetectionDate().isAfter(mostRecent.getDetectionDate())) {
					mostRecent = notification;
				}
			}
		}
		return mostRecent;
	}

	private <T extends SensorEvent> void manageEvent(T event, Threshold threshold, String caregiverUsername, User primaryUser, ArrayList<Notification> notifications) {
		try {
			double value=-1;
			NotificationType notificationType=NotificationType.UNDEFINED;
			LocationType locationType=LocationType.UNDEFINED;
			if(event instanceof GasCoEvent){
				log.info("managing event " + event.getClass() + ", value: " + ((GasEvent)event).getGasValue());
				value = ((GasCoEvent)event).getGasValue();
				notificationType=NotificationType.GAS_CO_VALUE_OUT_OF_RANGE;
				locationType=LocationType.valueOf(event.getLocation());
			} else
				if(event instanceof GasLpgEvent){
					log.info("managing event " + event.getClass() + ", value: " + ((GasEvent)event).getGasValue());
					value = ((GasLpgEvent)event).getGasValue();
					notificationType=NotificationType.GAS_LPG_VALUE_OUT_OF_RANGE;
					locationType=LocationType.valueOf(event.getLocation());
				} else
					if(event instanceof GasNgEvent){
						log.info("managing event " + event.getClass() + ", value: " + ((GasEvent)event).getGasValue());
						value = ((GasNgEvent)event).getGasValue();
						notificationType=NotificationType.GAS_NG_VALUE_OUT_OF_RANGE;
						locationType=LocationType.valueOf(event.getLocation());
					} else
						if(event instanceof TemperatureEvent){
							log.info("managing event " + event.getClass() + ", value: " + ((TemperatureEvent)event).getTemperature());
							value =  ((TemperatureEvent)event).getTemperature();
							notificationType=NotificationType.TEMPERATURE_VALUE_OUT_OF_RANGE;
							locationType=LocationType.valueOf(event.getLocation());
						} else
							if(event instanceof IlluminanceEvent){
								log.info("managing event " + event.getClass() + ", value: " + ((IlluminanceEvent)event).getIlluminance());
								value =  ((IlluminanceEvent)event).getIlluminance();
								notificationType=NotificationType.ILLUMINANCE_VALUE_OUT_OF_RANGE;
								locationType=LocationType.valueOf(event.getLocation());
							} else
								if(event instanceof HumidityEvent){
									log.info("managing event " + event.getClass() + ", value: " + ((HumidityEvent)event).getHumidity());
									value =  ((HumidityEvent)event).getHumidity();
									notificationType=NotificationType.HUMIDITY_VALUE_OUT_OF_RANGE;
									locationType=LocationType.valueOf(event.getLocation());
								} 
			
			if(value>0 && isOutOfThreshold(value, threshold)) {
				createNotification(value, event.getTimestamp(), threshold, caregiverUsername, primaryUser, notificationType, locationType, notifications);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
		}


	}

	private boolean isOutOfThreshold(double value, Threshold threshold) {
		if(threshold.getTargetMin()<0 && threshold.getTargetMax()<0){
			log.info("No thresholds configured");
			return false;
		}
		if(threshold.getTargetMin()<0){
			if(value>threshold.getTargetMax()){
				log.info("Triggering notification (must be < of "+threshold.getTargetMax()+")");
				return true;
			}
		} else 
			if(threshold.getTargetMax()<0){
				if(value<threshold.getTargetMin()){
					log.info("Triggering notification (must be > of "+threshold.getTargetMin()+")");
					return true;
				}
			} else 
				if(value<threshold.getTargetMin() || value>threshold.getTargetMax()){
					log.info("Triggering notification (must be between "+threshold.getTargetMin()+" and "+threshold.getTargetMax()+")");
					return true;
				}
		return false;
	}
}
