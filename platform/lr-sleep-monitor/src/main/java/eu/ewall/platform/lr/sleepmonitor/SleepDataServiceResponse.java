package eu.ewall.platform.lr.sleepmonitor;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.ewall.platform.idss.utils.json.SqlDateDeserializer;
import eu.ewall.platform.idss.utils.json.SqlDateSerializer;

@JsonIgnoreProperties(ignoreUnknown=true)
public class SleepDataServiceResponse {
	private String username;

	@JsonSerialize(using=SqlDateSerializer.class)
	@JsonDeserialize(using=SqlDateDeserializer.class)
	private LocalDate timestamp;
	
	private int totalSleepTime;
	private int sleepEfficiency;
	private int sleepLatency;
	private int frequencyWakingUp;
	private int snoringTime;
	private int timeInBed;
	private int remTime;
	private long bedOnTime;
	private long bedOffTime;
	private int o2;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public LocalDate getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDate timestamp) {
		this.timestamp = timestamp;
	}

	public int getTotalSleepTime() {
		return totalSleepTime;
	}

	public void setTotalSleepTime(int totalSleepTime) {
		this.totalSleepTime = totalSleepTime;
	}

	public int getSleepEfficiency() {
		return sleepEfficiency;
	}

	public void setSleepEfficiency(int sleepEfficiency) {
		this.sleepEfficiency = sleepEfficiency;
	}

	public int getSleepLatency() {
		return sleepLatency;
	}

	public void setSleepLatency(int sleepLatency) {
		this.sleepLatency = sleepLatency;
	}

	public int getFrequencyWakingUp() {
		return frequencyWakingUp;
	}

	public void setFrequencyWakingUp(int frequencyWakingUp) {
		this.frequencyWakingUp = frequencyWakingUp;
	}

	public int getSnoringTime() {
		return snoringTime;
	}

	public void setSnoringTime(int snoringTime) {
		this.snoringTime = snoringTime;
	}

	public int getTimeInBed() {
		return timeInBed;
	}

	public void setTimeInBed(int timeInBed) {
		this.timeInBed = timeInBed;
	}

	public int getRemTime() {
		return remTime;
	}

	public void setRemTime(int remTime) {
		this.remTime = remTime;
	}

	public long getBedOnTime() {
		return bedOnTime;
	}

	public void setBedOnTime(long bedOnTime) {
		this.bedOnTime = bedOnTime;
	}

	public long getBedOffTime() {
		return bedOffTime;
	}

	public void setBedOffTime(long bedOffTime) {
		this.bedOffTime = bedOffTime;
	}

	public int getO2() {
		return o2;
	}

	public void setO2(int o2) {
		this.o2 = o2;
	}
}
