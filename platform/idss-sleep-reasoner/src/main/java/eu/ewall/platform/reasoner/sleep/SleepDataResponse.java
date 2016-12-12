package eu.ewall.platform.reasoner.sleep;

import eu.ewall.platform.reasoner.sleep.service.PSQIDataResponse;
import org.joda.time.LocalDate;
import org.joda.time.DateTime;

public class SleepDataResponse {

	private String username; 
	private String timestamp;
	private int totalSleepTime;
	private int sleepEfficiency;
	private int sleepLatency;
	private int frequencyWakingUp;
	private int snoringTime;
	private int timeInBed;
	private PSQIDataResponse PSQI;
	private int remTime;
	private int O2;
	private long BedOnTime;
	private long BedOffTime;

	public SleepDataResponse(String username,String timestamp, int totalSleepTime, int sleepEfficiency, int sleepLatency, int frequencyWakingUp, int snoringTime, int timeInBed, PSQIDataResponse PSQI, int remTime,
			int O2,long BedOnTime, long BedOffTime) {
		this.setUsername(username);
		this.setTimestamp(timestamp);
		this.setTotalSleepTime(totalSleepTime);
		this.setSleepEfficiency(sleepEfficiency);
		this.setSleepLatency(sleepLatency);
		this.setFrequencyWakingUp(frequencyWakingUp);
		this.setSnoringTime(snoringTime);
		this.setTimeInBed(timeInBed);
		this.setPSQI(PSQI);
		this.setRemTime(remTime);
		this.setO2(O2);
		this.setBedOnTime(BedOnTime);
		this.setBedOffTime(BedOffTime);

	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(String value) {
		this.timestamp = value;
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

	public PSQIDataResponse getPSQI() {
		return PSQI;
	}

	public void setPSQI(PSQIDataResponse PSQI) {
		this.PSQI = PSQI;
	}

	public int getRemTime() {
		return remTime;
	}

	public void setRemTime(int remTime) {
		this.remTime = remTime;
	}

	public int getO2() {
		return O2;
	}

	public void setO2(int O2) {
		this.O2 = O2;
	}
	
	public long getBedOnTime() {
		return BedOnTime;
	}

	public void setBedOnTime(long BedOnTime) {
		this.BedOnTime = BedOnTime;
	}
	
	public long getBedOffTime() {
		return BedOffTime;
	}

	public void setBedOffTime(long BedOffTime) {
		this.BedOffTime = BedOffTime;
	}
}
