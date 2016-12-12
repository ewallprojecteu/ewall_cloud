package eu.ewall.servicebrick.caregiverwebapp.model;

import java.util.UUID;

import org.joda.time.DateTime;

import eu.ewall.servicebrick.caregiverwebapp.model.Threshold.Priority;

public 	class Notification {
		
	private String uuid;
	private DateTime date;
	private DateTime detectionDate;
	private String caregiverUsername;
	private String username;
	private String firstname;
	private String lastname;
	private NotificationType notificationType;
	private LocationType locationType;
	private double value;
	private double targetMin;
	private double targetMax;
	private boolean unread = true;
	private DateTime firstReadingDate = null;
	private DateTime lastReadingDate = null;
	private Priority priority;
	private String thresholdName;
	
	public enum NotificationType {
		UNDEFINED,
		GAS_CO_VALUE_OUT_OF_RANGE, 
		GAS_NG_VALUE_OUT_OF_RANGE, 
		GAS_LPG_VALUE_OUT_OF_RANGE, 
		TEMPERATURE_VALUE_OUT_OF_RANGE, 
		HUMIDITY_VALUE_OUT_OF_RANGE, 
		ILLUMINANCE_VALUE_OUT_OF_RANGE, 
		BLOOD_PRESSURE_DIASTOLIC_VALUE_OUT_OF_RANGE, 
		BLOOD_PRESSURE_SYSTOLIC_VALUE_OUT_OF_RANGE, 
		HEART_RATE_VALUE_OUT_OF_RANGE, 
		OXYGEN_SATURATION_VALUE_OUT_OF_RANGE, 
		STEPS_GOAL_NOT_MATCHED,
		CALORIES_GOAL_NOT_MATCHED,
		KILOMETERS_GOAL_NOT_MATCHED
	}

	public enum LocationType {
		UNDEFINED,
		kitchen,
		livingroom,
		bedroom,
		bathroom
	}

	public Notification() {
	}

	public Notification(DateTime date, DateTime detectionDate, String caregiverUsername, String username, String firstname, String lastname, NotificationType notificationType, LocationType locationType, double value, double targetMin, double targetMax, boolean unread, DateTime firstReadingDate, DateTime lastReadingDate, Priority priority, String thresholdName){
		this.uuid = UUID.randomUUID().toString();
		this.date = date;
		this.detectionDate = detectionDate;
		this.caregiverUsername = caregiverUsername;
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.notificationType = notificationType;
		this.locationType = locationType;
		this.value = value;
		this.targetMin = targetMin;
		this.targetMax = targetMax;
		this.unread = unread;
		this.firstReadingDate = firstReadingDate;
		this.lastReadingDate = lastReadingDate;
		this.priority = priority;
		this.thresholdName = thresholdName;
	}

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public String getCaregiverUsername() {
		return caregiverUsername;
	}

	public void setCaregiverUsername(String caregiverUsername) {
		this.caregiverUsername = caregiverUsername;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public boolean isUnread() {
		return unread;
	}

	public void setUnread(boolean unread) {
		this.unread = unread;
	}

	public DateTime getFirstReadingDate() {
		return firstReadingDate;
	}

	public void setFirstReadingDate(DateTime firstReadingDate) {
		this.firstReadingDate = firstReadingDate;
	}

	public DateTime getLastReadingDate() {
		return lastReadingDate;
	}

	public void setLastReadingDate(DateTime lastReadingDate) {
		this.lastReadingDate = lastReadingDate;
	}


	public String getUuid() {
		return uuid;
	}

	public DateTime getDetectionDate() {
		return detectionDate;
	}

	public void setDetectionDate(DateTime detectionDate) {
		this.detectionDate = detectionDate;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public String getThresholdName() {
		return thresholdName;
	}

	public void setThresholdName(String thresholdName) {
		this.thresholdName = thresholdName;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getTargetMin() {
		return targetMin;
	}

	public void setTargetMin(double targetMin) {
		this.targetMin = targetMin;
	}

	public double getTargetMax() {
		return targetMax;
	}

	public void setTargetMax(double targetMax) {
		this.targetMax = targetMax;
	}

	public NotificationType getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}

	public LocationType getLocationType() {
		return locationType;
	}

	public void setLocationType(LocationType locationType) {
		this.locationType = locationType;
	}

}
