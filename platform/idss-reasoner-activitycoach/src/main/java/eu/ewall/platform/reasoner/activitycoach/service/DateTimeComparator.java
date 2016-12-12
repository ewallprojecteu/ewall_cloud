package eu.ewall.platform.reasoner.activitycoach.service;

import java.util.Comparator;

import org.joda.time.DateTime;

/**
 * Comparator for {@link DateTime DateTime} objects, in ascending or descending
 * order. Null values always come after non-null values.
 * 
 * @author Dennis Hofs
 */
public class DateTimeComparator implements Comparator<DateTime> {
	private boolean ascending = true;
	
	/**
	 * Constructs a new comparator with ascending order.
	 */
	public DateTimeComparator() {
	}
	
	/**
	 * Constructs a new comparator.
	 * 
	 * @param ascending true for ascending order, false for descending order
	 */
	public DateTimeComparator(boolean ascending) {
		this.ascending = ascending;
	}

	@Override
	public int compare(DateTime o1, DateTime o2) {
		if (o1 == null && o2 == null)
			return 0;
		if (o1 == null)
			return 1;
		if (o2 == null)
			return -1;
		if (o1.isBefore(o2))
			return ascending ? -1 : 1;
		else if (o1.isAfter(o2))
			return ascending ? 1 : -1;
		else
			return 0;
	}
}
