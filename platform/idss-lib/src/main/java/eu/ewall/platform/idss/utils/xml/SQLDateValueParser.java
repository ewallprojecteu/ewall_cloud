package eu.ewall.platform.idss.utils.xml;

import eu.ewall.platform.idss.utils.exception.ParseException;

import org.joda.time.LocalDate;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Values parser for SQL date values. It accepts strings formatted as
 * yyyy-MM-dd.
 * 
 * @author Dennis Hofs (RRD)
 */
public class SQLDateValueParser implements XMLValueParser<LocalDate> {

	@Override
	public LocalDate parse(String xml) throws ParseException {
		try {
			DateTimeFormatter parser = DateTimeFormat.forPattern("yyyy-MM-dd");
			return parser.parseLocalDate(xml);
		} catch (Exception ex) {
			throw new ParseException("Value is not an SQL date string: \"" +
					xml + "\": " + ex.getMessage(), ex);
		}
	}
}
