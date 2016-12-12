package eu.ewall.platform.userinteractionlogger.protocol;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.joda.time.DateTime;

import eu.ewall.platform.userinteractionlogger.model.DailyInteractionLog;

public class ResponseDailyInteractions {
	
	private String username;
	private List<DailyInteractionLog> dailyInteractions;
	
	// ---------- Constructors ---------- //
	
	public ResponseDailyInteractions() {
		dailyInteractions = new ArrayList<DailyInteractionLog>();
	}
	
	public ResponseDailyInteractions(String username, List<DailyInteractionLog> dailyInteractions) {
		this.username = username;
		this.dailyInteractions = dailyInteractions;
	}
	
	// ---------- Getters ---------- //
	
	public String getUsername() {
		return username;
	}
	
	public List<DailyInteractionLog> getDailyInteractions() {
		return dailyInteractions;
	}
	
	// ---------- Setters ---------- //
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setDailyInteractions(LinkedHashMap<DateTime,Integer> dailyInteractions) {
		
	}
}
