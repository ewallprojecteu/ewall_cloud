package eu.ewall.fusioner.fitbit.model;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.utils.json.SqlDateDeserializer;
import eu.ewall.platform.idss.utils.json.SqlDateSerializer;

public class ActivitySummary extends AbstractDatabaseObject {
	@DatabaseField(value=DatabaseType.STRING)
	private String user;
	
	@DatabaseField(value=DatabaseType.DATE)
	@JsonSerialize(using=SqlDateSerializer.class)
	@JsonDeserialize(using=SqlDateDeserializer.class)
	private LocalDate date;
	
	@DatabaseField(value=DatabaseType.INT)
	private int activityCalories;

	@DatabaseField(value=DatabaseType.INT)
	private int caloriesBMR;

	@DatabaseField(value=DatabaseType.INT)
	private int caloriesOut;
	
	@DatabaseField(value=DatabaseType.FLOAT)
	private float distance;

	@DatabaseField(value=DatabaseType.FLOAT)
	private float elevation;

	@DatabaseField(value=DatabaseType.INT)
	private int floors;

	@DatabaseField(value=DatabaseType.INT)
	private int steps;

	@DatabaseField(value=DatabaseType.INT)
	private int sedentaryMinutes;

	@DatabaseField(value=DatabaseType.INT)
	private int lightlyActiveMinutes;

	@DatabaseField(value=DatabaseType.INT)
	private int fairlyActiveMinutes;

	@DatabaseField(value=DatabaseType.INT)
	private int veryActiveMinutes;

	@DatabaseField(value=DatabaseType.BYTE)
	private boolean complete = false;

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

	public int getActivityCalories() {
		return activityCalories;
	}

	public void setActivityCalories(int activityCalories) {
		this.activityCalories = activityCalories;
	}

	public int getCaloriesBMR() {
		return caloriesBMR;
	}

	public void setCaloriesBMR(int caloriesBMR) {
		this.caloriesBMR = caloriesBMR;
	}

	public int getCaloriesOut() {
		return caloriesOut;
	}

	public void setCaloriesOut(int caloriesOut) {
		this.caloriesOut = caloriesOut;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public float getElevation() {
		return elevation;
	}

	public void setElevation(float elevation) {
		this.elevation = elevation;
	}

	public int getFloors() {
		return floors;
	}

	public void setFloors(int floors) {
		this.floors = floors;
	}

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public int getSedentaryMinutes() {
		return sedentaryMinutes;
	}

	public void setSedentaryMinutes(int sedentaryMinutes) {
		this.sedentaryMinutes = sedentaryMinutes;
	}

	public int getLightlyActiveMinutes() {
		return lightlyActiveMinutes;
	}

	public void setLightlyActiveMinutes(int lightlyActiveMinutes) {
		this.lightlyActiveMinutes = lightlyActiveMinutes;
	}

	public int getFairlyActiveMinutes() {
		return fairlyActiveMinutes;
	}

	public void setFairlyActiveMinutes(int fairlyActiveMinutes) {
		this.fairlyActiveMinutes = fairlyActiveMinutes;
	}

	public int getVeryActiveMinutes() {
		return veryActiveMinutes;
	}

	public void setVeryActiveMinutes(int veryActiveMinutes) {
		this.veryActiveMinutes = veryActiveMinutes;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}
}
