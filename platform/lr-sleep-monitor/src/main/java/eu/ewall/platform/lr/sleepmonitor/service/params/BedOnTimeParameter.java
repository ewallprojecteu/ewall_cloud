package eu.ewall.platform.lr.sleepmonitor.service.params;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import eu.ewall.platform.idss.service.model.common.LRSleepDayParameter;
import eu.ewall.platform.lr.sleepmonitor.service.NightSleepData;
import eu.ewall.platform.lr.sleepmonitor.service.SleepParameter;

/**
 * This sleep parameter calculates the time the user went to sleep, in minutes
 * since 0:00 of the day preceding the night. If the user went to sleep after
 * midnight, this will be more than 24 hours. DST is not taken into account.
 * For example in London time 2015-03-29 03:00:00 is only two hours after
 * 00:00:00, but this parameter will return three hours.
 * 
 * @author Dennis Hofs (RRD)
 */
public class BedOnTimeParameter extends SleepParameter {

	public BedOnTimeParameter() {
		super(LRSleepDayParameter.PARAM_BED_ON_TIME);
	}

	@Override
	public int calculate(NightSleepData sleepData) {
		DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser();
		DateTime sleepTime = parser.parseDateTime(sleepData.getSleepParameter(
				LRSleepDayParameter.PARAM_BED_ON_TIME));
		int msInDay = sleepTime.getMillisOfDay();
		LocalDate sleepDate = sleepTime.toLocalDate();
		if (!sleepDate.isBefore(sleepData.getDate()))
			msInDay += 86400000;
		return (int)Math.round(msInDay / 60000.0);
	}
}
