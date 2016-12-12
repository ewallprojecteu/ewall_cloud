package eu.ewall.platform.idss.utils.xml;

import eu.ewall.platform.idss.utils.exception.ParseException;

import org.joda.time.LocalTime;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Values parser for SQL time values. It accepts strings formatted as HH:mm:ss.
 * 
 * @author Dennis Hofs (RRD)
 */
public class SQLTimeValueParser implements XMLValueParser<LocalTime> {

	@Override
	public LocalTime parse(String xml) throws ParseException {
		try {
			DateTimeFormatter formatter = DateTimeFormat.forPattern(
					"HH:mm:ss");
			return formatter.parseLocalTime(xml);
		} catch (Exception ex) {
			throw new ParseException("Value is not an SQL time string: \"" +
					xml + "\": " + ex.getMessage(), ex);
		}
	}
}
