package eu.ewall.fusioner.fitbit.model;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;


import java.io.Serializable;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="sleep_summaries")
public class FitBitEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// The id is using for saving the mongo _id only and not exposed externally at the moment
	@Id
	private ObjectId id;
	

	private int awakeCount;
	private int awakeDuration;
	private int complete;
	private String date;
	private int duration;
	private int efficiency;
	private int isMainSleep;
	private String  isoStartTime;
	private int minutesAfterWakeup;
	private int minutesAsleep;
	private int minutesAwake;
	private int minutesToFallAsleep;
	private int restlessCount;
	private int restlessDuration;
	private long startTime;
	private int timeInBed;
	private String user;
	
	
	
	
	@PersistenceConstructor
	public FitBitEvent( int awakeCount,	int awakeDuration, int complete, String date,
						 int duration, int efficiency, int isMainSleep, String isoStartTime,
						 int minutesAfterWakeup, int minutesAsleep, int minutesAwake, int minutesToFallAsleep,
						 int restlessCount, int restlessDuration, long startTime, int timeInBed,String user) {
		
		this.awakeCount=awakeCount;
		this.awakeDuration=awakeDuration;
		this.complete=complete;
		this.date=date;
		this.duration=duration;
		this.efficiency=efficiency;
		this.isMainSleep=isMainSleep;
		this.isoStartTime=isoStartTime;
		this.minutesAfterWakeup=minutesAfterWakeup;
		this.minutesAsleep=minutesAsleep;
		this.minutesAwake=minutesAwake;
		this.minutesToFallAsleep=minutesToFallAsleep;
		this.restlessCount=restlessCount;
		this.restlessDuration=restlessDuration;
		this.startTime=startTime;
		this.timeInBed=timeInBed;
		this.user=user;
	}

	
	
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public String getIsoStartTime() {
		return isoStartTime;
	}

	public void setIsoStartTime(String isoStartTime) {
		this.isoStartTime = isoStartTime;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getIsMainSleep() {
		return isMainSleep;
	}

	public void setIsMainSleep(int isMainSleep) {
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

	public int isComplete() {
		return complete;
	}

	public void setComplete(int complete) {
		this.complete = complete;
	}
}
