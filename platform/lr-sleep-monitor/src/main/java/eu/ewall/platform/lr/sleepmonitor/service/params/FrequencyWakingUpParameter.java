package eu.ewall.platform.lr.sleepmonitor.service.params;

import eu.ewall.platform.idss.service.model.common.LRSleepDayParameter;
import eu.ewall.platform.lr.sleepmonitor.service.NightSleepData;
import eu.ewall.platform.lr.sleepmonitor.service.SleepParameter;

/**
 * This sleep parameter calculates the number of times the user was out of bed
 * during the night.
 * 
 * @author Dennis Hofs (RRD)
 */
public class FrequencyWakingUpParameter extends SleepParameter {

	public FrequencyWakingUpParameter() {
		super(LRSleepDayParameter.PARAM_FREQUENCY_WAKING_UP);
	}

	@Override
	public int calculate(NightSleepData sleepData) {
		return Integer.parseInt(sleepData.getSleepParameter(
				LRSleepDayParameter.PARAM_FREQUENCY_WAKING_UP));
	}
}
