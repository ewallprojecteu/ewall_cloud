package eu.ewall.platform.reasoner.sleep.service;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.utils.json.SqlDateDeserializer;
import eu.ewall.platform.idss.utils.json.SqlDateSerializer;

public class PSQIDataOutput extends AbstractDatabaseObject{

	@JsonIgnore
	private String id;
	
	@DatabaseField(value=DatabaseType.STRING)
	private String user;
	
	@DatabaseField(value=DatabaseType.DATE)
	@JsonSerialize(using=SqlDateSerializer.class)
	@JsonDeserialize(using=SqlDateDeserializer.class)
	private LocalDate date;
	
	@DatabaseField(value=DatabaseType.INT)
	private int overallSleepQuality;
	
	@DatabaseField(value=DatabaseType.INT)
	private int sleepLatency;
	
	@DatabaseField(value=DatabaseType.INT)
	private int sleepDuration;
	
	@DatabaseField(value=DatabaseType.INT)
	private int sleepEfficiency;
	
	@DatabaseField(value=DatabaseType.INT)
	private int sleepDisturbance;
	
	@DatabaseField(value=DatabaseType.INT)
	private int needMedicine;
	
	@DatabaseField(value=DatabaseType.INT)
	private int dayDysfunction;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getOverallSleepQuality() {
		return overallSleepQuality;
	}

	public void setOverallSleepQuality(int overallSleepQuality) {
		this.overallSleepQuality = overallSleepQuality;
	}

	public int getSleepLatency() {
		return sleepLatency;
	}

	public void setSleepLatency(int sleepLatency) {
		this.sleepLatency = sleepLatency;
	}

	public int getSleepDuration() {
		return sleepDuration;
	}

	public void setSleepDuration(int sleepDuration) {
		this.sleepDuration = sleepDuration;
	}

	public int getSleepEfficiency() {
		return sleepEfficiency;
	}

	public void setSleepEfficiency(int sleepEfficiency) {
		this.sleepEfficiency = sleepEfficiency;
	}

	public int getSleepDisturbance() {
		return sleepDisturbance;
	}

	public void setSleepDisturbance(int sleepDisturbance) {
		this.sleepDisturbance = sleepDisturbance;
	}

	public int getNeedMedicine() {
		return needMedicine;
	}

	public void setNeedMedicine(int needMedicine) {
		this.needMedicine = needMedicine;
	}

	public int getDayDysfunction() {
		return dayDysfunction;
	}

	public void setDayDysfunction(int dayDysfunction) {
		this.dayDysfunction = dayDysfunction;
	}

}
