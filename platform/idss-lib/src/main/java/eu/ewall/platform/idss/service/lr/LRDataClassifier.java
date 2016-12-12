package eu.ewall.platform.idss.service.lr;

import java.util.List;

import eu.ewall.platform.idss.service.model.type.ParameterClassification;

/**
 * This classifier takes weighted averages of week pattern parameters from a
 * lifestyle reasoner and classifies them as below normal, normal or above
 * normal.
 * 
 * @author Dennis Hofs (RRD)
 * 
 * @param <T> the source data class
 */
public interface LRDataClassifier<T extends LRSourceDayData> {

	/**
	 * Classifies the specified weighted average of a week pattern parameter
	 * with respect to the specified history.
	 * 
	 * @param param the parameter
	 * @param history the history
	 * @param average the weighted average
	 * @return the classification
	 */
	ParameterClassification classify(WeekPatternParameter<T> param,
			List<T> history, float average);
}
