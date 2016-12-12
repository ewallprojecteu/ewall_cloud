package eu.ewall.servicebrick.common.mongo;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import eu.ewall.servicebrick.common.time.TimeZoneContext;

/**
 * Time zone aware converter to read a Joda DateTime from a MongoDB Date object. Improves over the standard converter
 * available in Spring Data by being able to create DateTime objects in the time zone returned by the given
 * TimeZoneContext instead of just defaulting to the system default time zone.
 */
@ReadingConverter
public class DateTimeReadConverter implements Converter<Date, DateTime> {

	private TimeZoneContext timeZoneCtx;
	
	/**
	 * @param timeZoneCtx the time zone context to use
	 */
	public DateTimeReadConverter(TimeZoneContext timeZoneCtx) {
		this.timeZoneCtx = timeZoneCtx;
	}
	
	/**
	 * Converts the given Date to a DateTime in the time zone returned by the time zone context. The default system
	 * time zone is used if the context returns a null time zone.
	 */
	@Override
	public DateTime convert(Date source) {
		if (source == null) {
			return null;
		} else {
			DateTimeZone zone = timeZoneCtx.getTimeZone();
			return new DateTime(source.getTime(), zone);
		}
	}

}
