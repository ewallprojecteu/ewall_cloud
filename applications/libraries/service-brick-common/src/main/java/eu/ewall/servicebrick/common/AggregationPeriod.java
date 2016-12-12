package eu.ewall.servicebrick.common;

import org.joda.time.DateTime;

public class AggregationPeriod {

	private int length;
	private AggregationUnit unit;
	
	
	public AggregationPeriod() {
		// TODO Auto-generated constructor stub
	}
	
	public AggregationPeriod(int length, AggregationUnit unit) {
		this.length = length;
		this.unit = unit;
	}
	
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

	public int getLength() {
		return length;
	}

	public AggregationUnit getUnit() {
		return unit;
	}
	
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

	@Override
	public String toString() {
		return length + unit.toString();
	}
	
	/**
	 * Returns the given date plus this period
	 * 
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
	
}
