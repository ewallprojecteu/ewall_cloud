package eu.ewall.platform.idss.service.lr;

import java.util.List;

import eu.ewall.platform.idss.service.model.type.ParameterClassification;

/**
 * This default classifier calculates the average and variance of a data set
 * and defines a normal band of values of 0.5 * SQRT(variance) below and above
 * the average of the data set.
 * 
 * @author Dennis Hofs (RRD)
 */
public class DefaultLRDataClassifier<T extends LRSourceDayData>
implements LRDataClassifier<T> {

	@Override
	public ParameterClassification classify(WeekPatternParameter<T> param,
			List<T> history, float average) {
		float[] values = getValues(param, history);
		float avg = getAverage(values);
		float variance = getVariance(avg, values);
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
	private float[] getValues(WeekPatternParameter<T> param, List<T> history) {
		float[] result = new float[history.size()];
		for (int i = 0; i < result.length; i++) {
			T dayData = history.get(i);
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
	private float getAverage(float[] values) {
		double sum = 0;
		for (double val : values) {
			sum += val;
		}
		return (float)(sum / values.length);
	}
	
	/**
	 * Returns the variance for the specified values.
	 * 
	 * @param avg the average
	 * @param values the values
	 * @return the variance
	 */
	private float getVariance(float avg, float[] values) {
		float[] diffs = new float[values.length];
		for (int i = 0; i < values.length; i++) {
			float diff = values[i] - avg;
			diffs[i] = diff * diff;
		}
		return getAverage(diffs);
	}
}
