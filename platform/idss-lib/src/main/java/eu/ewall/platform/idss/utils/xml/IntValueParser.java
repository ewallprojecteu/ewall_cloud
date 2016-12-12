package eu.ewall.platform.idss.utils.xml;

import eu.ewall.platform.idss.utils.exception.ParseException;

/**
 * Value parser for integer values. It validates whether the input string is an
 * integer, and it can validate whether the value is within a specified range.
 * 
 * @author Dennis Hofs (RRD)
 */
public class IntValueParser implements XMLValueParser<Integer> {
	private Integer minVal = null;
	private Integer maxVal = null;

	/**
	 * Constructs a new parser without range validation.
	 */
	public IntValueParser() {
	}
	
	/**
	 * Constructs a new parser with range validation.
	 * 
	 * @param minVal the minimum value or null
	 * @param maxVal the maximum value or null
	 */
	public IntValueParser(Integer minVal, Integer maxVal) {
		this.minVal = minVal;
		this.maxVal = maxVal;
	}

	@Override
	public Integer parse(String xml) throws ParseException {
		int val;
		try {
			val = Integer.parseInt(xml);
		} catch (NumberFormatException ex) {
			throw new ParseException("Value is not an integer: \"" + xml +
					"\"", ex);
		}
		if ((minVal != null && val < minVal) ||
				(maxVal != null && val > maxVal)) {
			String error = "Integer value must be ";
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
