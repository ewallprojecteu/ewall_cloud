package eu.ewall.fusioner.fitbit.api;

import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.ewall.fusioner.fitbit.utils.json.LocalDateTimeDeserializer;
import eu.ewall.fusioner.fitbit.utils.json.LocalDateTimeSerializer;
import eu.ewall.platform.idss.utils.json.JsonObject;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FitbitSleepPeriod extends JsonObject {
	private boolean isMainSleep;
	private int efficiency;
	@JsonSerialize(using=LocalDateTimeSerializer.class)
	@JsonDeserialize(using=LocalDateTimeDeserializer.class)
	private LocalDateTime startTime;
	private int duration;
	private int minutesToFallAsleep;
	private int minutesAsleep;
	private int minutesAwake;
	private int minutesAfterWakeup;
	private int awakeCount;
	private int awakeDuration;
	private int restlessCount;
	private int restlessDuration;
	private int timeInBed;

	public boolean getIsMainSleep() {
		return isMainSleep;
	}
	
	public void setIsMainSleep(boolean isMainSleep) {
		this.isMainSleep = isMainSleep;
	}
	
	public int getEfficiency() {
		return efficiency;
	}
	
	public void setEfficiency(int efficiency) {
		this.efficiency = efficiency;
	}
	
	public LocalDateTime getStartTime() {
		return startTime;
	}
	
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
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
}
