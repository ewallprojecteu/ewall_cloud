package eu.ewall.platform.lr.physicalactivity.service;

import java.util.ArrayList;
import java.util.List;

import eu.ewall.platform.idss.service.model.type.ActivityCategory;

/**
 * The default implementation of {@link ActivityClassifier ActivityClassifier}.
 * 
 * @author Dennis Hofs (RRD)
 */
public class DefaultActivityClassifier implements ActivityClassifier {

	@Override
	public boolean isOutlier(List<DayActivity> history, int value) {
		return value == 0;
	}

	@Override
	public ActivityCategory getSingleDayCategory(List<DayActivity> history,
			int value) {
		// history is without outliers
		List<Integer> intValues = getValues(history);
		intValues.add(value);
		double[] values = toDoubleArray(intValues);
		double avg = getAverage(values);
		double variance = getVariance(avg, values);
		double stdDev = Math.sqrt(variance);
		if (value < avg - stdDev)
			return ActivityCategory.INACTIVE;
		else if (value > avg + stdDev)
			return ActivityCategory.ACTIVE;
		else
			return ActivityCategory.NORMAL;
	}
	
	/**
	 * Returns a list of all activity values in the specified history.
	 * 
	 * @param history the history (without outliers)
	 * @return the activity values (without outliers)
	 */
	private List<Integer> getValues(List<DayActivity> history) {
		List<Integer> values = new ArrayList<Integer>();
		for (DayActivity day : history) {
			values.add(day.getValue());
		}
		return values;
	}
	
	/**
	 * Converts a list of integers to a double array.
	 * 
	 * @param values the list of integers
	 * @return the double array
	 */
	private double[] toDoubleArray(List<Integer> values) {
		double[] result = new double[values.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = values.get(i);
		}
		return result;
	}

	@Override
	public ActivityCategory getAverageCategory(List<DayActivity> history,
			int average) {
		// history is without outliers
		List<Integer> intValues = getValues(history);
		double[] values = toDoubleArray(intValues);
		double avg = getAverage(values);
		double variance = getVariance(avg, values);
		double allowedDiff = 0.5 * Math.sqrt(variance);
		if (average < avg - allowedDiff)
			return ActivityCategory.INACTIVE;
		else if (average > avg + allowedDiff)
			return ActivityCategory.ACTIVE;
		else
			return ActivityCategory.NORMAL;
	}

	/**
	 * Returns the average of the specified values.
	 * 
	 * @param values the values
	 * @return the average
	 */
	private double getAverage(double[] values) {
		double sum = 0;
		for (double val : values) {
			sum += val;
		}
		return sum / values.length;
	}
	
	/**
	 * Returns the variance for the specified values.
	 * 
	 * @param avg the average
	 * @param values the values
	 * @return the variance
	 */
	private double getVariance(double avg, double[] values) {
		double[] diffs = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			double diff = values[i] - avg;
			diffs[i] = diff * diff;
		}
		return getAverage(diffs);
	}
}
