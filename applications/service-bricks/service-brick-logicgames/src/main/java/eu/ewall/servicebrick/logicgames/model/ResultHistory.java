package eu.ewall.servicebrick.logicgames.model;

import java.util.List;

import org.joda.time.DateTime;

/**
 * Lists illuminance events for a given user and possibly a location, either in a given time interval or the latest n 
 * events.
 */
public class ResultHistory {

	private List<Result> results;
	private DateTime from;
	private DateTime to;
	private Integer latestResults;

	public ResultHistory(String username, DateTime from, DateTime to, 
			List<Result> results) {
		this.from = from;
		this.to = to;
		this.results = results;
	}

	public ResultHistory(String username, Integer latestResults, 
			List<Result> results) {
		this.latestResults = latestResults;
		this.results = results;
	}

	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
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

	public Integer getLatestResults() {
		return latestResults;
	}

	public void setLatestResults(Integer latestResults) {
		this.latestResults = latestResults;
	}
	
}
