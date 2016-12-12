package eu.ewall.platform.ewallet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;

/**
 * Class to read Fitbit activity data from the respective fusioner using:
 * http://localhost:8080/applications-dev/fusioner-fitbit2/movementData?username=apne
 * and then write the data to the database.
 *
 * Created by apne on 4/21/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityData extends AbstractDatabaseObject {
    @DatabaseField(value=DatabaseType.LONG, index=true)
    private long timestamp;
    @DatabaseField(value=DatabaseType.STRING, index=true)
    private String username;
    @DatabaseField(value=DatabaseType.STRING)
    private String lastRetrieveTime;
    @DatabaseField(value=DatabaseType.STRING)
    private String lastTrackerSyncTime;
    @DatabaseField(value=DatabaseType.INT)
    private int activityCalories;
    @DatabaseField(value=DatabaseType.INT)
    private int caloriesBMR;
    @DatabaseField(value=DatabaseType.INT)
    private int caloriesOut;
    private int complete;
    private String date;
    @DatabaseField(value=DatabaseType.DOUBLE)
    private double distance;
    @DatabaseField(value=DatabaseType.DOUBLE)
    private double elevation;
    @DatabaseField(value=DatabaseType.INT)
    private int fairlyActiveMinutes;
    @DatabaseField(value=DatabaseType.INT)
    private int floors;
    @DatabaseField(value=DatabaseType.INT)
    private int lightlyActiveMinutes;
    @DatabaseField(value=DatabaseType.INT)
    private int sedentaryMinutes;
    @DatabaseField(value=DatabaseType.INT)
    private int steps;
    @DatabaseField(value=DatabaseType.INT)
    private int veryActiveMinutes;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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
}
