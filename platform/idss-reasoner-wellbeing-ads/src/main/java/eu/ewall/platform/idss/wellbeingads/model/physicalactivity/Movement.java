package eu.ewall.platform.idss.wellbeingads.model.physicalactivity;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Contains the movement data (steps, kilometers and burned calories) aggregated on the given time interval.
 * The userId and aggregation fields are required only when persisting to the database and may be left null
 * when the movement is inserted in a container such as MovementHistory where userId and aggregation are
 * specified.
 */
@Document(collection="movements")
public class Movement implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	@Id
	private ObjectId id;
	
	/** The username. */
	private String username;
	
	/** The from. */
	private DateTime from;
	
	/** The to. */
	private DateTime to;
	
	/** The aggregation. */
	private AggregationPeriod aggregation;
	
	/** The steps. */
	private long steps;
	
	/** The kilometers. */
	private double kilometers;
	
	/** The burned calories. */
	private double burnedCalories;
	
	/**
	 * Instantiates a new movement.
	 *
	 * @param from the from
	 * @param to the to
	 */
	public Movement(DateTime from, DateTime to) {
		this(null, from, to);
	}
	
	/**
	 * Instantiates a new movement.
	 *
	 * @param username the username
	 * @param from the from
	 * @param to the to
	 */
	@PersistenceConstructor
	public Movement(String username, DateTime from, DateTime to) {
		this.username = username;
		this.from = from;
		this.to = to;
	}
	

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	// Don't include in output as the username is already stated in MovementHistory
	@JsonIgnore
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 *
	 * @param username the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the from.
	 *
	 * @return the from
	 */
	public DateTime getFrom() {
		return from;
	}

	/**
	 * Sets the from.
	 *
	 * @param from the new from
	 */
	public void setFrom(DateTime from) {
		this.from = from;
	}

	/**
	 * Gets the to.
	 *
	 * @return the to
	 */
	public DateTime getTo() {
		return to;
	}

	/**
	 * Sets the to.
	 *
	 * @param to the new to
	 */
	public void setTo(DateTime to) {
		this.to = to;
	}
	
	/**
	 * Gets the aggregation.
	 *
	 * @return the aggregation
	 */
	// Don't include in output as the aggregation is already stated in MovementHistory
	@JsonIgnore
	public AggregationPeriod getAggregation() {
		return aggregation;
	}

	/**
	 * Sets the aggregation.
	 *
	 * @param aggregation the new aggregation
	 */
	public void setAggregation(AggregationPeriod aggregation) {
		this.aggregation = aggregation;
	}

	/**
	 * Gets the steps.
	 *
	 * @return the steps
	 */
	public long getSteps() {
		return steps;
	}

	/**
	 * Sets the steps.
	 *
	 * @param steps the new steps
	 */
	public void setSteps(long steps) {
		this.steps = steps;
	}

	/**
	 * Adds the steps.
	 *
	 * @param steps the steps
	 */
	public void addSteps(long steps) {
		this.steps += steps;
	}
	
	/**
	 * Gets the kilometers.
	 *
	 * @return the kilometers
	 */
	public double getKilometers() {
		return kilometers;
	}

	/**
	 * Sets the kilometers.
	 *
	 * @param kilometers the new kilometers
	 */
	public void setKilometers(double kilometers) {
		this.kilometers = kilometers;
	}

	/**
	 * Adds the kilometers.
	 *
	 * @param kilometers the kilometers
	 */
	public void addKilometers(double kilometers) {
		this.kilometers += kilometers;
	}
	
	/**
	 * Gets the burned calories.
	 *
	 * @return the burned calories
	 */
	public double getBurnedCalories() {
		return burnedCalories;
	}

	/**
	 * Sets the burned calories.
	 *
	 * @param burnedCalories the new burned calories
	 */
	public void setBurnedCalories(double burnedCalories) {
		this.burnedCalories = burnedCalories;
	}
	
	/**
	 * Adds the burned calories.
	 *
	 * @param burnedCalories the burned calories
	 */
	public void addBurnedCalories(double burnedCalories) {
		this.burnedCalories += burnedCalories;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || !(obj instanceof Movement)) {
			return false;
		}
		Movement other = (Movement) obj;
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

	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
}
