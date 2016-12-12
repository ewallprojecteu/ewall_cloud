package eu.ewall.servicebrick.physicalactivity.model;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

import eu.ewall.servicebrick.common.AggregationPeriod;

/**
 * Contains data about end-user physical movements in a given time frame, aggregated with the given granularity.
 */
public class FitBit_MovementHistory implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String username;
	private DateTime from;
	private DateTime to;
	private AggregationPeriod aggregation;
	private List<Movement> movements;
	
	public FitBit_MovementHistory(String username, DateTime from, DateTime to, AggregationPeriod aggregation, 
			List<Movement> movements) {
		this.username = username;
		this.from = from;
		this.to = to;
		this.aggregation = aggregation;
		this.movements = movements;
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

	public AggregationPeriod getAggregation() {
		return aggregation;
	}

	public void setAggregation(AggregationPeriod aggregation) {
		this.aggregation = aggregation;
	}

	public List<Movement> getMovements() {
		return movements;
	}

	public void setMovements(List<Movement> movements) {
		this.movements = movements;
	}
	
}
