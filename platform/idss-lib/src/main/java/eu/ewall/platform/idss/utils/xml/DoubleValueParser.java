package eu.ewall.platform.idss.utils.xml;

import eu.ewall.platform.idss.utils.exception.ParseException;

/**
 * Value parser for double values. It validates whether the input string is a
 * double, and it can validate whether the value is within a specified range.
 * 
 * @author Dennis Hofs (RRD)
 */
public class DoubleValueParser implements XMLValueParser<Double> {
	private Double minVal;
	private Double maxVal;
	
	/**
	 * Constructs a new parser without range validation.
	 */
	public DoubleValueParser() {
	}
	
	/**
	 * Constructs a new parser with range validation.
	 * 
	 * @param minVal the minimum value or null
	 * @param maxVal the maximum value or null
	 */
	public DoubleValueParser(Double minVal, Double maxVal) {
		this.minVal = minVal;
		this.maxVal = maxVal;
	}

	@Override
	public Double parse(String xml) throws ParseException {
		double val;
		try {
			val = Double.parseDouble(xml);
		} catch (NumberFormatException ex) {
			throw new ParseException("Value is not a double: \"" + xml +
					"\"", ex);
		}
		if ((minVal != null && val < minVal) ||
				(maxVal != null && val > maxVal)) {
			String error = "Double value must be ";
			if (maxVal == null)
				error += "at least " + minVal;
			else if (minVal == null)
				error += "at most " + maxVal;
			else
				error += "between " + minVal + " and " + maxVal;
			error += ": " + val;
			throw new ParseException(error);
		}
		return val;
	}
}
