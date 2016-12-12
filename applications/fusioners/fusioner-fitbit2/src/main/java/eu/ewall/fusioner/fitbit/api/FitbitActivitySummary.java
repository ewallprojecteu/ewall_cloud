package eu.ewall.fusioner.fitbit.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import eu.ewall.platform.idss.utils.json.JsonObject;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FitbitActivitySummary extends JsonObject {
	private int activityCalories;
	private int caloriesBMR;
	private int caloriesOut;
	private List<Distance> distances;
	private float elevation;
	private int fairlyActiveMinutes;
	private int floors;
	private int lightlyActiveMinutes;
	private int marginalCalories;
	private int sedentaryMinutes;
	private int steps;
	private int veryActiveMinutes;

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

	public List<Distance> getDistances() {
		return distances;
	}

	public void setDistances(List<Distance> distances) {
		this.distances = distances;
	}

	public float getElevation() {
		return elevation;
	}

	public void setElevation(float elevation) {
		this.elevation = elevation;
	}

	public int getFairlyActiveMinutes() {
		return fairlyActiveMinutes;
	}

	public void setFairlyActiveMinutes(int fairlyActiveMinutes) {
		this.fairlyActiveMinutes = fairlyActiveMinutes;
	}

	public int getFloors() {
		return floors;
	}

	public void setFloors(int floors) {
		this.floors = floors;
	}

	public int getLightlyActiveMinutes() {
		return lightlyActiveMinutes;
	}

	public void setLightlyActiveMinutes(int lightlyActiveMinutes) {
		this.lightlyActiveMinutes = lightlyActiveMinutes;
	}

	public int getMarginalCalories() {
		return marginalCalories;
	}

	public void setMarginalCalories(int marginalCalories) {
		this.marginalCalories = marginalCalories;
	}

	public int getSedentaryMinutes() {
		return sedentaryMinutes;
	}

	public void setSedentaryMinutes(int sedentaryMinutes) {
		this.sedentaryMinutes = sedentaryMinutes;
	}

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public int getVeryActiveMinutes() {
		return veryActiveMinutes;
	}

	public void setVeryActiveMinutes(int veryActiveMinutes) {
		this.veryActiveMinutes = veryActiveMinutes;
	}
	
	public float findDistance(String activity) {
		for (Distance distance : distances) {
			if (distance.getActivity().equals(activity))
				return distance.getDistance();
		}
		return 0;
	}

	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Distance {
		private String activity;
		private float distance;

		public String getActivity() {
			return activity;
		}

		public void setActivity(String activity) {
			this.activity = activity;
		}

		public float getDistance() {
			return distance;
		}

		public void setDistance(float distance) {
			this.distance = distance;
		}
	}
}
