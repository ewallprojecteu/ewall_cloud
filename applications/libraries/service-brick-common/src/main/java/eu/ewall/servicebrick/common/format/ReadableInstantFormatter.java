package eu.ewall.servicebrick.common.format;

import java.text.ParseException;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.format.datetime.joda.DateTimeParser;
import org.springframework.format.datetime.joda.ReadableInstantPrinter;

/**
 * Formatter for Joda ReadableInstant (e.g. DateTime) that retains the time zone offset of the input string when
 * parsing, instead of replacing it with the default time zone.<br>
 * The parser accepts strings in ISO format with mandatory date, hour, minutes and time zone and optional seconds and
 * milliseconds, i.e. {@code yyyy-MM-dd'T'HH:mm[:ss[.SSS]]ZZ}.<br>
 * The printer outputs strings in ISO format with all the components specified, i.e. 
 * {@code yyyy-MM-dd'T'HH:mm:ss.SSSZZ}.
 */
public class ReadableInstantFormatter implements Formatter<ReadableInstant> {

	private static final org.joda.time.format.DateTimeFormatter ISO_FORMATTER =	ISODateTimeFormat.dateTime();
	private static final org.joda.time.format.DateTimeParser MILLIS_PARSER =
			new DateTimeFormatterBuilder()
    			.appendLiteral('.')
    			.appendFractionOfSecond(3, 3)
    			.toParser();
	private static final org.joda.time.format.DateTimeParser SECONDS_PARSER =
			new DateTimeFormatterBuilder()
    			.appendLiteral(':')
    			.appendSecondOfMinute(2)
    			.appendOptional(MILLIS_PARSER)
    			.toParser();
	private static final org.joda.time.format.DateTimeFormatter DATE_TIME_OPTIONAL_SECONDS_FORMATTER = 
			new DateTimeFormatterBuilder()
    			.append(ISODateTimeFormat.date())
    			.appendLiteral('T')
    			.append(ISODateTimeFormat.hourMinute())
    			.appendOptional(SECONDS_PARSER)
    			.appendTimeZoneOffset("Z", true, 2, 4)
    			.toFormatter()
    			.withOffsetParsed();
	
	private Printer<ReadableInstant> printer;
	private Parser<DateTime> parser;
	
	public ReadableInstantFormatter() {
		printer = new ReadableInstantPrinter(ISO_FORMATTER);
		parser = new DateTimeParser(DATE_TIME_OPTIONAL_SECONDS_FORMATTER);
	}
	
	@Override
	public String print(ReadableInstant object, Locale locale) {
		return printer.print(object, locale);
	}

	@Override
	public DateTime parse(String text, Locale locale) throws ParseException {
		return parser.parse(text, locale);
	}

}
