package eu.ewall.platform.lr.sleepmonitor.service.params;

import eu.ewall.platform.idss.service.model.common.LRSleepDayParameter;
import eu.ewall.platform.lr.sleepmonitor.service.NightSleepData;
import eu.ewall.platform.lr.sleepmonitor.service.SleepParameter;

/**
 * This sleep parameter calculates the total amount of time asleep, in minutes.
 * 
 * @author Dennis Hofs (RRD)
 */
public class TotalSleepTimeParameter extends SleepParameter {

	public TotalSleepTimeParameter() {
		super(LRSleepDayParameter.PARAM_TOTAL_SLEEP_TIME);
	}

	@Override
	public int calculate(NightSleepData sleepData) {
		return Integer.parseInt(sleepData.getSleepParameter(
				LRSleepDayParameter.PARAM_TOTAL_SLEEP_TIME));
	}
}
