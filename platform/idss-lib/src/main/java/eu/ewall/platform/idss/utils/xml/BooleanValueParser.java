package eu.ewall.platform.idss.utils.xml;

import eu.ewall.platform.idss.utils.exception.ParseException;

/**
 * Values parser for boolean values. It accepts strings "0" or "false" for
 * false, and "1" or "true" for true. The string comparison is
 * case-insensitive.
 * 
 * @author Dennis Hofs (RRD)
 */
public class BooleanValueParser implements XMLValueParser<Boolean> {

	@Override
	public Boolean parse(String xml) throws ParseException {
		String lower = xml.toLowerCase();
		if (lower.equals("0") || lower.equals("false"))
			return false;
		else if (lower.equals("1") || lower.equals("true"))
			return true;
		throw new ParseException("Value is not a boolean: \"" + xml + "\"");
	}
}
