package eu.ewall.servicebrick.physicalactivity.model;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import eu.ewall.servicebrick.common.AggregationPeriod;

/**
 * Contains the movement data (steps, kilometers and burned calories) aggregated on the given time interval.
 * The userId and aggregation fields are required only when persisting to the database and may be left null
 * when the movement is inserted in a container such as MovementHistory where userId and aggregation are
 * specified.
 */
@Document(collection="fitbit_movements")
public class FitBit_Movement implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// The id is used for saving the mongo _id only and not exposed externally at the moment
	@Id
	private ObjectId id;
	
	private String username;
	private DateTime from;
	private DateTime to;
	private AggregationPeriod aggregation;
	private long steps;
	private double kilometers;
	private double burnedCalories;
	
	public FitBit_Movement(DateTime from, DateTime to) {
		this(null, from, to);
	}
	
	@PersistenceConstructor
	public FitBit_Movement(String username, DateTime from, DateTime to) {
		this.username = username;
		this.from = from;
		this.to = to;
	}
	

	// Don't include in output as the username is already stated in MovementHistory
	@JsonIgnore
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
	
	// Don't include in output as the aggregation is already stated in MovementHistory
	@JsonIgnore
	public AggregationPeriod getAggregation() {
		return aggregation;
	}

	public void setAggregation(AggregationPeriod aggregation) {
		this.aggregation = aggregation;
	}

	public long getSteps() {
		return steps;
	}

	public void setSteps(long steps) {
		this.steps = steps;
	}

	public void addSteps(long steps) {
		this.steps += steps;
	}
	
	public double getKilometers() {
		return kilometers;
	}

	public void setKilometers(double kilometers) {
		this.kilometers = kilometers;
	}

	public void addKilometers(double kilometers) {
		this.kilometers += kilometers;
	}
	
	public double getBurnedCalories() {
		return burnedCalories;
	}

	public void setBurnedCalories(double burnedCalories) {
		this.burnedCalories = burnedCalories;
	}
	
	public void addBurnedCalories(double burnedCalories) {
		this.burnedCalories += burnedCalories;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || !(obj instanceof FitBit_Movement)) {
			return false;
		}
		FitBit_Movement other = (FitBit_Movement) obj;
		if (other.id == null && id != null || other.id != null && !other.id.equals(id)) {
			return false;
		} else if (other.username == null && username != null || other.username != null && !other.username.equals(username)) {
			return false;
		} else if (!other.from.equals(from)) {
			return false;
		} else if (!other.to.equals(to)) {
			return false;
		} else if (other.aggregation == null && aggregation != null 
				|| other.aggregation != null && !other.aggregation.equals(aggregation)) {
			return false;
		} else if (other.steps != steps) {
			return false;
		} else if (other.kilometers != kilometers) {
			return false;
		} else if (other.burnedCalories != burnedCalories) {
			return false;
		}
		return true;
	}
	
}
