package eu.ewall.platform.idss.utils.datetime;

import eu.ewall.platform.idss.service.model.type.DayPart;
import eu.ewall.platform.idss.utils.exception.ParseException;

import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * This class contains various utility methods related to date and time.
 * 
 * @author Dennis Hofs (RRD)
 */
public class DateTimeUtils {
	
	/**
	 * Parses a date/time string and returns a date/time object of the
	 * specified class. We distinguish three types of date/time classes:
	 * UTC time, time with time zone, local date/time. The supported date/time
	 * classes are:
	 * 
	 * <p><ul>
	 * <li>long/Long (UNIX timestamp in milliseconds, UTC time)</li>
	 * <li>{@link Date Date} (UTC time)</li>
	 * <li>{@link Instant Instant} (UTC time)</li>
	 * <li>{@link Calendar Calendar} (with time zone)</li>
	 * <li>{@link DateTime DateTime} (with time zone)</li>
	 * <li>{@link LocalDate LocalDate}</li>
	 * <li>{@link LocalTime LocalTime}</li>
	 * <li>{@link LocalDateTime LocalDateTime}</li>
	 * </ul></p>
	 * 
	 * <p>It depends on the string format what class can be returned. This is
	 * detailed below. Supported string formats:</p>
	 *  
	 * <p><ul>
	 * <li>
	 * UNIX timestamp
	 * <p><ul>
	 * <li>long/Long, Date, Instant</li>
	 * <li>Calendar, DateTime: the timestamp is translated to the default time
	 * zone.</li>
	 * <li>LocalDate, LocalTime, LocalDateTime: the timestamp is translated to
	 * the default time zone before creating the local date/time.</li>
	 * </ul></p>
	 * </li>
	 * 
	 * <li>
	 * SQL date: yyyy-MM-dd
	 * <p><ul>
	 * <li>LocalDate</li>
	 * </ul></p>
	 * </li>
	 * 
	 * <li>
	 * SQL time: HH:mm:ss
	 * <p><ul>
	 * <li>LocalTime</li>
	 * </ul></p>
	 * </li>
	 * 
	 * <li>
	 * SQL datetime: yyyy-MM-dd HH:mm:ss
	 * <p><ul>
	 * <li>LocalDateTime</li>
	 * </ul></p>
	 * </li>
	 * 
	 * <li>
	 * any ISO date/time accepted by {@link ISODateTimeFormat#dateTimeParser()
	 * ISODateTimeFormat.dateTimeParser()}
	 * <p><ul>
	 * <li>long/Long, Date, Instant. If no time zone is given in the string, it
	 * interprets the date/time with the default time zone. If the date/time
	 * does not exist in the time zone (because of a DST change), this method
	 * throws an exception. These classes store UTC times, so any specified
	 * time zone is eventually lost. Note that a string with only a date is a
	 * valid ISO date, but this method considers it an SQL date so the result
	 * must be a LocalDate.</li>
	 * <li>Calendar, DateTime. The same as the UTC times except that any
	 * specified time zone is preserved in the result.</li>
	 * <li>LocalDate, LocalTime, LocalDateTime. Any specified time zone is
	 * ignored.</li>
	 * </ul></p>
	 * </li>
	 * </ul></p>
	 * 
	 * @param dateTimeString the date/time string
	 * @param clazz the result class
	 * @return the date/time with the specified class
	 * @throws ParseException if the date/time string is invalid, or a
	 * date/time without a time zone is parsed in a time zone where that
	 * date/time does not exist
	 */
	public static <T> T parseDateTime(String dateTimeString,
			Class<T> clazz) throws ParseException {
		// try long
		try {
			long timestamp = Long.parseLong(dateTimeString);
			return dateTimeToType(new DateTime(timestamp), clazz);
		} catch (NumberFormatException ex) {}

		// try yyyy-MM-dd
		DateTimeFormatter parser = DateTimeFormat.forPattern(
				"yyyy-MM-dd");
		LocalDate localDate = null;
		try {
			localDate = parser.parseLocalDate(dateTimeString);
		} catch (Exception ex) {}
		try {
			if (localDate != null)
				return clazz.cast(localDate);
		} catch (ClassCastException ex) {
			throw new ParseException(
					"Pattern yyyy-MM-dd expects result class LocalDate, found: " +
					clazz.getName());
		}
		
		// try HH:mm:ss
		parser = DateTimeFormat.forPattern("HH:mm:ss");
		LocalTime localTime = null;
		try {
			localTime = parser.parseLocalTime(dateTimeString);
		} catch (Exception ex) {}
		try {
			if (localTime != null)
				return clazz.cast(localTime);
		} catch (ClassCastException ex) {
			throw new ParseException(
					"Pattern HH:mm:ss expects result class LocalTime, found: " +
					clazz.getName());
		}

		// try yyyy-MM-dd HH:mm:ss
		parser = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime localDateTime = null;
		try {
			localDateTime = parser.parseLocalDateTime(dateTimeString);
		} catch (Exception ex) {}
		try {
			if (localDateTime != null)
				return clazz.cast(localDateTime);
		} catch (ClassCastException ex) {
			throw new ParseException(
					"Pattern yyyy-MM-dd HH:mm:ss expects result class LocalDateTime, found: " +
					clazz.getName());
		}
		
		// try ISO time
		try {
			parser = ISODateTimeFormat.dateTimeParser().withOffsetParsed();
			return dateTimeToType(parser.parseDateTime(dateTimeString), clazz);
		} catch (Exception ex) {
			throw new ParseException("Invalid date/time string: " +
					dateTimeString + ": " + ex.getMessage(), ex);
		}
	}
	
	/**
	 * Converts a {@link DateTime DateTime} object to an object of the
	 * specified class. It supports the following classes.
	 * 
	 * <p><ul>
	 * <li>long/Long (UNIX timestamp in milliseconds): translated to UTC time,
	 * time zone is lost</li>
	 * <li>{@link Date Date}: translated to UTC time, time zone is lost</li>
	 * <li>{@link Instant Instant}: translated to UTC time, time zone is
	 * lost</li>
	 * <li>{@link Calendar Calendar}</li>
	 * <li>{@link DateTime DateTime}</li>
	 * <li>{@link LocalDate LocalDate}: time and time zone is ignored</li>
	 * <li>{@link LocalTime LocalTime}: date and time zone is ignored</li>
	 * <li>{@link LocalDateTime LocalDateTime}: time zone is ignored</li>
	 * </ul></p>
	 * 
	 * @param dateTime the date/time
	 * @param clazz the result class
	 * @return the date/time with the specified class
	 */
	public static <T> T dateTimeToType(DateTime dateTime, Class<T> clazz) {
		if (clazz == Long.TYPE || clazz == Long.class) {
			@SuppressWarnings("unchecked")
			T result = (T)Long.class.cast(dateTime.getMillis());
			return result;
		} else if (clazz == Date.class) {
			return clazz.cast(dateTime.toDate());
		} else if (clazz == Instant.class) {
			return clazz.cast(dateTime.toInstant());
		} else if (clazz == Calendar.class) {
			return clazz.cast(dateTime.toCalendar(null));
		} else if (clazz == DateTime.class) {
			return clazz.cast(dateTime);
		} else if (clazz == LocalDate.class) {
			return clazz.cast(dateTime.toLocalDate());
		} else if (clazz == LocalTime.class) {
			return clazz.cast(dateTime.toLocalTime());
		} else if (clazz == LocalDateTime.class) {
			return clazz.cast(dateTime.toLocalDateTime());
		} else {
			throw new IllegalArgumentException(
					"Unsupported date/time class: " + clazz.getName());
		}
	}
	
	public static DayPart getDayPart(DateTime dateTime) {
		int hour = dateTime.getHourOfDay();
		if(hour < 6) return DayPart.NIGHT; // 00:00 till 05:59 == NIGHT 
		else if(hour < 12) return DayPart.MORNING; // 06:00 till 11:59 == MORNING
		else if(hour < 18) return DayPart.AFTERNOON; // 12:00 till 17:59 == AFTERNOON
		else return DayPart.EVENING; // 18:00 till 23:59 == EVENING
	}
}
