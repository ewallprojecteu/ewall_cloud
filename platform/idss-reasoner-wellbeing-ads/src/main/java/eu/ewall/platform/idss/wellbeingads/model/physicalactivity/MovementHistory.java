package eu.ewall.platform.idss.wellbeingads.model.physicalactivity;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

/**
 * Contains data about end-user physical movements in a given time frame,
 * aggregated with the given granularity.
 */
public class MovementHistory implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The username. */
	private String username;

	/** The from. */
	private DateTime from;

	/** The to. */
	private DateTime to;

	/** The aggregation. */
	private AggregationPeriod aggregation;

	/** The movements. */
	private List<Movement> movements;

	/**
	 * Instantiates a new movement history.
	 *
	 * @param username
	 *            the username
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param aggregation
	 *            the aggregation
	 * @param movements
	 *            the movements
	 */
	public MovementHistory(String username, DateTime from, DateTime to, AggregationPeriod aggregation,
			List<Movement> movements) {
		this.username = username;
		this.from = from;
		this.to = to;
		this.aggregation = aggregation;
		this.movements = movements;
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 *
	 * @param username
	 *            the new username
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
	 * @param from
	 *            the new from
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
	 * @param to
	 *            the new to
	 */
	public void setTo(DateTime to) {
		this.to = to;
	}

	/**
	 * Gets the aggregation.
	 *
	 * @return the aggregation
	 */
	public AggregationPeriod getAggregation() {
		return aggregation;
	}

	/**
	 * Sets the aggregation.
	 *
	 * @param aggregation
	 *            the new aggregation
	 */
	public void setAggregation(AggregationPeriod aggregation) {
		this.aggregation = aggregation;
	}

	/**
	 * Gets the movements.
	 *
	 * @return the movements
	 */
	public List<Movement> getMovements() {
		return movements;
	}

	/**
	 * Sets the movements.
	 *
	 * @param movements
	 *            the new movements
	 */
	public void setMovements(List<Movement> movements) {
		this.movements = movements;
	}

}
