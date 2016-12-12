package eu.ewall.fusioner.fitbit.model;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;

public class ActivityEvent extends AbstractDatabaseObject {

	private String user;
	private String date;
	private int activityCalories;
	private int caloriesBMR;
	private int caloriesOut;
	private float distance;
	private float elevation;
	private int floors;
	private int steps;
	private int sedentaryMinutes;
	private int lightlyActiveMinutes;
	private int fairlyActiveMinutes;
	private int veryActiveMinutes;
	private int complete;

	
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

	public int isComplete() {
		return complete;
	}

	public void setComplete(int complete) {
		this.complete = complete;
	}
}
