package eu.ewall.platform.lr.sleepmonitor.service.params;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import eu.ewall.platform.idss.service.model.common.LRSleepDayParameter;
import eu.ewall.platform.lr.sleepmonitor.service.NightSleepData;
import eu.ewall.platform.lr.sleepmonitor.service.SleepParameter;

/**
 * This sleep parameter calculates the time the user got out of bed, in minutes
 * since 0:00 of the day following the night. If the user got out of bed before
 * midnight, this will be less than 0. DST is not taken into account. For
 * example in London time 2015-03-29 03:00:00 is only two hours after 00:00:00,
 * but this parameter will return three hours.
 * 
 * @author Dennis Hofs (RRD)
 */
public class BedOffTimeParameter extends SleepParameter {

	public BedOffTimeParameter() {
		super(LRSleepDayParameter.PARAM_BED_OFF_TIME);
	}

	@Override
	public int calculate(NightSleepData sleepData) {
		DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser();
		DateTime wakeTime = parser.parseDateTime(sleepData.getSleepParameter(
				LRSleepDayParameter.PARAM_BED_OFF_TIME));
		int msInDay = wakeTime.getMillisOfDay();
		LocalDate wakeDate = wakeTime.toLocalDate();
		if (wakeDate.isBefore(sleepData.getDate()))
			msInDay -= 86400000;
		return (int)Math.round(msInDay / 60000.0);
	}
}
