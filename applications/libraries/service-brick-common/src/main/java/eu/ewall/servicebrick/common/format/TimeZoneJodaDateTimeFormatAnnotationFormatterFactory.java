package eu.ewall.servicebrick.common.format;

import org.joda.time.format.DateTimeFormatter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.joda.JodaDateTimeFormatAnnotationFormatterFactory;

/**
 * Replacement for the default Spring JodaDateTimeFormatAnnotationFormatterFactory that preserves the input time zone
 * offset when parsing DateTime objects.
 */
public class TimeZoneJodaDateTimeFormatAnnotationFormatterFactory extends JodaDateTimeFormatAnnotationFormatterFactory {

	@Override
	protected DateTimeFormatter getFormatter(DateTimeFormat annotation,	Class<?> fieldType) {
		DateTimeFormatter formatter = super.getFormatter(annotation, fieldType);
		return formatter.withOffsetParsed();
	}
	
}
