package eu.ewall.platform.idss.response.ewall.coins;

import org.joda.time.DateTime;

public class EWalletData {
	
	private String username;
	private long timestamp;
	private DateTime isoTime;
	private double currentCoins;
	private double coinsFromActivity;
	private double coinsFromSleep;
	private double coinsFromExercise;
	private double coinsFromGames;
	private double coinsFromUsage;
	
	
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

    public double getCoinsFromActivity() {
        return coinsFromActivity;
    }

    public void setCoinsFromActivity(double coinsFromActivity) {
        this.coinsFromActivity = coinsFromActivity;
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

    public double getCoinsFromGames() {
        return coinsFromGames;
    }

    public void setCoinsFromGames(double coinsFromGames) {
        this.coinsFromGames = coinsFromGames;
    }

    public double getCoinsFromUsage() {
        return coinsFromUsage;
    }

    public void setCoinsFromUsage(double coinsFromUsage) {
        this.coinsFromUsage = coinsFromUsage;
    }
}
