package eu.ewall.platform.lr.sleepmonitor.service.params;

import eu.ewall.platform.idss.service.model.common.LRSleepDayParameter;
import eu.ewall.platform.lr.sleepmonitor.service.NightSleepData;
import eu.ewall.platform.lr.sleepmonitor.service.SleepParameter;

/**
 * This sleep parameter calculates the sleep efficiency in percent.
 * 
 * @author Dennis Hofs (RRD)
 */
public class SleepEfficiencyParameter extends SleepParameter {

	public SleepEfficiencyParameter() {
		super(LRSleepDayParameter.PARAM_SLEEP_EFFICIENCY);
	}

	@Override
	public int calculate(NightSleepData sleepData) {
		return Integer.parseInt(sleepData.getSleepParameter(
				LRSleepDayParameter.PARAM_SLEEP_EFFICIENCY));
	}
}
