package eu.ewall.platform.lr.sleepmonitor.service;

import java.util.List;

import eu.ewall.platform.idss.service.model.type.ParameterClassification;

/**
 * This class can classify sleep parameters as below normal, normal or above
 * normal.
 * 
 * @author Dennis Hofs (RRD)
 */
public class SleepParameterClassifier {

	/**
	 * Classifies the specified weighted average of a sleep parameter with
	 * respect to the specified history.
	 * 
	 * @param param the sleep parameter
	 * @param history the history
	 * @param average the weighted average
	 * @return the classification
	 */
	public ParameterClassification getAverageCategory(SleepParameter param,
			List<NightSleepData> history, int average) {
		double[] values = getValues(param, history);
		double avg = getAverage(values);
		double variance = getVariance(avg, values);
		double allowedDiff = 0.5 * Math.sqrt(variance);
		if (average < avg - allowedDiff)
			return ParameterClassification.BELOW_NORMAL;
		else if (average > avg + allowedDiff)
			return ParameterClassification.ABOVE_NORMAL;
		else
			return ParameterClassification.NORMAL;
	}

	/**
	 * Calculates the value of the specified parameter for all items in the
	 * history.
	 * 
	 * @param history the history
	 * @return the values
	 */
	private double[] getValues(SleepParameter param,
			List<NightSleepData> history) {
		double[] result = new double[history.size()];
		for (int i = 0; i < result.length; i++) {
			NightSleepData dayData = history.get(i);
			result[i] = param.calculate(dayData);
		}
		return result;
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
