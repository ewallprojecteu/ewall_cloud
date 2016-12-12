package eu.ewall.platform.idss.response.ewall.activity;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import eu.ewall.platform.idss.utils.json.DateTimeFromIsoDateTimeDeserializer;

/**
 * JSON response object containing activity data as part of the {@link ActivityServiceBrickResponse} object.
 * 
 * @author Harm op den Akker (RRD)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityDataItem {
	
	@JsonProperty("from")
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime startTime;
	
	@JsonProperty("to")
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime endTime;
	
	@JsonProperty("steps")
	private int steps;
	
	@JsonProperty("kilometers")
	private float kilometers;

	@JsonProperty("burnedCalories")
	private int burnedCalories;
	
	/**
	 * Returns the start time of this {@link ActivityDataItem} data interval.
	 * @return the start time of this {@link ActivityDataItem} data interval.
	 */
	public DateTime getStartTime() {
		return startTime;
	}
	
	/**
	 * Sets the start time of this {@link ActivityDataItem} data interval.
	 * @param startTime the start time of this {@link ActivityDataItem} data interval.
	 */
	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}
	
	/**
	 * Returns the end time of this {@link ActivityDataItem} data interval.
	 * @return the end time of this {@link ActivityDataItem} data interval.
	 */
	public DateTime getEndTime() {
		return endTime;
	}
	
	/**
	 * Sets the end time of this {@link ActivityDataItem} data interval.
	 * @param endTime the end time of this {@link ActivityDataItem} data interval.
	 */
	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * Returns the number of steps within the start- and end-times of this {@link ActivityDataItem}.
	 * @return the number of steps within the start- and end-times of this {@link ActivityDataItem}.
	 */
	public int getSteps() {
		return steps;
	}
	
	/**
	 * Sets the number of steps within the start- and end-times of this {@link ActivityDataItem}.
	 * @param steps the number of steps within the start- and end-times of this {@link ActivityDataItem}.
	 */
	public void setSteps(int steps) {
		this.steps = steps;
	}
	
	/**
	 * Returns the number of kilometers walked within the start- and end-times of this {@link ActivityDataItem}.
	 * @return the number of kilometers walked within the start- and end-times of this {@link ActivityDataItem}.
	 */
	public float getKilometers() {
		return kilometers;
	}
	
	/**
	 * Sets the number of kilometers walked within the start- and end-times of this {@link ActivityDataItem}.
	 * @param kilometers the number of kilometers walked within the start- and end-times of this {@link ActivityDataItem}.
	 */
	public void setKilometers(float kilometers) {
		this.kilometers = kilometers;
	}
	
	/**
	 * Returns the number of calories burned within the start- and end-times of this {@link ActivityDataItem}.
	 * @return the number of calories burned within the start- and end-times of this {@link ActivityDataItem}.
	 */
	public int getBurnedCalories() {
		return burnedCalories;
	}
	
	/**
	 * Sets the number of calories burned within the start- and end-times of this {@link ActivityDataItem}.
	 * @param burnedCalories the number of calories burned within the start- and end-times of this {@link ActivityDataItem}.
	 */
	public void setBurnedCalories(int burnedCalories) {
		this.burnedCalories = burnedCalories;
	}
}
