package eu.ewall.platform.idss.utils.xml;

import eu.ewall.platform.idss.utils.exception.ParseException;

/**
 * Value parser for string values. It just returns the input string, but it can
 * validate the string length.
 * 
 * @author Dennis Hofs (RRD)
 */
public class StringValueParser implements XMLValueParser<String> {
	private int minLen = 0;
	private int maxLen = -1;
	
	/**
	 * Constructs a new parser without string length validation.
	 */
	public StringValueParser() {
	}
	
	/**
	 * Constructs a new parser with string length validation.
	 * 
	 * @param minLen the minimum length (0 if no minimum)
	 * @param maxLen the maximum length (negative if no maximum)
	 */
	public StringValueParser(int minLen, int maxLen) {
		this.minLen = minLen;
		this.maxLen = maxLen;
	}

	@Override
	public String parse(String xml) throws ParseException {
		if (xml.length() < minLen ||
				(maxLen >= 0 && xml.length() > maxLen)) {
			String error = "String length must be ";
			if (maxLen < 0)
				error += "at least " + minLen;
			else if (minLen <= 0)
				error += "at most " + maxLen;
			else
				error += "between " + minLen + " and " + maxLen;
			error += ": \"" + xml + "\"";
			throw new ParseException(error);
		}
		return xml;
	}
}
