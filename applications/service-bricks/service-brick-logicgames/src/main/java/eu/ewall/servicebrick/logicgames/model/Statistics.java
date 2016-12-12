package eu.ewall.servicebrick.logicgames.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;

/**
 * 
 */
public class Statistics {

	public static enum DBAggregation {
		NONE,
		DAY,
		MONTH,
		YEAR
	}

	private String username;
	private DateTime from;
	private DateTime to;
	private DBAggregation aggregation;
	//private List<Statistic> statistics = new ArrayList<Statistic>();
	private HashMap<String, List<StatisticItem>> statistics = new HashMap<String, List<StatisticItem>>(); 
	
	public Statistics(String username, DateTime from, DateTime to, DBAggregation aggregation, HashMap<String, List<StatisticItem>> statistics) {
		this.username = username;
		this.from = from;
		this.to = to;
		this.statistics = statistics;
	}

	public Statistics(String username, DateTime from, DateTime to, DBAggregation aggregation) {
		this(username, from, to, aggregation , new HashMap<String, List<StatisticItem>>());
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public DateTime getFrom() {
		return from;
	}

	public void setFrom(DateTime from) {
		this.from = from;
	}

	public DateTime getTo() {
		return to;
	}

	public void setTo(DateTime to) {
		this.to = to;
	}

	public HashMap<String, List<StatisticItem>> getStatistics() {
		return statistics;
	}

	public void setStatistics(HashMap<String, List<StatisticItem>> statistics) {
		this.statistics = statistics;
	}


	public void putStatistic(String key, List<StatisticItem> statistic){
		statistics.put(key, statistic);
	}

	public DBAggregation getAggregation() {
		return aggregation;
	}

	public void setAggregation(DBAggregation aggregation) {
		this.aggregation = aggregation;
	}
	
}
