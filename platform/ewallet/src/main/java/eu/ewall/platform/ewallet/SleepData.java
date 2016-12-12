package eu.ewall.platform.ewallet;

/**
 * Created by apne on 4/28/2016.
 */
public class SleepData {
    private String username;
    private String timestamp;
    private int totalSleepTime;
    private int sleepEfficiency;
    private int sleepLatency;
    private int frequencyWakingUp;
    private int snoringTime;
    private int timeInBed;
    private int remTime;
    private Psqi psqi;
    private int o2;
    private long bedOnTime;
    private long bedOffTime;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
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

    public Psqi getPsqi() {
        return psqi;
    }

    public void setPsqi(Psqi psqi) {
        this.psqi = psqi;
    }

    public int getO2() {
        return o2;
    }

    public void setO2(int o2) {
        this.o2 = o2;
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
}
