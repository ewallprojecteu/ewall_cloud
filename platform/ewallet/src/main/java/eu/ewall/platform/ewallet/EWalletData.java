package eu.ewall.platform.ewallet;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.utils.json.DateTimeFromIsoDateTimeDeserializer;
import eu.ewall.platform.idss.utils.json.IsoDateTimeSerializer;
import org.joda.time.DateTime;

/**
 * Example data object. Data objects to store in the database should extend
 * {@link AbstractDatabaseObject AbstractDatabaseObject}. Each field should be
 * annotated with @DatabaseField. Provide public get/set methods for each
 * private field.
 *
 * <p>Data objects that are returned by a REST endpoint should support JSON
 * mapping. You may need to add annotations @JsonSerialize and @JsonDeserialize
 * to fields with data types that are not supported or not mapped correctly
 * by the Jackson ObjectMapper.</p>
 *
 * Created by apne on 4/21/2016.
 */
public class EWalletData extends AbstractDatabaseObject {
    @DatabaseField(value=DatabaseType.STRING, index=true)
    private String username;

    // Store times as LONG if you need queries with greater than/less than
    // or queries with sorting by time.
    // ISOTIME does not work for that because of time zones
    @DatabaseField(value=DatabaseType.LONG, index=true)
    private long timestamp;

    @DatabaseField(value=DatabaseType.ISOTIME)
    @JsonSerialize(using=IsoDateTimeSerializer.class)
    @JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
    private DateTime isoTime;

    @DatabaseField(value=DatabaseType.DOUBLE)
    private double currentCoins = 0;

    @DatabaseField(value=DatabaseType.DOUBLE)
    private double coinsFromActivity = 0;

    @DatabaseField(value=DatabaseType.DOUBLE)
    private double coinsFromSleep = 0;

    @DatabaseField(value=DatabaseType.DOUBLE)
    private double coinsFromExercise = 0;

    @DatabaseField(value=DatabaseType.DOUBLE)
    private double coinsFromGames = 0;

    @DatabaseField(value=DatabaseType.DOUBLE)
    private double coinsFromUsage = 0;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public DateTime getIsoTime() {
        return isoTime;
    }

    public void setIsoTime(DateTime isoTime) {
        this.isoTime = isoTime;
    }

    public double getCurrentCoins() {
        return currentCoins;
    }

    public void setCurrentCoins(double currentCoins) {
        this.currentCoins = currentCoins;
    }

    public void addToCurrentCoins(double currentCoins) {
        this.currentCoins += currentCoins;
    }

    public double getCoinsFromActivity() {
        return coinsFromActivity;
    }

    public void setCoinsFromActivity(double coinsFromActivity) {
        this.coinsFromActivity = coinsFromActivity;
    }

    public void addToCoinsFromActivity(double coinsFromActivity) {
        this.coinsFromActivity += coinsFromActivity;
    }

    public double getCoinsFromSleep() {
        return coinsFromSleep;
    }

    public void setCoinsFromSleep(double coinsFromSleep) {
        this.coinsFromSleep = coinsFromSleep;
    }

    public void addToCoinsFromSleep(double coinsFromSleep) {
        this.coinsFromSleep += coinsFromSleep;
    }

    public double getCoinsFromExercise() {
        return coinsFromExercise;
    }

    public void setCoinsFromExercise(double coinsFromExercise) {
        this.coinsFromExercise = coinsFromExercise;
    }

    public void addToCoinsFromExercise(double coinsFromExercise) {
        this.coinsFromExercise += coinsFromExercise;
    }

    public double getCoinsFromGames() {
        return coinsFromGames;
    }

    public void setCoinsFromGames(double coinsFromGames) {
        this.coinsFromGames = coinsFromGames;
    }

    public void addToCoinsFromGames(double coinsFromGames) {
        this.coinsFromGames += coinsFromGames;
    }

    public double getCoinsFromUsage() {
        return coinsFromUsage;
    }

    public void setCoinsFromUsage(double coinsFromUsage) {
        this.coinsFromUsage = coinsFromUsage;
    }

    public void addToCoinsFromUsage(double coinsFromUsage) {
        this.coinsFromUsage += coinsFromUsage;
    }
}