package eu.ewall.servicebrick.logicgames.model;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StatisticItem implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private DateTime date;
	private Integer rounds;
	private Integer timeSpent;
	private Integer abandoned;
	private Integer completed;
	private Double avgPerformance = 0d;
	
	@JsonCreator
	public StatisticItem(@JsonProperty("date") DateTime date,
			@JsonProperty("rounds") Integer rounds, @JsonProperty("timeSpent") Integer timeSpent,					
			@JsonProperty("abandoned") Integer abandoned, @JsonProperty("completed") Integer completed,
			@JsonProperty("avgPerformance") Double avgPerformance
			) {
		this.date = date;
		this.rounds = rounds;
		this.timeSpent = timeSpent;
		this.abandoned = abandoned;
		this.completed = completed;
		this.avgPerformance = avgPerformance;
	}


	public Integer getTimeSpent() {
		return timeSpent;
	}

	public void setTimeSpent(Integer timeSpent) {
		this.timeSpent = timeSpent;
	}

	public Integer getRounds() {
		return rounds;
	}

	public void setRounds(Integer rounds) {
		this.rounds = rounds;
	}


	public DateTime getDate() {
		return date;
	}


	public void setDate(DateTime date) {
		this.date = date;
	}


	public Integer getAbandoned() {
		return abandoned;
	}


	public void setAbandoned(Integer abandoned) {
		this.abandoned = abandoned;
	}


	public Integer getCompleted() {
		return completed;
	}


	public void setCompleted(Integer completed) {
		this.completed = completed;
	}


	public Double getAvgPerformance() {
		return avgPerformance;
	}


	public void setAvgPerformance(Double avgPerformance) {
		this.avgPerformance = avgPerformance;
	}




}
