package eu.ewall.servicebrick.common.format;

import java.text.ParseException;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ReadableInstantFormatterTest {

	private ReadableInstantFormatter formatter;
	
	@Before
	public void setUp() {
		this.formatter = new ReadableInstantFormatter();
	}
	
	private DateTime parse(String text) {
		try {
			return formatter.parse(text, Locale.getDefault());
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	@Test
	public void testParseWithoutZone() {
		try {
			parse("2015-01-01T00:00");
			Assert.fail("An IllegalArgumentException was expected");
		} catch (IllegalArgumentException e) {}
	}
	
	@Test
	public void testParseDateHourMinute() {
		DateTime expected = new DateTime(2015, 1, 1, 1, 2, 0, 0, DateTimeZone.forOffsetHours(1));
		DateTime actual = parse("2015-01-01T01:02+01:00");
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testParseDateHourMinuteSecond() {
		DateTime expected = new DateTime(2015, 1, 1, 1, 2, 3, 0, DateTimeZone.forOffsetHours(2));
		DateTime actual = parse("2015-01-01T01:02:03+02:00");
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testParseDateHourMinuteSecondMillis() {
		DateTime expected = new DateTime(2015, 1, 1, 1, 2, 3, 4, DateTimeZone.UTC);
		DateTime actual = parse("2015-01-01T01:02:03.004Z");
		Assert.assertEquals(expected, actual);
	}
	
}
