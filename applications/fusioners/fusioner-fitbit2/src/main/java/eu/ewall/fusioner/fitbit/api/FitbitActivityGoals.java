package eu.ewall.fusioner.fitbit.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import eu.ewall.platform.idss.utils.json.JsonObject;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FitbitActivityGoals extends JsonObject {
	private Integer activeMinutes;
	private Integer caloriesOut;
	private Float distance;
	private Integer floors;
	private Integer steps;

	public Integer getActiveMinutes() {
		return activeMinutes;
	}

	public void setActiveMinutes(Integer activeMinutes) {
		this.activeMinutes = activeMinutes;
	}

	public Integer getCaloriesOut() {
		return caloriesOut;
	}

	public void setCaloriesOut(Integer caloriesOut) {
		this.caloriesOut = caloriesOut;
	}

	public Float getDistance() {
		return distance;
	}

	public void setDistance(Float distance) {
		this.distance = distance;
	}

	public Integer getFloors() {
		return floors;
	}

	public void setFloors(Integer floors) {
		this.floors = floors;
	}

	public Integer getSteps() {
		return steps;
	}

	public void setSteps(Integer steps) {
		this.steps = steps;
	}
}
