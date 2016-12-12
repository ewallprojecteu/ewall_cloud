package eu.ewall.platform.idss.wellbeingads.model.physicalactivity;

import java.io.Serializable;

import org.joda.time.DateTime;

/**
 * The Class AggregationPeriod.
 */
public class AggregationPeriod implements Serializable{

	private static final long serialVersionUID = 1L;

	/** The length. */
	private int length;
	
	/** The unit. */
	private AggregationUnit unit;
	
	
	/**
	 * Instantiates a new aggregation period.
	 */
	public AggregationPeriod() {
	}
	
	/**
	 * Instantiates a new aggregation period.
	 *
	 * @param length the length
	 * @param unit the unit
	 */
	public AggregationPeriod(int length, AggregationUnit unit) {
		this.length = length;
		this.unit = unit;
	}
	
	/**
	 * Instantiates a new aggregation period.
	 *
	 * @param period the period
	 */
	public AggregationPeriod(String period) {
		if (period == null || period.length() == 0) {
			throw new IllegalArgumentException("Null or empty string received");
		}
		int i = 0;
		while (i < period.length() && Character.isDigit(period.charAt(i))) {
			i++;
		}
		if (i == 0) {
			throw new IllegalArgumentException("Missing aggregation period length");
		} else if (i == period.length()) {
			throw new IllegalArgumentException("Missing aggregation period unit");
		}
		length = Integer.parseInt(period.substring(0, i));
		if (length <= 0) {
			throw new IllegalArgumentException("Invalid aggregation length: " + length);
		}
		unit = AggregationUnit.from(period.substring(i));
	}

	/**
	 * Gets the length.
	 *
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Gets the unit.
	 *
	 * @return the unit
	 */
	public AggregationUnit getUnit() {
		return unit;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || !(obj instanceof AggregationPeriod)) {
			return false;
		}
		AggregationPeriod other = (AggregationPeriod) obj;
		return other.length == length && other.unit == unit;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return length + unit.toString();
	}
	
	/**
	 * Returns the given date plus this period.
	 *
	 * @param date the date
	 * @return the given date plus this period
	 */
	public DateTime addTo(DateTime date) {
		if (unit.equals(AggregationUnit.HOUR)) {
			return date.plusHours(length);
		} else if (unit.equals(AggregationUnit.DAY)) {
			return date.plusDays(length);
		} else if (unit.equals(AggregationUnit.WEEK)) {
			return date.plusWeeks(length);
		} else if (unit.equals(AggregationUnit.MONTH)) {
			return date.plusMonths(length);
		} else {
			throw new IllegalArgumentException("Unsupported aggregation unit: " + unit);
		}
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
}
