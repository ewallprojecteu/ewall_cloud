package eu.ewall.fusioner.fitbit.model;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;


import java.io.Serializable;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;



public class MovementUpdate implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// The id is using for saving the mongo _id only and not exposed externally at the moment
	@Id
	private ObjectId id;
	

	private String username;
	private String lastRetrieveTime;
	private String lastTrackerSyncTime;
	private int activityCalories;
	private int caloriesBMR;
	private int caloriesOut;
	private int complete;
	private String date;
	private double distance;
	private double elevation;
	private int fairlyActiveMinutes;
	private int floors;
	private int lightlyActiveMinutes;
	private int sedentaryMinutes;
	private int steps;
	private int veryActiveMinutes;
	
	
	
	
	@PersistenceConstructor
	public MovementUpdate( ) {
		
		
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLastRetrieveTime() {
		return lastRetrieveTime;
	}

	public void setLastRetrieveTime(String lastRetrieveTime) {
		this.lastRetrieveTime = lastRetrieveTime;
	}

	
	public String getLastTrackerSyncTime() {
		return lastTrackerSyncTime;
	}

	public void setLastTrackerSyncTime(String lastTrackerSyncTime) {
		this.lastTrackerSyncTime = lastTrackerSyncTime;
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
	
	public int getComplete() {
		return complete;
	}

	public void setComplete(int complete) {
		this.complete = complete;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	
	public double getElevation() {
		return elevation;
	}

	public void setElevation(double elevation) {
		this.elevation = elevation;
	}
	
	public int getFairlyActiveMinutes() {
		return fairlyActiveMinutes;
	}

	public void setFairlyActiveMinutes(int fairlyActiveMinutes) {
		this.fairlyActiveMinutes = fairlyActiveMinutes;
	}
	
	public int getLightlyActiveMinutes() {
		return lightlyActiveMinutes;
	}

	public void setLightlyActiveMinutes(int lightlyActiveMinutes) {
		this.lightlyActiveMinutes = lightlyActiveMinutes;
	}
	
	public int getSedentaryMinutes() {
		return sedentaryMinutes;
	}

	public void setSedentaryMinutes(int sedentaryMinutes) {
		this.sedentaryMinutes = sedentaryMinutes;
	}
	
	public int getVeryActiveMinutes() {
		return veryActiveMinutes;
	}

	public void setVeryActiveMinutes(int veryActiveMinutes) {
		this.veryActiveMinutes = veryActiveMinutes;
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
	
	

}
