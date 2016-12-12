package eu.ewall.platform.idss.response.ewall.activity;

import java.util.List;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import eu.ewall.platform.idss.utils.json.DateTimeFromIsoDateTimeDeserializer;

/**
 * JSON response object for the eWALL physical activity service brick.
 * 
 * @author Harm op den Akker (RRD)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityServiceBrickResponse {
	
	@JsonProperty("username")
	private String username;
	
	@JsonProperty("from")
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime startTime;
	
	@JsonProperty("to")
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime endTime;
	
	@JsonProperty("movements")
	private List<ActivityDataItem> movements;
	
	/**
	 * Returns the eWALL user name associated with this {@link ActivityServiceBrickResponse}.
	 * @return the eWALL user name associated with this {@link ActivityServiceBrickResponse}.
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Sets the eWALL user name associated with this {@link ActivityServiceBrickResponse}.
	 * @param username the eWALL user name associated with this {@link ActivityServiceBrickResponse}.
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Returns the start time of this {@link ActivityServiceBrickResponse} data interval.
	 * @return the start time of this {@link ActivityServiceBrickResponse} data interval.
	 */
	public DateTime getStartTime() {
		return startTime;
	}
	
	/**
	 * Sets the start time of this {@link ActivityServiceBrickResponse} data interval.
	 * @param startTime the start time of this {@link ActivityServiceBrickResponse} data interval.
	 */
	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}
	
	/**
	 * Returns the end time of this {@link ActivityServiceBrickResponse} data interval.
	 * @return the end time of this {@link ActivityServiceBrickResponse} data interval.
	 */
	public DateTime getEndTime() {
		return endTime;
	}
	
	/**
	 * Sets the end time of this {@link ActivityServiceBrickResponse} data interval.
	 * @param endTime the end time of this {@link ActivityServiceBrickResponse} data interval.
	 */
	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * Returns the list of {@link ActivityDataItem}s in this {@link ActivityServiceBrickResponse}.
	 * @return the list of {@link ActivityDataItem}s in this {@link ActivityServiceBrickResponse}.
	 */
	public List<ActivityDataItem> getMovements() {
		return movements;
	}
	
	/**
	 * Sets the list of {@link ActivityDataItem}s in this {@link ActivityServiceBrickResponse}.
	 * @param movements the list of {@link ActivityDataItem}s in this {@link ActivityServiceBrickResponse}.
	 */
	public void setMovements(List<ActivityDataItem> movements) {
		this.movements = movements;
	}
	
	// ---------- Convenience methods ---------- //
	
	/**
	 * Returns the total number of steps reported in this {@link ActivityServiceBrickResponse}.
	 * @return the total number of steps reported in this {@link ActivityServiceBrickResponse}.
	 */
	public int getTotalSteps() {
		int totalSteps = 0;
		for(ActivityDataItem item : movements) {
			totalSteps += item.getSteps();
		}
		return totalSteps;
	}
	
	/**
	 * Returns the total number of kilometers reported in this {@link ActivityServiceBrickResponse}.
	 * @return the total number of kilometers reported in this {@link ActivityServiceBrickResponse}.
	 */
	public float getTotalKilometers() {
		float totalKilometers = 0;
		for(ActivityDataItem item : movements) {
			totalKilometers += item.getKilometers();
		}
		return totalKilometers;
	}
	
	/**
	 * Returns the total number of burned calories reported in this {@link ActivityServiceBrickResponse}.
	 * @return the total number of burned calories reported in this {@link ActivityServiceBrickResponse}.
	 */
	public int getTotalBurnedCalories() {
		int totalBurnedCalories = 0;
		for(ActivityDataItem item : movements) {
			totalBurnedCalories += item.getBurnedCalories();
		}
		return totalBurnedCalories;
	}
}
