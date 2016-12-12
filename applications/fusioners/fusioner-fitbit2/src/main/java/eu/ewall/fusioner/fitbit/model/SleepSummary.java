package eu.ewall.fusioner.fitbit.model;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.utils.json.DateTimeFromIsoDateTimeDeserializer;
import eu.ewall.platform.idss.utils.json.IsoDateTimeSerializer;
import eu.ewall.platform.idss.utils.json.SqlDateDeserializer;
import eu.ewall.platform.idss.utils.json.SqlDateSerializer;

public class SleepSummary extends AbstractDatabaseObject {
	@DatabaseField(value=DatabaseType.STRING)
	private String user;
	
	@DatabaseField(value=DatabaseType.DATE)
	@JsonSerialize(using=SqlDateSerializer.class)
	@JsonDeserialize(using=SqlDateDeserializer.class)
	private LocalDate date;

	@DatabaseField(value=DatabaseType.LONG)
	private long startTime;
	
	@DatabaseField(value=DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime isoStartTime;
	
	@DatabaseField(value=DatabaseType.INT)
	private int duration;
	
	@DatabaseField(value=DatabaseType.BYTE)
	private boolean isMainSleep;

	@DatabaseField(value=DatabaseType.INT)
	private int efficiency;

	@DatabaseField(value=DatabaseType.INT)
	private int minutesToFallAsleep;

	@DatabaseField(value=DatabaseType.INT)
	private int minutesAsleep;

	@DatabaseField(value=DatabaseType.INT)
	private int minutesAwake;
	
	@DatabaseField(value=DatabaseType.INT)
	private int minutesAfterWakeup;
	
	@DatabaseField(value=DatabaseType.INT)
	private int awakeCount;
	
	@DatabaseField(value=DatabaseType.INT)
	private int awakeDuration;
	
	@DatabaseField(value=DatabaseType.INT)
	private int restlessCount;
	
	@DatabaseField(value=DatabaseType.INT)
	private int restlessDuration;
	
	@DatabaseField(value=DatabaseType.INT)
	private int timeInBed;
	
	@DatabaseField(value=DatabaseType.BYTE)
	private boolean complete;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public DateTime getIsoStartTime() {
		return isoStartTime;
	}

	public void setIsoStartTime(DateTime isoStartTime) {
		this.isoStartTime = isoStartTime;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public boolean isMainSleep() {
		return isMainSleep;
	}

	public void setMainSleep(boolean isMainSleep) {
		this.isMainSleep = isMainSleep;
	}

	public int getEfficiency() {
		return efficiency;
	}

	public void setEfficiency(int efficiency) {
		this.efficiency = efficiency;
	}

	public int getMinutesToFallAsleep() {
		return minutesToFallAsleep;
	}

	public void setMinutesToFallAsleep(int minutesToFallAsleep) {
		this.minutesToFallAsleep = minutesToFallAsleep;
	}

	public int getMinutesAsleep() {
		return minutesAsleep;
	}

	public void setMinutesAsleep(int minutesAsleep) {
		this.minutesAsleep = minutesAsleep;
	}

	public int getMinutesAwake() {
		return minutesAwake;
	}

	public void setMinutesAwake(int minutesAwake) {
		this.minutesAwake = minutesAwake;
	}

	public int getMinutesAfterWakeup() {
		return minutesAfterWakeup;
	}

	public void setMinutesAfterWakeup(int minutesAfterWakeup) {
		this.minutesAfterWakeup = minutesAfterWakeup;
	}

	public int getAwakeCount() {
		return awakeCount;
	}

	public void setAwakeCount(int awakeCount) {
		this.awakeCount = awakeCount;
	}

	public int getAwakeDuration() {
		return awakeDuration;
	}

	public void setAwakeDuration(int awakeDuration) {
		this.awakeDuration = awakeDuration;
	}

	public int getRestlessCount() {
		return restlessCount;
	}

	public void setRestlessCount(int restlessCount) {
		this.restlessCount = restlessCount;
	}

	public int getRestlessDuration() {
		return restlessDuration;
	}

	public void setRestlessDuration(int restlessDuration) {
		this.restlessDuration = restlessDuration;
	}

	public int getTimeInBed() {
		return timeInBed;
	}

	public void setTimeInBed(int timeInBed) {
		this.timeInBed = timeInBed;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}
}
